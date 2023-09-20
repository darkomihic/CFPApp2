package com.example.cfpapp;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cfpapp.R;

public class RunTimerActivity extends AppCompatActivity {

    private EditText restEditText;
    private EditText runEditText;
    private EditText setNumberEditText;
    private TextView countdownTextView;
    private Button btnStart;

    private Button button;

    private TextView runrest;

    private Vibrator vibrator;

    private long restPeriodMillis;
    private long runPeriodMillis;
    private int setNumber;

    private CountDownTimer countdownTimer;
    private boolean isRunPeriod = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_timer);

        restEditText = findViewById(R.id.restEditText);
        runEditText = findViewById(R.id.runEditText);
        setNumberEditText = findViewById(R.id.runEditText2);
        countdownTextView = findViewById(R.id.countdownTextView);
        btnStart = findViewById(R.id.btnStart);
        runrest = findViewById(R.id.runrest);
        button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                countdownTimer.cancel();
                setNumber=0;
                restPeriodMillis=0;
                runPeriodMillis=0;
                runrest.setText("Run/Rest");
                vibrator.cancel();
                countdownTextView.setText("0");
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restPeriodMillis = parseLong(restEditText.getText().toString()) * 1000;
                runPeriodMillis = parseLong(runEditText.getText().toString()) * 1000;
                setNumber = Integer.parseInt(setNumberEditText.getText().toString())*2;

                if (countdownTimer != null) {
                    countdownTimer.cancel();
                }
                countdownTimer = new CountDownTimer(3000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        if(millisUntilFinished>2000){
                            runrest.setText("Ready");
                        }else if(millisUntilFinished>1000 && millisUntilFinished<=2000){
                            runrest.setText("Set");
                        }else if(millisUntilFinished<1000){
                            runrest.setText("GO");
                        }

                    }

                    @Override
                    public void onFinish() {
                        countdownTextView.setText("0");
                        startNextSet();
                    }
                }.start();




            }
        });
    }

    private long parseLong(String value) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private void startNextSet() {


        vibrator.vibrate(1000);

        if (setNumber > 0) {
            if (isRunPeriod) {
                runrest.setText("Run");
                startCountdown(runPeriodMillis);
            } else {
                runrest.setText("Rest");
                startCountdown(restPeriodMillis);
            }
            isRunPeriod = !isRunPeriod;
            setNumber--;
        }
    }

    private void startCountdown(long durationMillis) {
        if (countdownTimer != null) {
            countdownTimer.cancel();
        }
        countdownTimer = new CountDownTimer(durationMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long secondsRemaining = millisUntilFinished / 1000;
                countdownTextView.setText(String.valueOf(secondsRemaining));
            }

            @Override
            public void onFinish() {
                countdownTextView.setText("0");
                startNextSet();
            }
        }.start();
    }
}
