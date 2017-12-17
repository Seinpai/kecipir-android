package com.kecipir.petani.preference;

/**
 * Created by Patriot Muslim on 12/07/2017.
 */

import com.kecipir.petani.AppContainer;
import com.kecipir.petani.BuildConfig;

public class AppPreference extends Preference {

    private static final int APP_VERSION = 1;

    public AppPreference(AppContainer app) {
        super(app, "app", true);
    }

    public void setInitialized(boolean initialized) {
        sharePreferences.edit()
                .putBoolean("initialized", initialized)
                .putInt("version", APP_VERSION)
                .apply();
    }

    public boolean isInitialized() {
        return  sharePreferences.getBoolean("initialized", false) &&
                sharePreferences.getInt("version", 0) == APP_VERSION;
    }

    public boolean isUpdateAvailable() {
        int versionCode = sharePreferences.getInt("version_code", BuildConfig.VERSION_CODE);
        return versionCode > BuildConfig.VERSION_CODE;
    }

    public void setUpdateAvailable(int versionCode) {
        sharePreferences.edit().putInt("version_code", versionCode)
                .apply();
    }
}
