package com.timnhatro1.asus.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;

import com.timnhatro1.asus.view.activity.R;
import com.timnhatro1.asus.contract.ListMotelSavedContract;
import com.timnhatro1.asus.view.adapter.ListMotelSavedAdapter;
import com.timnhatro1.asus.view.dialog.bottom_sheet_dialog.MotelDetailBottomSheetFragment;
import com.timnhatro1.asus.model.event_bus.EventBusMotelModelSave;
import com.timnhatro1.asus.interactor.model.motel.MotelModel;
import com.timnhatro1.asus.utils.Tools;
import com.gemvietnam.base.viper.ViewFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * The ListMotelSaved Fragment
 */
public class ListMotelSavedFragment extends ViewFragment<ListMotelSavedContract.Presenter> implements ListMotelSavedContract.View {
    @BindView(R.id.rv_list_item_saved)
    RecyclerView mRvListItemSaved;

    private List<MotelModel> listMotelSaved;
    private ListMotelSavedAdapter listMotelAdapter;

    private static ListMotelSavedFragment listMotelSavedFragment;

    public static ListMotelSavedFragment getInstance() {
        return new ListMotelSavedFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_list_motel_saved;
    }

    @Override
    public void initLayout(Bundle savedInstanceState) {
        Tools.setupVerticalRecyclerView(getActivity(), mRvListItemSaved);
        listMotelSaved = new ArrayList<>();
        listMotelAdapter = new ListMotelSavedAdapter(getActivity(), listMotelSaved, new ListMotelSavedAdapter.OnClickItemListener() {
            @Override
            public void onClickItem(int position) {
                MotelDetailBottomSheetFragment fragment = new MotelDetailBottomSheetFragment();
                fragment.setmMotelModel(listMotelSaved.get(position));
                fragment.setShowStaticMap(true);
                fragment.show(getActivity().getSupportFragmentManager(), fragment.getTag());
            }
        });
        mRvListItemSaved.setAdapter(listMotelAdapter);
        mPresenter.getListItemSaved();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setHasOptionsMenu(true);

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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSaveMotelModelChange(EventBusMotelModelSave event) {
        for (MotelModel model : listMotelSaved) {
            if (model.getId().equals(event.getModel().getId())) {
                model.setSave(event.getModel().isSave());
                model.setNote(event.getModel().getNote());
                if (!model.isSave()) {
                    listMotelSaved.remove(model);
                }
                if (listMotelAdapter != null)
                    listMotelAdapter.notifyDataSetChanged();
                return;
            }
        }
        listMotelSaved.add(event.getModel());
        if (listMotelAdapter != null)
            listMotelAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGetListItemSavedSuccess(List<MotelModel> listMotelSaved) {
        this.listMotelSaved.clear();
        this.listMotelSaved.addAll(listMotelSaved);
        listMotelAdapter.notifyDataSetChanged();
    }
}
