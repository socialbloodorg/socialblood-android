package co.nexlabs.socialblood.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by myozawoo on 3/14/16.
 */
public class User implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("birth_date")
    @Expose
    private String birth_date;

    @SerializedName("password_confirmation")
    @Expose
    private String password_confirmation;
    @SerializedName("phone_no")
    @Expose
    private String phoneNo;
    @SerializedName("gender")
    @Expose
    private String gender;
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
    @SerializedName("auth_token")
    @Expose
    private String authToken;
    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("longitude")
    @Expose
    private Double longitude;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("fb_id")
    @Expose
    private String fbId;
    @SerializedName("about")
    @Expose
    private String about;

    @SerializedName("has_pet")
    @Expose
    private boolean hasPet;

    @SerializedName("pet")
    @Expose
    private String pet;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(String birth_date) {
        this.birth_date = birth_date;
    }

    public String getPassword_confirmation() {
        return password_confirmation;
    }

    public void setPassword_confirmation(String password_confirmation) {
        this.password_confirmation = password_confirmation;
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
     * The email
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @param email
     * The email
     */
    public void setEmail(String email) {
        this.email = email;
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
     * The gender
     */
    public String getGender() {
        return gender;
    }

    /**
     *
     * @param gender
     * The gender
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     *
     * @return
     * The profilePhotoUrl
     */
    public String getProfilePhotoUrl() {
        return profilePhotoUrl;
    }

    /**
     *
     * @param profilePhotoUrl
     * The profile_photo_url
     */
    public void setProfilePhotoUrl(String profilePhotoUrl) {
        this.profilePhotoUrl = profilePhotoUrl;
    }

    /**
     *
     * @return
     * The profilePhotoThumbUrl
     */
    public String getProfilePhotoThumbUrl() {
        return profilePhotoThumbUrl;
    }

    /**
     *
     * @param profilePhotoThumbUrl
     * The profile_photo_thumb_url
     */
    public void setProfilePhotoThumbUrl(String profilePhotoThumbUrl) {
        this.profilePhotoThumbUrl = profilePhotoThumbUrl;
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

    /**
     *
     * @return
     * The authToken
     */
    public String getAuthToken() {
        return authToken;
    }

    /**
     *
     * @param authToken
     * The auth_token
     */
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    /**
     *
     * @return
     * The latitude
     */
    public Double getLatitude() {
        return latitude;
    }

    /**
     *
     * @param latitude
     * The latitude
     */
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    /**
     *
     * @return
     * The longitude
     */
    public Double getLongitude() {
        return longitude;
    }

    /**
     *
     * @param longitude
     * The longitude
     */
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    /**
     *
     * @return
     * The address
     */
    public String getAddress() {
        return address;
    }

    /**
     *
     * @param address
     * The address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     *
     * @return
     * The fbId
     */
    public String getFbId() {
        return fbId;
    }

    /**
     *
     * @param fbId
     * The fb_id
     */
    public void setFbId(String fbId) {
        this.fbId = fbId;
    }

    /**
     *
     * @return
     * The about
     */
    public String getAbout() {
        return about;
    }

    /**
     *
     * @param about
     * The about
     */
    public void setAbout(String about) {
        this.about = about;
    }
}
