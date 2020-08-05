package com.techuva.blackcard.new_changes.api_interface;


import com.techuva.blackcard.contus.app.Constants;
import com.techuva.blackcard.new_changes.models.MobileExistMainObject;
import com.techuva.blackcard.new_changes.post_parameters.MobileExistPostParameters;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface MobileExistDataInterface {

    @POST(Constants.MOBILE_EXIST)
   // Call<CurrentDataMainObject> fetchCurrentData(@Part("deviceId") String deviceId, @Part("userId") String userId);
    Call<MobileExistMainObject> getStringScalar(@Body MobileExistPostParameters postParameter);

}
