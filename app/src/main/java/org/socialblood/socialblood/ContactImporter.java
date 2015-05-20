package org.socialblood.socialblood;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anuraag on 5/19/15.
 */
public class ContactImporter {

    //TODO create the contact importer, create the adapter to populate listview
    //TODO COMPLETE: I have done the classes for contact adapter, contact class, and I have the contact list item

    public static List<Contact> importContacts(Context context) {
        List<Contact> contacts = new ArrayList<Contact>();
        ContentResolver cr = context.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String hasNumber = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                Uri person = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long
                        .parseLong(id));
                Uri photo = Uri.withAppendedPath(person, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
                String contactHasPhoto = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.PHOTO_ID));
                if (contactHasPhoto == null){
                    photo = null;
                }
//                Log.d("URI",cur.getString(cur.getColumnIndex(ContactsContract.Contacts.PHOTO_URI)));
//                if(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.PHOTO_URI)).isEmpty()){
//                    photo = null;
//                }
//                Log.d("hasnumber", hasNumber);

                if (!hasNumber.equals("0")) {
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    Log.d("ID", id);
                    Log.d("Name", name);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        ArrayList<String> phoneNumbers = new ArrayList<String>();
                        phoneNumbers.add(phoneNo);
                        contacts.add(new Contact(name,"N/A","N/A", "N/A", photo,phoneNumbers));
//                        contacts. add(new Contact(phoneNo.replace(" ", "").replace("(", "").replace(")", "").replace("-", "")));
                    }

                    pCur.close();
                }
            }
        }
        return contacts;
    }
}
