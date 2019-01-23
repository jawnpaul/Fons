package ng.org.knowit.fons.Models;

import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


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

    public ArrayList<JsonElement> parseValues(JsonObject valuesObject) {
        ArrayList<JsonElement> result = new ArrayList<>();

        for (Map.Entry<String, JsonElement> valueEntry : valuesObject.entrySet()) {
            JsonElement element = valueEntry.getValue();

            result.add(element);

            /*if (element.isJsonPrimitive()) {
                JsonPrimitive value = element.getAsJsonPrimitive();
                if (value.isString()) {
                    result.add(new StringDataEntry(valueEntry.getKey(), value.getAsString()));
                } else if (value.isBoolean()) {
                    result.add(new BooleanDataEntry(valueEntry.getKey(), value.getAsBoolean()));
                } else if (value.isNumber()) {
                    if (value.getAsString().contains(".")) {
                        result.add(new DoubleDataEntry(valueEntry.getKey(), value.getAsDouble()));
                    } else {
                        result.add(new LongDataEntry(valueEntry.getKey(), value.getAsLong()));
                    }
                } else {
                    throw new JsonSyntaxException("Can't parse value: " + value);
                }
            } else {
                throw new JsonSyntaxException("Can't parse value: " + element);
            }*/
        }
        return result;
    }

}
