package ng.org.knowit.fons.Rest;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {


    private static final String ALPHAVANTAGE_BASE_URL = "https://www.alphavantage.co/";
    private static final String NEWS_BASE_URL = "https://newsapi.org/v2/";
    private static Retrofit.Builder stockRetrofitBuilder = null;
    private static Retrofit.Builder newsRetrofitBuilder = null;
    private static Retrofit stockRetrofit;
    private static Retrofit newsRetrofit;



    /**
     *
     *
     *
     * A static method to initialize the retrofit object with the base url and converter
     *
     *
     *
     * @return Retrofit object
     */
    public static Retrofit getStockClient(){

        if(stockRetrofit == null) {
            //initializing the Retrofit builder, adding the base url and Converter to use
            stockRetrofitBuilder = new Retrofit.Builder();
            stockRetrofitBuilder.baseUrl(ALPHAVANTAGE_BASE_URL);
            stockRetrofitBuilder.addConverterFactory(GsonConverterFactory.create());
            stockRetrofit = stockRetrofitBuilder.build();

        }
        return stockRetrofit;

    }

    public static Retrofit getNewsClient(){

        if(newsRetrofit == null) {
            //initializing the Retrofit builder, adding the base url and Converter to use
            newsRetrofitBuilder = new Retrofit.Builder();
            newsRetrofitBuilder.baseUrl(NEWS_BASE_URL);
            newsRetrofitBuilder.addConverterFactory(GsonConverterFactory.create());
            newsRetrofit = newsRetrofitBuilder.build();

        }
        return newsRetrofit;

    }
}
