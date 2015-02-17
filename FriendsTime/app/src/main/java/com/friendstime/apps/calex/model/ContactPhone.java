package com.friendstime.apps.calex.model;


/**
 * Created by oleg on 2/4/2015.
 */

public class ContactPhone {
    private String m_number;
    private String m_type;

    public ContactPhone (String number, String type) {
        m_number = number;
        m_type = type;
    }

    public String getType() {
        return m_type;
    }

    public String getNumber() {
        return m_number;
    }

}