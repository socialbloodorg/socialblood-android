package org.socialblood.socialblood;

import android.media.Image;
import android.widget.ImageView;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseClassName;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by aadeshpatel on 6/11/15.
 */

@ParseClassName("User")
public class User extends ParseObject {
    private static final String emailKey = "email";
    private static final String firstNameKey = "firstName";
    private static final String lastNameKey = "lastName";
    private static final String bloodTypeKey = "bloodType";
    private static final String dateOfBirthKey = "dateOfBirth";
    private static final String phoneNumberKey = "phoneNumber";
    private static final String profilePictureKey = "profilePicture";
    private static final String cityKey = "city";
    private static final String donationsKey = "donations";

    public String getEmail() {
        return getString(emailKey);
    }

    public void setEmail(String value) {
        put(emailKey, value);
    }

    public String getFirstName() {
        return getString(firstNameKey);
    }

    public void setFirstName(String value) {
        put(firstNameKey, value);
    }

    public String getLastName() {
        return getString(lastNameKey);
    }

    public void setLastName(String value) {
        put(lastNameKey, value);
    }

    public String getBloodType() {
        return getString(bloodTypeKey);
    }

    public void setBloodType(String value) {
        put(bloodTypeKey, value);
    }

    public Date getDateOfBirth() {
        return getDate(dateOfBirthKey);
    }

    public void setDateOfBirth(Date value) {
        put(dateOfBirthKey, value);
    }

    public String getPhoneNumber() {
        return getString(phoneNumberKey);
    }

    public void setPhoneNumber(String value) {
        put(phoneNumberKey, value);
    }

    public ParseFile getProfilePicture() {
        return getParseFile(profilePictureKey);
    }

    public void setProfilePicture(ParseFile value) {
        put(profilePictureKey, value);
    }

    public String getCity() {
        return getString(cityKey);
    }

    public void setCity(String value) {
        put(cityKey, value);
    }

    public JSONArray getDonations() {
        return getJSONArray(donationsKey);
    }

    public void setDonationsKey(JSONArray value) {
        put(donationsKey, value);
    }

    public void getDataForParseFile(ParseFile file, GetDataCallback getDataCallback) {
        file.getDataInBackground(getDataCallback);
    }

    public void save(SaveCallback saveCallback) {
        this.saveInBackground(saveCallback);
    }

    public void retrieveUser(ArrayList<String> phoneNumbers, FindCallback findCallback) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("User");
        query.whereContainedIn(phoneNumberKey, phoneNumbers);

        query.findInBackground(findCallback);
    }
}
