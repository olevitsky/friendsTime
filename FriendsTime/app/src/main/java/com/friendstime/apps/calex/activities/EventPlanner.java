package com.friendstime.apps.calex.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.friendstime.apps.calex.R;
import com.friendstime.apps.calex.fragments.EventPlannerFragment;
import com.friendstime.apps.calex.model.Contact;
import com.friendstime.apps.calex.model.EventData;
import com.friendstime.apps.calex.model.EventDataStore;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Map;

public class EventPlanner extends SingleFragmentActivity
        implements EventPlannerFragment.OnFragmentInteractionListener {
    public static String TAG = "EventPlanner";
    private int position;
    private final int REQUEST_CODE = 500;

    @Override
    protected Fragment createFragment() {
        return EventPlannerFragment.newInstance();
    }

    // If i didn't include this method here, app would crash with NoSuchMethod Exception.
    // Method is included in the CreatedEventDisplay java file and xml file. Why does it need to be here also?
    public void onClickDelete(View view) throws ParseException

    {
        position = (Integer)view.getTag();

        EventPlannerFragment frag = (EventPlannerFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        String selectedDate = frag.getSelectedDateCalendar();

        // removing object from array
        SuperCreate object = frag.eventsArray.get(position);
        String objectDate = object.dateFrom;
        Map<String, ArrayList<EventData>> theMap = EventDataStore.getInstance().getEventDataMap();
        ArrayList<EventData> arrayOfKey = EventDataStore.getInstance().getEventDataListFromMap(objectDate);
            for (EventData event : arrayOfKey) {

                    if ((event.getEventName().equals(object.eventName))) {
                        Toast toast = Toast.makeText(getApplicationContext(), "removed", Toast.LENGTH_SHORT);
                        toast.show();
                        theMap.get(objectDate).remove(event);
                        frag.displayEventDataForDate(selectedDate);
                        break;
                    }

            }

    }

    public void onClickEdit(View view)
    {
        position = (Integer)view.getTag();
        EventPlannerFragment frag = (EventPlannerFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        SuperCreate object = frag.eventsArray.get(position);
        String selectedDate = frag.getSelectedDateCalendar();
        Intent i = new Intent(EventPlanner.this, CreateEventActivity.class);
        i.putExtra("name", object.eventName);
        i.putExtra("dateFrom", object.dateFrom);
        i.putExtra("dateTo", object.dateTo);
        i.putExtra("startTime", object.startTime);
        i.putExtra("endTime", object.endTime);
        i.putExtra("description", object.eventDescription);
        i.putExtra("selectedDate", selectedDate);
        i.putExtra("intent", "eventPlanner");
        i.putStringArrayListExtra("notes", object.notes);
        i.putParcelableArrayListExtra("actions", object.actions);
        i.putParcelableArrayListExtra("participants", object.participants);
        startActivityForResult(i, REQUEST_CODE);

    }



    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Toast toast = Toast.makeText(getApplicationContext(), "eventplanner NOT FRAG", Toast.LENGTH_SHORT);
        toast.show();
        if (resultCode == RESULT_CANCELED){
            return;
        }
        EventPlannerFragment frag = (EventPlannerFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        String name = data.getStringExtra("name");
        String dateFrom = data.getStringExtra("dateFrom");
        String dateTo = data.getStringExtra("dateTo");
        String startTime = data.getStringExtra("startTime");
        String endTime = data.getStringExtra("endTime");
        String description = data.getStringExtra("description");
        String selectedDate = data.getStringExtra("selectedDate");
        ArrayList<String> mArrayNotes = data.getStringArrayListExtra("notes");
        ArrayList<Action> mArrayActions = data.getParcelableArrayListExtra("actions");
        ArrayList<Contact> mArrayContacts = data.getParcelableArrayListExtra("participants");
        SuperCreate editedObject = new CreatedEventDisplay(name, dateFrom, dateTo, startTime, endTime, description, mArrayNotes, mArrayActions, mArrayContacts);

        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            Toast toast2 = Toast.makeText(getApplicationContext(), Integer.toString(mArrayNotes.size()), Toast.LENGTH_SHORT);
            toast2.show();


            SuperCreate object = frag.eventsArray.get(position);
            String objectDate = object.dateFrom;
            Map<String, ArrayList<EventData>> theMap = EventDataStore.getInstance().getEventDataMap();
            ArrayList<EventData> arrayOfKey = EventDataStore.getInstance().getEventDataListFromMap(objectDate);
            for (EventData event : arrayOfKey) {

                if ((event.getEventName().equals(object.eventName))) {
                    theMap.get(objectDate).remove(event);
                    Toast toast1 = Toast.makeText(getApplicationContext(), "removed", Toast.LENGTH_SHORT);
                    toast1.show();
                    break;
                }

            }
        }

        frag.mCreatedEventDisplayAdapter.add(editedObject);
        try {
            frag.displayEventDataForDate(selectedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }






    public void onFragmentInteraction(Uri uri) {
        // Don't know why this is needed.

        Log.d(TAG, "onFragmentInteraction called.");
    }
}
