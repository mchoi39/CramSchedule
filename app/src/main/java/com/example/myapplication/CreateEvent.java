package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CreateEvent extends AppCompatActivity {
    private static final String TAG = "MyActivity";

    public static final String CLASS_NAME = "className";
    public static final String TIME = "time";
    public static final String LOCATION = "location";
    public static final String DATE = "date";
    Button add, selectTime, selectDate;
    FirebaseAuth rAuth;
    String userID;
    DocumentReference mDocRef;
    DocumentReference mUserDocRef;
    String yearForDocRef;
    String monthForDocRef;
    String dayForDocRef;
    int hour, minutes;
    Calendar calendar = Calendar.getInstance();
    final int day = calendar.get(Calendar.DAY_OF_MONTH);
    final int months = calendar.get(Calendar.MONTH);
    final int years = calendar.get(Calendar.YEAR);
    EditText className, Etime, location, Edate;
    DatePickerDialog.OnDateSetListener datePickerListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        className = (EditText) findViewById(R.id.etCreateEventClass);
        Etime = (EditText) findViewById(R.id.etCreateEventTime);
        location = (EditText) findViewById(R.id.etCreateEventLocation);
        Edate = (EditText) findViewById(R.id.etCreateEventDate);
        selectTime = (Button) findViewById(R.id.selectTimeBtn);
        selectDate = (Button) findViewById(R.id.selectDateBtn);
        add = (Button) findViewById(R.id.createEventButton);
        add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                rAuth = FirebaseAuth.getInstance();
                userID = rAuth.getCurrentUser().getUid();
                mDocRef = FirebaseFirestore.getInstance().collection(yearForDocRef).document(monthForDocRef).collection(dayForDocRef).document();
                mUserDocRef = FirebaseFirestore.getInstance().collection("Users").document(userID).collection("EventsMadeByUser").document(mDocRef.getId());

                try {
                    createEvent(v);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                finish();
            }
        });

        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePicker = new DatePickerDialog(CreateEvent.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, datePickerListener,years, months, day);
                datePicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePicker.show();
            }
        });

        datePickerListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1;
                String date = day + "/" + month + "/" + year;
                Edate.setText(date);
                yearForDocRef = Integer.toString(year);
                monthForDocRef = Integer.toString(month);
                dayForDocRef = Integer.toString(dayOfMonth);
            }
        };

        selectTime.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                TimePickerDialog timePicker = new TimePickerDialog(CreateEvent.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        hour = hourOfDay;
                        minutes = minute;
                        String time = hour + ":" + minutes;
                        SimpleDateFormat twentyfour_hours = new SimpleDateFormat("HH:mm");
                        try {
                            Date date = twentyfour_hours.parse(time);
                            SimpleDateFormat twelve_hours = new SimpleDateFormat("hh:mm aa");
                            Etime.setText(twelve_hours.format(date));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, 12, 0, false);

                timePicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePicker.updateTime(hour, minutes);
                timePicker.show();
            }
        });


    }

    public void createEvent(View view) throws ParseException {
        WriteBatch writeBatch = FirebaseFirestore.getInstance().batch();
        String classNameText = className.getText().toString();
        String timeText = Etime.getText().toString();
        String locationText = location.getText().toString();
        String dateText = Edate.getText().toString();


        if (classNameText.isEmpty() || timeText.isEmpty() || locationText.isEmpty() || dateText.isEmpty()) return;
        Map<String, Object> dataToSave = new HashMap<>();
        dataToSave.put(CLASS_NAME, classNameText);
        dataToSave.put(TIME, timeText);
        dataToSave.put(LOCATION, locationText);
        dataToSave.put(DATE, dateText);
        DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        Date d = dateFormat.parse(timeText);

        dataToSave.put("dateTime", d);

        writeBatch.set(mDocRef, dataToSave);
        writeBatch.set(mUserDocRef, dataToSave);

        writeBatch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                // ...
                Log.w(TAG, "Doc saved");
            }
        });
    }
}