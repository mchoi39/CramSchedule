package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.example.myapplication.R.layout.single_block;

public class gridAdapter extends ArrayAdapter {

    List<Date> dates;
    Calendar currentDate;
    List<Events> events;
    LayoutInflater inflater;
    public gridAdapter(@NonNull Context context, List<Date> dates, Calendar currentDate, List<Events> events) {
        super(context, single_block);

        this.dates = dates;
        this.currentDate = currentDate;
        this.events = events;
        inflater = LayoutInflater.from(context);

    }

    @SuppressLint("ResourceAsColor")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){

        Date monthDate = dates.get(position);
        Calendar dateCalendar = Calendar.getInstance();

        dateCalendar.setTime(monthDate);
        int DayNo = dateCalendar.get(Calendar.DAY_OF_MONTH);
        int displayMonth = dateCalendar.get(Calendar.MONTH)+1;
        int displayYear = dateCalendar.get(Calendar.YEAR);
        int currentMonth = currentDate.get(Calendar.MONTH)+1;
        int currentYear = currentDate.get(Calendar.YEAR);
        View v = convertView;
        if (v == null){
            v = inflater.inflate(single_block, parent, false);

        }

        if(displayMonth == currentMonth && displayYear == currentYear){
            v.setBackgroundColor(getContext().getResources().getColor(R.color.green));
        }
        else {
            v.setBackgroundColor(R.color.navigationBarColor);
        }

        TextView Day_Number = v.findViewById(R.id.calendar_day);
        Day_Number.setText(String.valueOf(DayNo));
        return v;
    }

    @Override
    public int getCount(){
        return dates.size();
    }

    @Override
    public int getPosition(@Nullable Object item){
        return dates.indexOf(item);
    }


    @Nullable
    @Override
    public Object getItem(int position){
        return dates.get(position);
    }
}
