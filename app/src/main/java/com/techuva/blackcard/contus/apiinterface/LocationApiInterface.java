package com.techuva.blackcard.contus.apiinterface;

import com.techuva.blackcard.contus.responsemodel.LocationResponseModel;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

public interface LocationApiInterface {
    // annotation used to post the data
    @GET("/api/geocode/json")//An object can be specified for use as an HTTP request body with the @Body annotation.
    //Request for location google api
    void getHomePage(@Query("sensor") String storeId, @Query("address") String websiteId, Callback<LocationResponseModel> callback);

}