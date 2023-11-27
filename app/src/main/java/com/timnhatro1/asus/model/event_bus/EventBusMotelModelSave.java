package com.timnhatro1.asus.model.event_bus;

import com.timnhatro1.asus.interactor.model.motel.MotelModel;

public class EventBusMotelModelSave {
    private MotelModel model;

    public EventBusMotelModelSave(MotelModel model) {
        this.model = model;
    }

    public MotelModel getModel() {
        return model;
    }
}
