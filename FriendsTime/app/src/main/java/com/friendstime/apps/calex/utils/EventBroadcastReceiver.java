package com.friendstime.apps.calex.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by oleg on 3/9/2015.
 */
public class EventBroadcastReceiver extends ParsePushBroadcastReceiver {
    private static final String TAG = "EventBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        try {
            if (intent == null) {
                Log.d(TAG, "Receiver intent null");
            } else {

                String action = intent.getAction();

                if (action.equals("com.parse.push.intent.OPEN")) {
                    String parseDataString =  intent.getExtras().getString("com.parse.Data");
                    if (parseDataString == null) {
                        return;
                    } else {
                        JSONObject json = new JSONObject(parseDataString);
                        Iterator itr = json.keys();
                        while (itr.hasNext()) {
                            String key = (String) itr.next();
                            if (key.equals("username")) {
                                Toast.makeText(context, json.getString(key), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }
            }
        } catch (JSONException e) {
            Log.d(TAG, "JSONException: " + e.getMessage());
        }
    }
}
