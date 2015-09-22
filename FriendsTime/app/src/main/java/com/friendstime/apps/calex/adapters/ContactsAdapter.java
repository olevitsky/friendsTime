package com.friendstime.apps.calex.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.friendstime.apps.calex.R;
import com.friendstime.apps.calex.model.Contact;
import com.friendstime.apps.calex.utils.ContactFetcher;

import java.util.ArrayList;

/**
 * Created by oleg on 2/4/2015.
 */



public class ContactsAdapter extends ArrayAdapter<Contact> implements Filterable {

    boolean[] checkBoxState;
    private ArrayList<Contact> mOriginalValues; // Original Values
    private ArrayList<Contact> mDisplayedValues;    // Values to be displayed
    private ContactsFilter mFilter = new ContactsFilter();


    public ContactsAdapter(Context context, ArrayList<Contact> contacts) {
        super(context, 0, contacts);
        this.mOriginalValues = new ArrayList<Contact>();
        mOriginalValues.addAll(contacts);
        this.mDisplayedValues = new ArrayList<Contact>();
        mDisplayedValues.addAll(contacts);
        checkBoxState = new boolean[contacts.size()]; //from stackoverflow to solve scrolling issue, see additional comment lower in code
    }



    private View getCustomView (final int position, View convertView, ViewGroup parent) {
        Contact contact = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        View view = convertView;


        LayoutInflater inflater = LayoutInflater.from(getContext());
        view = inflater.inflate(R.layout.adapter_contact_item, parent, false);
        // Populate the data into the template view using the data object
        TextView tvName = (TextView) view.findViewById(R.id.tvContactName);
        TextView tvEmail = (TextView) view.findViewById(R.id.tvContactEmail);
        TextView tvPhone = (TextView) view.findViewById(R.id.tvContactPhone);
        CheckBox checkbox = (CheckBox) view.findViewById(R.id.cbCheckBox);
        tvName.setText(contact.getContactName());
        tvName.setTextSize(15);
        tvEmail.setText("");
        tvPhone.setText("");
        checkbox.setTag(position);
        if (contact.checked == 1){
            checkbox.setChecked(true);
        }
        //Oleg TBD check if delay to open spinner is real. Seems to be there is some delay, but not
        // sure if it is something to worry about
        // check if method 2 from this link is faster:
        //http://guides.codepath.com/android/Loading-Contacts-with-Content-Providers
        if(!contact.isEmailNumberSet()) {
            ContactFetcher.fetchContactNumbersAndEmail(contact, getContext());
        }

        if (contact.numEmails() != 0) {
            tvEmail.setText(contact.getFirstEmail().getAddress());
            tvEmail.setTextSize(10);
        }
        if (contact.numNumbers() != 0) {
            tvPhone.setText(contact.getFirstNumber().getNumber());
        }
        //unsure how the following code works. got it off stackOverflow, seemed to fix problem of incorrect checkboxes being checked
        //while scrolling.

        checkbox.setOnCheckedChangeListener(null);
        //if(checkBoxState[position])
         //   checkbox.setChecked(true);
        //else
        //    checkbox.setChecked(false);

        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Integer pos = (Integer)buttonView.getTag();
                if(isChecked){

                    checkBoxState[pos.intValue()]=true;

                }
                else{
                    checkBoxState[pos.intValue()]=false;
                    Log.e("checked", "unchecked");

                }

            }
        });

        //debug

       // Toast.makeText(getContext(),tvEmail.getText().toString() + "  " + tvPhone.getText().toString(), Toast.LENGTH_SHORT  ).show();
        view.setTag(position);
        return view;
    }



    @Override
    public Filter getFilter() {
        return mFilter;
    }
            private class ContactsFilter extends Filter {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {

                    String filterString = constraint.toString().toLowerCase();
                    FilterResults results = new FilterResults();

                    final ArrayList<Contact> list = mOriginalValues;

                    int count = list.size();
                    final ArrayList<Contact> nlist = new ArrayList<Contact>(count);

                    Contact filterableContact;

                    for (int i = 0; i < count; i++) {
                        filterableContact = list.get(i);
                        if (filterableContact.getContactName().toLowerCase().contains(filterString)) {
                            nlist.add(filterableContact);
                        }
                    }

                    results.values = nlist;
                    results.count = nlist.size();
                    return results;
                }


                @SuppressWarnings("unchecked")
                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    mDisplayedValues = (ArrayList<Contact>) results.values;
                    notifyDataSetChanged();
                    clear();
                    addAll(mDisplayedValues);
                    notifyDataSetInvalidated();




                }
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