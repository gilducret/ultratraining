package ultrathlete.com;

import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import io.jenetics.jpx.GPX;
import io.jenetics.jpx.Track;
import io.jenetics.jpx.TrackSegment;
import io.jenetics.jpx.WayPoint;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SimulateRunnerTest {

    // ── Configuration ─────────────────────────────────────────────
    private static final int    SESSION_ID = 5;
    private static final String GPX_FILE   =
            "C:\\Users\\gille\\Downloads\\CCC_2023.gpx";
    private static final String BASE_URL   =
            "http://localhost:8081";
    private static final int    STEP       = 50;  // every Nth GPX point
    private static final int    DELAY_MS   = 2000; // 2 seconds between broadcasts

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final SimpleDateFormat timeFmt  =
            new SimpleDateFormat("HH:mm:ss");

    // ── Test ──────────────────────────────────────────────────────
    @Test
    public void simulateRunner() throws Exception {

        // Read GPX
        GPX gpx = GPX.read(Paths.get(GPX_FILE));
        List<WayPoint> points = new ArrayList<>();
        for (Track track : gpx.getTracks()) {
            for (TrackSegment segment : track.getSegments()) {
                points.addAll(segment.getPoints());
            }
        }

        System.out.println("═══════════════════════════════════════════");
        System.out.println("  ULTRATHLETE — Runner Simulation");
        System.out.println("  Session: " + SESSION_ID);
        System.out.println("  GPX points: " + points.size()
                + " (broadcasting every " + STEP + ")");
        System.out.println("  Follow page: http://localhost:8080/follow/"
                + SESSION_ID);
        System.out.println("═══════════════════════════════════════════");

        double cumDistM    = 0;
        int    elapsedSecs = 0;
        WayPoint prev      = null;
        int broadcastCount = 0;

        for (int i = 0; i < points.size(); i += STEP) {
            WayPoint wp = points.get(i);

            double lat = wp.getLatitude().doubleValue();
            double lon = wp.getLongitude().doubleValue();
            double alt = wp.getElevation()
                    .map(e -> e.doubleValue()).orElse(0.0);

            if (prev != null) {
                cumDistM += haversine(
                        prev.getLatitude().doubleValue(),
                        prev.getLongitude().doubleValue(),
                        lat, lon);
            }

            // Simulate elapsed at 6 km/h average
            elapsedSecs = (int) (cumDistM / 1000.0 * 600);

            broadcastCount++;

            // ── 1. Send position ──────────────────────────────────
            String payload = String.format(
                    java.util.Locale.US,  // ← force US locale for dots not commas
                    "{\"latitude\":%f,\"longitude\":%f," +
                            "\"altitude\":%f,\"distanceFromStartM\":%f," +
                            "\"elapsedSeconds\":%d}",
                    lat, lon, alt, cumDistM, elapsedSecs);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> request = new HttpEntity<>(payload, headers);

            try {
                restTemplate.postForEntity(
                        BASE_URL + "/api/follow/" + SESSION_ID + "/position",
                        request, Void.class);
            } catch (Exception e) {
                System.out.println("  ✗ Broadcast failed: " + e.getMessage());
                prev = wp;
                continue;
            }

            // ── 2. Fetch ETAs and print ───────────────────────────
            System.out.printf("%n── Broadcast #%d ───────────────────────%n",
                    broadcastCount);
            System.out.printf(java.util.Locale.US,
                    "   Position : lat=%.4f lon=%.4f alt=%.0fm%n",
                    lat, lon, alt);
            System.out.printf(java.util.Locale.US,
                    "   Distance : %.2f km from start%n",
                    cumDistM / 1000);
            System.out.printf(java.util.Locale.US,
                    "   Elapsed  : %s%n",
                    formatDuration(elapsedSecs));

            try {
                ResponseEntity<String> posResponse = restTemplate.getForEntity(
                        BASE_URL + "/api/follow/" + SESSION_ID + "/position",
                        String.class);

                JsonNode update = objectMapper.readTree(
                        posResponse.getBody());

                // Print server-calculated distance
                double serverDist = update.path("distanceFromStartM")
                        .asDouble(0);
                System.out.printf(java.util.Locale.US,
                        "   Server dist: %.2f km (calc from GPS)%n",
                        serverDist / 1000);

                // Print checkpoint ETAs
                JsonNode etas = update.path("checkpointEtas");
                if (etas.isArray() && etas.size() > 0) {
                    System.out.println("   Checkpoint ETAs:");
                    for (JsonNode cp : etas) {
                        String name    = cp.path("name").asText();
                        boolean passed = cp.path("passed").asBoolean();
                        double cpDist  = cp.path("distanceFromStartM")
                                .asDouble();
                        long etaMs     = cp.path("etaTimestamp").asLong(0);

                        if (passed) {
                            System.out.printf(java.util.Locale.US,
                                    "     ✓ %-25s %.1f km  PASSED%n",
                                    name, cpDist / 1000);
                        } else if (etaMs > 0) {
                            String etaTime = timeFmt.format(new Date(etaMs));
                            long secsLeft  = (etaMs - System.currentTimeMillis())
                                    / 1000;
                            double kmLeft  = (cpDist - serverDist) / 1000;
                            System.out.printf(java.util.Locale.US,
                                    "     → %-25s %.1f km  ETA: %s  "
                                            + "(in %s, %.1f km left)%n",
                                    name, cpDist / 1000,
                                    etaTime,
                                    formatDuration((int) secsLeft),
                                    kmLeft);
                        }
                    }
                } else {
                    System.out.println(
                            "   No ETAs returned (race not started?)");
                }

            } catch (Exception e) {
                System.out.println("   ✗ Could not fetch ETAs: "
                        + e.getMessage());
            }

            prev = wp;
            Thread.sleep(DELAY_MS);
        }

        System.out.println("\n═══════════════════════════════════════════");
        System.out.println("  Simulation complete. "
                + broadcastCount + " positions sent.");
        System.out.printf(java.util.Locale.US,"  Total distance: %.1f km%n", cumDistM / 1000);
        System.out.printf(java.util.Locale.US,"  Simulated race time: %s%n",
                formatDuration(elapsedSecs));
        System.out.println("═══════════════════════════════════════════");
    }

    // ── Helpers ───────────────────────────────────────────────────

    private double haversine(double lat1, double lon1,
                             double lat2, double lon2) {
        final double R = 6_371_000;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon/2) * Math.sin(dLon/2);
        return R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
    }

    private String formatDuration(int totalSeconds) {
        if (totalSeconds <= 0) return "0:00:00";
        int h = totalSeconds / 3600;
        int m = (totalSeconds % 3600) / 60;
        int s = totalSeconds % 60;
        return String.format("%02d:%02d:%02d", h, m, s);
    }
}