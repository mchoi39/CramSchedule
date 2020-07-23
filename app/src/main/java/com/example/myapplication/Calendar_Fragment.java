package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Calendar_Fragment extends Fragment {

    CalendarView calendarView;
    View v;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_calendar, container, false);
        calendarView = (CalendarView) v.findViewById(R.id.custom_cal_view);
        calendarView.InitializeLayout();
        calendarView.SetUpCalendar();

        return v;
    }
}
