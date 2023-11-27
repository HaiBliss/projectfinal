package com.timnhatro1.asus.contract;

import android.content.Context;

import com.timnhatro1.asus.interactor.model.notify.RegisterNotify;
import com.gemvietnam.base.viper.interfaces.IInteractor;
import com.gemvietnam.base.viper.interfaces.IPresenter;
import com.gemvietnam.base.viper.interfaces.PresentView;

import java.util.List;

/**
 * The RegisterNotify Contract
 */
public interface RegisterNotifyContract {

    interface Interactor extends IInteractor<Presenter> {
        List<RegisterNotify> getListRegister(Context context);
    }

    interface View extends PresentView<Presenter> {
    }

    interface Presenter extends IPresenter<View, Interactor> {
        List<RegisterNotify> getListRegister();
    }
}



