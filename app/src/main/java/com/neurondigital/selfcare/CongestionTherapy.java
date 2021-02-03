package com.neurondigital.selfcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toolbar;

import com.neurondigital.selfcare.ct.CTRecordListActivity;

public class CongestionTherapy extends AppCompatActivity {
Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congestion_therapy);
        Button day = findViewById(R.id.day);
        Button night = findViewById(R.id.night);
        Button btn_open_ct_record_list = findViewById(R.id.btn_open_ct_record_list);
        day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View btn) {
                Intent goToDay = new Intent(CongestionTherapy.this, Day.class);
                goToDay.putExtra("dayOrNight","Day");
                CongestionTherapy.this.startActivity(goToDay);
            }
        });
        night.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View btn) {
                Intent goToNight = new Intent(CongestionTherapy.this, Night.class);
                goToNight.putExtra("dayOrNight","Night");
                CongestionTherapy.this.startActivity(goToNight);
            }
        });

        btn_open_ct_record_list.setOnClickListener(click ->{
            Intent goToctrecordlist = new Intent(CongestionTherapy.this, CTRecordListActivity.class);
            CongestionTherapy.this.startActivity(goToctrecordlist);
        });
    }
}