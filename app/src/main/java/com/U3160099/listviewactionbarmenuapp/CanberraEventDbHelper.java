package com.U3160099.listviewactionbarmenuapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;

public class CanberraEventDbHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "food";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_URI = "uri";
    public static final String COLUMN_SCORE = "score";

    public CanberraEventDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(" +
                COLUMN_ID + " integer primary key, " +
                COLUMN_TITLE + " text, " +
                COLUMN_URI + " string, " +
                COLUMN_SCORE + " text)" );


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_NAME);
        onCreate(db);


    }

    public ArrayList<CanberraEvent> getAllEvents() {
        ArrayList<CanberraEvent> eventList = new ArrayList<CanberraEvent>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            CanberraEvent event = new CanberraEvent(
                    res.getString(res.getColumnIndex(COLUMN_ID)),
                    res.getString(res.getColumnIndex(COLUMN_TITLE)),
                    res.getString(res.getColumnIndex(COLUMN_URI)),
                    res.getString(res.getColumnIndex(COLUMN_SCORE)));
            eventList.add(event);
            res.moveToNext();
        }
        return eventList;
    }

  /*  public boolean insertEvent(CanberraEvent event) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TITLE, event.getTitle());
        contentValues.put(COLUMN_IMAGE_RESOURCE, event.getImageResource());
        contentValues.put(COLUMN_SCORE, event.getScore());
        db.insert(TABLE_NAME, null, contentValues);
        return true;
    }*/

    public long insertEvent(CanberraEvent event) {
        long id;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_TITLE, event.getTitle());
        contentValues.put(COLUMN_URI, event.getUri());
        contentValues.put(COLUMN_SCORE, event.getScore());
        id = db.insert(TABLE_NAME, null, contentValues);
        return id;
    }

    public Integer deleteEvent(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "id = ? ", new String[]{id});
    }

    public boolean updateEvent(String id, String newTitle, String newScore) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TITLE, newTitle);
        contentValues.put(COLUMN_SCORE, newScore);
        db.update(TABLE_NAME, contentValues, "id = ? ", new String[]{id});
        return true;
    }

    /*public boolean updateEvent(String id, CanberraEvent event) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_TITLE, event.getTitle());
        contentValues.put(COLUMN_URI, event.getUri());
        contentValues.put(COLUMN_SCORE, event.getScore());
        db.update(TABLE_NAME, contentValues, "id = ? ", new String[]{id});
        return true;
    }*/




}
