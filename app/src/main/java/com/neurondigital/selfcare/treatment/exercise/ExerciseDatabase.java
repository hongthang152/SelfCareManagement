package com.neurondigital.selfcare.treatment.exercise;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.neurondigital.selfcare.treatment.manuallymphdrainagemassage.MLDModel;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExerciseDatabase extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "EXERCISE_DB";
    public static int VERSION_NUM = 1;
    public static final String TABLE_NAME = "EXERCISE";
    public final static String COL_ID = "_id";
    public final static String COL_NAME = "name";
    public static final String COL_START_TIME = "start_time";
    public static final String COL_END_TIME = "end_time";
    public static final String DURATION = "duration";

    public ExerciseDatabase(Context ctx)
    {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }


    //This function gets called if no database file exists.
    //Look on your device in the /data/data/package-name/database directory.
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_NAME + " text,"
                + COL_START_TIME + " text,"
                + COL_END_TIME + " text,"
                + DURATION + " text);");  // add or remove columns
    }


    //this function gets called if the database version on your device is lower than VERSION_NUM
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {   //Drop the old table:
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create the new table:
        onCreate(db);
    }

    //this function gets called if the database version on your device is higher than VERSION_NUM
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {   //Drop the old table:
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create the new table:
        onCreate(db);
    }
    void addmodel(ExerciseModel mld) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_NAME,mld.getName());
        values.put(COL_START_TIME,mld.getStartTime());
        values.put(COL_END_TIME, mld.getEndTime());
        values.put(DURATION, mld.getDuration());
        // Inserting Row
        db.insert(TABLE_NAME, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    void update(ExerciseModel mld) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_NAME,mld.getName());
        values.put(COL_START_TIME,mld.getStartTime());
        values.put(DURATION, mld.getDuration());
        values.put(COL_END_TIME, mld.getEndTime());

        db.update(TABLE_NAME, values, "_id = ?", new String[]{String.valueOf(mld.getID())});
        db.close(); // Closing database connection
    }

    public List<ExerciseModel> getAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        List<ExerciseModel> list = new ArrayList<>();
        String query = "SELECT * FROM "+ TABLE_NAME;
        Cursor cursor = db.rawQuery(query,null);
        while (cursor.moveToNext()){
            list.add(new ExerciseModel(cursor.getInt(cursor.getColumnIndex(COL_ID)),cursor.getString(cursor.getColumnIndex(COL_NAME)),cursor.getString(cursor.getColumnIndex(COL_START_TIME)), cursor.getString(cursor.getColumnIndex(DURATION)),
                    cursor.getString(cursor.getColumnIndex(COL_END_TIME))));
        }
        return list;
    }

    ExerciseModel getModel(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[] { COL_ID,
                        COL_START_TIME, DURATION}, COL_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        ExerciseModel model = new ExerciseModel(cursor.getInt(cursor.getColumnIndex(COL_ID)),cursor.getString(cursor.getColumnIndex(COL_NAME)), cursor.getString(cursor.getColumnIndex(COL_START_TIME)), cursor.getString(cursor.getColumnIndex(DURATION)),
                cursor.getString(cursor.getColumnIndex(COL_END_TIME)));
        // return contact
        return model;
    }


    public void deleterow(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COL_ID + " = ?",
                new String[] { String.valueOf(id) });
        Log.d("deleting: ", "id: "+id);

        db.close();
    }

    public ExerciseModel getLatest() {
        List<ExerciseModel> list = this.getAll();
        Collections.sort(list, (m1, m2) -> {
            try {
                return ExerciseModel.DATE_FORMATTER.parse(m2.getStartTime()).compareTo(ExerciseModel.DATE_FORMATTER.parse(m1.getStartTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return 0;
        });
        return list.isEmpty() ? null : list.get(0);
    }
}



