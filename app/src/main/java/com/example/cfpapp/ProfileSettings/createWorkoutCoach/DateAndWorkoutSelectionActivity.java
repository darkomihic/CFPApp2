package com.example.cfpapp.ProfileSettings.createWorkoutCoach;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.cfpapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateAndWorkoutSelectionActivity extends AppCompatActivity {

    private Button btnAccept;
    private DatePicker datePicker;
    private EditText etWorkoutName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_and_workout_selection);
        Intent intent = getIntent();
        String username = intent.getStringExtra("user");
        String group = intent.getStringExtra("group");
        btnAccept = findViewById(R.id.btnDateSelect);
        datePicker = findViewById(R.id.datePicker);
        etWorkoutName = findViewById(R.id.etWorkoutName);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");


        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etWorkoutName.getText().toString().equals("")==false){
                    int year = datePicker.getYear();
                    int month = datePicker.getMonth();
                    int dayOfMonth = datePicker.getDayOfMonth();


                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    Date selectedDate = calendar.getTime();
                    String workoutName = etWorkoutName.getText().toString();

                    Intent intent = new Intent(DateAndWorkoutSelectionActivity.this, ExcerciseSelectionActivity.class);
                    intent.putExtra("user", username);
                    intent.putExtra("group", group);
                    intent.putExtra("date", selectedDate.toString());
                    intent.putExtra("workoutName", workoutName);
                    startActivity(intent);

                }

            }
        });

    }
}