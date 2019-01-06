package ng.org.knowit.fons;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import ng.org.knowit.fons.Models.NewsItem;
import ng.org.knowit.fons.Utility.GlideApp;

public class NewsDetail extends AppCompatActivity {

    NewsItem mNewsItem;
    TextView newsTitleTextView, newsContentTextView, newsAuthorTextView;
    ImageView newsImageView;

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
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
        newsContentTextView.setText(newsContent);

        String newsAuthor = newsItem.getAuthor();
        newsAuthorTextView.setText(newsAuthor);

        String imageUrl = newsItem.getUrlToImage();
        GlideApp.with(this).load(imageUrl).into(newsImageView);
    }
}
