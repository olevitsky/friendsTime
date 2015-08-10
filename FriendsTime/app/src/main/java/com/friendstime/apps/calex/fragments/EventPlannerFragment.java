package com.friendstime.apps.calex.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.friendstime.apps.calex.R;
import com.friendstime.apps.calex.activities.CreateEventActivity;
import com.friendstime.apps.calex.activities.EventPlanner;
import com.friendstime.apps.calex.adapters.CalendarAdapter;
import com.friendstime.apps.calex.model.CurrentData;
import com.friendstime.apps.calex.model.EventData;
import com.friendstime.apps.calex.model.EventDataStore;
import com.friendstime.apps.calex.utils.Utility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
public class EventPlannerFragment extends Fragment {
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
    ArrayList<String> mDesc;

    TextView mTitleView;

    Button mCreateNewEvent;

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
                displayEventDataForDate(mCurrentData.getSelectedDate());
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment.
        View v = inflater.inflate(R.layout.fragment_planner, container, false);
        Locale.setDefault(Locale.US);

        mRowLayout = (LinearLayout) v.findViewById(R.id.event_description);
        mCurrentData = new CurrentData();
        mItemMonth = (GregorianCalendar) mCurrentData.getCurrentMonth().clone();

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
                    displayEventDataForDate(mCurrentData.getSelectedDate());
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
                    displayEventDataForDate(mCurrentData.getSelectedDate());
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
                displayEventDataForDate(selectedGridDate);
            }

        });
        mCreateNewEvent = (Button) v.findViewById(R.id.create_new_event);
        mCreateNewEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = CreateEventActivity.newIntent(getActivity(),
                        mCurrentData.getSelectedDate());
                startActivity(i);
            }
        });
        // Run calenderUpdater2 after delay of 2 secs to give chance to
        // get eventdata getting populated.
        mHandler.postDelayed(calendarUpdater2, 2000);

        return v;
    }

    public void displayEventDataForDate(String selectedDate) {
        // removing the previous view if added
        if (((LinearLayout) mRowLayout).getChildCount() > 0) {
            ((LinearLayout) mRowLayout).removeAllViews();
        }
        mDesc = new ArrayList<String>();

        ArrayList<EventData> allEvents = EventDataStore.getInstance().
                getEventDataListFromMap(selectedDate);
        for (EventData eventData : allEvents) {
            String eventDate = eventData.getFromDateString();
            if (eventData.getFromDateString().equals(selectedDate)) {
                mDesc.add(eventData.getEventDescription(getActivity()));
            }
        }

        if (mDesc.size() > 0) {
            for (int i = 0; i < mDesc.size(); i++) {
                TextView rowTextView = new TextView(getActivity());

                // set some properties of rowTextView or something
                rowTextView.setText("Event:" + mDesc.get(i));
                rowTextView.setTextColor(Color.BLACK);

                // add the textview to the linearlayout
                mRowLayout.addView(rowTextView);
            }
        }

        mDesc = null;
    }

    // TODO: Rename method, update argument and hook method into UI mEvent
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

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
        if (((LinearLayout) mRowLayout).getChildCount() > 0) {
            ((LinearLayout) mRowLayout).removeAllViews();
        }
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
