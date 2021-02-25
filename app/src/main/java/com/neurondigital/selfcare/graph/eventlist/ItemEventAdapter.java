package com.neurondigital.selfcare.graph.eventlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.neurondigital.selfcare.R;
import com.neurondigital.selfcare.graph.model.ItemEvent;

import java.util.List;

public class ItemEventAdapter extends ArrayAdapter<ItemEvent> {
    public ItemEventAdapter(@NonNull Context context, List<ItemEvent> list) {
        super(context, 0, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ItemEvent itemEvent = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_event_per_day_item, parent, false);
        }

        ImageView eventIcon = convertView.findViewById(R.id.event_icon);
        TextView eventDescDuration = convertView.findViewById(R.id.event_desc_duration);
        TextView eventTime = convertView.findViewById(R.id.event_time);

        eventIcon.setImageDrawable(itemEvent.getIconDrawable());
        eventDescDuration.setText(itemEvent.getActivityDesc());
        eventTime.setText(itemEvent.getStartAndEnd());

        return convertView;
    }
}
