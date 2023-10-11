package com.example.cfpapp.CalorieCalendar;

import static com.example.cfpapp.LoginRegister.LoginActivity.userid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cfpapp.Adapters.MealAdapter;
import com.example.cfpapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;



public class CalorieCounterActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private RecyclerView recyclerView;
    private ArrayList<DocumentSnapshot> mealList = new ArrayList<>();
    private ArrayList<DocumentSnapshot> mealList2 = new ArrayList<>();
    private MealAdapter adapter;

    private Button btnAddMeal;

    private Context context = this;

    private FirebaseFirestore fs;

    public static float caloriesSum;
    public static float proteinSum;

    private TextView caloriesandprotiein;

    private Date dateOnCalendar;


    @Override
    protected void onResume() {
        super.onResume();
        // Refresh your CalendarView or UI elements here
        Date todaysDate = new Date();

        dateOnCalendar = todaysDate;




        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        // Move the Firestore query to this location
        fs = FirebaseFirestore.getInstance();
        CollectionReference collectionRef = fs.collection("meal"); // Replace with your collection name

        collectionRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        mealList.clear(); // Clear the list before populating it
                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            if (document.get("userid").toString().equals(userid)) {
                                mealList.add(document);
                                mealList2.add(document);
                            }
                        }
                        adapter = new MealAdapter(context, mealList, new MealAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position, DocumentSnapshot item) {
                                /*Intent intent = new Intent(getContext(), WorkoutActivity.class);
                                intent.putExtra("itemId", item.getId()); // Replace itemId with your data
                                getContext().startActivity(intent);
                                Log.d("RecyclerView", "Item clicked at position " + position);*/
                            }
                        });

                        recyclerView.setAdapter(adapter);

                        filterAndDisplayWorkouts(todaysDate);

                        sumCaloriesAndProtein(todaysDate);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Firestore", "Error getting documents: " + e.getMessage());
                    }
                });


        Toast.makeText(this, "RADI", Toast.LENGTH_SHORT).show();

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calorie_counter);
        calendarView = findViewById(R.id.calendarView2);
        recyclerView = findViewById(R.id.recyclerView2);
        btnAddMeal = findViewById(R.id.addMeal);
        caloriesandprotiein = findViewById(R.id.txtCalorieProtein);

        Date todaysDate = new Date();

        dateOnCalendar = todaysDate;




        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        // Move the Firestore query to this location
        fs = FirebaseFirestore.getInstance();
        CollectionReference collectionRef = fs.collection("meal"); // Replace with your collection name

        collectionRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        mealList.clear(); // Clear the list before populating it
                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            if (document.get("userid").toString().equals(userid)) {
                                mealList.add(document);
                                mealList2.add(document);
                            }
                        }
                        adapter = new MealAdapter(context, mealList, new MealAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position, DocumentSnapshot item) {
                                /*Intent intent = new Intent(getContext(), WorkoutActivity.class);
                                intent.putExtra("itemId", item.getId()); // Replace itemId with your data
                                getContext().startActivity(intent);
                                Log.d("RecyclerView", "Item clicked at position " + position);*/
                            }
                        });

                        recyclerView.setAdapter(adapter);

                        filterAndDisplayWorkouts(todaysDate);

                        sumCaloriesAndProtein(todaysDate);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Firestore", "Error getting documents: " + e.getMessage());
                    }
                });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                Calendar selectedCalendar = Calendar.getInstance();
                selectedCalendar.set(Calendar.YEAR, year);
                selectedCalendar.set(Calendar.MONTH, month);
                selectedCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                Date selectedDate = selectedCalendar.getTime();
                dateOnCalendar = selectedCalendar.getTime();

                filterAndDisplayWorkouts(selectedDate);
                sumCaloriesAndProtein(selectedDate);


            }
        });

        recyclerView.setAdapter(adapter);


        btnAddMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalorieCounterActivity.this, AddMealActivity.class);
                long dateAsLong = dateOnCalendar.getTime();
                intent.putExtra("dateKey", dateAsLong);
                startActivity(intent);
            }
        });





    }

    private void sumCaloriesAndProtein(Date date){

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        caloriesSum = 0;
        proteinSum = 0;
        DecimalFormat decimalFormat = new DecimalFormat("0.00");

        if(mealList.isEmpty()){
            caloriesandprotiein.setText("No meals today");

        }

        for (DocumentSnapshot item : mealList) {
            if(df.format(item.getTimestamp("date").toDate()).equals(df.format(date)) && item.get("userid").equals(userid)){
                caloriesSum = caloriesSum + Float.parseFloat(item.get("calories").toString());
                proteinSum = proteinSum + Float.parseFloat(item.get("protein").toString());

                caloriesandprotiein.setText("Calories: "+ decimalFormat.format(caloriesSum) + "Proteins: "+ decimalFormat.format(proteinSum));


            }
        }

    }




    private void filterAndDisplayWorkouts(Date selectedDate) {
        ArrayList<DocumentSnapshot> filteredWorkouts = new ArrayList<>();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        fillWorkoutList();



        for (DocumentSnapshot workout : mealList) {

            if (df.format(workout.getTimestamp("date").toDate()).equals(df.format(selectedDate)) && workout.get("userid").equals(userid)) {
                Toast.makeText(this, "usao", Toast.LENGTH_SHORT).show();
                filteredWorkouts.add(workout);
            }
        }


        adapter.updateData(filteredWorkouts);

        recyclerView.setAdapter(adapter);
    }

    private void fillWorkoutList(){
        mealList.clear();
        mealList.addAll(mealList2);
    }






}