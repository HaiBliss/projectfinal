package com.timnhatro1.asus.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;


import com.timnhatro1.asus.model.LocationModel;
import com.timnhatro1.asus.utils.DummyData;
import com.timnhatro1.asus.view.activity.R;

import java.util.ArrayList;
import java.util.List;

public class DialogRegister extends Dialog {
    private Spinner mSpinnerTinhThanhPho;
    private Spinner mSpinnerQuanHuyen;
    private View mLlApply;
    private LocationModel locationModel;
    private Context context;
    private int indexQuanHuyen;
    private List<LocationModel.ListProvince> provinceChoose;
    private OnClickDangKy onClickDangKy;

    public DialogRegister(@NonNull Context context, OnClickDangKy onClickDangKy) {
        super(context, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
        setContentView(R.layout.dialog_register);
        mSpinnerQuanHuyen = findViewById(R.id.spinner_quan_huyen);
        mSpinnerTinhThanhPho = findViewById(R.id.spinner_tinh_thanh_pho);
        mLlApply = findViewById(R.id.ll_apply);
        locationModel = DummyData.initDummyData(getContext());
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
                    onClickDangKy.onClick(provinceChoose.get(indexQuanHuyen).getCode(),
                            mSpinnerQuanHuyen.getSelectedItem().toString() + " " + mSpinnerTinhThanhPho.getSelectedItem().toString());
                }
            }
        });
    }

    private boolean validate() {
        if (mSpinnerTinhThanhPho.getSelectedItem().toString().equals("---")) {
            Toast.makeText(getContext(), "Cần chọn Tỉnh/TP đăng ký", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (mSpinnerQuanHuyen.getSelectedItem().toString().equals("---")) {
            Toast.makeText(getContext(), "Cần chọn Quận/Huyện đăng ký", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void setupView() {
        List<String> listOptionsSpinnerTinhThanhPho = new ArrayList<>();
        listOptionsSpinnerTinhThanhPho.add("---");
        for (LocationModel.Data tinhThanhPho : locationModel.getData()) {
            listOptionsSpinnerTinhThanhPho.add(tinhThanhPho.getNameCity());
        }
        ArrayAdapter<String> adapterTinhThanhPho = new ArrayAdapter(context, android.R.layout.simple_spinner_item, listOptionsSpinnerTinhThanhPho);
        adapterTinhThanhPho.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        mSpinnerTinhThanhPho.setAdapter(adapterTinhThanhPho);
        mSpinnerTinhThanhPho.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                setupSpinnerQuanHuyen(mSpinnerTinhThanhPho.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setupSpinnerQuanHuyen(String tinhThanhPho) {
        List<String> listOptionsSpinnerQuanHuyen = new ArrayList<>();
        for (LocationModel.Data data : locationModel.getData()) {
            if (data.getNameCity().equals(tinhThanhPho)) {
                for (LocationModel.ListProvince str : data.getListProvince()) {
                    listOptionsSpinnerQuanHuyen.add(str.getNameProvince());
                }
                provinceChoose = data.getListProvince();
                break;
            }
        }
        if (listOptionsSpinnerQuanHuyen.size() < 1)
            listOptionsSpinnerQuanHuyen.add("---");
        ArrayAdapter<String> adapterQuanHuyen = new ArrayAdapter(context, android.R.layout.simple_spinner_item, listOptionsSpinnerQuanHuyen);
        adapterQuanHuyen.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        mSpinnerQuanHuyen.setAdapter(adapterQuanHuyen);
        mSpinnerQuanHuyen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(getActivity(), provinceChoose.get(i).getCode(), Toast.LENGTH_SHORT).show();
                indexQuanHuyen = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public interface OnClickDangKy {
        void onClick(String codeArea, String nameArea);
    }
}
