package com.friendstime.apps.calex.activities;

import com.friendstime.apps.calex.model.Contact;

import java.util.ArrayList;

/**
 * Created by David on 8/15/2015.
 */
public class SuperCreate {

    public String dateFrom;
    public String dateTo;
    public String eventName;
    public String eventDescription;
    public String startTime;
    public String endTime = "?";
    public int TYPE;
    public String text;  // ONLY FOR TEXTBOXES "today's events" and "upcoming events"
    public String occasion;
    public ArrayList<String> notes;
    public ArrayList<Action> actions;
    public ArrayList<Contact> participants;


}
