package ultrathlete.com.training.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ultrathlete.com.training.bo.User;
import ultrathlete.com.training.bo.impl.UserImpl;
import ultrathlete.com.training.model.Forecast;
import ultrathlete.com.training.model.SegmentForecast;
import ultrathlete.com.training.service.UserService;
import ultrathlete.com.training.service.impl.ForecastManager;
import ultrathlete.com.training.service.model.PredictionModel;
import ultrathlete.com.training.util.TimeUtil;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/api/forecast")
@CrossOrigin
public class ForecastControler {

    @Autowired
    private UserService userService;
    @Autowired
    private ForecastManager forecastManager;

    /**
     * This endpoint describes at what time one is supposed to reach a given
     * @param file
     * @param userId
     * @param name
     * @param points
     * @param names
     * @return
     * @throws IOException
     */
    @GetMapping(path = "/")
    public ResponseEntity<Forecast> forecast(@RequestParam("file") MultipartFile file,
                                             @RequestParam("userId") int userId,
                                             @RequestParam("name") String name,
                                             @RequestParam("points") Double[] points,
                                             @RequestParam("names") String[] names
                                             ) throws IOException {
        User user = userService.getUser(userId);
        if (user != null) {
            List<PredictionModel> predictions = forecastManager.predictRun(file.getInputStream(), (UserImpl) user);
            Forecast forecast = generateForecast(name, predictions, points, names);
            return new ResponseEntity<>(forecast, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new Forecast(), HttpStatus.NO_CONTENT);
        }
    }

    private Forecast generateForecast(String name, List<PredictionModel> predictions, Double[] points, String[] names) {

        Forecast forecast = new Forecast();
        forecast.setName(name);
        double distance = 0.0;
        int elevationGain = 0;
        int elevationLoss = 0;

        int pLen = points.length;
        int nLen = names.length;
        boolean useNames = pLen == nLen ? true : false;
        int counter = 0;
        PredictionModel lastRecorded = null;
        PredictionModel last = null;

        for (PredictionModel prediction : predictions) {
            distance = prediction.getRanSegment().getTotalDistance();
            elevationGain = prediction.getRanSegment().getElevationGain();
            elevationLoss = prediction.getRanSegment().getElevationLoss();

            if (counter < pLen && distance >= points[counter] * 1000) {     // Make sure the points are ordered ...
                SegmentForecast segment = addSegmentToForecast(prediction, forecast);
                String segName = useNames ? names[counter] : Double.toString(points[counter]);
                segment.setName(segName);
                lastRecorded = prediction;
                counter = nextCount(counter, points);
            }
            last = prediction;
        }
        // Record the finish line if not recorded yet
        if (last != lastRecorded) {
            SegmentForecast segment = addSegmentToForecast(last, forecast);
            segment.setName(String.format("%.2f", last.getRanSegment().getTotalDistance() / 1000));
        }

        forecast.setDistance(distance);
        forecast.setElevationGain(elevationGain);
        forecast.setElevationLoss(elevationLoss);

        return forecast;
    }

    /**
     * This method is used to ensure that the point are used in an ascending order
     * @param counter
     * @param points
     * @return
     */
    private int nextCount(int counter, Double[] points) {
        int previous = counter;
        counter++;
        while (counter < points.length && points[counter] < points[previous]) {
            counter++;
        }
        return counter;
    }

    private SegmentForecast addSegmentToForecast(PredictionModel prediction, Forecast forecast) {
        double distance = prediction.getRanSegment().getTotalDistance();
        int elevationGain = prediction.getRanSegment().getElevationGain();
        int elevationLoss = prediction.getRanSegment().getElevationLoss();

        SegmentForecast segment = new SegmentForecast();
        forecast.add(segment);
        segment.setDistance(String.format("%.2f", distance / 1000));
        segment.setElevationGain(elevationGain);
        segment.setElevationLoss(elevationLoss);
        double min = prediction.getModel2() < prediction.getModel3() ? prediction.getModel2() : prediction.getModel3();
        double max = prediction.getModel2() > prediction.getModel3() ? prediction.getModel2() : prediction.getModel3();
        segment.setMin(TimeUtil.MakeReadable((long) min));
        segment.setMax(TimeUtil.MakeReadable((long) max));

        return segment;
    }
}
