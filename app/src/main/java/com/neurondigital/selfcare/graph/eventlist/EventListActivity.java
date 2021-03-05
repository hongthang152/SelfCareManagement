package com.neurondigital.selfcare.graph.eventlist;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.neurondigital.helpers.Utility;
import com.neurondigital.selfcare.R;
import com.neurondigital.selfcare.graph.model.ItemEvent;
import com.neurondigital.selfcare.treatment.manuallymphdrainagemassage.MLDDatabase;
import com.neurondigital.selfcare.treatment.manuallymphdrainagemassage.MLDModel;
import com.neurondigital.selfcare.treatment.manuallymphdrainagemassage.MLDRecordDetail;
import com.neurondigital.selfcare.treatment.skincare.SCRecordDetail;
import com.neurondigital.selfcare.treatment.skincare.SkinCareDatabase;
import com.neurondigital.selfcare.treatment.skincare.SkinCareModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

public class EventListActivity extends AppCompatActivity {

    MLDDatabase mldDB;
    SkinCareDatabase scDB;
//    ExerciseDatabase exerciseDB;
//    PneumaticDatabase pneumaticDB;
//    SkinCareDatabase skincareDB;

    Toolbar toolbar;
    LinearLayout eventListView;

    Map<String, List<ItemEvent>> dayMap = new TreeMap<>((String d1, String d2) -> {
        try {
            Date date1 = DATE_FORMATTER.parse(d1);
            Date date2 = DATE_FORMATTER.parse(d2);
            return date2.compareTo(date1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    });
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

        scDB = new SkinCareDatabase(getBaseContext());
        ArrayList<HashMap<String, String>> scEventList = scDB.getAll();

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

        loadDayMap(scEventList);

        for(Map.Entry<String, List<ItemEvent>> entry : dayMap.entrySet()) {
            View day = LayoutInflater.from(getBaseContext()).inflate(R.layout.activity_event_item, null);
            TextView date  = day.findViewById(R.id.event_item_date);
            try {
                date.setText(EVENT_LIST_DF.format(DATE_FORMATTER.parse(entry.getKey())));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            LinearLayout eventsPerDayListView = day.findViewById(R.id.event_list_per_day);
            for(ItemEvent itemEvent : entry.getValue()) {
                View view = generateItemPerDayView(itemEvent);
                eventsPerDayListView.addView(view);
                view.setOnClickListener(e -> startActivityForResult(itemEvent.getDetailActivityIntent(), 1));
            }

            eventListView.addView(day);
        }


    }


    @Override
    public void onResume() {
        Log.d("onResume", "called");
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.recreate();
    }

    public void loadDayMap(ArrayList<HashMap<String, String>> scEventList) {
        for(HashMap<String, String> sc : scEventList) {
            Log.d("starting SC loop", ":)");
            try {
                Date startDate = SkinCareModel.DATE_FORMATTER.parse(sc.get("Date"));
                String startDateStr = DATE_FORMATTER.format(startDate);

                Calendar cal = Calendar.getInstance();
                cal.setTime(startDate);
                cal.add(Calendar.MINUTE, 5);
                String endDateStr = SkinCareModel.DATE_FORMATTER.format(cal.getTime());

                if(!dayMap.containsKey(startDateStr))
                    dayMap.put(startDateStr, new ArrayList<>());
                Intent intent = new Intent(EventListActivity.this, SCRecordDetail.class);
                intent.putExtra(SCRecordDetail.RECORD_EXTRA, sc);
                dayMap.get(startDateStr).add(new ItemEvent(getResources().getDrawable(R.drawable.skincare_icon),
                        "Skincare performed: " + sc.get("Note"),
                        ItemEvent.TIME_SIMPLE_DATE_FORMAT.format(SkinCareModel.DATE_FORMATTER.parse(sc.get("Date")) ) + " - "
                                +  ItemEvent.TIME_SIMPLE_DATE_FORMAT.format(SkinCareModel.DATE_FORMATTER.parse(endDateStr)),
                        intent));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private View generateItemPerDayView(ItemEvent itemEvent) {
        View convertView = LayoutInflater.from(getBaseContext()).inflate(R.layout.activity_event_per_day_item, null);

        ImageView eventIcon = convertView.findViewById(R.id.event_icon);
        TextView eventDescDuration = convertView.findViewById(R.id.event_desc_duration);
        TextView eventTime = convertView.findViewById(R.id.event_time);

        eventIcon.setImageDrawable(itemEvent.getIconDrawable());
        eventDescDuration.setText(itemEvent.getActivityDesc());
        eventTime.setText(itemEvent.getStartAndEnd());

        return convertView;
    }

}
