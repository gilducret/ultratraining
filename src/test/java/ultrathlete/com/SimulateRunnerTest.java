package ultrathlete.com;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * SimulateRunnerTest
 *
 * Simulates a runner on the CCC 2023 route.
 * Uses real GPS points extracted from the GPX file, one per kilometre.
 *
 * Broadcasts position to the follow API every DELAY_MS milliseconds.
 * Open http://localhost:8080/follow/5 to watch the follower page update.
 *
 * Also injects GPS into the Android emulator via adb so the mobile
 * tracking screen updates in real time.
 *
 * Usage:
 *   1. Start ultratraining (port 8082) and webapp (port 8080)
 *   2. Start Android emulator with your app running
 *   3. Make sure adb is in your PATH
 *   4. Set SESSION_ID to your CCC planned race session ID
 *   5. Run this test
 */
public class SimulateRunnerTest {

    // ── Configuration ─────────────────────────────────────────────
    private static final int    SESSION_ID = 5;
    private static final String BASE_URL   = "http://localhost:8082";

    // Delay between position updates in milliseconds
    // 10000 = 10 seconds between each km point
    private static final int    DELAY_MS   = 10_000;

    // Whether to inject GPS into Android emulator via adb
    private static final boolean INJECT_ADB = true;

    // ── GPS points every 1km along CCC 2023 route ─────────────────
    // Format: { latitude, longitude, altitude, distanceFromStartM }
    private static final double[][] GPS_POINTS = {
            {45.79284, 6.97162, 1234, 0},       // 0km
            {45.79155, 6.97244, 1238, 1041},    // 1km
            {45.79913, 6.97803, 1353, 2016},    // 2km
            {45.80280, 6.98426, 1472, 3001},    // 3km
            {45.80714, 6.99501, 1558, 4038},    // 4km
            {45.80764, 7.00147, 1718, 5003},    // 5km
            {45.80813, 7.00735, 1983, 6005},    // 6km
            {45.81224, 7.01602, 2010, 7001},    // 7km
            {45.81762, 7.01904, 2310, 8005},    // 8km
            {45.82356, 7.01841, 2546, 9007},    // 9km
            {45.82356, 7.00701, 2375, 10010},   // 10km
            {45.81989, 6.99558, 2328, 11001},   // 11km
            {45.81503, 6.98550, 2266, 12000},   // 12km
            {45.80940, 6.97887, 2006, 13004},   // 13km
            {45.81614, 6.97836, 1998, 14013},   // 14km
            {45.82269, 6.98650, 1961, 15001},   // 15km
            {45.82683, 6.99763, 1997, 16010},   // 16km
            {45.83253, 7.00673, 1941, 17015},   // 17km
            {45.83641, 7.01703, 1995, 18012},   // 18km
            {45.84043, 7.02246, 1972, 19007},   // 19km
            {45.84737, 7.03009, 1956, 20015},   // 20km
            {45.84867, 7.03726, 2024, 21043},   // 21km
            {45.85606, 7.04080, 2055, 22008},   // 22km
            {45.86228, 7.04923, 2047, 23008},   // 23km
            {45.86731, 7.05724, 2034, 24003},   // 24km
            {45.86999, 7.05581, 1913, 25014},   // 25km
            {45.87286, 7.05544, 1807, 26025},   // 26km
            {45.87895, 7.06227, 1966, 27002},   // 27km
            {45.88405, 7.06814, 2095, 28006},   // 28km
            {45.88273, 7.07573, 2325, 29006},   // 29km
            {45.88949, 7.07812, 2517, 30005},   // 30km
            {45.89197, 7.08733, 2410, 31013},   // 31km
            {45.89392, 7.09918, 2287, 32011},   // 32km
            {45.89778, 7.10993, 2156, 33008},   // 33km
            {45.90260, 7.11288, 2025, 34011},   // 34km
            {45.90820, 7.10499, 1984, 35010},   // 35km
            {45.91117, 7.09625, 1905, 36009},   // 36km
            {45.91551, 7.10398, 1686, 37027},   // 37km
            {45.92259, 7.09955, 1630, 38014},   // 38km
            {45.93067, 7.09711, 1602, 39066},   // 39km
            {45.93729, 7.09528, 1574, 40016},   // 40km
            {45.94539, 7.09796, 1528, 41008},   // 41km
            {45.95322, 7.10256, 1450, 42007},   // 42km
            {45.96070, 7.10864, 1371, 43018},   // 43km
            {45.96930, 7.10925, 1395, 44008},   // 44km
            {45.97699, 7.11336, 1300, 45014},   // 45km
            {45.97914, 7.12109, 1254, 46005},   // 46km
            {45.98651, 7.12152, 1168, 47008},   // 47km
            {45.99252, 7.12676, 1110, 48002},   // 48km
            {46.00097, 7.12785, 1100, 49008},   // 49km
            {46.00811, 7.12316, 1103, 50011},   // 50km
            {46.01318, 7.12000, 1236, 51001},   // 51km
            {46.01874, 7.11817, 1403, 52017},   // 52km
            {46.02594, 7.12158, 1466, 53013},   // 53km
            {46.03070, 7.11438, 1473, 54016},   // 54km
            {46.03363, 7.10328, 1506, 55021},   // 55km
            {46.03844, 7.09502, 1433, 56041},   // 56km
            {46.04477, 7.08741, 1410, 57015},   // 57km
            {46.04960, 7.07969, 1353, 58004},   // 58km
            {46.04998, 7.06999, 1376, 59025},   // 59km
            {46.04505, 7.06111, 1524, 60012},   // 60km
            {46.04786, 7.05730, 1656, 61001},   // 61km
            {46.05297, 7.05611, 1873, 62011},   // 62km
            {46.05522, 7.04744, 2004, 63001},   // 63km
            {46.05763, 7.03762, 1976, 64001},   // 64km
            {46.05824, 7.02880, 1843, 65003},   // 65km
            {46.05689, 7.01904, 1684, 66004},   // 66km
            {46.05778, 7.00748, 1544, 67004},   // 67km
            {46.05337, 7.00087, 1531, 68030},   // 68km
            {46.05691, 6.99584, 1297, 69012},   // 69km
            {46.05112, 6.99421, 1307, 70080},   // 70km
            {46.04540, 6.99139, 1373, 71002},   // 71km
            {46.04586, 6.98491, 1702, 72004},   // 72km
            {46.04921, 6.97892, 1970, 73010},   // 73km
            {46.04345, 6.97122, 2011, 74007},   // 74km
            {46.04003, 6.96782, 1951, 75007},   // 75km
            {46.04238, 6.96283, 1790, 76008},   // 76km
            {46.03854, 6.95106, 1783, 77098},   // 77km
            {46.03778, 6.94501, 1652, 78008},   // 78km
            {46.03158, 6.93670, 1390, 79005},   // 79km
            {46.03512, 6.93502, 1286, 80002},   // 80km
            {46.02826, 6.92773, 1279, 81018},   // 81km
            {46.02046, 6.92211, 1331, 82010},   // 82km
            {46.01249, 6.92365, 1386, 83003},   // 83km
            {46.00446, 6.92419, 1454, 84002},   // 84km
            {46.00069, 6.91795, 1713, 85008},   // 85km
            {45.99730, 6.91366, 1979, 86004},   // 86km
            {45.98931, 6.91219, 2046, 87006},   // 87km
            {45.98205, 6.90615, 2102, 88001},   // 88km
            {45.97664, 6.89900, 1939, 89014},   // 89km
            {45.96924, 6.89404, 1920, 90001},   // 90km
            {45.96280, 6.88687, 1839, 91006},   // 91km
            {45.95656, 6.88246, 1761, 92001},   // 92km
            {45.95492, 6.88435, 1614, 93002},   // 93km
            {45.95131, 6.88157, 1409, 94000},   // 94km
            {45.94460, 6.87716, 1322, 95006},   // 95km
            {45.93736, 6.87223, 1191, 96009},   // 96km
            {45.93060, 6.87245, 1041, 97006},   // 97km
            {45.92322, 6.87036, 1029, 98007},   // 98km
    };

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final SimpleDateFormat timeFmt  =
            new SimpleDateFormat("HH:mm:ss");

    @Test
    public void simulateRunner() throws Exception {

        System.out.println("═══════════════════════════════════════════════════");
        System.out.println("  ULTRATHLETE — CCC 2023 Runner Simulation");
        System.out.println("  Session ID : " + SESSION_ID);
        System.out.println("  Points     : " + GPS_POINTS.length + " (every 1km)");
        System.out.println("  Interval   : " + DELAY_MS/1000 + " seconds per km");
        System.out.println("  Follow page: http://localhost:8080/follow/"
                + SESSION_ID);
        System.out.println("  ADB inject : " + INJECT_ADB);
        System.out.println("═══════════════════════════════════════════════════");
        System.out.println();

        long raceStartMs = System.currentTimeMillis();

        for (int i = 0; i < GPS_POINTS.length; i++) {
            double lat     = GPS_POINTS[i][0];
            double lon     = GPS_POINTS[i][1];
            double alt     = GPS_POINTS[i][2];
            double distM   = GPS_POINTS[i][3];
            int    elapsed = (int) ((System.currentTimeMillis() - raceStartMs) / 1000);

            // ── 1. Broadcast to follow API ─────────────────────────
            String payload = String.format(
                    java.util.Locale.US,
                    "{\"latitude\":%.5f,\"longitude\":%.5f," +
                            "\"altitude\":%.0f,\"distanceFromStartM\":%.0f," +
                            "\"elapsedSeconds\":%d}",
                    lat, lon, alt, distM, elapsed);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> request = new HttpEntity<>(payload, headers);

            try {
                restTemplate.postForEntity(
                        BASE_URL + "/api/follow/" + SESSION_ID + "/position",
                        request, Void.class);
            } catch (Exception e) {
                System.out.println("  ✗ Broadcast failed: " + e.getMessage());
            }

            // ── 2. Inject GPS into Android emulator ────────────────
            if (INJECT_ADB) {
                try {
                    // adb emu geo fix <lon> <lat> <alt>
                    String cmd = String.format(
                            java.util.Locale.US,
                            "adb emu geo fix %.5f %.5f %.0f",
                            lon, lat, alt);
                    Runtime.getRuntime().exec(cmd);
                } catch (Exception e) {
                    System.out.println("  ✗ ADB inject failed: " + e.getMessage());
                }
            }

            // ── 3. Fetch ETAs and print ────────────────────────────
            System.out.printf(java.util.Locale.US,
                    "── km %2d ──────────────────────────────────────────%n",
                    (int)(distM / 1000));
            System.out.printf(java.util.Locale.US,
                    "   Position : lat=%.5f lon=%.5f alt=%.0fm%n",
                    lat, lon, alt);
            System.out.printf(java.util.Locale.US,
                    "   Distance : %.1f km  Elapsed: %s%n",
                    distM / 1000, formatDuration(elapsed));

            try {
                ResponseEntity<String> posResponse = restTemplate.getForEntity(
                        BASE_URL + "/api/follow/" + SESSION_ID + "/position",
                        String.class);

                JsonNode update = objectMapper.readTree(posResponse.getBody());
                double serverDist = update.path("distanceFromStartM")
                        .asDouble(0);
                System.out.printf(java.util.Locale.US,
                        "   Server dist: %.1f km%n", serverDist / 1000);

                JsonNode etas = update.path("checkpointEtas");
                if (etas.isArray() && etas.size() > 0) {
                    System.out.println("   Checkpoints:");
                    for (JsonNode cp : etas) {
                        String  name   = cp.path("name").asText();
                        boolean passed = cp.path("passed").asBoolean();
                        double  cpDist = cp.path("distanceFromStartM").asDouble();
                        long    etaMs  = cp.path("etaTimestamp").asLong(0);

                        if (passed) {
                            System.out.printf(
                                    "     ✓ %-28s %.1f km%n",
                                    name, cpDist / 1000);
                        } else if (etaMs > 0) {
                            String etaTime  = timeFmt.format(new Date(etaMs));
                            long   secsLeft = (etaMs - System.currentTimeMillis()) / 1000;
                            double kmLeft   = (cpDist - serverDist) / 1000;
                            System.out.printf(
                                    "     → %-28s %.1f km  ETA: %s  in %s  (%.1f km left)%n",
                                    name, cpDist / 1000, etaTime,
                                    formatDuration((int) secsLeft), kmLeft);
                        }
                    }
                }

            } catch (Exception e) {
                System.out.println("   ✗ Could not fetch ETAs: " + e.getMessage());
            }

            System.out.println();

            // Wait before next point (skip wait after last point)
            if (i < GPS_POINTS.length - 1) {
                Thread.sleep(DELAY_MS);
            }
        }

        System.out.println("═══════════════════════════════════════════════════");
        System.out.println("  Simulation complete — 98.1 km covered");
        System.out.println("═══════════════════════════════════════════════════");
    }

    private String formatDuration(int totalSeconds) {
        if (totalSeconds <= 0) return "0:00:00";
        int h = totalSeconds / 3600;
        int m = (totalSeconds % 3600) / 60;
        int s = totalSeconds % 60;
        return String.format("%02d:%02d:%02d", h, m, s);
    }
}