package com.friendstime.apps.calex.model;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by oleg on 1/24/2015.
 */
public class EventData {
    private String m_eventName;
    private String m_in_honorOf; // TBD convert to list
    private String m_occasion;
    private String m_dateFrom;
    private String m_dateTo;
    private String m_timeFrom;
    private String m_timeTo;
    private String m_notes;
    private String m_actions;
    private boolean m_allDay;

    public  void setEventData(String eventName, String in_honorOf, String occasion, String dateFrom,
                             String dateTo, String timeFrom, String timeTo, String notes, String actions, Boolean allDay) {
        m_eventName = eventName;
        m_in_honorOf = in_honorOf;
        m_occasion = occasion;
        m_dateFrom = dateFrom;
        m_dateTo = dateTo;
        m_timeFrom = timeFrom;
        m_timeTo = timeTo;
        m_notes = notes;
        m_actions = actions;
        m_allDay = allDay;
    }

    public   void save(Context context
    ) {

                Toast.makeText(context, "EventName= " + m_eventName + " InHonorOf= " + m_in_honorOf +
                        " Occasion=" + m_occasion + " dateFrom " + m_dateFrom + " dateTo= " + m_dateTo +
                        " timeFrom= " + m_timeFrom + " timeTo= " + m_timeTo + " notes= " + m_notes +
                        " actions= " + m_actions + "IsAllDay= " + m_allDay, Toast.LENGTH_LONG).show();
    }
    public Boolean getM_allDay() {
        return m_allDay;
    }

    public void setM_allDay(Boolean m_allDay) {
        this.m_allDay = m_allDay;
    }

    public String getM_eventName() {
        return m_eventName;
    }

    public void setM_eventName(String m_eventName) {
        this.m_eventName = m_eventName;
    }

    public String getM_in_honorOf() {
        return m_in_honorOf;
    }

    public void setM_in_honorOf(String m_in_honorOf) {
        this.m_in_honorOf = m_in_honorOf;
    }

    public String getM_occasion() {
        return m_occasion;
    }

    public void setM_occasion(String m_occasion) {
        this.m_occasion = m_occasion;
    }

    public String getM_dateFrom() {
        return m_dateFrom;
    }

    public void setM_dateFrom(String m_dateFrom) {
        this.m_dateFrom = m_dateFrom;
    }

    public String getM_dateTo() {
        return m_dateTo;
    }

    public void setM_dateTo(String m_dateTo) {
        this.m_dateTo = m_dateTo;
    }

    public String getM_timeFrom() {
        return m_timeFrom;
    }

    public void setM_timeFrom(String m_timeFrom) {
        this.m_timeFrom = m_timeFrom;
    }

    public String getM_timeTo() {
        return m_timeTo;
    }

    public void setM_timeTo(String m_timeTo) {
        this.m_timeTo = m_timeTo;
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
