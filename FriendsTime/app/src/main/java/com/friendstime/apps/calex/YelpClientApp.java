package com.friendstime.apps.calex;

import android.content.Context;

import com.friendstime.apps.calex.model.YelpClient;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.parse.Parse;

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

    @Override
    public void onCreate() {
        super.onCreate();
        YelpClientApp.sContext = this;

        // Create global configuration and initialize ImageLoader with this configuration
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().
                cacheInMemory().cacheOnDisc().build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config);

        //init parse
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "BNtuAUlmqp4bI2V6oxz9R1ll7Y5nQUiRaWBSnyI4", "7MuCAzxPBM1n29QbCjxqX70jZl31fslE2qfJgBOG");

        // Test creation of object - use this only to test parse connection - see instruction on Parse website
        //https://www.parse.com/apps/quickstart#parse_data/mobile/android/native/existing

        //ParseObject testObject = new ParseObject("TestObject");
        //testObject.put("foo", "bar");
        //testObject.saveInBackground();
    }

    public static YelpClient getRestClient() {
        return (YelpClient) YelpClient.getInstance(YelpClient.class, YelpClientApp.sContext);
    }
}