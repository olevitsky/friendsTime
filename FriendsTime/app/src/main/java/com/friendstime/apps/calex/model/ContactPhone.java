package com.friendstime.apps.calex.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by oleg on 2/4/2015.
 */
@ParseClassName("ContactPhone")
public class ContactPhone extends ParseObject{
    //public String number;
    //public String type;
    //Contact contactId

    public ContactPhone () {
        super();

    }

    public void setContactPhone(String number, String type, Contact cid) {
        put("number", number);
        put("type", type);
        put("contactId" , cid);
    }

    public String getType() {
        return (getString("type"));
    }

    public String getNumber() {
        return (getString("number"));
    }

    public Contact contactId() {
        return ((Contact) get("contactId"));
    }
}