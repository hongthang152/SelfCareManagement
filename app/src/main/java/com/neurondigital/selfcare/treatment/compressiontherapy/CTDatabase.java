package com.neurondigital.selfcare.treatment.compressiontherapy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.neurondigital.selfcare.treatment.exercise.ExerciseModel;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CTDatabase extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "CT_DB";
    public static int VERSION_NUM = 1;
    public static final String TABLE_NAME = "CT_TABLE";
    public final static String COL_ID = "id";
    public final static String COL_DAY_OR_NIGHT_TIME = "day_night_time";
    public final static String COL_GARMENT_NAME = "garment_name";
    public static final String COL_START_TIME = "start_time";
    public static final String COL_END_TIME = "end_time";
    public static final String DURATION = "duration";

    public CTDatabase(Context ctx)
    {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    //This function gets called if no database file exists.
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " ("+COL_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_DAY_OR_NIGHT_TIME + " VARCHAR2(20),"
                + COL_GARMENT_NAME + " VARCHAR2(20),"
                + COL_START_TIME + " VARCHAR2(20),"
                + COL_END_TIME + " VARCHAR2(20),"
                + DURATION + " VARCHAR2(20));");
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


    void addCTRecord(CTRecord record) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_DAY_OR_NIGHT_TIME,record.getDaynightTime());
        values.put(COL_GARMENT_NAME, record.getName());
        values.put(COL_START_TIME,record.getStartTime());
        values.put(COL_END_TIME, record.getEndTime());
        values.put(DURATION, record.getDuration());
        // Inserting Row
        db.insert(TABLE_NAME, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    void updateCTRecord(CTRecord record) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_DAY_OR_NIGHT_TIME,record.getDaynightTime());
        values.put(COL_GARMENT_NAME, record.getName());
        values.put(COL_START_TIME,record.getStartTime());
        values.put(COL_END_TIME, record.getEndTime());
        values.put(DURATION, record.getDuration());

        db.update(TABLE_NAME, values, COL_ID +" = ?", new String[]{String.valueOf(record.getId())});
        db.close(); // Closing database connection
    }

    public List<CTRecord> getAllCTRecords(){
        SQLiteDatabase db = this.getWritableDatabase();
        List<CTRecord> list = new ArrayList<>();
        String query = "SELECT * FROM "+ TABLE_NAME;
        Cursor cursor = db.rawQuery(query,null);
        while (cursor.moveToNext()){
            list.add(new CTRecord(cursor.getInt(cursor.getColumnIndex(COL_ID)),cursor.getString(cursor.getColumnIndex(COL_DAY_OR_NIGHT_TIME)),cursor.getString(cursor.getColumnIndex(COL_GARMENT_NAME)), cursor.getString(cursor.getColumnIndex(COL_START_TIME)), cursor.getString(cursor.getColumnIndex(DURATION)),
                    cursor.getString(cursor.getColumnIndex(COL_END_TIME))));
        }
        return list;
    }

    public CTRecord getLatest() {
        List<CTRecord> list = getAllCTRecords();
        Collections.sort(list, (m1, m2) -> {
            try {
                return CTRecordDetailsActivity.DATE_FORMATTER.parse(m2.getStartTime()).compareTo(CTRecordDetailsActivity.DATE_FORMATTER.parse(m1.getStartTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return 0;
        });
        return list.isEmpty() ? null : list.get(0);
    }

    public void deleterow(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COL_ID + " = ?",
                new String[] { String.valueOf(id) });
        Log.d("deleting: ", "id: "+id);

        db.close();
    }
}
