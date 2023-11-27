package com.timnhatro1.asus.contract;

import com.timnhatro1.asus.interactor.model.model_map.MotelResult;
import com.timnhatro1.asus.interactor.model.motel.MotelModel;
import com.timnhatro1.asus.connect_database.connect_server.CommonCallBack;
import com.gemvietnam.base.viper.interfaces.IInteractor;
import com.gemvietnam.base.viper.interfaces.IPresenter;
import com.gemvietnam.base.viper.interfaces.PresentView;

import java.util.List;

/**
 * The Home Contract
 */
public interface MapContract {

    interface Interactor extends IInteractor<Presenter> {
        void getListMotel(double latitude, double longitude, float defaultRadius, CommonCallBack<MotelResult> callback);

        void searchNearMe(double latitude, double longitude, float radius, String minPrice, String maxPrice, String minSpace, String maxSpace, String time, CommonCallBack<MotelResult> commonCallBack);

        void searchQuanHuyen(double lat, double lng, String codeQuanHuyen, String minPrice, String maxPrice, String minSpace, String maxSpace, String time, CommonCallBack<MotelResult> commonCallBack);

    }

    interface View extends PresentView<Presenter> {
        void onGetListMotelSuccess(List<MotelModel> listMotelModel);

        void onGetListMotelFail();
    }

    interface Presenter extends IPresenter<View, Interactor> {
        void getListMotel(double latitude, double longitude, float defaultRadius);

        void searchNearMe(double latitude, double longitude, float radius, String minPrice, String maxPrice, String minSpace, String maxSpace, String time);

        void searchArea(double lat, double lng, float radius, String minPrice, String maxPrice, String minSpace, String maxSpace, String time);

        void searchQuanHuyen(double lat, double lng, String minPrice, String maxPrice, String minSpace, String maxSpace, String time, String codeQuanHuyen);
    }
}




