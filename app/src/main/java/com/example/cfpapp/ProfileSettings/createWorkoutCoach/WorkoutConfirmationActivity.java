package com.example.cfpapp.ProfileSettings.createWorkoutCoach;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.cfpapp.ProfileSettings.UserSettingsActivity;
import com.example.cfpapp.R;
import com.example.cfpapp.fragments.ScheduleFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class WorkoutConfirmationActivity extends AppCompatActivity {

    private TextView workoutNameAndDate;

    private TextView usernameAndGroup;

    private ListView listview;

    private Button btnAccept;

    private ArrayList<String> help = new ArrayList<>();

    private FirebaseFirestore fs;

    private String usersID;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_confirmation);
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String group = intent.getStringExtra("group");
        String dateString = intent.getStringExtra("date");
        String workoutName = intent.getStringExtra("workoutName");
        ArrayList setList = intent.getStringArrayListExtra("setList");
        ArrayList exerciseList = intent.getStringArrayListExtra("exerciseList");

        workoutNameAndDate = findViewById(R.id.tvWorkoutName);
        usernameAndGroup = findViewById(R.id.tvWorkoutName2);
        listview = findViewById(R.id.listView);
        btnAccept = findViewById(R.id.btnMakeWorkout);

        Map<String, Object> workout = new HashMap<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss 'GMT'Z yyyy", Locale.US);

        try {
            Date date = dateFormat.parse(dateString);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String newDate = df.format(date);
            System.out.println(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        workoutNameAndDate.setText("User with the username: " +username+ " who is in group "+group);
        usernameAndGroup.setText("Workout with the name "+workoutName+" is set on the date "+dateString);

        for(int i = 0; i<setList.size(); i++){
            help.add(exerciseList.get(i).toString()+" "+setList.get(i).toString());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, help);

        listview.setAdapter(adapter);

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fs = FirebaseFirestore.getInstance();

                String field = "username";
                String desiredValue = username;

                CollectionReference collectionRef = fs.collection("users");

                collectionRef.whereEqualTo(field, desiredValue)
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String documentId = document.get("username").toString();
                                    usersID = documentId;
                                    Log.d("Document ID", usersID);

                                    // Once you have the usersID, you can proceed with your Firebase operations here
                                    // For example, you can add the workout document with the correct usersID
                                    workout.put("done", false);
                                    workout.put("workoutname", workoutName);
                                    workout.put("date", dateString);
                                    workout.put("userid", usersID);

                                    fs.collection("workout").add(workout).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            String workoutID = documentReference.getId();

                                            for (int i = 0; i < setList.size(); i++) {
                                                Map<String, Object> exercise = new HashMap<>();
                                                exercise.put("sets", setList.get(i));
                                                exercise.put("excerciseid", exerciseList.get(i));
                                                exercise.put("workoutid", workoutID);

                                                fs.collection("wrk_exc_link").add(exercise).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {
                                                        Intent intent = new Intent(WorkoutConfirmationActivity.this, UserSettingsActivity.class); // Replace MainActivity with your main activity or desired destination after workout creation.
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {

                                                    }
                                                });
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });
                                }
                            } else {
                                Log.w("Error", "Error getting documents.", task.getException());
                            }
                        });
            }
        });

    }


}