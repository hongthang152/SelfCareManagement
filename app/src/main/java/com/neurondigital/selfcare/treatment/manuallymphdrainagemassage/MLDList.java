package com.neurondigital.selfcare.treatment.manuallymphdrainagemassage;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.neurondigital.selfcare.R;

import java.util.Collections;
import java.util.List;

public class MLDList extends AppCompatActivity {

    MLDDatabase db = new MLDDatabase(this);
//    Date startTime;
//    Date endTime;
//    Button timerButton;
//
    List<MLDModel> records;
    ArrayAdapter<MLDModel> adapter;
//    private Chronometer chronometer;
//    private long pauseOffset;
//    private boolean running;
//    public static final String url = "https://klosetraining.com/resources/self-care-videos/";
//    TextView helpVidText;
    Toolbar toolbar;
//
//    TextView newRecordStartTextView;
//    TextView newRecordEndTextView;
//    TextView resetBtn;
//    TextView continueBtn;
//
//    RelativeLayout resetContinueContainer;
    ListView lv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mld_record_list);
        lv = findViewById(R.id.record_list);


//        newRecordStartTextView = findViewById(R.id.new_record_start_date);
//        newRecordEndTextView = findViewById(R.id.new_record_end_date);
//        newRecordStartTextView.setText("Set time");
//        newRecordEndTextView.setText("Set time");
//        newRecordStartTextView.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
//        newRecordEndTextView.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
//
//        timerButton = findViewById(R.id.timer_btn);
//        resetContinueContainer = findViewById(R.id.reset_continue_container);
//        resetContinueContainer.setVisibility(View.INVISIBLE);
//
//        resetBtn = findViewById(R.id.reset_btn);
//        continueBtn = findViewById(R.id.continue_btn);
//
//        chronometer = findViewById(R.id.chronometer);
//        chronometer.setFormat("%s");
//        chronometer.setBase(SystemClock.elapsedRealtime());
//
////        ListView lv = findViewById(R.id.user_list);
////        helpVidText = findViewById(R.id.help_vid_text);
////        helpVidText.setPaintFlags(helpVidText.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
//
        toolbar = findViewById(R.id.mld_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
//
//        timerButton.setOnClickListener(e -> {
//            startChronometer();
//            timerButton.setText("Stop");
//            timerButton.setOnClickListener(e2 -> {
//                StopChronometer();
//                timerButton.setBackground(getResources().getDrawable(R.drawable.rounded_save_button_green));
//                timerButton.setText("Save");
//                resetContinueContainer.setVisibility(View.VISIBLE);
//            });
//        });
//
//        DialogInterface.OnClickListener dialogClickListener = (DialogInterface dialog, int which)  -> {
//            switch(which) {
//                case DialogInterface.BUTTON_POSITIVE:
//                    recreate();
//                    break;
//                case DialogInterface.BUTTON_NEGATIVE:
//                    break;
//            }
//        };
//
//        resetBtn.setOnClickListener(e -> {
//            AlertDialog.Builder builder = new AlertDialog.Builder(getBaseContext());
//            builder.setMessage("Are you sure you want to reset this massage session? This action cannot be undone")
//                    .setPositiveButton("Yes", dialogClickListener)
//                    .setNegativeButton("No", dialogClickListener).show();
//            recreate();
//        });
//

        records = db.getAll();

        adapter = new MLDRecordAdapter(this, records);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MLDList.this, MLDRecordDetail.class);
                intent.putExtra(MLDRecordDetail.RECORD_EXTRA, records.get(position));
                startActivityForResult(intent, 0);
            }
        });
        loadDataFromDatabase();
        registerForContextMenu(lv);
    }

//    public void startChronometer() {
//        if (!running) {
//            startTime = Calendar.getInstance().getTime();
//            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
//            chronometer.start();
//            running = true;
//        }
//    }
//
//    public void help(View v) {
//        Intent i = new Intent(Intent.ACTION_VIEW);
//        i.setData(Uri.parse(url));
//        startActivity(i);
//    }
//
//    public void pauseChronometer(View v) {
//        if (running) {
//            chronometer.stop();
//            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
//            running = false;
//        }
//    }
//
//    public void resetChronometer(View v) {
//        chronometer.setBase(SystemClock.elapsedRealtime());
//        pauseOffset = 0;
//    }
//
//    public void StopChronometer() {
//        MLDDatabase db = new MLDDatabase(this);
//
//        if (running) {
//            endTime = Calendar.getInstance().getTime();
//            chronometer.stop();
//            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
//            String start = formatter.format(startTime);
//            String end = formatter.format(endTime);
//            db.addmodel(new MLDModel(start, chronometer.getText().toString(), end));
//            loadDataFromDatabase();
//            running = false;
//        }
//    }
//
    public void loadDataFromDatabase() {
        records.clear();
        records.addAll(db.getAll());
        Collections.reverse(records);
        adapter.notifyDataSetChanged();
    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        loadDataFromDatabase();
//    }
//
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.mld_menu, menu);

    }
//
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
        }

        return super.onOptionsItemSelected(item);
    }
}


