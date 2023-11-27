package com.timnhatro1.asus.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.timnhatro1.asus.view.activity.R;

public class DialogFindPassword extends Dialog {

    View mLlApply;
    DialogAddNote.OnFilterListener onFilterListener;
    EditText mEtNote;
    Context context;

    public DialogFindPassword(@NonNull Context context, final DialogAddNote.OnFilterListener onFilterListener,String title, String textButton) {
        super(context,android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
        setContentView(R.layout.dialog_find_password);
        ((TextView) findViewById(R.id.tv_title)).setText(title);
        ((TextView) findViewById(R.id.tv_apply)).setText(textButton);
        mLlApply = findViewById(R.id.ll_apply);
        mEtNote = findViewById(R.id.et_note);
        this.context = context;
        this.onFilterListener = onFilterListener;
        mLlApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    onFilterListener.onClickApply(mEtNote.getText().toString());
                    dismiss();
                }
            }
        });
    }

    private boolean validate() {
        if (mEtNote.getText().toString().trim().isEmpty()) {
            Toast.makeText(context,"Nhập email đăng ký", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public interface OnFilterListener {
        void onClickApply(String note);
    }
}
