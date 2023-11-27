package com.timnhatro1.asus.model.event_bus;

import android.graphics.Bitmap;

import java.util.List;

public class EventBusChoosePicture {
    private Bitmap bitmap;
    private List<Bitmap> bitmapList;

    public EventBusChoosePicture(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
    public EventBusChoosePicture(List<Bitmap> bitmapList) {
        this.bitmapList = bitmapList;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public List<Bitmap> getBitmapList() {
        return bitmapList;
    }
}
