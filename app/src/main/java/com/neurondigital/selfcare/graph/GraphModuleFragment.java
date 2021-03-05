package com.neurondigital.selfcare.graph;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.neurondigital.selfcare.R;
import com.neurondigital.selfcare.graph.eventlist.EventListActivity;
import com.neurondigital.selfcare.treatment.manuallymphdrainagemassage.MLDDatabase;
import com.neurondigital.selfcare.treatment.manuallymphdrainagemassage.MLDModel;
import com.neurondigital.selfcare.treatment.skincare.SkinCareDatabase;
import com.neurondigital.selfcare.treatment.skincare.SkinCareModel;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class GraphModuleFragment extends Fragment {
    WeekView mWeekView;
    MLDDatabase mldDB;
    List<MLDModel> mldList;
    FloatingActionButton eventListBtn;

    SkinCareDatabase scDB;
    ArrayList<HashMap<String, String>> scList;
    View view;

    public GraphModuleFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_graph_module, container, false);
        load();
        return view;
    }

    private void load() {
        eventListBtn = view.findViewById(R.id.event_list_btn);

        mldDB = new MLDDatabase(getContext());
        mldList = mldDB.getAll();

        scDB = new SkinCareDatabase(getContext());
        scList = scDB.getAll();

        mWeekView = view.findViewById(R.id.weekView);
        mWeekView.goToHour(7);

        mWeekView.setMonthChangeListener((int newYear, int newMonth) -> {
            List<WeekViewEvent> eventList = new ArrayList<>();
            for(MLDModel mld : mldList) {
                Date start = null;
                Date end = null;
                try {
                    start = MLDModel.DATE_FORMATTER.parse(mld.getStartTime());
                    end = MLDModel.DATE_FORMATTER.parse(mld.getEndTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar calStart = Calendar.getInstance();
                calStart.setTime(start);
                Calendar calEnd = Calendar.getInstance();
                calEnd.setTime(end);
                if(calStart.get(Calendar.MONTH) != newMonth || calEnd.get(Calendar.MONTH) != newMonth ||
                        calStart.get(Calendar.YEAR) != newYear || calEnd.get(Calendar.YEAR) != newYear) continue;
                WeekViewEvent event = new WeekViewEvent(mld.getID(), "MLD", calStart, calEnd);
                event.setColor(getResources().getColor(R.color.brown));
                eventList.add(event);
            }

            for (HashMap<String, String> sc : scList) {
                WeekViewEvent scEvent = loadSkinCareEvents(sc, newYear, newMonth);
                if(scEvent != null){ eventList.add(scEvent); }
            }

            return eventList;
        });


        eventListBtn.setOnClickListener(e -> {
            Intent intent = new Intent(getContext(), EventListActivity.class);
            startActivityForResult(intent, 1);
        });
    }

    public WeekViewEvent loadSkinCareEvents(HashMap<String, String> sc, int newYear, int newMonth) {
            Date start = null;
            Date end = null;
            try {
                start = SkinCareModel.DATE_FORMATTER.parse(sc.get("Date"));

                Calendar cal = Calendar.getInstance();
                cal.setTime(start);
                cal.add(Calendar.MINUTE, 5);
                String endDateStr = SkinCareModel.DATE_FORMATTER.format(cal.getTime());
                end = SkinCareModel.DATE_FORMATTER.parse(endDateStr);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar calStart = Calendar.getInstance();
            calStart.setTime(start);
            Calendar calEnd = Calendar.getInstance();
            calEnd.setTime(end);
            if (calStart.get(Calendar.MONTH) != newMonth || calEnd.get(Calendar.MONTH) != newMonth ||
                    calStart.get(Calendar.YEAR) != newYear || calEnd.get(Calendar.YEAR) != newYear)
                return null;

                WeekViewEvent event = new WeekViewEvent(Integer.parseInt(sc.get("_id")), "SC", calStart, calEnd);
                event.setColor(getResources().getColor(R.color.blue));
                return event;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getFragmentManager()
                .beginTransaction()
                .detach(this)
                .attach(this)
                .commit();
    }
}
