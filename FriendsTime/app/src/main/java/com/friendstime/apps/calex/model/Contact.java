package com.friendstime.apps.calex.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.ArrayList;

/**
 * Created by oleg on 2/4/2015.
 */
@ParseClassName("Contact")
public class Contact extends ParseObject implements Parcelable  {
    //PARSE OBJECTS:
    public final String KEY_CONTENT_LOOKUP_URI = "contentLookupURI";
    public final String KEY_NAME = "name";
    public String name;
    public int checked;
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
        put("name", name);
        this.name = name;

    }

    public void setContact(int checked){
        put("checked", checked);
    }

    public void setIsEmailNumberSet(boolean v) {
        mIsEmailNumberSet = v;
    }
    public String getContactLookupURI() {
        return (getString("KEY_CONTENT_LOOKUP_URI"));
    }

    public String getContactName() {
        return (getString("name"));
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


    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(name);
        out.writeInt(checked);
    }

    private Contact(Parcel in) {
        name = in.readString();
        checked = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

// After implementing the `Parcelable` interface, we need to create the
// `Parcelable.Creator<MyParcelable> CREATOR` constant for our class;
// Notice how it has our class specified as its type.
public static final Parcelable.Creator<Contact> CREATOR
        = new Parcelable.Creator<Contact>() {

    // This simply calls our new constructor (typically private) and
    // passes along the unmarshalled `Parcel`, and then returns the new object!
    @Override
    public Contact createFromParcel(Parcel in) {
        return new Contact(in);
    }

    // We just need to copy this and change the type to match our class.
    @Override
    public Contact[] newArray(int size) {
        return new Contact[size];
    }
};
}