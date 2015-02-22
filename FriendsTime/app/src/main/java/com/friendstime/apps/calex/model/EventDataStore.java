package com.friendstime.apps.calex.model;

import android.content.Context;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by oleg on 2/18/2015.
 */
public class EventDataStore {
    private ArrayList<EventData> mEventDataList;
    private Map<String, Contact> mContactMap;
    private static EventDataStore mInstance;


    private EventDataStore() {
        mEventDataList = new ArrayList<EventData>();
        mContactMap = new HashMap<>();
    }

    public ArrayList<EventData> getEventDataList() {
        return mEventDataList;
    }

    public static EventDataStore getInstance() {
        if (mInstance == null) {
            mInstance = new EventDataStore();
        }
        return mInstance;
    }

    public void addEventData(EventData ed) {
        mEventDataList.add(ed);
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
                            mEventDataList.addAll(eventDatas);
                            for (int i = 0; i < mEventDataList.size(); i++) {
                                EventData ed = mEventDataList.get(i);
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
        ArrayList<EventData> edl = EventDataStore.getInstance().getEventDataList();
        for (int i = 0 ; i < edl.size(); i++)  {
            EventData ed = edl.get(i);
            ed.printDebug(context);
            // delay to see the Toast
           /* try {
                Thread.sleep(5000);
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }*/
        }
    }
    public Contact getContactFromMap(String key) {
        return mContactMap.get(key);
    }
}
