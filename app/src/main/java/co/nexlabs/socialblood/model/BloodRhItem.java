package co.nexlabs.socialblood.model;

/**
 * Created by myozawoo on 3/15/16.
 */
public class BloodRhItem {

    private int bloodRhId;
    private String bloodRhType;

    public int getBloodRhId() {
        return bloodRhId;
    }

    public void setBloodRhId(int bloodRhId) {
        this.bloodRhId = bloodRhId;
    }

    public String getBloodRhType() {
        return bloodRhType;
    }

    public void setBloodRhType(String bloodRhType) {
        this.bloodRhType = bloodRhType;
    }

    public BloodRhItem(int bloodRhId, String bloodRhType) {
        this.bloodRhId = bloodRhId;
        this.bloodRhType = bloodRhType;
    }
}
