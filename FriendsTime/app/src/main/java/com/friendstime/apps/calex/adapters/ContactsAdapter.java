package com.friendstime.apps.calex.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.friendstime.apps.calex.R;
import com.friendstime.apps.calex.model.Contact;
import com.friendstime.apps.calex.utils.ContactFetcher;

import java.util.ArrayList;

/**
 * Created by oleg on 2/4/2015.
 */
public class ContactsAdapter extends ArrayAdapter<Contact> {

    public ContactsAdapter(Context context, ArrayList<Contact> contacts) {
        super(context, 0, contacts);
    }

    private View getCustomView (int position, View convertView, ViewGroup parent) {
        Contact contact = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.adapter_contact_item, parent, false);
        }
        // Populate the data into the template view using the data object
        TextView tvName = (TextView) view.findViewById(R.id.tvContactName);
        TextView tvEmail = (TextView) view.findViewById(R.id.tvContactEmail);
        TextView tvPhone = (TextView) view.findViewById(R.id.tvContactPhone);
        tvName.setText(contact.getContactName());
        tvEmail.setText("");
        tvPhone.setText("");

        //Oleg TBD check if delay to open spinner is real. Seems to be there is some delay, but not
        // sure if it is something to worry about
        // check if method 2 from this link is faster:
        //http://guides.codepath.com/android/Loading-Contacts-with-Content-Providers
        if(!contact.isEmailSet())
            ContactFetcher.fetchContactEmails(contact, getContext());
        if(!contact.isNumberSet())
            ContactFetcher.fetchContactNumbers(contact, getContext());
        if (contact.mEmails.size() > 0 && contact.mEmails.get(0) != null) {
            tvEmail.setText(contact.mEmails.get(0).getAddress());
        }
        if (contact.mNumbers.size() > 0 && contact.mNumbers.get(0) != null) {
            tvPhone.setText(contact.mNumbers.get(0).getNumber());
        }

        //debug
       // Toast.makeText(getContext(),tvEmail.getText().toString() + "  " + tvPhone.getText().toString(), Toast.LENGTH_SHORT  ).show();

        return view;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getDropDownView (int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }
}