package com.friendstime.apps.calex.model;

import java.util.ArrayList;

/**
 * Created by oleg on 2/4/2015.
 */
public class Contact {
    public String id;
    public String name;
    public ArrayList<ContactEmail> mEmails;
    public ArrayList<ContactPhone> mNumbers;

    public boolean isEmailSet() {
        return mIsEmailSet;
    }

    public boolean isNumberSet() {
        return mIsNumberSet;
    }

    private boolean mIsEmailSet = false;
    private boolean mIsNumberSet = false;

    public Contact(String id, String name) {
        this.id = id;
        this.name = name;
        this.mEmails = new ArrayList<ContactEmail>();
        this.mNumbers = new ArrayList<ContactPhone>();
    }

    public void addEmail(String address, String type) {
        mEmails.add(new ContactEmail(address, type));
        mIsEmailSet = true;
    }

    public void addNumber(String number, String type) {
        mNumbers.add(new ContactPhone(number, type));
        mIsNumberSet = true;
    }
}
