package com.neurondigital.selfcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.neurondigital.selfcare.ct.CTRecordDetailsActivity;

public class Day extends AppCompatActivity {

    private TextView garment1;
    private TextView garment2;
    private Intent goToCtRecordDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);

        garment1 = findViewById(R.id.garment1);
        garment2 = findViewById(R.id.garment2);

        String dayOrNight = getIntent().getStringExtra("dayOrNight");

        goToCtRecordDetails = new Intent(Day.this, CTRecordDetailsActivity.class);


        Log.d("dang", dayOrNight);

        garment1.setOnClickListener(click->{
            goToCtRecordDetails.putExtra("name",garment1.getText().toString());
            goToCtRecordDetails.putExtra("dayOrNight",dayOrNight);

            Day.this.startActivity(goToCtRecordDetails);
            finish();
        });

        garment2.setOnClickListener(click->{
            goToCtRecordDetails.putExtra("name",garment2.getText().toString());
            goToCtRecordDetails.putExtra("dayOrNight",dayOrNight);
            Day.this.startActivity(goToCtRecordDetails);
            finish();
        });
    }
}