package com.timnhatro1.asus.presenter;

import com.timnhatro1.asus.contract.ListMotelSavedContract;
import com.timnhatro1.asus.interactor.list_motel_saved.ListMotelSavedInteractor;
import com.timnhatro1.asus.interactor.model.motel.MotelModel;
import com.timnhatro1.asus.view.fragment.ListMotelSavedFragment;
import com.gemvietnam.base.viper.Presenter;

import java.util.ArrayList;
import java.util.List;

/**
 * The ListMotelSaved Presenter
 */
public class ListMotelSavedPresenter extends Presenter<ListMotelSavedContract.View, ListMotelSavedContract.Interactor>
        implements ListMotelSavedContract.Presenter {


    @Override
    public ListMotelSavedContract.View onCreateView() {
        return ListMotelSavedFragment.getInstance();
    }

    @Override
    public void start() {
        // Start getting data here
    }

    @Override
    public ListMotelSavedContract.Interactor onCreateInteractor() {
        return new ListMotelSavedInteractor(this);
    }

    @Override
    public void getListItemSaved() {
        List<MotelModel> listMotel = new ArrayList<>();
        listMotel.addAll(mInteractor.getListMotelSaved(getViewContext()));
        mView.onGetListItemSavedSuccess(listMotel);
    }

    @Override
    public void onFragmentDisplay() {
        super.onFragmentDisplay();
        getListItemSaved();
    }
}
