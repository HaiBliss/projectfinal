package com.timnhatro1.asus.connect_database.connect_server;

import com.timnhatro1.asus.interactor.model.model_map.MotelResult;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.timnhatro1.asus.model.model_result_api.LoginResult;
import com.timnhatro1.asus.model.model_result_api.SimpleResult;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkController {
    private static volatile HouseAPI apiBuilder;
    private static final String BASE_URL = "http://192.168.51.161:8886/";

    public static Gson getGson() {
        return new GsonBuilder()
                .setLenient()
                .create();
    }

    public static OkHttpClient okHttpClient(){
        try {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            final OkHttpClient okHttpClient1 = new OkHttpClient().newBuilder()
                 .addInterceptor(loggingInterceptor)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60,TimeUnit.SECONDS)
                    .build();

            return okHttpClient1;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static HouseAPI getApiBulder() {
        if (apiBuilder == null) {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(okHttpClient())
                    .build();
            apiBuilder = retrofit.create(HouseAPI.class);
        }
        return apiBuilder;
    }

    public static void login(String email,String password, CommonCallBack<LoginResult> callback){
        getApiBulder().login(email,password).enqueue(callback);
    }
    public static void register(String email,String password,String fullName,String phone, CommonCallBack<LoginResult> callback){
        getApiBulder().register(email,password,fullName,phone).enqueue(callback);
    }


    public static void getListMotel(double latitude, double longitude, float defaultRadius, CommonCallBack<MotelResult> callback) {
        getApiBulder().getAllMotel(String.valueOf(latitude),String.valueOf(longitude),String.valueOf(defaultRadius)).enqueue(callback);
    }

    public static void searchNearMe(double latitude, double longitude, float radius, String minPrice, String maxPrice, String minSpace, String maxSpace, String time, CommonCallBack<MotelResult> commonCallBack) {
        getApiBulder().searchNearMe(String.valueOf(latitude),String.valueOf(longitude),String.valueOf(radius),minPrice,maxPrice,minSpace,maxSpace,time).enqueue(commonCallBack);
    }

    public static void searchQuanHuyen(double latitude, double longitude, String codeQuanHuyen, String minPrice, String maxPrice, String minSpace, String maxSpace, String time, CommonCallBack<MotelResult> commonCallBack) {
        getApiBulder().searchQuanHuyen(String.valueOf(latitude),String.valueOf(longitude),codeQuanHuyen,minPrice,maxPrice,minSpace,maxSpace,time).enqueue(commonCallBack);
    }

    public static void updateUser(String email, String fullName, String phone, CommonCallBack<LoginResult> commonCallBack) {
        getApiBulder().updateUser(email,fullName,phone).enqueue(commonCallBack);
    }

    public static void postMotel(String id,String email,String title, String price, String space, String phone, String address
            , String description, String listPicture, String chungChu, String choDeXe, String phoiQuanAo, String khepKin,
                                 CommonCallBack<MotelResult> commonCallBack) {
        getApiBulder().postMotel(id,email,title,price,space,phone,address,description,listPicture,chungChu,choDeXe,phoiQuanAo,khepKin).enqueue(commonCallBack);
    }

    public static void getListPost(String email, CommonCallBack<MotelResult> commonCallBack) {
        getApiBulder().getListPost(email).enqueue(commonCallBack);
    }

    public static void removeMotelPost(String email, String id, CommonCallBack<SimpleResult> commonCallBack) {
        getApiBulder().removeMotelPost(email,id).enqueue(commonCallBack);
    }
    public static void findPassword(String email,CommonCallBack<SimpleResult> commonCallBack) {
        getApiBulder().findPassword(email).enqueue(commonCallBack);
    }

    public static void changePassword(String email,String oldPassword, String newPassword, CommonCallBack<SimpleResult> commonCallBack) {
        getApiBulder().changePassword(email,oldPassword,newPassword).enqueue(commonCallBack);
    }

    public static void updateToken(String token, String os, String androidID, CommonCallBack<SimpleResult> commonCallBack) {
        getApiBulder().updateToken(token,os,androidID).enqueue(commonCallBack);
    }
    public static void reportMotel(String deciveId,String code_reason, String id_nha_tro, CommonCallBack<SimpleResult> commonCallBack) {
        getApiBulder().reportMotel(deciveId,code_reason,id_nha_tro).enqueue(commonCallBack);
    }
}
