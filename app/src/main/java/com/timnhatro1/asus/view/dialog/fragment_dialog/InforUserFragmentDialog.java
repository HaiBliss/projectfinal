package com.timnhatro1.asus.view.dialog.fragment_dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.timnhatro1.asus.Constant;
import com.timnhatro1.asus.model.model_result_api.LoginResult;
import com.timnhatro1.asus.view.activity.R;
import com.timnhatro1.asus.model.event_bus.EventBusUpdateUser;
import com.timnhatro1.asus.model.UserModel;
import com.timnhatro1.asus.connect_database.connect_server.CommonCallBack;
import com.timnhatro1.asus.connect_database.connect_server.NetworkController;
import com.timnhatro1.asus.utils.DialogUtils;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

@SuppressLint("ValidFragment")
public class InforUserFragmentDialog extends DialogFragment {
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.et_email)
    TextInputEditText mEtEmail;
    @BindView(R.id.et_full_name)
    TextInputEditText mEtFullName;
    @BindView(R.id.et_phone)
    TextInputEditText mEtPhone;
    @BindView(R.id.ll_confirm)
    View viewConfirm;
    @BindView(R.id.view_change_infor)
    View viewChangeInfor;
    @BindView(R.id.ll_change_infor)
    View mLLChangeInfor;
    @BindView(R.id.ll_accept)
    View mLLAccept;
    @BindView(R.id.ll_cancel)
    View mLLCancel;
    private UserModel userModel;

    @SuppressLint("ValidFragment")
    public InforUserFragmentDialog(UserModel userModel) {
        this.userModel = userModel;
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
        View view = inflater.inflate(R.layout.fragment_profile,container,false);
        ButterKnife.bind(this,view);
        initView();
        return view;
    }

    private void initView() {
        setDisableEditText();
        mEtPhone.setText(userModel.getPhone());
        mEtEmail.setText(userModel.getEmail());
        mEtFullName.setText(userModel.getFullName());
    }

    private void setDisableEditText() {
        mEtFullName.setEnabled(false);
        mEtPhone.setEnabled(false);
        mEtEmail.clearFocus();
        mEtFullName.clearFocus();
        mEtPhone.clearFocus();
    }
    private void setEnableEditText() {
        mEtFullName.setEnabled(true);
        mEtPhone.setEnabled(true);
    }

    @OnClick({R.id.iv_back,R.id.ll_change_infor,R.id.ll_accept,R.id.ll_cancel,R.id.ll_change_password})
    void onClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                dismiss();
                break;
            case R.id.ll_change_infor:
                viewChangeInfor.setVisibility(View.GONE);
                viewConfirm.setVisibility(View.VISIBLE);
                setEnableEditText();
                break;
            case R.id.ll_cancel:
                viewChangeInfor.setVisibility(View.VISIBLE);
                viewConfirm.setVisibility(View.GONE);
                initView();
                break;
            case R.id.ll_change_password:
                ChangePasswordDialogFragment dialog = new ChangePasswordDialogFragment(userModel.getEmail());
                dialog.show(getChildFragmentManager(),dialog.getTag());
                break;
            case R.id.ll_accept:
                if (validate()) {
                    viewChangeInfor.setVisibility(View.VISIBLE);
                    viewConfirm.setVisibility(View.GONE);
                    setDisableEditText();
                    DialogUtils.showProgressDialog(getActivity());
                    NetworkController.updateUser(mEtEmail.getText().toString(),mEtFullName.getText().toString().trim()
                            ,mEtPhone.getText().toString().trim(), new CommonCallBack<LoginResult>(getActivity(),false) {

                                @Override
                                public void onCallSuccess(Call<LoginResult> call, Response<LoginResult> response) {
                                    DialogUtils.dismissProgressDialog();
                                    SharedPreferences mPrefs = getActivity().getSharedPreferences(Constant.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
                                    if (response.body().getErrorCode() == 0 && response.body().getData()!=null) {
                                        SharedPreferences.Editor prefsEditor = mPrefs.edit();
                                        Gson gson = new Gson();
                                        String json = gson.toJson(response.body().getData());
                                        prefsEditor.putString(Constant.KEY_USER_MODEL, json);
                                        prefsEditor.apply();
                                        Toast.makeText(getActivity(), "Thay đổi thông tin thành công!",Toast.LENGTH_SHORT).show();
                                        EventBus.getDefault().post(new EventBusUpdateUser(response.body().getData()));
                                    } else {
                                        Toast.makeText(getActivity(), response.body().getMessage(),Toast.LENGTH_SHORT).show();
                                        initView();
                                    }
                                }

                                @Override
                                public void onCallFailure(Call<LoginResult> call) {
                                    DialogUtils.dismissProgressDialog();
                                    initView();
                                    Toast.makeText(getActivity(), R.string.error_fail_default,Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                //save to server here
                break;
        }
    }

    private boolean validate() {
        if ( mEtFullName.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(),"Tên hiển thị không được để trống",Toast.LENGTH_SHORT).show();
            return false;
        }
        if (mEtPhone.getText().toString().length() < 9) {
            Toast.makeText(getActivity(),"Số điện thoại chưa đúng định dạng",Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
