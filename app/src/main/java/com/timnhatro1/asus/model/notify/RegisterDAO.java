package com.timnhatro1.asus.interactor.model.notify;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface RegisterDAO {
    @Insert
    public void insert(RegisterNotify... registerNotifies);

    @Update
    public void update(RegisterNotify... registerNotifies);

    @Delete
    public void delete(RegisterNotify contact);

    @Query("SELECT * FROM register_notify")
    public List<RegisterNotify> getAllRegisterNotify();

    @Query("SELECT * FROM register_notify WHERE codeArea = :codeArea")
    public RegisterNotify getRegisterNotifyById(String codeArea);
}
