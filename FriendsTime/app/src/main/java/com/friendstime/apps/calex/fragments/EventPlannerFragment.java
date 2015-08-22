package com.friendstime.apps.calex.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.friendstime.apps.calex.R;
import com.friendstime.apps.calex.activities.CreateEventActivity;
import com.friendstime.apps.calex.activities.CreatedEventDisplay;
import com.friendstime.apps.calex.activities.CreatedEventSublist;
import com.friendstime.apps.calex.activities.CreatedEventTextBox;
import com.friendstime.apps.calex.activities.CreatedEventToday;
import com.friendstime.apps.calex.activities.SuperCreate;
import com.friendstime.apps.calex.adapters.CalendarAdapter;
import com.friendstime.apps.calex.adapters.CreatedEventDisplayAdapter;
import com.friendstime.apps.calex.adapters.CreatedEventTodayAdapter;
import com.friendstime.apps.calex.model.CurrentData;
import com.friendstime.apps.calex.model.EventData;
import com.friendstime.apps.calex.model.EventDataStore;
import com.friendstime.apps.calex.utils.Utility;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EventPlannerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EventPlannerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventPlannerFragment extends Fragment  {
    private OnFragmentInteractionListener mListener;

    // Calendar instances.
    public GregorianCalendar mItemMonth;

    // Current Month Data.
    public CurrentData mCurrentData;

    // Adapter to display calendar.
    public CalendarAdapter mCalendarAdapter;

    // For grabbing some mEvent values for showing the dot.
    public Handler mHandler;

    // container to store calendar mItems which
    // Needs showing the mEvent marker.
    public ArrayList<String> mItems;
    ArrayList<String> mEvent;

    // Row Layout to show the description of events.
    LinearLayout mRowLayout;

    //ArrayList<String> date;

    // Descriptions of the events.
    ArrayList<EventData> mDesc;

    TextView mTitleView;

    ImageButton mCreateNewEvent;

    private final int REQUEST_CODE = 20;

    private String nameInHonorOf;
    private String eventDescription;
    private String eventDate;
    private String selectedDateCalendar; // this will be passed stored and passed on to refresh list view of upcoming events in eventPlanner upon deletion
    public ArrayList <SuperCreate> eventsArray;
    public ArrayList<SuperCreate> TodayEventsArray;
    public ArrayAdapter<SuperCreate> mCreatedEventDisplayAdapter; // = new CreatedEventDisplayAdapter(getActivity(), eventsArray);
    public CreatedEventTodayAdapter mTodayEventAdapter;
    public enum DisplayView {
        VIEW_MONTH, VIEW_WEEK, VIEW_DAY, VIEW_SURPRISE
    }
    public DisplayView mDisplayView;


    /**
     * Use this factory method to create a new instance of
     * this fragment.
     * @return A new instance of fragment PlannerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EventPlannerFragment newInstance() {
        EventPlannerFragment fragment = new EventPlannerFragment();
        return fragment;
    }

    public EventPlannerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_planner, menu);



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_month:
                mDisplayView = DisplayView.VIEW_MONTH;
                refreshCalendar();
                return true;
            case R.id.menu_item_week:
                mDisplayView = DisplayView.VIEW_WEEK;
                refreshCalendar();
                return true;
            case R.id.menu_item_day:
                // No grid view. Text should be day + month + year.
                mDisplayView = DisplayView.VIEW_DAY;
                refreshCalendar();
                try {
                    displayEventDataForDate(mCurrentData.getSelectedDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return true;
            case R.id.menu_item_surprise:
                Intent i = new Intent(getActivity(), CreateEventActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDisplayView = DisplayView.VIEW_MONTH;
        setHasOptionsMenu(true);
        //Bundle args = getActivity().getIntent().getExtras();
        //nameInHonorOf= args.getString("name");
        //eventDescription= args.getString("description");
        //eventDate=args.getString("date");




    }





        @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment.
        View v = inflater.inflate(R.layout.fragment_planner, container, false);
        Locale.setDefault(Locale.US);

        //mRowLayout = (LinearLayout) v.findViewById(R.id.event_description);
        mCurrentData = new CurrentData();
        mItemMonth = (GregorianCalendar) mCurrentData.getCurrentMonth().clone();
//*************** EDITS ************************

        eventsArray = new ArrayList<SuperCreate>();
        mCreatedEventDisplayAdapter = new CreatedEventDisplayAdapter(getActivity(), eventsArray);
            ListView lvEventDisplay = (ListView) v.findViewById(R.id.lvEventDisplay);
            lvEventDisplay.setAdapter(mCreatedEventDisplayAdapter);


        mItems = new ArrayList<String>();
        mCalendarAdapter = new CalendarAdapter(getActivity(), mCurrentData);
        GridView gridview = (GridView) v.findViewById(R.id.gridview);
        gridview.setAdapter(mCalendarAdapter);
        // No need to populate again and again.
        if (EventDataStore.getInstance().getEventDataMap().size() <= 0) {
            EventDataStore.getInstance().populateEventData(getActivity());
        }
        mHandler = new Handler();

        mTitleView = (TextView) v.findViewById(R.id.title);
        mTitleView.setText(android.text.format.DateFormat.format("MMMM yyyy",
                mCurrentData.getCurrentMonth()));

        ImageView previous = (ImageView) v.findViewById(R.id.previous);

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentData.setPreviousView(mDisplayView);
                refreshCalendar();
                if (mDisplayView == DisplayView.VIEW_DAY) {
                    try {
                        displayEventDataForDate(mCurrentData.getSelectedDate());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        ImageView next = (ImageView) v.findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentData.setNextView(mDisplayView);
                refreshCalendar();
                if (mDisplayView == DisplayView.VIEW_DAY) {
                    try {
                        displayEventDataForDate(mCurrentData.getSelectedDate());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                // date = new ArrayList<String>();
                ((CalendarAdapter) parent.getAdapter()).setSelected(v);
                String selectedGridDate = CalendarAdapter.mDayStrings
                        .get(position);
                String[] separatedTime = selectedGridDate.split("-");
                selectedDateCalendar = selectedGridDate;

                // Taking last part of date. ie; 2 from 2012-12-02.
                String gridvalueString = separatedTime[2].replaceFirst("^0*", "");

                int gridvalue = Integer.parseInt(gridvalueString);
                // navigate to next or previous mCurrentMonth on clicking offdays.
                if (mDisplayView == DisplayView.VIEW_MONTH) {
                    if ((gridvalue > 10) && (position < 8)) {
                        mCurrentData.setPreviousView(mDisplayView);
                        refreshCalendar();

                   /* OLEG HACK, Need to figure out how to support months with different number of rows.
                   Previous implementation SEGV for August 3 (August has more rows than other months. Same is for January
                    */

                   // } else if ((gridvalue < 7) && (position > 28)) {
                    } else if ((gridvalue < 15) && (position > 28)) {
                        mCurrentData.setNextView(mDisplayView);
                        refreshCalendar();
                    }
                }
                ((CalendarAdapter) parent.getAdapter()).setSelected(v);
                mCurrentData.setSelectedDate(CalendarAdapter.mDayStrings.get(position));
                try {
                    displayEventDataForDate(selectedGridDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

        });
        mCreateNewEvent = (ImageButton) v.findViewById(R.id.create_new_event);
        mCreateNewEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = CreateEventActivity.newIntent(getActivity(),
                        mCurrentData.getSelectedDate());
                startActivityForResult(i, REQUEST_CODE);

            }
        });
        // Run calenderUpdater2 after delay of 2 secs to give chance to
        // get eventdata getting populated.
        mHandler.postDelayed(calendarUpdater2, 2000);

        return v;
    }


public void removeData(int position)
{
    eventsArray.remove(position);
    mCreatedEventDisplayAdapter.notifyDataSetChanged();
}


    public void displayEventDataForDate(String selectedDate) throws ParseException {
        // removing the previous view if added
//        if (((LinearLayout) mRowLayout).getChildCount() > 0) {
        //          ((LinearLayout) mRowLayout).removeAllViews();
        //    }

        mDesc = new ArrayList<EventData>();
        //showToast(selectedDate);

        ArrayList<EventData> allEvents = EventDataStore.getInstance().
                getEventDataListFromMap(selectedDate);
        //showToast(Integer.toString(allEvents.size()));
        for (EventData eventData : allEvents) {
            String eventDate = eventData.getFromDateString();
            if (eventDate.equals(selectedDate)) {
                mDesc.add(eventData);
            }
        }
        Collections.sort(mDesc, new Comparator<EventData>() {
            @Override
            public int compare(EventData lhs, EventData rhs) {
                return lhs.getFromDate().compareTo(rhs.getFromDate());
            }
        });

       /* TodayEventsArray = new ArrayList<SuperCreate>();
        mTodayEventAdapter = new CreatedEventTodayAdapter(getActivity(), TodayEventsArray);
        ListView lvTodayEventDisplay = (ListView) getView().findViewById(R.id.lvTodayEvents);
        lvTodayEventDisplay.setAdapter(mTodayEventAdapter);
        mTodayEventAdapter.clear();
        mTodayEventAdapter.notifyDataSetChanged();
        mCreatedEventDisplayAdapter.clear();
        mCreatedEventDisplayAdapter.notifyDataSetChanged();
        if (mDesc.size() > 0) {

            for (int i = 0; i < mDesc.size(); i++) {
                SuperCreate todayEvent = new CreatedEventToday(mDesc.get(i).getEventName(), mDesc.get(i).getFromDateString(), mDesc.get(i).getTimeFrom() + " - ", mDesc.get(i).getTimeTo()); // date passed in twice because textbox only contains 1st parameter
               mCreatedEventDisplayAdapter.add(todayEvent);
                TextView rowTextView = new TextView(getActivity());

                // set some properties of rowTextView or something
                //DAVID HACK

                //
                rowTextView.setMaxLines(1);
                rowTextView.setEllipsize(TextUtils.TruncateAt.END);
                rowTextView.setText("Today's Event: " + mDesc.get(i));
                rowTextView.setTextColor(Color.parseColor("#303F9F"));

                // add the textview to the linearlayout
                mRowLayout.addView(rowTextView);
            }*/

       // displayUpcomingEvents(selectedDate);
        mCreatedEventDisplayAdapter.clear();
        mCreatedEventDisplayAdapter.notifyDataSetChanged();
        // adding "Today Events"
        if (mDesc.size() > 0) {
            SuperCreate textBoxTodaysEvents = new CreatedEventTextBox("Today's Events");
            mCreatedEventDisplayAdapter.add(textBoxTodaysEvents);
            for (int i = 0; i < mDesc.size(); i++) {
                SuperCreate todayEvent = new CreatedEventToday(mDesc.get(i).getEventName(), mDesc.get(i).getFromDateString(), mDesc.get(i).getTimeFrom() + " - ", mDesc.get(i).getTimeTo()); // date passed in twice because textbox only contains 1st parameter
                mCreatedEventDisplayAdapter.add(todayEvent);
            }
        }
        // adding "Upcoming Events"

            ArrayList<EventData> ArrayUpcomingEvents = getUpcomingEventsArray(selectedDate);
        Collections.sort(ArrayUpcomingEvents, new Comparator<EventData>() {
            @Override
            public int compare(EventData lhs, EventData rhs) {
                return lhs.getFromDate().compareTo(rhs.getFromDate());
            }
        });
            if (ArrayUpcomingEvents.size() != 0) {
                SuperCreate textBoxUpcomingEvents = new CreatedEventTextBox("Upcoming Events:");
                mCreatedEventDisplayAdapter.add(textBoxUpcomingEvents);
                CreatedEventDisplay firstEvent = new CreatedEventDisplay(ArrayUpcomingEvents.get(0).getEventName(), ArrayUpcomingEvents.get(0).getFromDateString(), "event description");


                mCreatedEventDisplayAdapter.add(firstEvent);
                for (int i = 1; i < ArrayUpcomingEvents.size(); i++) {
                    EventData currentEvent = ArrayUpcomingEvents.get(i);
                    EventData previousEvent = ArrayUpcomingEvents.get(i - 1);
                    if (currentEvent.getFromDateString().equals(previousEvent.getFromDateString())) {
                        CreatedEventSublist subEvent = new CreatedEventSublist(currentEvent.getEventName(), currentEvent.getFromDateString());
                        mCreatedEventDisplayAdapter.add(subEvent);
                    } else {
                        CreatedEventDisplay event = new CreatedEventDisplay(currentEvent.getEventName(), currentEvent.getFromDateString(), "event description");
                        mCreatedEventDisplayAdapter.add(event);
                    }

                }
            }



        mDesc = null;



    }
//DAVID HACK

    /*public void displayUpcomingEvents(String date) throws ParseException {
        // mCreatedEventDisplayAdapter.clear();
        // mCreatedEventDisplayAdapter.notifyDataSetChanged();
        //showToast("cleared");

        mCreatedEventDisplayAdapter.clear();
        mCreatedEventDisplayAdapter.notifyDataSetChanged();
        if (mDesc.size() > 0) {

            for (int i = 0; i < mDesc.size(); i++) {
                SuperCreate todayEvent = new CreatedEventToday(mDesc.get(i).getEventName(), mDesc.get(i).getFromDateString(), mDesc.get(i).getTimeFrom() + " - ", mDesc.get(i).getTimeTo()); // date passed in twice because textbox only contains 1st parameter
                mCreatedEventDisplayAdapter.add(todayEvent);
            }

            ArrayList<EventData> ArrayUpcomingEvents = getUpcomingEventsArray(date);
            if (ArrayUpcomingEvents.size() != 0) {
                CreatedEventDisplay firstEvent = new CreatedEventDisplay(ArrayUpcomingEvents.get(0).getEventName(), ArrayUpcomingEvents.get(0).getFromDateString(), "event description");
                mCreatedEventDisplayAdapter.add(firstEvent);
                for (int i = 1; i < ArrayUpcomingEvents.size(); i++) {
                    EventData currentEvent = ArrayUpcomingEvents.get(i);
                    EventData previousEvent = ArrayUpcomingEvents.get(i - 1);
                    if (currentEvent.getFromDateString().equals(previousEvent.getFromDateString())) {
                        CreatedEventSublist subEvent = new CreatedEventSublist(currentEvent.getEventName(), currentEvent.getFromDateString());
                        mCreatedEventDisplayAdapter.add(subEvent);
                    } else {
                        CreatedEventDisplay event = new CreatedEventDisplay(currentEvent.getEventName(), currentEvent.getFromDateString(), "event description");
                        mCreatedEventDisplayAdapter.add(event);
                    }

                }
            }


        }
        mDesc = null;
    }
*/

    //DAVID HACK

    public String getSelectedDateCalendar(){
        return selectedDateCalendar;
    }

    public ArrayList<EventData> getUpcomingEventsArray(String selectedDate) throws ParseException {
        ArrayList<EventData> ArrayUpcomingEvents = new ArrayList<EventData>();
        for (int i = 0; i < 10; i++) { //figure out how many upcoming events total to display.. talk to dad, maybe check size and break loop after certain #
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();
            cal.setTime(dateFormat.parse(selectedDate));
            cal.add(Calendar.DATE, 1);
            String convertedDate = dateFormat.format(cal.getTime());
            ArrayList<EventData> temporaryEvents = EventDataStore.getInstance().
                    getEventDataListFromMap(convertedDate);
            selectedDate = convertedDate;
            ArrayUpcomingEvents.addAll(temporaryEvents);
        }
        return ArrayUpcomingEvents;
    }
//END DAVID HACK

    // TODO: Rename method, update argument and hook method into UI mEvent
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }




//DAVID HACK
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == CreateEventActivity.RESULT_OK && requestCode == REQUEST_CODE) {
            // Extract name value from result extras
            String name = data.getExtras().getString("name");
            String date = data.getExtras().getString("date");
            String description = data.getExtras().getString("description");
            // Toast the name to display temporarily on screen
            //Toast.makeText(this, name, Toast.LENGTH_SHORT).show();
            SuperCreate new_event = new CreatedEventDisplay(name, date, description); //CHANGE HARDCODED VALUE TO VARIABLE ABOVE WHEN BUILT FULLY
            mCreatedEventDisplayAdapter.add(new_event);
            try {
                displayEventDataForDate(selectedDateCalendar);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
//END DAVID HACK
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    protected void showToast(String string) {
        Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT).show();

    }

    public void refreshCalendar() {
//        if (((LinearLayout) mRowLayout).getChildCount() > 0) {
  //          ((LinearLayout) mRowLayout).removeAllViews();
    //    }
        mCalendarAdapter.populateDays(mDisplayView);
        mHandler.post(calendarUpdater2); // generate some calendar mItems
        mCalendarAdapter.notifyDataSetChanged();

        if (mDisplayView == DisplayView.VIEW_DAY) {
            mTitleView.setText("" + mCurrentData.getCurrentDate()
                    + " " + android.text.format.DateFormat.
                    format("MMMM yyyy", mCurrentData.getCurrentMonth()));
        } else {
            mTitleView.setText(android.text.format.DateFormat.format("MMMM yyyy",
                    mCurrentData.getCurrentMonth()));
        }
    }

    // Not used.
    public Runnable calendarUpdater = new Runnable() {

        @Override
        public void run() {
            mItems.clear();
        }
    };

    // It populates dates corresponding to events in Google calendar.
    // We are not using this right now.
    public Runnable calendarUpdater1 = new Runnable() {

        @Override
        public void run() {
            mItems.clear();
            // Print dates of the current week
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            mEvent = Utility.readCalendarEvent(getActivity(), false /* forceRefresh */);
            for (int i = 0; i < Utility.startDates.size(); i++) {
                mItemMonth.add(GregorianCalendar.DATE, 1);
                mItems.add(Utility.startDates.get(i).toString());
            }
            mCalendarAdapter.setItems(mItems);
            mCalendarAdapter.notifyDataSetChanged();
        }
    };

    // It populates mItems with dates for which we have some events.
    // CalendarAdapter uses mItems to show the dot for such dates.
    public Runnable calendarUpdater2 = new Runnable() {
        @Override
        public void run() {
            mCalendarAdapter.notifyDataSetChanged();
            //addDotIfEvent();
        }
    };

    /*
    public void addDotIfEvent() {
        mItems.clear();
        // Print dates of the current week
        ArrayList<EventData> allEvents = EventDataStore.getInstance().
                getEventDataListFromMap(selectedDate);
        for (EventData eventData : allEvents) {
            String eventDate = eventData.getFromDateString();
            mItems.add(eventDate);
        }
        mCalendarAdapter.setItems(mItems);
        mCalendarAdapter.notifyDataSetChanged();
    }
    */
}
