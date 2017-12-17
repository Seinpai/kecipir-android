package com.kecipir.petani;

/**
 * Created by Patriot Muslim on 12/07/2017.
 */

import android.content.Context;

/**
 * Application Container.
 */
public class AppContainer {

    private final App app;

    AppContainer(App app) {
        this.app = app;
    }

    public Context getContext() {
        return app.getApplicationContext();
    }
}
