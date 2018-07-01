package co.nexlabs.socialblood.rest;

import java.util.List;

import co.nexlabs.socialblood.model.BloodBank;
import co.nexlabs.socialblood.model.BloodDonor;
import co.nexlabs.socialblood.model.Message;
import co.nexlabs.socialblood.model.User;
import retrofit.http.DELETE;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.Callback;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by myozawoo on 3/14/16.
 */
public interface RestService {

    @FormUrlEncoded
    @POST("/users/sign_up") void signUp(
            @Field("email") String email,
            @Field("password") String password,
            @Field("password_confirmation") String password_confirmation,
            @Field("name") String name,
            @Field("phone_no") String phone_no,
            @Field("blood_type_id") String blood_type_id,
            @Field("blood_rh_type_id") String blood_rh_type_id,
            @Field("address") String address,
            @Field("latitude") String latitude,
            @Field("longitude") String longitude,
            @Field("about") String about,
            @Field("has_pet") String has_pet,
            @Field("pet") String pet,
            Callback<User> callback
    );

    @FormUrlEncoded
    @POST("/users/login")
    void login(
            @Field("email") String email,
            @Field("password") String password,
            Callback<User> callback
    );

    @FormUrlEncoded
    @POST("/users/fb_login")
    void fbLogin(
            @Field("fb_access_token") String accessToken,
            Callback<User> callback
    );


    @DELETE("/users/logout")
    void logout(
            @Query("auth_token") String authToken,
            Callback<Message> callback
    );

    @FormUrlEncoded
    @PUT("/users/{id}/update_location")
    void updateLocation(
            @Path("id") int id,
            @Query("auth_token") String auth_token,
            @Field("latitude") String latitude,
            @Field("longitude") String longitude,
            Callback<User> callback
    );

    @FormUrlEncoded
    @PUT("/users/{id}")
    void updateProfileForFacebookLogin(
            @Path("id") int id,
            @Query("auth_token") String auth_token,
            @Field("name") String name,
            @Field("phone_no") String phone_no,
            @Field("blood_type_id") String blood_type_id,
            @Field("blood_rh_type_id") String blood_rh_type_id,
            @Field("address") String address,
            @Field("latitude") String latitude,
            @Field("longitude") String longitude,
            @Field("about") String about,
            @Field("has_pet") String has_pet,
            @Field("pet") String pet,
            Callback<User> callback

    );

    @GET("/search") void searchBloodDonors(
            @Query("blood_type_id") String blood_type_id,
            @Query("blood_rh_type_id") String blood_rh_type_id,
            @Query("latitude") String latitude,
            @Query("longitude") String longitude,
            Callback<List<BloodDonor>> callback
    );

    @GET("/blood_banks") void searchBloodBanks(
            @Query("auth_token") String auth_token,
            Callback<List<BloodBank>> callback
    );

    @FormUrlEncoded
    @PUT("/users/{id}")
    void updateProfile(
            @Path("id") int id,
            @Query("auth_token") String auth_token,
            @Field("name") String name,
            @Field("phone_no") String phone_no,
            @Field("profile_photo") String profile_photo,
            @Field("blood_type_id") String blood_type_id,
            @Field("blood_rh_type_id") String blood_rh_type_id,
            @Field("about") String about,
            Callback<User> callback

    );

    @FormUrlEncoded
    @PUT("/users/{id}/update_profile_photo")
    void updateProfilePhoto(
            @Path("id") int id,
            @Query("auth_token") String auth_token,
            @Field("profile_photo") String profile_photo,
            Callback<User> callback

    );

    @FormUrlEncoded
    @PUT("/users/{id}/update_phone_no")
    void updatePhoneNumber(
            @Path("id") int id,
            @Query("auth_token") String auth_token,
            @Field("phone_no") String phone_no,
            Callback<User> callback
    );

    @FormUrlEncoded
    @PUT("/users/{id}/update_blood_type")
    void updateBlood(
            @Path("id") int id,
            @Query("auth_token") String auth_token,
            @Field("blood_type_id") String blood_type_id,
            @Field("blood_rh_type_id") String blood_rh_type_id,
            Callback<User> callback
    );

    @FormUrlEncoded
    @PUT("/users/{id}/update_address")
    void updateAddress(
            @Path("id") int id,
            @Query("auth_token") String auth_token,
            @Field("address") String address,
            Callback<User> callback
    );

    @FormUrlEncoded
    @PUT("/users/{id}/update_birth_date")
    void updateDateOfBirth(
            @Path("id") int id,
            @Query("auth_token") String auth_token,
            @Field("birth_date") String birth_date,
            Callback<User> callback
    );

    @FormUrlEncoded
    @PUT("/users/{id}/update_gender")
    void updateGender(
            @Path("id") int id,
            @Query("auth_token") String auth_token,
            @Field("gender") String gender,
            Callback<User> callback
    );

    @FormUrlEncoded
    @PUT("/users/{id}/update_pet")
    void updatePet(
            @Path("id") int id,
            @Query("auth_token") String auth_token,
            @Field("has_pet") String has_pet,
            @Field("pet") String pet,
            Callback<User> callback
    );






}
