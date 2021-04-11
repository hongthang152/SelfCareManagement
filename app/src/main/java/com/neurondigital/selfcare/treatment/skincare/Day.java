package com.neurondigital.selfcare.treatment.skincare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import com.neurondigital.selfcare.R;
import com.neurondigital.selfcare.treatment.compressiontherapy.CTRecordDetailsActivity;

public class Day extends AppCompatActivity {

    private LinearLayout garment1;
    private LinearLayout garment2;
    private Intent goToCtRecordDetails;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);

        toolbar = findViewById(R.id.day_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        garment1 = findViewById(R.id.garment1);
        garment2 = findViewById(R.id.garment2);
        toolbar = findViewById(R.id.toolbar);



        String dayOrNight = getIntent().getStringExtra("dayOrNight");

        goToCtRecordDetails = new Intent(Day.this, CTRecordDetailsActivity.class);


        Log.d("dang", dayOrNight);

        garment1.setOnClickListener(click->{
            goToCtRecordDetails.putExtra("name","Elastic Compression Garment");
            goToCtRecordDetails.putExtra("dayOrNight",dayOrNight);

            Day.this.startActivity(goToCtRecordDetails);
            finish();
        });

        garment2.setOnClickListener(click->{
            goToCtRecordDetails.putExtra("name", "Inelastic Velcro Garment");
            goToCtRecordDetails.putExtra("dayOrNight",dayOrNight);
            Day.this.startActivity(goToCtRecordDetails);
            finish();
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}