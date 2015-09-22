package com.friendstime.apps.calex.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.friendstime.apps.calex.R;
import com.friendstime.apps.calex.model.Contact;

import java.util.ArrayList;

/**
 * Created by David on 9/8/2015.
 * FOR ADDING SELECTED PARTICIPANT TO LISTVIEW
 */
public class ParticipantsAdapter extends ArrayAdapter<Contact> {
    private ArrayList<Contact> contacts;
    public ParticipantsAdapter(Context context, ArrayList<Contact> contacts) {
        super(context, 0, contacts);
        this.contacts = contacts;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Contact contact = getItem(position);
        if (contact == null){
            return convertView;
        }
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_participant, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.tvAdapterParticipantName);
        tvName.setText(contact.name);
        return convertView;
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return contacts.size();
    }
}