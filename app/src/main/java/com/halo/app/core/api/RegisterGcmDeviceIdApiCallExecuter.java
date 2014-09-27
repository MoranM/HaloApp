package com.halo.app.core.api;

import com.halo.app.Injector;

import javax.inject.Inject;

import retrofit.RestAdapter;

public class RegisterGcmDeviceIdApiCallExecuter implements IApiCallExecuter {

    @Inject
    protected RestAdapter restAdapter;

    private String deviceId;

    private static RegisterGcmDeviceIdApiCallExecuter instance;

    private RegisterGcmDeviceIdApiCallExecuter() {
        Injector.inject(this);
    }

    public static RegisterGcmDeviceIdApiCallExecuter getInstance(){
        if(instance == null)
            instance = new RegisterGcmDeviceIdApiCallExecuter();

        return instance;
    }

    @Override
    public IApiResult execute() {
        return restAdapter.create(GcmRegisterDeviceApiCall.class).registerDevice(deviceId);
    }


    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
