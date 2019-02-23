package ng.org.knowit.fons.Adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ng.org.knowit.fons.Data.NewsContract;
import ng.org.knowit.fons.NewsDetail;
import ng.org.knowit.fons.R;
import ng.org.knowit.fons.Utility.GlideApp;

public class NewsDatabaseAdapter extends RecyclerView.Adapter<NewsDatabaseAdapter.NewsDatabaseViewHolder> {



    private final String TAG = NewsDatabaseAdapter.class.getCanonicalName();
    private final Context mContext;
    private Cursor mCursor;

    private Cursor singleCursor;

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
    public void onBindViewHolder(@NonNull final NewsDatabaseViewHolder holder, int position) {
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


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                retrieveParticularNews(holder.getAdapterPosition() + 1);

            }
        });

    }


    @Override
    public int getItemCount() {
        return mCursor.getCount();

    }

    private Cursor getSingleNews(int position) {

       return mContext.getContentResolver().query(NewsContract.buildSingleNews(position),
                null,
                null,
                null,
                null); }

    private void retrieveParticularNews(int position){
        if(singleCursor!= null) singleCursor.close();
        singleCursor = getSingleNews(position);

        int indexAuthor = singleCursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_AUTHOR);
        int indexTitle = singleCursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_TITLE);
        int indexContent = singleCursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_CONTENT);
        int indexImageUrl = singleCursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_URL_TO_IMAGE);
        int indexNewsUrl  = singleCursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_URL);


        if (singleCursor != null) {

            while (singleCursor.moveToNext()) {

                String author = singleCursor.getString(indexAuthor);
                String title = singleCursor.getString(indexTitle);
                String content = singleCursor.getString(indexContent);
                String imageUrl = singleCursor.getString(indexImageUrl);
                String newsUrl = singleCursor.getString(indexNewsUrl);

                sendIntentForNewsDetail(author, content, title, imageUrl, newsUrl);

                // end of while loop
            }
        } else {

            // Insert code here to report an error if the cursor is null or the provider threw an exception.
        }
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

    private void sendIntentForNewsDetail(String author, String content, String title, String imageUrl, String newsUrl){
        Bundle bundle = new Bundle();
        bundle.putString("author", author);
        bundle.putString("newsContent", content);
        bundle.putString("newsTitle", title);
        bundle.putString("newsImageUrl", imageUrl);
        bundle.putString("newsUrl", newsUrl);


        Intent intent = new Intent(mContext, NewsDetail.class);
        intent.putExtras(bundle);
        mContext.startActivity(intent);

    }
}
