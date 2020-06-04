package pl.tenk.bullseye;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    TextView timer, comstockFactor, comstockCalculation;
    Button startButton, stopButton, resetButton, addAlphaButton, addCharlieButton, addDeltaButton;
    long milisecondsTime, startTime, bufTime, updateTime = 0L;
    Handler stopwatchHandler;
    int Seconds, Minutes, Miliseconds, updatePoints = 0;
    float Factor = (float) 0.000;
    float timeInSeconds = (float) 0.000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timer = (TextView)findViewById(R.id.text_stopwatchCounter);
        startButton = (Button)findViewById(R.id.button_start);
        stopButton = (Button)findViewById(R.id.button_stop);
        resetButton = (Button)findViewById(R.id.button_reset);
        comstockFactor = (TextView)findViewById(R.id.text_factorValue);
        comstockCalculation = (TextView)findViewById(R.id.text_factor_calculation);
        addAlphaButton = (Button)findViewById(R.id.button_add_alpha);
        addCharlieButton = (Button)findViewById(R.id.button_add_charlie);
        addDeltaButton = (Button)findViewById(R.id.button_add_delta);


  //      addAlphaButton.setText(String.format(getString(R.id.button_add_alpha, )));

        stopwatchHandler = new Handler();

        startButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startTime = SystemClock.elapsedRealtime();
                stopwatchHandler.postDelayed(runnableStopwatch, 0);
                resetButton.setEnabled(false);
                startButton.setEnabled(false);
                addAlphaButton.setEnabled(false);
                addCharlieButton.setEnabled(false);
                addDeltaButton.setEnabled(false);
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                bufTime += milisecondsTime;
                stopwatchHandler.removeCallbacks(runnableStopwatch);
                resetButton.setEnabled(true);
                startButton.setEnabled(true);
                addAlphaButton.setEnabled(true);
                addCharlieButton.setEnabled(true);
                addDeltaButton.setEnabled(true);
            }
        });
        resetButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                milisecondsTime = 0L ;
                startTime = 0L;
                bufTime = 0L;
                updateTime = 0L;
                Minutes = 0;
                Seconds = 0;
                Miliseconds = 0;
                timer.setText(getResources().getString(R.string.stopwatch_counter_zero));
                comstockFactor.setText(getResources().getString(R.string.factor_value_zero));
                updatePoints = 0;
            }
        });
        addAlphaButton.setText(getString(R.string.button_alpha));
        addAlphaButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                updatePoints += getResources().getInteger(R.integer.alpha_value);
                comstockCalculation.setText(String.format("%d", updatePoints) + " / " + String.format("%.3f", timeInSeconds));
                Factor = updatePoints / timeInSeconds;
                comstockFactor.setText(String.format("%.3f", Factor));
            }
        });

// Tomek mnie męczy o gicie, a ja tu chce kodzic!
    }
    public Runnable runnableStopwatch = new Runnable(){
        public void run(){
            milisecondsTime = SystemClock.elapsedRealtime() - startTime;
            updateTime = bufTime + milisecondsTime;
            Seconds = (int)(updateTime / 1000);
            Minutes = Seconds / 60;
            Seconds = Seconds % 60;
            Miliseconds = (int)(updateTime % 1000);
            timeInSeconds = (float) (updateTime / 1000.0);
            timer.setText(String.format("%02d", Minutes) + ":" + String.format("%02d", Seconds) + "." + String.format("%03d", Miliseconds));
            stopwatchHandler.postDelayed(this, 0);
        }
    };
}
