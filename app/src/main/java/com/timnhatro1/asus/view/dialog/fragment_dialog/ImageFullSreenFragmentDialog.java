package com.timnhatro1.asus.view.dialog.fragment_dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.timnhatro1.asus.view.activity.R;
import com.timnhatro1.asus.view.adapter.AdapterImageSliderFullScreen;
import com.timnhatro1.asus.interactor.model.motel.MotelModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@SuppressLint("ValidFragment")
public class ImageFullSreenFragmentDialog extends DialogFragment {
    @BindView(R.id.pager)
    ViewPager mViewPager;
    @BindView(R.id.layout_dots)
    LinearLayout mLayoutDots;
    private AdapterImageSliderFullScreen adapterImageSlider;
    private MotelModel mMotelModel;
    private int currentIndex;

    public ImageFullSreenFragmentDialog(MotelModel mMotelModel,int currentIndex) {
        this.mMotelModel = mMotelModel;
        this.currentIndex = currentIndex;
    }

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
        View view = inflater.inflate(R.layout.fragment_list_image_full_screen,container,false);
        ButterKnife.bind(this,view);
        intiView();
        return view;
    }

    private void intiView() {
        initSliderImage();
    }
    private void initSliderImage() {
        List<String> items = new ArrayList<>();
        String[] listPicture = mMotelModel.getListPicture().split(" ");
        if (listPicture.length > 0) {
            items.addAll(Arrays.asList(listPicture));
        } else {
            items.add("error");
        }
        adapterImageSlider = new AdapterImageSliderFullScreen(getActivity(), items);
        mViewPager.setAdapter(adapterImageSlider);

        // displaying selected image first
        mViewPager.setCurrentItem(currentIndex);
        addBottomDots(mLayoutDots, adapterImageSlider.getCount(), currentIndex);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int pos, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int pos) {
                addBottomDots(mLayoutDots, adapterImageSlider.getCount(), pos);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

//        startAutoSlider(adapterImageSlider.getCount());
    }
    private void addBottomDots(LinearLayout layout_dots, int size, int current) {
        ImageView[] dots = new ImageView[size];

        layout_dots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new ImageView(getActivity());
            int width_height = 15;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(width_height, width_height));
            params.setMargins(10, 0, 10, 0);
            dots[i].setLayoutParams(params);
            dots[i].setImageResource(R.drawable.shape_circle_outline);
            dots[i].setColorFilter(ContextCompat.getColor(getActivity(), R.color.grey_40), PorterDuff.Mode.SRC_ATOP);
            layout_dots.addView(dots[i]);
        }

        if (dots.length > 0) {
            dots[current].setImageResource(R.drawable.shape_circle);
            dots[current].setColorFilter(ContextCompat.getColor(getActivity(), R.color.grey_40), PorterDuff.Mode.SRC_ATOP);
        }
    }
    @OnClick({R.id.ll_back})
    void onClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                dismiss();
                break;
        }
    }
}
