package com.kecipir.petani.rest.callback;

/**
 * Created by Patriot Muslim on 12/07/2017.
 */

import android.content.Intent;
import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.kecipir.petani.AccountDialogActivity;
import com.kecipir.petani.App;
import com.kecipir.petani.R;
import com.kecipir.petani.preference.AccountPreference;
import com.kecipir.petani.rest.Api;
import com.kecipir.petani.rest.response.AuthResponse;
import com.kecipir.petani.rest.response.ErrorReponse;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class TokenCallback<T> implements Callback<T> {

    private static final String TAG = TokenCallback.class.getSimpleName();
    private final Api api;
    private final AccountPreference accountPreference;
    private String errorMessage;
    private int errorCode;

    public abstract void onResponse(Call<T> call, Response<T> response, boolean hasNewToken);

    public abstract Call<T> recreateCall(String newToken);

    public TokenCallback(Api api, AccountPreference accountPreference) {
        this.api = api;
        this.accountPreference = accountPreference;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful()) {
            onResponse(call, response, false);
        } else {
            try {
                String responseString = response.errorBody().string();
                parseError(responseString);

                // Invalid or expired token
                if (errorCode == 401) {
                    updateToken();
                } else {
                    onResponse(call, response, false);
                }
            } catch (IOException e) {
                onResponse(call, response, false);
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        Log.d(TAG, "onFailure()", t);
    }

    private void updateToken() {
        final String phone = accountPreference.getPhone();
        final String password = accountPreference.getPassword();
        Call<AuthResponse> authCall;
        authCall = api.login(phone, password, Api.KEY);

        Log.d(TAG, "updateToken");
        authCall.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful()) {
                    AuthResponse authResponse = response.body();
                    Log.d(TAG, "token => " + authResponse.getCredential().getToken());
                    accountPreference.saveToken(authResponse.getCredential().getToken());
                    accountPreference.saveLogin(phone, password);
                    retryCall(authResponse.getCredential().getToken());
                } else {
                    Intent intent = new Intent(accountPreference.getContext(), AccountDialogActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    accountPreference.getContext().startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Log.d(TAG, "onFailure()", t);
            }
        });
    }

    private void retryCall(String token) {
        Log.d(TAG, "retry");
        Call<T> call = recreateCall(token);
        call.enqueue(new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                TokenCallback.this.onResponse(call, response, true);
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                Log.d(TAG, "onFailure()", t);
            }
        });
    }

    private void parseError(String responseString) {
        final Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        try {
            ErrorReponse e = gson.fromJson(responseString, ErrorReponse.class);

            errorCode    = e==null ? 500 : e.getStatusCode();
            errorMessage = e==null ? "Unknown Error" : e.getMessage();
        } catch (JsonSyntaxException e) {
            errorMessage = App.getContext().getResources().getString(R.string.error_occured);
        }
    }
}
