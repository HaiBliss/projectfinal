package com.timnhatro1.asus.interactor.map;

import com.timnhatro1.asus.contract.MapContract;
import com.timnhatro1.asus.interactor.model.model_map.MotelResult;
import com.timnhatro1.asus.connect_database.connect_server.CommonCallBack;
import com.timnhatro1.asus.connect_database.connect_server.NetworkController;
import com.gemvietnam.base.viper.Interactor;

/**
 * The Home interactor
 */
public class MapInteractor extends Interactor<MapContract.Presenter>
        implements MapContract.Interactor {

    public MapInteractor(MapContract.Presenter presenter) {
        super(presenter);
    }

    @Override
    public void getListMotel(double latitude, double longitude, float defaultRadius, CommonCallBack<MotelResult> callback) {
        NetworkController.getListMotel(latitude,longitude,defaultRadius,callback);
    }

    @Override
    public void searchNearMe(double latitude, double longitude, float radius, String minPrice, String maxPrice, String minSpace, String maxSpace, String time, CommonCallBack<MotelResult> commonCallBack) {
        NetworkController.searchNearMe(latitude,longitude,radius,minPrice,maxPrice,minSpace,maxSpace,time,commonCallBack);
    }

    @Override
    public void searchQuanHuyen(double lat, double lng, String codeQuanHuyen, String minPrice, String maxPrice, String minSpace, String maxSpace, String time, CommonCallBack<MotelResult> commonCallBack) {
        NetworkController.searchQuanHuyen(lat,lng,codeQuanHuyen,minPrice,maxPrice,minSpace,maxSpace,time,commonCallBack);
    }
}
