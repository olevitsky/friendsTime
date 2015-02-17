package com.friendstime.apps.calex.model;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.widget.Toast;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by oleg on 1/24/2015.
 */
@ParseClassName("eventData")
public class EventData extends ParseObject {
    //String eventName;
    //String inHonorOfLookupURI; // TBD convert to list
    //String occasion;
    //Date date;
    //int duration; // minutes
    //String location;
    //String foodPreference;
    //String notes;
    //String actions;

    //TBD
    //ParseGeoPoint geoLocation
    //int search radius (miles)

    //non-persistent data
    Contact contact;
    public EventData() {
        super();
        contact = null;
    }

    public void setEventData(String eventName, Contact contact, String occasion, String dateFrom,
                              String timeFrom, String timeTo, String location, String foodPreference,
                              String notes, String actions) {


        String in_honorOfLookupUri = contact.getContactLookupURI();
        put("eventName", eventName);
        put("inHonorOfLookupURI",in_honorOfLookupUri );
        put("occasion", occasion);
        String fromDateObjStr = dateFrom + "," + timeFrom;
        String toDateObjStr = dateFrom + "," +timeTo;
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yy,HH:mm");
        try {
            Date fromDateObj = formatter.parse(fromDateObjStr);
            Date toDateObj = formatter.parse(toDateObjStr);
            put("date",fromDateObj );
            int duration = minutesDiff(fromDateObj,toDateObj );
            put("duration", duration);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        put("location", location);
        put("foodPreference", foodPreference);
        put ("notes", notes);
        put ("actions", actions);

    }

    private static int minutesDiff(Date earlierDate, Date laterDate)
    {
        if( earlierDate == null || laterDate == null ) return 0;

        return (int)((laterDate.getTime()/60000) - (earlierDate.getTime()/60000));
    }
    public void save(Context context
    ) {

        String eventName = getString("eventName");
        String inHonorOf = contact.getContactName();
        String inHonorOfFromURI = "???";
        String URIName = "???";
        String occasion = getString("occasion");
        Date fromDateObj = getDate("date");
        String dateFromStr =  fromDateObj.toString();
        int duration = getInt("duration");
        int durationHH = (int) (duration/60);
        int durationMM = duration % 60;
        String durationStr = durationMM +":" + durationHH;
        String location = getString("location");
        String foodPreference = getString("foodPreference");
        String notes = getString("notes");
        String actions = getString("actions");

        String uriString = contact.getContactLookupURI();
        Uri lookupURI = Uri.parse(uriString);
        final String[] contactProjection = new String[] {ContactsContract.Contacts.DISPLAY_NAME};
        Cursor c = context.getContentResolver().query(lookupURI,contactProjection, null, null, null);
        if (c.moveToNext()) { // move to first (and only) row.
            int nameIndex = c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            URIName = c.getString(nameIndex);
        }
        c.close();

        Toast.makeText(context, "EventName= " + eventName + " InHonorOf= " + inHonorOf +
                " URIName= " + URIName +  " Occasion=" + occasion + " dateFrom " + dateFromStr +
                " duration= " + durationStr +
                " location= " + location + " foodPreference = " + foodPreference +  "notes= " + notes +
                " actions= " + actions , Toast.LENGTH_LONG).show();
    }
}