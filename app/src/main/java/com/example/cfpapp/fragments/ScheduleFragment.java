package com.example.cfpapp.fragments;

import static com.example.cfpapp.LoginRegister.LoginActivity.userName;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cfpapp.Adapters.WorkoutAdapter;
import com.example.cfpapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ScheduleFragment extends Fragment {

    private CalendarView calendarView;
    private RecyclerView recyclerView;
    private ArrayList<DocumentSnapshot> workoutList = new ArrayList<>();
    private ArrayList<DocumentSnapshot> workoutList2 = new ArrayList<>();
    private WorkoutAdapter adapter;

    private FirebaseFirestore fs;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    // Get the current date

    // Format the current date as a string


    // ...

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);

        calendarView = view.findViewById(R.id.calendarView);
        recyclerView = view.findViewById(R.id.recyclerView);

        Date todaysDate = new Date();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);



        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");



        // Move the Firestore query to this location
        fs = FirebaseFirestore.getInstance();
        CollectionReference collectionRef = fs.collection("workout"); // Replace with your collection name

        collectionRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        workoutList.clear(); // Clear the list before populating it
                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            if (document.get("userid").toString().equals(userName)) {
                                workoutList.add(document);
                                workoutList2.add(document);


                            }
                        }

                        adapter = new WorkoutAdapter(getContext(), workoutList, new WorkoutAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position, DocumentSnapshot item) {
                                /*Intent intent = new Intent(getContext(), WorkoutActivity.class);
                                intent.putExtra("itemId", item.getId()); // Replace itemId with your data
                                getContext().startActivity(intent);
                                Log.d("RecyclerView", "Item clicked at position " + position);*/
                            }
                        });

                        recyclerView.setAdapter(adapter);

                        try {
                            filterAndDisplayWorkouts(todaysDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


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
                Log.d("Calendar", "Selected Date: " + selectedDate.toString());

                try {
                    filterAndDisplayWorkouts(selectedDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

        });



        return view;
    }




    private void filterAndDisplayWorkouts(Date selectedDate) throws ParseException {
        ArrayList<DocumentSnapshot> filteredWorkouts = new ArrayList<>();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        fillWorkoutList();

        for (DocumentSnapshot workout : workoutList) {
            String workoutDate = workout.get("date").toString();
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);

            try {
                date = dateFormat.parse(workoutDate);

                // Now 'date' contains the parsed date and time information
                System.out.println(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Log.d("Filter", "Workout Date: " + workoutDate.toString() + selectedDate);
            System.out.println(date.equals(selectedDate)+" " + date + " " + selectedDate);
            // Assuming selectedDate is also in the same format
            if (workoutDate != null && df.format(date).equals(df.format(selectedDate)) && workout.get("userid").toString().equals(userName)) {
                filteredWorkouts.add(workout);
            }
        }

        Log.d("Filter", "Filtered Workouts Count: " + filteredWorkouts.size());
        adapter.updateData(filteredWorkouts);
        recyclerView.setAdapter(adapter);
    }



    private void fillWorkoutList(){
        workoutList.clear();
        workoutList.addAll(workoutList2);
    }







}

