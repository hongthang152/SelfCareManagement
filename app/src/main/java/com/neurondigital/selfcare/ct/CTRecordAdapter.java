package com.neurondigital.selfcare.ct;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.neurondigital.selfcare.MLD.MLDModel;
import com.neurondigital.selfcare.R;

import java.util.List;

public class CTRecordAdapter extends ArrayAdapter<CTRecord> {

    public CTRecordAdapter(Context context, List<CTRecord> records) {
        super(context, 0, records);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CTRecord record = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_ct_record_row, parent, false);
        }
        TextView name = convertView.findViewById(R.id.name);
        TextView date = convertView.findViewById(R.id.date);
        TextView time = convertView.findViewById(R.id.time);

        name.setText(record.getName());
        date.setText(record.getStartTime() + " to\n" + record.getEndTime());
        time.setText(record.getDuration());
        // Return the completed view to render on screen
        return convertView;
    }

}
