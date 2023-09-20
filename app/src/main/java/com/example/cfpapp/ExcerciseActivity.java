package com.example.cfpapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.cfpapp.Adapters.ExcerciseAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class ExcerciseActivity extends AppCompatActivity {

    private RecyclerView rw;
    private TextView tw;

    private FirebaseFirestore fs;

    private ExcerciseAdapter adapter;

    private ArrayList<DocumentSnapshot> exerciseList = new ArrayList<>();
    private ArrayList<DocumentSnapshot> exerciseList2 = new ArrayList<>();

    private String workoutID;


    private Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excercise);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            workoutID = extras.getString("itemId");
            // Use the itemId in your activity as needed
        }

        Date todaysDate = new Date();


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);


        rw = findViewById(R.id.exerciseRecyclerView);
        tw = findViewById(R.id.exerciseTextView);

        rw.setLayoutManager(layoutManager);

        fs = FirebaseFirestore.getInstance();
        CollectionReference collectionRef = fs.collection("wrk_exc_link"); // Replace with your collection name

        collectionRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        exerciseList.clear(); // Clear the list before populating it
                        for (DocumentSnapshot document : queryDocumentSnapshots) {

                            Log.e("Firestore",  workoutID+" "+document.get("workoutid").toString());


                            if (workoutID.equals(document.get("workoutid").toString())) {



                                exerciseList.add(document);
                                exerciseList2.add(document);


                            }
                        }

                        Log.e("Firestore",  "usaooooo" + exerciseList.size());

                        adapter = new ExcerciseAdapter(context, exerciseList, new ExcerciseAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position, DocumentSnapshot item) {

                                /*Intent intent = new Intent(getContext(), WorkoutActivity.class);
                                intent.putExtra("itemId", item.getId()); // Replace itemId with your data
                                getContext().startActivity(intent);
                                Log.d("RecyclerView", "Item clicked at position " + position);*/
                            }
                        });

                        rw.setAdapter(adapter);

                        filterAndDisplayExercise();


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Firestore", "Error getting documents: " + e.getMessage());
                    }
                });




    }

    private void filterAndDisplayExercise() {
        ArrayList<DocumentSnapshot> filteredWorkouts = new ArrayList<>();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        fillWorkoutList();

        for (DocumentSnapshot workout : exerciseList) {
            Log.e("Firestore", "usao ovded ");

            filteredWorkouts.add(workout);

        }


        adapter.updateData(filteredWorkouts);

        rw.setAdapter(adapter);
    }

    private void fillWorkoutList(){
        exerciseList.clear();
        exerciseList.addAll(exerciseList2);
    }

}
