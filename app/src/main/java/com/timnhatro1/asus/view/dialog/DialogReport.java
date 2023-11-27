package com.timnhatro1.asus.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.timnhatro1.asus.view.activity.R;
import com.timnhatro1.asus.utils.DummyData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DialogReport extends Dialog {
    private Spinner mSpinnerReport;
    private View mLlApply;
    private Context context;
    private OnClickDangKy onClickDangKy;
    private HashMap<String,String> hashMap;
    public DialogReport(@NonNull Context context, OnClickDangKy onClickDangKy) {
        super(context, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
        setContentView(R.layout.dialog_report);
        mSpinnerReport = findViewById(R.id.spinner_report);
        mLlApply = findViewById(R.id.ll_apply);
//        locationModel = DummyData.initDummyData(getContext());
        hashMap = DummyData.initReport();
        this.context = context;
        this.onClickDangKy = onClickDangKy;
        setupView();
        setupOnClick();
    }

    private void setupOnClick() {
        mLlApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    onClickDangKy.onClick(hashMap.get(mSpinnerReport.getSelectedItem().toString()));
                    dismiss();
                }
            }
        });
    }

    private boolean validate() {
        if (mSpinnerReport.getSelectedItem().toString().equals("---")) {
            Toast.makeText(getContext(), "Cần chọn lý do", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void setupView() {
        List<String> listOptionsSpinnerReport = new ArrayList<>();
        listOptionsSpinnerReport.add("---");
        for (String str : hashMap.keySet()) {
            listOptionsSpinnerReport.add(str);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item, listOptionsSpinnerReport);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        mSpinnerReport.setAdapter(adapter);
        mSpinnerReport.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    public interface OnClickDangKy {
        void onClick(String codeReason);
    }
}
