package com.neurondigital.selfcare.graph.eventlist;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.neurondigital.helpers.Utility;
import com.neurondigital.selfcare.R;
import com.neurondigital.selfcare.graph.model.ItemEvent;
import com.neurondigital.selfcare.treatment.manuallymphdrainagemassage.MLDDatabase;
import com.neurondigital.selfcare.treatment.manuallymphdrainagemassage.MLDModel;
import com.neurondigital.selfcare.treatment.manuallymphdrainagemassage.MLDRecordDetail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class EventListActivity extends AppCompatActivity {

    MLDDatabase mldDB;
//    ExerciseDatabase exerciseDB;
//    PneumaticDatabase pneumaticDB;
//    SkinCareDatabase skincareDB;

    Toolbar toolbar;

    LinearLayout eventListView;

    Map<String, List<ItemEvent>> dayMap;
    private static final String DATE_FORMAT_STRING = "dd-MM-yyyy";
    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(DATE_FORMAT_STRING, Locale.ENGLISH);
    public static final SimpleDateFormat EVENT_LIST_DF = new SimpleDateFormat("EEE, MMM dd, yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        eventListView = findViewById(R.id.event_list);

        mldDB = new MLDDatabase(getBaseContext());
        List<MLDModel> mldEventList = mldDB.getAll();

        dayMap = new HashMap<>();
        for(MLDModel mld : mldEventList) {
            try {
                Date startDate = MLDModel.DATE_FORMATTER.parse(mld.getStartTime());
                String startDateStr = DATE_FORMATTER.format(startDate);
                if(!dayMap.containsKey(startDateStr))
                    dayMap.put(startDateStr, new ArrayList<>());
                Intent intent = new Intent(EventListActivity.this, MLDRecordDetail.class);
                intent.putExtra(MLDRecordDetail.RECORD_EXTRA, mld);
                dayMap.get(startDateStr).add(new ItemEvent(getResources().getDrawable(R.drawable.ic_accessibility_brown_24dp),
                        "You massaged " + Utility.getReadableDuration(mld.getDuration()),
                        ItemEvent.TIME_SIMPLE_DATE_FORMAT.format(MLDModel.DATE_FORMATTER.parse(mld.getStartTime())) + " - " + ItemEvent.TIME_SIMPLE_DATE_FORMAT.format(MLDModel.DATE_FORMATTER.parse(mld.getEndTime())),
                        intent));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        for(Map.Entry<String, List<ItemEvent>> entry : dayMap.entrySet()) {
            View day = LayoutInflater.from(getBaseContext()).inflate(R.layout.activity_event_item, null);
            TextView date  = day.findViewById(R.id.event_item_date);
            try {
                date.setText(EVENT_LIST_DF.format(DATE_FORMATTER.parse(entry.getKey())));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            ListView eventsPerDayListView = day.findViewById(R.id.event_list_per_day);
            eventsPerDayListView.setAdapter(new ItemEventAdapter(getBaseContext(), entry.getValue()));

            eventsPerDayListView.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
                startActivity(entry.getValue().get(position).getDetailActivityIntent());
            });

            eventListView.addView(day);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
