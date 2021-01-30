package com.neurondigital.selfcare;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Chronometer;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class pneumatic extends AppCompatActivity {
    private PneumaticDatabase db = new PneumaticDatabase(this);
    Date startTime;
    Date endTime;

    private List<PneumaticModel> records = new ArrayList<>();
   private  ListView lv;
    private ArrayAdapter<PneumaticModel> adapter;
    private Chronometer chronometer;
    private long pauseOffset;
    private boolean running;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pneumatic);
        chronometer = findViewById(R.id.chronometerPneumatic);
        chronometer.setFormat("%s");
        chronometer.setBase(SystemClock.elapsedRealtime());



        lv = findViewById(R.id.user_list_pn);
        records = db.getAll();
        adapter = new PneumaticAdapter(this, records);
        lv.setAdapter(adapter);

    }



    public void startChronometer(View v) {
        if (!running) {
            startTime = Calendar.getInstance().getTime();
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
        PneumaticDatabase db = new PneumaticDatabase(this);

        if (running) {
            endTime = Calendar.getInstance().getTime();
            chronometer.stop();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String start = formatter.format(startTime);
            String end = formatter.format(endTime);
            db.addModel(new PneumaticModel(start, chronometer.getText().toString(), end));
            loadDataFromDatabase();
            running = false;
        }
    }

    public void loadDataFromDatabase() {
        records.clear();
        records.addAll(db.getAll());
        adapter.notifyDataSetChanged();

    }



}
