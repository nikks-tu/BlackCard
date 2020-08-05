package com.techuva.blackcard.new_changes.api_interface;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.techuva.blackcard.contus.app.Constants;
import com.techuva.blackcard.new_changes.post_parameters.OTPValidatePostParameters;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface CheckOtpDataInterface {

    @POST("api/v1"+ Constants.SMS_VERIFY_API)
   // Call<CurrentDataMainObject> fetchCurrentData(@Part("deviceId") String deviceId, @Part("userId") String userId);
   // Call<CheckOTPMainObject> getStringScalar(@Body OTPValidatePostParameters postParameter);
    Call<JsonElement> getStringScalar(@Body OTPValidatePostParameters postParameter);

    @POST("api/v1"+ Constants.SMS_VERIFY_API)
   // Call<CurrentDataMainObject> fetchCurrentData(@Part("deviceId") String deviceId, @Part("userId") String userId);
   // Call<CheckOTPMainObject> getStringScalar(@Body OTPValidatePostParameters postParameter);
    Call<JsonElement> getStringScalar(@Body JsonObject postParameter);

}
