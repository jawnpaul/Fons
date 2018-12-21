package ng.org.knowit.fons.Fragments;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;

import ng.org.knowit.fons.Adapters.CompanyAdapter;
import ng.org.knowit.fons.Data.CompanyContract;
import ng.org.knowit.fons.Data.CompanyDbHelper;
import ng.org.knowit.fons.Data.CompanyUpdateService;
import ng.org.knowit.fons.Main2Activity;
import ng.org.knowit.fons.Models.GlobalQuote;
import ng.org.knowit.fons.R;
import ng.org.knowit.fons.Rest.ApiClient;
import ng.org.knowit.fons.Rest.ApiInterface;
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

    private static final String GLOBAL_QUOTE = "GLOBAL_QUOTE";
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

    private String[] companyNames;

    private SQLiteDatabase mSQLiteDatabase;

    private int spinnerPosition;
    FragmentPagerAdapter adapterViewPager;

    CompanyAdapter mCompanyAdapter;

    private Cursor mCursor;

    Context mContext;

    Toolbar toolbar;

    Spinner companySpinner;

    ProgressBar mProgressBar;

    TextView priceTextView, openPriceTextView, highPriceTextView,
            lowPriceTextView, volumeTextView, changePercentTextView;
    String priceText, openPriceText, highPriceText, lowPriceText, volumeText, changePercentText, symbolText,
    latestTradingDayText, prevousDayCloseText, changeText;


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

        mCursor = getSpecificCompany();
        mCompanyAdapter = new CompanyAdapter(mContext, mCursor);

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

        ViewPager vpPager = (ViewPager) view.findViewById(R.id.vpPager);
        adapterViewPager = new MyPagerAdapter(getChildFragmentManager());
        vpPager.setAdapter(adapterViewPager);
        insertNestedFragment();


        vpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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

                } else {
                    collapsingToolbarLayout.setTitle(" ");
                    pagerTabStrip.setVisibility(View.VISIBLE);
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
                //makeApiCall(MICROSOFT_SYMBOL);
                break;
            case 1:
                //makeApiCall(GOOGLE_SYMBOL);

                break;
            case 2:
                //makeApiCall(TESLA_SYMBOL);
                break;
            case 3:
                //makeApiCall(WALMART_SYMBOL);
                break;
            case 4:
               // makeApiCall(PZ_SYMBOL);
                break;
            case 5:
              //  makeApiCall(APPLE_SYMBOL);
                break;
            case 6:
              //  makeApiCall(GOLDMAN_SYMBOL);
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
                    int f_param2 = f_bundle.getInt("param2");
                   // Log.d("Home Fragment", f_param1 +" " + f_param2);
                    //return OneDayFragment.newInstance("Page zero", 0);
                    break;
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    //Log.d("Home Fragment", "Second fragment has loaded");
                    oneDayFragment = OneDayFragment.newInstance("Page one",1);
                    //Bundle s_bundle = oneDayFragment.getArguments();
                   // String s_param1 = s_bundle.getString("param1");
                    //int s_param2 = s_bundle.getInt("param2");
                  //  Log.d("Home Fragment", s_param1 +" " + s_param2);
                    break;
                    //return OneDayFragment.newInstance("Page one", 1);
                case 2: // Fragment # 1 - This will show SecondFragment

                    oneDayFragment = OneDayFragment.newInstance("Page Two", 2);
                    //Log.d("Home Fragment", "Third fragment has loaded");
                    break;
                case 3:
                    oneDayFragment = OneDayFragment.newInstance("Page Three", 3);
                    break;
                    //return OneDayFragment.newInstance("page two", 2);
                case 4:
                    oneDayFragment = OneDayFragment.newInstance("Page Four", 4);
                    break;
                default:
                    Log.d("Home Fragment", "Which one? fragment has loaded");
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
                        changePercentText = globalQuote.getCompanyQuote().getChangePercent();
                        openPriceText = globalQuote.getCompanyQuote().getOpenPrice();
                        highPriceText = globalQuote.getCompanyQuote().getHighPrice();
                        lowPriceText = globalQuote.getCompanyQuote().getLowPrice();
                        volumeText = globalQuote.getCompanyQuote().getCurrentVolume();
                        changeText = globalQuote.getCompanyQuote().getChange();
                        symbolText = globalQuote.getCompanyQuote().getCompanySymbol();
                        latestTradingDayText = globalQuote.getCompanyQuote().getLatestTradingDay();
                        prevousDayCloseText = globalQuote.getCompanyQuote().getPreviousClose();
                        updateViews();

                        addNewCompany(symbolText, openPriceText, highPriceText, lowPriceText,
                                priceText, volumeText, latestTradingDayText, prevousDayCloseText, changeText, changePercentText);


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
        if (priceText.contains("-")){
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

        CompanyUpdateService.insertNewCompany(mContext, cv);

        mCompanyAdapter.swappCursor(mCursor);
    }

    private void updateCompany(String companySymbol, String companyOpen,
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

        //CompanyUpdateService.updateCompany(mContext, CompanyContract.getColumnLong(null, null));
    }

    private Cursor getSpecificCompany(){
       /* String id = "1";

        Uri uri = CompanyContract.CONTENT_URI;
        uri = uri.buildUpon().appendPath(id).build();*/

        Log.d(TAG1, CompanyContract.buildSingleCompany(6).toString());
        return mContext.getContentResolver().query(CompanyContract.buildSingleCompany(6),
                null,
                null,
                null,
                CompanyContract.CompanyEntry._ID);
    }
}


