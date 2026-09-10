package ultrathlete.com.training.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ultrathlete.com.data.stats.regression.MultipleLinearRegression;
import ultrathlete.com.training.bo.RanSegment;
import ultrathlete.com.training.bo.Segment;
import ultrathlete.com.training.bo.SessionFile;
import ultrathlete.com.training.bo.impl.SessionFileImpl;
import ultrathlete.com.training.bo.impl.UserImpl;
import ultrathlete.com.training.data.UserDAO;
import ultrathlete.com.training.service.FileStorageService;
import ultrathlete.com.training.service.model.*;
import ultrathlete.com.training.stat.collector.ModelDataCollector;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class ForecastManager {

    @Autowired
    private FileStorageService storageService;
    @Autowired
    private UserDAO userDAO;

    public Models addReferenceData(SessionFileImpl[] files, UserImpl user) {
        if (files != null && user != null) {
            // 1st save the files
            storageService.save(files, user);
            // then recalculate
            return analyseUserData(user);
        }
        return null;
    }

    public Models removeReferenceData(List<Integer> fileIds, UserImpl user) {
        if (fileIds != null && user != null) {
            // 1st save the files
            storageService.delete(fileIds, user);
            // then recalculate
            return analyseUserData(user);
        }
        return null;
    }

    public Models analyseUserData(UserImpl user) {
        if (user == null) {
            return null;
        }
        Collection<SessionFileImpl> allFiles = storageService.getFiles(user);
        Models models = analyseData(allFiles);
        // Update the models for this user
        user.setModel2(models.getModel2());
        user.setModel3(models.getModel3());
        user.setModel4(models.getModel4());
        userDAO.save(user);
        return models;
    }

    public Models analyseData(Collection<SessionFileImpl> gpxs) {

        List<RanSegment> allRanSegments = new ArrayList<>();
        for (SessionFileImpl file : gpxs) {
            System.out.println(file.getName());
            SegmentBuilder segmentBuilder = new SegmentBuilder();
            List<Segment> segments = segmentBuilder.getSegments(file, 100);
            RanSegmentBuilder ranSegmentBuilder = new RanSegmentBuilder();
            //List<RanSegment> ranSegments = ranSegmentBuilder.getRanSegments(segments, 5, false);
            //allRanSegments.addAll(ranSegments);
            allRanSegments = ranSegmentBuilder.getRanSegments(segments, 5, false);
        }
        return calculateRegression(allRanSegments);
    }

    private Models calculateRegression(List<RanSegment> ranSegments) {

        if (ranSegments == null) {
            return null;
        }

        Model2 model2 = null;
        Model3 model3 = null;
        Model4 model4 = null;

        ModelDataCollector collector = new ModelDataCollector();
        collector.collectModelData(ranSegments);

        if (ranSegments.size() > 0) {

            String reg2 = null, reg3 = null, reg4 = null;

            try {
                ultrathlete.com.data.stats.regression.MultipleLinearRegression regression2 = new ultrathlete.com.data.stats.regression.MultipleLinearRegression(
                        collector.getModel2Data().getXs(), collector.getModel2Data().getYs());
                System.out.print(" R2: ");
                if (regression2.R2() * 100 >= 90) {
                    System.out.print("***");
                    System.out.print(String.format("%3.2f", regression2.R2() * 100) + "; ");

                    reg2 = regression2.beta(0) + " + " + regression2.beta(1) + "distance + "
                            + regression2.beta(2) + "gain + " + regression2.beta(3) + "loss";
                    model2 = createModel2(regression2);
                }
            } catch (Exception e) {
                System.out.print(e.getMessage() + "; ");
            }
            try {
                ultrathlete.com.data.stats.regression.MultipleLinearRegression regression3 = new ultrathlete.com.data.stats.regression.MultipleLinearRegression(
                        collector.getModel3Data().getXs(), collector.getModel3Data().getYs());
                System.out.print(" R3: ");
                if (regression3.R2() * 100 >= 90) {
                    System.out.print("***");
                    System.out.print(String.format("%3.2f", regression3.R2() * 100) + "; ");

                    reg3 = regression3.beta(0) + " + " + regression3.beta(1) + "easy + "
                            + regression3.beta(2) + "medium + " + regression3.beta(3) + "hard" +
                            +regression3.beta(4) + "mediumD + " + regression3.beta(5) + "hardD + "
                            + regression3.beta(6) + "Vhard + " + regression3.beta(7) + "VhardD";

                    model3 = createModel3(regression3);
                }

            } catch (Exception e) {
                System.out.print(e.getMessage() + "; ");
            }

            try {
                ultrathlete.com.data.stats.regression.MultipleLinearRegression regression4 = new ultrathlete.com.data.stats.regression.MultipleLinearRegression(
                        collector.getModel4Data().getXs(), collector.getModel4Data().getYs());
                System.out.print(" R4: ");
                if (regression4.R2() * 100 >= 90) {
                    System.out.print("***");
                    System.out.print(String.format("%3.2f", regression4.R2() * 100) + "; ");
                    reg4 = regression4.beta(0) + " + " + regression4.beta(1) + "distance";

                    model4 = createModel4(regression4);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            System.out.println();
            System.out.println("R2: " + reg2);
            System.out.println("R3: " + reg3);
        }
        Models models = new Models();
        models.setModel2(model2);
        models.setModel3(model3);
        models.setModel4(model4);
        return models;
    }

    private Model2 createModel2(MultipleLinearRegression regression2) {
        Model2 model2 = new Model2();
        model2.setConstant(regression2.beta(0));
        model2.setDistance(regression2.beta(1));
        model2.setGain(regression2.beta(2));
        model2.setLoss(regression2.beta(3));
        model2.setR2(regression2.R2());
        return model2;
    }

    private Model3 createModel3(MultipleLinearRegression regression3) {
        Model3 model3 = new Model3();
        model3.setConstant(regression3.beta(0));
        model3.setEasy(regression3.beta(1));
        model3.setMedium(regression3.beta(2));
        model3.setHard(regression3.beta(3));
        model3.setMediumD(regression3.beta(4));
        model3.setHardD(regression3.beta(5));
        model3.setVeryHard(regression3.beta(6));
        model3.setVeryHardD(regression3.beta(7));
        model3.setR2(regression3.R2());
        return model3;
    }

    private Model4 createModel4(MultipleLinearRegression regression4) {
        Model4 model4 = new Model4();
        model4.setConstant(regression4.beta(0));
        model4.setDistance(regression4.beta(1));
        model4.setR2(regression4.R2());
        return model4;
    }

    public List<PredictionModel> predictRun(SessionFile file, UserImpl user) {
        if (file != null) {
            System.out.println(file.getName());
            SegmentBuilder segmentBuilder = new SegmentBuilder();
            List<Segment> segments = segmentBuilder.getSegments(file, 100);
            RanSegmentBuilder ranSegmentBuilder = new RanSegmentBuilder();
            List<RanSegment> ranSegments = ranSegmentBuilder.getRanSegments(segments, 5, false);
            return predictRun(ranSegments, user);
        } else {
            return new ArrayList<>();
        }
    }

    public List<PredictionModel> predictRun(InputStream file, UserImpl user) {
        if (file != null) {
            SegmentBuilder segmentBuilder = new SegmentBuilder();
            List<Segment> segments = segmentBuilder.getSegments(file, 100);
            RanSegmentBuilder ranSegmentBuilder = new RanSegmentBuilder();
            List<RanSegment> ranSegments = ranSegmentBuilder.getRanSegments(segments, 5, false);
            return predictRun(ranSegments, user);
        } else {
            return new ArrayList<>();
        }
    }

    private List<PredictionModel> predictRun(List<RanSegment> ranSegments, UserImpl user) {

        List<PredictionModel> models = new ArrayList<>();

        Model2 model2 = user.getModel2();
        Model3 model3 = user.getModel3();
        Model4 model4 = user.getModel4();

        for (RanSegment ran : ranSegments) {

            Predictor predictor = new Predictor();
            double predict2 = predictor.getModel2Prediction(ran, model2);
            double predict3 = predictor.getModel3Prediction(ran, model3);
            double predict4 = predictor.getModel4Prediction(ran, model4);

            predict2 = predict2 > 0 ? predict2 : 0.0;
            predict3 = predict3 > 0 ? predict3 : 0.0;
            predict4 = predict4 > 0 ? predict4 : 0.0;

            PredictionModel model = new PredictionModel(ran, predict2, predict3, predict4);
            models.add(model);
        }
        return models;
    }
}
