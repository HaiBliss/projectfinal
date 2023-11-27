package com.timnhatro1.asus.view.activity;

import com.timnhatro1.asus.MyApplication;
import com.gemvietnam.base.BaseActivity;

public abstract class MyBaseActivity extends BaseActivity {
    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.onResume();
    }

    @Override
    protected void onPause() {
        MyApplication.onPause();
        super.onPause();
    }
}
