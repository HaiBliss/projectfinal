package com.timnhatro1.asus.contract;

import com.timnhatro1.asus.interactor.model.model_map.MotelResult;
import com.timnhatro1.asus.interactor.model.motel.MotelModel;
import com.timnhatro1.asus.connect_database.connect_server.CommonCallBack;
import com.gemvietnam.base.viper.interfaces.IInteractor;
import com.gemvietnam.base.viper.interfaces.IPresenter;
import com.gemvietnam.base.viper.interfaces.PresentView;

import java.util.List;

/**
 * The ListMotelPost Contract
 */
public interface ListMotelPostContract {

    interface Interactor extends IInteractor<Presenter> {
        void getListPost(String email, CommonCallBack<MotelResult> commonCallBack);
    }

    interface View extends PresentView<Presenter> {
        void onGetListMotelSavedSuccess(List<MotelModel> listMotelModel);

        void onGetListMotelFail();
    }

    interface Presenter extends IPresenter<View, Interactor> {
        void getListMotelSaved(String email);
    }
}



