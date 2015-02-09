package com.friendstime.apps.calex.utils;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.friendstime.apps.calex.model.Contact;

import java.util.ArrayList;

/**
 * Created by oleg on 2/4/2015.
 */
// new ContactFetcher(this).fetchAll();
public class ContactFetcher {
    private Context mContext;

    public ContactFetcher(Context c) {
        this.mContext = c;
    }

    public ArrayList<Contact> fetchAll() {
        ArrayList<Contact> listContacts = new ArrayList<Contact>();
        CursorLoader cursorLoader = new CursorLoader(mContext,
                ContactsContract.Contacts.CONTENT_URI, // uri
                null, // the columns to retrieve (all)
                null, // the selection criteria (none)
                null, // the selection args (none)
                null // the sort order (default)
        );
        // This should probably be run from an AsyncTask
        Cursor c = cursorLoader.loadInBackground();
        if (c.moveToFirst()) {
            do {
                Contact contact = loadContactData(c);
                listContacts.add(contact);
            } while (c.moveToNext());
        }
        c.close();
        return listContacts;
    }

    private Contact loadContactData(Cursor c) {
        // Get Contact ID
        int idIndex = c.getColumnIndex(ContactsContract.Contacts._ID);
        String contactId = c.getString(idIndex);
        // Get Contact Name
        int nameIndex = c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
        String contactDisplayName = c.getString(nameIndex);
        Contact contact = new Contact();
        contact.setContact(contactId, contactDisplayName);
      //  fetchContactNumbers(c, contact);
      //  fetchContactEmails(c, contact);
        return contact;
    }

    //Oleg these two functions should be moved to Contact class if we continue using them

    public static void fetchContactNumbers( Contact contact, Context context) {
        // Get numbers
        final String[] numberProjection = new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.TYPE, };
        Cursor phone = new CursorLoader(context, ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                numberProjection,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "= ?",
                new String[] { String.valueOf(contact.getContactId()) },
                null).loadInBackground();

        if (phone.moveToFirst()) {
            final int contactNumberColumnIndex = phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            final int contactTypeColumnIndex = phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);

            while (!phone.isAfterLast()) {
                final String number = phone.getString(contactNumberColumnIndex);
                final int type = phone.getInt(contactTypeColumnIndex);
                String customLabel = "Custom";
                CharSequence phoneType =
                        ContactsContract.CommonDataKinds.Phone.getTypeLabel(
                                context.getResources(), type, customLabel);
                contact.addNumber(number, phoneType.toString());
                phone.moveToNext();
            }

        }
        phone.close();
    }

    public static void fetchContactEmails(Contact contact, Context context) {
        // Get email
        final String[] emailProjection = new String[] { ContactsContract.CommonDataKinds.Email.DATA, ContactsContract.CommonDataKinds.Email.TYPE };

        Cursor email = new CursorLoader(context, ContactsContract.CommonDataKinds.Email.CONTENT_URI, emailProjection,
                ContactsContract.CommonDataKinds.Email.CONTACT_ID + "= ?",
                new String[] { String.valueOf(contact.getContactId()) },
                null).loadInBackground();

        if (email.moveToFirst()) {
            final int contactEmailColumnIndex = email.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA);
            final int contactTypeColumnIndex = email.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE);

            while (!email.isAfterLast()) {
                final String address = email.getString(contactEmailColumnIndex);
                final int type = email.getInt(contactTypeColumnIndex);
                String customLabel = "Custom";
                CharSequence emailType =
                        ContactsContract.CommonDataKinds.Email.getTypeLabel(
                                context.getResources(), type, customLabel);
                contact.addEmail(address, emailType.toString());
                email.moveToNext();
            }

        }

        email.close();
    }
}