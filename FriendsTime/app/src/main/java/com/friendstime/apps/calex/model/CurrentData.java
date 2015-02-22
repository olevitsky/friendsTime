package com.friendstime.apps.calex.model;

import com.friendstime.apps.calex.fragments.EventPlannerFragment;
import com.friendstime.apps.calex.utils.Utility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by dineshgupta on 12/30/14.
 */
public class CurrentData {
    private GregorianCalendar mCurrentMonth;
    private int mCurrentWeek;
    private int mCurrentDate;
    private DateFormat mDateFormat;
    private String mCurrentDateString;
    private String mSelectedDate;

    public CurrentData() {
        mCurrentMonth = (GregorianCalendar) GregorianCalendar.getInstance();
        mCurrentWeek = mCurrentMonth.get(GregorianCalendar.WEEK_OF_MONTH);
        mCurrentDate = mCurrentMonth.get(GregorianCalendar.DAY_OF_MONTH);
        //mDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        mDateFormat = new SimpleDateFormat(Utility.DateFormatMonthDayYearString, Locale.US);
        mCurrentDateString = mDateFormat.format(mCurrentMonth.getTime());
    }

    public GregorianCalendar getCurrentMonth() {
        return mCurrentMonth;
    }

    public int getCurrentWeek() {
        return mCurrentWeek;
    }

    public int getCurrentDate() {
        return mCurrentDate;
    }

    public DateFormat getDateFormat() {
        return mDateFormat;
    }

    public String getCurrentDateString() {
        return mCurrentDateString;
    }

    public GregorianCalendar getPreviousMonth() {
        GregorianCalendar prevMonth = (GregorianCalendar) mCurrentMonth.clone();
        prevMonth.set(GregorianCalendar.DAY_OF_MONTH, 1);
        if (mCurrentMonth.get(GregorianCalendar.MONTH) == mCurrentMonth
                .getActualMinimum(GregorianCalendar.MONTH)) {
            prevMonth.set((mCurrentMonth.get(GregorianCalendar.YEAR) - 1),
                    mCurrentMonth.getActualMaximum(GregorianCalendar.MONTH), 1);
        } else {
            int monthNumber = mCurrentMonth.get(GregorianCalendar.MONTH) - 1;
            prevMonth.clear(GregorianCalendar.MONTH);
            prevMonth.set(GregorianCalendar.MONTH, monthNumber);
        }
        return prevMonth;
    }

    public void setNextMonth() {
        if (mCurrentMonth.get(GregorianCalendar.MONTH) == mCurrentMonth
                .getActualMaximum(GregorianCalendar.MONTH)) {
            mCurrentMonth.set((mCurrentMonth.get(GregorianCalendar.YEAR) + 1),
                    mCurrentMonth.getActualMinimum(GregorianCalendar.MONTH), 1);
        } else {
            mCurrentMonth.set(GregorianCalendar.MONTH,
                    mCurrentMonth.get(GregorianCalendar.MONTH) + 1);
        }
        mCurrentWeek = 1;
        mCurrentDate = 1;
    }

    public void setPreviousMonth() {
        if (mCurrentMonth.get(GregorianCalendar.MONTH) == mCurrentMonth
                .getActualMinimum(GregorianCalendar.MONTH)) {
            mCurrentMonth.set((mCurrentMonth.get(GregorianCalendar.YEAR) - 1),
                    mCurrentMonth.getActualMaximum(GregorianCalendar.MONTH), 1);
        } else {
            mCurrentMonth.set(GregorianCalendar.MONTH,
                    mCurrentMonth.get(GregorianCalendar.MONTH) - 1);
        }
        mCurrentWeek = mCurrentMonth.getActualMaximum(GregorianCalendar.WEEK_OF_MONTH);
        mCurrentDate = mCurrentMonth.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
    }

    public void setNextWeek() {
        // If nextweek is in the same month then do nothing. Else increase the month.
        if (mCurrentWeek == mCurrentMonth.getActualMaximum(GregorianCalendar.WEEK_OF_MONTH)) {
            setNextMonth();
        } else {
            ++mCurrentWeek;
            mCurrentDate = (mCurrentWeek - 1) * 7 + 1;
        }
    }

    public void setPreviousWeek() {
        // If nextweek is in the same month then do nothing. Else increase the month.
        // Somehow min is 0 whereas max is 5 ?  Used +1 because of this.
        if (mCurrentWeek == mCurrentMonth.getActualMinimum(GregorianCalendar.WEEK_OF_MONTH) + 1) {
            setPreviousMonth();
        } else {
            --mCurrentWeek;
            mCurrentDate = (mCurrentWeek) * 7 - 1;
        }
    }

    public void setNextDay() {
        // If nextday is next month then increment month. If nextday is sunday then increment week.
        if (mCurrentDate == mCurrentMonth.getActualMaximum(GregorianCalendar.DAY_OF_MONTH)) {
            setNextMonth();
        } else if (mCurrentDate == mCurrentMonth.getActualMaximum(GregorianCalendar.DAY_OF_WEEK)) {
            setNextWeek();
        } else {
            ++mCurrentDate;
        }
    }

    public void setPreviousDay() {
        if (mCurrentDate == mCurrentMonth.getActualMinimum(GregorianCalendar.DAY_OF_MONTH)) {
            setPreviousMonth();
        } else if (mCurrentDate == mCurrentMonth.getActualMinimum(GregorianCalendar.DAY_OF_WEEK)) {
            setPreviousWeek();
        } else {
            --mCurrentDate;
        }
    }

    public void setNextView(EventPlannerFragment.DisplayView displayView) {
        if (displayView == EventPlannerFragment.DisplayView.VIEW_MONTH) {
            this.setNextMonth();
        } else if (displayView == EventPlannerFragment.DisplayView.VIEW_WEEK) {
            this.setNextWeek();
        } else if (displayView == EventPlannerFragment.DisplayView.VIEW_DAY) {
            this.setNextDay();
        }
    }

    public void setPreviousView(EventPlannerFragment.DisplayView displayView) {
        if (displayView == EventPlannerFragment.DisplayView.VIEW_MONTH) {
            this.setPreviousMonth();
        } else if (displayView == EventPlannerFragment.DisplayView.VIEW_WEEK) {
            this.setPreviousWeek();
        } else if (displayView == EventPlannerFragment.DisplayView.VIEW_DAY) {
            this.setPreviousDay();
        }
    }

    public void setSelectedDate(String selectedDate) {
        mSelectedDate = selectedDate;
    }

    public String getSelectedDate() {
        return mSelectedDate;
    }
}
