package com.friendstime.apps.calex.utils;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
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
        String lookupKey = c.getString(c.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
        Uri contentLookupUri = ContactsContract.Contacts.getLookupUri(idIndex, lookupKey);
        String lookupUriString = contentLookupUri.toString();

        // TO get URI back for lookup
        //Uri tmpUri = Uri.parse(lookupUriString);

        // Get Contact Name
        int nameIndex = c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
        String contactDisplayName = c.getString(nameIndex);
        Contact contact = new Contact();
        contact.setContact(lookupUriString, contactDisplayName);
      //  fetchContactNumbers(c, contact);
      //  fetchContactEmails(c, contact);
        return contact;
    }

    //Oleg these two functions should be moved to Contact class if we continue using them

    public static void fetchContactNumbersAndEmail( Contact contact, Context context) {
        fetchContactEmails(contact, context);
        fetchContactNumbers(contact, context);
        // Get numbers

 /*       String contactId ;
        if((contact.getLocalId()).compareTo("-1") == 0) {
            Uri lookupURI = Uri.parse(contact.getContactLookupURI());
            final String[] contactProjection = new String[] {ContactsContract.Contacts._ID};
            Cursor c = context.getContentResolver().query(lookupURI,contactProjection, null, null, null);
            if (!c.moveToNext()) { // move to first (and only) row.
                return; // nothing to set
            }
            int localId = c.getColumnIndex(ContactsContract.Contacts._ID);
            contactId = c.getString(localId);
            c.close();
        } else {
            contactId = contact.getLocalId();
        }

        final String[] emailNumberProjection = new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Email.DATA,
                ContactsContract.CommonDataKinds.Email.TYPE};

        //Cursor emailPhone = new CursorLoader(context, ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                emailNumberProjection,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "= ?",
                new String[] { String.valueOf(contactId) },
                null).loadInBackground();

        if (emailPhone.moveToFirst()) {
            final int contactNumberColumnIndex = emailPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            final int contactNumberTypeColumnIndex = emailPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);
            final int contactEmailColumnIndex = emailPhone.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA);
            final int contactTypeColumnIndex = emailPhone.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE);


            while (!emailPhone.isAfterLast()) {
                final String number = emailPhone.getString(contactNumberColumnIndex);
                final int numberTypeIndex = emailPhone.getInt(contactNumberTypeColumnIndex);
                String customLabel = "Custom";
                CharSequence phoneType =
                        ContactsContract.CommonDataKinds.Phone.getTypeLabel(
                                context.getResources(), numberTypeIndex, customLabel);
                contact.addNumber(number, phoneType.toString());



                //email
                final String address = emailPhone.getString(contactEmailColumnIndex);
                final int emailTypeIndex = emailPhone.getInt(contactTypeColumnIndex);
                CharSequence emailType =
                        ContactsContract.CommonDataKinds.Email.getTypeLabel(
                                context.getResources(), emailTypeIndex, customLabel);
                contact.addEmail(address, emailType.toString());

                emailPhone.moveToNext();
            }

        }
        emailPhone.close();
        */
    }

    private static void fetchContactNumbers( Contact contact, Context context) {
        // Get numbers
        String contactId = getContactId(context, contact) ;

        final String[] numberProjection = new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.TYPE, };
        Cursor phone = new CursorLoader(context, ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                numberProjection,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "= ?",
                new String[] { String.valueOf(contactId) },
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

    private static void fetchContactEmails(Contact contact, Context context) {
        // Get email
        String contactId = getContactId(context, contact) ;
        final String[] emailProjection = new String[] { ContactsContract.CommonDataKinds.Email.DATA, ContactsContract.CommonDataKinds.Email.TYPE };

        Cursor email = new CursorLoader(context, ContactsContract.CommonDataKinds.Email.CONTENT_URI, emailProjection,
                ContactsContract.CommonDataKinds.Email.CONTACT_ID + "= ?",
                new String[] { String.valueOf(contactId) },
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

    private static String getContactId(Context context, Contact contact) {
        String contactId = null;
        if((contact.getLocalId()).compareTo("-1") == 0) {
            Uri lookupURI = Uri.parse(contact.getContactLookupURI());
            final String[] contactProjection = new String[] {ContactsContract.Contacts._ID};
            Cursor c = context.getContentResolver().query(lookupURI,contactProjection, null, null, null);
            if (!c.moveToNext()) { // move to first (and only) row.
                return contactId; // nothing to set
            }
            int localId = c.getColumnIndex(ContactsContract.Contacts._ID);
            contactId = c.getString(localId);
            c.close();
        } else {
            contactId = contact.getLocalId();
        }
        return contactId;
    }
}