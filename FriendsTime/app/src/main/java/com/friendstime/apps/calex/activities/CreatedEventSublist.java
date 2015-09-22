package com.friendstime.apps.calex.activities;

import com.friendstime.apps.calex.model.Contact;

import java.util.ArrayList;

/**
 * Created by David on 8/15/2015.
 */
public class CreatedEventSublist extends SuperCreate{

    public CreatedEventSublist(String eventName, String dateFrom, String dateTo, String startTime, String endTime, String eventDescription, ArrayList<String> notes, ArrayList<Action> actions, ArrayList<Contact> participants)
    {
        this.eventName = eventName;
        this.TYPE = 1;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.startTime = startTime;
        this.endTime = endTime;
        this.eventDescription = eventDescription;
        //this.occasion = occasion;
        this.notes = notes;
        this.actions = actions;
        this.participants = participants;

    }
}
