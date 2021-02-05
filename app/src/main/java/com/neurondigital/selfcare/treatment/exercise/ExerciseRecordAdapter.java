package com.neurondigital.selfcare.treatment.exercise;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.neurondigital.selfcare.R;

import java.util.List;

public class ExerciseRecordAdapter extends ArrayAdapter<ExerciseModel> {
    public ExerciseRecordAdapter(Context context, List<ExerciseModel> records) {
        super(context, 0, records);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ExerciseModel record = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_mld_row, parent, false);
        }
        TextView date = convertView.findViewById(R.id.Date);
        TextView time = convertView.findViewById(R.id.Time);

        date.setText(record.getStartTime());
        time.setText(record.getDuration());
        // Return the completed view to render on screen
        return convertView;
    }

}
