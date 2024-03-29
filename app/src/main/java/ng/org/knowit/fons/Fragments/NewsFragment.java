package ng.org.knowit.fons.Fragments;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;

import ng.org.knowit.fons.Adapters.NewsAdapter;
import ng.org.knowit.fons.Adapters.NewsDatabaseAdapter;
import ng.org.knowit.fons.Data.NewsContract;
import ng.org.knowit.fons.Data.NewsDbHelper;
import ng.org.knowit.fons.Data.NewsUpdateService;
import ng.org.knowit.fons.Main2Activity;
import ng.org.knowit.fons.Models.NewsItem;
import ng.org.knowit.fons.Models.NewsQuote;
import ng.org.knowit.fons.NewsDetail;
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
 * {@link //NewsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsFragment extends Fragment implements NewsAdapter.OnListItemClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = NewsFragment.class.getCanonicalName();
    private static final String BUSINESS_INSIDER = "business-insider";
    private static final String API_KEY = "e3345a05e8814c36ba5d8939f12c1604";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String urlToImage;
    //private byte[] imageByteArray;

    private RecyclerView mRecyclerView;
    private Context mContext;
    private NewsAdapter mNewsAdapter;

    private NewsDatabaseAdapter mNewsDatabaseAdapter;
    private SQLiteDatabase mSQLiteDatabase;
    private  Cursor mCursor;

    private SwipeRefreshLayout mSwipeRefreshLayout;


    Toolbar toolbar;
    ProgressBar mProgressBar;
    ArrayList<NewsItem> mNewsItems;

    //private OnFragmentInteractionListener mListener;

    public NewsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewsFragment newInstance(String param1, String param2) {
        NewsFragment fragment = new NewsFragment();
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

        if(mCursor!= null) mCursor.close();
        mContext = getContext();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        NewsDbHelper dbHelper = new NewsDbHelper(getActivity());
        mSQLiteDatabase = dbHelper.getWritableDatabase();

        if(mCursor!= null) mCursor.close();
        mCursor = getAllNews();
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        mRecyclerView = view.findViewById(R.id.newsRecyclerView);

        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);

        if (isOnline() &&  mCursor.getCount() <= 0){
            makeApiCall();
            mProgressBar = view.findViewById(R.id.news_progress_bar);
        } else{

            loadNewsFromDatabase();
        }


        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                makeApiCall();

            }
        });


    return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        toolbar.setTitle("News ");
        ((Main2Activity)getActivity()).setToolbar(toolbar);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar = view.findViewById(R.id.toolbar);
        Log.d("News Fragment", "Toolbar created");

    }


    public void makeApiCall(){

        if (!isOnline()) {

            String title = "Connection";
            String message = "No internet connection. Please try again.";
            displayMessage(title, message);
        }

       // mProgressBar.setVisibility(View.VISIBLE);

        ApiInterface apiInterface = ApiClient.getNewsClient().create(ApiInterface.class);

        Call<NewsQuote> callToApi = apiInterface.getNewsQuote(BUSINESS_INSIDER, API_KEY);

        callToApi.enqueue(new Callback<NewsQuote>() {
            @Override
            public void onResponse(Call<NewsQuote> call, Response<NewsQuote> response) {
                NewsQuote newsQuote = response.body();

                //mProgressBar.setVisibility(View.INVISIBLE);

                if (newsQuote == null) {

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

                    mNewsItems = newsQuote.getResults();
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    mNewsAdapter = new NewsAdapter(getActivity(), newsQuote.getResults(), NewsFragment.this);
                    mRecyclerView.setAdapter(mNewsAdapter);
                    mSwipeRefreshLayout.setRefreshing(false);

                    Log.d(TAG, String.valueOf(newsQuote.getResults().size()));

                    getNewsItem(newsQuote);
                }
            }


            @Override
            public void onFailure(Call<NewsQuote> call, Throwable t) {
                //mProgressBar.setVisibility(View.INVISIBLE);
                mSwipeRefreshLayout.setRefreshing(false);
                String errorTitle = "Error";
                String errorMessage = "Data request failed.";
                displayMessage(errorTitle, errorMessage);
                mProgressBar.setVisibility(View.INVISIBLE);
                //TODO: Set a refresh button
            }
        });

    }

    private void getNewsItem(NewsQuote newsQuote){

        for (int i = 0; i <=9 ; i++) {
            NewsItem newsItem = newsQuote.getResults().get(i);
            final String author = newsItem.getAuthor();
            final String title = newsItem.getTitle();
            final String description = newsItem.getDescription();
            final String content = newsItem.getContent();
            final String publishedAt = newsItem.getPublishedAt();
            final String url = newsItem.getUrl();
            String urlToImage = newsItem.getUrlToImage();


            if(mCursor.getCount() <= 0){
                saveNewsToDatabase(author, title, description, content, publishedAt, url, urlToImage);
            } else {
                updateNews(i, author, title, description, content, publishedAt, url, urlToImage );
            }


        }
    }

    private void saveNewsToDatabase(String author, String title, String description, String content,
            String publishedAt, String url, String imageUrl){
        ContentValues contentValues = new ContentValues();
        contentValues.put(NewsContract.NewsEntry.COLUMN_AUTHOR, author);
        contentValues.put(NewsContract.NewsEntry.COLUMN_CONTENT, content);
        contentValues.put(NewsContract.NewsEntry.COLUMN_DESCRIPTION, description);
        contentValues.put(NewsContract.NewsEntry.COLUMN_TITLE, title);
        contentValues.put(NewsContract.NewsEntry.COLUMN_PUBLISHED_AT, publishedAt);
        contentValues.put(NewsContract.NewsEntry.COLUMN_URL, url);
        contentValues.put(NewsContract.NewsEntry.COLUMN_URL_TO_IMAGE, imageUrl);

        Uri uri = mContext.getContentResolver().insert(NewsContract.CONTENT_URI, contentValues);


        if (uri != null){
            Log.d(TAG, uri.toString());
        }


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

    // TODO: Rename method, update argument and hook method into UI event

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((Main2Activity)getActivity()).setToolbar(null);
        super.onDestroyView();
    }

    @Override
    public void onListItemClick(int position) {

        Intent intent = new Intent(getActivity(), NewsDetail.class);
        NewsItem newsItem = mNewsItems.get(position);
        intent.putExtra(Intent.EXTRA_TEXT, newsItem);

        startActivity(intent);
    }

    private void loadNewsFromDatabase(){


        mNewsDatabaseAdapter = new NewsDatabaseAdapter(getActivity(), mCursor);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mRecyclerView.setAdapter(mNewsDatabaseAdapter);


        //mCursor.close();
    }

    private void updateNews(int i, String author, String title, String description, String content,
            String publishedAt, String url, String imageUrl){
        Uri uriForNewsItem = NewsContract.buildSingleNews(i+1);

        ContentValues contentValues = new ContentValues();
        contentValues.put(NewsContract.NewsEntry.COLUMN_AUTHOR, author);
        contentValues.put(NewsContract.NewsEntry.COLUMN_CONTENT, content);
        contentValues.put(NewsContract.NewsEntry.COLUMN_DESCRIPTION, description);
        contentValues.put(NewsContract.NewsEntry.COLUMN_TITLE, title);
        contentValues.put(NewsContract.NewsEntry.COLUMN_PUBLISHED_AT, publishedAt);
        contentValues.put(NewsContract.NewsEntry.COLUMN_URL, url);
        contentValues.put(NewsContract.NewsEntry.COLUMN_URL_TO_IMAGE, imageUrl);

        NewsUpdateService.updateNewsItem(mContext, uriForNewsItem, contentValues);

    }

    private Cursor getAllNews() {
        return mSQLiteDatabase.query(NewsContract.NewsEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                NewsContract.NewsEntry._ID); }



    /*public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    *//**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     *//*
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/

    // TODO: Add transitions
}
