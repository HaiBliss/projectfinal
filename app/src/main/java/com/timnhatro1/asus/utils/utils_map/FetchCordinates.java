package com.timnhatro1.asus.utils.utils_map;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.timnhatro1.asus.utils.DialogUtils;

public class FetchCordinates extends AsyncTask<String, Integer, String> {
    private static final String TAG = FetchCordinates.class.getSimpleName();
    private final FetchCordinatesCallback mFetchCordinatesCallback;

    public LocationManager mLocationManager;
    public VeggsterLocationListener mVeggsterLocationListener;
    Context mActivity;
    Location currentLocationNStore;
//    ProgressDialog progDialog;

    public FetchCordinates(Context context, Location location, FetchCordinatesCallback callback) {
        this.mActivity = context;
        this.currentLocationNStore = location;
        this.mFetchCordinatesCallback=callback;
    }

    @Override
    protected void onPreExecute() {
        mVeggsterLocationListener = new VeggsterLocationListener();
        mLocationManager = (LocationManager) mActivity.getSystemService(Context.LOCATION_SERVICE);

        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mVeggsterLocationListener);

//        progDialog = new ProgressDialog(mActivity);
//        progDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//            @Override
//            public void onCancel(DialogInterface dialog) {
//                FetchCordinates.this.cancel(true);
//            }
//        });
//        progDialog.setMessage("Đang xác định vị trí ...");
//        progDialog.setIndeterminate(true);
//        progDialog.setCancelable(true);
//        progDialog.setCanceledOnTouchOutside(false);
//        progDialog.show();
        DialogUtils.showProgressDialog((Activity) mActivity);

    }

    @Override
    protected void onCancelled() {
//        progDialog.dismiss();
        mLocationManager.removeUpdates(mVeggsterLocationListener);
        mLocationManager = null;
    }

    @Override
    protected void onPostExecute(String result) {
//        progDialog.dismiss();
        if (currentLocationNStore != null) {
            Log.e(TAG, "location:" + currentLocationNStore.getLatitude() + "," + currentLocationNStore.getLongitude());
            mFetchCordinatesCallback.fetchCordinatesCompleted(currentLocationNStore);
        } else
            Log.e(TAG, "onPostExecute location is null");
    }

    @Override
    protected String doInBackground(String... params) {
        // TODO Auto-generated method stub

        while (currentLocationNStore == null) {

        }
        return null;
    }

    public class VeggsterLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {

            try {
                currentLocationNStore = location;
                if (currentLocationNStore != null)
                    Log.e(TAG, "location:" + currentLocationNStore.getLatitude() + "," + currentLocationNStore.getLongitude());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.i("OnProviderDisabled", "OnProviderDisabled");
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.i("onProviderEnabled", "onProviderEnabled");
        }

        @Override
        public void onStatusChanged(String provider, int status,
                                    Bundle extras) {
            Log.i("onStatusChanged", "onStatusChanged");

        }

    }

    public interface FetchCordinatesCallback {
        void fetchCordinatesCompleted(Location currentLocationNStore);
    }
}