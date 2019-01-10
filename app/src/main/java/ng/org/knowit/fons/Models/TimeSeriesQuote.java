package ng.org.knowit.fons.Models;

import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;



public class TimeSeriesQuote {

    private static final String TAG = TimeSeriesQuote.class.getSimpleName();



    @SerializedName("Time Series (Daily)")
    @Expose
    private JsonObject results = null;

    public JsonObject getResults() {
        return results;
    }

    public void setResults(JsonObject results) {
        this.results = results;
    }



     public void getJsonItem(){
        //JSONObject ade = getResults().get(1);
        //String adeleke = ade.toString();
        //Log.d(TAG, adeleke);
    }
}
