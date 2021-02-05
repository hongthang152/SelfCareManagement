package com.neurondigital.selfcare.treatment.compressiontherapy;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;
import com.neurondigital.helpers.Utility;
import com.neurondigital.selfcare.treatment.manuallymphdrainagemassage.MLDModel;
import com.neurondigital.selfcare.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CTRecordDetailsActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String DATE_FORMAT_STRING = "dd-MM-yyyy HH:mm:ss";
    public static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(DATE_FORMAT_STRING);

    private TextView tv_day_night;
    private TextView tv_name;
    private TextView tv_start_date;
    private TextView tv_end_date;
    private TextView tv_detail_duration;
    private Button recordDetailSaveBtn;

    private CTDatabase db;
    private CTRecord ctRecord;

    private String name;
    private String dayOrNight;
    private Date start;
    private Date end;
    private String duration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c_t_record_details);

        //initializing Database
        db = new CTDatabase(this);

        //initializing views
        tv_day_night = findViewById(R.id.tv_day_night);
        tv_name = findViewById(R.id.tv_name);
        tv_start_date = findViewById(R.id.tv_start_date);
        tv_end_date = findViewById(R.id.tv_end_date);
        tv_detail_duration = findViewById(R.id.tv_duration);
        recordDetailSaveBtn = findViewById(R.id.btn_save);

        //if we want to update record then this condition will be true
        if(getIntent().hasExtra("updateRecord")) {
            ctRecord = (CTRecord) getIntent().getSerializableExtra("updateRecord");
            name = ctRecord.getName();
            dayOrNight = ctRecord.getDaynightTime();
            try {
                start = DATE_FORMATTER.parse(ctRecord.getStartTime());
                end = DATE_FORMATTER.parse(ctRecord.getEndTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        //otherwise we will create new record
        }else{
            ctRecord = new CTRecord();
            dayOrNight = getIntent().getStringExtra("dayOrNight");
            Log.d("dang again",dayOrNight);
            name = getIntent().getStringExtra("name");
            ctRecord.setName(name);
            ctRecord.setDaynightTime(dayOrNight);
            start = new Date();
            end = new Date();
        }

        tv_start_date.setOnClickListener(this);
        tv_end_date.setOnClickListener(this);
        recordDetailSaveBtn.setOnClickListener(this);

        setupScreen();
    }

    //setting up screen with data
    private void setupScreen() {
        tv_day_night.setText(dayOrNight);
        tv_name.setText(name);
        tv_start_date.setText(DATE_FORMATTER.format(start));
        tv_end_date.setText(DATE_FORMATTER.format(end));
        duration = Utility.diff(start, end);
        tv_detail_duration.setText(duration);

    }

    //handling click events
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tv_start_date:
                new SingleDateAndTimePickerDialog.Builder(this)
                        .title(getResources().getString(R.string.select_date_time))
                        .defaultDate(start)
                        .minutesStep(1)
                        .backgroundColor(getResources().getColor(R.color.apptitlebackground))
                        .mainColor(getResources().getColor(R.color.brown))
                        .titleTextColor(getResources().getColor(R.color.apptitlebackground))
                        .listener((Date date) -> {
                            start = date;
                            setupScreen();
                        }).display();
                break;

            case R.id.tv_end_date:
                new SingleDateAndTimePickerDialog.Builder(this)
                        .defaultDate(end)
                        .title(getResources().getString(R.string.select_date_time))
                        .backgroundColor(getResources().getColor(R.color.apptitlebackground))
                        .minutesStep(1)
                        .mainColor(getResources().getColor(R.color.brown))
                        .titleTextColor(getResources().getColor(R.color.apptitlebackground))
                        .listener((Date date) -> {
                            end = date;
                            setupScreen();
                        }).display();
                break;

            case R.id.btn_save:

                if(end.compareTo(start) < 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle(R.string.error)
                            .setMessage(R.string.start_date_must_be_before_end_date)
                            .setPositiveButton(R.string.OK, null);
                    builder.show();
                    return;
                }

                ctRecord.setStartTime(MLDModel.DATE_FORMATTER.format(start));
                ctRecord.setEndTime(MLDModel.DATE_FORMATTER.format(end));
                ctRecord.setDuration(Utility.diff(start, end));

                //db id cannot be 0 so if record doesn't exists then id will return 0 and we have to store it
                if(ctRecord.getId() == 0)
                    db.addCTRecord(ctRecord);
                //otherwise update the record
                else
                    db.updateCTRecord(ctRecord);

                startActivity(new Intent(CTRecordDetailsActivity.this,CTRecordListActivity.class));
                finish();

                break;
        }
    }
}