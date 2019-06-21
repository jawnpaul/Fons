package ng.org.knowit.fons.Adapters;

import android.content.Context;
import android.database.Cursor;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.chip.Chip;

import ng.org.knowit.fons.Data.CompanyContract;
import ng.org.knowit.fons.R;

public class AllCompanyAdapter extends RecyclerView.Adapter<AllCompanyAdapter.AllCompanyDatabaseViewHolder> {

    private final Context mContext;
    private Cursor mCursor;


    public AllCompanyAdapter(Context context, Cursor cursor) {
        this.mContext = context;
        this.mCursor = cursor;
    }


    @Override
    public AllCompanyDatabaseViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.company_item, parent, false);
        return new AllCompanyDatabaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder( AllCompanyDatabaseViewHolder holder,
            int position) {
        if (!mCursor.moveToPosition(position))
            return;

        String currentPrice = mCursor.getString(mCursor.getColumnIndex(CompanyContract.CompanyEntry.COLUMN_COMPANY_PRICE));
        String openPrice = mCursor.getString(mCursor.getColumnIndex(CompanyContract.CompanyEntry.COLUMN_COMPANY_OPEN));
        String highPrice = mCursor.getString(mCursor.getColumnIndex(CompanyContract.CompanyEntry.COLUMN_COMPANY_HIGH));
        String lowPrice = mCursor.getString(mCursor.getColumnIndex(CompanyContract.CompanyEntry.COLUMN_COMPANY_LOW));
        String volume = mCursor.getString(mCursor.getColumnIndex(CompanyContract.CompanyEntry.COLUMN_COMPANY_VOLUME));
        String changePercent = mCursor.getString(mCursor.getColumnIndex(CompanyContract.CompanyEntry.COLUMN_COMPANY_CHANGE_PERCENT));

        String companySymbol = mCursor.getString(
                mCursor.getColumnIndex(CompanyContract.CompanyEntry.COLUMN_COMPANY_SYMBOL));


        holder.priceTextView.setText(currentPrice);
        holder.highPriceTextView.setText(highPrice);
        holder.changePercentTextView.setText(changePercent);
        if (changePercent.contains("-")){
            holder.changePercentTextView.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
        } else {
            holder.changePercentTextView.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
        }
        holder.lowPriceTextView.setText(lowPrice);
        holder.volumeTextView.setText(volume);
        holder.openPriceTextView.setText(openPrice);
        holder.itemChip.setText(companySymbol);

    }



    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {

        // Always close the previous mCursor first
        if (mCursor != null) mCursor.close();

        mCursor = newCursor;

        if (newCursor != null) {
            // Force the RecyclerView to refresh
            this.notifyDataSetChanged();
        }
    }

    class AllCompanyDatabaseViewHolder extends RecyclerView.ViewHolder{
        final TextView priceTextView, openPriceTextView, highPriceTextView,
        lowPriceTextView, volumeTextView, changePercentTextView;

        final Chip itemChip;

        public AllCompanyDatabaseViewHolder(View itemView) {
            super(itemView);
            priceTextView = itemView.findViewById(R.id.textViewStockPrice);
            openPriceTextView = itemView.findViewById(R.id.textViewOpenPrice);
            highPriceTextView = itemView.findViewById(R.id.textViewHighPrice);
            lowPriceTextView = itemView.findViewById(R.id.textViewLowPrice);
            volumeTextView = itemView.findViewById(R.id.textViewVolumeQuantity);
            changePercentTextView = itemView.findViewById(R.id.textViewPercentage);

            itemChip = itemView.findViewById(R.id.companyItemChip);
        }
    }
}
