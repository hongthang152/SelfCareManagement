package com.neurondigital.selfcare.treatment.pneumatic;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;

import android.os.SystemClock;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

public class Pneumatic extends AppCompatActivity {
    private PneumaticDatabase db = new PneumaticDatabase(this);
    Date startTime;
    Date endTime;
    Button timerButton;

    private List<PneumaticModel> records;
    private ListView lv;
    private ArrayAdapter<PneumaticModel> adapter;
    private Chronometer chronometer;
    private long pauseOffset;
    private boolean running;
    Toolbar toolbar;
    TextView newRecordStartTextView;
    TextView newRecordEndTextView;
    TextView resetBtn;
    TextView continueBtn;

    RelativeLayout resetContinueContainer;
    FloatingActionButton eventListBtn;

    public static String DATE_FORMAT = "MMM dd, yyyy, hh:mm a";
    public static SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pneumatic);

     newRecordStartTextView = findViewById(R.id.new_record_PC_start_date);
        newRecordEndTextView = findViewById(R.id.new_record_PC_end_date);
        newRecordStartTextView.setText("Set time");
        newRecordEndTextView.setText("Set time");
        newRecordStartTextView.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        newRecordEndTextView.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        timerButton = findViewById(R.id.timer_PC_btn);
        resetContinueContainer = findViewById(R.id.reset_PC_continue_container);
        resetContinueContainer.setVisibility(View.INVISIBLE);
        eventListBtn = findViewById(R.id.event_list_btn);
        resetBtn = findViewById(R.id.reset_PC_btn);
        continueBtn = findViewById(R.id.PC_continue_btn);
        chronometer = findViewById(R.id.chronometerPneumatic);
        chronometer.setFormat("%s");
        chronometer.setBase(SystemClock.elapsedRealtime());

        toolbar = findViewById(R.id.pc_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        timerButton.setOnClickListener(this::startTimerListener);


        /*lv = findViewById(R.id.user_l ist_pn);
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
        loadDataFromDatabase();*/

        DialogInterface.OnClickListener dialogClickListener = (DialogInterface dialog, int which)  -> {
            switch(which) {
                case DialogInterface.BUTTON_POSITIVE:
                    recreate();
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        };

        resetBtn.setOnClickListener(e -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(Pneumatic.this);
            builder.setMessage("Are you sure you want to reset this massage session? This action cannot be undone")
                    .setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
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

        continueBtn.setOnClickListener(e -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(Pneumatic.this);
            builder.setMessage("Are you sure you want to this massage session to continue?")
                    .setPositiveButton("Yes", continueDialogClickListener)
                    .setNegativeButton("No", continueDialogClickListener).show();


        });

        eventListBtn.setOnClickListener(e -> {
            startActivity(new Intent(Pneumatic.this, PneumaticList.class));
        });

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
        ProgressDialog pd = ProgressDialog.show(this, "Loading", "Saving session...");
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String start = formatter.format(startTime);
        String end = formatter.format(endTime);
        db.addModel(new PneumaticModel(start, chronometer.getText().toString(), end));
        pd.dismiss();
        Toast.makeText(getBaseContext(), "PCP session saved", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getBaseContext(), PneumaticList.class));
    }


    public void pauseChronometer() {
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

    public void StopChronometer() {
        if (running) {
            endTime = Calendar.getInstance().getTime();
            chronometer.stop();
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

  /*  @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.mld_record_menu, menu);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mld_toolbar_menu, menu);
        return true;
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if(item.getItemId() == R.id.action_help) {
            help();
        }

        return super.onOptionsItemSelected(item);
    }*/
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
      if (item.getItemId() == android.R.id.home) {
          finish();
      }

      return super.onOptionsItemSelected(item);
  }
}
