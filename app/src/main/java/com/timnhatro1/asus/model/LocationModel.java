package com.timnhatro1.asus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LocationModel {
    @SerializedName("data")
    @Expose
    private List<Data> data = null;

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public class Data {

        @SerializedName("name_city")
        @Expose
        private String nameCity;
        @SerializedName("list_province")
        @Expose
        private List<ListProvince> listProvince = null;

        public String getNameCity() {
            return nameCity;
        }

        public void setNameCity(String nameCity) {
            this.nameCity = nameCity;
        }

        public List<ListProvince> getListProvince() {
            return listProvince;
        }

        public void setListProvince(List<ListProvince> listProvince) {
            this.listProvince = listProvince;
        }

    }
    public class ListProvince {

        @SerializedName("name_province")
        @Expose
        private String nameProvince;
        @SerializedName("lat")
        @Expose
        private String lat;
        @SerializedName("long")
        @Expose
        private String _long;
        @SerializedName("code")
        @Expose
        private String code;

        public String getNameProvince() {
            return nameProvince;
        }

        public void setNameProvince(String nameProvince) {
            this.nameProvince = nameProvince;
        }

        public Double getLat() {
            return Double.valueOf(lat);
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public Double getLong() {
            return Double.valueOf(_long);
        }

        public void setLong(String _long) {
            this._long = _long;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }
}
