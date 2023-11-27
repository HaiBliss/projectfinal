package com.timnhatro1.asus.contract;

import android.content.Context;

import com.timnhatro1.asus.interactor.model.motel.MotelModel;
import com.gemvietnam.base.viper.interfaces.IInteractor;
import com.gemvietnam.base.viper.interfaces.IPresenter;
import com.gemvietnam.base.viper.interfaces.PresentView;

import java.util.List;

/**
 * The ListMotelSaved Contract
 */
public interface ListMotelSavedContract {

    interface Interactor extends IInteractor<Presenter> {
        List<MotelModel> getListMotelSaved(Context context);
    }

    interface View extends PresentView<Presenter> {
        void onGetListItemSavedSuccess(List<MotelModel> listMotelSaved);
    }

    interface Presenter extends IPresenter<View, Interactor> {
        void getListItemSaved();
    }
}




