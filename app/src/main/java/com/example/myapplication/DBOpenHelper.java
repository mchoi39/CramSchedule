package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.FirebaseFirestore;

public class DBOpenHelper extends SQLiteOpenHelper {

    public static final String createEventsTable = "create table" + DBStructure.tableName + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " + DBStructure.event + "TEXT, " + DBStructure.time + " TEXT, " + DBStructure.date + " TEXT, " + DBStructure.MONTH + " TEXT, " + DBStructure.YEAR + " TEXT) ";

    public static final String dropEventsTable = "Drop table if exists" + DBStructure.tableName;
    public DBOpenHelper(@Nullable Context context) {
        super(context, DBStructure.DB_NAME, null, DBStructure.version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createEventsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(dropEventsTable);
        onCreate(db);
    }

    public void saveEvent(String event, String time, String date, String month, String year, SQLiteDatabase db){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBStructure.event, event);
        contentValues.put(DBStructure.time, time);
        contentValues.put(DBStructure.date, date);
        contentValues.put(DBStructure.MONTH, month);
        contentValues.put(DBStructure.YEAR, year);
        db.insert(DBStructure.tableName, null, contentValues);

    }

    public Cursor ReadEvents(String date, SQLiteDatabase db){
        String[] projections = {DBStructure.event, DBStructure.time, DBStructure.date};
        String selection = DBStructure.date+ "-?";
        String[] selectionArgs = {date};

        return db.query(DBStructure.tableName, projections, selection, selectionArgs, null, null, null);
    }

    public Cursor ReadEventsperMonth(String month, String year, SQLiteDatabase db){
        String[] projections = {DBStructure.event, DBStructure.time, DBStructure.date};
        String selection = DBStructure.MONTH+ "-? and " + DBStructure.YEAR;
        String[] selectionArgs = {month,year};

        return db.query(DBStructure.tableName, projections, selection, selectionArgs, null, null, null);
    }
}
