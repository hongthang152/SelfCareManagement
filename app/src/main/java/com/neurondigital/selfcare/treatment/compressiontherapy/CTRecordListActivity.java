package com.neurondigital.selfcare.treatment.compressiontherapy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.neurondigital.selfcare.R;

import java.util.ArrayList;
import java.util.List;

public class CTRecordListActivity extends AppCompatActivity {

    CTDatabase db;
    ListView ct_list;
    List<CTRecord> records;
    ArrayAdapter<CTRecord> adapter;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c_t_record_list);

        toolbar = findViewById(R.id.record_detail_bar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //initializing database
        db = new CTDatabase(this);

        //initializing listview
        ct_list = findViewById(R.id.ct_list);

        //initializing arraylist
        records = new ArrayList<>();

        //initializing adapter
        adapter = new CTRecordAdapter(this, records);

        //setting up adapter
        ct_list.setAdapter(adapter);

        //on click, open to update CT Record
        ct_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CTRecordListActivity.this,CTRecordDetailsActivity.class);
                intent.putExtra("updateRecord",records.get(position));
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //refreshing data whenever this activity is resumed
        records.clear();
        records.addAll(db.getAllCTRecords());
        adapter.notifyDataSetChanged();
    }
}