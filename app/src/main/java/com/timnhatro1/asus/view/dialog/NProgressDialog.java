package com.timnhatro1.asus.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import com.timnhatro1.asus.view.activity.R;

public class NProgressDialog extends Dialog {
    View progressBar;

    public NProgressDialog(Context context) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        setContentView(R.layout.n_dialog_progress);
        progressBar = findViewById(R.id.progress_bar);
        this.setCancelable(false);
    }

    @Override
    public void show() {
        try {
            progressBar.setVisibility(View.VISIBLE);
            super.show();
        } catch (Exception e) {

        }
    }
}
