package com.neurondigital.selfcare.treatment.skincare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.neurondigital.selfcare.R;
import com.neurondigital.selfcare.treatment.compressiontherapy.CTRecordDetailsActivity;

public class Night extends AppCompatActivity {

    private TextView garment1;
    private TextView garment2;
    private Intent goToCtRecordDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_night);

        garment1 = findViewById(R.id.garment1);
        garment2 = findViewById(R.id.garment2);

        String dayOrNight = getIntent().getStringExtra("dayOrNight");

        goToCtRecordDetails = new Intent(Night.this, CTRecordDetailsActivity.class);

        Log.d("dang", dayOrNight);

        garment1.setOnClickListener(click->{
            goToCtRecordDetails.putExtra("name",garment1.getText().toString());
            goToCtRecordDetails.putExtra("dayOrNight",dayOrNight);
            Night.this.startActivity(goToCtRecordDetails);
            finish();
        });

        garment2.setOnClickListener(click->{
            goToCtRecordDetails.putExtra("name",garment2.getText().toString());
            goToCtRecordDetails.putExtra("dayOrNight",dayOrNight);
            Night.this.startActivity(goToCtRecordDetails);
            finish();
        });
    }
}