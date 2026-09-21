package ultrathlete.com.training;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ultrathlete.com.training.bo.PredictedSession;
import ultrathlete.com.training.bo.impl.*;
import ultrathlete.com.training.service.model.Models;
import ultrathlete.com.training.service.FileStorageService;
import ultrathlete.com.training.service.LocationService;
import ultrathlete.com.training.service.PredictionService;
import ultrathlete.com.training.service.UserService;
import ultrathlete.com.training.service.impl.ForecastManager;
import ultrathlete.com.training.service.model.PredictionModel;
import ultrathlete.com.training.util.TimeUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@SpringBootTest
@ActiveProfiles("test")
class TrainingApplicationTests {
    @Autowired
    private LocationService locationService;
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private UserService userService;
    @Autowired
    private ForecastManager forecastManager;
    @Autowired
    private PredictionService predictionService;

    @Test
    void contextLoads() {
    }

    @Test
    public void testElevation() {

        Locations locations = new Locations();
        locations.addLocation(new LocationImpl(10.0, 515));
        locations.addLocation(new LocationImpl(46.2792817,6.4261949));
        locations.addLocation(new LocationImpl(41.161758, -8.583933));

        ResultLocations response = locationService.getElevation(locations);
        System.out.println(response);
    }

    @Test
    public void calculateRegressions() {
        final String[] gpxs = {

               "C:\\Users\\gille\\Downloads\\pj1.gpx",
                "C:\\Users\\gille\\Downloads\\pj2.gpx",
                "C:\\Users\\gille\\Downloads\\pj3.gpx",
                "C:\\Users\\gille\\Downloads\\pj4.gpx",
                "C:\\Users\\gille\\Downloads\\pj5.gpx",
                "C:\\Users\\gille\\Downloads\\pj6.gpx",
                "C:\\Users\\gille\\Downloads\\plainejouxcap.gpx",
                "C:\\Users\\gille\\Downloads\\m1.gpx",
                "C:\\Users\\gille\\Downloads\\m2.gpx",
                "C:\\Users\\gille\\Downloads\\m3.gpx",
                "C:\\Users\\gille\\Downloads\\m4.gpx",
                "C:\\Users\\gille\\Downloads\\m5.gpx",
                "C:\\Users\\gille\\Downloads\\m6.gpx",
                "C:\\Users\\gille\\Downloads\\m7.gpx",
                "C:\\Users\\gille\\Downloads\\cretes.gpx",
                "C:\\Users\\gille\\Downloads\\20220611_Mont_Cretes.gpx",
                "C:\\Users\\gille\\Downloads\\20220618-Montagne-Cova-Cretes.gpx",
                "C:\\Users\\gille\\Downloads\\20210821_belier_montagne_7342591908.gpx",
                "C:\\Users\\gille\\Downloads\\challenge-lac-montagne-s.gpx",
                "C:\\Users\\gille\\Downloads\\montagne-leforchat-lescretes-20220724.gpx",
                "C:\\Users\\gille\\Downloads\\20220827_Montagne_ColdeCou_9489574596.gpx",

                "C:\\Users\\gille\\Downloads\\plaine1.gpx",
                "C:\\Users\\gille\\Downloads\\plaine2.gpx",
                "C:\\Users\\gille\\Downloads\\plaine3.gpx",
                "C:\\Users\\gille\\Downloads\\plaine4.gpx",
                "C:\\Users\\gille\\Downloads\\plaine5.gpx",
                "C:\\Users\\gille\\Downloads\\plaine6.gpx",
                "C:\\Users\\gille\\Downloads\\plaine7.gpx",

            "C:\\Users\\gille\\Downloads\\ultra.gpx",
                "C:\\Users\\gille\\Downloads\\orcier.gpx",
                "C:\\Users\\gille\\Downloads\\plainebalar.gpx",
                "C:\\Users\\gille\\Downloads\\plainebalbrens.gpx",
                "C:\\Users\\gille\\Downloads\\long1.gpx",
                "C:\\Users\\gille\\Downloads\\allinges.gpx",
                "C:\\Users\\gille\\Downloads\\cfer.gpx",
                "C:\\Users\\gille\\Downloads\\20220611_Mont_Cretes.gpx",
                "C:\\Users\\gille\\Downloads\\20220528_Plaine.gpx",
                "C:\\Users\\gille\\Downloads\\20220423_MontForchat.gpx",
                "C:\\Users\\gille\\Downloads\\20220409_Plaine_Allinges.gpx",
                "C:\\Users\\gille\\Downloads\\20220528_Plaine.gpx",      // Ballaison
                "C:\\Users\\gille\\Downloads\\20220626_sortiedraillant_hilly_bonnesmontees_9090691733.gpx",
                "C:\\Users\\gille\\Downloads\\lestempliers2022_activity_9845878137.gpx",

                "C:\\Users\\gille\\Downloads\\activity_20231.gpx",
                "C:\\Users\\gille\\Downloads\\activity_20232.gpx",
                "C:\\Users\\gille\\Downloads\\activity_20233.gpx",
                "C:\\Users\\gille\\Downloads\\activity_20234.gpx",
                "C:\\Users\\gille\\Downloads\\activity_20235.gpx",
                "C:\\Users\\gille\\Downloads\\activity_20236.gpx",
                "C:\\Users\\gille\\Downloads\\activity_20237.gpx",
                "C:\\Users\\gille\\Downloads\\activity_20238.gpx",

                "C:\\Users\\gille\\Downloads\\cou-cova.gpx",
                "C:\\Users\\gille\\Downloads\\cova-cou.gpx",
                "C:\\Users\\gille\\Downloads\\forchat20260829.gpx"

/*                "C:\\Users\\gille\\Downloads\\ross1.gpx",
                "C:\\Users\\gille\\Downloads\\ross2.gpx",
                "C:\\Users\\gille\\Downloads\\ross3.gpx",
                "C:\\Users\\gille\\Downloads\\ross4.gpx",
                "C:\\Users\\gille\\Downloads\\ross5.gpx",
//                "C:\\Users\\gille\\Downloads\\ross100m.gpx",
                "C:\\Users\\gille\\Downloads\\rossCCC_2023.gpx"*/

//                "C:\\Users\\gille\\Downloads\\pam\\2019-11-30.gpx",
//                "C:\\Users\\gille\\Downloads\\pam\\2022-03-19.gpx",
//                "C:\\Users\\gille\\Downloads\\pam\\2023-02-20.gpx",
//                "C:\\Users\\gille\\Downloads\\pam\\2023-03-05.gpx",
//                "C:\\Users\\gille\\Downloads\\pam\\2023-04-23.gpx",
//                "C:\\Users\\gille\\Downloads\\pam\\2023-05-01.gpx",
//                "C:\\Users\\gille\\Downloads\\pam\\2023-05-29.gpx",
//                "C:\\Users\\gille\\Downloads\\pam\\2023-06-03-80k.gpx",
//                "C:\\Users\\gille\\Downloads\\pam\\2023-06-24.gpx",
//                "C:\\Users\\gille\\Downloads\\pam\\2023-06-30.gpx",
//                "C:\\Users\\gille\\Downloads\\pam\\2023-08-13.gpx",
//                "C:\\Users\\gille\\Downloads\\pam\\2023-08-26.gpx",
//                "C:\\Users\\gille\\Downloads\\pam\\2023-08-30.gpx",
//                "C:\\Users\\gille\\Downloads\\pam\\2023-09-08-100k.gpx",
//                "C:\\Users\\gille\\Downloads\\pam\\2023-09-24.gpx"
        };

        List<SessionFileImpl> files = new ArrayList<>();
        for (String fileName : gpxs) {
            SessionFileImpl sess = new SessionFileImpl();
            try {
                sess.setName(fileName);
                sess.setData(Files.readAllBytes(Paths.get(fileName)));
                files.add(sess);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        forecastManager.analyseData(files);
    }

    @Test
    public void testPredictRun() {
        final String[] gpxs = {
//                "C:\\Users\\gille\\Downloads\\hoka-les-templiers-2025-grand-trail-des-templiers.gpx",
//                "C:\\Users\\gille\\Downloads\\CCC_2023.gpx",
//                "C:\\Users\\gille\\Downloads\\lestempliers.gpx",
//                "C:\\Users\\gille\\Downloads\\templiers2025-endurancetrail-abandon.gpx"
//                "C:\\Users\\gille\\Downloads\\hoka-les-templiers-2025-endurance-trail.gpx",
//                "C:\\Users\\gille\\Downloads\\trail-ete-des-gets-2025-trail-bleu.gpx"
//                "C:\\Users\\gille\\Downloads\\saintjacquesofficiel.gpx",
//                               "C:\\Users\\gille\\Downloads\\wild50k_parcours_officiel_e741372209.gpx",
//                "C:\\Users\\gille\\Downloads\\grand_trail_des_templiers_171094.gpx",
//                "C:\\Users\\gille\\Downloads\\lestempliers.gpx"
//                "C:\\Users\\gille\\Downloads\\trace-du-trail-ccc-courmayeur-champex-chamonix.gpx",
//               "C:\\Users\\gille\\Downloads\\CCC_2023.gpx",
//                "C:\\Users\\gille\\Downloads\\West_Highland_Way_Race_2019.gpx"
//                "C:\\Users\\gille\\Downloads\\CCC_2023_bis.gpx"
//                "C:\\Users\\gille\\Downloads\\rossCCC_2023.gpx"
//                "C:\\Users\\gille\\Downloads\\ross100m.gpx"
//                "C:\\Users\\gille\\Downloads\\pam\\2023-06-03-80k.gpx",
//                "C:\\Users\\gille\\Downloads\\pam\\2023-09-08-100k.gpx",
//                "C:\\Users\\gille\\Downloads\\pam\\DiagonaleDesFous2023.gpx"
//                "C:\\Users\\gille\\Downloads\\CharmeyTrail_54km.gpx",
//                "C:\\Users\\gille\\Downloads\\charmey_activity_23639092364.gpx",
                "C:\\Users\\gille\\Downloads\\WILD_70_K_officiel_06edfafe0d.gpx",
                "C:\\Users\\gille\\Downloads\\100k_Puglia_2026_501bcc7259.gpx"

        };

        for (String file : gpxs) {
            UserImpl user = (UserImpl) userService.getUser(1);
            SessionFileImpl sess = new SessionFileImpl();
            try {
                sess.setName(file);
                sess.setData(Files.readAllBytes(Paths.get(file)));
                List<PredictionModel> models = forecastManager.predictRun(sess, user);

                for (PredictionModel model : models) {
                    System.out.println(model.getRanSegment().getTotalDistance() + "\t("
                            + model.getRanSegment().getElevationGain() + ")\t" + "\t["
                            + model.getRanSegment().getElevationLoss() + "]\t"
                            + TimeUtil.MakeReadable(model.getRanSegment().getTotalTime())  + "\t"
                            + "\t" + TimeUtil.MakeReadable((long) model.getModel2())
                            + "\t" + TimeUtil.MakeReadable((long) model.getModel3())
                            + "\t" + TimeUtil.MakeReadable((long) model.getModel4())
                    );
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

//    @Test
//    public void testAddPredictedSession() {
//        PredictedSession session = new PredictedSession();
//        session.setName("CCC");
//        try {
//            session.setData(Files.readAllBytes(Paths.get("C:\\Users\\gille\\Downloads\\CCC_2023_bis.gpx")));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        session.setDate(new Date());
//        UserImpl user = (UserImpl) userService.getUser(1);
//        PredictedSession pSession = predictionService.addPredictedSession(user, session);
//    }

    @Test
    public void testFileStorage() {
        final String[] gpxs = {

                "C:\\Users\\gille\\Downloads\\pj1.gpx",
                "C:\\Users\\gille\\Downloads\\pj2.gpx",
                "C:\\Users\\gille\\Downloads\\pj3.gpx",
                "C:\\Users\\gille\\Downloads\\pj4.gpx"
        };

        Collection<SessionFileImpl> files = generateFiles(gpxs);
        UserImpl user = (UserImpl) userService.getUser(1);
        fileStorageService.save(files.toArray(new SessionFileImpl[files.size()]), user);
    }

    @Test
    public void TestAddReferenceData() {
        final String[] gpxs = {
               "C:\\Users\\gille\\Downloads\\pj1.gpx",
                       "C:\\Users\\gille\\Downloads\\pj2.gpx",
                       "C:\\Users\\gille\\Downloads\\pj3.gpx",
                       "C:\\Users\\gille\\Downloads\\pj4.gpx",
                       "C:\\Users\\gille\\Downloads\\pj5.gpx",
                       "C:\\Users\\gille\\Downloads\\pj6.gpx",
                                   "C:\\Users\\gille\\Downloads\\plainejouxcap.gpx",
                                 "C:\\Users\\gille\\Downloads\\m1.gpx",
                                  "C:\\Users\\gille\\Downloads\\m2.gpx",
                                  "C:\\Users\\gille\\Downloads\\m3.gpx",
                                  "C:\\Users\\gille\\Downloads\\m4.gpx",
                                  "C:\\Users\\gille\\Downloads\\m5.gpx",
                                  "C:\\Users\\gille\\Downloads\\m6.gpx",
                                  "C:\\Users\\gille\\Downloads\\m7.gpx"
               /*                   "C:\\Users\\gille\\Downloads\\cretes.gpx",
                                  "C:\\Users\\gille\\Downloads\\20220611_Mont_Cretes.gpx",
                                  "C:\\Users\\gille\\Downloads\\20220618-Montagne-Cova-Cretes.gpx",
                                  "C:\\Users\\gille\\Downloads\\20210821_belier_montagne_7342591908.gpx",
                                  "C:\\Users\\gille\\Downloads\\challenge-lac-montagne-s.gpx",
                                  "C:\\Users\\gille\\Downloads\\montagne-leforchat-lescretes-20220724.gpx",
                                  "C:\\Users\\gille\\Downloads\\20220827_Montagne_ColdeCou_9489574596.gpx",

                                  "C:\\Users\\gille\\Downloads\\plaine1.gpx",
                                  "C:\\Users\\gille\\Downloads\\plaine2.gpx",y

                                  "C:\\Users\\gille\\Downloads\\plaine3.gpx",
                                  "C:\\Users\\gille\\Downloads\\plaine4.gpx",
                                  "C:\\Users\\gille\\Downloads\\plaine5.gpx",
                                  "C:\\Users\\gille\\Downloads\\plaine6.gpx",
                                  "C:\\Users\\gille\\Downloads\\plaine7.gpx",

                                  "C:\\Users\\gille\\Downloads\\ultra.gpx",
                                  "C:\\Users\\gille\\Downloads\\orcier.gpx",
                                  "C:\\Users\\gille\\Downloads\\plainebalar.gpx",
                                  "C:\\Users\\gille\\Downloads\\plainebalbrens.gpx",
                                  "C:\\Users\\gille\\Downloads\\long1.gpx",
                                  "C:\\Users\\gille\\Downloads\\allinges.gpx",
                                  "C:\\Users\\gille\\Downloads\\cfer.gpx",
                                  "C:\\Users\\gille\\Downloads\\20220611_Mont_Cretes.gpx",
                                  "C:\\Users\\gille\\Downloads\\20220528_Plaine.gpx",
                                  "C:\\Users\\gille\\Downloads\\20220423_MontForchat.gpx",
                                  "C:\\Users\\gille\\Downloads\\20220409_Plaine_Allinges.gpx",
                                  "C:\\Users\\gille\\Downloads\\20220528_Plaine.gpx",      // Ballaison
                                  "C:\\Users\\gille\\Downloads\\20220626_sortiedraillant_hilly_bonnesmontees_9090691733.gpx",
                                  "C:\\Users\\gille\\Downloads\\lestempliers2022_activity_9845878137.gpx",

                                  "C:\\Users\\gille\\Downloads\\activity_20231.gpx",
                                  "C:\\Users\\gille\\Downloads\\activity_20232.gpx",
                                  "C:\\Users\\gille\\Downloads\\activity_20233.gpx",
                                  "C:\\Users\\gille\\Downloads\\activity_20234.gpx",
                                  "C:\\Users\\gille\\Downloads\\activity_20235.gpx",
                                  "C:\\Users\\gille\\Downloads\\activity_20236.gpx",
                                  "C:\\Users\\gille\\Downloads\\activity_20237.gpx",
                                  "C:\\Users\\gille\\Downloads\\activity_20238.gpx"

                                  "C:\\Users\\gille\\Downloads\\moises1.gpx",
                                  "C:\\Users\\gille\\Downloads\\moises2.gpx",
                                  "C:\\Users\\gille\\Downloads\\moises3.gpx"
                                  "C:\\Users\\gille\\Downloads\\activity_20251.gpx", */
                //"C:\\Users\\gille\\Downloads\\activity_20252.gpx"

        };
        Collection<SessionFileImpl> files = generateFiles(gpxs);
        UserImpl user = (UserImpl) userService.getUser(1);
        Models models = forecastManager.addReferenceData(files.toArray(new SessionFileImpl[files.size()]), user);
        System.out.println(models);
    }

    @Test
    public void TestRemoveReferenceData() {
        List<Integer> ids = new ArrayList<>();
        ids.add(75);

        UserImpl user = (UserImpl) userService.getUser(1);
        Models models = forecastManager.removeReferenceData(ids, user);
        System.out.println(models);
    }

    @Test
    public void TestAnalyseUserData() {
        UserImpl user = (UserImpl) userService.getUser(1);
        Models models = forecastManager.analyseUserData(user);
        System.out.println(models);
    }

    private Collection<SessionFileImpl> generateFiles(String[] gpxs) {
        List<SessionFileImpl> files = new ArrayList<>();
        for (String fileName : gpxs) {
            SessionFileImpl sess = new SessionFileImpl();
            try {
                sess.setName(fileName);
                sess.setType("application/gpx");
                sess.setData(Files.readAllBytes(Paths.get(fileName)));
                files.add(sess);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return files;
    }
}
