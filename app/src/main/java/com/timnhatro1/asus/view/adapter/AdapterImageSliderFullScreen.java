package com.timnhatro1.asus.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.balysv.materialripple.MaterialRippleLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.timnhatro1.asus.view.activity.R;

import java.util.List;

public class AdapterImageSliderFullScreen extends PagerAdapter {

    private Activity act;
    private List<String> items;

    // constructor
    public AdapterImageSliderFullScreen(Activity activity, List<String> items) {
        this.act = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return this.items.size();
    }

    public String getItem(int pos) {
        return items.get(pos);
    }

    public void setItems(List<String> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final String url = items.get(position);
        LayoutInflater inflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.item_slider_image_zoom, container, false);

        final SubsamplingScaleImageView image =  v.findViewById(R.id.image);
        MaterialRippleLayout lyt_parent = (MaterialRippleLayout) v.findViewById(R.id.lyt_parent);
//        Tools.loadImage(act,url, image, R.drawable.default_image_thumbnail,R.drawable.default_image_thumbnail,true);
        RequestOptions options = new RequestOptions()
                .fitCenter()
                .placeholder(R.drawable.default_image_thumbnail)
                .error(R.drawable.default_image_thumbnail)
                .dontAnimate();
        Glide.with(act).asBitmap().load(url).apply(options).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                image.setImage(ImageSource.bitmap(resource));
            }
        });


        ((ViewPager) container).addView(v);

        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);

    }

}