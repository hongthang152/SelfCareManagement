package com.neurondigital.selfcare;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class MeasureDatabase  extends SQLiteOpenHelper{


    public static final String DATABASE_NAME = "Measure";
    public static int VERSION_NUM = 2;
    public static final String TABLE_NAME = "MeasureTable";
    public final static String COL_ID = "_id";
    public static final String COLUMN1 = "type";

    public MeasureDatabase(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }


    //This function gets called if no database file exists.
    //Look on your device in the /data/data/package-name/database directory.
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "

                + COLUMN1 + " text);");  // add or remove columns
    }


    //this function gets called if the database version on your device is lower than VERSION_NUM
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {   //Drop the old table:
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create the new table:
        onCreate(db);
    }

    //this function gets called if the database version on your device is higher than VERSION_NUM
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {   //Drop the old table:
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create the new table:
        onCreate(db);
    }

    void addmodel(MeasureClass measure) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN1, measure.getDate()); // date


        // Inserting Row
        db.insert(TABLE_NAME, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    public ArrayList<HashMap<String, String>> getAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<HashMap<String, String>> userList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            HashMap<String, String> user = new HashMap<>();
            user.put("date", cursor.getString(cursor.getColumnIndex(COLUMN1)));

            userList.add(user);
        }
        return userList;
    }

    public void deleterow(int i)
    {
        SQLiteDatabase db = this.getWritableDatabase();


        // Inserting Row
        db.delete(TABLE_NAME, "_id = ?", new String[] {Integer.toString(i)});
        //2nd argument is String containing nullColumnHack
        db.close();
    }
}


