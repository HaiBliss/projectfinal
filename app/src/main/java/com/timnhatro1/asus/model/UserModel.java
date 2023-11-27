package com.timnhatro1.asus.model;

import com.google.gson.annotations.SerializedName;

public class UserModel  {

    @SerializedName("email")
    private String email;
    @SerializedName("fullName")
    private String fullName;
    @SerializedName("phone")
    private String phone;

    public String getEmail() {
        return email == null ? "" : email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName == null ? "" : fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone == null ? "" : phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
