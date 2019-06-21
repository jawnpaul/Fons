package ng.org.knowit.fons;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.model.SliderPage;

public class IntroActivity extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setFadeAnimation();

        /*setFadeAnimation();
        setZoomAnimation();
        setFlowAnimation();
        setSlideOverAnimation();
        setDepthAnimation();*/

        // Instead of fragments, you can also use our default slide.
        // Just create a `SliderPage` and provide title, description, background and image.
        // AppIntro will do the rest.
        SliderPage sliderPage = new SliderPage();
        sliderPage.setTitle("Quick Intro");
        sliderPage.setDescription("A quick introduction of the app amazing features");
        sliderPage.setImageDrawable(R.drawable.app_installation);
        sliderPage.setBgColor(Color.parseColor("#1976D2"));
        addSlide(AppIntroFragment.newInstance(sliderPage));

        SliderPage sliderPage2 = new SliderPage();
        sliderPage2.setTitle("Predicting");
        sliderPage2.setDescription("Predict the stock prices of your favourite company");
        sliderPage2.setImageDrawable(R.drawable.ic_undraw_predictive_analytic);
        sliderPage2.setBgColor(Color.parseColor("#1976D2"));
        addSlide(AppIntroFragment.newInstance(sliderPage2));


        SliderPage sliderPage3 = new SliderPage();
        sliderPage3.setTitle("Trending");
        sliderPage3.setDescription("Get valuable insight into how companies are performing");
        sliderPage3.setImageDrawable(R.drawable.ic_undraw_investing);
        sliderPage3.setBgColor(Color.parseColor("#1976D2"));
        addSlide(AppIntroFragment.newInstance(sliderPage3));

        SliderPage sliderPage4 = new SliderPage();
        sliderPage4.setTitle("Graph");
        sliderPage4.setDescription("Real time graph showing movement of prices");
        sliderPage4.setImageDrawable(R.drawable.ic_undraw_financial_data);
        sliderPage4.setBgColor(Color.parseColor("#1976D2"));
        addSlide(AppIntroFragment.newInstance(sliderPage4));

        SliderPage sliderPage5 = new SliderPage();
        sliderPage5.setTitle("News");
        sliderPage5.setDescription("Up-to-date financial news");
        sliderPage5.setImageDrawable(R.drawable.ic_undraw_news);
        sliderPage5.setBgColor(Color.parseColor("#1976D2"));
        addSlide(AppIntroFragment.newInstance(sliderPage5));


        SliderPage sliderPage6 = new SliderPage();
        sliderPage6.setTitle("Share news");
        sliderPage6.setDescription("Enhance the spread of information through sharing");
        sliderPage6.setImageDrawable(R.drawable.ic_undraw_social_share);
        sliderPage6.setBgColor(Color.parseColor("#1976D2"));
        addSlide(AppIntroFragment.newInstance(sliderPage6));

        SliderPage sliderPage7 = new SliderPage();
        sliderPage7.setTitle("Yaay!!");
        sliderPage7.setDescription("That's it, you can start using. Enjoy!");
        sliderPage7.setImageDrawable(R.drawable.ic_undraw_finish_line);
        sliderPage7.setBgColor(Color.parseColor("#1976D2"));
        addSlide(AppIntroFragment.newInstance(sliderPage7));

        /*SliderPage sliderPage8 = new SliderPage();
        sliderPage8.setTitle("I don't know which title");
        sliderPage8.setDescription("In here goes the description of the error mango");
        sliderPage8.setImageDrawable(R.drawable.error);
        sliderPage8.setBgColor(Color.parseColor("#1976D2"));
        addSlide(AppIntroFragment.newInstance(sliderPage8));*/

        // OPTIONAL METHODS
        // Override bar/separator color.
        setBarColor(Color.parseColor("#3F51B5"));
        setSeparatorColor(Color.parseColor("#2196F3"));

        // Hide Skip/Done button.
        showSkipButton(true);
        setProgressButtonEnabled(true);

        // Turn vibration on and set intensity.
        // NOTE: you will probably need to ask VIBRATE permission in Manifest.
        setVibrate(false);
        setVibrateIntensity(30);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        // Do something when users tap on Skip button.

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        // Do something when users tap on Done button.

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }
}



