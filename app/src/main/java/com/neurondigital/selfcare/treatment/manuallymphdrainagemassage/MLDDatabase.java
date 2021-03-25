package com.neurondigital.selfcare.treatment.manuallymphdrainagemassage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MLDDatabase extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "SELFCARE";
    public static int VERSION_NUM = 1;
    public static final String TABLE_NAME = "MLDTABLE";
    public final static String COL_ID = "_id";
    public static final String COL_START_TIME = "start_time";
    public static final String COL_END_TIME = "end_time";
    public static final String DURATION = "duration";

    public MLDDatabase(Context ctx)
    {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }


    //This function gets called if no database file exists.
    //Look on your device in the /data/data/package-name/database directory.
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "

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
    void addmodel(MLDModel mld) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_START_TIME,mld.getStartTime());
        values.put(COL_END_TIME, mld.getEndTime());
        values.put(DURATION, mld.getDuration());
        // Inserting Row
        db.insert(TABLE_NAME, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    void update(MLDModel mld) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_START_TIME,mld.getStartTime());
        values.put(DURATION, mld.getDuration());
        values.put(COL_END_TIME, mld.getEndTime());

        db.update(TABLE_NAME, values, "_id = ?", new String[]{String.valueOf(mld.getID())});
        db.close(); // Closing database connection
    }

    public List<MLDModel> getAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        List<MLDModel> list = new ArrayList<>();
        String query = "SELECT * FROM "+ TABLE_NAME;
        Cursor cursor = db.rawQuery(query,null);
        while (cursor.moveToNext()){
            list.add(new MLDModel(cursor.getInt(cursor.getColumnIndex(COL_ID)), cursor.getString(cursor.getColumnIndex(COL_START_TIME)), cursor.getString(cursor.getColumnIndex(DURATION)),
                    cursor.getString(cursor.getColumnIndex(COL_END_TIME))));
        }
        return list;
    }

    public MLDModel getLatest() {
        List<MLDModel> list = this.getAll();
        Collections.sort(list, (m1, m2) -> {
            try {
                return MLDModel.DATE_FORMATTER.parse(m2.getStartTime()).compareTo(MLDModel.DATE_FORMATTER.parse(m1.getStartTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return 0;
        });
        return list.isEmpty() ? null : list.get(0);
    }

    MLDModel getModel(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[] { COL_ID,
                        COL_START_TIME, DURATION}, COL_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        MLDModel model = new MLDModel(cursor.getInt(cursor.getColumnIndex(COL_ID)), cursor.getString(cursor.getColumnIndex(COL_START_TIME)), cursor.getString(cursor.getColumnIndex(DURATION)),
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

}



