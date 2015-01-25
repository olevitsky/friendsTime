package com.friendstime.apps.calex.model;

import org.scribe.model.Token;

import org.scribe.builder.api.DefaultApi10a;

/**
 * Created by oleg on 1/25/2015.
 */



/**
 * Service provider for "2-legged" OAuth10a for Yelp API (version 2).
 */
public class YelpApi2 extends DefaultApi10a {

    @Override
    public String getAccessTokenEndpoint() {
        return null;
    }

    @Override
    public String getAuthorizationUrl(Token arg0) {
        return null;
    }

    @Override
    public String getRequestTokenEndpoint() {
        return null;
    }

    }

