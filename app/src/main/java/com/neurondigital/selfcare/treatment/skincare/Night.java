package com.neurondigital.selfcare.treatment.skincare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import com.neurondigital.selfcare.R;
import com.neurondigital.selfcare.treatment.compressiontherapy.CTRecordDetailsActivity;

public class Night extends AppCompatActivity {

    private LinearLayout garment1;
    private LinearLayout garment2;
    private Toolbar toolbar;
    private Intent goToCtRecordDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_night);

        garment1 = findViewById(R.id.garment1);
        garment2 = findViewById(R.id.garment2);
        toolbar = findViewById(R.id.night_toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        String dayOrNight = getIntent().getStringExtra("dayOrNight");

        goToCtRecordDetails = new Intent(Night.this, CTRecordDetailsActivity.class);

        Log.d("dang", dayOrNight);

        garment1.setOnClickListener(click->{
            goToCtRecordDetails.putExtra("name","Elastic Compression Garment");
            goToCtRecordDetails.putExtra("dayOrNight",dayOrNight);
            Night.this.startActivity(goToCtRecordDetails);
            finish();
        });

        garment2.setOnClickListener(click->{
            goToCtRecordDetails.putExtra("name","Chip Foam Garment");
            goToCtRecordDetails.putExtra("dayOrNight",dayOrNight);
            Night.this.startActivity(goToCtRecordDetails);
            finish();
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}