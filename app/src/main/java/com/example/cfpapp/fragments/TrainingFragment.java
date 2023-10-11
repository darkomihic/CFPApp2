package com.example.cfpapp.fragments;

import static com.example.cfpapp.LoginRegister.LoginActivity.coach;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.cfpapp.ProfileSettings.CheckUserCalendarActivity;
import com.example.cfpapp.ProfileSettings.createWorkoutCoach.GroupSelectionActivity;
import com.example.cfpapp.ProfileSettings.DeleteAccountActivity;
import com.example.cfpapp.ProfileSettings.JoinGroupActivity;
import com.example.cfpapp.ProfileSettings.UserSettingsActivity;
import com.example.cfpapp.R;

public class TrainingFragment extends Fragment {

    private Button btnUserSettings;
    private Button btnJoinGroup;
    private Button btnDeleteAcc;
    private Button btnCreateWorkout4User;
    private Button btnCheckUserCalendar;
    private View view1;
    private View view2;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_training, container, false);

        btnUserSettings = view.findViewById(R.id.btnUserSettings);
        btnJoinGroup = view.findViewById(R.id.btnJoinGroup);
        btnDeleteAcc = view.findViewById(R.id.btnDeleteAcc);
        btnCreateWorkout4User = view.findViewById(R.id.btnCreateWorkout4User);
        view2 = view.findViewById(R.id.view2);



        if(coach==false){
            btnCreateWorkout4User.setVisibility(View.INVISIBLE);
            view2.setVisibility(View.INVISIBLE);
        }

        btnUserSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UserSettingsActivity.class);
                startActivity(intent);
            }
        });

        btnJoinGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), JoinGroupActivity.class);
                startActivity(intent);
            }
        });

        btnDeleteAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DeleteAccountActivity.class);
                startActivity(intent);
            }
        });

        btnCreateWorkout4User.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GroupSelectionActivity.class);
                startActivity(intent);
            }
        });



        return view;
    }
}
