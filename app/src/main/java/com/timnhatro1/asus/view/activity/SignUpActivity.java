package com.timnhatro1.asus.view.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


import com.timnhatro1.asus.Constant;
import com.timnhatro1.asus.connect_database.connect_server.CommonCallBack;
import com.timnhatro1.asus.connect_database.connect_server.NetworkController;
import com.timnhatro1.asus.model.model_result_api.LoginResult;
import com.timnhatro1.asus.utils.DialogUtils;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class SignUpActivity extends MyBaseActivity implements View.OnClickListener {
    @BindView(R.id.cv_infor_1)
    CardView cvInfor1;
    @BindView(R.id.cv_infor_2)
    CardView cvInfor2;
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.et_email)
    TextInputEditText etEmail;
    @BindView(R.id.et_password)
    TextInputEditText etPassword;
    @BindView(R.id.et_full_name)
    TextInputEditText etFullName;
    @BindView(R.id.et_phone)
    TextInputEditText etPhone;
    SharedPreferences mPrefs;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_sign_up;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void initListener() {
        mIvBack.setOnClickListener(this);
    }

    @Override
    public void initLayout() {
        cvInfor1.setCardBackgroundColor(Color.TRANSPARENT);
        cvInfor1.setCardElevation(0);
        cvInfor2.setCardBackgroundColor(Color.TRANSPARENT);
        cvInfor2.setCardElevation(0);
        initListener();
        mPrefs = getSharedPreferences(Constant.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }
    private boolean validate() {
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches()) {
            Toast.makeText(this, this.getString(R.string.message_email_error), Toast.LENGTH_LONG).show();
            return false;
        }
        if (etPassword.getText().toString().length() < 6 ) {
            Toast.makeText(this, "Mật khẩu cần lớn hơn 6 kí tự", Toast.LENGTH_LONG).show();
            return false;
        }
        if (etFullName.getText().toString().isEmpty()) {
            Toast.makeText(this, "Bạn cần nhập tên hiển thị", Toast.LENGTH_LONG).show();
            return false;
        }
        if (etPhone.getText().toString().length() < 8) {
            Toast.makeText(this, "Số điện thoại chưa đúng định dạng", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
    @OnClick({R.id.sign_up_for_account})
    void onClicked(View view) {
        switch (view.getId()) {
            case R.id.sign_up_for_account:
                if (validate()) {
                    DialogUtils.showProgressDialog(this);
                    NetworkController.register(etEmail.getText().toString(), etPassword.getText().toString(),
                            etFullName.getText().toString(),etPhone.getText().toString(),new CommonCallBack<LoginResult>(SignUpActivity.this,false) {
                        @Override
                        public void onCallSuccess(Call<LoginResult> call, Response<LoginResult> response) {
                            DialogUtils.dismissProgressDialog();
                            if (response.body().getErrorCode() == 0 && response.body().getData()!=null) {
                                SharedPreferences.Editor prefsEditor = mPrefs.edit();
                                Gson gson = new Gson();
                                String json = gson.toJson(response.body().getData());
                                prefsEditor.putString(Constant.KEY_USER_MODEL, json);
                                prefsEditor.apply();
                                Toast.makeText(SignUpActivity.this, R.string.register_success,Toast.LENGTH_SHORT).show();
                                setResult(Activity.RESULT_OK);
                                finish();
                            } else {
                                Toast.makeText(SignUpActivity.this, response.body().getMessage(),Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onCallFailure(Call<LoginResult> call) {
                            DialogUtils.dismissProgressDialog();
                            Toast.makeText(SignUpActivity.this, R.string.register_error,Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                break;
        }
    }
}
