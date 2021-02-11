package com.neurondigital.selfcare.graph;

import android.content.Context;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.neurondigital.selfcare.R;
import com.neurondigital.selfcare.treatment.manuallymphdrainagemassage.MLDDatabase;
import com.neurondigital.selfcare.treatment.manuallymphdrainagemassage.MLDModel;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GraphModuleFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GraphModuleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GraphModuleFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    WeekView mWeekView;
    MLDDatabase mldDB;
    List<MLDModel> mldList;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    public GraphModuleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GraphModuleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GraphModuleFragment newInstance(String param1, String param2) {
        GraphModuleFragment fragment = new GraphModuleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_graph_module, container, false);

        mldDB = new MLDDatabase(getContext());
        mldList = mldDB.getAll();

        mWeekView = view.findViewById(R.id.weekView);

// Set an action when any event is clicked.
        mWeekView.setOnEventClickListener((WeekViewEvent event, RectF eventRect) -> {

        });

// The week view has infinite scrolling horizontally. We have to provide the events of a
// month every time the month changes on the week view.
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


// Set long press listener for events.
        mWeekView.setEventLongPressListener((WeekViewEvent event, RectF eventRect) -> {

        });

//        mWeekView.setDateTimeInterpreter(() -> {
//
//        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
