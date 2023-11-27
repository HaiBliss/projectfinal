package com.timnhatro1.asus.interactor.model;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.timnhatro1.asus.interactor.model.motel.MotelDAO;
import com.timnhatro1.asus.interactor.model.motel.MotelModel;
import com.timnhatro1.asus.interactor.model.notify.RegisterNotify;
import com.timnhatro1.asus.interactor.model.notify.RegisterDAO;

@Database(entities = {MotelModel.class,RegisterNotify.class}, version = 9)
public abstract class AppDatabase  extends RoomDatabase {
    private static AppDatabase appDatabase;

    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "Room-database";

    public abstract MotelDAO getMotelDAO();
    public abstract RegisterDAO getRegisterNotifyDAO();

    public static AppDatabase getInstance(Context context) {
        if (appDatabase == null) {
            appDatabase = Room.databaseBuilder(context, AppDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return appDatabase;
    }
}
