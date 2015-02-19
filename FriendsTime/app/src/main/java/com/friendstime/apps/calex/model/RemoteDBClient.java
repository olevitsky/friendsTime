package com.friendstime.apps.calex.model;

import android.content.Context;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Created by oleg on 2/18/2015.
 */
public class RemoteDBClient {
    public static void removeAllCachedData(){
        ParseObject.unpinAllInBackground();
    }


    public static void saveContact(Contact contact) {
        // for future save in DB
        //contact.saveInBackground();
        contact.pinInBackground();
    }

    public static void saveEventData(EventData evData) {
        // for future save in DB
        //contact.saveInBackground();
        evData.pinInBackground();
    }

    public static void getAllEventData(final Context context, final FindCallback<EventData>callback) {
        ParseQuery<EventData> query = ParseQuery.getQuery(EventData.class);
        query.fromLocalDatastore();
        query.findInBackground(new FindCallback<EventData>() {
            @Override
            public void done(List<EventData> objects, ParseException e) {
                if (e == null) {
                    callback.done(objects, e);
                } else {
                    Toast.makeText(context, "getAllEventData FAILED", Toast.LENGTH_SHORT).show();
                    //callback.fail(e.getMessage());
                }
            }
        });
    }

    public static void getAllContactData (final Context context, final FindCallback<Contact>callback) {
        ParseQuery<Contact> query = ParseQuery.getQuery(Contact.class);
        query.fromLocalDatastore();
        query.findInBackground(new FindCallback<Contact>() {
            @Override
            public void done(List<Contact> objects, ParseException e) {
                if (e == null) {
                    callback.done(objects, e);
                } else {
                    Toast.makeText(context, "getAllEventData FAILED", Toast.LENGTH_SHORT).show();
                    //callback.fail(e.getMessage());
                }
            }
        });
    }
}
