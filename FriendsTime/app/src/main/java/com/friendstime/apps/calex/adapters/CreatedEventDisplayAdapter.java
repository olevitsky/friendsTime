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
 * Created by David on 7/31/2015.
 */


public class CreatedEventDisplayAdapter extends ArrayAdapter<SuperCreate> {

    private static class ViewHolder {
        TextView tvDate;
        TextView tvDescription;
        TextView tvEmbeddedName;
        ImageButton ibDelete;
        ImageButton ibEmbeddedDelete;
    }

    public CreatedEventDisplayAdapter(Context context, ArrayList<SuperCreate> displayArray) {
        super(context, 0, displayArray);

    }


    @Override
    public int getItemViewType(int position) {
        return getItem(position).TYPE;
    }


    @Override
    public int getViewTypeCount() {
        // Returns the number of types of Views that will be created by this adapter
        // Each type represents a set of views that can be converted
        return 4;
    }


    public String convertDate(String date){

        String temp = date.substring(5,7); // month
        String day = date.substring(8,10);
        String year = date.substring(0,4);
        if (temp.equals("01"))
            temp = "January";
        else if (temp.equals("02"))
            temp  = "February";
        else if (temp.equals("03"))
            temp  = "March";
        else if (temp.equals("04"))
            temp  = "April";
        else if (temp.equals("05"))
            temp  = "May";
        else if (temp.equals("06"))
            temp  = "June";
        else if (temp.equals("07"))
            temp  = "July";
        else if (temp.equals("08"))
            temp  = "August";
        else if (temp.equals("09"))
            temp  = "September";
        else if (temp.equals("10"))
            temp  = "October";
        else if (temp.equals("11"))
            temp  = "November";
        else if (temp.equals("12"))
            temp = "December";
        return temp + " " + day;

    }




    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        SuperCreate event = (SuperCreate) getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        //   if (convertView == null) {


        if (convertView == null) {
            int type = getItemViewType(position);
            // Inflate XML layout based on the type
            convertView = getInflatedLayoutForType(type);
        }
        TextView tvDate = (TextView) convertView.findViewById(R.id.tvDate);
        TextView tvEventName = (TextView) convertView.findViewById(R.id.tvEventName);
        ImageButton button = (ImageButton) convertView.findViewById(R.id.ibDeleteEvent);
        ImageButton editButtonDisplay = (ImageButton) convertView.findViewById(R.id.ibEditDisplay);
        if (tvDate != null) {
            String convertedDate = convertDate(event.dateFrom);
            tvDate.setText(convertedDate);
        }
        if (tvEventName != null) {
            if (event.eventName.length() < 15) {
                tvEventName.setText(event.eventName);
            } else {
                tvEventName.setText(event.eventName.substring(0, 14) + "...");
            }
        }

        // Return the completed view to render on screen
        if (button != null) {
            button.setTag(new Integer(position));
        }
        if (editButtonDisplay != null){
            editButtonDisplay.setTag(new Integer(position));
        }

        TextView tvEmbeddedName = (TextView) convertView.findViewById(R.id.tvEmbeddedName);
        ImageButton embeddedButton = (ImageButton) convertView.findViewById(R.id.ibEmbeddedDeleteEvent);
        ImageButton editButtonSublist = (ImageButton) convertView.findViewById(R.id.ibEditSublist);
        if (tvEmbeddedName != null) {
            if (event.eventName.length() < 15) {
                tvEmbeddedName.setText(event.eventName);
            } else {
                tvEmbeddedName.setText(event.eventName.substring(0, 14) + "...");
            }
        }
        if (embeddedButton != null) {
            embeddedButton.setTag(new Integer(position));
        }
        if (editButtonSublist != null){
            editButtonSublist.setTag(new Integer(position));
        }


        TextView tvTodayEventName = (TextView) convertView.findViewById(R.id.tvTodayName);
        TextView tvEventStartTime = (TextView) convertView.findViewById(R.id.tvTodayStartTime);
        TextView tvEventEndTime = (TextView) convertView.findViewById(R.id.tvTodayEndTime);
        ImageButton todayButton = (ImageButton) convertView.findViewById(R.id.ibTodayDeleteEvent);
        ImageButton editButtonToday = (ImageButton) convertView.findViewById(R.id.ibEditToday);
//        todayButton.setTag(new Integer(position));

        if (tvTodayEventName != null) {

            if (event.eventName.length() < 15) {
                tvTodayEventName.setText(event.eventName);
            } else {
                tvTodayEventName.setText(event.eventName.substring(0, 14) + "...");
            }
        }


        if (tvEventStartTime != null) {
            tvEventStartTime.setText(event.startTime);
        }
        if (tvEventEndTime != null) {
            if (event.endTime == null || event.endTime.equals("00:00")) {
                tvEventEndTime.setText("?");
            }
            else {
                tvEventEndTime.setText(event.endTime);
            }

        }

        if (todayButton != null){
            todayButton.setTag(new Integer(position));
        }

        if (editButtonToday != null){
            editButtonToday.setTag(new Integer(position));
        }

        TextView tvText = (TextView) convertView.findViewById(R.id.tvText);
        if (tvText != null){
            tvText.setText(event.text);
        }


        return convertView;
    }





        private View getInflatedLayoutForType(int type) {
            if (type == 0) {
                return LayoutInflater.from(getContext()).inflate(R.layout.activity_created_event_display, null);
            } else if (type == 1) {
                return LayoutInflater.from(getContext()).inflate(R.layout.activity_created_event_sublist, null);
            } else if (type == 2){
                return LayoutInflater.from(getContext()).inflate(R.layout.activity_created_event_today, null);
            } else if (type == 3){
                return LayoutInflater.from(getContext()).inflate(R.layout.activity_created_event_text_box, null);
            }

            else{
            return null;}
        }

/*

            if (event instanceof CreatedEventDisplay) {

                convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_created_event_display, parent, false);
                TextView tvDate = (TextView) convertView.findViewById(R.id.tvDate);
                TextView tvEventDescription = (TextView) convertView.findViewById(R.id.tvEventDescription);
                ImageButton button = (ImageButton) convertView.findViewById(R.id.ibDeleteEvent);
                // Populate the data into the template view using the data object
                tvDate.setText(event.date);
                tvEventDescription.setText(event.eventDescription);
                // Return the completed view to render on screen
                button.setTag(new Integer(position));
            }

            if (event instanceof CreatedEventSublist) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_created_event_sublist, parent, false);
                TextView tvEmbeddedName = (TextView) convertView.findViewById(R.id.tvEmbeddedName);
                ImageButton button = (ImageButton) convertView.findViewById(R.id.ibEmbeddedDeleteEvent);
                tvEmbeddedName.setText(event.embeddedName);
                button.setTag(new Integer(position));

            }
        }
        return convertView;
    }*/
}
