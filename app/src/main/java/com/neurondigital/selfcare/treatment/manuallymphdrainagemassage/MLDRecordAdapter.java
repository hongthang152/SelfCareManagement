package com.neurondigital.selfcare.treatment.manuallymphdrainagemassage;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.neurondigital.selfcare.R;

import java.util.List;

public class MLDRecordAdapter extends ArrayAdapter<MLDModel> {
    public MLDRecordAdapter(Context context, List<MLDModel> records) {
        super(context, 0, records);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MLDModel record = getItem(position);
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
