package com.techuva.blackcard.contus.apiinterface;

import com.techuva.blackcard.contus.app.Constants;
import com.techuva.blackcard.contus.responsemodel.DeleteResponseModel;

import retrofit.Callback;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;

/**
 * Created by user on 1/27/2016.
 */
public interface DeleteApiInterface {
    // annotation used to post the data
    @Multipart
    @POST("/deleteAccount")////An object can be specified for use as an HTTP request body with the @Body annotation.
    //Requesting for the country api
    void postDeleteData(@Part(Constants.ACTION) String email, @Part(Constants.USER_ID) String userId, @Part("phone_no") String phoneNumber, Callback<DeleteResponseModel> callback);
}

