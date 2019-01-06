package ng.org.knowit.fons.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.JsonArray;

import java.util.ArrayList;
import java.util.List;

import ng.org.knowit.fons.Models.NewsItem;
import ng.org.knowit.fons.R;
import ng.org.knowit.fons.Utility.GlideApp;
import ng.org.knowit.fons.Utility.ImageUtility;



public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    Context mContext;
    ArrayList<NewsItem> mNewsItems;
    private final OnListItemClickListener mOnListItemClickListener;

    private static final String TAG = NewsAdapter.class.getSimpleName();

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

                //Glide.with(mContext).asBitmap().load(imageUrl).into(newsImageView);
                GlideApp.with(mContext)
                        .asBitmap()
                        .load(imageUrl)
                        //TODO: Change the drawable for placeholder(while image is still loading) and error
                        .placeholder(R.drawable.app_installation)
                        .error(R.drawable.ic_menu)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .listener(new RequestListener<Bitmap>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                    Target<Bitmap> target,
                                    boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Bitmap resource, Object model,
                                    Target<Bitmap> target,
                                    DataSource dataSource, boolean isFirstResource) {

                                if (resource != null){

                                    Palette palette = Palette.from(resource).generate();
                                    int defaultValue = 0x000000;
                                   titleTextView.setTextColor(ImageUtility.manipulateColor(palette.getVibrantColor(defaultValue),10f));

                                }

                                return false;
                            }
                        }).into(newsImageView);

            }
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mOnListItemClickListener.onListItemClick(position);

        }
    }


}
