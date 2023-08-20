package com.example.cfpapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import androidx.fragment.app.Fragment;

import com.example.cfpapp.R;

public class ScheduleFragment extends Fragment {

    private CalendarView calendarView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);

        calendarView = view.findViewById(R.id.calendarView); // Use 'view.findViewById'
        calendarView.setDateTextAppearance(R.style.CustomCalendarTextAppearance);

        // Initialize UI elements and set up listeners
        return view;
    }
}
