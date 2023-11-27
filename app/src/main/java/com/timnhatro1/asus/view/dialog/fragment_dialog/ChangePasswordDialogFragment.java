package com.timnhatro1.asus.view.dialog.fragment_dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.timnhatro1.asus.model.model_result_api.SimpleResult;
import com.timnhatro1.asus.view.activity.R;
import com.timnhatro1.asus.view.dialog.DialogConfrim;
import com.timnhatro1.asus.connect_database.connect_server.CommonCallBack;
import com.timnhatro1.asus.connect_database.connect_server.NetworkController;
import com.timnhatro1.asus.utils.DialogUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

@SuppressLint("ValidFragment")
public class ChangePasswordDialogFragment extends DialogFragment {

    @BindView(R.id.et_old_password)
    EditText mEtOldPassword;
    @BindView(R.id.et_new_password)
    EditText mEtNewPassword;
    @BindView(R.id.et_repeat_password)
    EditText mEtRepeatPassword;
    @BindView(R.id.ll_change_infor)
    View mViewInfor;
    @BindView(R.id.iv_back)
    View ivBack;
    private String email;

    public ChangePasswordDialogFragment(String email) {
        this.email = email;
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
        View view = inflater.inflate(R.layout.dialog_change_password, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        mViewInfor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    DialogConfrim dialog = new DialogConfrim(getActivity(), "Xác nhận", "Bạn có chắc chắn muốn đổi mật khẩu?",
                            "Đồng ý", "Huỷ bỏ", new DialogConfrim.OnClickDialogTTC() {
                        @Override
                        public void onButonYesClick() {
                            DialogUtils.showProgressDialog(getActivity());
                            NetworkController.changePassword(email,mEtOldPassword.getText().toString(),mEtNewPassword.getText().toString(), new CommonCallBack<SimpleResult>(getActivity(),false) {
                                @Override
                                public void onCallSuccess(Call<SimpleResult> call, Response<SimpleResult> response) {
                                    DialogUtils.dismissProgressDialog();
                                    Toast.makeText(getActivity(),response.body().getMessage(),Toast.LENGTH_SHORT).show();
                                    if (response.body().getErrorCode() == 0) {
                                        ChangePasswordDialogFragment.this.dismiss();
                                    } else {

                                    }
                                }

                                @Override
                                public void onCallFailure(Call<SimpleResult> call) {
                                    DialogUtils.dismissProgressDialog();
                                    Toast.makeText(getActivity(),R.string.error_fail_default,Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onButtonNoClick() {

                        }

                        @Override
                        public void onButtonCloseClick() {

                        }
                    });
                    dialog.show();
                }

            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    private boolean validate() {
        if (mEtOldPassword.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(),"Nhập mật khẩu cũ",Toast.LENGTH_SHORT).show();
            mEtOldPassword.requestFocus();
            return false;
        }
        if (mEtNewPassword.getText().toString().length() < 6) {
            Toast.makeText(getActivity(),"Mật khẩu mới phải lớn hơn 6 kí tự",Toast.LENGTH_SHORT).show();
            mEtNewPassword.requestFocus();
            return false;
        }
        if (!mEtNewPassword.getText().toString().equals(mEtRepeatPassword.getText().toString())) {
            Toast.makeText(getActivity(),"Nhập lại mật khẩu mới chưa chính xác",Toast.LENGTH_SHORT).show();
            mEtRepeatPassword.requestFocus();
            return false;
        }
        return true;
    }

}