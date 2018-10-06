package ng.org.knowit.fons.Rest;

import ng.org.knowit.fons.Models.CompanyQuote;
import ng.org.knowit.fons.Models.GlobalQuote;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("query")
    Call <GlobalQuote> getCompanyQuote(@Query("function") String GLOBAL_QUOTES,
                                        @Query("symbol") String symbol,
                                        @Query("apikey") String apikey);

}
