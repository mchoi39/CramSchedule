package com.example.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.github.jhonnyx2012.horizontalpicker.DatePickerListener;
import com.github.jhonnyx2012.horizontalpicker.HorizontalPicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class Home_Fragment extends Fragment  implements DatePickerListener {
    private Button createEvent;
    private Button logout;
    private RecyclerView mFirestoreList;
    private FirebaseFirestore firebaseFirestore;
    private FirestoreRecyclerAdapter adapter;
    private String day = Integer.toString(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
    private String month = Integer.toString(Calendar.getInstance().get(Calendar.MONTH) + 1);
    private String year =  Integer.toString(Calendar.getInstance().get(Calendar.YEAR));
    private EventAdapter eventAdapter;
    private CheckBox checkBox;
    private List<String> classes;
    private TextView todayDay;


    View v;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_home, container, false);

        createEvent = (Button) v.findViewById(R.id.createEventBtn2);
        //logout = (Button) v.findViewById(R.id.logoutBtn2);
        checkBox = (CheckBox) v.findViewById(R.id.filter_checkbox);
        todayDay = (TextView) v.findViewById(R.id.dayOfTheWeekTextView);

        HorizontalPicker picker = (HorizontalPicker) v.findViewById(R.id.datePicker);
        picker.setListener(this).setDays(120).setOffset(7).setDateSelectedColor(Color.DKGRAY)
                .setDateSelectedTextColor(Color.WHITE)
                .setMonthAndYearTextColor(Color.DKGRAY)
                .setTodayButtonTextColor(getResources().getColor(R.color.horizontalCalendarChosenDayColor))
                .setTodayDateTextColor(getResources().getColor(R.color.horizontalCalendarTextColor))
                .setTodayDateBackgroundColor(getResources().getColor(R.color.horizontalCalendarTodaysDateBackgorund))
                .setUnselectedDayTextColor(getResources().getColor(R.color.horizontalCalendarTextColor))
                .setDayOfWeekTextColor(getResources().getColor(R.color.horizontalCalendarChosenDayColor))
                .setDateSelectedColor(getResources().getColor(R.color.horizontalCalendarChosenDayColor))
                .showTodayButton(true).init();
        picker.setBackgroundColor(getResources().getColor(R.color.horizontalCalendarChosenDayColor));
        picker.setDate(new DateTime());
        firebaseFirestore = FirebaseFirestore.getInstance();
        mFirestoreList = v.findViewById(R.id.firestore_list);
        setUpRecyclerView(day, month, year);
        mFirestoreList.setHasFixedSize(true);
        mFirestoreList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mFirestoreList.setAdapter(eventAdapter);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    setUpUserRecycler();
                } else {
                    setUpRecyclerView(day, month, year);
                }
            }
        });
        createEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreatePage();
            }
        });
        return v;
    }

    public void openCreatePage(){
        Intent intent = new Intent(v.getContext(), CreateEvent.class);
        startActivity(intent);
    }

    @Override
    public void onDateSelected(DateTime dateSelected){
        day = Integer.toString(dateSelected.getDayOfMonth());
        month = Integer.toString(dateSelected.getMonthOfYear());
        year = Integer.toString(dateSelected.getYear());
        int dayOfWeek = dateSelected.getDayOfWeek();
        switch (dayOfWeek){
            case 1:
                todayDay.setText("Monday");
                break;
            case 2:
                todayDay.setText("Tuesday");
                break;
            case 3:
                todayDay.setText("Wednesday");
                break;
            case 4:
                todayDay.setText("Thursday");
                break;
            case 5:
                todayDay.setText("Friday");
                break;
            case 6:
                todayDay.setText("Saturday");
                break;
            case 7:
                todayDay.setText("Sunday");
                break;

        }
        setUpUserRecycler();
    }

    private void setUpRecyclerView(String day, String month, String year){
        Query query = firebaseFirestore.collection(year).document(month).collection(day).orderBy("dateTime", Query.Direction.ASCENDING);
        //Recycler options
        FirestoreRecyclerOptions < EventModel > options = new FirestoreRecyclerOptions.Builder < EventModel > ()
                .setQuery(query, EventModel.class)
                .build();

        eventAdapter = new EventAdapter(options);
        mFirestoreList.setAdapter(eventAdapter);
        eventAdapter.startListening();
        eventAdapter.notifyDataSetChanged();
    }


    private void setUpUserRecycler(){
        classes = new ArrayList<>();

        DocumentReference docRef = firebaseFirestore.collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("MyClasses").document("classes");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    classes.add(Objects.requireNonNull(task.getResult()).getString("class1"));
                    if (task.getResult().getString("class2") != null){
                        classes.add((task.getResult()).getString("class2"));
                    }
                    if (task.getResult().getString("class3") != null){
                        classes.add((task.getResult()).getString("class3"));
                    }
                    if (task.getResult().getString("class4") != null){
                        classes.add((task.getResult()).getString("class4"));
                    }
                    if (task.getResult().getString("class5") != null){
                        classes.add((task.getResult()).getString("class5"));
                    }
                    if (task.getResult().getString("class6") != null){
                        classes.add((task.getResult()).getString("class6"));
                    }

                    Query query = firebaseFirestore.collection(year).document(month).collection(day).whereIn("className", classes).orderBy("dateTime", Query.Direction.ASCENDING);
                    //Recycler options
                    FirestoreRecyclerOptions < EventModel > options = new FirestoreRecyclerOptions.Builder < EventModel > ()
                            .setQuery(query, EventModel.class)
                            .build();

                    eventAdapter = new EventAdapter(options);
                    mFirestoreList.setAdapter(eventAdapter);
                    eventAdapter.startListening();
                    eventAdapter.notifyDataSetChanged();


                }else {
                    System.out.println("sike");
                }
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        eventAdapter.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        eventAdapter.startListening();
    }
    }

