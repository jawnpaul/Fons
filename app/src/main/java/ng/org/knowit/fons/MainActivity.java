package ng.org.knowit.fons;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class MainActivity extends AppCompatActivity {

    Interpreter tflite;
    Button doInference;
    TextView showResult;
    String TAG = "MainActivity";

    Handler mHandler;

    SharedPreferences mSharedPreferences;

    Boolean isFirstStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        isFirstStart = mSharedPreferences.getBoolean("firstStart", true);


        //  Declare a new thread to do a preference check
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                //  Initialize SharedPreferences
                /*SharedPreferences getPrefs = PreferenceManager
                        .getDefaultSharedPreferences(getBaseContext());*/

                //  Create a new boolean and preference and set it to true
                /* boolean isFirstStart = getPrefs.getBoolean("firstStart", true);*/

                //  If the activity has never started before...
                if (isFirstStart) {

                    //  Launch app intro
                    final Intent i = new Intent(MainActivity.this, IntroActivity.class);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(i);
                        }
                    });

                    //  Make a new preferences editor
                    SharedPreferences.Editor e = mSharedPreferences.edit();

                    //  Edit preference to make it false because we don't want this to run again
                    e.putBoolean("firstStart", false);

                    //  Apply changes
                    e.apply();


                }
            }
        });

        // Start the thread
        t.start();
        showSplash(isFirstStart);



    }


    private void showSplash(boolean isFirstStart) {

        if (isFirstStart) {
            finish();
        } else {
            mHandler = new Handler();
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                    startActivity(intent);
                    finish();
                }
            }, 3000);
        }


    }

}
