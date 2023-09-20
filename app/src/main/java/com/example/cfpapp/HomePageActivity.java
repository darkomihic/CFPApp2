package com.example.cfpapp;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.cfpapp.R;


import com.example.cfpapp.fragments.HomeFragment;
import com.example.cfpapp.fragments.ScheduleFragment;
import com.example.cfpapp.fragments.TrainingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


public class HomePageActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    private Button btnRunTimer;

    private Button btnSetCounter;

    private Button btnCreateWorkout;

    private static final int NAVIGATION_TRENING = R.id.navigation_trening;
    private static final int NAVIGATION_RASPORED = R.id.navigation_raspored;
    private static final int NAVIGATION_VEZBE = R.id.navigation_vezbe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page2);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(NAVIGATION_RASPORED);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;

                if (item.getItemId() == NAVIGATION_TRENING) {
                    fragment = new HomeFragment();
                } else if (item.getItemId() == NAVIGATION_RASPORED) {
                    fragment = new ScheduleFragment();
                } else if (item.getItemId() == NAVIGATION_VEZBE) {
                    fragment = new TrainingFragment();
                } else {
                    return false;
                }

                loadFragment(fragment);
                return true;
            }
        });





        // Load the initial fragment
        loadFragment(new ScheduleFragment());
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }
}
