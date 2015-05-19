package org.socialblood.socialblood;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

/**
 * Created by Anuraag on 5/19/15.
 */
public class Contact {
    private String myName,myLocation,myDistance,myBloodType;
    private Drawable myProfilePicture;

    public Contact(String name, String location,String distance, String bloodtype, Drawable pfp) {
        myName = name;
        myLocation = location;
        myDistance = distance;
        myBloodType = bloodtype;
        myProfilePicture = pfp;
    }

    public Drawable getProfilePicture() {
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

    public void setProfilePicture(Drawable myProfilePicture) {
        this.myProfilePicture = myProfilePicture;
    }

    public String toString() {
        return myName;
    }
}
