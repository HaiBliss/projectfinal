package com.timnhatro1.asus.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.timnhatro1.asus.view.activity.R;


public class DialogAddNote extends Dialog {

    View mLlApply;
    DialogAddNote.OnFilterListener onFilterListener;
    EditText mEtNote;

    public DialogAddNote(@NonNull Context context, final DialogAddNote.OnFilterListener onFilterListener) {
        super(context,android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
        setContentView(R.layout.dialog_add_note);
        mLlApply = findViewById(R.id.ll_apply);
        mEtNote = findViewById(R.id.et_note);
        this.onFilterListener = onFilterListener;
        mLlApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               onFilterListener.onClickApply(mEtNote.getText().toString());
               dismiss();
            }
        });
    }
    public DialogAddNote(@NonNull Context context, final DialogAddNote.OnFilterListener onFilterListener,String title, String textButton) {
        super(context,android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
        setContentView(R.layout.dialog_add_note);
        ((TextView) findViewById(R.id.tv_title)).setText(title);
        ((TextView) findViewById(R.id.tv_apply)).setText(textButton);
        mLlApply = findViewById(R.id.ll_apply);
        mEtNote = findViewById(R.id.et_note);
        this.onFilterListener = onFilterListener;
        mLlApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFilterListener.onClickApply(mEtNote.getText().toString());
                dismiss();
            }
        });
    }
    public interface OnFilterListener {
        void onClickApply(String note);
    }
}
