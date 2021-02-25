package com.neurondigital.selfcare.graph;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class GraphModuleFragment extends Fragment {

    WeekView mWeekView;
    MLDDatabase mldDB;
    List<MLDModel> mldList;
    FloatingActionButton eventListBtn;


    public GraphModuleFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_graph_module, container, false);

        eventListBtn = view.findViewById(R.id.event_list_btn);

        mldDB = new MLDDatabase(getContext());
        mldList = mldDB.getAll();

        mWeekView = view.findViewById(R.id.weekView);


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
            return eventList;
        });

        eventListBtn.setOnClickListener(e -> {
            Intent intent = new Intent(getContext(), EventListActivity.class);
            startActivity(intent);
        });
        return view;
    }


}
