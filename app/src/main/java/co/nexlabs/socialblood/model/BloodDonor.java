package co.nexlabs.socialblood.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by myozawoo on 3/17/16.
 */
public class BloodDonor implements Serializable{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("phone_no")
    @Expose
    private String phoneNo;
    @SerializedName("profile_photo_url")
    @Expose
    private String profilePhotoUrl;
    @SerializedName("profile_photo_thumb_url")
    @Expose
    private String profilePhotoThumbUrl;
    @SerializedName("blood_type_id")
    @Expose
    private Integer bloodTypeId;
    @SerializedName("blood_rh_type_id")
    @Expose
    private Integer bloodRhTypeId;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("city_name")
    @Expose
    private String cityName;
    @SerializedName("distance")
    @Expose
    private Double distance;

    public BloodDonor(Integer id, String name, String phoneNo, String profilePhotoUrl, String profilePhotoThumbUrl, Integer bloodTypeId, Integer bloodRhTypeId, String address, String cityName, Double distance) {
        this.id = id;
        this.name = name;
        this.phoneNo = phoneNo;
        this.profilePhotoUrl = profilePhotoUrl;
        this.profilePhotoThumbUrl = profilePhotoThumbUrl;
        this.bloodTypeId = bloodTypeId;
        this.bloodRhTypeId = bloodRhTypeId;
        this.address = address;
        this.cityName = cityName;
        this.distance = distance;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    /**
     *
     * @return
     * The id
     */
    public Integer getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     * The phoneNo
     */
    public String getPhoneNo() {
        return phoneNo;
    }

    /**
     *
     * @param phoneNo
     * The phone_no
     */
    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    /**
     *
     * @return
     * The bloodTypeId
     */
    public Integer getBloodTypeId() {
        return bloodTypeId;
    }

    /**
     *
     * @param bloodTypeId
     * The blood_type_id
     */
    public void setBloodTypeId(Integer bloodTypeId) {
        this.bloodTypeId = bloodTypeId;
    }

    /**
     *
     * @return
     * The bloodRhTypeId
     */
    public Integer getBloodRhTypeId() {
        return bloodRhTypeId;
    }

    /**
     *
     * @param bloodRhTypeId
     * The blood_rh_type_id
     */
    public void setBloodRhTypeId(Integer bloodRhTypeId) {
        this.bloodRhTypeId = bloodRhTypeId;
    }


    public String getProfilePhotoUrl() {
        return profilePhotoUrl;
    }

    public void setProfilePhotoUrl(String profilePhotoUrl) {
        this.profilePhotoUrl = profilePhotoUrl;
    }

    public String getProfilePhotoThumbUrl() {
        return profilePhotoThumbUrl;
    }

    public void setProfilePhotoThumbUrl(String profilePhotoThumbUrl) {
        this.profilePhotoThumbUrl = profilePhotoThumbUrl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }


}
