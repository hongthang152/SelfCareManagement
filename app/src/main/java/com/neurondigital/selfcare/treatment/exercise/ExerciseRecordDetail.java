package com.neurondigital.selfcare.treatment.exercise;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;
import com.neurondigital.helpers.Utility;
import com.neurondigital.selfcare.treatment.manuallymphdrainagemassage.MLDModel;
import com.neurondigital.selfcare.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ExerciseRecordDetail extends AppCompatActivity {

    public static String MLD_RECORD_DETAIL_DATE_FORMAT = "MMM dd, yyyy, hh:mm a";
    public static SimpleDateFormat MLD_RECORD_DETAIL_DATE_FORMATTER = new SimpleDateFormat(MLD_RECORD_DETAIL_DATE_FORMAT, Locale.ENGLISH);

    TextView recordDetailStartDate;
    TextView recordDetailEndDate;
    TextView recordDetailStartDateCap;
    TextView recordDetailEndDateCap;
    TextView recordDetailDurationCap;
    TextView recordDetailDuration;
    TextView exeNote;
    RelativeLayout noteContainer;

    Button recordDetailSaveBtn;
    Button editButton;

    ExerciseModel record;
    ExerciseDatabase db;

    Date start;
    Date end;
    String duration;
    EditText newNote;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new ExerciseDatabase(this);
        setContentView(R.layout.activity_record_detail);
        Toolbar toolbar = findViewById(R.id.record_detail_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        record = (ExerciseModel) getIntent().getSerializableExtra("record");

        noteContainer = findViewById(R.id.noteContainer);
        noteContainer.setVisibility(View.VISIBLE);

        exeNote = findViewById(R.id.exe_note);

        recordDetailStartDate = findViewById(R.id.record_detail_start_date);
        recordDetailEndDate = findViewById(R.id.record_detail_end_date);
        recordDetailStartDateCap = findViewById(R.id.start_time_cap);
        recordDetailEndDateCap = findViewById(R.id.end_time_cap);
        recordDetailDuration = findViewById(R.id.record_detail_duration);
        recordDetailSaveBtn = findViewById(R.id.record_detail_save_btn);
        recordDetailDurationCap = findViewById(R.id.duration_cap);

        recordDetailStartDate.setPaintFlags(recordDetailStartDate.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        recordDetailEndDate.setPaintFlags(recordDetailEndDate.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        recordDetailStartDateCap.setTypeface(recordDetailStartDateCap.getTypeface(), Typeface.BOLD);
        recordDetailEndDateCap.setTypeface(recordDetailEndDateCap.getTypeface(), Typeface.BOLD);
        recordDetailDurationCap.setTypeface(recordDetailEndDateCap.getTypeface(), Typeface.BOLD);

        editButton = findViewById(R.id.editButton);
        newNote = findViewById(R.id.new_note);

        try {
            start = MLDModel.DATE_FORMATTER.parse(record.getStartTime());
            end = MLDModel.DATE_FORMATTER.parse(record.getEndTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        exeNote.setText(record.getName());

        update();
        recordDetailStartDate.setOnClickListener(e -> {
            new SingleDateAndTimePickerDialog.Builder(this)
                    .title(getResources().getString(R.string.select_date_time))
                    .defaultDate(start)
                    .minutesStep(1)
                    .backgroundColor(getResources().getColor(R.color.appcolor))
                    .mainColor(getResources().getColor(R.color.brown))
                    .titleTextColor(getResources().getColor(R.color.appcolor))
                    .listener((Date date) -> {
                        start = date;
                        update();
                    }).display();
        });

        recordDetailEndDate.setOnClickListener(e -> {
            new SingleDateAndTimePickerDialog.Builder(this)
                    .defaultDate(end)
                    .title(getResources().getString(R.string.select_date_time))
                    .backgroundColor(getResources().getColor(R.color.appcolor))
                    .minutesStep(1)
                    .mainColor(getResources().getColor(R.color.brown))
                    .titleTextColor(getResources().getColor(R.color.appcolor))
                    .listener((Date date) -> {
                        end = date;
                        update();
                    }).display();
        });

        newNote.setVisibility(View.INVISIBLE);
        editButton.setOnClickListener(e -> {
            newNote.setVisibility(View.VISIBLE);
        });

        recordDetailSaveBtn.setOnClickListener(e -> {
            if(end.compareTo(start) < 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.error)
                        .setMessage(R.string.start_date_must_be_before_end_date)
                        .setPositiveButton(R.string.OK, null);
                builder.show();
                return;
            }

            if(!newNote.getText().toString().isEmpty()){
                String str = newNote.getText().toString();
                exeNote.setText(str);
                record.setName(str);
            }

            record.setStartTime(MLDModel.DATE_FORMATTER.format(start));
            record.setEndTime(MLDModel.DATE_FORMATTER.format(end));
            record.setDuration(Utility.diff(start, end));
            db.update(record);
            finish();
        });



    }

    private void update() {
        recordDetailStartDate.setText(MLD_RECORD_DETAIL_DATE_FORMATTER.format(start));
        recordDetailEndDate.setText(MLD_RECORD_DETAIL_DATE_FORMATTER.format(end));
        duration = Utility.diff(start, end);
        recordDetailDuration.setText(duration);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
