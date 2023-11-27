package com.timnhatro1.asus.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.timnhatro1.asus.view.fragment.function_map.NItemMotelFragment;
import com.timnhatro1.asus.interactor.model.motel.MotelModel;

import java.util.ArrayList;
import java.util.List;

public class ResultMotelViewPagerAdapter  extends FragmentStatePagerAdapter {

    private List<MotelModel> list;
    private List<NItemMotelFragment> listFragment;


    public ResultMotelViewPagerAdapter(FragmentManager fm, List<MotelModel> list) {
        super(fm);
        this.list = list;
        this.listFragment = new ArrayList<>();
    }

    public void setData(ArrayList<MotelModel> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public List<MotelModel> getData() {
        return list;
    }

    @Override
    public Fragment getItem(int position) {
        NItemMotelFragment fragment = new NItemMotelFragment();
        listFragment.add(fragment);
        fragment.setData(list.get(position));
        return fragment;
    }

    @Override
    public int getCount() {
        return list==null?0:list.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }




}