package com.timnhatro1.asus.presenter;

import android.widget.Toast;


import com.timnhatro1.asus.contract.ListMotelPostContract;
import com.timnhatro1.asus.interactor.list_motel_post.ListMotelPostInteractor;
import com.timnhatro1.asus.interactor.model.model_map.MotelResult;
import com.timnhatro1.asus.interactor.model.AppDatabase;
import com.timnhatro1.asus.interactor.model.motel.MotelDAO;
import com.timnhatro1.asus.view.activity.R;
import com.timnhatro1.asus.interactor.model.motel.MotelModel;
import com.timnhatro1.asus.connect_database.connect_server.CommonCallBack;
import com.timnhatro1.asus.utils.DialogUtils;
import com.timnhatro1.asus.view.fragment.ListMotelPostFragment;
import com.gemvietnam.base.viper.Presenter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * The ListMotelPost Presenter
 */
public class ListMotelPostPresenter extends Presenter<ListMotelPostContract.View, ListMotelPostContract.Interactor>
        implements ListMotelPostContract.Presenter {



    @Override
    public ListMotelPostContract.View onCreateView() {
        return ListMotelPostFragment.getInstance();
    }

    @Override
    public void start() {
        // Start getting data here
    }

    @Override
    public ListMotelPostContract.Interactor onCreateInteractor() {
        return new ListMotelPostInteractor(this);
    }

    @Override
    public void getListMotelSaved(String email) {
//        mView.onGetListMotelSavedSuccess(DummyData.getListMotelModel());
        DialogUtils.showProgressDialog(getViewContext());
        mInteractor.getListPost(email, new CommonCallBack<MotelResult>(getViewContext(),false) {
            @Override
            public void onCallSuccess(Call<MotelResult> call, Response<MotelResult> response) {
                DialogUtils.dismissProgressDialog();
                if (response.body().getErrorCode() == 0 && response.body().getData()!=null) {
                    MotelDAO motelDAO = AppDatabase.getInstance(getViewContext()).getMotelDAO();
                    List<MotelModel> list = new ArrayList<>();
                    List<MotelModel> listSave = new ArrayList<>();
                    listSave.addAll(motelDAO.getAllMotelModelSaved());
                    list.addAll(response.body().getData());
                    for (MotelModel model : list) {
                        for (MotelModel modelSave : listSave) {
                            if (model.getId().equals(modelSave.getId())) {
                                model.setSave(true);
                                break;
                            }
                        }
                    }
                    mView.onGetListMotelSavedSuccess(list);
                } else {
                    Toast.makeText(getViewContext(), response.body().getMessage(),Toast.LENGTH_SHORT).show();
                    mView.onGetListMotelFail();
                }
            }

            @Override
            public void onCallFailure(Call<MotelResult> call) {
                DialogUtils.dismissProgressDialog();
                Toast.makeText(getViewContext(), R.string.error_fail_default,Toast.LENGTH_SHORT).show();
                mView.onGetListMotelFail();
            }
        });
    }
}
