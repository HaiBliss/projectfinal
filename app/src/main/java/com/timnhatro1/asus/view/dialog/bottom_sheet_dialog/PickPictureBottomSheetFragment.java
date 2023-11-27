package com.timnhatro1.asus.view.dialog.bottom_sheet_dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.View;
import android.widget.TextView;

import com.timnhatro1.asus.view.activity.R;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("ValidFragment")
public class PickPictureBottomSheetFragment extends BottomSheetDialogFragment {

    @BindView(R.id.btn_gallery)
    TextView mTvGallery;
    @BindView(R.id.btn_camera)
    TextView mTvCamera;
    private OnClickChoose onClickChoose;
    private BottomSheetBehavior mBehavior;

    public PickPictureBottomSheetFragment(OnClickChoose onClickChoose) {
        this.onClickChoose = onClickChoose;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        final View view = View.inflate(getContext(), R.layout.bottom_sheet_pick_picture, null);
        dialog.setContentView(view);
        ButterKnife.bind(this,view);
        mBehavior = BottomSheetBehavior.from((View) view.getParent());
        mTvGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if (onClickChoose!=null)
                    onClickChoose.onClickedGallery();
            }
        });
        mTvCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if (onClickChoose!=null)
                    onClickChoose.onClickedCamera();
            }
        });
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
//        mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }
    public interface OnClickChoose {
        void onClickedGallery();
        void onClickedCamera();
    }
}
