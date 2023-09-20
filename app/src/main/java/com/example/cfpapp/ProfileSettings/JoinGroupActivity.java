package com.example.cfpapp.ProfileSettings;

import static com.example.LoginRegister.LoginActivity.userid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.LoginRegister.MainActivity;
import com.example.cfpapp.HomePageActivity;
import com.example.cfpapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class JoinGroupActivity extends AppCompatActivity {

    private Button btnAccept1;
    private Button btnAccept2;
    private EditText groupCode1;
    private EditText groupCode2;
    private TextView tv1;
    private TextView tv2;

    private TextView onaj;

    private FirebaseFirestore fs;

    private String code;

    private String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);
        btnAccept1 = findViewById(R.id.btnGroup1);
        btnAccept2 = findViewById(R.id.btnGroup2);
        groupCode1 = findViewById(R.id.editTextGroupCode1);
        groupCode2 = findViewById(R.id.editTextGroupCode2);
        tv1 = findViewById(R.id.textViewJoinGroup);
        tv2 = findViewById(R.id.textViewInGroup);
        onaj = findViewById(R.id.tvOnaj);

        final String group;

        fs = FirebaseFirestore.getInstance();

        DocumentReference docRef = fs.collection("users").document(userid);

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    String groupCode = documentSnapshot.getString("group");
                    code = groupCode;
                    if(groupCode==""){
                        btnAccept2.setVisibility(View.GONE);
                        groupCode2.setVisibility(View.GONE);
                        tv2.setVisibility(View.GONE);
                    }else{
                        btnAccept1.setVisibility(View.GONE);
                        groupCode1.setVisibility(View.GONE);
                        tv1.setVisibility(View.GONE);
                        onaj.setVisibility(View.GONE);
                    }
                } else {
                    Log.e("Firestore", "ja ne znam ni kako je moguce da u ovaj eror udje a kamoli sta da pise ako se to  slucajno desi ");

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Firestore", "Error getting documents: " + e.getMessage());
            }
        });

        btnAccept1.setOnClickListener(v -> joinGroup());

        btnAccept2.setOnClickListener(v -> leaveGroup());




    }

    private void joinGroup(){
        fs = FirebaseFirestore.getInstance();

        CollectionReference colRef = fs.collection("group");


        colRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot document : queryDocumentSnapshots){
                    if(document.get("code").toString().equals(groupCode1.getText().toString())){
                        DocumentReference docRef = fs.collection("users").document(userid);
                        Log.e("Firestore", "usao u leave group");

                        String groupName = document.get("name").toString();

                        Map<String, Object> updates = new HashMap<>();
                        updates.put("group", groupName); // Replace "fieldName" with the name of the field to update

                        docRef.update(updates)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getApplicationContext(), "Successfully joined group.", Toast.LENGTH_SHORT);

                                        Intent intent = new Intent(JoinGroupActivity.this, HomePageActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                    }
                                });


                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Firestore", "Error getting documents: " + e.getMessage());

            }
        });

    }

    private void leaveGroup(){
        fs = FirebaseFirestore.getInstance();

        CollectionReference colRef = fs.collection("group");


        colRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot document : queryDocumentSnapshots){
                    if(document.get("code").toString().equals(groupCode2.getText().toString())){

                        DocumentReference docRef = fs.collection("users").document(userid);



                        Map<String, Object> updates = new HashMap<>();
                        updates.put("group", ""); // Replace "fieldName" with the name of the field to update

                        docRef.update(updates)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        Intent intent = new Intent(JoinGroupActivity.this, HomePageActivity.class);
                                        startActivity(intent);
                                        finish();

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                    }
                                });


                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Firestore", "Error getting documents: " + e.getMessage());

            }
        });
    }


}