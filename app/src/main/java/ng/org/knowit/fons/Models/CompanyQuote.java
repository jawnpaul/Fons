package ng.org.knowit.fons.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CompanyQuote implements Parcelable{

    @SerializedName("01. symbol")
    @Expose
    private String companySymbol;

    @SerializedName("02. open")
    @Expose
    private String openPrice;

    @SerializedName("03. high")
    @Expose
    private String highPrice;

    @SerializedName("04. low")
    @Expose
    private String lowPrice;

    @SerializedName("05. price")
    @Expose
    private String currentPrice;

    @SerializedName("06. volume")
    @Expose
    private String currentVolume;

    @SerializedName("07. latest trading day")
    @Expose
    private String latestTradingDay;

    @SerializedName("08. previous close")
    @Expose
    private String previousClose;

    @SerializedName("09. change")
    @Expose
    private String change;

    @SerializedName("10. change percent")
    @Expose
    private String changePercent;

    public CompanyQuote(){
        super();
    }

    public CompanyQuote(Parcel parcel) {
        setCompanySymbol(parcel.readString());
        setOpenPrice(parcel.readString());
        setHighPrice(parcel.readString());
        setLowPrice(parcel.readString());
        setCurrentPrice(parcel.readString());
        setCurrentVolume(parcel.readString());
        setLatestTradingDay(parcel.readString());
        setPreviousClose(parcel.readString());
        setChange(parcel.readString());
        setChangePercent(parcel.readString());
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        @Override
        public CompanyQuote createFromParcel(Parcel in) {
            return new CompanyQuote(in);
        }

        @Override
        public CompanyQuote[] newArray(int size) {
            return new CompanyQuote[size];
        }
    };

    public String getOpenPrice() {
        return openPrice;
    }

    public String getHighPrice() {
        return highPrice;
    }

    public String getLowPrice() {
        return lowPrice;
    }

    public String getCurrentPrice() {
        return currentPrice;
    }

    public String getCurrentVolume() {
        return currentVolume;
    }

    public String getLatestTradingDay() {
        return latestTradingDay;
    }

    public String getPreviousClose() {
        return previousClose;
    }

    public String getChange() {
        return change;
    }

    public String getChangePercent() {
        return changePercent;
    }

    public String getCompanySymbol() {
        return companySymbol;
    }

    public void setCompanySymbol(String companySymbol) {
        this.companySymbol = companySymbol;
    }

    public void setOpenPrice(String openPrice) {
        this.openPrice = openPrice;
    }

    public void setCurrentPrice(String currentPrice) {
        this.currentPrice = currentPrice;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public void setCurrentVolume(String currentVolume) {
        this.currentVolume = currentVolume;
    }

    public void setLowPrice(String lowPrice) {
        this.lowPrice = lowPrice;
    }

    public void setChangePercent(String changePercent) {
        this.changePercent = changePercent;
    }

    public void setLatestTradingDay(String latestTradingDay) {
        this.latestTradingDay = latestTradingDay;
    }

    public void setPreviousClose(String previousClose) {
        this.previousClose = previousClose;
    }

    public void setHighPrice(String highPrice) {
        this.highPrice = highPrice;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(getCompanySymbol());
        parcel.writeString(getOpenPrice());
        parcel.writeString(getHighPrice());
        parcel.writeString(getLowPrice());
        parcel.writeString(getCurrentPrice());
        parcel.writeString(getCurrentVolume());
        parcel.writeString(getLatestTradingDay());
        parcel.writeString(getPreviousClose());
        parcel.writeString(getChange());
        parcel.writeString(getChangePercent());
    }
}
