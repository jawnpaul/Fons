package ng.org.knowit.fons.Models;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NewsQuote {

    private static final String  TAG = NewsQuote.class.getCanonicalName();

    @SerializedName("articles")
    @Expose
    private JsonArray mArticlesJsonArray;


    public void setArticlesJsonArray(JsonArray articlesJsonArray) {
        mArticlesJsonArray = articlesJsonArray;
    }

    public JsonArray getArticlesJsonArray() {
        return mArticlesJsonArray;
    }
}
