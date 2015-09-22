package com.friendstime.apps.calex.activities;

import com.friendstime.apps.calex.model.Contact;

import java.util.ArrayList;

/**
 * Created by David on 8/18/2015.
 */
public class CreatedEventToday extends SuperCreate {

    public CreatedEventToday(String eventName, String dateFrom, String dateTo, String startTime, String endTime, String eventDescription, ArrayList<String> notes, ArrayList<Action> actions, ArrayList<Contact> participants)
    {
        this.eventName = eventName;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.startTime = startTime;
        this.endTime = endTime;
        this.eventDescription = eventDescription;
        //this.occasion = occasion;
        this.TYPE = 2;
        this.notes = notes;
        this.actions = actions;
        this.participants = participants;

    }
}
