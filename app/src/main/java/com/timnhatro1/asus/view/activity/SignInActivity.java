package com.timnhatro1.asus.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.timnhatro1.asus.Constant;
import com.timnhatro1.asus.model.model_result_api.LoginResult;
import com.timnhatro1.asus.model.model_result_api.SimpleResult;
import com.timnhatro1.asus.view.dialog.DialogAddNote;
import com.timnhatro1.asus.connect_database.connect_server.CommonCallBack;
import com.timnhatro1.asus.connect_database.connect_server.NetworkController;
import com.timnhatro1.asus.utils.DialogUtils;
import com.timnhatro1.asus.utils.NetworkUtils;
import com.timnhatro1.asus.utils.StringUtils;
import com.google.gson.Gson;
import com.timnhatro1.asus.view.dialog.DialogFindPassword;

import retrofit2.Call;
import retrofit2.Response;


public class SignInActivity  extends MyBaseActivity {

    private ProgressBar progress_bar;
    private FloatingActionButton fab;
    private View parent_view;
    private TextInputEditText mEtAccount;
    private TextInputEditText mEtPassword;
    private SharedPreferences mPrefs;
    @Override
    public void initLayout() {
        //        Tools.setSystemBarColor(this, R.color.blue_900);
//        Tools.setSystemBarLight(this);
        mPrefs = getSharedPreferences(Constant.SHARED_PREFERENCE_NAME, MODE_PRIVATE);
        parent_view = findViewById(android.R.id.content);
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        ((View) findViewById(R.id.sign_up_for_account)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this,SignUpActivity.class);
                startActivityForResult(intent,Constant.LOGIN_CODE);
            }
        });
        mEtAccount = findViewById(R.id.et_account);
        mEtPassword = findViewById(R.id.et_password);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                searchAction();
            }
        });
        findViewById(R.id.tv_find_password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFindPassword dialog = new DialogFindPassword(SignInActivity.this, new DialogAddNote.OnFilterListener() {
                    @Override
                    public void onClickApply(String note) {
                        if (!note.isEmpty()) {
                            DialogUtils.showProgressDialog(SignInActivity.this);
                            NetworkController.findPassword(note, new CommonCallBack<SimpleResult>(SignInActivity.this,false) {
                                @Override
                                public void onCallSuccess(Call<SimpleResult> call, Response<SimpleResult> response) {
                                    DialogUtils.dismissProgressDialog();
                                    if (response.body().getErrorCode() == 0) {
                                        Toast.makeText(SignInActivity.this, "Mật khẩu hiện tại của bạn đã được gửi vào email!",Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(SignInActivity.this, response.body().getMessage(),Toast.LENGTH_SHORT).show();
                                    }

                                }

                                @Override
                                public void onCallFailure(Call<SimpleResult> call) {
                                    DialogUtils.dismissProgressDialog();
                                    Toast.makeText(SignInActivity.this, R.string.error_fail_default,Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                },"Lấy lại mật khẩu","Xác nhận");
                dialog.show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.LOGIN_CODE && resultCode == RESULT_OK) {
            setResult(Activity.RESULT_OK);
            finish();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }


    private void searchAction() {
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                progress_bar.setVisibility(View.GONE);
//                fab.setAlpha(1f);
//                Snackbar.make(parent_view, "Login data submitted", Snackbar.LENGTH_SHORT).show();
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        Intent intent = new Intent(SignInActivity.this,MainActivity.class);
//                        startActivity(intent);
//                        finish();
//                    }
//                },1000);
//            }
//        }, 1000);
        if (StringUtils.isEmpty(mEtAccount.getText().toString())) {
            Toast.makeText(this, "Tài khoản đăng nhập không được để trống!",Toast.LENGTH_SHORT).show();
            return;
        }
        if (StringUtils.isEmpty( mEtPassword.getText().toString())) {
            Toast.makeText(this, "Mật khẩu đăng nhập không được để trống!",Toast.LENGTH_SHORT).show();
            return;
        }
        if (!NetworkUtils.isConnect(this)) {
            Toast.makeText(this, R.string.msg_network_not_available,Toast.LENGTH_SHORT).show();
            return;
        }
        progress_bar.setVisibility(View.VISIBLE);
        fab.setAlpha(0f);
        DialogUtils.showProgressDialog(this);
        NetworkController.login(mEtAccount.getText().toString(), mEtPassword.getText().toString(), new CommonCallBack<LoginResult>(SignInActivity.this,false) {
            @Override
            public void onCallSuccess(Call<LoginResult> call, Response<LoginResult> response) {
                DialogUtils.dismissProgressDialog();
                progress_bar.setVisibility(View.GONE);
                fab.setAlpha(1f);
                if (response.body().getErrorCode() == 0 && response.body().getData()!=null) {
                    SharedPreferences.Editor prefsEditor = mPrefs.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(response.body().getData());
                    prefsEditor.putString(Constant.KEY_USER_MODEL, json);
                    prefsEditor.apply();
                    Toast.makeText(SignInActivity.this, R.string.login_success,Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            setResult(Activity.RESULT_OK);
                            finish();
                        }
                    },100);
                } else {
                    Toast.makeText(SignInActivity.this, response.body().getMessage(),Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCallFailure(Call<LoginResult> call) {
                progress_bar.setVisibility(View.GONE);
                fab.setAlpha(1f);
                DialogUtils.dismissProgressDialog();
                Toast.makeText(SignInActivity.this, R.string.login_error,Toast.LENGTH_SHORT).show();
            }
        });
    }
}
