package com.neurondigital.selfcare.treatment.pneumatic;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.neurondigital.selfcare.R;
import com.neurondigital.selfcare.treatment.manuallymphdrainagemassage.MLDDatabase;
import com.neurondigital.selfcare.treatment.manuallymphdrainagemassage.MLDModel;
import com.neurondigital.selfcare.treatment.manuallymphdrainagemassage.MLDRecordAdapter;
import com.neurondigital.selfcare.treatment.manuallymphdrainagemassage.MLDRecordDetail;

import java.util.Collections;
import java.util.List;

public class PneumaticList extends AppCompatActivity {

    PneumaticDatabase db = new PneumaticDatabase(this);
//    Date startTime;
//    Date endTime;
//    Button timerButton;
//
    List<PneumaticModel> records;
    ArrayAdapter<PneumaticModel> adapter;
//    private Chronometer chronometer;
//    private long pauseOffset;
//    private boolean running;
//    public static final String url = "https://klosetraining.com/resources/self-care-videos/";
//    TextView helpVidText;
    Toolbar toolbar;
//
//    TextView newRecordStartTextView;
//    TextView newRecordEndTextView;
//    TextView resetBtn;
//    TextView continueBtn;
//
//    RelativeLayout resetContinueContainer;
    ListView lv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pc_record_list);
        lv = findViewById(R.id.record_list);


        toolbar = findViewById(R.id.mld_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        records = db.getAll();

        adapter = new PneumaticAdapter(this, records);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(PneumaticList.this, PneumaticRecordDetail.class);
                intent.putExtra("record", records.get(position));
                startActivityForResult(intent, 0);
            }
        });
        loadDataFromDatabase();
        registerForContextMenu(lv);
    }

    public void loadDataFromDatabase() {
        records.clear();
        records.addAll(db.getAll());
        Collections.reverse(records);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.mld_record_menu, menu);

    }
//
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}


