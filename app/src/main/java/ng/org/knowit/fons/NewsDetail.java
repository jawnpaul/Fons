package ng.org.knowit.fons;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.app.NavUtils;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ng.org.knowit.fons.Models.NewsItem;
import ng.org.knowit.fons.Utility.GlideApp;

public class NewsDetail extends AppCompatActivity {

    NewsItem mNewsItem;
    TextView newsTitleTextView, newsContentTextView, newsAuthorTextView;
    ImageView newsImageView;

    private String newsUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        Toolbar toolbar = findViewById(R.id.newsDetailToolbar);
        setSupportActionBar(toolbar);

        newsContentTextView = findViewById(R.id.newsDetailContentTextView);
        newsTitleTextView = findViewById(R.id.newsDetailTitleText);
        newsImageView = findViewById(R.id.newsDetailImageView);
        newsAuthorTextView = findViewById(R.id.newsDetailAuthor);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)){
            mNewsItem = intent.getParcelableExtra(Intent.EXTRA_TEXT);
            setUpViews(mNewsItem);
        } else if (intent != null ){
            newsAuthorTextView.setText(intent.getStringExtra("author"));
            newsUrl = intent.getStringExtra("newsUrl");
            newsContentTextView.setText(intent.getStringExtra("newsContent"));
            newsTitleTextView.setText(intent.getStringExtra("newsTitle"));
            //GlideApp.with(this).load(intent.getStringExtra("newsImageUrl")).into(newsImageView);
            GlideApp.with(this)
                    .load(intent.getStringExtra("newsImageUrl"))
                    .placeholder(R.drawable.picture_loading)
                    .error(R.drawable.error)
                    .onlyRetrieveFromCache(true)
                    .into(newsImageView);

        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, newsUrl);
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "Share on"));
            }
        });

        final CollapsingToolbarLayout mCollapsingToolbarLayout = findViewById(R.id.newsDetailCollapsingToolbar);
        AppBarLayout mAppBarLayout = findViewById(R.id.newsDetailAppBar);

        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int scrollRange = -1;
                //Initialize the size of the scroll
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                //Check if the view is collapsed
                if (scrollRange + verticalOffset == 0) {
                    mCollapsingToolbarLayout.setTitle("News Detail");

                } else {
                    mCollapsingToolbarLayout.setTitle(" ");

                }

            }

        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent parentIntent = NavUtils.getParentActivityIntent(this);
                parentIntent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(parentIntent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);

    }

    private void setUpViews(NewsItem newsItem){
        String newsTitle = newsItem.getTitle();
        newsTitleTextView.setText(newsTitle);

        String newsContent = newsItem.getContent();
        newsUrl = newsItem.getUrl();
        newsContentTextView.setText(newsContent);

        String newsAuthor = newsItem.getAuthor();
        newsAuthorTextView.setText(newsAuthor);

        String imageUrl = newsItem.getUrlToImage();
        GlideApp.with(this).load(imageUrl).into(newsImageView);
    }
}
