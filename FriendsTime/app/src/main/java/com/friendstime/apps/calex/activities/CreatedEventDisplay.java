package com.friendstime.apps.calex.activities;

import android.widget.ImageButton;

public class CreatedEventDisplay extends SuperCreate {

    //public String date;
    //public String eventDescription;
    public boolean visible;
    public ImageButton button;

    public CreatedEventDisplay(){}

    public CreatedEventDisplay(String eventName, String date, String eventDescription)
    {
        this.date = date;
        this.eventDescription = eventDescription;
        this.TYPE = 0;
        this.eventName = eventName;
    }



}
