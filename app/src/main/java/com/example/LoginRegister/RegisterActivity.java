package com.example.LoginRegister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.cfpapp.HomePageActivity;
import com.example.cfpapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {


    private FirebaseFirestore fs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fs = FirebaseFirestore.getInstance();

        EditText txtUsername = findViewById(R.id.textUsername);
        EditText txtPassword = findViewById(R.id.textPassword);
        EditText txtPassword2 = findViewById(R.id.textPassword2);
        Button btnRegister = findViewById(R.id.btnRegister2);

        btnRegister.setOnClickListener(view -> {



            if (txtPassword.getText().toString().equals(txtPassword2.getText().toString())==false) {

                Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show();

            }
            else {

                checkUsername(txtUsername.getText().toString()).addOnSuccessListener(new OnSuccessListener<Boolean>() {
                    @Override
                    public void onSuccess(Boolean usernameExists) {
                        if (usernameExists) {

                            Toast.makeText(RegisterActivity.this, "Username exists!", Toast.LENGTH_SHORT).show();


                        } else {
                            // Username is available, you can proceed with registration or other actions

                            String username = txtUsername.getText().toString().trim();
                            String password = txtPassword.getText().toString();

                            Map<String, Object> user = new HashMap<>();

                            user.put("username", username);
                            user.put("password", password);
                            user.put("coach",false);
                            user.put("group","");

                            // Register the user with email and password in Firebase Authentication
                            fs.collection("users")
                                    .add(user)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {

                                            Intent intent = new Intent(RegisterActivity.this, HomePageActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {


                                        }
                                    });

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // An error occurred while checking the username
                        // Handle the error here
                    }
                });




                }
            });
    }

    private Task<Boolean> checkUsername(String username) {
        TaskCompletionSource<Boolean> completionSource = new TaskCompletionSource<>();

        fs.collection("users")
                .whereEqualTo("username", username)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // Check if any documents were found
                            boolean usernameExists = task.getResult() != null && !task.getResult().isEmpty();
                            completionSource.setResult(usernameExists);
                        } else {
                            // An error occurred while fetching data from the database
                            // Handle the error here
                            completionSource.setException(task.getException());
                        }
                    }
                });

        return completionSource.getTask();
    }


}


