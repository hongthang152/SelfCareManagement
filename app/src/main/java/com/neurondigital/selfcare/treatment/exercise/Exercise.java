package com.neurondigital.selfcare.treatment.exercise;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.neurondigital.selfcare.R;
import com.neurondigital.selfcare.treatment.manuallymphdrainagemassage.MLD;
import com.neurondigital.selfcare.treatment.manuallymphdrainagemassage.MLDList;
import com.neurondigital.selfcare.treatment.manuallymphdrainagemassage.MLDModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
    String name;
    Button timerButton;
    RelativeLayout reset_exe_continue_container;
    TextView exe_continue_btn,reset_exe_btn;
    FloatingActionButton event_list_btn;
    TextView newRecordStartTextView;
    TextView newRecordEndTextView;

    RelativeLayout resetContinueContainer;

    public static String DATE_FORMAT = "MMM dd, yyyy, hh:mm a";
    public static SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exe);

        chronometer = findViewById(R.id.chronometer);
        chronometer.setFormat("%s");
        chronometer.setBase(SystemClock.elapsedRealtime());

        newRecordStartTextView = findViewById(R.id.new_record_start_date);
        newRecordEndTextView = findViewById(R.id.new_record_end_date);
        newRecordStartTextView.setText("Set time");
        newRecordEndTextView.setText("Set time");
        newRecordStartTextView.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        newRecordEndTextView.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        etExeName = findViewById(R.id.enterNote);
        timerButton = findViewById(R.id.timer_btn);
        reset_exe_continue_container = findViewById(R.id.reset_continue_container);
        exe_continue_btn = findViewById(R.id.continue_btn);
        reset_exe_btn = findViewById(R.id.reset_btn);
        event_list_btn = findViewById(R.id.event_list_btn);

        resetContinueContainer = findViewById(R.id.reset_continue_container);
        resetContinueContainer.setVisibility(View.INVISIBLE);

        timerButton.setOnClickListener(this::startTimerListener);

        newRecordStartTextView.setOnClickListener(e -> {
            new SingleDateAndTimePickerDialog.Builder(this)
                    .defaultDate(startTime == null ? new Date() : startTime)
                    .title(getResources().getString(R.string.select_date_time))
                    .minutesStep(1)
                    .mainColor(getResources().getColor(R.color.appcolor))
                    .listener((Date date) -> {
                        startTime = date;
                        newRecordStartTextView.setText(DATE_FORMATTER.format(startTime));
                        updateChronometer();
                    }).display();
        });

        newRecordEndTextView.setOnClickListener(e -> {
            new SingleDateAndTimePickerDialog.Builder(this)
                    .defaultDate(endTime == null ? new Date() : endTime)
                    .title(getResources().getString(R.string.select_date_time))
                    .minutesStep(1)
                    .mainColor(getResources().getColor(R.color.appcolor))
                    .listener((Date date) -> {
                        endTime = date;
                        newRecordEndTextView.setText(DATE_FORMATTER.format(endTime));
                        updateChronometer();
                    }).display();
        });

        DialogInterface.OnClickListener continueDialogClickListener = (DialogInterface dialog, int which)  -> {
            switch(which) {
                case DialogInterface.BUTTON_POSITIVE:
                    startChronometer();
                    timerButton.setText("Stop");
                    timerButton.setBackground(getResources().getDrawable(R.drawable.rounded_save_button));
                    timerButton.setOnClickListener(this::stopTimerListener);
                    resetContinueContainer.setVisibility(View.INVISIBLE);
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        };

        exe_continue_btn.setOnClickListener(e -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(Exercise.this);
            builder.setMessage("Are you sure you want to this exercise session to continue?")
                    .setPositiveButton("Yes", continueDialogClickListener)
                    .setNegativeButton("No", continueDialogClickListener).show();


        });

        DialogInterface.OnClickListener dialogClickListener = (DialogInterface dialog, int which)  -> {
            switch(which) {
                case DialogInterface.BUTTON_POSITIVE:
                    recreate();
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        };

        reset_exe_btn.setOnClickListener(e -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(Exercise.this);
            builder.setMessage("Are you sure you want to reset this massage session? This action cannot be undone")
                    .setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
        });

        event_list_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Exercise.this,ExerciseListActivity.class));
            }
        });
    }




    public void startChronometer() {
        if (!running) {
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            chronometer.start();
            running = true;
        }
    }

    private void startTimerListener(View v) {
        if(startTime == null)
            startTime = Calendar.getInstance().getTime();
        startChronometer();
        newRecordStartTextView.setText(DATE_FORMATTER.format(startTime));
        timerButton.setText("Stop");
        timerButton.setOnClickListener(this::stopTimerListener);
    }

    private void stopTimerListener(View v) {
        pauseChronometer();
        endTime = Calendar.getInstance().getTime();
        newRecordEndTextView.setText(DATE_FORMATTER.format(endTime));
        setSaveTimerBtnVisible();
    }

    private void setSaveTimerBtnVisible() {
        timerButton.setBackground(getResources().getDrawable(R.drawable.rounded_save_button_green));
        timerButton.setText("Save");
        resetContinueContainer.setVisibility(View.VISIBLE);
        timerButton.setOnClickListener(this::saveTimerListener);
    }

    private void saveTimerListener(View v) {
        if(endTime.compareTo(startTime) < 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.error)
                    .setMessage(R.string.start_date_must_be_before_end_date)
                    .setPositiveButton(R.string.OK, null);
            builder.show();
            return;
        }
        if(etExeName.getText().toString().isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.error)
                    .setMessage("Exercise name cannot be empty")
                    .setPositiveButton(R.string.OK, null);
            builder.show();
            return;
        }
        ProgressDialog pd = ProgressDialog.show(this, "Loading", "Saving session...");
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String start = formatter.format(startTime);
        String end = formatter.format(endTime);
        db.addmodel(new ExerciseModel(etExeName.getText().toString(), start,chronometer.getText().toString(), end));
        pd.dismiss();
        Toast.makeText(getBaseContext(), "Exercise session saved", Toast.LENGTH_SHORT);
        startActivity(new Intent(getBaseContext(), ExerciseListActivity.class));
    }


    public void pauseChronometer() {
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
            loadDataFromDatabase();
            running = false;
            resetChronometer(v);
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

    public void updateChronometer() {
        if(startTime != null && endTime != null) {
            pauseOffset = endTime.getTime() - startTime.getTime();
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            setSaveTimerBtnVisible();
        } else if(endTime == null) {
            pauseOffset = System.currentTimeMillis() - startTime.getTime();
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
        }
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


