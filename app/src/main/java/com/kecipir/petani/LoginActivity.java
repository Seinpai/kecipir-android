package com.kecipir.petani;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.kecipir.petani.preference.AccountPreference;
import com.kecipir.petani.rest.Api;
import com.kecipir.petani.rest.response.AuthResponse;
import com.kecipir.petani.rest.response.ErrorReponse;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * A login screen that offers login via phone/password.
 */
public class LoginActivity extends BaseActivity {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;
    private Api mApi;
    private AccountPreference mAccountPreference;

    // UI references.
    private AutoCompleteTextView mPhoneView;
    private EditText mPasswordView;
    private ProgressDialog mProgressView;
    private View mLoginFormView;
    private LoginResult mResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mPhoneView = (AutoCompleteTextView) findViewById(R.id.phone);
        getSupportActionBar().hide();

        mApi = getApp().getRestApi();
        mAccountPreference = getApp().getAccountPreference();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mPhoneSignInButton = (Button) findViewById(R.id.phone_sign_in_button);
        mPhoneSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Light_Dialog);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        return false;
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid phone, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mPhoneView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String phone = mPhoneView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid phone address.
        if (TextUtils.isEmpty(phone)) {
            mPhoneView.setError(getString(R.string.error_field_required));
            focusView = mPhoneView;
            cancel = true;
        } else if (!isPhoneValid(phone)) {
            mPhoneView.setError(getString(R.string.error_invalid_phone));
            focusView = mPhoneView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(phone, password);
            mAuthTask.execute((Void) null);
        }
    }

    private static class LoginResult {
        final boolean success;
        final String error;

        public LoginResult(boolean success, String error) {
            this.success = success;
            this.error = error;
        }
    }

    private boolean isPhoneValid(String phone) {
        //TODO: Replace this with your own logic
        return !phone.contains(" ");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    private void showProgress(final boolean show) {
        mProgressView.setIndeterminate(true);
        mProgressView.setCancelable(false);
        mProgressView.setMessage("Authenticating...");

        if (show)
            mProgressView.show();
        else if (!show && mProgressView.isShowing())
            mProgressView.dismiss();
    }

    private void addPhonesToAutoComplete(List<String> phoneAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, phoneAddressCollection);

        mPhoneView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, LoginResult> {
        private static final String TAG = "LoginTask";
        private final Api api;
        private final AccountPreference accountPreference;
        private final String phone, password;

        UserLoginTask(String phone, String password) {
            api = getApp().getRestApi();
            accountPreference = getApp().getAccountPreference();
            this.phone = phone;
            this.password = password;
        }

        @Override
        protected LoginResult doInBackground(Void... params) {
            // attempt authentication against a network service.
            Call<AuthResponse> authCall;
            authCall = api.login(phone, password, Api.KEY);

            try {
                Response<AuthResponse> authResponse = authCall.execute();

                if (authResponse.isSuccessful()) {
                    AuthResponse auth = authResponse.body();
                    Log.d(TAG, "token => " + auth.getCredential().getToken());
                    accountPreference.saveLogin(phone, password);
                    accountPreference.saveToken(auth.getCredential().getToken());
                    accountPreference.saveEmail(auth.getCredential().getEmail());
                    accountPreference.saveName(auth.getCredential().getName());
                    accountPreference.savePhoto(auth.getCredential().getPhoto());
                    return new LoginResult(true, null);
                }

                String errorString = authResponse.errorBody().string();
                Log.d(TAG, "error => " + errorString);
                Gson gson = new GsonBuilder()
                        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                        .create();
                String error = null;

                try {
                    ErrorReponse e = gson.fromJson(errorString, ErrorReponse.class);
                    error = e.getMessage();
                } catch (JsonSyntaxException e) {
                    error = getResources().getString(R.string.error_occured);
                }

                return new LoginResult(false, error);
            } catch (IOException e) {
                e.printStackTrace();
                return new LoginResult(false, null);
            }
        }

        @Override
        protected void onPostExecute(LoginResult result) {
            mAuthTask = null;
            showProgress(false);

            if (result.success) {
                Intent main = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(main);
                finish();
            } else {
                Toast.makeText(getApplicationContext(),
                        (TextUtils.isEmpty(result.error) ? getResources().getString(R.string.error_internet_connection) : result.error),
                        Toast.LENGTH_SHORT).show();

                mPhoneView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

