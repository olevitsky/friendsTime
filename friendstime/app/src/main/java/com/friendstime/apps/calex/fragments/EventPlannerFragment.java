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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.friendstime.apps.calex.adapters.CalendarAdapter;
import com.friendstime.apps.calex.R;
import com.friendstime.apps.calex.model.CurrentData;
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
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public GregorianCalendar itemmonth;  // Calendar instances.

    public CurrentData mCurrentData;

    public CalendarAdapter mCalendarAdapter;// mCalendarAdapter instance
    public Handler mHandler;// for grabbing some event values for showing the dot
    // marker.
    public ArrayList<String> items; // container to store calendar items which
    // needs showing the event marker
    ArrayList<String> event;
    LinearLayout rLayout;
    ArrayList<String> date;
    ArrayList<String> desc;
    TextView mTitleView;
    public enum DisplayView {
        VIEW_MONTH, VIEW_WEEK, VIEW_DAY, VIEW_SURPRISE
    }
    public DisplayView mDisplayView;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlannerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EventPlannerFragment newInstance(String param1, String param2) {
        EventPlannerFragment fragment = new EventPlannerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
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
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mDisplayView = DisplayView.VIEW_MONTH;
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_planner, container, false);
        Locale.setDefault(Locale.US);

        rLayout = (LinearLayout)v.findViewById(R.id.text);
        mCurrentData = new CurrentData();
        itemmonth = (GregorianCalendar) mCurrentData.getCurrentMonth().clone();

        items = new ArrayList<String>();

        mCalendarAdapter = new CalendarAdapter(getActivity(), mCurrentData);

        GridView gridview = (GridView)v.findViewById(R.id.gridview);
        gridview.setAdapter(mCalendarAdapter);

        mHandler = new Handler();
        mHandler.post(calendarUpdater);

        mTitleView = (TextView)v.findViewById(R.id.title);
        mTitleView.setText(android.text.format.DateFormat.format("MMMM yyyy",
                mCurrentData.getCurrentMonth()));

        ImageView previous = (ImageView)v.findViewById(R.id.previous);

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDisplayView == DisplayView.VIEW_MONTH) {
                    mCurrentData.setPreviousMonth();
                } else if (mDisplayView == DisplayView.VIEW_WEEK) {
                    mCurrentData.setPreviousWeek();
                } else if (mDisplayView == DisplayView.VIEW_DAY) {
                    mCurrentData.setPreviousDay();
                }
                refreshCalendar();
            }
        });

        ImageView next = (ImageView)v.findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDisplayView == DisplayView.VIEW_MONTH) {
                    mCurrentData.setNextMonth();
                } else if (mDisplayView == DisplayView.VIEW_WEEK) {
                    mCurrentData.setNextWeek();
                } else if (mDisplayView == DisplayView.VIEW_DAY) {
                    mCurrentData.setNextDay();
                }
                refreshCalendar();
            }
        });

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // removing the previous view if added
                if (((LinearLayout) rLayout).getChildCount() > 0) {
                    ((LinearLayout) rLayout).removeAllViews();
                }
                desc = new ArrayList<String>();
                date = new ArrayList<String>();
                ((CalendarAdapter) parent.getAdapter()).setSelected(v);
                String selectedGridDate = CalendarAdapter.mDayStrings
                        .get(position);
                String[] separatedTime = selectedGridDate.split("-");
                String gridvalueString = separatedTime[2].replaceFirst("^0*",
                        "");// taking last part of date. ie; 2 from 2012-12-02.
                int gridvalue = Integer.parseInt(gridvalueString);
                // navigate to next or previous mCurrentMonth on clicking offdays.
                if ((gridvalue > 10) && (position < 8)) {
                    mCurrentData.setPreviousMonth();
                    refreshCalendar();
                } else if ((gridvalue < 7) && (position > 28)) {
                    mCurrentData.setNextMonth();
                    refreshCalendar();
                }
                ((CalendarAdapter) parent.getAdapter()).setSelected(v);

                for (int i = 0; i < Utility.startDates.size(); i++) {
                    if (Utility.startDates.get(i).equals(selectedGridDate)) {
                        desc.add(Utility.nameOfEvent.get(i));
                    }
                }

                if (desc.size() > 0) {
                    for (int i = 0; i < desc.size(); i++) {
                        TextView rowTextView = new TextView(getActivity());

                        // set some properties of rowTextView or something
                        rowTextView.setText("Event:" + desc.get(i));
                        rowTextView.setTextColor(Color.BLACK);

                        // add the textview to the linearlayout
                        rLayout.addView(rowTextView);

                    }

                }

                desc = null;
            }

        });
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
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
        mCalendarAdapter.populateDays(mDisplayView);
        mCalendarAdapter.notifyDataSetChanged();
        mHandler.post(calendarUpdater); // generate some calendar items

        if (mDisplayView == DisplayView.VIEW_DAY) {
            mTitleView.setText("" + mCurrentData.getCurrentDate()
                    + " " + android.text.format.DateFormat.
                    format("MMMM yyyy", mCurrentData.getCurrentMonth()));
        } else {
            mTitleView.setText(android.text.format.DateFormat.format("MMMM yyyy",
                    mCurrentData.getCurrentMonth()));
        }
    }

    public Runnable calendarUpdater = new Runnable() {

        @Override
        public void run() {
            items.clear();
        }
    };

    public Runnable calendarUpdater1 = new Runnable() {

        @Override
        public void run() {
            items.clear();
            // Print dates of the current week
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            String itemvalue;
            event = Utility.readCalendarEvent(getActivity(), false /* forceRefresh */);
            // Log.d("=====Event====", event.toString());
            // Log.d("=====Date ARRAY====", Utility.startDates.toString());

            for (int i = 0; i < Utility.startDates.size(); i++) {
                itemvalue = df.format(itemmonth.getTime());
                itemmonth.add(GregorianCalendar.DATE, 1);
                items.add(Utility.startDates.get(i).toString());
            }
            mCalendarAdapter.setItems(items);
            mCalendarAdapter.notifyDataSetChanged();
        }
    };
}
