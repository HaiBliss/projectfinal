package com.timnhatro1.asus.interactor.list_motel_saved;

import android.content.Context;

import com.timnhatro1.asus.contract.ListMotelSavedContract;
import com.timnhatro1.asus.interactor.model.AppDatabase;
import com.timnhatro1.asus.interactor.model.motel.MotelDAO;
import com.timnhatro1.asus.interactor.model.motel.MotelModel;
import com.timnhatro1.asus.view.activity.R;
import com.gemvietnam.base.viper.Interactor;

import java.util.List;

/**
 * The ListMotelSaved interactor
 */
public class ListMotelSavedInteractor extends Interactor<ListMotelSavedContract.Presenter>
        implements ListMotelSavedContract.Interactor {

    public ListMotelSavedInteractor(ListMotelSavedContract.Presenter presenter) {
        super(presenter);
    }

    @Override
    public List<MotelModel> getListMotelSaved(Context context) {
        MotelDAO motelDAO = AppDatabase.getInstance(context).getMotelDAO();
        return motelDAO.getAllMotelModelSaved();
    }
}
