package com.friendstime.apps.calex.model;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by oleg on 1/24/2015.
 */
public class EventData {
    private String event_name;
    private String m_in_honor_of; // TBD convert to list
    private String m_occasion;
    private String m_date_from;
    private String m_date_to;
    private String m_time_from;
    private String m_time_to;
    private String m_notes;
    private String m_actions;
    private boolean m_all_day;

    public  void setEventData(String eventName, String in_honorOf, String occasion, String dateFrom,
                             String dateTo, String timeFrom, String timeTo, String notes, String actions, Boolean allDay) {
        event_name = eventName;
        m_in_honor_of = in_honorOf;
        m_occasion = occasion;
        m_date_from = dateFrom;
        m_date_to = dateTo;
        m_time_from = timeFrom;
        m_time_to = timeTo;
        m_notes = notes;
        m_actions = actions;
        m_all_day = allDay;
    }

    public   void save(Context context
    ) {

                Toast.makeText(context, "EventName= " + event_name + " InHonorOf= " + m_in_honor_of +
                        " Occasion=" + m_occasion + " dateFrom " + m_date_from + " dateTo= " + m_date_to +
                        " timeFrom= " + m_time_from + " timeTo= " + m_time_to + " notes= " + m_notes +
                        " actions= " + m_actions + "IsAllDay= " + m_all_day, Toast.LENGTH_LONG).show();
    }
    public Boolean getM_all_day() {
        return m_all_day;
    }

    public void setM_all_day(Boolean m_all_day) {
        this.m_all_day = m_all_day;
    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public String getM_in_honor_of() {
        return m_in_honor_of;
    }

    public void setM_in_honor_of(String m_in_honor_of) {
        this.m_in_honor_of = m_in_honor_of;
    }

    public String getM_occasion() {
        return m_occasion;
    }

    public void setM_occasion(String m_occasion) {
        this.m_occasion = m_occasion;
    }

    public String getM_date_from() {
        return m_date_from;
    }

    public void setM_date_from(String m_date_from) {
        this.m_date_from = m_date_from;
    }

    public String getM_date_to() {
        return m_date_to;
    }

    public void setM_date_to(String m_date_to) {
        this.m_date_to = m_date_to;
    }

    public String getM_time_from() {
        return m_time_from;
    }

    public void setM_time_from(String m_time_from) {
        this.m_time_from = m_time_from;
    }

    public String getM_time_to() {
        return m_time_to;
    }

    public void setM_time_to(String m_time_to) {
        this.m_time_to = m_time_to;
    }

    public String getM_notes() {
        return m_notes;
    }

    public void setM_notes(String m_notes) {
        this.m_notes = m_notes;
    }

    public String getM_actions() {
        return m_actions;
    }

    public void setM_actions(String m_actions) {
        this.m_actions = m_actions;
    }

}
