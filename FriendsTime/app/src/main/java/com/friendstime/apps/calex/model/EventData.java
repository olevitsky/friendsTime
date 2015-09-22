package com.friendstime.apps.calex.model;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.widget.Toast;

import com.friendstime.apps.calex.utils.Utility;
import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by oleg on 1/24/2015.
 */
@ParseClassName("eventData")
public class EventData extends ParseObject {
    public final String KEY_EVENT_NAME = "";
    public final String KEY_INHONOROFLOOKUPURI = "inHonorOfLookupURI"; // TBD convert to list
    public final String KEY_OCCASION = "occasion";
    public final String KEY_FROM_DATE =  "date";
    public final String KEY_TO_DATE = "toDate"; //value of string matters?
    public final  String KEY_DURATION = "duration"; // minutes
    public final String KEY_LOCATION = "location";
    public final String KEY_FOODPREFERENCE = "foodPreference";
    public final String KEY_NOTES = "notes";
    public final String KEY_ACTIONS = "actions";
    public final String KEY_START_TIME = "start time";
    public final String KEY_END_TIME = "end time";
    public final String KEY_EVENT_DESCRIPTION = "description";
    public Date KEY_DATE_OBJECT;
    //public final String KEY_CONTACT = "contact";
    private Contact mContact;
    //TBD
    //ParseGeoPoint geoLocation
    //int search radius (miles)

    //DO NOT REMOVE, Required by PARSE
    public EventData() {
        super();

    }



    public Contact getContact() {
        return mContact;
    }

    public void setContact(Contact contact) {
        mContact = contact;
    }

    public String getEventName () {
        return getString(KEY_EVENT_NAME);
    }

    public String getInHonorOfFromURI() {
        return getString(KEY_INHONOROFLOOKUPURI);
    }

    public String getOccasion() {
        return getString(KEY_OCCASION);
    }

    public Date getFromDate() {
        return getDate(KEY_FROM_DATE);
    }

    public String getFromDateString() {
        SimpleDateFormat ft =
                new SimpleDateFormat ("yyyy-MM-dd,hh:mm");

        String dateFromStr =  ft.format(getFromDate());
        return dateFromStr.substring(0, dateFromStr.indexOf(','));
    }

    public Date getToDate() {return getDate(KEY_TO_DATE);}

    public String getToDateString() {
        SimpleDateFormat ft =
                new SimpleDateFormat ("yyyy-MM-dd,hh:mm");

        String dateToStr =  ft.format(getToDate());
        return dateToStr.substring(0, dateToStr.indexOf(','));
    }

    public int getDuration() {
        return getInt(KEY_DURATION);
    }

    //OVERLOADED METHOD - NO PARAMETERS IN THIS ONE. RETURNS STRING FROM TEXT BOX
    public String getEventDescription() { return getString(KEY_EVENT_DESCRIPTION);}

    public String getLocation() {
        return getString(KEY_LOCATION);
    }

    public String getFoodPreference() {
        return getString(KEY_FOODPREFERENCE);
    }

    public ArrayList<String> getNotes () {
        return (ArrayList<String>)get(KEY_NOTES);
    }

    public String getTimeFromString() {
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd,hh:mm");

        String dateFromStr =  ft.format(getFromDate());
        return dateFromStr.substring(dateFromStr.indexOf(",") + 1 , dateFromStr.length());
    }

    public String getTimeToString() {
        SimpleDateFormat ft =
                new SimpleDateFormat ("yyyy-MM-dd,hh:mm");

        String dateToStr =  ft.format(getToDate());
        return dateToStr.substring(dateToStr.indexOf(",") + 1, dateToStr.length());
    }


    public ArrayList<String> getActionNames() {

         return (ArrayList<String>)get("actionName");

    }

    public ArrayList<String> getActionPersons()
    {
        return (ArrayList<String>)get("actionPerson");
    }

    public ArrayList<String> getParticipantNames(){
        return (ArrayList<String>) get("participantName");
    }

    //public ArrayList<Contact> getParticipants(){
     //   return (ArrayList<Contact>)get("participants");
   // }
    public void setEventData(Context context, String eventName, String occasion, String dateFrom,
                              String timeFrom, String timeTo, String eventDescription, String location, String foodPreference,
                              ArrayList<String> notes, ArrayList<String> actionNames, ArrayList<String> actionPersons, ArrayList<String> participantNames) {

        //String in_honorOfLookupUri = contact.getContactLookupURI();
        //put(KEY_INHONOROFLOOKUPURI, in_honorOfLookupUri);
        //put (KEY_CONTACT,contact);
        //mContact = contact;
        put(KEY_EVENT_NAME, eventName);
        //put("inHonorOfLookupURI",in_honorOfLookupUri );
        put(KEY_OCCASION, occasion);
        String dateTimeFrom = timeFrom.length() == 0 ?  "00:00" : timeFrom;
        String dateTimeTo = timeTo.length() == 0 ?  "00:00" : timeTo;
        String fromDateObjStr = dateFrom + "," + dateTimeFrom;
        String toDateObjStr = dateFrom + "," +dateTimeTo;
        //SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy,HH:mm");
        SimpleDateFormat formatter = new
                SimpleDateFormat(Utility.DateFormatMonthDayYearString + ","+Utility.DateFormatTimeString);

        try {
            Date fromDateObj = formatter.parse(fromDateObjStr);
            Date toDateObj = formatter.parse(toDateObjStr);
            put(KEY_FROM_DATE,fromDateObj );
            int duration = minutesDiff(fromDateObj,toDateObj );
            put(KEY_DURATION, duration);
            put(KEY_TO_DATE, toDateObj);
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(context, "DataFormat is wrong " + "fromDateObjStr= " +  fromDateObjStr  +
                    "toDateObjStr= " + toDateObjStr, Toast.LENGTH_SHORT).show();
        }
        put(KEY_EVENT_DESCRIPTION, eventDescription);
        put(KEY_LOCATION, location);
        put(KEY_FOODPREFERENCE, foodPreference);
        put (KEY_NOTES, notes);
        put("actionName", actionNames);
        put("actionPerson", actionPersons);
        put("participantName", participantNames);


    }

    private static int minutesDiff(Date earlierDate, Date laterDate)
    {
        if( earlierDate == null || laterDate == null ) return 0;

        return (int)((laterDate.getTime()/60000) - (earlierDate.getTime()/60000));
    }
    public void save(final Context context
    ) {

        RemoteDBClient.saveEventData(this);
        /*ParseQuery<ParseObject> query = ParseQuery.getQuery("eventData");
        query.fromLocalDatastore();
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, com.parse.ParseException e) {
                if (e == null) {
                    EventData ev = (EventData) object;
                    ev.printDebug(context);

                } else {
                    // something went wrong
                }
            }


        });*/
    }

    public  static void clearDebug(final Context context) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("eventData");
        query.fromLocalDatastore();
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, com.parse.ParseException e) {
                if (e == null) {
                    EventData ev = (EventData) object;
                    ev.printDebug(context);

                } else {
                    // something went wrong
                }
            }


        });
    }

    public String getEventDescription(Context context) {
        String eventName = getString(KEY_EVENT_NAME);
        String inHonorOf = "NOP"; //m_contact.getContactName();
        String inHonorOfFromURI = getString(KEY_INHONOROFLOOKUPURI);
        String URIName = "???";
        String EventDataURIName = "???";
        String occasion = getString(KEY_OCCASION);
        Date fromDateObj = getDate(KEY_FROM_DATE);
        String dateFromStr =  fromDateObj.toString();
        int duration = getInt(KEY_DURATION);
        int durationHH = (int) (duration/60);
        int durationMM = duration % 60;
        String durationStr = durationMM +":" + durationHH;
        String location = getString(KEY_LOCATION);
        String foodPreference = getString(KEY_FOODPREFERENCE);
        String notes = getString(KEY_NOTES);
        String actions = getString(KEY_ACTIONS);
        String uriString = mContact.getContactLookupURI();
        Uri lookupURI = Uri.parse(uriString);
        final String[] contactProjection = new String[] {ContactsContract.Contacts.DISPLAY_NAME};
        Cursor c = context.getContentResolver().query(lookupURI,contactProjection, null, null, null);
        if (c.moveToNext()) { // move to first (and only) row.
            int nameIndex = c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            URIName = c.getString(nameIndex);
        }
        c.close();

        uriString = inHonorOfFromURI;
        lookupURI = Uri.parse(uriString);
        //final String[] contactProjection = new String[] {ContactsContract.Contacts.DISPLAY_NAME};
        c = context.getContentResolver().query(lookupURI,contactProjection, null, null, null);
        if (c.moveToNext()) { // move to first (and only) row.
            int nameIndex = c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            EventDataURIName = c.getString(nameIndex);
        }

        c.close();

        String description = "EventName= " + eventName + " InHonorOf= " + inHonorOf +
                " EventDataURIName= " + EventDataURIName + " URIName= " + URIName +  " Occasion=" + occasion + " dateFrom " + dateFromStr +
                " duration= " + durationStr +
                " location= " + location + " foodPreference = " + foodPreference +  "notes= " + notes +
                " actions= " + actions;
        return description;
    }

    public void printDebug(Context context) {
        Toast.makeText(context, getEventDescription(context), Toast.LENGTH_LONG).show();
    }
}