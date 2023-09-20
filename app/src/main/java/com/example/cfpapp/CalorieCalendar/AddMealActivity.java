package com.example.cfpapp.CalorieCalendar;

import static com.example.LoginRegister.LoginActivity.userid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cfpapp.R;
import com.example.cfpapp.klase.Meal;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
public class AddMealActivity extends AppCompatActivity {


    private EditText txtMeal;
    private EditText txtGrams;
    private Button btnAdd;

    private Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meal);
        txtMeal = findViewById(R.id.txtMealName);
        btnAdd = findViewById(R.id.btnAdd);
        txtGrams = findViewById(R.id.txtMealGrams);

        Intent intent = getIntent();
        if (intent != null) {
            long dateAsLong = intent.getLongExtra("dateKey", 0);

            // Convert the long value back to a Date object
            Date receivedDate = new Date(dateAsLong);

            date = receivedDate;

            // Now you can use the receivedDate in this activity as needed
            if (receivedDate != null) {

            }
        }


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    run(txtMeal.getText().toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    void run(String query) throws IOException {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .header("X-Api-Key", "JUXhfmSKhiYSX7HSthCtaQ==Sbo3Nfg4F74OVMoB")
                .url("https://api.calorieninjas.com/v1/nutrition?query="+query)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String myResponse = response.body().string();

                AddMealActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonResponse = new JSONObject(myResponse); // Assuming myResponse contains your JSON string
                            JSONArray itemsArray = jsonResponse.getJSONArray("items");

                            // Assuming you have only one item in the "items" array
                            if (itemsArray.length() > 0) {
                                JSONObject item = itemsArray.getJSONObject(0); // Get the first item

                                // Extract data from the item
                                String mealName = item.getString("name");
                                String calories = item.getString("calories");
                                String protein = item.getString("protein_g");

                                Log.e("api", "Meal Name: " + mealName);
                                Log.e("api", "Calories: " + calories);

                                addMeal(mealName, calories, protein);
                            } else {
                                Log.e("api", "No items found in the 'items' array.");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("api", "Error parsing JSON: " + e.getMessage());
                        }
                    }
                });

            }

            private void addMeal(String name,String calories, String protein){
                // Initialize Firebase
                Meal myData = new Meal();
                myData.setName(name);
                myData.setCalories(Float.parseFloat(calories)*Float.parseFloat(String.valueOf(txtGrams.getText()))/100);
                myData.setProtein(Float.parseFloat(protein)*Float.parseFloat(String.valueOf(txtGrams.getText()))/100);
                myData.setUserid(userid);
                myData.setDate(date);

// Create a reference to the Firebase Realtime Database
                 FirebaseFirestore fs = FirebaseFirestore.getInstance();
// Use the DatabaseReference to set the user data
                CollectionReference collectionRef = fs.collection("meal");
// Add a document with an auto-generated document ID
                collectionRef.add(myData)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                String successMessage = "Data added successfully!";
                                showToast(successMessage);



                                Intent intent = new Intent(AddMealActivity.this, CalorieCounterActivity.class);
                                startActivity(intent);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Handle errors here
                            }
                        });

            }

        });


    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }





}