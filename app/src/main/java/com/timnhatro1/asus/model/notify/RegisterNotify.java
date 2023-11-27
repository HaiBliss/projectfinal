package com.timnhatro1.asus.interactor.model.notify;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "register_notify")
public class RegisterNotify {
    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "codeArea")
    private String codeArea;
    @ColumnInfo(name = "nameArea")
    private String nameArea;
    @ColumnInfo(name = "dateRegister")
    private String dateRegister;

    public String getCodeArea() {
        return codeArea;
    }

    public void setCodeArea(String codeArea) {
        this.codeArea = codeArea;
    }

    public String getNameArea() {
        return nameArea;
    }

    public void setNameArea(String nameArea) {
        this.nameArea = nameArea;
    }

    public String getDateRegister() {
        return dateRegister;
    }

    public void setDateRegister(String dateRegister) {
        this.dateRegister = dateRegister;
    }
}
