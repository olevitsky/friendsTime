package com.friendstime.apps.calex.activities;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.friendstime.apps.calex.fragments.EventPlannerFragment;

public class EventPlanner extends SingleFragmentActivity
        implements EventPlannerFragment.OnFragmentInteractionListener {
    public static String TAG = "EventPlanner";

    @Override
    protected Fragment createFragment() {
        return EventPlannerFragment.newInstance();
    }

    public void onFragmentInteraction(Uri uri) {
        // Don't know why this is needed.
        Log.d(TAG, "onFragmentInteraction called.");
    }
}
