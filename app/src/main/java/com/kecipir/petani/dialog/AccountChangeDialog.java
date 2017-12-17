package com.kecipir.petani.dialog;

/**
 * Created by Patriot Muslim on 12/07/2017.
 */


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.kecipir.petani.R;
import com.kecipir.petani.preference.AccountPreference;
import com.kecipir.petani.BaseActivity;

public class AccountChangeDialog extends DialogFragment {

    private AccountPreference mAccountPreference;

    private DialogInterface.OnDismissListener dismissListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAccountPreference = ((BaseActivity) getActivity()).getApp().getAccountPreference();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AppTheme_Dialog_Alert);
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_form_change_account, null);
        initView(view);
        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (dismissListener != null) {
            dismissListener.onDismiss(dialog);
        }
    }

    public void setDismissListener(DialogInterface.OnDismissListener dismissListener) {
        this.dismissListener = dismissListener;
    }

    private void initView(View view) {
        TextView textTitle = (TextView) view.findViewById(R.id.text_title);
        textTitle.setText("Ubah No. Telepon & Password");
        final TextInputEditText inputPhone = (TextInputEditText) view.findViewById(R.id.et_phone);
        final TextInputEditText inputPassword = (TextInputEditText) view.findViewById(R.id.et_password);
        String phone = mAccountPreference.getPhone();
        inputPhone.setText(phone);
        if (!TextUtils.isEmpty(phone)) {
            inputPhone.setSelection(phone.length());
        }
        view.findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissAllowingStateLoss();
            }
        });
        view.findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = inputPhone.getText().toString();
                String password = inputPassword.getText().toString();
                if (!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(password)) {
                    mAccountPreference.saveLogin(phone, password);
                    dismissAllowingStateLoss();
                }
            }
        });
    }
}
