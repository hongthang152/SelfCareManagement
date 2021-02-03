package com.neurondigital.selfcare.Pneumatic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.neurondigital.selfcare.R;

import java.util.List;

public class PneumaticAdapter extends ArrayAdapter<PneumaticModel> {

    public PneumaticAdapter( Context context, List<PneumaticModel> records) {
        super(context, 0,records);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PneumaticModel record = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_pneumatic_row, parent, false);
        }
        TextView date = convertView.findViewById(R.id.Date1);
        TextView time = convertView.findViewById(R.id.Time1);

        date.setText(record.getStartTime());
        time.setText(record.getDuration());
        // Return the completed view to render on screen
        return convertView;
    }
}
