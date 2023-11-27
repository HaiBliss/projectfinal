package com.timnhatro1.asus.model.model_result_api;

import com.google.gson.annotations.SerializedName;

public class SimpleResult {
    @SerializedName("statusCode")
    Integer errorCode = -100;

    @SerializedName("message")
    String message  ="";

    @Override
    public String toString() {
        return "SimpleResult{" +
                "errorCode=" + errorCode +
                ", message='" + message + '\'' +
                '}';
    }

    public Integer getErrorCode() {
        return errorCode == null ? 1 : errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message == null ? "" : message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
