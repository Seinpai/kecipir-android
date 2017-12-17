package com.kecipir.petani;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Window;
import android.view.WindowManager;

import com.kecipir.petani.preference.AppPreference;
import com.kecipir.petani.preference.AccountPreference;

public class SplashActivity extends BaseActivity {
    private static final String TAG = SplashActivity.class.getSimpleName();
    private boolean mInitialized;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ActionBar actionBar = getSupportActionBar();

        if(null != actionBar){
            actionBar.hide();
        }

        AppPreference appPreference = getApp().getAppPreference();

        if (!(mInitialized = appPreference.isInitialized())) {
            new InitializeTask().execute();
            return;
        }

        startActivity();
    }

    @Override
    public void onBackPressed() {
        if (mInitialized) {
            super.onBackPressed();
        }
    }

    void startActivity() {
        AccountPreference accountPreference = getApp().getAccountPreference();

        if (accountPreference.isLogin())
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        else
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));

        finish();
    }

    private class InitializeTask extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            boolean allDone = true;

            /**
             * TODO: Save init data before start using app
             *

             Api api = getApp().getRestApi();

            try {
                Response<List<Category>> responseCategories = api.getCategories(Api.KEY).execute();
                if (responseCategories.isSuccess()) {
                    saveCategories(responseCategories.body());
                } else {
                    allDone = false;
                }
            } catch (IOException e) {
                e.printStackTrace();
                allDone = false;
            }
            */

            return allDone;
        }

        @Override
        protected void onPostExecute(Boolean allDone) {
            AppPreference appPreference = getApp().getAppPreference();
            appPreference.setInitialized(allDone);
            startActivity();
        }

        /**
         * TODO: Save init data before start using app
         *
        private void saveCategories(List<Category> list) {
            ArrayList<ContentProviderOperation> operations = new ArrayList<>(list.size() + 1);
            operations.add(ContentProviderOperation.newDelete(DataContract.Category.CONTENT_URI).build());
            for (Category c : list) {
                operations.add(ContentProviderOperation
                        .newInsert(DataContract.Category.CONTENT_URI)
                        .withValue(DataContract.Category._ID, c.getId())
                        .withValue(DataContract.Category.NAME, c.getName())
                        .build());
            }
            try {
                getContentResolver().applyBatch(DataContract.AUTHORITY, operations);
            } catch (RemoteException | OperationApplicationException e) {
                e.printStackTrace();
            }
        }
        */
    }
}