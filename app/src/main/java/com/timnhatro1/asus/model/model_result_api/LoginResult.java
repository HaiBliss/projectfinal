package com.timnhatro1.asus.model.model_result_api;

import com.timnhatro1.asus.model.UserModel;
import com.google.gson.annotations.SerializedName;

public class LoginResult extends SimpleResult {
    @SerializedName("data")
    private UserModel data;


    public UserModel getData() {
        return data;
    }

    public void setData(UserModel data) {
        this.data = data;
    }

}
