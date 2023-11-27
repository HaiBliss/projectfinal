package com.timnhatro1.asus.model.notify;

public class NotifyFirebaseModel {

    private String title;
    private String body;
    private String lat;
    private String lng;

    public String getTitle() {
        return title == null ? "" : title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body == null ? "" : body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getLat() {
        return lat == null ? "" : lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng == null ? "" : lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }
}
