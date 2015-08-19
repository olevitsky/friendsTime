package com.friendstime.apps.calex.activities;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.friendstime.apps.calex.R;
import com.friendstime.apps.calex.fragments.EventPlannerFragment;
import com.friendstime.apps.calex.model.EventData;
import com.friendstime.apps.calex.model.EventDataStore;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Map;

public class EventPlanner extends SingleFragmentActivity
        implements EventPlannerFragment.OnFragmentInteractionListener {
    public static String TAG = "EventPlanner";
    private int position;
    private int todayPosition;

    @Override
    protected Fragment createFragment() {
        return EventPlannerFragment.newInstance();
    }

    // If i didn't include this method here, app would crash with NoSuchMethod Exception.
    // Method is included in the CreatedEventDisplay java file and xml file. Why does it need to be here also?
    public void onClickDelete(View view) throws ParseException

    {
        position = (Integer)view.getTag();

      /*  Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        Fragment fragInfo = new Fragment();
        fragInfo.setArguments(bundle);
        String p = String.valueOf(position);
        Toast toast = Toast.makeText(this, p, Toast.LENGTH_SHORT);
        toast.show();*/
        EventPlannerFragment frag = (EventPlannerFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        String selectedDate = frag.getSelectedDateCalendar();
      //  frag.eventsArray.remove(position);
        //frag.mCreatedEventDisplayAdapter.notifyDataSetChanged();

        // removing object from array
        SuperCreate object = frag.eventsArray.get(position);
        String objectDate = object.date;
        Map<String, ArrayList<EventData>> theMap = EventDataStore.getInstance().getEventDataMap();
        ArrayList<EventData> arrayOfKey = EventDataStore.getInstance().getEventDataListFromMap(objectDate);
            for (EventData event : arrayOfKey) {
                if (object instanceof CreatedEventDisplay) {

                    if ((event.getEventName().equals(object.eventName))) {
                        Toast toast = Toast.makeText(getApplicationContext(), "removed", Toast.LENGTH_SHORT);
                        toast.show();
                        theMap.get(objectDate).remove(event);
                        frag.displayUpcomingEvents(selectedDate);
                        break;

                    }
                }
                if (object instanceof CreatedEventSublist) {
                    try {
                        if (event.getEventName().equals(object.embeddedName)) {
                            theMap.get(objectDate).remove(event);
                            frag.displayUpcomingEvents(selectedDate);


                            break;
                        }
                    }
                    catch (Exception e)
                    {
                        Toast toast = Toast.makeText(getApplicationContext(), "no event name", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }

            }


        //frag.removeData(position);

    }

    public void onClickDeleteToday(View view) throws ParseException {
        EventPlannerFragment frag = (EventPlannerFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        String selectedDate = frag.getSelectedDateCalendar();
        todayPosition = (Integer) view.getTag();
        SuperCreate object = frag.TodayEventsArray.get(position);

        String objectDate = object.date;
        Map<String, ArrayList<EventData>> theMap = EventDataStore.getInstance().getEventDataMap();
        ArrayList<EventData> arrayOfKey = EventDataStore.getInstance().getEventDataListFromMap(objectDate);

        for (EventData event : arrayOfKey) {
            if ((event.getEventName().equals(object.eventName))) {
                Toast toast = Toast.makeText(getApplicationContext(), "removed", Toast.LENGTH_SHORT);
                toast.show();
                theMap.get(objectDate).remove(event);
                frag.mTodayEventAdapter.remove(object);

                break;
            }

        }
    }


    public void onFragmentInteraction(Uri uri) {
        // Don't know why this is needed.

        Log.d(TAG, "onFragmentInteraction called.");
    }
}
