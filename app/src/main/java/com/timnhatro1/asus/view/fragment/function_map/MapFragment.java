package com.timnhatro1.asus.view.fragment.function_map;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.timnhatro1.asus.Constant;
import com.timnhatro1.asus.MyApplication;
import com.timnhatro1.asus.view.activity.R;
import com.timnhatro1.asus.utils.utils_map.FetchCordinates;
import com.timnhatro1.asus.utils.utils_map.GPSService;
import com.timnhatro1.asus.contract.MapContract;
import com.timnhatro1.asus.view.adapter.ResultMotelViewPagerAdapter;
import com.timnhatro1.asus.view.dialog.DialogConfrim;
import com.timnhatro1.asus.model.event_bus.EventBusClickFromDangKy;
import com.timnhatro1.asus.model.event_bus.EventBusMotelModelSave;
import com.timnhatro1.asus.model.event_bus.EventBusNotify;
import com.timnhatro1.asus.view.dialog.fragment_dialog.SearchMotelFragmentDialog;
import com.timnhatro1.asus.interactor.model.motel.MotelModel;
import com.timnhatro1.asus.utils.DialogUtils;
import com.timnhatro1.asus.utils.StringUtils;
import com.timnhatro1.asus.utils.utils_map.MotelModelRenderer;
import com.gemvietnam.base.viper.ViewFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.algo.Algorithm;
import com.google.maps.android.clustering.algo.NonHierarchicalDistanceBasedAlgorithm;
import com.takusemba.spotlight.OnSpotlightStateChangedListener;
import com.takusemba.spotlight.Spotlight;
import com.takusemba.spotlight.target.SimpleTarget;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import io.reactivex.functions.Consumer;

/**
 * The Home Fragment
 */
public class MapFragment extends ViewFragment<MapContract.Presenter> implements MapContract.View, OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        ClusterManager.OnClusterClickListener<MotelModel>,
        ClusterManager.OnClusterInfoWindowClickListener<MotelModel>, ClusterManager.OnClusterItemClickListener<MotelModel>,
        ClusterManager.OnClusterItemInfoWindowClickListener<MotelModel>,FetchCordinates.FetchCordinatesCallback {
    @BindView(R.id.map)
    MapView mMapView;
    @BindView(R.id.bottom_sheet)
    LinearLayout mBottomSheet;
    @BindView(R.id.fab_search)
    FloatingActionButton mFab;
    @BindView(R.id.iv_return)
    View mIvReturn;
    @BindView(R.id.view_title)
    View mViewTitle;
    @BindView(R.id.result_viewpaper)
    ViewPager mResultViewPaper;
    @BindView(R.id.layout_no_data)
    View mViewNoData;
    @BindView(R.id.layout_no_data_return)
    View mViewNoDataReturn;
    @BindView(R.id.btnRetry)
    Button btnRetry;
    private boolean ivReturnVisible;
    private GoogleMap mGoogleMap;
    private GoogleApiClient mGoogleApiClient;
    private LatLng mCurrentLocation;
    private BottomSheetBehavior bottomSheetBehavior;
    private List<MotelModel> listMotelMap;
    private List<MotelModel> listMotelViewpaper;
    private ClusterManager<MotelModel> mClusterManager;
    private static MapFragment mapFragment;
    private ResultMotelViewPagerAdapter adapter;
    private boolean isFirst;
    private MotelModelRenderer motelModelRenderer;
    private Algorithm<MotelModel> clusterManagerAlgorithm;
    private List<MotelModel> listMotelMarker;
    private Marker previousMarker;
    private Circle mCircleLast;
    private int lastTypeCallAPI = -1;
    private double lastLat = -1;
    private double lastLng = -1;
    private float lastRadius = -1;
    private String lastMinPrice = null, lastMaxPrice = null, lastMinSpace = null, lastMaxSpace = null, lastTime = null, lastCodeQuanHuyen = null;
    private String latNotify, lngNotify;
    private boolean isCheckSeachNofity = false;
    private GPSService gpsServiceNStore;
    private boolean isShowDialogAskPermission = false;
    private View mViewIcRefresh;
    public static MapFragment getInstance() {
        if (mapFragment == null) {
            return new MapFragment();
        }
        return mapFragment;
    }

    @Override
    protected void parseArgs(Bundle args) {
        super.parseArgs(args);
        if (args != null) {
            this.latNotify = args.getString("lat", "");
            this.lngNotify = args.getString("lng", "");
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_map;
    }

    @Override
    public void initLayout(Bundle savedInstanceState) {
        isFirst = true;
        listMotelMarker = new ArrayList<>();
        initMaps(savedInstanceState);
        setHasOptionsMenu(true);
        listMotelViewpaper = new ArrayList<>();
        listMotelMap = new ArrayList<>();
//        listMotel = DummyData.getListMotelModel();
        initBottomSheet();
        initViewPager();
        initListener();
        if (MyApplication.isFirstTimeRun()) {
            DialogUtils.dismissProgressDialog();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    DialogUtils.dismissProgressDialog();
                    initIntroView();
                }
            },1000);
        }
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                ViewTooltip
//                        .on(getViewContext(), mFab)
//
//                        .autoHide(true, 2000)
//                        .clickToHide(false)
//
//                        .align(CENTER)
//
//                        .position(TOP)
//
//                        .text("The text")
//
//                        .textColor(Color.WHITE)
//                        .color(Color.BLUE)
//
//                        .corner(10)
//
//                        .arrowWidth(15)
//                        .arrowHeight(15)
//
//                        .distanceWithView(0)
//
//                        //listeners
//                        .onDisplay(new ViewTooltip.ListenerDisplay() {
//                            @Override
//                            public void onDisplay(View view) {
//
//                            }
//                        })
//                        .onHide(new ViewTooltip.ListenerHide() {
//                            @Override
//                            public void onHide(View view) {
//
//                            }
//                        })
//                        .show();
//            }
//        },1000);
    }

    private void initIntroView() {

        int[] array = new int[2];
        mFab.getLocationInWindow(array);
        float oneX = array[0] + mFab.getWidth() / 2f;
        float oneY = array[1] + mFab.getHeight() /2f;
        float oneRadius = 100f;
        SimpleTarget firstTarget = new SimpleTarget.Builder(getViewContext())
                .setPoint(oneX, oneY)
                .setShape(new com.takusemba.spotlight.shape.Circle(oneRadius))
                .setTitle("Thực hiện tìm kiếm")
                .setDescription("Nhấn vào nút này để thay đổi các tiêu chí tìm kiếm")
//                .setOverlayPoint(100f, oneY + oneRadius + 100f)
                .build();
        int[] array2 = new int[2];
        mViewTitle.getLocationInWindow(array2);
        float twoX = array2[0] + mViewTitle.getWidth() / 2f;
        float twoY = array2[1] + mViewTitle.getHeight() /2f;
        SimpleTarget secTarget = new SimpleTarget.Builder(getViewContext())
                .setPoint(twoX, twoY)
                .setShape(new com.takusemba.spotlight.shape.Circle(oneRadius))
                .setTitle("Danh sách kết quả")
                .setDescription("Thực hiện thao tác vuốt từ dưới lên để hiển thị danh sách kết quả")
//                .setOverlayPoint(100f, oneY + oneRadius + 100f)
                .build();
        Spotlight.with(getViewContext())
          .setOverlayColor(R.color.black_overlay)
                .setDuration(500L)
                .setTargets(firstTarget,secTarget)
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

    private void initListener() {
        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RxPermissions rxPermissions = new RxPermissions(getViewContext());
                rxPermissions.request(Manifest.permission.ACCESS_FINE_LOCATION)
                        .subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(Boolean aBoolean) throws Exception {
                                if (aBoolean) {
                                    if (mGoogleApiClient == null)
                                        mGoogleApiClient = new GoogleApiClient.Builder(getActivity()).addConnectionCallbacks(MapFragment.this)
                                                .addOnConnectionFailedListener(MapFragment.this)
                                                .addApi(LocationServices.API)
                                                .build();
                                    if (mGoogleApiClient != null)
                                        mGoogleApiClient.connect();
                                    findGPSWithcheckPermission();
//                                    if (lastTypeCallAPI == -1) {
//                                        lastTypeCallAPI = SearchMotelFragmentDialog.CHOOSE_NEAR_ME;
//                                    }
//                                    if (lastTypeCallAPI == SearchMotelFragmentDialog.CHOOSE_NEAR_ME) {
//                                        if (lastTime != null) {
//                                            mPresenter.searchNearMe(lastLat, lastLng, lastRadius, lastMinPrice, lastMaxPrice, lastMinSpace, lastMaxSpace, lastTime);
//                                        } else {
//                                            mPresenter.getListMotel(lastLat, lastLng, lastRadius);
//                                        }
//                                    } else if (lastTypeCallAPI == SearchMotelFragmentDialog.CHOOSE_QUAN_HUYEN) {
//                                        mPresenter.searchQuanHuyen(lastLat, lastLng, lastMinPrice, lastMaxPrice, lastMinSpace, lastMaxSpace, lastTime, lastCodeQuanHuyen);
//                                    } else if (lastTypeCallAPI == SearchMotelFragmentDialog.CHOOSE_KHU_VUC) {
//                                        mPresenter.searchArea(lastLat, lastLng, lastRadius, lastMinPrice, lastMaxPrice, lastMinSpace, lastMaxSpace, lastTime);
//                                    }
                                } else {

                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Log.e("ErrorPermission", throwable.toString());
                            }
                        });

            }
        });
    }

    private void initViewPager() {
        adapter = new ResultMotelViewPagerAdapter(getChildFragmentManager(), listMotelViewpaper);

//        for (int i = 0; i < listServiceModels.get(currentParent).getListService().size(); i++) {
//            if (data.getServiceId().equals(listServiceModels.get(currentParent).getListService().get(i).getServiceId())) {
//                currentPosition = i;
//                break;
//            }
//        }
        mResultViewPaper.setAdapter(adapter);
        mResultViewPaper.setClipToPadding(false);
        mResultViewPaper.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Do nothing
            }

            @Override
            public void onPageSelected(int position) {
                LatLng latLng = new LatLng(listMotelViewpaper.get(position).getLat() - 0.002, listMotelViewpaper.get(position).getLng());
                showCameraToPosition(latLng, 16f);
                if (bottomSheetBehavior.getState() == bottomSheetBehavior.STATE_COLLAPSED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                if (position > 0) {
                    mIvReturn.setVisibility(View.VISIBLE);
                    ivReturnVisible = true;
                }
                else{
                    mIvReturn.setVisibility(View.GONE);
                    ivReturnVisible = false;
                }
                try {
                    if (previousMarker != null) {
                        previousMarker.setIcon(BitmapDescriptorFactory.defaultMarker(
                                BitmapDescriptorFactory.HUE_RED));
                    }
                    previousMarker = motelModelRenderer.getMarker(listMotelMarker.get(position));
                    if (previousMarker != null) {
                        previousMarker.setIcon(BitmapDescriptorFactory.defaultMarker(
                                BitmapDescriptorFactory.HUE_BLUE));

                    }
                } catch (Exception e) {
                    previousMarker = null;

                }


            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // Do nothing
            }
        });
    }

    private void initBottomSheet() {
        // init the bottom sheet behavior
        bottomSheetBehavior = BottomSheetBehavior.from(mBottomSheet);

        // change the state of the bottom sheet
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        // set callback for changes
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
//                Log.e("khoado", "" + newState);
                if (newState == bottomSheetBehavior.STATE_COLLAPSED) {
                    mFab.setVisibility(View.VISIBLE);
                    mViewTitle.setVisibility(View.VISIBLE);
                    if (ivReturnVisible)
                        mIvReturn.setVisibility(View.GONE);
                } else {
                    mFab.setVisibility(View.GONE);
                    mViewTitle.setVisibility(View.GONE);
                    if (ivReturnVisible)
                       mIvReturn.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        mIvReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listMotelViewpaper.size() > 0)
                    mResultViewPaper.setCurrentItem(0, true);
            }
        });
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchMotelFragmentDialog dialog = new SearchMotelFragmentDialog();
                dialog.setOnDismiss(new SearchMotelFragmentDialog.OnDismiss() {
                    @Override
                    public void onSearchNearMe(float radius, String minPrice, String maxPrice, String minSpace, String maxSpace, String time) {
                        if (mCurrentLocation != null) {
                            LatLngBounds circleBounds;
                            circleBounds =
                                    new LatLngBounds(locationMinMax(false, mCurrentLocation, radius),
                                            locationMinMax(true, mCurrentLocation, radius));
                            showCameraToPosition(circleBounds, 100);
                            showCircleToGoogleMap(mCurrentLocation, radius);
                            mPresenter.searchNearMe(mCurrentLocation.latitude, mCurrentLocation.longitude, radius, minPrice, maxPrice, minSpace, maxSpace, time);
                            lastTypeCallAPI = SearchMotelFragmentDialog.CHOOSE_NEAR_ME;
                            lastLat = mCurrentLocation.latitude;
                            lastLng = mCurrentLocation.longitude;
                            lastRadius = radius;
                            lastMinPrice = minPrice;
                            lastMaxPrice = maxPrice;
                            lastMinSpace = minSpace;
                            lastMaxSpace = maxSpace;
                            lastTime = time;
                        } else {
                            Toast.makeText(getViewContext(), "Lỗi lấy được vị trí hiện tại!", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onSearchArea(double lat, double log, float radius, String minPrice, String maxPrice, String minSpace, String maxSpace, String time) {
                        LatLngBounds circleBounds;
                        circleBounds =
                                new LatLngBounds(locationMinMax(false, new LatLng(lat, log), radius),
                                        locationMinMax(true, new LatLng(lat, log), radius));
                        showCameraToPosition(circleBounds, 100);
                        showCircleToGoogleMap(new LatLng(lat, log), radius);
                        mPresenter.searchArea(lat, log, radius, minPrice, maxPrice, minSpace, maxSpace, time);
                        lastTypeCallAPI = SearchMotelFragmentDialog.CHOOSE_KHU_VUC;
                        lastLat = mCurrentLocation.latitude;
                        lastLng = mCurrentLocation.longitude;
                        lastRadius = radius;
                        lastMinPrice = minPrice;
                        lastMaxPrice = maxPrice;
                        lastMinSpace = minSpace;
                        lastMaxSpace = maxSpace;
                        lastTime = time;
                    }

                    @Override
                    public void onSearchQuanHuyen(double lat, double log, String minPrice, String maxPrice, String minSpace,
                                                  String maxSpace, String time, String codeQuanHuyen) {
                        showCameraToPosition(new LatLng(lat, log), 13f);
                        if (mCircleLast != null) {
                            mCircleLast.remove();
                        }
                        mPresenter.searchQuanHuyen(lat, log, minPrice, maxPrice, minSpace, maxSpace, time, codeQuanHuyen);
                        lastTypeCallAPI = SearchMotelFragmentDialog.CHOOSE_QUAN_HUYEN;
                        lastLat = lat;
                        lastLng = log;
                        lastMinPrice = minPrice;
                        lastMaxPrice = maxPrice;
                        lastMinSpace = minSpace;
                        lastMaxSpace = maxSpace;
                        lastTime = time;
                        lastCodeQuanHuyen = codeQuanHuyen;
                    }
                });
                dialog.show(getFragmentManager(), dialog.getTag());
            }
        });
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxPermissions rxPermissions = new RxPermissions(getViewContext());
        rxPermissions.request(Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            mGoogleApiClient = new GoogleApiClient.Builder(getActivity()).addConnectionCallbacks(MapFragment.this)
                                    .addOnConnectionFailedListener(MapFragment.this)
                                    .addApi(LocationServices.API)
                                    .build();
                            if (mGoogleApiClient != null)
                                mGoogleApiClient.connect();
                        } else {

                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e("ErrorPermission", throwable.toString());
                    }
                });

        EventBus.getDefault().register(this);
    }


    private void initMaps(Bundle savedInstanceState) {

        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(this);

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && mGoogleApiClient != null) {
            final Location lastLocation =
                    LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (lastLocation == null) {
                return;
            }
            mCurrentLocation = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
            if (isFirst) {
//                showCameraToPosition(mCurrentLocation, 14f);
                isFirst = false;
                if (StringUtils.isEmpty(latNotify) || StringUtils.isEmpty(lngNotify)) {

                    LatLngBounds circleBounds;
                    circleBounds =
                            new LatLngBounds(locationMinMax(false, mCurrentLocation, Constant.DEFAULT_RADIUS),
                                    locationMinMax(true, mCurrentLocation, Constant.DEFAULT_RADIUS));
                    showCameraToPosition(circleBounds, 100);
                    showCircleToGoogleMap(mCurrentLocation, Constant.DEFAULT_RADIUS);
                    Log.e("khoado","1");
                    mPresenter.getListMotel(mCurrentLocation.latitude, mCurrentLocation.longitude, Constant.DEFAULT_RADIUS);
                    lastTypeCallAPI = SearchMotelFragmentDialog.CHOOSE_NEAR_ME;
                    lastLat = mCurrentLocation.latitude;
                    lastLng = mCurrentLocation.longitude;
                    lastRadius = Constant.DEFAULT_RADIUS;


                } else {
                    if (isCheckSeachNofity) {
                        LatLngBounds circleBounds;
                        circleBounds =
                                new LatLngBounds(locationMinMax(false, new LatLng(Double.valueOf(latNotify), Double.valueOf(lngNotify)), Constant.DEFAULT_RADIUS),
                                        locationMinMax(true, new LatLng(Double.valueOf(latNotify), Double.valueOf(lngNotify)), Constant.DEFAULT_RADIUS));
                        showCameraToPosition(circleBounds, 100);
                        showCircleToGoogleMap(new LatLng(Double.valueOf(latNotify), Double.valueOf(lngNotify)), Constant.DEFAULT_RADIUS);
                        Log.e("khoado","2");
                        mPresenter.getListMotel(Double.valueOf(latNotify), Double.valueOf(lngNotify), Constant.DEFAULT_RADIUS);
                        lastTypeCallAPI = SearchMotelFragmentDialog.CHOOSE_NEAR_ME;
                        lastLat = Double.valueOf(latNotify);
                        lastLng = Double.valueOf(lngNotify);
                        lastRadius = Constant.DEFAULT_RADIUS;
                    } else {
                        isCheckSeachNofity = true;
                    }
                }

            }
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_map, menu);
        mViewIcRefresh = menu.findItem(R.id.ic_refresh).getActionView();
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.ic_refresh:
                // do stuff, like showing settings fragment
                RxPermissions rxPermissions = new RxPermissions(getViewContext());
                rxPermissions.request(Manifest.permission.ACCESS_FINE_LOCATION)
                        .subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(Boolean aBoolean) throws Exception {
                                if (aBoolean) {
                                    if (mGoogleApiClient == null)
                                        mGoogleApiClient = new GoogleApiClient.Builder(getActivity()).addConnectionCallbacks(MapFragment.this)
                                                .addOnConnectionFailedListener(MapFragment.this)
                                                .addApi(LocationServices.API)
                                                .build();
                                    if (mGoogleApiClient != null)
                                        mGoogleApiClient.connect();
                                    findGPSWithcheckPermission();
//                                    if (lastTypeCallAPI == -1) {
//                                        lastTypeCallAPI = SearchMotelFragmentDialog.CHOOSE_NEAR_ME;
//                                    }
//                                    if (lastTypeCallAPI == SearchMotelFragmentDialog.CHOOSE_NEAR_ME) {
//                                        if (lastTime != null) {
//                                            mPresenter.searchNearMe(lastLat, lastLng, lastRadius, lastMinPrice, lastMaxPrice, lastMinSpace, lastMaxSpace, lastTime);
//                                        } else {
//                                            mPresenter.getListMotel(lastLat, lastLng, lastRadius);
//                                        }
//                                    } else if (lastTypeCallAPI == SearchMotelFragmentDialog.CHOOSE_QUAN_HUYEN) {
//                                        mPresenter.searchQuanHuyen(lastLat, lastLng, lastMinPrice, lastMaxPrice, lastMinSpace, lastMaxSpace, lastTime, lastCodeQuanHuyen);
//                                    } else if (lastTypeCallAPI == SearchMotelFragmentDialog.CHOOSE_KHU_VUC) {
//                                        mPresenter.searchArea(lastLat, lastLng, lastRadius, lastMinPrice, lastMaxPrice, lastMinSpace, lastMaxSpace, lastTime);
//                                    }
                                } else {

                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Log.e("ErrorPermission", throwable.toString());
                            }
                        });

                return true;
        }

        return super.onOptionsItemSelected(item); // important line
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
//        mGoogleMap.setOnMapClickListener(this);
        //bat tat chuc nang chuyen huong sang app google map
        mGoogleMap.getUiSettings().setCompassEnabled(false);
        //tat + - tren map
        mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
        //bat tat chuc nang cu chi tren map
        mGoogleMap.getUiSettings().setZoomGesturesEnabled(true);
        //bat dinh vi vi tri hien taisdasd
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        mGoogleMap.setTrafficEnabled(false);
        mGoogleMap.setBuildingsEnabled(false);


        if (ActivityCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mGoogleMap.setMyLocationEnabled(true);
        } else {
            //            Common.checkAndRequestPermissionsGPS(getActivity());
        }
        mGoogleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                LatLng latlng = mGoogleMap.getProjection().getVisibleRegion().latLngBounds.getCenter();
                float zoom = mGoogleMap.getCameraPosition().zoom;
                Log.e("onCameraIdle", latlng.latitude + " " + latlng.longitude + " " + zoom);
                //tinh khoang cach di chuyen camera o day
            }
        });
        mGoogleMap.setOnInfoWindowClickListener(mClusterManager);
        mClusterManager = new ClusterManager<MotelModel>(getActivity(), mGoogleMap);
        mGoogleMap.setOnCameraIdleListener(mClusterManager);
        mGoogleMap.setOnMarkerClickListener(mClusterManager);
        motelModelRenderer = new MotelModelRenderer(getActivity(), mGoogleMap, mClusterManager);
        clusterManagerAlgorithm = new NonHierarchicalDistanceBasedAlgorithm();
        mClusterManager.setAlgorithm(clusterManagerAlgorithm);
        mClusterManager.setRenderer(motelModelRenderer);
        mClusterManager.setOnClusterClickListener(this);
        mClusterManager.setOnClusterInfoWindowClickListener(this);
        mClusterManager.setOnClusterItemClickListener(this);
        mClusterManager.setOnClusterItemInfoWindowClickListener(this);
        if (StringUtils.isEmpty(latNotify) || StringUtils.isEmpty(lngNotify)) {

        } else {
            if (isCheckSeachNofity) {
                LatLngBounds circleBounds;
                circleBounds =
                        new LatLngBounds(locationMinMax(false, new LatLng(Double.valueOf(latNotify), Double.valueOf(lngNotify)), Constant.DEFAULT_RADIUS),
                                locationMinMax(true, new LatLng(Double.valueOf(latNotify), Double.valueOf(lngNotify)), Constant.DEFAULT_RADIUS));
                showCameraToPosition(circleBounds, 100);
                showCircleToGoogleMap(new LatLng(Double.valueOf(latNotify), Double.valueOf(lngNotify)), Constant.DEFAULT_RADIUS);
                Log.e("khoado","3");
                mPresenter.getListMotel(Double.valueOf(latNotify), Double.valueOf(lngNotify), Constant.DEFAULT_RADIUS);
                lastTypeCallAPI = SearchMotelFragmentDialog.CHOOSE_NEAR_ME;
                lastLat = Double.valueOf(latNotify);
                lastLng = Double.valueOf(lngNotify);
                lastRadius = Constant.DEFAULT_RADIUS;
            } else {
                isCheckSeachNofity = true;
            }
        }
//        mPresenter.getListMotel();
    }

    private void addMarker() {
        mClusterManager.clearItems();
        mClusterManager.addItems(listMotelMap);
        mClusterManager.cluster();
        listMotelMarker.clear();
        Collection<MotelModel> items = clusterManagerAlgorithm.getItems();
        listMotelMarker.addAll(items);
    }

    private void clearMarker() {
        mClusterManager.clearItems();
        listMotelMarker.clear();
    }


    //    final LatLngBounds circleBounds;  circleBounds =
//            new LatLngBounds(locationMinMax(false, mCurrentLocation, radiusChose),
//    locationMinMax(true, mCurrentLocation, radiusChose));
//    showCameraToPosition(circleBounds, 100);
    public void showCameraToPosition(LatLng position, float zoomLevel) {
        CameraPosition cameraPosition = CameraPosition.builder()
                .target(position)
                .zoom(zoomLevel)
                .bearing(0.0f)
                .tilt(0.0f)
                .build();

        if (mGoogleMap != null) {
            mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), null);
        }
    }

    public void showCameraToPosition(LatLngBounds bounds, int padding) {
        if (mGoogleMap != null) {
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));
        }
    }

    public void showCircleToGoogleMap(LatLng position, float radius) {
        if (position == null) {
            return;
        }
        if (mCircleLast != null) {
            mCircleLast.remove();
        }
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(position);
        //Radius in meters
        circleOptions.radius(radius * 1000);
        circleOptions.fillColor(getResources().getColor(R.color.circle_on_map));
        circleOptions.strokeColor(getResources().getColor(R.color.circle_on_map));
        circleOptions.strokeWidth(0);

        if (mGoogleMap != null) {
            mCircleLast = mGoogleMap.addCircle(circleOptions);
        }
    }

    public void showMarkerToGoogleMap(LatLng position) {
        mGoogleMap.clear();
        MarkerOptions markerOptions = new MarkerOptions().position(position);
//        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_on_light_blue_800_24dp));
        markerOptions.icon(null);
        Marker marker = mGoogleMap.addMarker(markerOptions);
        MotelModel model = new MotelModel();
        model.setLat(position.latitude);
        model.setLng(position.longitude);
        model.setId("marker");
        mClusterManager.addItem(model);

    }


    private LatLng locationMinMax(boolean positive, LatLng position, float radius) {
        double sign = positive ? 1 : -1;
        double dx = (sign * radius * 1000) / 6378000 * (180 / Math.PI);
        double lat = position.latitude + dx;
        double lon = position.longitude + dx / Math.cos(position.latitude * Math.PI / 180);
        return new LatLng(lat, lon);
    }


    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        if (mGoogleApiClient != null)
            mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        mMapView.onResume();
//        if (listMotelViewpaper == null || listMotelViewpaper.size() == 0) {
//            findGPSWithcheckPermission();
//        }
        super.onResume();
    }

    private void findGPSWithcheckPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            findGPS();
        } else {
            int hasAccessLocationPermission = PermissionChecker.checkSelfPermission(getViewContext(), Manifest.permission.ACCESS_FINE_LOCATION);
            if (hasAccessLocationPermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Constant.REQUEST_CODE_ASK_PERMISSIONS);
                return;
            }
            findGPS();
        }
    }

    private void findGPS() {
        gpsServiceNStore = new GPSService(getViewContext());
        if (gpsServiceNStore.canGetLocation()) {
            if (gpsServiceNStore.getLocation() == null)
                startService(gpsServiceNStore.getLocation());
            else {
                if (lastTypeCallAPI == -1) {
                    mCurrentLocation = new LatLng(gpsServiceNStore.getLocation().getLatitude(),gpsServiceNStore.getLocation().getLongitude());
                    LatLngBounds circleBounds;
                    circleBounds =
                            new LatLngBounds(locationMinMax(false, mCurrentLocation, Constant.DEFAULT_RADIUS),
                                    locationMinMax(true, mCurrentLocation, Constant.DEFAULT_RADIUS));
                    showCameraToPosition(circleBounds, 100);
                    showCircleToGoogleMap(mCurrentLocation, Constant.DEFAULT_RADIUS);
                    Log.e("khoado","4");
                    mPresenter.getListMotel(mCurrentLocation.latitude, mCurrentLocation.longitude, Constant.DEFAULT_RADIUS);
                    lastTypeCallAPI = SearchMotelFragmentDialog.CHOOSE_NEAR_ME;
                    lastLat = mCurrentLocation.latitude;
                    lastLng = mCurrentLocation.longitude;
                    lastRadius = Constant.DEFAULT_RADIUS;
                } else {
                    if (lastTypeCallAPI == SearchMotelFragmentDialog.CHOOSE_NEAR_ME) {
                        if (lastTime != null) {
                            mPresenter.searchNearMe(lastLat, lastLng, lastRadius, lastMinPrice, lastMaxPrice, lastMinSpace, lastMaxSpace, lastTime);
                        } else {
                            Log.e("khoado","5");
                            mPresenter.getListMotel(lastLat, lastLng, lastRadius);
                        }
                    } else if (lastTypeCallAPI == SearchMotelFragmentDialog.CHOOSE_QUAN_HUYEN) {
                        if (mCircleLast != null) {
                            mCircleLast.remove();
                        }
                        mPresenter.searchQuanHuyen(lastLat, lastLng, lastMinPrice, lastMaxPrice, lastMinSpace, lastMaxSpace, lastTime, lastCodeQuanHuyen);
                    } else if (lastTypeCallAPI == SearchMotelFragmentDialog.CHOOSE_KHU_VUC) {
                        mPresenter.searchArea(lastLat, lastLng, lastRadius, lastMinPrice, lastMaxPrice, lastMinSpace, lastMaxSpace, lastTime);
                    }
                }
            }


        } else {

            gpsServiceNStore.setOnClickDialogTTC(new DialogConfrim.OnClickDialogTTC() {
                @Override
                public void onButonYesClick() {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    getViewContext().startActivity(intent);
                    isShowDialogAskPermission = false;
                }

                @Override
                public void onButtonNoClick() {
                    isShowDialogAskPermission = false;
                }

                @Override
                public void onButtonCloseClick() {
                    isShowDialogAskPermission = false;
                }
            });
            if (!isShowDialogAskPermission) {
                gpsServiceNStore.showSettingsAlert();
                isShowDialogAskPermission = true;
            }
        }
    }

    private void startService(Location location) {
        try {
            final FetchCordinates fetchCordinatesNStore = new FetchCordinates(getViewContext(),location,this);
            fetchCordinatesNStore.execute();
            CountDownTimer countDownTimerNStore = new CountDownTimer(5000, 5000) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    if (fetchCordinatesNStore.getStatus() == AsyncTask.Status.RUNNING) {
                        fetchCordinatesNStore.cancel(true);
                    }
                }
            };
            countDownTimerNStore.start();
        } catch (Exception error) {
        }
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        EventBus.getDefault().unregister(this);
        if (gpsServiceNStore != null) {
            gpsServiceNStore.stopUsingGPS();
        }
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        mMapView.onLowMemory();
        super.onLowMemory();
    }


    @Override
    public boolean onClusterClick(Cluster<MotelModel> cluster) {
        LatLngBounds.Builder builder = LatLngBounds.builder();
        for (ClusterItem item : cluster.getItems()) {
            builder.include(item.getPosition());
        }
        // Get the LatLngBounds
        final LatLngBounds bounds = builder.build();

        // Animate camera to the bounds
        try {
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public void onClusterInfoWindowClick(Cluster<MotelModel> cluster) {

    }

    @Override
    public boolean onClusterItemClick(MotelModel motelModel) {
//        if (motelModel.getId().equals("marker")) {
//            final Dialog dialog = new Dialog(getActivity());
//            dialog.setContentView(R.layout.dialog_with_seekbar_two_thumb);
//            dialog.setTitle("Title...");
//            dialog.show();
//        } else {
//            MotelDetailBottomSheetFragment fragment = new MotelDetailBottomSheetFragment();
//            fragment.setmMotelModel(motelModel);
//            fragment.show(getActivity().getSupportFragmentManager(), fragment.getTag());
//        }
//        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.image_26);
//        motelModelRenderer.getMarker(motelModel).setIcon(BitmapDescriptorFactory.fromBitmap(largeIcon));
        int position = -1;
        for (int i = 0; i < listMotelViewpaper.size(); i++) {
            if (motelModel.getId().equals(listMotelViewpaper.get(i).getId())) {
                position = i;
            }
        }
        if (bottomSheetBehavior.getState() == bottomSheetBehavior.STATE_COLLAPSED) {
            mViewTitle.setVisibility(View.GONE);
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
        if (position == -1) {
            listMotelViewpaper.add(motelModel);
            adapter.notifyDataSetChanged();
            mResultViewPaper.setCurrentItem(listMotelViewpaper.size(), true);
        } else {
            mResultViewPaper.setCurrentItem(position, true);
        }
        try {
            if (previousMarker != null) {
                previousMarker.setIcon(BitmapDescriptorFactory.defaultMarker(
                        BitmapDescriptorFactory.HUE_RED));
            }
            previousMarker = motelModelRenderer.getMarker(motelModel);
            if (previousMarker != null) {
                previousMarker.setIcon(BitmapDescriptorFactory.defaultMarker(
                        BitmapDescriptorFactory.HUE_BLUE));

            }
        } catch (Exception e) {
            Log.e("khoado", "crashed here");
            previousMarker = null;
        }

        return true;
    }

    @Override
    public void onClusterItemInfoWindowClick(MotelModel motelModel) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSaveMotelModelChange(EventBusMotelModelSave event) {
        for (MotelModel model : listMotelViewpaper) {
            if (model.getId().equals(event.getModel().getId())) {
                model.setSave(event.getModel().isSave());
                model.setNote(event.getModel().getNote());
                if (adapter != null)
                    adapter.notifyDataSetChanged();
                break;
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Constant.REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults!=null&&grantResults.length>0&&grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    findGPSWithcheckPermission();
                } else {
                    // Permission Denied
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    @Override
    public void onGetListMotelSuccess(List<MotelModel> listMotelModel) {
        if (listMotelModel.size() > 0) {
            mViewNoData.setVisibility(View.GONE);
            mResultViewPaper.setVisibility(View.VISIBLE);
            mViewNoDataReturn.setVisibility(View.GONE);
//            mIvReturn.setVisibility(View.VISIBLE);
        } else {
            mViewNoDataReturn.setVisibility(View.VISIBLE);
            mViewNoData.setVisibility(View.GONE);
            mResultViewPaper.setVisibility(View.GONE);
//            mIvReturn.setVisibility(View.GONE);
            Toast.makeText(getViewContext(), "Không tìm thấy thông tin nhà trong khu vực", Toast.LENGTH_LONG).show();
        }
        listMotelMap.clear();
        listMotelMap.addAll(listMotelModel);
        listMotelViewpaper.clear();
        listMotelViewpaper.addAll(listMotelModel);
        addMarker();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onGetListMotelFail() {
        mViewNoDataReturn.setVisibility(View.GONE);
        mViewNoData.setVisibility(View.VISIBLE);
        mResultViewPaper.setVisibility(View.GONE);
//        mIvReturn.setVisibility(View.GONE);
        listMotelMap.clear();
        listMotelViewpaper.clear();
        clearMarker();
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setCurrentAreaSearch(EventBusClickFromDangKy event) {
        String lat = event.getLat();
        String lng = event.getLng();
        if (lat.isEmpty() || lng.isEmpty()) {

        } else {
            LatLngBounds circleBounds;
            circleBounds =
                    new LatLngBounds(locationMinMax(false, new LatLng(Double.valueOf(lat), Double.valueOf(lng)), Constant.DEFAULT_RADIUS),
                            locationMinMax(true, new LatLng(Double.valueOf(lat), Double.valueOf(lng)), Constant.DEFAULT_RADIUS));
            showCameraToPosition(circleBounds, 100);
            showCircleToGoogleMap(new LatLng(Double.valueOf(lat), Double.valueOf(lng)), Constant.DEFAULT_RADIUS);
            Log.e("khoado","6");
            mPresenter.getListMotel(Double.valueOf(lat), Double.valueOf(lng), Constant.DEFAULT_RADIUS);
            lastTypeCallAPI = SearchMotelFragmentDialog.CHOOSE_NEAR_ME;
            lastLat = Double.valueOf(lat);
            lastLng = Double.valueOf(lng);
            lastRadius = Constant.DEFAULT_RADIUS;
        }

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void notify(EventBusNotify eventBus) {
        if (eventBus!=null) {
            String lat = eventBus.getLat();
            String lng = eventBus.getLng();
            LatLngBounds circleBounds;
            circleBounds =
                    new LatLngBounds(locationMinMax(false, new LatLng(Double.valueOf(lat), Double.valueOf(lng)), Constant.DEFAULT_RADIUS),
                            locationMinMax(true, new LatLng(Double.valueOf(lat), Double.valueOf(lng)), Constant.DEFAULT_RADIUS));
            showCameraToPosition(circleBounds, 100);
            showCircleToGoogleMap(new LatLng(Double.valueOf(lat), Double.valueOf(lng)), Constant.DEFAULT_RADIUS);
            mPresenter.getListMotel(Double.valueOf(lat), Double.valueOf(lng), Constant.DEFAULT_RADIUS);
            Log.e("khoado","7");
            lastTypeCallAPI = SearchMotelFragmentDialog.CHOOSE_NEAR_ME;
            lastLat = Double.valueOf(lat);
            lastLng = Double.valueOf(lng);
            lastRadius = Constant.DEFAULT_RADIUS;
        }
    }

    @Override
    public void fetchCordinatesCompleted(Location currentLocationNStore) {
        if (currentLocationNStore!=null) {
            mCurrentLocation = new LatLng(currentLocationNStore.getLatitude(),currentLocationNStore.getLongitude());
            LatLngBounds circleBounds;
            circleBounds =
                    new LatLngBounds(locationMinMax(false, mCurrentLocation, Constant.DEFAULT_RADIUS),
                            locationMinMax(true, mCurrentLocation, Constant.DEFAULT_RADIUS));
            showCameraToPosition(circleBounds, 100);
            showCircleToGoogleMap(mCurrentLocation, Constant.DEFAULT_RADIUS);
            mPresenter.getListMotel(mCurrentLocation.latitude, mCurrentLocation.longitude, Constant.DEFAULT_RADIUS);
            Log.e("khoado","8");
            lastTypeCallAPI = SearchMotelFragmentDialog.CHOOSE_NEAR_ME;
            lastLat = mCurrentLocation.latitude;
            lastLng = mCurrentLocation.longitude;
            lastRadius = Constant.DEFAULT_RADIUS;
        }

    }
}
