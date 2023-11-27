package com.timnhatro1.asus.interactor.model.motel;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.text.Html;

import com.timnhatro1.asus.utils.Tools;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;
import com.google.maps.android.clustering.ClusterItem;

@Entity(tableName = "motel_model")
public class MotelModel implements ClusterItem {
    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "id")
    @SerializedName("id")
    private String id;
    @ColumnInfo(name = "title")
    @SerializedName("title")
    private String title;
    @ColumnInfo(name = "description")
    @SerializedName("description")
    private String description;
    @ColumnInfo(name = "space")
    @SerializedName("space")
    private String space;
    @ColumnInfo(name = "price")
    @SerializedName("price")
    private String price;
    @ColumnInfo(name = "timePost")
    @SerializedName("timePost")
    private String timePost;
    @ColumnInfo(name = "phone")
    @SerializedName("phone")
    private String phone;
    @ColumnInfo(name = "address")
    @SerializedName("address")
    private String address;
    @ColumnInfo(name = "lat")
    @SerializedName("lat")
    private double lat;
    @ColumnInfo(name = "lng")
    @SerializedName("lng")
    private double lng;
    @ColumnInfo(name = "listPicture")
    @SerializedName("listPicture")
    private String listPicture;
    @ColumnInfo(name = "khep_kin")
    @SerializedName("khepKin")
    private String khepKin;
    @ColumnInfo(name = "chung_chu")
    @SerializedName("chungChu")
    private String chungChu;
    @ColumnInfo(name = "cho_de_xe")
    @SerializedName("choDeXe")
    private String choDeXe;
    @ColumnInfo(name = "cho_phoi_quan_ao")
    @SerializedName("choPhoiQuanAo")
    private String choPhoiQuanAo;
    @ColumnInfo(name = "emailPost")
    @SerializedName("emailPost")
    private String emailPost;
    @ColumnInfo(name = "codeProvince")
    @SerializedName("codeProvince")
    private String codeProvince;
    @ColumnInfo(name = "sourceInternet")
    @SerializedName("sourceInternet")
    private String sourceInternet;
    @ColumnInfo(name = "nameSource")
    @SerializedName("nameSource")
    private String nameSource;
    @ColumnInfo(name = "note")
    @SerializedName("note")
    private String note;
    @ColumnInfo(name = "reason")
    @SerializedName("reason")
    private String reason;

    public String getReason() {
        return reason == null ? "" : reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    private boolean isSave = false;

//    @TypeConverters(DataConverter.class)
//    @ColumnInfo(name = "listPicture")
//    private List<String> listPicture;


    public String getListPicture() {
        return listPicture == null ? "" : listPicture;
    }

    public void setListPicture(String listPicture) {
        this.listPicture = listPicture;
    }

    public String getEmailPost() {
        return emailPost == null ? "" : emailPost;
    }

    public void setEmailPost(String emailPost) {
        this.emailPost = emailPost;
    }

    public String getCodeProvince() {
        return codeProvince == null ? "" : codeProvince;
    }

    public void setCodeProvince(String codeProvince) {
        this.codeProvince = codeProvince;
    }

    public String getSourceInternet() {
        return sourceInternet == null ? "" : sourceInternet;
    }

    public void setSourceInternet(String sourceInternet) {
        this.sourceInternet = sourceInternet;
    }

    public String getNameSource() {
        return nameSource == null ? "" : nameSource;
    }

    public void setNameSource(String nameSource) {
        this.nameSource = nameSource;
    }

    public String getKhepKin() {
        return khepKin == null ? "" : khepKin;
    }

    public void setKhepKin(String khepKin) {
        this.khepKin = khepKin;
    }

    public String getChungChu() {
        return chungChu == null ? "" : chungChu;
    }

    public void setChungChu(String chungChu) {
        this.chungChu = chungChu;
    }

    public String getChoDeXe() {
        return choDeXe == null ? "" : choDeXe;
    }

    public void setChoDeXe(String choDeXe) {
        this.choDeXe = choDeXe;
    }


    public String getChoPhoiQuanAo() {
        return choPhoiQuanAo == null ? "" : choPhoiQuanAo;
    }

    public void setChoPhoiQuanAo(String choPhoiQuanAo) {
        this.choPhoiQuanAo = choPhoiQuanAo;
    }



    public boolean isSave() {
        return isSave;
    }

    public void setSave(boolean save) {
        isSave = save;
    }



    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getNote() {
        return note == null ? "": note;
    }

    public void setNote(String note) {
        this.note = note;
    }


    public String getAddress() {
        return address == null? "" : address;
    }

    public void setAddress(String address) {
        this.address = address;
    }



//    public List<String> getListPicture() {
//        return listPicture == null ? new ArrayList<String>() : listPicture;
//    }
//
//    public void setListPicture(List<String> listPicture) {
//        this.listPicture = listPicture;
//    }

    public String getId() {
        return id == null? "" : id;
    }


    public void setId(String id) {
        this.id = id;
    }

    public String getPhone() {
        return phone == null? "" : phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public LatLng getPosition() {
        return new LatLng(lat,lng);
    }

    public String getTitle() {
        return title == null ? "" : title;
    }

    @Override
    public String getSnippet() {
        return null;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description == null ? "" : description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSpace() {
        return space == null ? "" : space;
    }

    public void setSpace(String space) {
        this.space = space;
    }

    public String getPrice() {
        return price == null ? "" : price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTimePost() {
        return timePost == null ? "" : timePost;
//        return Tools.howLongFrom(timePost);
    }

    public void setTimePost(String timePost) {
        this.timePost = timePost;
    }
    public String toString() {
        String text = title + "\n" + "Giá: "+price  + "\n" + "Diện tích: "  + space+ "\n" + "Thời gian đăng: " + Tools.howLongFrom(timePost)+
                "\n" + "Mô tả: " + Html.fromHtml(description).toString();
//        text = text + "\n" +"list picture: ";
//        for (String picture : listPicture) {
//            text = text + picture +"\n";
//        }
        return text;
    }
}
