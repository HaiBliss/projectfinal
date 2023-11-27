package com.timnhatro1.asus.connect_database.connect_server;


import com.timnhatro1.asus.interactor.model.model_map.MotelResult;
import com.timnhatro1.asus.model.model_result_api.LoginResult;
import com.timnhatro1.asus.model.model_result_api.SimpleResult;


import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface HouseAPI {

    @FormUrlEncoded
    @POST("login")
    Call<LoginResult> login(@Field("email") String email, @Field("password") String password);
    @FormUrlEncoded
    @POST("register")
    Call<LoginResult> register(@Field("email") String email, @Field("password") String password,
                               @Field("fullname") String fullName, @Field("phone") String phone);
    @FormUrlEncoded
    @POST("motel/getAllMotel")
    Call<MotelResult> getAllMotel(@Field("lat") String lat, @Field("lng") String lng,
                                  @Field("radius") String radius);

    @FormUrlEncoded
    @POST("motel/searchNearMe")
    Call<MotelResult> searchNearMe(@Field("lat") String lat, @Field("lng") String lng,
                                   @Field("radius") String radius,@Field("minPrice") String minPrice,
                                   @Field("maxPrice") String maxPrice,@Field("minSpace") String minSpace,
                                   @Field("maxSpace") String maxSpace,@Field("time") String time);
    @FormUrlEncoded
    @POST("motel/searchQuanHuyen")
    Call<MotelResult> searchQuanHuyen(@Field("lat") String lat, @Field("lng") String lng,
                                   @Field("codeQuanHuyen") String codeQuanHuyen,@Field("minPrice") String minPrice,
                                   @Field("maxPrice") String maxPrice,@Field("minSpace") String minSpace,
                                   @Field("maxSpace") String maxSpace,@Field("time") String time);
    @FormUrlEncoded
    @POST("user/upgradeUser")
    Call<LoginResult> updateUser(@Field("email") String email, @Field("fullname") String fullName, @Field("phone") String phone);
    @FormUrlEncoded
    @POST("motel/add")
    Call<MotelResult> postMotel(@Field("id") String id,@Field("email") String email, @Field("title") String title, @Field("price") String price,
                                 @Field("space") String space, @Field("phone") String phone, @Field("address") String address,
                                 @Field("description") String description, @Field("listPicture") String listPicture,
                                 @Field("chungChu") String chungChu,@Field("choDeXe") String choDeXe,
                                 @Field("choPhoiQuanAo") String choPhoiQuanAo,@Field("khepKin") String khepKin);

    @FormUrlEncoded
    @POST("motel/getListPost")
    Call<MotelResult> getListPost(@Field("email") String email);

    @FormUrlEncoded
    @POST("motel/removeMotelPost")
    Call<SimpleResult> removeMotelPost(@Field("email") String email, @Field("id")  String id);
    @FormUrlEncoded
    @POST("sendEmail")
    Call<SimpleResult> findPassword(@Field("email")String email);
    @FormUrlEncoded
    @POST("user/changePassword")
    Call<SimpleResult> changePassword(@Field("email")String email,@Field("oldPassword") String oldPassword,@Field("newPassword") String newPassword);
    @FormUrlEncoded
    @POST("notify/updateToken")
    Call<SimpleResult> updateToken(@Field("token")String token,@Field("os") String os,@Field("deviceId") String androidID);
    @FormUrlEncoded
    @POST("report/add")
    Call<SimpleResult> reportMotel(@Field("deciveId")String deciveId,@Field("code_reason") String code_reason,@Field("id_nha_tro") String id_nha_tro);
}
