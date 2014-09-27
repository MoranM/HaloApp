package com.halo.app.core.api;

import com.halo.app.core.Constants;
import com.halo.app.core.apiResults.RegisterDeviceResult;

import retrofit.http.GET;
import retrofit.http.Query;

public interface GcmRegisterDeviceApiCall {

    @GET(Constants.Http.URL_GCM_REGISTER)
    RegisterDeviceResult registerDevice(@Query("device_id") String id);
}
