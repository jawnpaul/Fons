package ng.org.knowit.fons.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonArray;

import ng.org.knowit.fons.Models.NewsItem;
import ng.org.knowit.fons.R;



public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private final Context mContext;
    private final JsonArray newsItems;

    public NewsAdapter(Context context, JsonArray NewsItems) {
        this.newsItems = NewsItems;
        this.mContext = context;
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


        //holder.titleTextView.setText(newsItems.get(position).);

    }

    @Override
    public int getItemCount() {
        return 0;
    }


    class NewsViewHolder extends RecyclerView.ViewHolder{
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
        }

        public void bind(int position){
            //NewsItem newsItem = (NewsItem) newsItems.get(position);
        }
    }
}
