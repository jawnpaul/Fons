package ng.org.knowit.fons.Utility;

import java.util.ArrayList;

public class Calculations {

    public static double calculateMean(ArrayList<Double> values){

        if(values.size() == 0) return 0;

        double sum = 0;
        for (int i = 0; i <values.size() ; i++) {
            sum+=values.get(i);

        }
        double mean = sum / values.size();
        return mean;
    }


    public static double calculateStandardDeviation(ArrayList<Double> values){

        if(values.size() == 0) return 0;

        double sum = 0;
        double mean = calculateMean(values);


        for (int i = 0; i <values.size() ; i++) {

            sum += Math.pow((values.get(i) - mean),2);

        }
        double squaredDifferenceMean = (sum) / (values.size());
        double standardDeviation = (Math.sqrt(squaredDifferenceMean));

        return standardDeviation;
    }

}
