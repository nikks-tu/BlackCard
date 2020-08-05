package com.techuva.blackcard.contusfly.api_interface;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.techuva.blackcard.contus.app.Constants;
import com.techuva.blackcard.contusfly.model.AppVersionPostParameters;
import com.techuva.blackcard.new_changes.post_parameters.SignupPostParameters;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;


public interface AppVersionDataInterface {

    @POST(Constants.APP_VERSION_CHECK)
   // Call<CurrentDataMainObject> fetchCurrentData(@Part("deviceId") String deviceId, @Part("userId") String userId);
    Call<JsonElement> getStringScalar(@Header("authKey") String headerValue, @Body AppVersionPostParameters postParameter);


    @POST(Constants.SEARCH_ANNOUNCEMENT)
   // Call<CurrentDataMainObject> fetchCurrentData(@Part("deviceId") String deviceId, @Part("userId") String userId);
    Call<JsonElement> searchAnnouncements(@Body JsonObject postParameter);


    @POST(Constants.SIGNUP_NEW_USER)
   // Call<CurrentDataMainObject> fetchCurrentData(@Part("deviceId") String deviceId, @Part("userId") String userId);
    Call<JsonElement> newUserSignup(@Body SignupPostParameters postParameter);

    @POST(Constants.GET_USER_COMPLAINTS)
        // Call<CurrentDataMainObject> fetchCurrentData(@Part("deviceId") String deviceId, @Part("userId") String userId);
    Call<JsonElement> getUserComplaints(@Body AppVersionPostParameters postParameter);

    @POST(Constants.Add_Complaint)
        // Call<CurrentDataMainObject> fetchCurrentData(@Part("deviceId") String deviceId, @Part("userId") String userId);
    Call<JsonElement> addUserComplaints(@Body AppVersionPostParameters postParameter);



}
