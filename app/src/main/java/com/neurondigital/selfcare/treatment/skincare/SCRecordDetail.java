package com.neurondigital.selfcare.treatment.skincare;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;
import com.neurondigital.helpers.Utility;
import com.neurondigital.selfcare.R;
import com.neurondigital.selfcare.graph.eventlist.EventListActivity;
import com.neurondigital.selfcare.treatment.manuallymphdrainagemassage.MLDDatabase;
import com.neurondigital.selfcare.treatment.manuallymphdrainagemassage.MLDModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class SCRecordDetail extends AppCompatActivity {

        public static String SC_RECORD_DATE_FORMAT = "MMM dd, yyyy, hh:mm a";
        public static SimpleDateFormat SC_RECORD_DATE_FORMATTER = new SimpleDateFormat(SC_RECORD_DATE_FORMAT, Locale.ENGLISH);
        public static String RECORD_EXTRA = "record";

        TextView recordTimeText;
        TextView recordTime;
        TextView recordNoteText;
        TextView recordNote;
        Button recordDetailSaveBtn;
        Button editButton;
        EditText newNote;

        //SkinCareModel record;
        SkinCareDatabase db;

        Date time;
        String note;
        int id;

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            db = new SkinCareDatabase(this);
            setContentView(R.layout.activity_sc_record_detail);
            Toolbar toolbar = findViewById(R.id.record_detail_bar);
            editButton = findViewById(R.id.editButton);
            newNote = findViewById(R.id.new_note);
            newNote.setVisibility(View.GONE);

            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            HashMap<String, String> record = (HashMap<String, String>) getIntent().getSerializableExtra("record");

            recordTimeText = findViewById(R.id.time_txt);
            recordTime = findViewById(R.id.time);
            recordNoteText = findViewById(R.id.sc_note_txt);
            recordNote = findViewById(R.id.sc_note);

            recordDetailSaveBtn = findViewById(R.id.record_detail_save_btn);

            recordTime.setPaintFlags(recordTime.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            recordNote.setPaintFlags(recordNote.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            recordNoteText.setTypeface(recordNoteText.getTypeface(), Typeface.BOLD);

            try {
                time = SkinCareModel.DATE_FORMATTER.parse(record.get("Date"));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            note = record.get("Note");
            id = Integer.parseInt(record.get("_id"));

            update();
            editButton.setOnClickListener(e -> {
                newNote.setVisibility(View.VISIBLE);
            });


            recordDetailSaveBtn.setOnClickListener(e -> {

                SkinCareModel model = new SkinCareModel();

                if(!newNote.getText().toString().isEmpty()){
                    model.setNote(newNote.getText().toString());
                    note = newNote.getText().toString();
                }
                model.setNote(note);
                model.setDate(SkinCareModel.DATE_FORMATTER.format(time));
                model.setID(id);
                db.update(model);
                finish();
            });


         //set date picker
            recordTime.setOnClickListener(e -> {
                new SingleDateAndTimePickerDialog.Builder(this)
                        .title(getResources().getString(R.string.select_date_time))
                        .defaultDate(time)
                        .minutesStep(1)
                        .mainColor(getResources().getColor(R.color.apptitlebackground))
                        .listener((Date date) -> {
                            time = date;
                            update();
                        }).display();
            });


        }

        private void update() {
            recordTime.setText(SC_RECORD_DATE_FORMATTER.format(time));
            recordNote.setText(note);

        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            if (item.getItemId() == android.R.id.home) {
                finish();
            }

            return super.onOptionsItemSelected(item);
        }
    }


