package com.example.gpstracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelperTraining extends SQLiteOpenHelper {
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DISTANCE = "distance";
    public static final String COLUMN_TEMPOFTRAINING = "tempOfTraining";
    public static final String COLUMN_CALORIES = "calories";
    public static final String COLUMN_TIMEOFTRAINING = "timeOfTrain";
    public static final String DBNAME = "TrainingResult.db";

    public DBHelperTraining(Context context) {
        super(context, DBNAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        MyDB.execSQL("CREATE TABLE training (" + COLUMN_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_DISTANCE
                + " INTEGER," + COLUMN_TEMPOFTRAINING + " TEXT,"+ COLUMN_CALORIES
                + " INTEGER,"+ COLUMN_TIMEOFTRAINING +" TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int oldVersion, int newVersion) {}



}
