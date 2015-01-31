package com.friendstime.apps.calex.model;

/**
 * Created by oleg on 1/25/2015.
 */
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Business {
    private String m_id;
    private String b_name;
    private String m_phone;
    private String m_image_url;

    public String getB_name() {
        return this.b_name;
    }

    public String getM_phone() {
        return this.m_phone;
    }

    public String getM_image_url() {
        return this.m_image_url;
    }

    // Decodes business json into business model object
    public static Business fromJson(JSONObject jsonObject) {
        Business b = new Business();
        // Deserialize json into object fields
        try {
            b.m_id = jsonObject.getString("id");
            b.b_name = jsonObject.getString("name");
            b.m_phone = jsonObject.getString("display_phone");
            b.m_image_url = jsonObject.getString("image_url");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        // Return new object
        return b;
    }

    // Decodes array of business json results into business model objects
    public static ArrayList<Business> fromJson(JSONArray jsonArray) {
        ArrayList<Business> businesses = new ArrayList<Business>(jsonArray.length());
        // Process each result in json array, decode and convert to business object
        for (int i=0; i < jsonArray.length(); i++) {
            JSONObject businessJson = null;
            try {
                businessJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            Business business = Business.fromJson(businessJson);
            if (business != null) {
                businesses.add(business);
            }
        }

        return businesses;
    }

    @Override
    public String toString() {
        return m_id + " " + b_name + " " + m_phone + " " + m_image_url;
    }
}
