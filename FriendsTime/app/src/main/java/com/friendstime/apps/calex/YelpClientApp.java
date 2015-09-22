package com.friendstime.apps.calex;

import android.content.Context;
import android.util.Log;

import com.friendstime.apps.calex.activities.Action;
import com.friendstime.apps.calex.model.Contact;
import com.friendstime.apps.calex.model.ParseClient;
import com.friendstime.apps.calex.model.YelpClient;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.SaveCallback;

/*
 * This is the Android application itself and is used to configure various settings
 * including the image cache in memory and on disk. This also adds a singleton
 * for accessing the relevant rest client.
 * 
 *     RestClient client = RestClientApp.getRestClient();
 *     // use client to send requests to API
 *     
 */
public class YelpClientApp extends com.activeandroid.app.Application {
    private static Context sContext;
    private static ParseClient mParseClient;

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Action.class);
        ParseObject.registerSubclass(Contact.class);

        YelpClientApp.sContext = this;

        // Create global configuration and initialize ImageLoader with this configuration
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().
                cacheInMemory().cacheOnDisc().build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config);

        //init parse
        ParseClient.getInstance(this);
        Log.i("Info", "I have setup the parse client");

        //Parse.enableLocalDatastore(this);

       // Parse.initialize(this, "BNtuAUlmqp4bI2V6oxz9R1ll7Y5nQUiRaWBSnyI4", "7MuCAzxPBM1n29QbCjxqX70jZl31fslE2qfJgBOG");


        // Test creation of object - use this only to test parse connection - see instruction on Parse website
        //https://www.parse.com/apps/quickstart#parse_data/mobile/android/native/existing

        //ParseObject testObject = new ParseObject("TestObject");
        //testObject.put("foo", "bar");
        //testObject.saveInBackground();
        ParsePush.subscribeInBackground("", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
                } else {
                    Log.e("com.parse.push", "failed to subscribe for push", e);
                }
            }
        });
    }

    public static YelpClient getRestClient() {
        return (YelpClient) YelpClient.getInstance(YelpClient.class, YelpClientApp.sContext);
    }
}