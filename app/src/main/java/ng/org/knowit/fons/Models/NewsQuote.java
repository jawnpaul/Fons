package ng.org.knowit.fons.Models;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class NewsQuote {


    @SerializedName("articles")
    @Expose
    private ArrayList<NewsItem> results = null;

    public ArrayList<NewsItem> getResults() {
        return results;
    }

    public void setResults(ArrayList<NewsItem> results) {
        this.results = results;
    }
}
