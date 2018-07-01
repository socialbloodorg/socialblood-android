package co.nexlabs.socialblood.model;

/**
 * Created by myozawoo on 3/15/16.
 */
public class BloodItem {

    private int bloodId;
    private String bloodType;

    public BloodItem(int bloodId, String bloodType) {
        this.bloodId = bloodId;
        this.bloodType = bloodType;
    }

    public int getBloodId() {
        return bloodId;
    }

    public void setBloodId(int bloodId) {
        this.bloodId = bloodId;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

}
