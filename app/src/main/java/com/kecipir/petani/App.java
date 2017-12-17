package com.kecipir.petani;

/**
 * Created by Patriot Muslim on 12/07/2017.
 */

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kecipir.petani.preference.AccountPreference;
import com.kecipir.petani.preference.AppPreference;
import com.kecipir.petani.rest.Api;
import net.danlew.android.joda.JodaTimeAndroid;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Global Application.
 */
public class App extends Application {

    private AppPreference mAppPref;
    private AccountPreference mAccountPref;
    private static Context mContext;
    private Api mRestApi;

    @Override
    public void onCreate() {
        super.onCreate();
        AppContainer app = new AppContainer(this);
        mContext = this;
        mAppPref = new AppPreference(app);
        mAccountPref = new AccountPreference(app);

        // Initialize Joda Time
        JodaTimeAndroid.init(getApplicationContext());

        // Override Font
        overrideFont();
    }

    public AppPreference getAppPreference() {
        return mAppPref;
    }

    public AccountPreference getAccountPreference() {
        return mAccountPref;
    }

    public static Context getContext(){
        return mContext;
    }

    private void overrideFont() {
        setFont("DEFAULT", "fonts/Roboto-Regular.ttf");
        setFont("DEFAULT_BOLD", "fonts/Roboto-Bold.ttf");
        setFont("SANS_SERIF", "fonts/Roboto-Regular.ttf");
        setFont("SERIF", "fonts/Roboto-Regular.ttf");
        setFont("MONOSPACE", "fonts/Inconsolata-g.ttf");
    }

    private void setFont(String staticTypefaceFieldName, String fontAssetName) {
        Typeface regular = Typeface.createFromAsset(getAssets(), fontAssetName);
        replaceFont(staticTypefaceFieldName, regular);
    }

    private void replaceFont(String staticTypefaceFieldName, final Typeface newTypeface) {
        try {
            final Field staticField = Typeface.class
                    .getDeclaredField(staticTypefaceFieldName);
            staticField.setAccessible(true);
            staticField.set(null, newTypeface);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public Api getRestApi() {
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        OkHttpClient.Builder httpClient  = new OkHttpClient.Builder();

        if (mAccountPref != null && mAccountPref.isLogin()) {
            Log.d("App", "Logged in => set header: Bearer " + getAccountPreference().getToken());

            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Interceptor.Chain chain) throws IOException {
                    Request original = chain.request();

                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Authorization", "Bearer "+getAccountPreference().getToken())
                            .addHeader("Cache-Control", "no-cache")
                            .addHeader("Cache-Control", "no-store");

                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            });
        } else {
            Log.d("App", "as Guest");
        }

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.interceptors().add(interceptor);
        }

        httpClient.connectTimeout(60, TimeUnit.SECONDS);
        httpClient.readTimeout(60, TimeUnit.SECONDS);
        httpClient.writeTimeout(60, TimeUnit.SECONDS);

        OkHttpClient client = httpClient.build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return mRestApi = retrofit.create(Api.class);
    }
}