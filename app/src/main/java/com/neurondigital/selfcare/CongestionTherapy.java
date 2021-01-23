package com.neurondigital.selfcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toolbar;

public class CongestionTherapy extends AppCompatActivity {
Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congestion_therapy);
        Button day = findViewById(R.id.day);
        Button night = findViewById(R.id.night);
        day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View btn) {
                Intent goToDay = new Intent(CongestionTherapy.this, Day.class);
                CongestionTherapy.this.startActivity(goToDay);
            }
        });
        night.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View btn) {
                Intent goToNight = new Intent(CongestionTherapy.this, Night.class);
                CongestionTherapy.this.startActivity(goToNight);
            }
        });

    }
}