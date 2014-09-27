package com.halo.app.ui.services;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.halo.app.BootstrapApplication;
import com.halo.app.core.Constants;
import com.halo.app.core.api.RegisterGcmDeviceIdApiCallExecuter;
import com.halo.app.core.storage.SharedPreferencesStorage;
import com.halo.app.ui.BaseWithoutActionBarActivity;
import com.halo.app.util.Ln;

import java.io.IOException;

/**
 * Created by MoranDev on 9/27/2014.
 */
public class GcmRegistrationService {
    private static GcmRegistrationService instance;
    private BaseWithoutActionBarActivity context;
    private GoogleCloudMessaging gcm;
    private String regid;


    public static GcmRegistrationService getInstance(){
        if (instance == null)
            instance = new GcmRegistrationService();

        return instance;
    }

    public boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, context,
                        Constants.Notification.PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Ln.e("This device is not supported.");
                context.finish();
            }
            return false;
        }
        return true;
    }

    public void registerIfNeeded(){
        String regId = SharedPreferencesStorage.getInstance().get(Constants.Notification.PROPERTY_REG_ID);

        if (regId.isEmpty())
            registerInBackground();

    }

    private void storeRegistrationId(String regId) {
        int appVersion = getAppVersion();
        Ln.i("Saving regId on app version " + appVersion);
        final SharedPreferencesStorage storage = SharedPreferencesStorage.getInstance();

        storage.save(Constants.Notification.PROPERTY_REG_ID, regId);
        storage.save(Constants.Notification.PROPERTY_APP_VERSION, appVersion);
    }

    private static int getAppVersion() {
        try {
            final BootstrapApplication appContext = BootstrapApplication.getInstance();
            PackageInfo packageInfo = appContext.getPackageManager()
                    .getPackageInfo(appContext.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    public void setContext(BaseWithoutActionBarActivity context) {
        this.context = context;
    }

    private void sendRegistrationIdToBackend(String regid) {
        final RegisterGcmDeviceIdApiCallExecuter remoteRegistrationService = RegisterGcmDeviceIdApiCallExecuter.getInstance();
        remoteRegistrationService.setDeviceId(regid);

        remoteRegistrationService.execute();

    }

    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regid = gcm.register(Constants.Notification.SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;

                    sendRegistrationIdToBackend(regid);
                    storeRegistrationId(regid);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
               Ln.d(msg + "\n");
            }
        }.execute(null, null, null);
    }
}
