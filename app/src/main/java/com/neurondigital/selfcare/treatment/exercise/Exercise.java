package com.neurondigital.selfcare.treatment.exercise;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.neurondigital.selfcare.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Exercise extends AppCompatActivity {

    ExerciseDatabase db = new ExerciseDatabase(this);
    Date startTime;
    Date endTime;
    List<ExerciseModel> records;
    ArrayAdapter<ExerciseModel> adapter;
    private Chronometer chronometer;
    private long pauseOffset;
    private boolean running;
    EditText etExeName;
    Button btnOk;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exe);

        chronometer = findViewById(R.id.chronometer);
        chronometer.setFormat("%s");
        chronometer.setBase(SystemClock.elapsedRealtime());

        etExeName = findViewById(R.id.etExeName);
        btnOk = findViewById(R.id.btnOk);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etExeName.getText().toString().isEmpty()){
                    Toast.makeText(Exercise.this, "Please Enter exercise name!", Toast.LENGTH_SHORT).show();
                }else{
                    name = etExeName.getText().toString().trim();
                    findViewById(R.id.linear).setVisibility(View.VISIBLE);
                    findViewById(R.id.enterLayout).setVisibility(View.GONE);
                    startChronometer(v);
                }
            }
        });

        ListView lv = findViewById(R.id.user_list);

        records = db.getAll();

        adapter = new ExerciseRecordAdapter(this, records);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Exercise.this, ExerciseRecordDetail.class);
                intent.putExtra("record", records.get(position));
                startActivityForResult(intent, 0);
            }
        });
        loadDataFromDatabase();
        registerForContextMenu(lv);
    }

    public void startName(View v){
        findViewById(R.id.linear).setVisibility(View.GONE);
        findViewById(R.id.enterLayout).setVisibility(View.VISIBLE);
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
        ExerciseDatabase db = new ExerciseDatabase(this);

        if (running) {
            endTime = Calendar.getInstance().getTime();
            chronometer.stop();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String start = formatter.format(startTime);
            String end = formatter.format(endTime);
            db.addmodel(new ExerciseModel(name,start, chronometer.getText().toString(), end));
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.mld_record_menu, menu);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mld_menu_delete:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                int position = (int) info.id;
                db.deleterow(records.get(position).getID());
                records.remove(position);
                adapter.notifyDataSetChanged();
                return true;
        }
        return super.onContextItemSelected(item);
    }
}


