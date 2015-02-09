package com.friendstime.apps.calex.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by oleg on 2/4/2015.
 */
@ParseClassName("ContactEmail")
public class ContactEmail extends ParseObject{
   // private String address;
   // private String type;
   // private Contact contactId;

    public ContactEmail () {
        super();
        //required for Parse
    }
    public void setContactEmail(String address, String type, Contact cid) {
        put("address" , address);
        put ("type", "type");
        put ("contactId", cid);
    }

    public String getAddress () {
        return (getString("address"));
    }

    public String getType() {
        return (getString("type"));
    }

    public Contact contactId() {
        return ((Contact) get("contactId"));
    }
}