package com.timnhatro1.asus.interactor.register_notify;

import android.content.Context;

import com.timnhatro1.asus.contract.RegisterNotifyContract;
import com.timnhatro1.asus.interactor.model.notify.RegisterDAO;
import com.timnhatro1.asus.interactor.model.notify.RegisterNotify;
import com.timnhatro1.asus.interactor.model.AppDatabase;
import com.timnhatro1.asus.view.activity.R;
import com.gemvietnam.base.viper.Interactor;

import java.util.List;

/**
 * The RegisterNotify interactor
 */
public class RegisterNotifyInteractor extends Interactor<RegisterNotifyContract.Presenter>
        implements RegisterNotifyContract.Interactor {

    public RegisterNotifyInteractor(RegisterNotifyContract.Presenter presenter) {
        super(presenter);
    }

    @Override
    public List<RegisterNotify> getListRegister(Context context) {
        RegisterDAO registerDAO = AppDatabase.getInstance(context).getRegisterNotifyDAO();
        return registerDAO.getAllRegisterNotify();
    }
}
