package com.friendstime.apps.calex.model;

import android.content.Context;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.ArrayList;

/**
 * Created by oleg on 2/4/2015.
 */
@ParseClassName("Contact")
public class Contact extends ParseObject {
    //PARSE OBJECTS:
    public final String KEY_CONTENT_LOOKUP_URI = "contentLookupURI";
    public final String KEY_NAME = "name";
    //String contentLookupURI (CONTENT_LOOKUP_URI)
    //String name;

    // Future item
    //String preferences (string of string preferences separated by ':'. Will be more complex object
    // in the future - preferences for food, travel, lodging, etc. May require separate DB objects

    // these two objects will be filled as needed using CONTENT_LOOKUP_URI
    private ArrayList<ContactEmail> m_emails;
    private ArrayList<ContactPhone> m_numbers;
    // Indicates that Emails and Phones have been retrieved. The lists still can be empty
    private boolean mIsEmailNumberSet;
    private String m_localId; //contact id



    // needed for Parse
    public Contact () {
        super();
        this.m_emails = new ArrayList<ContactEmail>();
        this.m_numbers = new ArrayList<ContactPhone>();
        mIsEmailNumberSet = false;
        m_localId = "-1";
    }


    public void setLocalId(String id) {
        m_localId = id;
    }

    public String getLocalId() {
        return m_localId;
    }
    public void setContact(String contentLookupURI, String name) {

        put("KEY_CONTENT_LOOKUP_URI", contentLookupURI);
        put("NAME", name);

    }

    public void setIsEmailNumberSet(boolean v) {
        mIsEmailNumberSet = v;
    }
    public String getContactLookupURI() {
        return (getString("KEY_CONTENT_LOOKUP_URI"));
    }

    public String getContactName() {
        return (getString("NAME"));
    }

    public void addEmail(String address, String type) {
        ContactEmail ce = new ContactEmail(address, type);
        m_emails.add(ce);
    }

    public void addNumber(String number, String type) {
        ContactPhone cp = new ContactPhone(number, type);
        m_numbers.add(cp);
    }

    public boolean isEmailNumberSet() {
        return mIsEmailNumberSet;
    }

    public int numEmails() {
        return m_emails.size();
    }

    public int numNumbers() {
        return m_numbers.size();
    }

    public ContactEmail getFirstEmail() {
        if (m_emails.size()!= 0) {
            return m_emails.get(0);
        }
        return null;
    }

    public ContactPhone getFirstNumber() {
        if (m_numbers.size()!= 0) {
            return m_numbers.get(0);
        }
        return null;
    }

    public void saveContact (Context context) {
        RemoteDBClient.saveContact(this);
    }
}
