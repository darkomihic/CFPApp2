package com.example.cfpapp.ProfileSettings.createWorkoutCoach;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cfpapp.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserSelectionActivity extends AppCompatActivity {

    private Button btnAccept;
    private TextView tv;
    private Spinner spinner;

    String selectedUser = "Select an user.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_selection);

        Intent intent = getIntent();
        String groupName = intent.getStringExtra("key");

        btnAccept = findViewById(R.id.btnUserSelect);
        tv = findViewById(R.id.tvUserSelect);
        spinner = findViewById(R.id.userSpinner);

        // Initialize Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

// Create a reference to your collection
        CollectionReference itemsRef = db.collection("users");

// Fetch data from Firestore
        itemsRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<String> itemNames = new ArrayList<>();
            itemNames.add(selectedUser);
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                String itemName = document.getString("username");

                if (itemName != null && document.get("group").toString().equals(groupName)) {
                    itemNames.add(itemName);
                }
            }

            // Now that you have the data, populate the Spinner

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, itemNames);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
        }).addOnFailureListener(e -> {
            // Handle errors if the data retrieval fails
            Log.e(TAG, "Error fetching data from Firestore: " + e.getMessage());
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedUser = parentView.getItemAtPosition(position).toString();

                // Check if the selected item is the default item
                String defaultItem = parentView.getItemAtPosition(0).toString(); // Assumes the default is the first item
                if (selectedUser.equals(defaultItem)) {
                    // Prompt the user to make a valid selection
                    Toast.makeText(getApplicationContext(), "Please select a valid item", Toast.LENGTH_SHORT).show();
                } else {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing when nothing is selected
            }
        });

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedUser.equals("Select a group.")==true){
                    Toast.makeText(getApplicationContext(), "Please select a valid item", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(UserSelectionActivity.this, DateAndWorkoutSelectionActivity.class);
                    intent.putExtra("user", selectedUser);
                    intent.putExtra("group", groupName);
                    startActivity(intent);


                }
            }
        });



    }
}
