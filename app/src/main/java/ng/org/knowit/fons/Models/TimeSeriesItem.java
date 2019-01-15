package ng.org.knowit.fons.Models;

import com.google.gson.JsonElement;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TimeSeriesItem extends JsonElement{

    @SerializedName("1. open")
    @Expose
    private String openPrice;

    @SerializedName("2. high")
    @Expose
    private String highPrice;

    @SerializedName("3. low")
    @Expose
    private String lowPrice;

    @SerializedName("4. close")
    @Expose
    private String closePrice;

    @SerializedName("5. volume")
    @Expose
    private String volumeQuantity;

    public String getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(String openPrice) {
        this.openPrice = openPrice;
    }

    public String getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(String highPrice) {
        this.highPrice = highPrice;
    }

    public String getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(String lowPrice) {
        this.lowPrice = lowPrice;
    }

    public String getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(String closePrice) {
        this.closePrice = closePrice;
    }

    public String getVolumeQuantity() {
        return volumeQuantity;
    }

    public void setVolumeQuantity(String volumeQuantity) {
        this.volumeQuantity = volumeQuantity;
    }

    @Override
    public JsonElement deepCopy() {
        return null;
    }
}
