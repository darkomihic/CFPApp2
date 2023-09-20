package com.example.cfpapp.ProfileSettings.createWorkoutCoach;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cfpapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExcerciseSelectionActivity extends AppCompatActivity {

    private AutoCompleteTextView autoCompleteTextView;

    private Button btnAccept;

    private Button btnFinalAccept;

    private FirebaseFirestore db;

    private ArrayAdapter<String> adapter;

    private ArrayList<String> selectedExercises = new ArrayList<>();

    private ArrayList<String> setList = new ArrayList<>();


    private EditText setCounter;

    private String selectedExercise;

    private String sets;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excercise_selection);
        Intent intent = getIntent();
        String username = intent.getStringExtra("user");
        String group = intent.getStringExtra("group");
        String date = intent.getStringExtra("date");
        String workoutName = intent.getStringExtra("workoutName");



        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        btnAccept = findViewById(R.id.btnExerciseSelect);
        btnFinalAccept = findViewById(R.id.btnExerciseSelect2);

        setCounter = findViewById(R.id.setsEditText);
        TextView selectedExerciseAndSetsTextView = findViewById(R.id.selectedExerciseAndSetsTextView);


        db = FirebaseFirestore.getInstance();

        // Create an ArrayAdapter for the AutoCompleteTextView
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line);
        autoCompleteTextView.setAdapter(adapter);

        // Set up a listener to react to text changes
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Query Firestore for group names as the user types
                queryGroups(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        /*autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedExercise = adapter.getItem(position);

            // Add the selected exercise to the list
            selectedExercises.add(selectedExercise);

            // Update the UI to display the selected exercises
            //updateSelectedExercisesUI();

            // Clear the AutoCompleteTextView after an item is selected
            autoCompleteTextView.setText("");
        });*/

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the currently selected exercise from the AutoCompleteTextView
                selectedExercise = autoCompleteTextView.getText().toString();

                // Get the number of sets from the EditText
                sets = setCounter.getText().toString();

                if (!selectedExercise.isEmpty() && !sets.isEmpty()) {
                    // Add the selected exercise and sets to the respective lists
                    selectedExercises.add(selectedExercise);
                    setList.add(sets);

                    // Update the UI to display the selected exercises
                    updateSelectedExercisesUI();

                    // Clear the AutoCompleteTextView and EditText after adding the data
                    autoCompleteTextView.setText("");
                    setCounter.setText("");
                }
            }
        });

        btnFinalAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExcerciseSelectionActivity.this, WorkoutConfirmationActivity.class);
                intent.putStringArrayListExtra("setList", setList);
                intent.putStringArrayListExtra("exerciseList", selectedExercises);
                intent.putExtra("username", username);
                intent.putExtra("group", group);
                intent.putExtra("date", date);
                intent.putExtra("workoutName", workoutName);
                startActivity(intent);

            }
        });


    }

    private void queryGroups(String groupName) {

        String groupNameLower = groupName.toLowerCase(); // or groupName.toUpperCase();
        // Query Firestore for matching group names
        db.collection("excercise")
                .orderBy("exercisename")
                .startAt(groupNameLower)
                .endAt(groupNameLower + "\uf8ff")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            adapter.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String name = document.getString("exercisename");
                                adapter.add(name);
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            // Handle errors
                        }
                    }
                });
    }

    /*private void updateSelectedExercisesUI() {
        LinearLayout selectedExercisesContainer = findViewById(R.id.selectedExercisesContainer);
        selectedExercisesContainer.removeAllViews(); // Clear existing views

        for (int i = 0; i < selectedExercises.size(); i++) {
            String exercise = selectedExercises.get(i);
            String sets = setList.get(i);

            // Create a new TextView for each selected exercise
            TextView exerciseTextView = new TextView(this);
            exerciseTextView.setText(exercise + " " + sets);

            // Create a "Remove" button for each selected exercise
            Button removeButton = new Button(this);
            removeButton.setText("Remove");
            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Remove the exercise and sets from their respective lists
                    selectedExercises.remove(exercise);
                    setList.remove(sets);

                    // Update the UI to reflect the changes
                    updateSelectedExercisesUI();
                }
            });

            // Add the TextView and "Remove" button to the container
            selectedExercisesContainer.addView(exerciseTextView);
            selectedExercisesContainer.addView(removeButton);
        }
    }*/

    private void updateSelectedExercisesUI() {
        LinearLayout selectedExercisesContainer = findViewById(R.id.selectedExercisesContainer);
        selectedExercisesContainer.removeAllViews(); // Clear existing views

        for (int i = 0; i < selectedExercises.size(); i++) {
            String exercise = selectedExercises.get(i);

            // Check if there are sets for this exercise
            String sets = (i < setList.size()) ? setList.get(i) : "";

            // Create a final variable to capture the current index 'i'
            final int index = i;

            // Create a new TextView for each selected exercise
            TextView exerciseTextView = new TextView(this);
            exerciseTextView.setText(exercise + " " + sets);

            // Create a "Remove" button for each selected exercise
            Button removeButton = new Button(this);
            removeButton.setText("Remove");
            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Remove the exercise and sets from their respective lists
                    selectedExercises.remove(exercise);

                    // Check if there are sets for this exercise and remove it
                    if (index < setList.size()) {
                        setList.remove(index);
                    }

                    // Update the UI to reflect the changes
                    updateSelectedExercisesUI();
                }
            });

            // Add the TextView and "Remove" button to the container
            selectedExercisesContainer.addView(exerciseTextView);
            selectedExercisesContainer.addView(removeButton);
        }
    }



}