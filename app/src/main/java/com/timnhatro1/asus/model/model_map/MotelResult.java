package com.timnhatro1.asus.interactor.model.model_map;

import com.timnhatro1.asus.model.model_result_api.SimpleResult;
import com.timnhatro1.asus.interactor.model.motel.MotelModel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MotelResult extends SimpleResult {
    @SerializedName("data")
    private List<MotelModel> data;

    public List<MotelModel> getData() {
        return data;
    }

    public void setData(List<MotelModel> motels) {
        this.data = motels;
    }
}
