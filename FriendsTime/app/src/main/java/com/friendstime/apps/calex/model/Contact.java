package com.friendstime.apps.calex.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.ArrayList;

/**
 * Created by oleg on 2/4/2015.
 */
@ParseClassName("Contact")
public class Contact extends ParseObject {
    //public String id;
    //public String name;

    public ArrayList<ContactEmail> mEmails;
    public ArrayList<ContactPhone> mNumbers;




    // needed for Parse
    public Contact () {
        super();
        this.mEmails = new ArrayList<ContactEmail>();
        this.mNumbers = new ArrayList<ContactPhone>();
    }

    public void setContact(String id, String name) {

        put("id", id);
        put("name", name);

    }

    public boolean isEmailSet() {
        return (mEmails.size() != 0);
    }

    public boolean isNumberSet() {
        return (mNumbers.size() != 0);
    }
    public int getContactId() {
        return (getInt("id"));
    }

    public String getContactName() {
        return (getString("name"));
    }

    public void addEmail(String address, String type) {
        ContactEmail ce = new ContactEmail();
        ce.setContactEmail(address, type, this);
        mEmails.add(ce);
    }

    public void addNumber(String number, String type) {
        ContactPhone cp = new ContactPhone();
        cp.setContactPhone(number, type, this);
        mNumbers.add(cp);
    }
}
