package com.timnhatro1.asus.model.event_bus;

public class EventBusClickFromDangKy {
    private String lat;
    private String lng;

    public EventBusClickFromDangKy(String lat, String lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }
}
