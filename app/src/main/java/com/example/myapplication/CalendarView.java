package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.zip.Inflater;

import javax.annotation.Nullable;

import static com.example.myapplication.R.layout.fragment_calendar;

public class CalendarView extends LinearLayout {
    ImageButton backBtn, forwardBtn;
    TextView currentDate;
    GridView gridView;
    private static final int MAX_CALENDAR_DAYS = 42;
    Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
    Context context;

    SimpleDateFormat dateFormat = new SimpleDateFormat(" MMMM yyyy", Locale.ENGLISH);
    SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM", Locale.ENGLISH);
    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.ENGLISH);

    gridAdapter gridAdapter;
    List<Date> dates = new ArrayList<>();
    List<Events> eventsList = new ArrayList<>();

    public CalendarView(Context context) {
        super(context);
    }

    public CalendarView(final Context context, @Nullable AttributeSet set){
        super(context, set);
        this.context = context;
        InitializeLayout();
        SetUpCalendar();
        backBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH, -1);
                SetUpCalendar();
            }
        });

        forwardBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.MONTH, 1);
                SetUpCalendar();
            }
        });

    }

    public CalendarView(Context context, @Nullable AttributeSet set, int defStyleAttr){
        super(context, set, defStyleAttr);

    }

    public void InitializeLayout(){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.calendar_layout, this);
        forwardBtn = view.findViewById(R.id.forwardsBtn);
        backBtn = view.findViewById(R.id.backwardsBtn);
        currentDate = view.findViewById(R.id.currentDate);
        gridView = view.findViewById(R.id.calendarGV);

    }

    public void SetUpCalendar(){
        String current_date = dateFormat.format(calendar.getTime());
        currentDate.setText(current_date);
        dates.clear();
        Calendar monthCalendar = (Calendar) calendar.clone();
        monthCalendar.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfMonth = monthCalendar.get(Calendar.DAY_OF_WEEK)-1;
        monthCalendar.add(Calendar.DAY_OF_MONTH, -firstDayOfMonth);

        while ((dates.size() <MAX_CALENDAR_DAYS)){
            dates.add(monthCalendar.getTime());
            monthCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        gridAdapter = new gridAdapter(context, dates, calendar, eventsList);
        gridView.setAdapter(gridAdapter);
    }


}
