package com.timnhatro1.asus.interactor.model.motel;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface MotelDAO  {
    @Insert
    public void insert(MotelModel... motels);

    @Update
    public void update(MotelModel... motels);

    @Delete
    public void delete(MotelModel contact);

    @Query("SELECT * FROM motel_model")
    public List<MotelModel> getAllMotelModelSaved();

    @Query("SELECT * FROM motel_model WHERE id = :id")
    public MotelModel getMotelById(String id);
}
