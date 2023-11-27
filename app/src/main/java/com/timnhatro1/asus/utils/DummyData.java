package com.timnhatro1.asus.utils;

import android.content.Context;

import com.timnhatro1.asus.model.LocationModel;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class DummyData {

    public static List<String> getListLocationCity() {
        List<String> listCity = new ArrayList<>();
        for (int i = 1;i<65;i++) {
            listCity.add("Hanoi "+i);
        }
        return listCity;
    }
    public static List<String> getListLocationQuanHuyen() {
        List<String> listCity = new ArrayList<>();
        for (int i = 1;i<20;i++) {
            listCity.add("Bach Khoa "+i);
        }
        return listCity;
    }

//    public static List<MotelModel> getListMotelSaved() {
//        List<MotelModel> list = new ArrayList<>();
//        for (int i = 1;i<20;i++) {
//            MotelModel model = new MotelModel();
//            model.setId("/quan-bac-tu-liem/thue-phong-tro/51612159.htm");
//            model.setTitle("Item "+ i);
//            model.setPrice("1.800.000 đ/tháng");
//            model.setSpace("24 m²");
//            model.setTimePost("1551109311352");
//            model.setDescription("- Phòng trọ khép kín rộng rãi, gần đh mỏ và tài chính.<br>- Giờ giấc tự do, không chung chủ<br>- Điện 4k/kwh, nước 25k/m3<br>- Phù hợp với hộ gia đình hoặc ở theo nhóm");
//            model.setPhone("0353377709");
//            List<String> listPicture = new ArrayList<>();
//            listPicture.add("https://static.chotot.com.vn/1/images/3QDrXECN27wBM8vMpmRiTBogLn2cWysvkihwdpn9DAmTrFhE1NYQg8qCf5fyaqbxsjXRnfq.Ch8dvHEuv4GyMZmZg459K3xVwjig14qZ9zRicWHin9gc");
//            listPicture.add("https://static.chotot.com.vn/1/images/3QDrXECN27wBM8sSWvoaJuWSTTzoUFwn8mPXz6bthVVEfAySFCFLrJ2FeVapiuZbL2a3uuh.4tZp1M6XR2ZdryGXADx5cFFnMs9a4AEUjfLs4s9dRoPV");
//            listPicture.add("https://static.chotot.com.vn/1/images/3QDrXECN27wBM8rUokcjeYiQCPenfKQHkhYW1MnjQLLLyoyhg6J11qpAE2NVZF5UMbLxs2w.3XfgJHVQ5QaxnWMWnFyz7cad5MG43sCsnWzg7Nqc66KQ");
//            model.setListPicture(listPicture);
//            model.setAddress("Phường Cổ Nhuế 2, Quận Bắc Từ Liêm, Hà Nội");
//            model.setNameUserPost("Khoa Do");
//            model.setSave(true);
//            if (i%3==0) {
//                model.setNote("note gi gi do");
//            }
//            list.add(model);
//        }
//        return list;
//    }
//    public static MotelModel getMotelModel() {
//        MotelModel model = new MotelModel();
//        model.setId("/quan-bac-tu-liem/thue-phong-tro/51612159.htm");
//        model.setTitle("Phòng trọ khép kín không chung chủ ngõ 305 Cổ Nhuế");
//        model.setPrice("1.800.000 đ/tháng");
//        model.setSpace("24 m²");
//        model.setTimePost("1551109311352");
//        model.setDescription("- Phòng trọ khép kín rộng rãi, gần đh mỏ và tài chính.<br>- Giờ giấc tự do, không chung chủ<br>- Điện 4k/kwh, nước 25k/m3<br>- Phù hợp với hộ gia đình hoặc ở theo nhóm");
//        model.setPhone("0353377709");
//        List<String> listPicture = new ArrayList<>();
//        listPicture.add("https://static.chotot.com.vn/1/images/3QDrXECN27wBM8vMpmRiTBogLn2cWysvkihwdpn9DAmTrFhE1NYQg8qCf5fyaqbxsjXRnfq.Ch8dvHEuv4GyMZmZg459K3xVwjig14qZ9zRicWHin9gc");
//        listPicture.add("https://static.chotot.com.vn/1/images/3QDrXECN27wBM8sSWvoaJuWSTTzoUFwn8mPXz6bthVVEfAySFCFLrJ2FeVapiuZbL2a3uuh.4tZp1M6XR2ZdryGXADx5cFFnMs9a4AEUjfLs4s9dRoPV");
//        listPicture.add("https://static.chotot.com.vn/1/images/3QDrXECN27wBM8rUokcjeYiQCPenfKQHkhYW1MnjQLLLyoyhg6J11qpAE2NVZF5UMbLxs2w.3XfgJHVQ5QaxnWMWnFyz7cad5MG43sCsnWzg7Nqc66KQ");
//        model.setListPicture(listPicture);
//        model.setAddress("Phường Cổ Nhuế 2, Quận Bắc Từ Liêm, Hà Nội");
//        model.setNameUserPost("Khoa Do");
//        return model;
//    }
//    public static List<MotelModel> getListMotelModel() {
//        List<MotelModel> list = new ArrayList<>();
//        for (int i = 1;i<mRandom.nextInt((10 - 3) + 1) + 3;i++) {
//            MotelModel model = new MotelModel();
//            model.setId("/quan-bac-tu-liem/thue-phong-tro/51612159.htm"+i);
//            model.setTitle("Item "+ i);
//            model.setPrice(i+"tr");
//            model.setSpace("24 m²");
//            model.setTimePost("1551109311352");
//            model.setNameUserPost("khoa do");
//            model.setDescription("- Phòng trọ khép kín rộng rãi, gần đh mỏ và tài chính.<br>- Giờ giấc tự do, không chung chủ<br>- Điện 4k/kwh, nước 25k/m3<br>- Phù hợp với hộ gia đình hoặc ở theo nhóm");
//            model.setPhone("0353377709");
//            List<String> listPicture = new ArrayList<>();
//            listPicture.add("https://static.chotot.com.vn/1/images/3QDrXECN27wBM8sSWvoaJuWSTTzoUFwn8mPXz6bthVVEfAySFCFLrJ2FeVapiuZbL2a3uuh.4tZp1M6XR2ZdryGXADx5cFFnMs9a4AEUjfLs4s9dRoPV");
//            listPicture.add("https://static.chotot.com.vn/1/images/3QDrXECN27wBM8vMpmRiTBogLn2cWysvkihwdpn9DAmTrFhE1NYQg8qCf5fyaqbxsjXRnfq.Ch8dvHEuv4GyMZmZg459K3xVwjig14qZ9zRicWHin9gc");
//            listPicture.add("https://static.chotot.com.vn/1/images/3QDrXECN27wBM8rUokcjeYiQCPenfKQHkhYW1MnjQLLLyoyhg6J11qpAE2NVZF5UMbLxs2w.3XfgJHVQ5QaxnWMWnFyz7cad5MG43sCsnWzg7Nqc66KQ");
//            model.setListPicture(listPicture);
//            model.setAddress("Phường Cổ Nhuế 2, Quận Bắc Từ Liêm, Hà Nội");
//            model.setNameUserPost("Khoa Do");
//            model.setLat(position().latitude);
//            model.setLng(position().longitude);
//            list.add(model);
//        }
//        return list;
//    }

    private static LatLng position() {
        return new LatLng(random(20.9492222,21.0034626), random(105.8019526,105.812076));
    }

    private static double random(double min, double max) {
        return mRandom.nextDouble() * (max - min) + min;
    }
    private static Random mRandom = new Random();


    public static LocationModel initDummyData(Context context) {
        BufferedReader reader = null;
        StringBuilder stringBuilder = new StringBuilder();
        String jsonStringData;
        try {
            reader = new BufferedReader(new InputStreamReader(context.getAssets().open("mapdata.json")));
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException ignored) {
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ignored) {
                }
            }
        }
        jsonStringData = stringBuilder.toString();
        LocationModel locationModel = new Gson().fromJson(jsonStringData, LocationModel.class);
        return locationModel;
    }
    public static HashMap<String,String> initReport() {
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("Nhà trọ đã có người thuê","reason_1");
        hashMap.put("Số điện thoại không chính xác","reason_2");
        hashMap.put("Thông tin nhà trọ sai lệch","reason_3");
        return hashMap;
    }

}
