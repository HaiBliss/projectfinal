package com.timnhatro1.asus.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.timnhatro1.asus.view.activity.R;
import com.gemvietnam.utils.StringUtils;

public class DialogConfrim extends Dialog implements View.OnClickListener {
    View btnClose;
    TextView tvDescription;
    TextView tvTitle;
    TextView tvConfirmRegister;
    TextView btnNo;
    OnClickDialogTTC delegate;

    public DialogConfrim(Context context, String title, String description, String button, OnClickDialogTTC delegate) {
        super(context, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
        this.delegate = delegate;
        setContentView(R.layout.dialog_confirm);
        btnClose = findViewById(R.id.btnClose);
        tvConfirmRegister = (TextView) findViewById(R.id.btnYes);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        btnNo = findViewById(R.id.btnNo);
        btnNo.setOnClickListener(this);
        btnNo.setVisibility(View.GONE);
        tvDescription.setText(description);
        tvTitle.setText(title);
        tvConfirmRegister.setText(StringUtils.isEmpty(button) ? "Đồng ý" : button);
        btnClose.setOnClickListener(this);
        tvConfirmRegister.setOnClickListener(this);
    }
    public DialogConfrim(Context context, String description, OnClickDialogTTC delegate) {
        super(context, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
        this.delegate = delegate;
        setContentView(R.layout.dialog_confirm);
        btnClose = findViewById(R.id.btnClose);
        tvConfirmRegister = (TextView) findViewById(R.id.btnYes);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        btnNo = findViewById(R.id.btnNo);
        btnNo.setVisibility(View.GONE);
        tvDescription.setText(description);
        btnClose.setOnClickListener(this);
        tvConfirmRegister.setOnClickListener(this);
    }
    public DialogConfrim(Context context, String title, String description, String button1,String button2, OnClickDialogTTC delegate) {
        super(context, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
        this.delegate = delegate;
        setContentView(R.layout.dialog_confirm);
        btnClose = findViewById(R.id.btnClose);
        tvConfirmRegister = (TextView) findViewById(R.id.btnYes);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        btnClose.setVisibility(View.GONE);
        btnNo = findViewById(R.id.btnNo);
        btnNo.setOnClickListener(this);
        tvDescription.setText(description);
        tvTitle.setText(title);
        tvConfirmRegister.setText(StringUtils.isEmpty(button1) ? "Đồng ý" : button1);
        btnNo.setText(StringUtils.isEmpty(button2) ? "Huỷ bỏ" : button2);
        btnClose.setOnClickListener(this);
        tvConfirmRegister.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {

        if (v == tvConfirmRegister) {
            if (delegate != null) {
                delegate.onButonYesClick();
            }
            setOnDismissListener(null);
            this.dismiss();
            return;
        }

        if (v == btnClose) {
            if (delegate != null) {
                delegate.onButtonCloseClick();
            }
            setOnDismissListener(null);
            this.dismiss();
            return;
        }
        if (v == btnNo) {
            if (delegate != null) {
                delegate.onButtonNoClick();
            }
            setOnDismissListener(null);
            this.dismiss();
        }
    }

    public void setListener(OnClickDialogTTC onDialogConfirm){
        this.delegate = onDialogConfirm;
    }

    public void setDesc(String title) {
        tvDescription.setText(title);
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void setTvConfirmRegister(String text) {
        tvConfirmRegister.setText(text);
    }
    public interface OnClickDialogTTC {
        void onButonYesClick();
        void onButtonNoClick();
        void onButtonCloseClick();
    }

}
