package com.friendstime.apps.calex.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.friendstime.apps.calex.R;
import com.friendstime.apps.calex.fragments.EventPlannerFragment;
import com.friendstime.apps.calex.model.CurrentData;
import com.friendstime.apps.calex.model.EventDataStore;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by dineshgupta on 12/27/14.
 */
public class CalendarAdapter extends BaseAdapter {
    private Context mContext;
    private CurrentData mCurrentData;
    int mFirstDay;
    String mItemvalue;
    String mCurentDateString;
    DateFormat mDateFormat;

    // It contains those date strings for which we have some events.
    // TODO: remove this . not needed.
    private ArrayList<String> mItems;

    // All daystrings corresponding to this Month or Week or Day view.
    public static List<String> mDayStrings;

    // Previously selected view.
    private View mPreviousView;

    private EventPlannerFragment.DisplayView mDisplayView;

    public CalendarAdapter(Context c, CurrentData currentData) {
        CalendarAdapter.mDayStrings = new ArrayList<String>();
        Locale.setDefault(Locale.US);
        mCurrentData = currentData;  // Same pointer as in parent fragment.
        mContext = c;
        mItems = new ArrayList<String>();
        populateDays(EventPlannerFragment.DisplayView.VIEW_MONTH);
    }

    public void setItems(ArrayList<String> items) {
        for (int i = 0; i != items.size(); i++) {
            if (items.get(i).length() == 1) {
                items.set(i, "0" + items.get(i));
            }
        }
        this.mItems = items;
    }

    // This many gridview boxes are created.
    public int getCount() {
        return mDayStrings.size();
    }

    public Object getItem(int position) {
        return mDayStrings.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    public boolean isClickable(int day, int position) {
        if (mDisplayView == EventPlannerFragment.DisplayView.VIEW_WEEK) {
            if (mCurrentData.getCurrentWeek() == 1 && day > 1 && position < mFirstDay) {
                return false;
            }
            if (mCurrentData.getCurrentWeek() == mCurrentData.getCurrentMonth().
                    getActualMaximum(GregorianCalendar.WEEK_OF_MONTH)
                    && day < 7) {
                return false;
            }
        } else if (mDisplayView == EventPlannerFragment.DisplayView.VIEW_MONTH) {
            if ((day > 1) && (position < mFirstDay)) {
                return false;
                /* OLEG HACK, Need to figure out how to support months with different number of rows.
                   Previous implementation SEGV for August 3 (August has more rows than other months. Same is for January
                    */
            //} else if ((day < 7) && (position > 28)) {
            } else if ((day < 15) && (position > 28)) {
                return false;
            }
        }
        return true;
    }

    // create a new view for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        TextView dayView;
        if (convertView == null) { // if it's not recycled, initialize some attributes.
            LayoutInflater vi = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.cal_item, null);
        }
        dayView = (TextView)v.findViewById(R.id.date);
        // Separates daystring into parts.
        String[] separatedTime = mDayStrings.get(position).split("-");
        // taking last part of date. ie; 2 from 2012-12-02
        String gridvalue = separatedTime[2].replaceFirst("^0*", "");
        // checking whether the day is in current mCurrentMonth or not.
        if (isClickable(Integer.parseInt(gridvalue), position)) {
            // Setting curent mCurrentMonth's days in blue color.
            dayView.setTextColor(Color.parseColor("#303F9F"));
        } else {
            // setting offdays to white color.
            dayView.setTextColor(Color.WHITE);
            dayView.setClickable(false);
            dayView.setFocusable(false);
        }

        if (isClickable(Integer.parseInt(gridvalue), position)
                && mDayStrings.get(position).equals(mCurrentData.getCurrentDateString())) {
            setSelected(v);
            mPreviousView = v;
            // Right now Today's date is selected date.
            mCurrentData.setSelectedDate(mDayStrings.get(position));
        } else {
            v.setBackgroundResource(R.drawable.list_item_background);
        }
        dayView.setText(gridvalue);

        // create date string for comparison
        String date = mDayStrings.get(position);

        if (date.length() == 1) {
            date = "0" + date;
        }
        String monthStr = "" + (mCurrentData.getCurrentMonth().get(GregorianCalendar.MONTH) + 1);
        if (monthStr.length() == 1) {
            monthStr = "0" + monthStr;
        }

        // show icon if date is not empty and it exists in the mItems array
        ImageView iw = (ImageView) v.findViewById(R.id.date_icon);
        if (date.length() > 0
                && EventDataStore.getInstance().getEventDataListFromMap(date).size() > 0) {
            iw.setVisibility(View.VISIBLE);
        } else {
            iw.setVisibility(View.INVISIBLE);
        }
        return v;
    }

    public View setSelected(View view) {
        if (mPreviousView != null) {
            mPreviousView.setBackgroundResource(R.drawable.list_item_background);
        }
        mPreviousView = view;
        view.setBackgroundResource(R.drawable.calendar_cel_selectl);
        return view;
    }

    public void populateDays(EventPlannerFragment.DisplayView displayView) {
        mItems.clear();
        mDayStrings.clear();
        Locale.setDefault(Locale.US);
        mDisplayView = displayView;

        if (displayView == EventPlannerFragment.DisplayView.VIEW_DAY) {
            GregorianCalendar selectedDate =
                    (GregorianCalendar)mCurrentData.getCurrentMonth().clone();
            selectedDate.set(GregorianCalendar.DAY_OF_MONTH, mCurrentData.getCurrentDate());
            mItemvalue = mCurrentData.getDateFormat().format(selectedDate.getTime());
            mCurrentData.setSelectedDate(mItemvalue);
            // mDayStrings.add(mItemvalue);
            return;
        }
        
        // mCurrentMonth start day. ie; sun, mon, etc
        // Set currentMonth to day one so that correct day of the week could be retrieved.
        GregorianCalendar currentMonth = (GregorianCalendar)(mCurrentData.getCurrentMonth()).clone();
        currentMonth.set(GregorianCalendar.DAY_OF_MONTH, 1);
        mFirstDay = currentMonth.get(GregorianCalendar.DAY_OF_WEEK);
        // finding number of weeks in current mCurrentMonth.
        int maxWeeknumber = mCurrentData.getCurrentMonth().
                getActualMaximum(GregorianCalendar.WEEK_OF_MONTH);
        // allocating maximum row number for the gridview.
        //int monthLength = maxWeeknumber * 7;
        int monthLength = 6 * 7;
        
        // Check previous mCurrentMonth is 31 days or lesser.
        GregorianCalendar prevMonth = (GregorianCalendar)mCurrentData.getPreviousMonth().clone();
        int maxP = prevMonth.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
        
        int calMaxP = maxP - (mFirstDay - 1);// calendar offday starting 24,25 ...
        /**
         * setting the start date as previous mCurrentMonth's required date.
         */
        prevMonth.set(GregorianCalendar.DAY_OF_MONTH, calMaxP);

        int weekNumber = mCurrentData.getCurrentWeek();

        /**
         * filling calendar gridview.
         */
        for (int n = 0; n < monthLength; n++) {
            prevMonth.add(GregorianCalendar.DATE, 1);
            if (displayView == EventPlannerFragment.DisplayView.VIEW_WEEK) {
                if (1 + n/7 != weekNumber) {
                    continue;
                }
            }
            mItemvalue = mCurrentData.getDateFormat().format(prevMonth.getTime());
            mDayStrings.add(mItemvalue);
        }
    }
}