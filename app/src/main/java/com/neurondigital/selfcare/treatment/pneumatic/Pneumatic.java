package com.neurondigital.selfcare.treatment.pneumatic;

import android.content.Intent;
import android.os.Bundle;

import android.os.SystemClock;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Chronometer;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.neurondigital.selfcare.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Pneumatic extends AppCompatActivity {
    private PneumaticDatabase db = new PneumaticDatabase(this);
    Date startTime;
    Date endTime;

    private List<PneumaticModel> records;
    private ListView lv;
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
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Pneumatic.this, PneumaticRecordDetail.class);
                intent.putExtra("record", records.get(position));
                startActivityForResult(intent, 0);
            }
        });

        lv.setOnItemLongClickListener((AdapterView<?> parent, View view, int position, long id) -> {
             PneumaticModel model = records.get(position);
            AlertDialog.Builder al = new AlertDialog.Builder(this);
            al.setPositiveButton("Delete", (click, s) -> {
                        db.recordDelete(model);
                       records.remove(position);
                       adapter.notifyDataSetChanged();
                    })
                    .create().show();
            return true;

        });
        loadDataFromDatabase();
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
        Collections.reverse(records);
        adapter.notifyDataSetChanged();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loadDataFromDatabase();
    }


}
