package com.neurondigital.selfcare;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class Exe extends AppCompatActivity {
    private Chronometer chronometer;
    private long pauseOffset;
    private boolean running;
    ExeDatabase db = new ExeDatabase(Exe.this);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exe);
        ExeDatabase db = new ExeDatabase(this);
        loadDataFromDatabase(); //get any previously saved Contact objects


        chronometer = findViewById(R.id.chronometer);
        chronometer.setFormat("%s");
        chronometer.setBase(SystemClock.elapsedRealtime());

        ListView lv = (ListView) findViewById(R.id.user_list);
        ArrayList<HashMap<String, String>> userList = db.getAll();

        SimpleAdapter adapter = new SimpleAdapter(Exe.this, userList, R.layout.activity_mld_row, new String[]{"Date", "SavedTime"}, new int[]{R.id.Date, R.id.Time});

        lv.setAdapter(adapter);
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                // TODO Auto-generated method stub
                ExeDatabase db = new ExeDatabase(Exe.this);
                ArrayList<HashMap<String, String>> userList = db.getAll();

                final  SimpleAdapter adapter = new SimpleAdapter(Exe.this, userList, R.layout.activity_mld_row, new String[]{"Date", "SavedTime"}, new int[]{R.id.Date, R.id.Time});

                db.deleterow(pos);

                Toast.makeText(Exe.this, "long clicked, "+"pos: " + pos, Toast.LENGTH_LONG).show();


                adapter.notifyDataSetChanged();
                loadDataFromDatabase();

                return true;
            }
        });
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                if ((SystemClock.elapsedRealtime() - chronometer.getBase()) >= 1000000) {
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    Toast.makeText(Exe.this, "Bing!", Toast.LENGTH_SHORT).show();


                }
            }
        });
    }

    public void startChronometer(View v) {
        if (!running) {
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            chronometer.start();
            running = true;
        }
    }

    public void pauseChronometer(View v) {
        if (running) {
            chronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
            running = false;
        }
    }

    public void resetChronometer(View v) {
        chronometer.setBase(SystemClock.elapsedRealtime());
        pauseOffset = 0;
    }

    public void StopChronometer(View v) {
        ExeDatabase db = new ExeDatabase(this);

        if (running) {
            String date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
            db.addmodel(new ExeModel(date, chronometer.getText().toString()));
            chronometer.stop();
            loadDataFromDatabase();
            running = false;
        }
    }

    public void loadDataFromDatabase() {
        ExeDatabase db = new ExeDatabase(this);
        ArrayList<HashMap<String, String>> userList = db.getAll();
        ListView lv = (ListView) findViewById(R.id.user_list);
        ListAdapter adapter = new SimpleAdapter(Exe.this, userList, R.layout.activity_mld_row, new String[]{"Date", "SavedTime"}, new int[]{R.id.Date, R.id.Time});
        lv.setAdapter(adapter);

    }

}


