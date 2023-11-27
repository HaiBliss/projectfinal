package com.timnhatro1.asus.presenter;

import android.widget.Toast;

import com.timnhatro1.asus.interactor.model.AppDatabase;
import com.timnhatro1.asus.view.activity.R;
import com.timnhatro1.asus.contract.MapContract;
import com.timnhatro1.asus.interactor.map.MapInteractor;
import com.timnhatro1.asus.interactor.model.model_map.MotelResult;
import com.timnhatro1.asus.interactor.model.motel.MotelDAO;
import com.timnhatro1.asus.interactor.model.motel.MotelModel;
import com.timnhatro1.asus.connect_database.connect_server.CommonCallBack;
import com.timnhatro1.asus.utils.DialogUtils;
import com.timnhatro1.asus.utils.NetworkUtils;
import com.timnhatro1.asus.view.fragment.function_map.MapFragment;
import com.gemvietnam.base.viper.Presenter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * The Home Presenter
 */
public class MapPresenter extends Presenter<MapContract.View, MapContract.Interactor>
        implements MapContract.Presenter {

    private String latNotify,lngNotify;

    @Override
    public MapContract.View onCreateView() {
        return MapFragment.getInstance();
    }

    @Override
    public void start() {
        // Start getting data here
    }

    @Override
    public MapContract.Interactor onCreateInteractor() {
        return new MapInteractor(this);
    }

    @Override
    public void getListMotel(double latitude, double longitude, float defaultRadius) {
        if (!NetworkUtils.isConnect(getViewContext())) {
            Toast.makeText(getViewContext(), R.string.msg_network_not_available,Toast.LENGTH_SHORT).show();
            return;
        }
        DialogUtils.showProgressDialog(getViewContext());
        mInteractor.getListMotel(latitude, longitude, defaultRadius, new CommonCallBack<MotelResult>(getViewContext(),false) {
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
                    mView.onGetListMotelSuccess(list);
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

    @Override
    public void searchNearMe(double latitude, double longitude, float radius, String minPrice, String maxPrice, String minSpace, String maxSpace, String time) {
        if (!NetworkUtils.isConnect(getViewContext())) {
            Toast.makeText(getViewContext(), R.string.msg_network_not_available,Toast.LENGTH_SHORT).show();
            return;
        }
        DialogUtils.showProgressDialog(getViewContext());
        mInteractor.searchNearMe(latitude, longitude, radius,minPrice,maxPrice,minSpace,maxSpace,time, new CommonCallBack<MotelResult>(getViewContext(),false) {
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
                    mView.onGetListMotelSuccess(list);
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

    @Override
    public void searchArea(double lat, double lng, float radius, String minPrice, String maxPrice, String minSpace, String maxSpace, String time) {
        if (!NetworkUtils.isConnect(getViewContext())) {
            Toast.makeText(getViewContext(), R.string.msg_network_not_available,Toast.LENGTH_SHORT).show();
            return;
        }
        DialogUtils.showProgressDialog(getViewContext());
        mInteractor.searchNearMe(lat, lng, radius,minPrice,maxPrice,minSpace,maxSpace,time, new CommonCallBack<MotelResult>(getViewContext(),false) {
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
                    mView.onGetListMotelSuccess(list);
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

    @Override
    public void searchQuanHuyen(double lat, double lng, String minPrice, String maxPrice, String minSpace, String maxSpace, String time, String codeQuanHuyen) {
        if (!NetworkUtils.isConnect(getViewContext())) {
            Toast.makeText(getViewContext(), R.string.msg_network_not_available,Toast.LENGTH_SHORT).show();
            return;
        }
        DialogUtils.showProgressDialog(getViewContext());
        mInteractor.searchQuanHuyen(lat, lng, codeQuanHuyen,minPrice,maxPrice,minSpace,maxSpace,time, new CommonCallBack<MotelResult>(getViewContext(),false) {
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
                    mView.onGetListMotelSuccess(list);
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
