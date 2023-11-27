package com.timnhatro1.asus.connect_database.connect_server;

import android.content.Context;
import android.util.Log;


import com.timnhatro1.asus.view.activity.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract  class CommonCallBack <T> implements Callback<T> {

    private Context mContext;
    private boolean isInternalErrorDisplayed = true;

    public abstract void onCallSuccess(Call<T> call, Response<T> response);

    public abstract void onCallFailure(Call<T> call);

    public CommonCallBack() {
        mContext = null;
        this.isInternalErrorDisplayed = false;
    }

    public CommonCallBack(boolean isInternalErrorDisplayed) {
        this.isInternalErrorDisplayed = isInternalErrorDisplayed;
    }

    public CommonCallBack(Context context) {
        mContext = context;
        this.isInternalErrorDisplayed = true;
    }

    public CommonCallBack(Context context, boolean isInternalErrorDisplayed) {
        mContext = context;
        this.isInternalErrorDisplayed = isInternalErrorDisplayed;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (mContext == null)
            return;
        if (response != null && response.code() >= 200 && response.code() < 300 && response.body() != null) {
            onCallSuccess(call, response);
        } else if (response != null && (response.code() < 200 || response.code() >= 300)) {
            if (mContext != null && isInternalErrorDisplayed) {
                android.widget.Toast.makeText(mContext, R.string.error_system_upgrading, android.widget.Toast.LENGTH_LONG).show();
            }
            this.onCallFailure(call);
        } else {
            if (mContext != null && this.isInternalErrorDisplayed) {
                android.widget.Toast.makeText(mContext, R.string.error_fail_default, android.widget.Toast.LENGTH_LONG).show();
            }
            this.onCallFailure(call);
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        if (mContext != null && this.isInternalErrorDisplayed) {
            android.widget.Toast.makeText(mContext, R.string.error_fail_default, android.widget.Toast.LENGTH_LONG).show();
        }
        Log.e("LOG: ", t.toString());
        this.onCallFailure(call);
    }

    public void setShowErrorMessage(boolean showErrorMessage) {
        isInternalErrorDisplayed = showErrorMessage;
    }
}

