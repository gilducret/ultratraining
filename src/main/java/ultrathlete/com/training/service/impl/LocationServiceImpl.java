package ultrathlete.com.training.service.impl;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import ultrathlete.com.training.bo.impl.LocationImpl;
import ultrathlete.com.training.bo.impl.Locations;
import ultrathlete.com.training.bo.impl.ResultLocations;
import ultrathlete.com.training.service.LocationService;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.util.List;

@Service
public class LocationServiceImpl implements LocationService {

    private static final Logger LOGGER = LogManager.getLogger(LocationServiceImpl.class);

    @Override
    public ResultLocations getElevation(Locations locations) {

        String url = "https://api.open-elevation.com/api/v1/lookup";

        try {

            CloseableHttpClient httpClient = getHttpClient(true);

            HttpPost post = new HttpPost(url);
            post.setHeader("Content-Type", "application/json");
            // Get Json request
            ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            String jsonRequest = mapper.writeValueAsString(locations);
            post.setEntity(new StringEntity(jsonRequest, "UTF-8"));

            // Handle the response as a string
            ResponseHandler<String> responseHandler = response -> {
                int status = response.getStatusLine().getStatusCode();
                if (status == 200) {
                    HttpEntity responseEntity = response.getEntity();
                    return responseEntity != null ? EntityUtils.toString(responseEntity) : null;
                } else {
                    LOGGER.error("Elevation method returned " + response.getStatusLine().getStatusCode() +
                            " " + response.getStatusLine().getReasonPhrase());
                    return null;
                }
            };

            try {
                String locationString = httpClient.execute(post, responseHandler);
                // Deserialize JSon response to ResponseBody object
                ObjectMapper responseMapper = new ObjectMapper();
                responseMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
                ResultLocations body = locationString != null ? responseMapper.readValue(locationString, ResultLocations.class)
                        : new ResultLocations();
                return body;
            } catch (IOException e) {
                LOGGER.error("Get elevation failed: " + e.getMessage());
            } finally {
                closeConnection(httpClient);
            }

            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Creates the http connection. This method can accept self signed certificates for testing.
     * This method is usefull when test certificates are self-signed and production ones are signed
     * In test we can set this value to true
     * While it has to be set to false in production
     * @param withSelfSigned true if a self signed certificate can be accepted
     * @return
     */
    private CloseableHttpClient getHttpClient(boolean withSelfSigned) {
        if (withSelfSigned) {   // Accept self-signed certificates
            TrustStrategy acceptingTrustStrategy = (cert, authType) -> true;
            SSLContext sslContext = null;
            try {
                sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
            } catch (Exception e) {
                LOGGER.error("Error when trying to accept any SSL connection: " + e.getMessage());
            }
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext,
                    NoopHostnameVerifier.INSTANCE);

            Registry<ConnectionSocketFactory> socketFactoryRegistry =
                    RegistryBuilder.<ConnectionSocketFactory> create()
                            .register("https", sslsf)
                            .register("http", new PlainConnectionSocketFactory())
                            .build();

            BasicHttpClientConnectionManager connectionManager =
                    new BasicHttpClientConnectionManager(socketFactoryRegistry);
            CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslsf)
                    .setConnectionManager(connectionManager).build();
            return httpClient;
        } else {    // Signed certificates
            return HttpClients.createDefault();
        }
    }

    /**
     * Closes an HttpClient once used
     * @param httpClient
     */
    private void closeConnection(CloseableHttpClient httpClient) {
        try {
            httpClient.close();
        } catch (IOException e) {
            LOGGER.debug("HttpClient close error: " + e.getMessage());
        }
    }
}
