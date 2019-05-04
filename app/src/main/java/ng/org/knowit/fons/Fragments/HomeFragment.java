package ng.org.knowit.fons.Fragments;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerTabStrip;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import org.tensorflow.lite.Interpreter;

import ng.org.knowit.fons.Adapters.CompanyAdapter;
import ng.org.knowit.fons.Data.CompanyContract;
import ng.org.knowit.fons.Data.CompanyDbHelper;
import ng.org.knowit.fons.Data.CompanyUpdateService;
import ng.org.knowit.fons.Main2Activity;
import ng.org.knowit.fons.Models.GlobalQuote;
import ng.org.knowit.fons.Models.TimeSeriesQuote;
import ng.org.knowit.fons.R;
import ng.org.knowit.fons.Rest.ApiClient;
import ng.org.knowit.fons.Rest.ApiInterface;
import ng.org.knowit.fons.Utility.Calculations;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link //HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static final String TAG = HomeFragment.class.getSimpleName();

    private static final String GLOBAL_QUOTE = "GLOBAL_QUOTE";
    private static final String TIME_SERIES_DAILY = "TIME_SERIES_DAILY";
    private static final String OUTPUT_SIZE = "compact";
    private static final String API_KEY = "FETXFXJ9VMMUJFE9";
    private static final String MICROSOFT_SYMBOL = "MSFT";
    private static final String GOOGLE_SYMBOL = "GOOGL";
    private static final String TESLA_SYMBOL = "TSLA";
    private static final String WALMART_SYMBOL = "WMT";
    private static final String PZ_SYMBOL = "PZ";
    private static final String APPLE_SYMBOL = "AAPL";
    private static final String GOLDMAN_SYMBOL = "GS";
    private static final String TAG1 = HomeFragment.class.getCanonicalName();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Interpreter msTflite, googleTflite, teslaTflite, walmartTflite, pzTflite, appleTflite, gsTflite;

    private String[] companyNames;

    static ArrayList<JsonElement> allItems = new ArrayList<>();

    private SQLiteDatabase mSQLiteDatabase;

    private int spinnerPosition;
    FragmentPagerAdapter adapterViewPager;

    CompanyAdapter mCompanyAdapter;

    //private Cursor mCursor;

    private Cursor singleCursor;

    private ViewPager mPager;

    Context mContext;

    Toolbar toolbar;

    Spinner companySpinner;

    ProgressBar mProgressBar;

    private double openPrice, highPrice, lowPrice, volumeQuantity, currentPrice;
    //private float openPriceNormalized, lowPriceNormalized, highPriceNormalized, currentPriceNormalized, volumeQuantityNormalized;

    TextView priceTextView, openPriceTextView, highPriceTextView,
            lowPriceTextView, volumeTextView, changePercentTextView;
    String priceText, openPriceText, highPriceText, lowPriceText, volumeText, changePercentText, symbolText,
    latestTradingDayText, previousDayCloseText, changeText;


    //private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


        companyNames = getResources().getStringArray(R.array.company_names);
        mContext = getContext();

        CompanyDbHelper dbHelper = new CompanyDbHelper(mContext);
        mSQLiteDatabase = dbHelper.getWritableDatabase();

        if(singleCursor!= null) singleCursor.close();
        singleCursor = getSingleCompany(spinnerPosition);


        //mCompanyAdapter = new CompanyAdapter(mContext, mCursor);

        String myjson = inputStreamToString(mContext.getResources().openRawResource(R.raw.my_json));

        TimeSeriesQuote myModel = new Gson().fromJson(myjson, TimeSeriesQuote.class);

        allItems = myModel.parseValues(myModel.getResults());
        Log.w(TAG, "size from local json "+String.valueOf(allItems.size()));

        openCompaniesTflites();



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view = inflater.inflate(R.layout.fragment_home, container, false);

        priceTextView = view.findViewById(R.id.textViewStockPrice);
        changePercentTextView = view.findViewById(R.id.textViewPercentage);
        openPriceTextView = view.findViewById(R.id.textViewOpenPrice);
        highPriceTextView = view.findViewById(R.id.textViewHighPrice);
        lowPriceTextView = view.findViewById(R.id.textViewLowPrice);
        volumeTextView = view.findViewById(R.id.textViewVolumeQuantity);

        companySpinner = view.findViewById(R.id.spinner_toolbar);

        mProgressBar = view.findViewById(R.id.home_progress_bar);

        spinnerPosition = companySpinner.getSelectedItemPosition();

        ArrayAdapter<String> companyNamesAdapter = new ArrayAdapter<String>(getActivity(),  android.R.layout.simple_spinner_item, companyNames);
        companyNamesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        companySpinner.setAdapter(companyNamesAdapter);

        companySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                selectedSpinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apiTimeSeriesCall(companyName(spinnerPosition));
                //Toast.makeText(getContext(), String.valueOf(doInference()), Toast.LENGTH_SHORT).show();
            }
        });

        if (isOnline() &&  singleCursor.getCount() <= 0){
            makeApiCall(MICROSOFT_SYMBOL);
            mProgressBar = view.findViewById(R.id.news_progress_bar);
        } else{

            loadCompanyFromDatabase();
        }

        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((Main2Activity)getActivity()).setToolbar(toolbar);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        toolbar = view.findViewById(R.id.toolbar);

        mPager = view.findViewById(R.id.vpPager);
        adapterViewPager = new MyPagerAdapter(getChildFragmentManager());
        mPager.setAdapter(adapterViewPager);
        insertNestedFragment();


        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset,
                    int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
               /* Toast.makeText(getContext(),
                        "Selected page position: " + position, Toast.LENGTH_SHORT).show();*/

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        final CollapsingToolbarLayout collapsingToolbarLayout = view.findViewById(R.id.collapsing_toolbar_layout);

        final PagerTabStrip pagerTabStrip = view.findViewById(R.id.pager_header);

        AppBarLayout appBarLayout = view.findViewById(R.id.app_bar_layout);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                //Initialize the size of the scroll
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                //Check if the view is collapsed
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle(" ");
                    pagerTabStrip.setVisibility(View.INVISIBLE);
                    mPager.setVisibility(View.INVISIBLE);

                } else {
                    collapsingToolbarLayout.setTitle(" ");
                    pagerTabStrip.setVisibility(View.VISIBLE);
                    mPager.setVisibility(View.VISIBLE);
                }

            }
        });
    }

    private void insertNestedFragment() {
        OneDayFragment childFragment = new OneDayFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        //loadFragment(childFragment);
        transaction.replace(R.id.frag, childFragment).commit();
    }

    private void selectedSpinner(){
        spinnerPosition = companySpinner.getSelectedItemPosition();
        switch (spinnerPosition){
            case 0:
                makeApiCall(MICROSOFT_SYMBOL);
                break;
            case 1:
                //singleCursor = getSingleCompany(spinnerPosition);
                makeApiCall(GOOGLE_SYMBOL);
                //apiTimeSeriesCall(GOOGLE_SYMBOL);
                break;
            case 2:
                makeApiCall(TESLA_SYMBOL);
                //apiTimeSeriesCall(TESLA_SYMBOL);
                break;
            case 3:
                makeApiCall(WALMART_SYMBOL);
                //apiTimeSeriesCall(WALMART_SYMBOL);
                break;
            case 4:
                makeApiCall(PZ_SYMBOL);
                //apiTimeSeriesCall(PZ_SYMBOL);
                break;
            case 5:
                makeApiCall(APPLE_SYMBOL);
               // apiTimeSeriesCall(APPLE_SYMBOL);
                break;
            case 6:
                makeApiCall(GOLDMAN_SYMBOL);
                //apiTimeSeriesCall(GOLDMAN_SYMBOL);
                break;

                default:
                    break;
        }
    }

    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 5;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            OneDayFragment oneDayFragment = new OneDayFragment();
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment

                    oneDayFragment = OneDayFragment.newInstance("Page zero",0);
                    Bundle f_bundle = oneDayFragment.getArguments();
                    String f_param1 = f_bundle.getString("param1");
                    break;
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    oneDayFragment = OneDayFragment.newInstance("Page one",1);

                    break;

                case 2: // Fragment # 1 - This will show SecondFragment

                    oneDayFragment = OneDayFragment.newInstance("Page Two", 2);

                    break;
                case 3:
                    oneDayFragment = OneDayFragment.newInstance("Page Three", 3);
                    break;
                    //return OneDayFragment.newInstance("page two", 2);
                case 4:
                    oneDayFragment = OneDayFragment.newInstance("Page Four", 4);
                    break;
                default:
                   // Log.d("Home Fragment", "Which one? fragment has loaded");
            }
            return oneDayFragment;
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {

            //Write a switch statement here to show each tab title for specific tab I want

            return "Page " + position;
        }

    }

    private boolean loadFragment(OneDayFragment fragment) {
        //switching fragment
        if (fragment != null) {
            getChildFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frag, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    public void displayMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    public void makeApiCall(String Symbol){

        if (!isOnline()) {

            String title = "Connection";
            String message = "No internet connection. Please try again.";
            displayMessage(title, message);
            }

        mProgressBar.setVisibility(View.VISIBLE);

            ApiInterface apiInterface = ApiClient.getStockClient().create(ApiInterface.class);

            Call<GlobalQuote> callToApi = apiInterface.getCompanyQuote(GLOBAL_QUOTE, Symbol, API_KEY);

            callToApi.enqueue(new Callback<GlobalQuote>() {
                @Override
                public void onResponse(Call<GlobalQuote> call, Response<GlobalQuote> response) {
                    GlobalQuote globalQuote = response.body();


                    mProgressBar.setVisibility(View.INVISIBLE);

                    if (globalQuote == null) {

                        ResponseBody responseBody = response.errorBody();
                        String errorTitle;
                        String errorMessage;
                        if (responseBody != null) {
                            errorTitle = "Error";
                            errorMessage = "An error occurred.";
                        } else {
                            errorTitle = "Error";
                            errorMessage = "No data Received.";
                        }
                        displayMessage(errorTitle, errorMessage);
                    } else {

                        Log.e(TAG1, new Gson().toJson(globalQuote));

                        priceText = globalQuote.getCompanyQuote().getCurrentPrice();
                        currentPrice = Double.parseDouble(priceText);

                        changePercentText = globalQuote.getCompanyQuote().getChangePercent();

                        openPriceText = globalQuote.getCompanyQuote().getOpenPrice();
                        openPrice = Double.parseDouble(openPriceText);

                        highPriceText = globalQuote.getCompanyQuote().getHighPrice();
                        highPrice = Double.parseDouble(highPriceText);

                        lowPriceText = globalQuote.getCompanyQuote().getLowPrice();
                        lowPrice = Double.parseDouble(lowPriceText);

                        volumeText = globalQuote.getCompanyQuote().getCurrentVolume();
                        volumeQuantity = Double.parseDouble(volumeText);

                        changeText = globalQuote.getCompanyQuote().getChange();
                        symbolText = globalQuote.getCompanyQuote().getCompanySymbol();
                        latestTradingDayText = globalQuote.getCompanyQuote().getLatestTradingDay();
                        previousDayCloseText = globalQuote.getCompanyQuote().getPreviousClose();
                        updateViews();

                        addNewCompany(symbolText, openPriceText, highPriceText, lowPriceText,
                                priceText, volumeText, latestTradingDayText, previousDayCloseText, changeText, changePercentText);


                        //Toast.makeText(getActivity(), "Open price is "+ globalQuote.getCompanyQuote().getOpenPrice(), Toast.LENGTH_LONG).show();

                        }
                    }



                @Override
                public void onFailure(Call<GlobalQuote> call, Throwable t) {
                    mProgressBar.setVisibility(View.INVISIBLE);
                    String errorTitle = "Error";
                    String errorMessage = "Data request failed.";
                    displayMessage(errorTitle, errorMessage);
                }
            });

        }


        public void updateViews(){
        if (changePercentText.contains("-")){
            changePercentTextView.setTextColor(getResources().getColor(R.color.colorAccent));
            } else {
            changePercentTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
        }

            priceTextView.setText(priceText);
            changePercentTextView.setText(changePercentText);
            openPriceTextView.setText(openPriceText);
            highPriceTextView.setText(highPriceText);
            lowPriceTextView.setText(lowPriceText);
            volumeTextView.setText(volumeText);
        }


    private void addNewCompany(String companySymbol, String companyOpen,
            String companyHigh, String companyLow, String companyPrice, String companyVolume,
            String companyLatestTradingDay, String companyPreviousClose, String companyChange,
            String companyChangePercent){
        ContentValues cv = new ContentValues();
        cv.put(CompanyContract.CompanyEntry.COLUMN_COMPANY_SYMBOL, companySymbol);
        cv.put(CompanyContract.CompanyEntry.COLUMN_COMPANY_OPEN, companyOpen);
        cv.put(CompanyContract.CompanyEntry.COLUMN_COMPANY_HIGH, companyHigh);
        cv.put(CompanyContract.CompanyEntry.COLUMN_COMPANY_LOW, companyLow);
        cv.put(CompanyContract.CompanyEntry.COLUMN_COMPANY_PRICE, companyPrice);
        cv.put(CompanyContract.CompanyEntry.COLUMN_COMPANY_VOLUME, companyVolume);
        cv.put(CompanyContract.CompanyEntry.COLUMN_COMPANY_LATEST_TRADING_DAY, companyLatestTradingDay);
        cv.put(CompanyContract.CompanyEntry.COLUMN_COMPANY_PREVIOUS_CLOSE, companyPreviousClose);
        cv.put(CompanyContract.CompanyEntry.COLUMN_COMPANY_CHANGE, companyChange);
        cv.put(CompanyContract.CompanyEntry.COLUMN_COMPANY_CHANGE_PERCENT, companyChangePercent);

        Uri uri = mContext.getContentResolver().insert(CompanyContract.CONTENT_URI, cv);

        if (uri != null){
            Log.d(TAG, uri.toString());
        }

        //mCompanyAdapter.swappCursor(mCursor);
    }

    private void updateCompany(int spinnerPosition, String companySymbol, String companyOpen,
            String companyHigh, String companyLow, String companyPrice, String companyVolume,
            String companyLatestTradingDay, String companyPreviousClose, String companyChange,
            String companyChangePercent){
        ContentValues cv = new ContentValues();
        cv.put(CompanyContract.CompanyEntry.COLUMN_COMPANY_SYMBOL, companySymbol);
        cv.put(CompanyContract.CompanyEntry.COLUMN_COMPANY_OPEN, companyOpen);
        cv.put(CompanyContract.CompanyEntry.COLUMN_COMPANY_HIGH, companyHigh);
        cv.put(CompanyContract.CompanyEntry.COLUMN_COMPANY_LOW, companyLow);
        cv.put(CompanyContract.CompanyEntry.COLUMN_COMPANY_PRICE, companyPrice);
        cv.put(CompanyContract.CompanyEntry.COLUMN_COMPANY_VOLUME, companyVolume);
        cv.put(CompanyContract.CompanyEntry.COLUMN_COMPANY_LATEST_TRADING_DAY, companyLatestTradingDay);
        cv.put(CompanyContract.CompanyEntry.COLUMN_COMPANY_PREVIOUS_CLOSE, companyPreviousClose);
        cv.put(CompanyContract.CompanyEntry.COLUMN_COMPANY_CHANGE, companyChange);
        cv.put(CompanyContract.CompanyEntry.COLUMN_COMPANY_CHANGE_PERCENT, companyChangePercent);

        Uri uriForCompany = CompanyContract.buildSingleCompany(spinnerPosition + 1);
        CompanyUpdateService.updateCompany(mContext, uriForCompany, cv);

        //CompanyUpdateService.updateCompany(mContext, CompanyContract.getColumnLong(null, null));
    }

    private Cursor getSingleCompany(int spinnerPosition){
        return mContext.getContentResolver().query(CompanyContract.buildSingleCompany(spinnerPosition + 1),
                null,
                null,
                null,
                null);
    }

    private MappedByteBuffer loadGoldmanModelFile() throws IOException {
        AssetFileDescriptor fileDescriptor = getActivity().getAssets().openFd("gs_model.tflite");
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffSet = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffSet, declaredLength);
    }

    private MappedByteBuffer loadGoogleModelFile() throws IOException {
        AssetFileDescriptor fileDescriptor = getActivity().getAssets().openFd("google_model.tflite");
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffSet = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffSet, declaredLength);
    }

    private MappedByteBuffer loadMicrosoftModelFile() throws IOException {
        AssetFileDescriptor fileDescriptor = getActivity().getAssets().openFd("model.tflite");
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffSet = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffSet, declaredLength);
    }

    private MappedByteBuffer loadAppleModelFile() throws IOException {
        AssetFileDescriptor fileDescriptor = getActivity().getAssets().openFd("apple_model.tflite");
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffSet = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffSet, declaredLength);
    }

    private MappedByteBuffer loadPzModelFile() throws IOException {
        AssetFileDescriptor fileDescriptor = getActivity().getAssets().openFd("pz_model.tflite");
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffSet = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffSet, declaredLength);
    }

    private MappedByteBuffer loadTeslaModelFile() throws IOException {
        AssetFileDescriptor fileDescriptor = getActivity().getAssets().openFd("tesla_model.tflite");
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffSet = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffSet, declaredLength);
    }

    private MappedByteBuffer loadWalmartModelFile() throws IOException {
        AssetFileDescriptor fileDescriptor = getActivity().getAssets().openFd("walmart_model.tflite");
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffSet = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffSet, declaredLength);
    }


    private void openCompaniesTflites(){
        try {
            msTflite = new Interpreter(loadMicrosoftModelFile());
            googleTflite = new Interpreter(loadGoogleModelFile());
            appleTflite = new Interpreter(loadAppleModelFile());
            gsTflite = new Interpreter(loadGoldmanModelFile());
            pzTflite = new Interpreter(loadPzModelFile());
            teslaTflite = new Interpreter(loadTeslaModelFile());
            walmartTflite = new Interpreter(loadWalmartModelFile());
            //Log.d(TAG, "Microsoft model file loaded");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public float doInference(float open, float high, float low, float price, float volume){
        float [][] inputVal = {{open, high, low, price, volume}};

        float [][] outputVal = new float[1][1];
        float inferredValue = 0;
        String companyName = "";
        switch (spinnerPosition){
            case 0:
                msTflite.run(inputVal, outputVal);
                 inferredValue = outputVal[0][0];
                 companyName =  companyName(spinnerPosition);
                Toast.makeText(mContext, companyName + " predicted price is " + String.valueOf(inferredValue), Toast.LENGTH_LONG).show();
                break;
            case 1:
                googleTflite.run(inputVal, outputVal);
                inferredValue = outputVal[0][0];
                companyName =  companyName(spinnerPosition);
                Toast.makeText(mContext, companyName + " predicted price is " + String.valueOf(inferredValue), Toast.LENGTH_LONG).show();
                break;
            case 2:
                teslaTflite.run(inputVal, outputVal);
                inferredValue = outputVal[0][0];
                companyName =  companyName(spinnerPosition);
                Toast.makeText(mContext, companyName + " predicted price is " + String.valueOf(inferredValue), Toast.LENGTH_LONG).show();
                break;
            case 3:
                walmartTflite.run(inputVal, outputVal);
                inferredValue = outputVal[0][0];
                companyName =  companyName(spinnerPosition);
                Toast.makeText(mContext, companyName + " predicted price is " + String.valueOf(inferredValue), Toast.LENGTH_LONG).show();
                break;
            case 4:
                pzTflite.run(inputVal, outputVal);
                inferredValue = outputVal[0][0];
                companyName =  companyName(spinnerPosition);
                Toast.makeText(mContext, companyName + " predicted price is " + String.valueOf(inferredValue), Toast.LENGTH_LONG).show();
                break;
            case 5:
                appleTflite.run(inputVal, outputVal);
                inferredValue = outputVal[0][0];
                companyName =  companyName(spinnerPosition);
                Toast.makeText(mContext, companyName + " predicted price is " + String.valueOf(inferredValue), Toast.LENGTH_LONG).show();
                break;
            case 6:
                gsTflite.run(inputVal, outputVal);
                inferredValue = outputVal[0][0];
                companyName =  companyName(spinnerPosition);
                Toast.makeText(mContext, companyName + " predicted price is " + String.valueOf(inferredValue), Toast.LENGTH_LONG).show();
                break;
                default:
                    break;
        }

        return inferredValue;
    }

    private void apiTimeSeriesCall(String symbol){

        if (!isOnline()) {

            String title = "Connection";
            String message = "No internet connection. Please try again.";
            displayMessage(title, message);
        }

        mProgressBar.setVisibility(View.VISIBLE);

        ApiInterface apiInterface = ApiClient.getStockTimeSeries().create(ApiInterface.class);

        Call<TimeSeriesQuote> callToApi = apiInterface.getCompanyTimeSeries(TIME_SERIES_DAILY,symbol, OUTPUT_SIZE, API_KEY);

        callToApi.enqueue(new Callback<TimeSeriesQuote>() {
            @Override
            public void onResponse(Call<TimeSeriesQuote> call, Response<TimeSeriesQuote> response) {
                TimeSeriesQuote timeSeriesQuote = response.body();

                mProgressBar.setVisibility(View.INVISIBLE);

                if (timeSeriesQuote == null) {

                    ResponseBody responseBody = response.errorBody();
                    String errorTitle;
                    String errorMessage;
                    if (responseBody != null) {
                        errorTitle = "Error";
                        errorMessage = "An error occurred.";
                    } else {
                        errorTitle = "Error";
                        errorMessage = "No data Received.";
                    }
                    displayMessage(errorTitle, errorMessage);
                } else {
                    ArrayList<JsonElement> individualItems =  timeSeriesQuote.parseValues(timeSeriesQuote.getResults());

                    getStockTimeSeries(individualItems);
                }
            }

            @Override
            public void onFailure(Call<TimeSeriesQuote> call, Throwable t) {

                mProgressBar.setVisibility(View.INVISIBLE);
                String errorTitle = "Error";
                String errorMessage = "Data request failed.";
                displayMessage(errorTitle, errorMessage);
            }
        });
    }

    private void getStockTimeSeries(ArrayList<JsonElement> individualItems){
        ArrayList<Double> openList = new ArrayList<>();
        ArrayList<Double> highList = new ArrayList<>();
        ArrayList<Double> lowList = new ArrayList<>();
        ArrayList<Double> closeList = new ArrayList<>();
        ArrayList<Double> volumeList = new ArrayList<>();


        allItems.addAll(individualItems);

        for (JsonElement jsonElement : individualItems ) {
            double open = Double.parseDouble(jsonElement.getAsJsonObject().get("1. open").getAsString());
            //Log.w(TAG, " Open is "+String.valueOf(open));
            openList.add(open);


            double high = Double.parseDouble(jsonElement.getAsJsonObject().get("2. high").getAsString());
            highList.add(high);

            double low = Double.parseDouble(jsonElement.getAsJsonObject().get("3. low").getAsString());
            lowList.add(low);

            double close = Double.parseDouble(jsonElement.getAsJsonObject().get("4. close").getAsString());
            closeList.add(close);

            double volume = Double.parseDouble(jsonElement.getAsJsonObject().get("5. volume").getAsString());
            volumeList.add(volume);

        }

        openList.add(openPrice);
        highList.add(highPrice);
        lowList.add(lowPrice);
        closeList.add(currentPrice);
        volumeList.add(volumeQuantity);

        double meanOpenPrices = Calculations.calculateMean(openList);
        Log.w(TAG, "Mean open"+ String.valueOf(meanOpenPrices));
        double meanHighPrices = Calculations.calculateMean(highList);
        Log.w(TAG, "Mean high"+String.valueOf(meanHighPrices));
        double meanLowPrices = Calculations.calculateMean(lowList);
        Log.w(TAG, "Mean low"+String.valueOf(meanLowPrices));
        double meanClosePrices = Calculations.calculateMean(closeList);
        Log.w(TAG, "Mean close"+String.valueOf(meanClosePrices));
        double meanVolumeQuantity = Calculations.calculateMean(volumeList);
        Log.w(TAG, "Mean volume"+String.valueOf(meanVolumeQuantity));

        double openStandardDev = Calculations.calculateStandardDeviation(openList);
        Log.w(TAG, "std open"+ String.valueOf(openStandardDev));
        double highStandardDev = Calculations.calculateStandardDeviation(highList);
        Log.w(TAG, "std high "+ String.valueOf(highStandardDev));
        double lowStandardDev = Calculations.calculateStandardDeviation(lowList);
        Log.w(TAG, "std low "+ String.valueOf(lowStandardDev));
        double closeStandardDev = Calculations.calculateStandardDeviation(closeList);
        Log.w(TAG, "std close "+ String.valueOf(closeStandardDev));
        double volumeStandardDev = Calculations.calculateStandardDeviation(volumeList);
        Log.w(TAG, "std vol "+ String.valueOf(volumeStandardDev));

        float openPriceNormalized = (float) ((openPrice - Calculations.calculateMean(openList)) / Calculations.calculateStandardDeviation(openList));
        Log.w(TAG, String.valueOf(openPriceNormalized));

       float  currentPriceNormalized = (float) ((currentPrice - meanClosePrices) / closeStandardDev);
        Log.w(TAG, String.valueOf(currentPriceNormalized));

       float highPriceNormalized = (float) ((highPrice - meanHighPrices) / highStandardDev);
        Log.w(TAG, String.valueOf(highPriceNormalized));

        float lowPriceNormalized = (float) ((lowPrice - meanLowPrices) / lowStandardDev);
        Log.w(TAG, String.valueOf(lowPriceNormalized));

        float volumeQuantityNormalized = (float) ((volumeQuantity - meanVolumeQuantity) / volumeStandardDev);
        Log.w(TAG, String.valueOf(volumeQuantityNormalized));

        float predictedPrice = doInference(openPriceNormalized, highPriceNormalized, lowPriceNormalized, currentPriceNormalized, volumeQuantityNormalized);

        Log.w(TAG, "Predicted price: " + String.valueOf(predictedPrice));



    }


    private String inputStreamToString(InputStream inputStream){
        try {
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes, 0, bytes.length);
            String json = new String(bytes);
            return json;
        } catch (IOException e){
            return null;
        }
    }


private String companyName(int spinnerPosition){
        String companyName = "";
        switch (spinnerPosition){
            case 0:
                companyName = "MSFT";
                break;
            case 1:
                companyName = "GOOGL";
                break;
            case 2:
                companyName = "TSLA";
                break;
            case 3:
                companyName = "WMT";
                break;
            case 4:
                companyName = "PZ";
                break;
            case 5:
                companyName = "AAPL";
                break;
            case 6:
                companyName = "GS";

        }
        return companyName;
}

private void loadCompanyFromDatabase(){
    mCompanyAdapter = new CompanyAdapter(getActivity(), singleCursor);

   retrieveParticularCompany(spinnerPosition + 1);
}

    private void retrieveParticularCompany(int position){
        if(singleCursor!= null) singleCursor.close();
        singleCursor = getSingleCompany(position);

        int indexCompanyOpen = singleCursor.getColumnIndex(CompanyContract.CompanyEntry.COLUMN_COMPANY_OPEN);
        int indexCompanyHigh = singleCursor.getColumnIndex(CompanyContract.CompanyEntry.COLUMN_COMPANY_HIGH);
        int indexCompanyLow = singleCursor.getColumnIndex(CompanyContract.CompanyEntry.COLUMN_COMPANY_LOW);
        int indexCompanyVolume = singleCursor.getColumnIndex(CompanyContract.CompanyEntry.COLUMN_COMPANY_VOLUME);
        int indexCompanyPrice  = singleCursor.getColumnIndex(CompanyContract.CompanyEntry.COLUMN_COMPANY_PRICE);
        int indexCompanyChangePercent  = singleCursor.getColumnIndex(CompanyContract.CompanyEntry.COLUMN_COMPANY_CHANGE_PERCENT);




        if (singleCursor != null) {

            while (singleCursor.moveToNext()) {

                openPriceText = singleCursor.getString(indexCompanyOpen);
                openPrice = Double.parseDouble(openPriceText);

                highPriceText = singleCursor.getString(indexCompanyHigh);
                highPrice = Double.parseDouble(highPriceText);

                lowPriceText = singleCursor.getString(indexCompanyLow);
                lowPrice = Double.parseDouble(lowPriceText);

                volumeText = singleCursor.getString(indexCompanyVolume);
                volumeQuantity = Double.parseDouble(volumeText);

                changePercentText = singleCursor.getString(indexCompanyChangePercent);

                priceText = singleCursor.getString(indexCompanyPrice);
                currentPrice = Double.parseDouble(priceText);

                updateViews();

            }
        } else {

            // Insert code here to report an error if the cursor is null or the provider threw an exception.
        }
    }




    /*public void setAllItems(ArrayList<JsonElement> allItems) {
        HomeFragment.allItems = myModel.parseValues(myModel.getResults());
    }*/
}


