package com.kecipir.petani.preference;

/**
 * Created by Patriot Muslim on 12/07/2017.
 */

import android.net.Uri;

import com.kecipir.petani.AppContainer;

/**
 * Account Preference.
 */
public class AccountPreference extends Preference {

    public AccountPreference(AppContainer app) {
        super(app, "account", true);
    }

    /***********************************
     * User Login Object
     **********************************/
    public void saveLogin(String phone, String password) {
        sharePreferences.edit().putString("phone", phone).putString("password", password).apply();
    }

    public void deleteLogin() {
        sharePreferences.edit().remove("phone").remove("password").apply();
    }

    public void deleteAll() {
        sharePreferences.edit().clear().apply();
    }

    public boolean isLogin() {
        return getToken() != null;
    }

    /***********************************
     * User Token Object
     **********************************/
    public String getToken() {
        return sharePreferences.getString("token", null);
    }

    public void saveToken(String token) {
        sharePreferences.edit().putString("token", token).apply();
    }

    public void deleteToken() {
        sharePreferences.edit().remove("token").apply();
    }

    /***********************************
     * User Password Object
     **********************************/
    public void changePassword(String password) {
        sharePreferences.edit().putString("password", password).apply();
    }

    public String getPassword() {
        return sharePreferences.getString("password", null);
    }

    /***********************************
     * User Phone Object
     **********************************/
    public String getPhone() {
        return sharePreferences.getString("phone", null);
    }

    public void changePhone(String phone) {
        sharePreferences.edit().putString("phone", phone).apply();
    }

    /***********************************
     * User Email Object
     **********************************/
    public String getEmail() {
        return sharePreferences.getString("email", null);
    }

    public void saveEmail(String email) {
        sharePreferences.edit().putString("email", email).apply();
    }

    public void deleteEmail() {
        sharePreferences.edit().remove("email").apply();
    }

    /***********************************
     * User Name Object
     **********************************/
    public String getName() {
        return sharePreferences.getString("name", null);
    }

    public void saveName(String name) {
        sharePreferences.edit().putString("name", name).apply();
    }

    public void deleteName() {
        sharePreferences.edit().remove("name").apply();
    }

    /***********************************
     * User Photo Object
     **********************************/
    public Uri getPhoto() {
        try {
            return Uri.parse(sharePreferences.getString("photo", null));
        } catch (NullPointerException e) {
            return null;
        }
    }

    public void savePhoto(String photo) {
        sharePreferences.edit().putString("photo", photo).apply();
    }

    public void deletePhoto() {
        sharePreferences.edit().remove("photo").apply();
    }
}