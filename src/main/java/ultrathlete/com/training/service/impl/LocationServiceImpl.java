package ultrathlete.com.training.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
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
        return null;

        /*
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

        return null; */
    }

    /**
     * Creates the http connection. This method can accept self signed certificates for testing.
     * This method is usefull when test certificates are self-signed and production ones are signed
     * In test we can set this value to true
     * While it has to be set to false in production
     * @param withSelfSigned true if a self signed certificate can be accepted
     * @return
     */
    /*
    private CloseableHttpClient getHttpClient(boolean withSelfSigned) {
        return null;

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
        */

    /**
     * Closes an HttpClient once used
     * @param httpClient
     */
    /*
    private void closeConnection(CloseableHttpClient httpClient) {
        try {
            httpClient.close();
        } catch (IOException e) {
            LOGGER.debug("HttpClient close error: " + e.getMessage());
        }
    }*/
}
