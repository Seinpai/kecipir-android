package com.kecipir.petani.rest.callback;

/**
 * Created by Patriot Muslim on 12/07/2017.
 */

import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.kecipir.petani.App;
import com.kecipir.petani.R;
import com.kecipir.petani.rest.response.ErrorReponse;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class DefaultCallback<T> implements Callback<T> {
    private static final String TAG = DefaultCallback.class.getSimpleName();
    private String errorMessage;

    public abstract void onResponse(Call<T> call, Response<T> response, boolean hasError);

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        // Do application relavent custom operation like manupulating reponse etc.
        if (response.isSuccessful()) {
            onResponse(call, response, false);
        } else {
            try {
                String responseString = response.errorBody().string();
                errorMessage = parseError(responseString);
                onResponse(call, response, true);
            } catch (IOException e) {
                onResponse(call, response, true);
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        Log.d(TAG, "onFailure()", t);
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    private String parseError(String responseString) {
        final Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        try {
            ErrorReponse e = gson.fromJson(responseString, ErrorReponse.class);
            return e.getMessage();
        } catch (JsonSyntaxException e) {
            return App.getContext().getResources().getString(R.string.error_occured);
        }
    }
}
