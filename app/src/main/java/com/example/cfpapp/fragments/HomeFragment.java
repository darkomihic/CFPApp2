package com.example.cfpapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.cfpapp.R;
import com.example.cfpapp.RunTimerActivity;
import com.example.cfpapp.CalorieCalendar.CalorieCounterActivity;

public class HomeFragment extends Fragment {

    private Button btnRunTimer;
    private Button btnSetCounter;
    private Button btnCreateWorkout;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        // Initialize UI elements and set up listeners

        btnRunTimer = view.findViewById(R.id.btnRunTimer);
        btnSetCounter = view.findViewById(R.id.btnSetCounter);

        btnRunTimer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RunTimerActivity.class);
                startActivity(intent);
            }
        });

        btnSetCounter.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CalorieCounterActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
