package com.timnhatro1.asus.view.view_custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.timnhatro1.asus.view.activity.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewItemCluster  extends FrameLayout {
    @BindView(R.id.textView2)
    TextView textView;
    public ViewItemCluster(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    public ViewItemCluster(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ViewItemCluster(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        View view = inflate(getContext(), R.layout.item_cluster, null);
        ButterKnife.bind(this,view);
        addView(view);
    }
    public void setTextView(String text) {
        textView.setText(text);
    }
    public void setColorText(int colorText) {
        textView.setTextColor(colorText);
    }
    public void setBackground(int colorBackground) {
        textView.setBackgroundColor(colorBackground);
    }
}

