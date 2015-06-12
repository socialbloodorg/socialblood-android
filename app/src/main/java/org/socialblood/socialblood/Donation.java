package org.socialblood.socialblood;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseClassName;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.Date;

/**
 * Created by aadeshpatel on 6/11/15.
 */

@ParseClassName("Donation")
public class Donation extends ParseObject {
    private static final String donationDateKey = "donationDate";
    private static final String hemoglobinKey = "hemoglobin";
    private static final String weightKey = "weight";
    private static final String bloodPressureKey = "bloodPressure";
    private static final String armUsedKey = "armUsed";
    private static final String userKey = "user";

    public Date getDonationDate() {
        return getDate(donationDateKey);
    }

    public void setDonationDate(Date value) {
        put(donationDateKey, value);
    }

    public float getHemoglobin() {
        return getNumber(hemoglobinKey).floatValue();
    }

    public void setHemoglobin(float value) {
        put(hemoglobinKey, value);
    }

    public int getWeight() {
        return getInt(weightKey);
    }

    public void setWeight(int value) {
        put(weightKey, value);
    }

    public String getBloodPressure() {
        return getString(bloodPressureKey);
    }

    public void setBloodPressure(String value) {
        put(bloodPressureKey, value);
    }

    public String getArmUsed() {
        return getString(armUsedKey);
    }

    public void setArmUsed(String value) {
        put(armUsedKey, value);
    }

    public User getUser() {
        return (User) getParseObject(userKey);
    }

    public void setUser(User user) {
        put(userKey, user);
    }

    public void save(SaveCallback saveCallback) {
        this.saveInBackground(saveCallback);
    }
}
