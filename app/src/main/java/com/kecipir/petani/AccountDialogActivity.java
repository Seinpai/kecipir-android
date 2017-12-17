package com.kecipir.petani;

/**
 * Created by Patriot Muslim on 12/07/2017.
 */

import android.content.DialogInterface;
import android.os.Bundle;

import com.kecipir.petani.dialog.AccountChangeDialog;

public class AccountDialogActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AccountChangeDialog dialog = new AccountChangeDialog();
        dialog.setCancelable(false);
        dialog.setDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                finish();
            }
        });
        dialog.show(getSupportFragmentManager(), "AccountChangeDialog");
    }

}
