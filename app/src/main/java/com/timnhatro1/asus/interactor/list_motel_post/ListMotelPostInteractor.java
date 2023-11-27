package com.timnhatro1.asus.interactor.list_motel_post;

import com.timnhatro1.asus.contract.ListMotelPostContract;
import com.timnhatro1.asus.interactor.model.model_map.MotelResult;
import com.timnhatro1.asus.connect_database.connect_server.CommonCallBack;
import com.timnhatro1.asus.connect_database.connect_server.NetworkController;
import com.gemvietnam.base.viper.Interactor;

/**
 * The ListMotelPost interactor
 */
public class ListMotelPostInteractor extends Interactor<ListMotelPostContract.Presenter>
        implements ListMotelPostContract.Interactor {

    public ListMotelPostInteractor(ListMotelPostContract.Presenter presenter) {
        super(presenter);
    }

    @Override
    public void getListPost(String email, CommonCallBack<MotelResult> commonCallBack) {
        NetworkController.getListPost(email,commonCallBack);
    }
}
