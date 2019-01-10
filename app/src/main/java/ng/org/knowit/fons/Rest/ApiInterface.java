package ng.org.knowit.fons.Rest;


import ng.org.knowit.fons.Models.GlobalQuote;
import ng.org.knowit.fons.Models.NewsQuote;
import ng.org.knowit.fons.Models.TimeSeriesQuote;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {


    @GET("query")
    Call <GlobalQuote> getCompanyQuote(@Query("function") String GLOBAL_QUOTES,
                                        @Query("symbol") String symbol,
                                        @Query("apikey") String apikey);


    @GET("top-headlines")
    Call <NewsQuote> getNewsQuote(@Query("sources") String business_insider,
                                    @Query("apiKey") String apiKey);


    @GET("query")
    Call <TimeSeriesQuote> getCompanyTimeSeries(@Query("function") String TIME_SERIES_DAILY,
                                                @Query("symbol") String symbol,
                                                @Query("outputsize") String full,
                                                @Query("apikey") String apikey);

}
