package com.techuva.blackcard.contus.apiinterface;

import com.techuva.blackcard.contus.app.Constants;
import com.techuva.blackcard.contus.responsemodel.SMSverifyModel;

import retrofit.Callback;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;

/**
 * Created by user on 9/18/2015.
 */
public interface SMSverifyApi {
    // annotation used to post the data
    @Multipart
    @POST(Constants.SMS_VERIFY_API)//An object can be specified for use as an HTTP request body with the @Body annotation.
    //Sms veify request
    void postSMSgatewayVerify(@Part(Constants.ACTION) String email, @Part(Constants.PHONE_NUM) String phoneNumber, @Part(Constants.OPTCODE) String optCode,
                              @Part("device_token") String device_token, @Part("os_name") String os_name, Callback<SMSverifyModel> callback);
}
