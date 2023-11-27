package com.timnhatro1.asus.presenter;

import com.timnhatro1.asus.contract.RegisterNotifyContract;
import com.timnhatro1.asus.interactor.register_notify.RegisterNotifyInteractor;
import com.timnhatro1.asus.interactor.model.notify.RegisterNotify;
import com.timnhatro1.asus.view.fragment.RegisterNotifyFragment;
import com.gemvietnam.base.viper.Presenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The RegisterNotify Presenter
 */
public class RegisterNotifyPresenter extends Presenter<RegisterNotifyContract.View, RegisterNotifyContract.Interactor>
        implements RegisterNotifyContract.Presenter {

    @Override
    public RegisterNotifyContract.View onCreateView() {
        return RegisterNotifyFragment.getInstance();
    }

    @Override
    public void start() {
        // Start getting data here
    }

    @Override
    public RegisterNotifyContract.Interactor onCreateInteractor() {
        return new RegisterNotifyInteractor(this);
    }

    @Override
    public List<RegisterNotify> getListRegister() {
        List<RegisterNotify> list = new ArrayList<>();
        list.addAll(mInteractor.getListRegister(getViewContext()));
        Collections.reverse(list);
        return list;
    }
}
