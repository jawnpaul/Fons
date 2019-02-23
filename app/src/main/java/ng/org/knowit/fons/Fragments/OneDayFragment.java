package ng.org.knowit.fons.Fragments;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import ng.org.knowit.fons.Models.TimeSeriesQuote;
import ng.org.knowit.fons.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OneDayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OneDayFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "dataList";
    private static final String TAG = OneDayFragment.class.getSimpleName();

    private  List<Entry> entries = new ArrayList<Entry>();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private int mParam2;
    private  String mad;
    private ArrayList<JsonElement> dataList;

    private TextView mTextView;

   // private String title;
    //private int page;

    /*public static OneDayFragment newInstance(int Page, String title) {
        // Required empty public constructor

        OneDayFragment oneDayFragment = new OneDayFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", Page);
        args.putString("someTitle", title);
        oneDayFragment.setArguments(args);
        return oneDayFragment;
    }*/

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OneDayFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OneDayFragment newInstance(String param1, int param2) {
        OneDayFragment fragment = new OneDayFragment();

        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putInt(ARG_PARAM2, param2);

        //args.putSerializable(ARG_PARAM3, dataList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getInt(ARG_PARAM2);

            String myjson = inputStreamToString(getContext().getResources().openRawResource(R.raw.my_json));

            TimeSeriesQuote myModel = new Gson().fromJson(myjson, TimeSeriesQuote.class);

            dataList = myModel.parseValues(myModel.getResults());
            Log.w(TAG, "size from local json "+String.valueOf(dataList.size()));


            for (float i =0; i <dataList.size();i++) {
                JsonElement data = dataList.get((int) i);
                entries.add(new Entry(i, Float.parseFloat(data.getAsJsonObject().get("4. close").getAsString())));
            }



            //dataList = (ArrayList<JsonElement>) getArguments().getSerializable(ARG_PARAM3);
            Log.d("One Day Fragment:param1", mParam1);
            //Toast.makeText(getActivity(), mParam1, Toast.LENGTH_SHORT).show();



        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_one_day, container, false);

        View view = inflater.inflate(R.layout.fragment_one_day, container, false);
        //TextView tvLabel = (TextView) view.findViewById(R.id.textViewOneDay);

       // tvLabel.setText(mParam1 + " -- " + mParam2);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        //Toast.makeText(getActivity(), mParam1, Toast.LENGTH_SHORT).show();

        TextView tvLabel = (TextView) view.findViewById(R.id.textViewHelloOneDay);

        LineChart lineChart = view.findViewById(R.id.lineChart);

        mTextView = view.findViewById(R.id.textViewPageTitle);
        mTextView.setText(mParam1);

        LineDataSet dataSet = new LineDataSet(entries, "prices"); // add entries to dataset
        dataSet.setColor(getResources().getColor(R.color.colorPrimaryDark));
        dataSet.setValueTextColor(getResources().getColor(R.color.colorPrimaryDark));
        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        //lineChart.invalidate();

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
}
