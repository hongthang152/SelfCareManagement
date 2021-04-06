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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
    Button startTimer;
    RelativeLayout reset_exe_continue_container;
    TextView exe_continue_btn,reset_exe_btn;
    FloatingActionButton event_list_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exe_2);

        chronometer = findViewById(R.id.chronometer);
        chronometer.setFormat("%s");
        chronometer.setBase(SystemClock.elapsedRealtime());

        etExeName = findViewById(R.id.enterNote);
        startTimer = findViewById(R.id.startTimer);
        reset_exe_continue_container = findViewById(R.id.reset_exe_continue_container);
        exe_continue_btn = findViewById(R.id.exe_continue_btn);
        reset_exe_btn = findViewById(R.id.reset_exe_btn);
        event_list_btn = findViewById(R.id.event_list_btn);

        startTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startTimer.getText().toString().equalsIgnoreCase("Stop\nTimer")){
                    pauseChronometer(v);
                    startTimer.setText("Save\nExercise");
                    reset_exe_continue_container.setVisibility(View.VISIBLE);
                }else if(startTimer.getText().toString().equalsIgnoreCase("Save\nExercise")){
                    StopChronometer(v);
                    startTimer.setText("Start\nTimer");
                    reset_exe_continue_container.setVisibility(View.GONE);
                }else{
                    if(etExeName.getText().toString().isEmpty()){
                        Toast.makeText(Exercise.this, "Please Enter exercise name!", Toast.LENGTH_SHORT).show();
                    }else{
                        name = etExeName.getText().toString().trim();
                        startChronometer(v);
                        startTimer.setText("Stop\nTimer");
                    }
                }
            }
        });



        exe_continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startChronometer(v);
                reset_exe_continue_container.setVisibility(View.GONE);
                startTimer.setText("Stop\nTimer");
            }
        });

        reset_exe_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetChronometer(v);
                startChronometer(v);
                reset_exe_continue_container.setVisibility(View.GONE);
                startTimer.setText("Stop\nTimer");

            }
        });

        event_list_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Exercise.this,ExerciseListActivity.class));
            }
        });
//        ListView lv = findViewById(R.id.user_list);
//
//        records = db.getAll();
//
//        adapter = new ExerciseRecordAdapter(this, records);
//        lv.setAdapter(adapter);
//        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(Exercise.this, ExerciseRecordDetail.class);
//                intent.putExtra("record", records.get(position));
//                startActivityForResult(intent, 0);
//            }
//        });
//        //loadDataFromDatabase();
//        registerForContextMenu(lv);
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
        reset_exe_continue_container.setVisibility(View.GONE);
        chronometer.setBase(SystemClock.elapsedRealtime());
        pauseOffset = 0;
    }

    public void StopChronometer(View v) {
        ExerciseDatabase db = new ExerciseDatabase(this);

        if (running) {
            reset_exe_continue_container.setVisibility(View.GONE);
            endTime = Calendar.getInstance().getTime();
            chronometer.setBase(SystemClock.elapsedRealtime());
            chronometer.stop();
            pauseOffset = 0;
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String start = formatter.format(startTime);
            String end = formatter.format(endTime);
            db.addmodel(new ExerciseModel(name,start, chronometer.getText().toString(), end));
            //loadDataFromDatabase();
            running = false;
            resetChronometer(v);
        }
    }

//    public void loadDataFromDatabase() {
//        records.clear();
//        records.addAll(db.getAll());
//        Collections.reverse(records);
//        adapter.notifyDataSetChanged();
//    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        loadDataFromDatabase();
//    }

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


