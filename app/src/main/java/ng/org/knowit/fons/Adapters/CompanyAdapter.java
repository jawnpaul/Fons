package ng.org.knowit.fons.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import ng.org.knowit.fons.Data.CompanyContract;
import ng.org.knowit.fons.R;

public class CompanyAdapter extends CursorAdapter {

    private Cursor mCursor;
    private final Context mContext;

    private LayoutInflater mLayoutInflater;
    private static final String TAG = CompanyAdapter.class.getSimpleName();
    public CompanyAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        this.mCursor = cursor;
        this.mContext = context;
        mLayoutInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);

    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return mLayoutInflater.inflate(R.layout.fragment_home, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView priceTextView = view.findViewById(R.id.textViewStockPrice);
        String price = cursor.getString(cursor.getColumnIndex(CompanyContract.CompanyEntry.COLUMN_COMPANY_PRICE));
        //priceTextView.setText(price);
        String pr = CompanyContract.getColumnString(mCursor, CompanyContract.CompanyEntry.COLUMN_COMPANY_SYMBOL);
        Log.d(TAG, pr);
        TextView  changePercentTextView = view.findViewById(R.id.textViewPercentage);
        TextView openPriceTextView = view.findViewById(R.id.textViewOpenPrice);
        TextView highPriceTextView = view.findViewById(R.id.textViewHighPrice);
        TextView lowPriceTextView = view.findViewById(R.id.textViewLowPrice);
        TextView volumeTextView = view.findViewById(R.id.textViewVolumeQuantity);
    }

    public void swappCursor(Cursor newCursor) {

        // Always close the previous mCursor first
        if (mCursor != null) mCursor.close();

        mCursor = newCursor;

        /*if (newCursor != null) {
            // Force the RecyclerView to refresh
            this.notifyDataSetChanged();
        }*/
    }
}
