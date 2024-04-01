package com.example.gpstracker;

import android.content.ContentValues;
import android.content.Context;
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
        super(context, "TrainingResult.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        MyDB.execSQL("CREATE TABLE training (" + COLUMN_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_DISTANCE
                + " INTEGER, " + COLUMN_TEMPOFTRAINING + " TEXT, "+ COLUMN_CALORIES
                + " INTEGER, "+ COLUMN_TIMEOFTRAINING +" TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int oldVersion, int newVersion) {}

    public boolean insertData(int _id, int distance, String tempOfTrain, int calories, String timeOfTrain){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", _id);
        contentValues.put("distance", distance);
        contentValues.put("tempOfTrain", tempOfTrain);
        contentValues.put("calories", calories);
        contentValues.put("timeOfTrain", timeOfTrain);

        long result = MyDB.insert("ResultOfTraining", null, contentValues);
        if(result==-1) return false;
        else return true;
    }

}
