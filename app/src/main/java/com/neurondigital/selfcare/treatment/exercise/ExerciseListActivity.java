package com.neurondigital.selfcare.treatment.exercise;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.neurondigital.selfcare.R;

import java.util.Collections;
import java.util.Date;
import java.util.List;

public class ExerciseListActivity extends AppCompatActivity {

    ExerciseDatabase db = new ExerciseDatabase(this);
    List<ExerciseModel> records;
    ArrayAdapter<ExerciseModel> adapter;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_list);
        ListView lv = findViewById(R.id.record_list);
        toolbar = findViewById(R.id.exe_toolbar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        records = db.getAll();

        adapter = new ExerciseRecordAdapter(this, records);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ExerciseListActivity.this, ExerciseRecordDetail.class);
                intent.putExtra("record", records.get(position));
                startActivityForResult(intent, 0);
            }
        });
        //loadDataFromDatabase();
        registerForContextMenu(lv);
    }

    public void loadDataFromDatabase() {
        records.clear();
        records.addAll(db.getAll());
        Collections.reverse(records);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loadDataFromDatabase();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.mld_record_menu, menu);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mld_menu_delete:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                int position = (int) info.id;
                db.deleterow(records.get(position).getID());
                records.remove(position);
                adapter.notifyDataSetChanged();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}