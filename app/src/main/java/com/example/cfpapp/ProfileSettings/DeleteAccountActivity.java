package com.example.cfpapp.ProfileSettings;

import static com.example.LoginRegister.LoginActivity.userid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.LoginRegister.MainActivity;
import com.example.cfpapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class DeleteAccountActivity extends AppCompatActivity {

    private EditText password1;
    private EditText password2;

    private Button btnAccept;

    private FirebaseFirestore fs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_account);

        password1 = findViewById(R.id.txtPasswordNewChange);
        password2 = findViewById(R.id.txtRepeatPasswordChange);
        btnAccept = findViewById(R.id.btnAcceptChange);

        fs = FirebaseFirestore.getInstance();


        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredPassword = password1.getText().toString();

                if (enteredPassword.equals(password2.getText().toString())) {
                    // Assuming you have a reference to the user document
                    // based on the user ID, you can delete it like this:
                    fs.collection("users")
                            .document(userid) // Assuming userid is the document ID
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Document deleted successfully
                                    Toast.makeText(DeleteAccountActivity.this, "Account deleted.", Toast.LENGTH_SHORT).show();

                                    // Redirect to your desired activity
                                    Intent intent = new Intent(DeleteAccountActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish(); // Close this activity
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Handle the error
                                    Toast.makeText(DeleteAccountActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(DeleteAccountActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
