package ng.org.knowit.fons.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.JsonArray;

import java.util.ArrayList;
import java.util.List;

import ng.org.knowit.fons.Models.NewsItem;
import ng.org.knowit.fons.R;



public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    Context mContext;
    ArrayList<NewsItem> mNewsItems;
    private final OnListItemClickListener mOnListItemClickListener;

    public NewsAdapter(Context context, ArrayList<NewsItem> NewsItems, OnListItemClickListener onListItemClickListener) {
        this.mContext = context;
        this.mNewsItems = NewsItems;
        this.mOnListItemClickListener = onListItemClickListener;
    }

    public interface OnListItemClickListener {
        void onListItemClick(int position);
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.news_item, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {

        holder.bind(position);

    }

    @Override
    public int getItemCount() {
        return mNewsItems.size();
    }


    class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        final TextView authorTextView, titleTextView;
                /*, descriptionTextView,
                urlToImageTextView, publishedAtTextView, contentTextView;*/
        final ImageView newsImageView;

        public NewsViewHolder(View itemView) {
            super(itemView);
            authorTextView = itemView.findViewById(R.id.authorNameTextView);
            titleTextView = itemView.findViewById(R.id.newsTitleTextView);
            newsImageView = itemView.findViewById(R.id.newsImageView);
            /*lowPriceTextView = itemView.findViewById(R.id.textViewLowPrice);
            volumeTextView = itemView.findViewById(R.id.textViewVolumeQuantity);
            changePercentTextView = itemView.findViewById(R.id.textViewPercentage);*/

            itemView.setOnClickListener(this);
        }

        public void bind(int position){
            NewsItem newsItem = mNewsItems.get(position);

            authorTextView.setText(newsItem.getAuthor());
            titleTextView.setText(newsItem.getTitle());

            String imageUrl = newsItem.getUrlToImage();
            if (imageUrl != null){
                Glide.with(mContext).load(imageUrl).into(newsImageView);
            } else {
                Glide.with(mContext).load(R.drawable.app_installation).into(newsImageView);
            }

        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mOnListItemClickListener.onListItemClick(position);
        }
    }
}
