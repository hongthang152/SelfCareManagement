package com.neurondigital.selfcare.SKINCARE;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class SkinCareDatabase extends SQLiteOpenHelper {


    public static final String DATABASE_NAME = "SKINCARE";
    public static int VERSION_NUM = 1;
    public static final String TABLE_NAME = "SCTABLE";
    public final static String COL_ID = "_id";
    public static final String COLUMN1 = "Date";
    public static final String COLUMN2 = "Note";


    public SkinCareDatabase(Context ctx)
    {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }


    //This function gets called if no database file exists.
    //Look on your device in the /data/data/package-name/database directory.
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "

                + COLUMN1  + " text,"
                + COLUMN2  + " text);");  // add or remove columns
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
    void addmodel(SkinCareModel sc) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN1,sc.getDate()); // get the date
        values.put(COLUMN2, sc.getNote()); // get the note

        // Inserting Row
        db.insert(TABLE_NAME, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    //get cursor from db with all rows, load them into the list of Hashmaps
    public ArrayList<HashMap<String, String>> getAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<HashMap<String, String>> scList = new ArrayList<>();
        String query = "SELECT * FROM "+ TABLE_NAME;
        Cursor cursor = db.rawQuery(query,null);
        while (cursor.moveToNext()){
            HashMap<String,String> scItem = new HashMap<>();
            scItem.put("_id", cursor.getString(cursor.getColumnIndex(COL_ID)));
            scItem.put("Date",cursor.getString(cursor.getColumnIndex(COLUMN1)));
            scItem.put("Note",cursor.getString(cursor.getColumnIndex(COLUMN2)));
            scList.add(scItem);
        }
        return  scList;
    }

    //convert rows from cursor into SkinCareModel objects
    SkinCareModel getModel(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[] { COL_ID,
                        COLUMN1, COLUMN2 }, COL_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        SkinCareModel model = new SkinCareModel(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2) );
        // return contact
        return model;
    }

    //delete 1 row by id
    public void deleterow(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_NAME, COL_ID + " = ?",
                new String[] {String.valueOf(id) });
        Log.d("deleting: ", "id: "+id);

        db.close();
    }

    //delete all rows
    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();

        Log.d("SkinCare DB: ", "Deleting all");

        db.delete(TABLE_NAME, null, null);

        db.close();
    }


}




