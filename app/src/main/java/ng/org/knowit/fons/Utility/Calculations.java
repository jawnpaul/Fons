package ng.org.knowit.fons.Utility;

import android.util.Log;

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

        if (values.size() == 0) {
            return 0;
        }

        double dv = 0;
        double sum = 0;
        double mean = calculateMean(values);

        //Log.w("Calculations: ", String.valueOf(mean) );
        for (double d : values) {
            double dm = Math.abs(d - mean);
            dv += Math.pow(dm, 2);
        }

        double sd = Math.sqrt(dv / values.size());
        //System.out.println("Standard dev is " + sd);

        return sd;
    }

    public static double summ(ArrayList<Double> values){
        double sum = 0;
        for (double number:values) {
            sum+= number;
        }
        return sum;
    }

}
