package pl.tenk.bullseye;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

import static pl.tenk.bullseye.R.integer.alpha_value;
import static pl.tenk.bullseye.R.integer.charlie_value;
import static pl.tenk.bullseye.R.integer.delta_value;
import static pl.tenk.bullseye.R.integer.miss_value;
import static pl.tenk.bullseye.R.integer.no_shoot_value;

public class MainActivity extends AppCompatActivity {

    TextView timer, comstockFactor, comstockCalculation, countACalculation, countCCalculation, countDCalculation, countNSCalculation, countMCalculation;
    Button startButton, stopButton, resetButton, addAlphaButton, addCharlieButton, addDeltaButton, addNoShootButton, addMissButton, resetPointsButton, soundButton;
    long milisecondsTime, startTime, bufTime, updateTime = 0L;
    Handler stopwatchHandler;
    int Seconds = 0,
            Minutes = 0,
            Miliseconds = 0,
            updatePoints = 0,
            countA = 0,
            countC = 0,
            countD = 0,
            countNS = 0,
            countM = 0,
            ASum,
            CSum,
            DSum,
            NSSum,
            MSum;
    float Factor = (float) 0;
    float timeInSeconds = (float) 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timer = findViewById(R.id.text_stopwatchCounter);
        soundButton = findViewById(R.id.button_sound);
        startButton = findViewById(R.id.button_start);
        stopButton = findViewById(R.id.button_stop);
        resetButton = findViewById(R.id.button_reset);
        resetPointsButton = findViewById(R.id.button_reset_points);
        comstockFactor = findViewById(R.id.text_factorValue);
        comstockCalculation = findViewById(R.id.text_factor_calculation);
        addAlphaButton = findViewById(R.id.button_add_alpha);
        addCharlieButton = findViewById(R.id.button_add_charlie);
        addDeltaButton = findViewById(R.id.button_add_delta);
        addNoShootButton = findViewById(R.id.button_add_no_shoot);
        addMissButton= findViewById(R.id.button_add_miss);
        countACalculation = findViewById(R.id.text_count_A);
        countCCalculation = findViewById(R.id.text_count_C);
        countDCalculation = findViewById(R.id.text_count_D);
        countNSCalculation = findViewById(R.id.text_count_NS);
        countMCalculation = findViewById(R.id.text_count_M);

        final MediaPlayer mp = MediaPlayer.create(this, R.raw.beep);

        timer.setText(getResources().getString(R.string.text_stopwatch_counter_zero,
                Minutes,
                Seconds,
                Miliseconds));
        comstockFactor.setText(String.format(Locale.getDefault(),"%.3f", Factor));
        addAlphaButton.setText(getResources().getString(R.string.button_add_alpha, getResources().getInteger(alpha_value)));
        addCharlieButton.setText(getResources().getString(R.string.button_add_charlie, getResources().getInteger(charlie_value)));
        addDeltaButton.setText(getResources().getString(R.string.button_add_delta, getResources().getInteger(delta_value)));
        addNoShootButton.setText(getResources().getString(R.string.button_add_no_shoot, getResources().getInteger(no_shoot_value)));
        addMissButton.setText(getResources().getString(R.string.button_add_miss, getResources().getInteger(miss_value)));
        comstockCalculation.setText(getResources().getString(R.string.text_factorCalculation,
                updatePoints,
                timeInSeconds));
        ASum = getResources().getInteger(alpha_value) * countA;
        CSum = getResources().getInteger(charlie_value) * countC;
        DSum = getResources().getInteger(delta_value) * countD;
        NSSum = getResources().getInteger(no_shoot_value) * countNS;
        MSum = getResources().getInteger(miss_value) * countM;
        countACalculation.setText(getResources().getString(R.string.text_count_A, countA, ASum));
        countCCalculation.setText(getResources().getString(R.string.text_count_C, countC, CSum));
        countDCalculation.setText(getResources().getString(R.string.text_count_D, countD, DSum));
        countNSCalculation.setText(getResources().getString(R.string.text_count_NS, countNS, NSSum));
        countMCalculation.setText(getResources().getString(R.string.text_count_M, countM, MSum));

        stopwatchHandler = new Handler();

        soundButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SoundDetails.class);
            startActivity(intent);
        });

        startButton.setOnClickListener(view -> {
            mp.start();
            timeInSeconds = 0f;
            startTime = SystemClock.elapsedRealtime();
            stopwatchHandler.postDelayed(runnableStopwatch, 0);
            resetButton.setEnabled(false);
            startButton.setEnabled(false);
            updatePoints = 0;
            countA = 0;
            ASum = getResources().getInteger(alpha_value) * countA;
            countACalculation.setText(getResources().getString(R.string.text_count_A, countA, ASum));
            countC = 0;
            CSum = getResources().getInteger(charlie_value) * countC;
            countCCalculation.setText(getResources().getString(R.string.text_count_C, countC, CSum));
            countD = 0;
            DSum = getResources().getInteger(delta_value) * countD;
            countDCalculation.setText(getResources().getString(R.string.text_count_D, countD, DSum));
            countNS = 0;
            NSSum = getResources().getInteger(no_shoot_value) * countNS;
            countNSCalculation.setText(getResources().getString(R.string.text_count_NS, countNS, NSSum));
            countM = 0;
            MSum = getResources().getInteger(miss_value) * countM;
            countMCalculation.setText(getResources().getString(R.string.text_count_M, countM, MSum));
            comstockCalculation.setText(getString(R.string.text_factorCalculation, updatePoints, timeInSeconds));
            if(timeInSeconds >0 && updatePoints > 0)
                Factor = (float) updatePoints / timeInSeconds;
            else Factor = 0;
            comstockFactor.setText(String.format(Locale.getDefault(),"%.3f", Factor));
        });

        stopButton.setOnClickListener(view -> {
            bufTime += milisecondsTime;
            stopwatchHandler.removeCallbacks(runnableStopwatch);
            resetButton.setEnabled(true);
            startButton.setEnabled(true);
            comstockCalculation.setText(getString(R.string.text_factorCalculation, updatePoints, timeInSeconds));
            if(timeInSeconds >0 && updatePoints > 0)
            Factor = (float) updatePoints / timeInSeconds;
            else Factor = 0;
            comstockFactor.setText(String.format(Locale.getDefault(),"%.3f", Factor));

        });

        resetButton.setOnClickListener(view -> {
            milisecondsTime = 0L ;
            startTime = 0L;
            bufTime = 0L;
            updateTime = 0L;
            timeInSeconds = 0f;
            updatePoints = 0;
            countA = 0;
            ASum = getResources().getInteger(alpha_value) * countA;
            countACalculation.setText(getResources().getString(R.string.text_count_A, countA, ASum));
            countC = 0;
            CSum = getResources().getInteger(charlie_value) * countC;
            countCCalculation.setText(getResources().getString(R.string.text_count_C, countC, CSum));
            countD = 0;
            DSum = getResources().getInteger(delta_value) * countD;
            countDCalculation.setText(getResources().getString(R.string.text_count_D, countD, DSum));
            countNS = 0;
            NSSum = getResources().getInteger(no_shoot_value) * countNS;
            countNSCalculation.setText(getResources().getString(R.string.text_count_NS, countNS, NSSum));
            countM = 0;
            MSum = getResources().getInteger(miss_value) * countM;
            countMCalculation.setText(getResources().getString(R.string.text_count_M, countM, MSum));
            Minutes = 0;
            Seconds = 0;
            Miliseconds = 0;
            if(timeInSeconds >0 && updatePoints > 0)
                Factor = (float) updatePoints / timeInSeconds;
            else Factor = 0;
            timer.setText(getResources().getString(R.string.text_stopwatch_counter_zero));
            comstockFactor.setText(String.format(Locale.getDefault(),"%.3f", Factor));
            comstockCalculation.setText(getString(R.string.text_factorCalculation, updatePoints, timeInSeconds));
            timer.setText(getResources().getString(R.string.text_stopwatch_counter_zero,
                    Minutes,
                    Seconds,
                    Miliseconds));
        });

        addAlphaButton.setOnClickListener(v -> {
            updatePoints += getResources().getInteger(alpha_value);
            countA++;
            ASum = getResources().getInteger(alpha_value) * countA;
            countACalculation.setText(getResources().getString(R.string.text_count_A, countA, ASum));
            comstockCalculation.setText(getString(R.string.text_factorCalculation, updatePoints, timeInSeconds));
            if(timeInSeconds >0 && updatePoints > 0)
                Factor = (float) updatePoints / timeInSeconds;
            else Factor = 0;
            comstockFactor.setText(String.format(Locale.getDefault(),"%.3f", Factor));
        });

        addCharlieButton.setOnClickListener(v -> {
            updatePoints += getResources().getInteger(charlie_value);
            countC++;
            CSum = getResources().getInteger(charlie_value) * countC;
            countCCalculation.setText(getResources().getString(R.string.text_count_C, countC, CSum));
            comstockCalculation.setText(getString(R.string.text_factorCalculation, updatePoints, timeInSeconds));
            if(timeInSeconds >0 && updatePoints > 0)
                Factor = (float) updatePoints / timeInSeconds;
            else Factor = 0;
            comstockFactor.setText(String.format(Locale.getDefault(),"%.3f", Factor));
        });

        addDeltaButton.setOnClickListener(v -> {
            updatePoints += getResources().getInteger(delta_value);
            countD++;
            DSum = getResources().getInteger(delta_value) * countD;
            countDCalculation.setText(getResources().getString(R.string.text_count_D, countD, DSum));
            comstockCalculation.setText(getString(R.string.text_factorCalculation, updatePoints, timeInSeconds));
            if(timeInSeconds >0 && updatePoints > 0)
                Factor = (float) updatePoints / timeInSeconds;
            else Factor = 0;
            comstockFactor.setText(String.format(Locale.getDefault(),"%.3f", Factor));
        });

        addNoShootButton.setOnClickListener(v -> {
            updatePoints += getResources().getInteger(no_shoot_value);
            countNS++;
            NSSum = getResources().getInteger(no_shoot_value) * countNS;
            countNSCalculation.setText(getResources().getString(R.string.text_count_NS, countNS, NSSum));
            comstockCalculation.setText(getString(R.string.text_factorCalculation, updatePoints, timeInSeconds));
            if(timeInSeconds >0 && updatePoints > 0)
                Factor = (float) updatePoints / timeInSeconds;
            else Factor = 0;
            comstockFactor.setText(String.format(Locale.getDefault(),"%.3f", Factor));
        });

        addMissButton.setOnClickListener(v -> {
            updatePoints += getResources().getInteger(miss_value);
            countM++;
            MSum = getResources().getInteger(miss_value) * countM;
            countMCalculation.setText(getResources().getString(R.string.text_count_M, countM, MSum));
            comstockCalculation.setText(getString(R.string.text_factorCalculation, updatePoints, timeInSeconds));
            if(timeInSeconds >0 && updatePoints > 0)
                Factor = (float) updatePoints / timeInSeconds;
            else Factor = 0;
            comstockFactor.setText(String.format(Locale.getDefault(),"%.3f", Factor));
        });

        resetPointsButton.setOnClickListener(view -> {
            updatePoints = 0;
            countA = 0;
            ASum = getResources().getInteger(alpha_value) * countA;
            countACalculation.setText(getResources().getString(R.string.text_count_A, countA, ASum));
            countC = 0;
            CSum = getResources().getInteger(charlie_value) * countC;
            countCCalculation.setText(getResources().getString(R.string.text_count_C, countC, CSum));
            countD = 0;
            DSum = getResources().getInteger(delta_value) * countD;
            countDCalculation.setText(getResources().getString(R.string.text_count_D, countD, DSum));
            comstockCalculation.setText(getString(R.string.text_factorCalculation, updatePoints, timeInSeconds));
            countNS = 0;
            NSSum = getResources().getInteger(no_shoot_value) * countNS;
            countNSCalculation.setText(getResources().getString(R.string.text_count_NS, countNS, NSSum));
            countM = 0;
            MSum = getResources().getInteger(miss_value) * countM;
            countMCalculation.setText(getResources().getString(R.string.text_count_M, countM, MSum));
            if(timeInSeconds >0 && updatePoints > 0)
                Factor = (float) updatePoints / timeInSeconds;
            else Factor = 0;
            comstockFactor.setText(String.format(Locale.getDefault(),"%.3f", Factor));
        });
    }

    public Runnable runnableStopwatch = new Runnable(){
        public void run(){
            milisecondsTime = SystemClock.elapsedRealtime() - startTime;
            updateTime = milisecondsTime;
            Seconds = (int)(updateTime / 1000);
            Minutes = Seconds / 60;
            Seconds = Seconds % 60;
            Miliseconds = (int)(updateTime % 1000);
            timeInSeconds = (float) (updateTime / 1000.0);
            timer.setText(getResources().getString(R.string.text_stopwatch_counter_zero,
                    Minutes,
                    Seconds,
                    Miliseconds));
            stopwatchHandler.postDelayed(this, 0);
        }
    };


}
