package com.example.cfpapp;

import static android.content.ContentValues.TAG;
import static com.example.cfpapp.R.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity {

    private FirebaseFirestore fs;
    private EditText txtUsername;
    private Button btnLogin2;
    private EditText txtPassword;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_login2);

        fs = FirebaseFirestore.getInstance();

        btnLogin2 = findViewById(id.btnLogin2);
        txtUsername = findViewById(id.TextUsername);
        txtPassword = findViewById(id.TextPassword);


        btnLogin2.setOnClickListener(v -> loginUser());
    }
    private void loginUser() {
        String username = txtUsername.getText().toString().trim();
        String password = txtPassword.getText().toString();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Query the Firestore collection for the entered username
        fs.collection("users")
                .whereEqualTo("username", username)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult() != null && !task.getResult().isEmpty()) {
                            // User with the entered username exists
                            // Check if the entered password matches the stored password
                            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                            String storedPassword = documentSnapshot.getString("password");
                            if (password.equals(storedPassword)) {
                                // Login successful, proceed to the next activity
                                // You can add your own logic here to handle the successful login
                                Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                // Proceed to the next activity or perform other actions
                            } else {
                                // Incorrect password
                                Toast.makeText(this, "Incorrect password", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // User with the entered username does not exist
                            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Error occurred while querying the database
                        Toast.makeText(this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }




}
