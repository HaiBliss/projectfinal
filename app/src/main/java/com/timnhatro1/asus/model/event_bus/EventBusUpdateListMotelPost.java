package com.timnhatro1.asus.model.event_bus;

import com.timnhatro1.asus.interactor.model.motel.MotelModel;

public class EventBusUpdateListMotelPost {
    private MotelModel motelModel;
    public EventBusUpdateListMotelPost() {

    }

    public MotelModel getMotelModel() {
        return motelModel;
    }

    public void setMotelModel(MotelModel motelModel) {
        this.motelModel = motelModel;
    }
}
