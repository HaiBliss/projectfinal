package com.timnhatro1.asus.view.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.timnhatro1.asus.view.fragment.function_map.MapFragment;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.annotations.Nullable;
import io.reactivex.functions.Consumer;

public class LocationSelectActivity extends MyBaseActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {
    @BindView(R.id.mapView)
    MapView mMapView;
    @BindView(R.id.iv_location)
    ImageView mIvLocation;
    private static final String TAG = "MapsActivity";
    private GoogleMap mMap;
    private double longitude;
    private double latitude;
    private GoogleApiClient googleApiClient;
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "LONGITUDE";
    public static final int RESULT_CODE = 1112;
//    private Marker marker;
    @Override
    public void initLayout() {
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.mapView);
//        mapFragment.getMapAsync(this);

    }

    @Override
    protected void initLayout(Bundle savedInstanceState) {
        super.initLayout(savedInstanceState);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        try {
            MapsInitializer.initialize(this.getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(this);
        //Initializing googleApiClient
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_location_picker;
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // googleMapOptions.mapType(googleMap.MAP_TYPE_HYBRID)
        //    .compassEnabled(true);
        mMap.getUiSettings().setCompassEnabled(false);
        //tat + - tren map
        mMap.getUiSettings().setZoomControlsEnabled(false);
        //bat tat chuc nang cu chi tren map
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        //tat dinh vi vi tri hien taisdasd
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.setTrafficEnabled(false);
        mMap.setBuildingsEnabled(false);
        // Add a marker in Sydney and move the camera
    }

    //Getting current location
    private void getCurrentLocation() {
        mMap.clear();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (location != null) {
            //Getting longitude and latitude
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            //moving the map to location
            moveMap();
        }
    }

    private void moveMap() {
        /**
         * Creating the latlng object to store lat, long coordinates
         * adding marker to map
         * move the camera with animation
         */
        LatLng latLng = new LatLng(latitude, longitude);
//        mMap.addMarker(new MarkerOptions()
//                .position(latLng));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition)
            {
//                if(marker==null)
//                {
//                    marker = mMap.addMarker(new MarkerOptions().position(cameraPosition.target).title("Marker")
//                            .icon(BitmapDescriptorFactory.defaultMarker(
//                                    BitmapDescriptorFactory.HUE_RED)));
//                    marker.showInfoWindow();
//                    mIvLocation.setVisibility(View.GONE);
//                }
//                else
//                {
//                    marker.setPosition(cameraPosition.target);
//                    mIvLocation.setVisibility(View.GONE);
//                }
                latitude = cameraPosition.target.latitude;
                longitude = cameraPosition.target.longitude;
            }
        });

    }
    @OnClick({R.id.selectLocationButton})
    public void onClickLocation(){
        Log.e("khoado", latitude + longitude  + "");
        Intent intent = new Intent();
        intent.putExtra(LATITUDE,latitude);
        intent.putExtra(LONGITUDE,longitude);
        setResult(RESULT_OK,intent);
        finish();
    }
    @OnClick({R.id.iv_back})
    public void onClickBack() {
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public void onClick(View view) {
        Log.v(TAG,"view click event");
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        getCurrentLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
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

    @Override
    protected void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }
    @Override
    public void onLowMemory() {
        mMapView.onLowMemory();
        super.onLowMemory();
    }



}
