package com.halo.app.core.apiResults;

import com.halo.app.core.api.IApiResult;
import com.halo.app.core.model.RegisterDevice;

public class RegisterDeviceResult implements IApiResult {
    private RegisterDevice result;


    public RegisterDevice getResult() {
        return result;
    }
}
