package com.kecipir.petani.preference;

/**
 * Created by Patriot Muslim on 12/07/2017.
 */

import android.content.Context;
import android.content.SharedPreferences;

import com.securepreferences.SecurePreferences;
import com.kecipir.petani.AppContainer;

/**
 * Abstract Preference.
 */
public abstract class Preference {

    protected final SharedPreferences sharePreferences;
    private final boolean secure;
    private final Context context;

    // Depend on AppContainer
    public Preference(AppContainer app, String name, boolean secure) {
        if (secure) {
            this.sharePreferences = new SecurePreferences(app.getContext(), (String) null, name);
        } else {
            this.sharePreferences = app.getContext().getSharedPreferences(name,
                    Context.MODE_PRIVATE);
        }
        this.secure = secure;
        this.context = app.getContext();
    }

    protected final boolean isSecure() {
        return secure;
    }

    public final Context getContext() {
        return context;
    }
}
