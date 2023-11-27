package com.timnhatro1.asus.model;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class DataConverter {

    @TypeConverter
    public String fromListPictureList(List<String> countryLang) {
        if (countryLang == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>() {}.getType();
        String json = gson.toJson(countryLang, type);
        return json;
    }

    @TypeConverter
    public List<String> toListPictureList(String countryLangString) {
        if (countryLangString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>() {}.getType();
        List<String> countryLangList = gson.fromJson(countryLangString, type);
        return countryLangList;
    }
}
