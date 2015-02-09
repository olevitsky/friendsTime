package com.friendstime.apps.calex.model;

import android.content.Context;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;

/**
 * Created by oleg on 2/8/2015.
 */
public class ParseClient {
    private static final String PARSE_APP_ID = "BNtuAUlmqp4bI2V6oxz9R1ll7Y5nQUiRaWBSnyI4";
    private static final String PARSE_CLIENT_KEY = "7MuCAzxPBM1n29QbCjxqX70jZl31fslE2qfJgBOG";
    public static final int LOGIN_REQUEST = 501;
    private static ParseClient client = null;
    @SuppressWarnings("unused")
    private Context context;

    private ParseClient(Context ctx) {
        ParseObject.registerSubclass(Contact.class);
        ParseObject.registerSubclass(ContactPhone.class);
        ParseObject.registerSubclass(ContactEmail.class);
        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);
        ParseACL defaultACL = new ParseACL();
        Parse.enableLocalDatastore(ctx);
        Parse.initialize(ctx, PARSE_APP_ID, PARSE_CLIENT_KEY);

        context = ctx;
// If you would like all objects to be private by default, remove this
// line.
        defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
    }

    public static ParseClient getInstance(Context ctx) {
// should probably synchronize this
        if (client == null) {
            client = new ParseClient(ctx);
        }
        return client;
    }
}
