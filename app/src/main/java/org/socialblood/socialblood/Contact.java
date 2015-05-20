package org.socialblood.socialblood;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import java.net.URI;
import java.util.ArrayList;

/**
 * Created by Anuraag on 5/19/15.
 */
public class Contact implements Comparable<Contact> {
    private String myName,myLocation,myDistance,myBloodType;
    private ArrayList<String> phoneNumbers;
    private Uri myProfilePicture;

    public Contact(String name, String location,String distance, String bloodtype, Uri pfp, ArrayList<String> numbers) {
        myName = name;
        myLocation = location;
        myDistance = distance;
        myBloodType = bloodtype;
        myProfilePicture = pfp;
        phoneNumbers = numbers;
    }

    public Uri getProfilePicture() {
        return myProfilePicture;
    }

    public String getBloodType() {
        return myBloodType;
    }

    public String getDistance() {
        return myDistance;
    }

    public String getLocation() {
        return myLocation;
    }

    public String getName() {
        return myName;
    }

    public ArrayList<String> getPhoneNumbers() {

        return phoneNumbers;
    }

    public void setBloodType(String myBloodType) {
        this.myBloodType = myBloodType;
    }

    public void setDistance(String myDistance) {
        this.myDistance = myDistance;
    }

    public void setLocation(String myLocation) {
        this.myLocation = myLocation;
    }

    public void setName(String myName) {
        this.myName = myName;
    }

    public void setProfilePicture(Uri myProfilePicture) {
        this.myProfilePicture = myProfilePicture;
    }

    public String toString() {
        return myName;
    }

    @Override
    public int compareTo(Contact contact) {
        return getName().compareToIgnoreCase(contact.getName());
    }
}
