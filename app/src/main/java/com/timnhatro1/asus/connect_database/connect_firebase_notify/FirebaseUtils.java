package com.timnhatro1.asus.connect_database.connect_firebase_notify;


import android.util.Log;

import com.blankj.utilcode.util.DeviceUtils;
import com.timnhatro1.asus.model.model_result_api.SimpleResult;
import com.timnhatro1.asus.connect_database.connect_server.CommonCallBack;
import com.timnhatro1.asus.connect_database.connect_server.NetworkController;

import retrofit2.Call;
import retrofit2.Response;

class FirebaseUtils {
    public static void updateTokenToServer(String token) {
        String os = "android";
        NetworkController.updateToken(token,os,DeviceUtils.getAndroidID(), new CommonCallBack<SimpleResult>() {
            @Override
            public void onCallSuccess(Call<SimpleResult> call, Response<SimpleResult> response) {
                Log.e("khoado","push token to server success");
            }

            @Override
            public void onCallFailure(Call<SimpleResult> call) {
                Log.e("khoado","push token to server fail");
            }
        });
    }
}
