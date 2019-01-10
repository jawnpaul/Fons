package ng.org.knowit.fons;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        doInference = findViewById(R.id.button_do_inference);
        showResult = findViewById(R.id.showResult);

        try {
            tflite = new Interpreter(loadModelFile());
            Log.d(TAG, "model file loaded");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        doInference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                startActivity(intent);
               // float prediction = doInference();
                //showResult.setText(Float.toString(prediction));
            }
        });
    }

    public float doInference (){
        float [][] inputVal = {{(float) 1.5300144, (float) 1.49734975, (float) 1.55317365, (float) 1.53696703,
                (float) -0.78733775}};

        float [][] outputVal = new float[1][1];

        tflite.run(inputVal, outputVal);

        float inferredValue = outputVal[0][0];

        return inferredValue;

    }

    private MappedByteBuffer loadModelFile() throws IOException {
        AssetFileDescriptor fileDescriptor = this.getAssets().openFd("model.tflite");
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffSet = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffSet, declaredLength);
    }

}
