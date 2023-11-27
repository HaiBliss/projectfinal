package com.timnhatro1.asus.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

public abstract class BaseFragment extends Fragment {
    public abstract int getLayoutId();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(),container,false);
        ButterKnife.bind(this,view);
        setupView(savedInstanceState);
        return view;
    }

    protected abstract void setupView(Bundle savedInstanceState);

    @Override
    public void onDestroy() {
//        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
