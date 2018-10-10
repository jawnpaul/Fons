package ng.org.knowit.fons.Fragments;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ng.org.knowit.fons.Adapters.AllCompanyAdapter;
import ng.org.knowit.fons.Data.CompanyContract;
import ng.org.knowit.fons.Data.CompanyDbHelper;
import ng.org.knowit.fons.Main2Activity;
import ng.org.knowit.fons.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link //AllFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AllFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static final String TAG = AllFragment.class.getCanonicalName();

    private AllCompanyAdapter mAllCompanyAdapter;
    private RecyclerView mRecyclerView;
    private SQLiteDatabase mSQLiteDatabase;
    Toolbar toolbar;
    Context mContext;
    //private OnFragmentInteractionListener mListener;

    public AllFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AllFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AllFragment newInstance(String param1, String param2) {
        AllFragment fragment = new AllFragment();
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
            mContext = getContext();

            CompanyDbHelper dbHelper = new CompanyDbHelper(getActivity());
            mSQLiteDatabase = dbHelper.getWritableDatabase();


            //Cursor cursor = getAllCompany();



           // mAllCompanyAdapter = new AllCompanyAdapter(mContext, cursor);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all, container, false);

        mRecyclerView = view.findViewById(R.id.allCompanyRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        CompanyDbHelper dbHelper = new CompanyDbHelper(getActivity());
        mSQLiteDatabase = dbHelper.getWritableDatabase();


        Cursor cursor = getAllCompany();


        mAllCompanyAdapter = new AllCompanyAdapter(getActivity(), cursor);

        mRecyclerView.setAdapter(mAllCompanyAdapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((Main2Activity)getActivity()).setToolbar(toolbar);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar = view.findViewById(R.id.toolbar);

        //mRecyclerView = view.findViewById(R.id.allCompanyRecyclerView);
        //mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        /*CompanyDbHelper dbHelper = new CompanyDbHelper(mContext);
        mSQLiteDatabase = dbHelper.getWritableDatabase();


        Cursor cursor = getAllCompany();*/


        //mAllCompanyAdapter = new AllCompanyAdapter(mContext, cursor);

        //mRecyclerView.setAdapter(mAllCompanyAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((Main2Activity)getActivity()).setToolbar(null);
        super.onDestroyView();
    }


    private Cursor getAllCompany() {
        return mSQLiteDatabase.query(CompanyContract.CompanyEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                CompanyContract.CompanyEntry.COLUMN_TIMESTAMP);
    }

    // TODO: Rename method, update argument and hook method into UI event
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
}
