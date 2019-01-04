package ng.org.knowit.fons.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ng.org.knowit.fons.Data.NewsContract;
import ng.org.knowit.fons.R;
import ng.org.knowit.fons.Utility.GlideApp;
import ng.org.knowit.fons.Utility.ImageUtility;

public class NewsDatabaseAdapter extends RecyclerView.Adapter<NewsDatabaseAdapter.NewsDatabaseViewHolder> {

    private final String TAG = NewsDatabaseAdapter.class.getCanonicalName();
    private final Context mContext;
    private Cursor mCursor;

    public NewsDatabaseAdapter(Context context, Cursor cursor){
        this.mContext = context;
        this.mCursor = cursor;

    }


    @NonNull
    @Override
    public NewsDatabaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.news_item, parent, false);

        return new NewsDatabaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsDatabaseViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position))
            return;

        String author  = mCursor.getString(mCursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_AUTHOR));
        String title = mCursor.getString(mCursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_TITLE));
        String newsImageUrl = mCursor.getString(mCursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_URL_TO_IMAGE));


        holder.authorTextView.setText(author);
        holder.titleTextView.setText(title);
        GlideApp.with(mContext)
                .load(newsImageUrl)
                //TODO: Change the drawable for placeholder(while image is still loading) and error
                .placeholder(R.drawable.app_installation)
                .error(R.drawable.ic_menu)
                .onlyRetrieveFromCache(true)
                .into(holder.newsImageView);

    }


    @Override
    public int getItemCount() {
        return mCursor.getCount();

    }


    class NewsDatabaseViewHolder extends RecyclerView.ViewHolder{
        final TextView authorTextView, titleTextView;
        /*, descriptionTextView,
        urlToImageTextView, publishedAtTextView, contentTextView;*/
        final ImageView newsImageView;

        public NewsDatabaseViewHolder(View itemView) {
            super(itemView);
            authorTextView = itemView.findViewById(R.id.authorNameTextView);
            titleTextView = itemView.findViewById(R.id.newsTitleTextView);
            newsImageView = itemView.findViewById(R.id.newsImageView);
            /*lowPriceTextView = itemView.findViewById(R.id.textViewLowPrice);
            volumeTextView = itemView.findViewById(R.id.textViewVolumeQuantity);
            changePercentTextView = itemView.findViewById(R.id.textViewPercentage);*/
        }

    }
}
