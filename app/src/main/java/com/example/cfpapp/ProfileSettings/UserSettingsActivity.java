package com.example.cfpapp.ProfileSettings;

import static com.example.cfpapp.LoginRegister.LoginActivity.userid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cfpapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UserSettingsActivity extends AppCompatActivity {

    private Button btnAccept1;
    private Button btnAccept2;
    private EditText etCurrPw;
    private EditText etNewPw;
    private EditText etRepeatNewPw;
    private EditText etFirstName;
    private EditText etLastName;

    private FirebaseFirestore fs;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);
        btnAccept1 = findViewById(R.id.btnAcceptChange);
        btnAccept2 = findViewById(R.id.btnMakeWorkout);
        etCurrPw = findViewById(R.id.txtPasswordCurrentChange);
        etNewPw = findViewById(R.id.txtPasswordNewChange);
        etRepeatNewPw = findViewById(R.id.txtRepeatPasswordChange);
        etFirstName = findViewById(R.id.txtFirstName);
        etLastName = findViewById(R.id.txtLastName);

        fs = FirebaseFirestore.getInstance();

        Map<String, Object> newData = new HashMap<>();
        newData.put("firstname", etFirstName.getText().toString()); // Replace with your first field name and value
        newData.put("lastname", etLastName.getText().toString()); // Replace with your first field name and value


        btnAccept1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnAccept2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(etFirstName.getText().length()>2 && etLastName.getText().length()>2){
                    fs.collection("users")
                            .document(userid)
                            .update(newData)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "Name changed.", Toast.LENGTH_SHORT).show();

                                    } else {
                                        Toast.makeText(getApplicationContext(), "Error changing name.", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                }else{
                    Toast.makeText(getApplicationContext(), "Please, type a name.", Toast.LENGTH_SHORT).show();

                }


            }
        });


    }
}