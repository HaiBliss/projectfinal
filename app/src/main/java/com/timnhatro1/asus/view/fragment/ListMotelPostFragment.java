package com.timnhatro1.asus.view.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Toast;

import com.timnhatro1.asus.Constant;
import com.timnhatro1.asus.view.activity.R;
import com.timnhatro1.asus.contract.ListMotelPostContract;
import com.timnhatro1.asus.view.adapter.ListMotelAdapter;
import com.timnhatro1.asus.view.dialog.bottom_sheet_dialog.MotelDetailBottomSheetFragment;
import com.timnhatro1.asus.model.event_bus.EventBusUpdateListMotelPost;
import com.timnhatro1.asus.view.dialog.fragment_dialog.PostMotelFragmentDialog;
import com.timnhatro1.asus.model.UserModel;
import com.timnhatro1.asus.interactor.model.motel.MotelModel;
import com.timnhatro1.asus.utils.Tools;
import com.gemvietnam.base.viper.ViewFragment;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static android.content.Context.MODE_PRIVATE;

/**
 * The ListMotelPost Fragment
 */
public class ListMotelPostFragment extends ViewFragment<ListMotelPostContract.Presenter> implements ListMotelPostContract.View {
    @BindView(R.id.rv_list_item_my_post)
    RecyclerView mRvListItemMyPost;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;
    @BindView(R.id.fab_add)
    FloatingActionButton mFabAdd;
    @BindView(R.id.layout_no_data)
    View mLayoutNoData;
    @BindView(R.id.layout_error_data)
    View mLayoutErrorData;
    protected List<MotelModel> listMotelSaved;
    private ListMotelAdapter listMotelAdapter;
    private UserModel userModel;
    private SharedPreferences mPrefs;
    public static ListMotelPostFragment getInstance() {
        return new ListMotelPostFragment();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_list_motel_post;
    }

    @Override
    public void initLayout(Bundle savedInstanceState) {
        mPrefs = getActivity().getSharedPreferences(Constant.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        try {
            if (mPrefs.getString(Constant.KEY_USER_MODEL, null) != null) {
                Gson gson = new Gson();
                String json = mPrefs.getString(Constant.KEY_USER_MODEL, "");
                if (!json.isEmpty()) {
                    userModel = gson.fromJson(json, UserModel.class);
                }
//            MyObject obj = gson.fromJson(json, MyObject.class);
            }
        } catch (Exception e) {

        }
        Tools.setupVerticalRecyclerView(getActivity(),mRvListItemMyPost);
        listMotelSaved = new ArrayList<>();
        listMotelAdapter = new ListMotelAdapter(listMotelSaved, getActivity(), new ListMotelAdapter.OnClickItemListener() {
            @Override
            public void onClickItem(int position) {
                MotelDetailBottomSheetFragment fragment = new MotelDetailBottomSheetFragment();
                fragment.setmMotelModel(listMotelSaved.get(position));
                fragment.setCanEdit(true);
                fragment.setShowStaticMap(true);
                fragment.show(getActivity().getSupportFragmentManager(), fragment.getTag());
            }
        });
        mRvListItemMyPost.setAdapter(listMotelAdapter);
        if (userModel!=null) {
            mPresenter.getListMotelSaved(userModel.getEmail());
        } else {
            Toast.makeText(getViewContext(),R.string.error_fail_default,Toast.LENGTH_SHORT).show();
        }
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefresh.setRefreshing(false);
                if (userModel!=null) {
                    mPresenter.getListMotelSaved(userModel.getEmail());
                } else {
                    Toast.makeText(getViewContext(),R.string.error_fail_default,Toast.LENGTH_SHORT).show();
                }
            }
        });
        mFabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PostMotelFragmentDialog dialog = new PostMotelFragmentDialog();
                dialog.show(getFragmentManager(),dialog.getTag());
            }
        });
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }
    @OnClick({R.id.btnRetry})
    public void onClicked(View view) {
        switch (view.getId()) {
            case R.id.btnRetry:
                mPresenter.getListMotelSaved(userModel.getEmail());
                break;
        }
    }
    @Override
    public void onGetListMotelSavedSuccess(List<MotelModel> listMotelModel) {
        this.listMotelSaved.clear();
        this.listMotelSaved.addAll(listMotelModel);
        listMotelAdapter.notifyDataSetChanged();
        if (listMotelModel.size() > 0 ) {
            mRvListItemMyPost.setVisibility(View.VISIBLE);
            mLayoutErrorData.setVisibility(View.GONE);
            mLayoutNoData.setVisibility(View.GONE);
        } else {
            mRvListItemMyPost.setVisibility(View.GONE);
            mLayoutErrorData.setVisibility(View.GONE);
            mLayoutNoData.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onGetListMotelFail() {
        this.listMotelSaved.clear();
        listMotelAdapter.notifyDataSetChanged();
        mRvListItemMyPost.setVisibility(View.GONE);
        mLayoutErrorData.setVisibility(View.VISIBLE);
        mLayoutNoData.setVisibility(View.GONE);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateListPost(EventBusUpdateListMotelPost event) {
        if (event!=null) {
            if (userModel!=null) {
                mPresenter.getListMotelSaved(userModel.getEmail());
            } else {
                Toast.makeText(getViewContext(),R.string.error_fail_default,Toast.LENGTH_SHORT).show();
            }
        }
    }
}
