package com.timnhatro1.asus.utils.utils_map;

import android.content.Context;
import android.graphics.Color;
import android.widget.ImageView;

import com.timnhatro1.asus.interactor.model.motel.MotelModel;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

public class MotelModelRenderer  extends DefaultClusterRenderer<MotelModel> {
    private  IconGenerator mIconGenerator;
    private  IconGenerator mClusterIconGenerator;
    private  ImageView mImageView;
    private  ImageView mClusterImageView;
    private  int mDimension;

    public MotelModelRenderer(Context context, GoogleMap googleMap,ClusterManager<MotelModel> mClusterManager) {
        super(context, googleMap, mClusterManager);
        mIconGenerator = new IconGenerator(context);
        mClusterIconGenerator = new IconGenerator(context);
//        View multiProfile = getLayoutInflater().inflate(R.layout.multi_profile, null);
//        mClusterIconGenerator.setContentView(multiProfile);
//        mClusterImageView = (ImageView) multiProfile.findViewById(R.id.image);
//
//        mImageView = new ImageView(getApplicationContext());
//        mDimension = (int) getResources().getDimension(R.dimen.custom_profile_image);
//        mImageView.setLayoutParams(new ViewGroup.LayoutParams(mDimension, mDimension));
//        int padding = (int) getResources().getDimension(R.dimen.custom_profile_padding);
//        mImageView.setPadding(padding, padding, padding, padding);
//        mIconGenerator.setContentView(mImageView);
    }

    @Override
    protected void onClusterItemRendered(MotelModel clusterItem, Marker marker) {
        super.onClusterItemRendered(clusterItem, marker);

    }

    @Override
    protected void onBeforeClusterItemRendered(MotelModel motel, MarkerOptions markerOptions) {
        // Draw a single person.
        // Set the info window to show their name.
        if (!motel.getId().equals("marker")) {
//            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(mIconGenerator.makeIcon(motel.getPrice())));
//            markerOptions.anchor(mIconGenerator.getAnchorU(), mIconGenerator.getAnchorV());
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(
                    BitmapDescriptorFactory.HUE_RED));
        } else {
            super.onBeforeClusterItemRendered(motel, markerOptions);
        }
    }


    @Override
    protected int getColor(int clusterSize) {
        return Color.BLUE;
    }
    //    @Override
//    protected void onBeforeClusterRendered(Cluster<MotelModel> cluster, MarkerOptions markerOptions) {
//        // Draw multiple people.
//        // Note: this method runs on the UI thread. Don't spend too much time in here (like in this example).
//        List<Drawable> profilePhotos = new ArrayList<Drawable>(Math.min(4, cluster.getSize()));
//        int width = mDimension;
//        int height = mDimension;
//
//        for (Person p : cluster.getItems()) {
//            // Draw 4 at most.
//            if (profilePhotos.size() == 4) break;
//            Drawable drawable = getResources().getDrawable(p.profilePhoto);
//            drawable.setBounds(0, 0, width, height);
//            profilePhotos.add(drawable);
//        }
//        MultiDrawable multiDrawable = new MultiDrawable(profilePhotos);
//        multiDrawable.setBounds(0, 0, width, height);
//
//        mClusterImageView.setImageDrawable(multiDrawable);
//        Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
//        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
//    }

    @Override
    protected boolean shouldRenderAsCluster(Cluster cluster) {
        // Always render clusters.
        return cluster.getSize() > 8;
    }
}

