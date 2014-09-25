package com.halo.app.core.storage;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.halo.app.BootstrapApplication;

/**
 * Created by MoranDev on 9/25/2014.
 */
public class SharedPreferencesStorage {

    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;

    private static SharedPreferencesStorage instance;

    private SharedPreferencesStorage() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(BootstrapApplication.getInstance());
        editor = sharedPreferences.edit();
    }

    public static SharedPreferencesStorage getInstance(){
        if (instance == null)
            instance = new SharedPreferencesStorage();

        return instance;
    }

    public void save(String key, String value){
        editor.putString(key, value);
        editor.commit();
    }

    public String get(String key){
        return sharedPreferences.getString(key,"");
    }
}
