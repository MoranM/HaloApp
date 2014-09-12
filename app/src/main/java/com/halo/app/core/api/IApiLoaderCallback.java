package com.halo.app.core.api;

/**
 * Created by MoranDev on 9/12/2014.
 */
public interface IApiLoaderCallback {
    void onResultReceived(IApiResult result);
    void onError(String message);
}
