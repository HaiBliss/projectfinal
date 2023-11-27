package com.timnhatro1.asus.view.dialog.fragment_dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.timnhatro1.asus.view.activity.R;
import com.timnhatro1.asus.interactor.model.motel.MotelModel;
import com.timnhatro1.asus.view.adapter.ListMotelAdapter;
import com.timnhatro1.asus.view.dialog.bottom_sheet_dialog.MotelDetailBottomSheetFragment;
import com.timnhatro1.asus.view.dialog.DialogFilterWithSeekbar;
import com.timnhatro1.asus.utils.Tools;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ListMotelFragmentDialog extends DialogFragment {

    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;
    @BindView(R.id.rv_list_item_motel)
    RecyclerView mRvListItemMotel;

    private ListMotelAdapter listMotelAdapter;
    private List<MotelModel> listMotel;

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {

        // the content
        final RelativeLayout root = new RelativeLayout(getActivity());
        root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        // creating the fullscreen dialog
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(root);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_motel,container,false);
        ButterKnife.bind(this,view);
        setupView();
        return view;
    }

    private void setupView() {
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(getActivity(),"refresh",Toast.LENGTH_SHORT).show();
                mSwipeRefresh.setRefreshing(false);
            }
        });
        Tools.setupVerticalRecyclerView(getActivity(),mRvListItemMotel);
//        listMotel = DummyData.getListMotelModel();
        listMotel = new ArrayList<>();
        listMotelAdapter = new ListMotelAdapter(listMotel, getActivity(), new ListMotelAdapter.OnClickItemListener() {
            @Override
            public void onClickItem(int position) {
                MotelDetailBottomSheetFragment fragment = new MotelDetailBottomSheetFragment();
                fragment.setmMotelModel(listMotel.get(position));
                fragment.show(getActivity().getSupportFragmentManager(), fragment.getTag());
            }
        });
        mRvListItemMotel.setAdapter(listMotelAdapter);
    }

    @OnClick({R.id.rl_filter_price,R.id.iv_back,R.id.rl_filter_space})
    void onClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                dismiss();
                break;
            case R.id.rl_filter_price:
//                Toast.makeText(getActivity(),"click filter price",Toast.LENGTH_SHORT).show();
                DialogFilterWithSeekbar dialog = new DialogFilterWithSeekbar(getActivity(), "Chọn mức giá", "Mức giá từ:", 0, 19, "triệu", new DialogFilterWithSeekbar.OnFilterListener() {
                    @Override
                    public void onClickApply(double min, double max,int filter) {
                        Toast.makeText(getActivity(),"" + min + " " + max+ " " + filter,Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.show();
                break;
            case R.id.rl_filter_space:
//                Toast.makeText(getActivity(),"click filter space",Toast.LENGTH_SHORT).show();
                DialogFilterWithSeekbar dialogSpace = new DialogFilterWithSeekbar(getActivity(), "Chọn diện tích", "Diện tích từ:", 0, 100, "m2", new DialogFilterWithSeekbar.OnFilterListener() {
                    @Override
                    public void onClickApply(double min, double max,int filter) {
                        Toast.makeText(getActivity(),"" + min + " " + max + " " + filter,Toast.LENGTH_SHORT).show();
                    }
                });
                dialogSpace.show();
                break;
            case R.id.rl_filter_more:
                Toast.makeText(getActivity(),"click filter more",Toast.LENGTH_SHORT).show();
                break;
        }
    }


}
