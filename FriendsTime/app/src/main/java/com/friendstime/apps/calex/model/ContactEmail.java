package com.friendstime.apps.calex.model;



/**
 * Created by oleg on 2/4/2015.
 */

public class ContactEmail {
   private String m_address;
   private String m_type;

    public ContactEmail (String address, String type) {
        m_address = address;
        m_type = type;
    }


    public String getAddress () {
        return m_address;
    }

    public String getType() {
        return m_type;
    }
}