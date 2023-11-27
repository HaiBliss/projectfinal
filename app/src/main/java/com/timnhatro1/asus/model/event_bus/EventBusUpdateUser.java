package com.timnhatro1.asus.model.event_bus;

import com.timnhatro1.asus.model.UserModel;

public class EventBusUpdateUser {
    private UserModel userModel;

    public EventBusUpdateUser(UserModel userModel) {
        this.userModel = userModel;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }
}
