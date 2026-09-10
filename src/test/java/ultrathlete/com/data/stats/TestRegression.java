package ultrathlete.com.data.stats;

import ultrathlete.com.data.stats.Utils.MultiplePoint;
import ultrathlete.com.data.stats.linearRegression.MultipleLinearRegression;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TestRegression {

    public static void main(String[] args) {
        //File file = new File("C:\\gd\\ultratraining\\src\\test\\java\\ultrathlete\\com\\data\\stats\\testData3.txt");
        File file = new File("C:\\gd\\ultratraining\\src\\test\\java\\ultrathlete\\com\\data\\stats\\testDataCompareExcel.txt");
        Scanner sc = null;
        try {
            sc = new Scanner(file);
            sc.nextLine(); //ignores first line with comment

            List<MultiplePoint> points = new ArrayList<>();

            while (sc.hasNext()) {
                //assume number of tokens is multiple of 3

                String x1 = sc.next();
                String x2 = sc.next();
                String y = sc.next();
                List<Double> list = new ArrayList<>();
                list.add(Double.parseDouble(x1));
                list.add(Double.parseDouble(x2));
                points.add(new MultiplePoint(list, Double.parseDouble(y)));
            }
            //creates the multiple linear regression object and computes the coefficients
            MultipleLinearRegression mlr = new MultipleLinearRegression(points);
            //get the coefficients
            double[][] coefficients = mlr.getCoefficients();
            //get the List of Multiple Points
            List<MultiplePoint> list = mlr.getPoints();
            //get a prediction for the given independent variables
            double prediction = mlr.getPrediction(1.3, 2.78);
            double[] values = new double[] {3, 1};
            double prediction2 = mlr.getPrediction(values);

            System.out.println(prediction);
            System.out.println(prediction2);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
