package com.timnhatro1.asus.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.timnhatro1.asus.view.activity.R;

import org.florescu.android.rangeseekbar.RangeSeekBar;

public class DialogFilterWithSeekbar extends Dialog {

    TextView mTvTitle;
    TextView mTvDescription;
    TextView mTvPriceFilter;
    RangeSeekBar<Integer> mRangeSeekbar;
    View mLlApply;
    int minValue,maxValue,minValueChoose,maxValueChoose;
    RadioGroup mRGFilter;
    String unit;
    OnFilterListener onFilterListener;
    public static final int CAO_THAP = 1;
    public static final int THAP_CAO = 0;
    boolean filterCaoThap;

    public DialogFilterWithSeekbar(Context context, String title, String description,int minValue, int maxValue, String unit, OnFilterListener onFilterListener) {
        super(context,android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
        setContentView(R.layout.dialog_with_seekbar_two_thumb);
        mTvTitle = findViewById(R.id.tv_title);
        mTvDescription = findViewById(R.id.tv_description);
        mTvPriceFilter = findViewById(R.id.tv_price_filter);
        mRangeSeekbar = findViewById(R.id.range_seekbar);
        mLlApply = findViewById(R.id.ll_apply);
        mRGFilter = findViewById(R.id.radioGroup_filter);
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.minValueChoose = minValue;
        this.maxValueChoose = maxValue;
        this.unit = unit;
        this.onFilterListener = onFilterListener;
        mTvTitle.setText(title);
        mTvDescription.setText(description);
        mRangeSeekbar.setRangeValues(minValue,maxValue);
        mRangeSeekbar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                setTextPriceFilter(minValue,maxValue);
                minValueChoose = minValue;
                maxValueChoose = maxValue;
            }
        });
        mLlApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogFilterWithSeekbar.this.onFilterListener.onClickApply(minValueChoose,maxValueChoose,filterCaoThap ? CAO_THAP : THAP_CAO);
                dismiss();
            }
        });
        ((RadioButton)findViewById(R.id.rb_thap_cao)).setChecked(true);
        filterCaoThap = false;
        mRGFilter.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.rb_cao_thap) {
                    filterCaoThap = true;
                } else  {
                    filterCaoThap = false;
                }
            }
        });
    }
    private void setTextPriceFilter(int min,int max) {
        String str = String.valueOf(min) + " - " + String.valueOf(max) + unit;
        if (min == minValue && max == maxValue) {
            str = "Tất cả";
        } else if (max == maxValue) {
            str = String.valueOf(min) + " "+unit+" trở lên";
        } else if (min == minValue) {
            str = "Dưới " + String.valueOf(max) +" " + unit;
        }
        mTvPriceFilter.setText(str);
    }
    public interface OnFilterListener {
        void onClickApply(double min,double max,int filter);
    }
}
