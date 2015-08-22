package com.friendstime.apps.calex.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.friendstime.apps.calex.R;
import com.friendstime.apps.calex.activities.SuperCreate;

import java.util.ArrayList;

/**
 * Created by David on 8/18/2015.
 */

// IGNORE THIS!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! ******************************
public class CreatedEventTodayAdapter extends ArrayAdapter<SuperCreate> {


    public CreatedEventTodayAdapter(Context context, ArrayList<SuperCreate> displayArray) {
        super(context, 0, displayArray);

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        SuperCreate todayEvent = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_created_event_today, parent, false);
        }
        // Lookup view for data population
        TextView tvEventName = (TextView) convertView.findViewById(R.id.tvTodayName);
        TextView tvEventStartTime = (TextView) convertView.findViewById(R.id.tvTodayStartTime);
        TextView tvEventEndTime = (TextView) convertView.findViewById(R.id.tvTodayEndTime);
        ImageButton todayButton = (ImageButton) convertView.findViewById(R.id.ibTodayDeleteEvent);
        todayButton.setTag(new Integer(position));
        // Populate the data into the template view using the data object
        if (todayEvent.eventName.length() < 15 ){
        tvEventName.setText(todayEvent.eventName);}
        else{
            tvEventName.setText(todayEvent.eventName.substring(0,14) + "...");
        }

        tvEventStartTime.setText(todayEvent.startTime);
        if (todayEvent.endTime == null){
            tvEventEndTime.setText("?");
        }
        else {
            tvEventEndTime.setText(todayEvent.endTime);
        }

        // Return the completed view to render on screen
        return convertView;
    }



}
