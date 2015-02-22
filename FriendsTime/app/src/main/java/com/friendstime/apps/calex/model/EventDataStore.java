package com.friendstime.apps.calex.model;

import android.content.Context;
import android.widget.Toast;

import com.friendstime.apps.calex.utils.Utility;
import com.parse.FindCallback;
import com.parse.ParseException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by oleg on 2/18/2015.
 */
public class EventDataStore {
    //private ArrayList<EventData> mEventDataList;
    private Map<String, List<EventData>> mEventDataMap;
    private Map<String, Contact> mContactMap;
    private static EventDataStore mInstance;


    private EventDataStore() {
        //mEventDataList = new ArrayList<EventData>();
        mEventDataMap = new HashMap<>();
        mContactMap = new HashMap<>();
    }

    private Map<String, List<EventData>> getEventDataMap() {
        return mEventDataMap;
    }

    public static EventDataStore getInstance() {
        if (mInstance == null) {
            mInstance = new EventDataStore();
        }
        return mInstance;
    }

    private List getEventDataListFromMap(String key) {
        List evdList;
        if(mEventDataMap.containsKey(key)) {
            evdList = mEventDataMap.get(key);
        } else {
            evdList = new ArrayList<EventData>();
        }
        return evdList;
    }
    public void addEventData(EventData ed) {
        //mEventDataList.add(ed);
        String key = getEventDataMapKey(ed);
        List evdList = getEventDataListFromMap(key);
        evdList.add(ed);
        mEventDataMap.put(key, evdList);
        mContactMap.put(ed.getInHonorOfFromURI(), ed.getContact());

    }
    public void populateEventData(final Context context) {
        RemoteDBClient.getAllContactData(context, new FindCallback<Contact>() {
            @Override
            public void done(List<Contact> contacts, ParseException e) {
                if(e==null) {
                    for(int i = 0; i < contacts.size(); i++) {
                        Contact contact = contacts.get(i);
                        mContactMap.put(contact.getContactLookupURI(), contact);
                    }
                    RemoteDBClient.getAllEventData(context, new FindCallback<EventData>() {
                        @Override
                        public void done(List<EventData> eventDatas, ParseException e) {
                           // mEventDataList.addAll(eventDatas);
                            for (int i = 0 ; i< eventDatas.size(); i++) {
                                EventData ed = eventDatas.get(i);
                                String key = getEventDataMapKey(ed);
                                List evdList = getEventDataListFromMap(key);
                                evdList.add(ed);
                                mEventDataMap.put(key, evdList);
                                ed.setContact(mContactMap.get(ed.getInHonorOfFromURI()));
                            }
                        }
                    });

                } else {
                    Toast.makeText(context, "getAllContactData FAILED", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void debugPrint(Context context) {
        //ArrayList<EventData> edl = EventDataStore.getInstance().getEventDataList();
        for(List<EventData> evdList : EventDataStore.getInstance().getEventDataMap().values()) {
            for(int i = 0; i < evdList.size(); i++) {
                EventData ed = evdList.get(i);
                ed.printDebug(context);
            }
        }
    }
    public Contact getContactFromMap(String key) {
        return mContactMap.get(key);
    }

    private String getEventDataMapKey(EventData ed) {
        Date edDate = ed.getFromDate();
        DateFormat edDateFormatter = new SimpleDateFormat(Utility.DateFormatMonthDayYearString);
        String key = edDateFormatter.format(edDate);
        return key;
    }
}
