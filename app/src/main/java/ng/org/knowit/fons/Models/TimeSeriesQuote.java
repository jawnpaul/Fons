package ng.org.knowit.fons.Models;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


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

}
