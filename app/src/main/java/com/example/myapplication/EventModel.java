package com.example.myapplication;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class EventModel {

    private String className;
    private String date;
    private String location;
    private String time;
    private String uid;
    @ServerTimestamp
    private Date dateTime;

    private EventModel(){

    }

    private EventModel(String className, String date, String location, String time, String uid) {
        this.className = className;
        this.date = date;
        this.location = location;
        this.time = time;
    }
    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }
}
