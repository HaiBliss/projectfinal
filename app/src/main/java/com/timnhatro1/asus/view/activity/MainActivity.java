package com.timnhatro1.asus.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.DeviceUtils;
import com.timnhatro1.asus.Constant;
import com.timnhatro1.asus.MyApplication;
import com.timnhatro1.asus.model.event_bus.EventBusChoosePicture;
import com.timnhatro1.asus.model.event_bus.EventBusClickFromDangKy;
import com.timnhatro1.asus.model.event_bus.EventBusNotify;
import com.timnhatro1.asus.model.event_bus.EventBusUpdateUser;
import com.timnhatro1.asus.model.model_result_api.SimpleResult;
import com.timnhatro1.asus.view.dialog.fragment_dialog.InforUserFragmentDialog;
import com.timnhatro1.asus.view.dialog.fragment_dialog.PostMotelFragmentDialog;
import com.timnhatro1.asus.presenter.ListMotelPostPresenter;
import com.timnhatro1.asus.presenter.ListMotelSavedPresenter;
import com.timnhatro1.asus.presenter.MapPresenter;
import com.timnhatro1.asus.presenter.RegisterNotifyPresenter;
import com.timnhatro1.asus.model.LocationModel;
import com.timnhatro1.asus.model.UserModel;
import com.timnhatro1.asus.connect_database.connect_server.CommonCallBack;
import com.timnhatro1.asus.connect_database.connect_server.NetworkController;
import com.timnhatro1.asus.utils.DummyData;
import com.timnhatro1.asus.utils.Tools;
import com.gemvietnam.base.BaseFragment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.takusemba.spotlight.OnSpotlightStateChangedListener;
import com.takusemba.spotlight.Spotlight;
import com.takusemba.spotlight.target.SimpleTarget;
import com.zhihu.matisse.Matisse;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import retrofit2.Call;
import retrofit2.Response;


public class MainActivity extends MyBaseActivity implements  View.OnClickListener, NavigationView.OnNavigationItemSelectedListener{


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    private ActionBar actionBar;
    private View viewHeader;
    private TextView mTvFullName;
    private TextView mTvEmail;
    private UserModel userModel;
    private SharedPreferences mPrefs;
    private Menu menu;
    private MenuItem itemMyPost;
    private MenuItem itemSignOut;
    private String latNotify,lngNotify;
    private boolean isFirstTime = true;
    private int lastSelect = R.id.nav_home;
    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Branch branch = Branch.getInstance();

        // Branch init
        branch.initSession(new Branch.BranchReferralInitListener() {
            @Override
            public void onInitFinished(JSONObject referringParams, BranchError error) {
                if (error == null) {
                    // params are the deep linked params associated with the link that the user clicked -> was re-directed to this app
                    // params will be empty if no data found
                    // ... insert custom logic here ...
                    Log.i("BRANCH SDK", referringParams.toString());
                } else {
                    Log.i("BRANCH SDK", error.getMessage());
                }
            }
        }, this.getIntent().getData(), this);

    }
    @Override
    public void onNewIntent(Intent intent) {
        this.setIntent(intent);
        if (intent.getAction().equals("click_action")) {
            Bundle bundle = new Bundle();
            latNotify = intent.getStringExtra("lat");
            lngNotify = intent.getStringExtra("lng");
            bundle.putString("lat",latNotify);
            bundle.putString("lng",lngNotify);
            BaseFragment baseFragment = (BaseFragment) new MapPresenter().getFragment();
            baseFragment.setArguments(bundle);
            actionBar.setTitle("Bản đồ");
            navigationView.getMenu().getItem(0).setChecked(true);
            addFragment(R.id.container_fragment, baseFragment,false,true);
            EventBus.getDefault().post(new EventBusNotify(latNotify,lngNotify));
        }

    }

    @Override
    public void onBackPressed() {
        back();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String os = "android";
                Log.e("khoado","FirebaseToken: "+instanceIdResult.getToken());
                NetworkController.updateToken(instanceIdResult.getToken(),os, DeviceUtils.getAndroidID(), new CommonCallBack<SimpleResult>() {
                    @Override
                    public void onCallSuccess(Call<SimpleResult> call, Response<SimpleResult> response) {
                        Log.e("khoado","push token to server success");
                    }

                    @Override
                    public void onCallFailure(Call<SimpleResult> call) {
                        Log.e("khoado","push token to server fail");
                    }
                });
            }
        });

    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void initLayout() {
        mPrefs = getSharedPreferences(Constant.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        initToolbar();
        initNavigationMenu();
        if (getIntent()!=null) {
            Intent intent = getIntent();
            latNotify = intent.getStringExtra("lat");
            lngNotify = intent.getStringExtra("lng");
        }
        initFragmentMap();
    }

    private void initFragmentMap() {
        if (latNotify == null || lngNotify == null)
            replaceFragment(R.id.container_fragment, (BaseFragment) new MapPresenter().getFragment());
        else {
            Bundle bundle = new Bundle();
            bundle.putString("lat",latNotify);
            bundle.putString("lng",lngNotify);
            BaseFragment baseFragment = (BaseFragment) new MapPresenter().getFragment();
            baseFragment.setArguments(bundle);
            actionBar.setTitle("Bản đồ");
            navigationView.getMenu().getItem(0).setChecked(true);
            addFragment(R.id.container_fragment, baseFragment,false,true);
        }
    }


    private void initToolbar() {
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Tìm kiếm nhà trọ");
        Tools.setSystemBarColor(this);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PostMotelFragmentDialog.REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            List<Uri> mSelected = Matisse.obtainResult(data);
            List<Bitmap> listBitmap = new ArrayList<>();
            for (Uri uri : mSelected) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    listBitmap.add(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            EventBus.getDefault().post(new EventBusChoosePicture(listBitmap));
        } else if (requestCode == PostMotelFragmentDialog.CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            EventBus.getDefault().post(new EventBusChoosePicture(photo));
        } else if (requestCode == Constant.LOGIN_CODE && resultCode == RESULT_OK) {
            initProfile();
        }
    }
    private void initNavigationMenu() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (isFirstTime) {
                    isFirstTime = false;
                    if (MyApplication.isFirstTimeRun()) {
                        initIntroView();
                    }
                }
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        View headerLayout = navigationView.getHeaderView(0);
        menu = navigationView.getMenu();
        itemMyPost = menu.findItem(R.id.nav_tin_da_dang);
        itemSignOut = menu.findItem(R.id.nav_sign_out);
        viewHeader = headerLayout.findViewById(R.id.rl_header_drawer);
        mTvFullName = headerLayout.findViewById(R.id.tv_full_name);
        mTvEmail = headerLayout.findViewById(R.id.tv_email);
        initProfile();
        // open drawer at start
//        drawer.openDrawer(GravityCompat.START);
    }
    private void initIntroView() {

        int[] array = new int[2];
        mTvFullName.getLocationInWindow(array);
        float oneX = array[0] + mTvFullName.getWidth() / 2f;
        float oneY = array[1] + mTvFullName.getHeight() /2f;
        float oneRadius = 100f;
        SimpleTarget firstTarget = new SimpleTarget.Builder(this)
                .setPoint(oneX, oneY)
                .setShape(new com.takusemba.spotlight.shape.Circle(oneRadius))
                .setTitle("Đăng nhập")
                .setDescription("Nhấn vào vùng này để đăng nhập")
//                .setOverlayPoint(100f, oneY + oneRadius + 100f)
                .build();
        int[] array2 = new int[2];
        Spotlight.with(this)
                .setOverlayColor(R.color.black_overlay)
                .setDuration(500L)
                .setTargets(firstTarget)
                .setClosedOnTouchedOutside(true)
                .setOnSpotlightStateListener(new OnSpotlightStateChangedListener() {
                    @Override
                    public void onStarted() {

                    }

                    @Override
                    public void onEnded() {

                    }
                })
                .start();
    }

    private void initProfile() {
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
        if (userModel!=null) {
            if (!userModel.getFullName().isEmpty()) {
                mTvFullName.setText(userModel.getFullName());
            } else {
                mTvFullName.setText("Đăng nhập");
            }
            if (!userModel.getEmail().isEmpty()) {
                mTvEmail.setText(userModel.getEmail());
                mTvEmail.setVisibility(View.VISIBLE);
            } else {
                mTvEmail.setVisibility(View.GONE);
            }
            viewHeader.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    InforUserFragmentDialog dialog = new InforUserFragmentDialog(userModel);
                    dialog.show(getSupportFragmentManager(),dialog.getTag());
                }
            });
            itemSignOut.setVisible(true);
//            itemMyPost.setVisible(true);
        } else {
            viewHeader.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this,SignInActivity.class);
                    startActivityForResult(intent,Constant.LOGIN_CODE);
                }
            });
            itemSignOut.setVisible(false);
//            itemMyPost.setVisible(false);
        }
    }
    private void setupViewLogout() {
        viewHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,SignInActivity.class);
                startActivityForResult(intent,Constant.LOGIN_CODE);
            }
        });
        itemSignOut.setVisible(false);
//        itemMyPost.setVisible(false);
        mTvFullName.setText("Đăng nhập");
        mTvEmail.setVisibility(View.GONE);
    }


    @Override
    public void onClick(View view) {

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
//                replaceView(FragmentMap.getInstance());
//                addFragment(FragmentMap.getInstance(),false,true);
                addFragment(R.id.container_fragment,(BaseFragment) new MapPresenter().getFragment(),false,true);
                actionBar.setTitle(item.getTitle());
                lastSelect = R.id.nav_home;
                break;
            case R.id.nav_tin_da_luu:
//                addFragment(FragmentListMotelSaved.getInstance(),false,true);
                addFragment(R.id.container_fragment,(BaseFragment) new ListMotelSavedPresenter().getFragment(),false,true);
                actionBar.setTitle(item.getTitle());
                lastSelect = R.id.nav_tin_da_luu;
                break;
            case R.id.nav_tin_da_dang:
//                addFragment(FragmentListMotelPost.getInstance(),false,true);
                if (userModel== null) {
                    Toast.makeText(this,"Bạn cần đăng nhập để sử dụng chức năng này!",Toast.LENGTH_SHORT).show();
                    navigationView.setCheckedItem(lastSelect);
                    drawer.closeDrawers();
                    return false;
                } else {
                    addFragment(R.id.container_fragment,(BaseFragment) new ListMotelPostPresenter().getFragment(),false,true);
                    actionBar.setTitle(item.getTitle());
                }
                break;
            case R.id.nav_dang_ky_theo_doi:
                addFragment(R.id.container_fragment,(BaseFragment) new RegisterNotifyPresenter().getFragment(),false,true);
                actionBar.setTitle(item.getTitle());
                lastSelect = R.id.nav_dang_ky_theo_doi;
                break;
            case R.id.nav_help:
                Toast.makeText(this,"Tính năng đang trong quá trình phát triển!",Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_danh_gia_gop_y:
                rateApp(this);
                break;
            case R.id.nav_settings:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBodyText = "Tải ứng dụng tại: https://play.google.com/store/apps/details?id=com.timnhatro1.asus.projectfinal";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Ứng dụng tìm nhà trọ");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
                startActivity(Intent.createChooser(sharingIntent, "Sharing With"));
                break;
            case R.id.nav_sign_out:
                SharedPreferences.Editor prefsEditor = mPrefs.edit();
                prefsEditor.remove(Constant.KEY_USER_MODEL);
                prefsEditor.apply();
                setupViewLogout();
                backToMap();
                userModel =null;
                break;
        }
        drawer.closeDrawers();
        return true;
    }
    public static void rateApp(Context context) {
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + context.getPackageName())));
        } catch (android.content.ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + context.getPackageName())));
        }
    }

    private void backToMap() {
        addFragment(R.id.container_fragment,(BaseFragment) new MapPresenter().getFragment(),false,true);
        actionBar.setTitle("Bản đồ");
//        navigationView.getMenu().getItem(0).setChecked(true);
        navigationView.setCheckedItem(R.id.nav_home);
    }
    public void backToMapFromDangKy(final String code) {
        addFragment(R.id.container_fragment,(BaseFragment) new MapPresenter().getFragment(),false,true);
        actionBar.setTitle("Bản đồ");
//        navigationView.getMenu().getItem(0).setChecked(true);
        navigationView.setCheckedItem(R.id.nav_home);
        String lat = "";
        String lng = "";
        LocationModel model = DummyData.initDummyData(MainActivity.this);
        for (LocationModel.Data data : model.getData()) {
            for (LocationModel.ListProvince province : data.getListProvince()) {
                if (province.getCode().equals(code)) {
                    lat = province.getLat() +"";
                    lng = province.getLong() +"";
                    break;
                }
            }
        }
        EventBus.getDefault().post(new EventBusClickFromDangKy(lat,lng));

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUpdateUser(EventBusUpdateUser eventbus) {
        if (eventbus!=null) {
            updateProfile(eventbus.getUserModel());
        }
    }

    private void updateProfile(UserModel userModel) {
        this.userModel = userModel;
        if (!userModel.getFullName().isEmpty()) {
            mTvFullName.setText(userModel.getFullName());
        } else {
            mTvFullName.setText("Đăng nhập");
        }
        if (!userModel.getEmail().isEmpty()) {
            mTvEmail.setText(userModel.getEmail());
            mTvEmail.setVisibility(View.VISIBLE);
        } else {
            mTvEmail.setVisibility(View.GONE);
        }
    }
}
