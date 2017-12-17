package com.kecipir.petani.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.kecipir.petani.R;

public class LoadingDialog extends DialogFragment {

    private static final String TITLE = "title";
    private static final String MESSAGE = "message";

    public LoadingDialog() {
    }

    public static LoadingDialog create(String title, String message) {
        LoadingDialog d = new LoadingDialog();
        Bundle args = new Bundle(2);
        args.putString(TITLE, title);
        args.putString(MESSAGE, message);
        d.setArguments(args);
        return d;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ProgressDialog dialog = new ProgressDialog(getContext(), R.style.AppTheme_Dialog_Alert);
        Bundle args = getArguments();
        dialog.setTitle(args.getString(TITLE));
        dialog.setMessage(args.getString(MESSAGE));
        return dialog;
    }
}
