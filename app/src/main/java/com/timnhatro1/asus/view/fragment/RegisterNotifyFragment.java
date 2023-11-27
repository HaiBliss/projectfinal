package com.timnhatro1.asus.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Toast;

import com.timnhatro1.asus.interactor.model.AppDatabase;
import com.timnhatro1.asus.interactor.model.notify.RegisterDAO;
import com.timnhatro1.asus.view.activity.MainActivity;
import com.timnhatro1.asus.view.activity.R;
import com.timnhatro1.asus.contract.RegisterNotifyContract;
import com.timnhatro1.asus.view.dialog.DialogConfrim;
import com.timnhatro1.asus.view.dialog.DialogRegister;
import com.timnhatro1.asus.view.adapter.NotifyRegisterAdapter;
import com.timnhatro1.asus.interactor.model.notify.RegisterNotify;
import com.timnhatro1.asus.utils.DialogUtils;
import com.timnhatro1.asus.utils.Tools;
import com.gemvietnam.base.viper.ViewFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

/**
 * The RegisterNotify Fragment
 */
public class RegisterNotifyFragment extends ViewFragment<RegisterNotifyContract.Presenter> implements RegisterNotifyContract.View {

    public static RegisterNotifyFragment getInstance() {
        return new RegisterNotifyFragment();
    }

    @BindView(R.id.rv_list_dang_ky)
    RecyclerView mRvListDangKy;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;
    @BindView(R.id.fab_add)
    FloatingActionButton mFabAdd;
    @BindView(R.id.layout_no_data)
    View mLayoutNoData;
    private NotifyRegisterAdapter adapter;
    private List<RegisterNotify> registerNotifies;
    private DialogRegister dialog;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_register_notify;
    }

    @Override
    public void initLayout(Bundle savedInstanceState) {
        registerNotifies = mPresenter.getListRegister();
        Tools.setupVerticalRecyclerView(getActivity(),mRvListDangKy);
        adapter = new NotifyRegisterAdapter(getViewContext(), registerNotifies, new NotifyRegisterAdapter.OnClickDelete() {
            @Override
            public void onClickDeleteNotify(final int position, final String codeArea,String nameArea) {
                DialogConfrim dialogConfrim = new DialogConfrim(getViewContext(),"Xác nhận","Bạn có chắc chắn muốn bỏ theo dõi "+nameArea,"Đồng ý", "Huỷ bỏ", new DialogConfrim.OnClickDialogTTC() {
                    @Override
                    public void onButonYesClick() {
                        DialogUtils.showProgressDialog(getActivity());
                        FirebaseMessaging.getInstance().unsubscribeFromTopic(codeArea).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                DialogUtils.dismissProgressDialog();
                                try {
                                    RegisterDAO registerDAO = AppDatabase.getInstance(getContext()).getRegisterNotifyDAO();
                                    registerDAO.delete(registerDAO.getRegisterNotifyById(codeArea));
                                    registerNotifies.remove(position);
                                    if (registerNotifies.size() > 0) {
                                        mRvListDangKy.setVisibility(View.VISIBLE);
                                        mLayoutNoData.setVisibility(View.GONE);
                                    } else {
                                        mRvListDangKy.setVisibility(View.GONE);
                                        mLayoutNoData.setVisibility(View.VISIBLE);
                                    }
                                    adapter.notifyDataSetChanged();
                                    Toast.makeText(getViewContext(),"Huỷ theo dõi thành công",Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    Toast.makeText(getViewContext(),"Huỷ đăng ký không thành công. Vui lòng thử lại sau!",Toast.LENGTH_SHORT).show();
                                }

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                DialogUtils.dismissProgressDialog();
                                Toast.makeText(getViewContext(),"Huỷ đăng ký không thành công. Vui lòng thử lại sau!",Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                    @Override
                    public void onButtonNoClick() {

                    }

                    @Override
                    public void onButtonCloseClick() {

                    }
                });
                dialogConfrim.show();

            }

            @Override
            public void onClickItem(String codeArea) {
                if (getViewContext() instanceof MainActivity)
                     ((MainActivity)getViewContext()).backToMapFromDangKy(codeArea);
            }
        });
        mRvListDangKy.setAdapter(adapter);
        if (registerNotifies.size() > 0) {
            mRvListDangKy.setVisibility(View.VISIBLE);
            mLayoutNoData.setVisibility(View.GONE);
        } else {
            mRvListDangKy.setVisibility(View.GONE);
            mLayoutNoData.setVisibility(View.VISIBLE);
        }
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefresh.setRefreshing(false);
                registerNotifies.clear();
                registerNotifies.addAll(mPresenter.getListRegister());
                if (registerNotifies.size() > 0) {
                    mRvListDangKy.setVisibility(View.VISIBLE);
                    mLayoutNoData.setVisibility(View.GONE);
                } else {
                    mRvListDangKy.setVisibility(View.GONE);
                    mLayoutNoData.setVisibility(View.VISIBLE);
                }
                adapter.notifyDataSetChanged();
            }
        });
        mFabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new DialogRegister(getActivity(), new DialogRegister.OnClickDangKy() {
                    @Override
                    public void onClick(String codeArea, String nameArea) {
                        DialogUtils.showProgressDialog(getActivity());
                        final RegisterNotify notify = new RegisterNotify();
                        Date fromTime = new Date(Long.valueOf(System.currentTimeMillis()));
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");
                        notify.setCodeArea(codeArea);
                        notify.setNameArea(nameArea);
                        notify.setDateRegister(simpleDateFormat.format(fromTime));
                        Log.e("khoado","codeArea: "+codeArea);
                        Log.e("khoado","nameArea: "+nameArea);
                        Log.e("khoado","DateRegister: "+simpleDateFormat.format(fromTime));
                        FirebaseMessaging.getInstance().subscribeToTopic(codeArea).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                DialogUtils.dismissProgressDialog();
                                try {
                                    RegisterDAO registerDAO = AppDatabase.getInstance(getContext()).getRegisterNotifyDAO();
                                    registerDAO.insert(notify);
                                    Toast.makeText(getViewContext(),"Đăng ký thành công",Toast.LENGTH_SHORT).show();
                                    registerNotifies.add(0,notify);
                                    if (registerNotifies.size() > 0) {
                                        mRvListDangKy.setVisibility(View.VISIBLE);
                                        mLayoutNoData.setVisibility(View.GONE);
                                    } else {
                                        mRvListDangKy.setVisibility(View.GONE);
                                        mLayoutNoData.setVisibility(View.VISIBLE);
                                    }
                                    adapter.notifyDataSetChanged();
                                    dialog.dismiss();
                                } catch (Exception e) {
                                    Toast.makeText(getViewContext(),"Khu vực đã được đăng ký theo dõi trước đó",Toast.LENGTH_SHORT).show();
                                }

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                DialogUtils.dismissProgressDialog();
                                Toast.makeText(getViewContext(),"Đăng ký không thành công",Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });
                dialog.show();
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }
}
