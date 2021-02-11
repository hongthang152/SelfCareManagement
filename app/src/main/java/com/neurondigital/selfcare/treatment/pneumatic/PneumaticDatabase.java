package com.neurondigital.selfcare.treatment.pneumatic;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class PneumaticDatabase extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "SELFCARE";
    public static final int VERSION_NUM = 1;
    public static final String TABLE_NAME = "PNEUMATIC";
    public final static String COL_ID = "_id";
    public static final String COL_START_TIME = "start_time";
    public static final String COL_END_TIME = "end_time";
    public static final String DURATION = "duration";


    public PneumaticDatabase(Context context) {
        super(context, TABLE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "

                + COL_START_TIME + " text,"
                + COL_END_TIME + " text,"
                + DURATION + " text);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {   //Drop the old table:
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        //Create the new table:
        onCreate(db);
    }

    public void addModel(PneumaticModel mdl) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_START_TIME, mdl.getStartTime());
        cv.put(COL_END_TIME, mdl.getEndTime());
        cv.put(DURATION, mdl.getDuration());
        long id = db.insert(TABLE_NAME, null, cv);
        db.close();

    }

    public List<PneumaticModel> getAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        List<PneumaticModel> pn = new ArrayList<>();
        String query = "Select * from " + TABLE_NAME;
        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            for (int i = 0; i < c.getCount(); i++) {

                pn.add(new PneumaticModel(c.getInt(c.getColumnIndex(COL_ID)), c.getString(c.getColumnIndex(COL_START_TIME)),
                        c.getString(c.getColumnIndex(DURATION)),
                        c.getString(c.getColumnIndex(COL_END_TIME))));

                c.moveToNext();
            }
        }

        return pn;


    }

     public void  recordDelete(PneumaticModel mdl){
         SQLiteDatabase db = this.getWritableDatabase();
         db.delete(TABLE_NAME,COL_ID + "=?", new String[]{Long.toString(mdl.getID())});
     }

  /*  public void deleterow(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COL_ID + " = ?",
                new String[] { String.valueOf(id) });
        Log.d("deleting: ", "id: "+id);

        db.close();
    }*/
  void update(PneumaticModel mld) {
      SQLiteDatabase db = this.getWritableDatabase();

      ContentValues values = new ContentValues();
      values.put(COL_START_TIME,mld.getStartTime());
      values.put(DURATION, mld.getDuration());
      values.put(COL_END_TIME, mld.getEndTime());

      db.update(TABLE_NAME, values, "_id = ?", new String[]{String.valueOf(mld.getID())});
      db.close(); // Closing database connection
  }
}
