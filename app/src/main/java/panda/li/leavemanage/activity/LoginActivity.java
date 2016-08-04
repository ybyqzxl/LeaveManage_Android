package panda.li.leavemanage.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import panda.li.leavemanage.R;
import panda.li.leavemanage.activity.Base.BaseActivity;
import panda.li.leavemanage.constans.Constants;
import panda.li.leavemanage.entity.AccountManager;
import panda.li.leavemanage.entity.LoginConfig;
import panda.li.leavemanage.entity.LoginUserInfo;
import panda.li.leavemanage.entity.UserWrapped;
import panda.li.leavemanage.service.LoginService;
import panda.li.leavemanage.service.Respon;
import panda.li.leavemanage.service.RetroFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by xueli on 2016/7/11.
 */
public class LoginActivity extends BaseActivity {

    @Bind(R.id.username)
    EditText et_username;
    @Bind(R.id.password)
    EditText et_password;
    @Bind(R.id.til_username)
    TextInputLayout til_Username;
    @Bind(R.id.til_pwd)
    TextInputLayout til_Pwd;
    @Bind(R.id.ll_login)
    LinearLayout llLogin;
    private String userName;
    private String userPwd;
    private LoginConfig mLoginConfig;
    private AccountManager mAccountManager;
    private long mExitTime;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        checkIsLogin();
    }

    private void checkIsLogin() {
        mAccountManager = AccountManager.getInstance();
        mLoginConfig = mAccountManager.getLoginConfig();
        if (mLoginConfig.isLogin()) {
            Intent intent = new Intent(LoginActivity.this,
                    MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @OnClick(R.id.btn_login)
    public void onClick() {
        login();
    }

    private void login() {

        if (!validate()) {
            return;
        }
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("正在登陆");
        progressDialog.show();
        progressDialog.setCancelable(false);
        checkLogin();
    }


    private void checkLogin() {

        LoginService.Login login = RetroFactory.getRetrofit(Constants.LOGIN_URL).create
                (LoginService.Login.class);
        Call<Respon> call = login.login(userName, userPwd);
        call.enqueue(new Callback<Respon>() {
            @Override
            public void onResponse(Call<Respon> call, Response<Respon> response) {
                String resp = response.body().getResponState();
                if (resp.trim().equals(Constants.HttpResponseState
                        .REQ_VERIFY_SUCCESS.toString())) {
                    loadUserInfo(mLoginConfig);

                } else if (resp.trim()
                        .equals(Constants.HttpResponseState.REQ_TIMEOUT.name())) {
                    Snackbar.make(llLogin, "请求超时", Snackbar.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                } else if (resp.trim()
                        .equals(Constants.HttpResponseState.REQ_USERNAME_ERR
                                .name())) {
                    Snackbar.make(llLogin, "用户名错误", Snackbar.LENGTH_SHORT).show();
                    et_username.setText("");
                    et_password.setText("");
                    progressDialog.dismiss();
                } else if (resp.trim()
                        .equals(Constants.HttpResponseState.REQ_USERPWD_ERR
                                .name())) {
                    Snackbar.make(llLogin, "密码错误", Snackbar.LENGTH_SHORT).show();
                    et_password.setText("");
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<Respon> call, Throwable t) {
                Snackbar.make(llLogin, "登录失败", Snackbar.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private void loadUserInfo(final LoginConfig loginConfig) {
        LoginService.LoadUserInfo loadUserInfo = RetroFactory.getRetrofit(Constants
                .LOAD_USER_INFO_URL).create(LoginService.LoadUserInfo.class);
        Call<LoginUserInfo> call = loadUserInfo.loadUserInfo(userName);
        call.enqueue(new Callback<LoginUserInfo>() {
            @Override
            public void onResponse(Call<LoginUserInfo> call, Response<LoginUserInfo> response) {
                loginConfig.setUserName(userName);
                loginConfig.setUserPwd(userPwd);
                loginConfig.setLogin(true);
                UserWrapped userWrapped = response.body().getUserWrapped();
                loginConfig.setUserId(userWrapped.getId());
                loginConfig.setDeptId(userWrapped.getDeptId());
                loginConfig.setUserRealName(userWrapped.getUserName());
                loginConfig.setUserDept(userWrapped.getDept());
                loginConfig.setUserGender(userWrapped.getUserGender());
                loginConfig.setUserType(userWrapped.getUserType());
                loginConfig.setRoleList(userWrapped.getRoleList());
                mAccountManager.saveLoginConfig(loginConfig);
                onLoginSuccess();
            }

            @Override
            public void onFailure(Call<LoginUserInfo> call, Throwable t) {
                Snackbar.make(llLogin, "登录失败", Snackbar.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private void onLoginSuccess() {
        progressDialog.dismiss();
        Intent intent = new Intent(LoginActivity.this,
                MainActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean validate() {
        boolean valid = true;
        String useCode = et_username.getText().toString();
        String password = et_password.getText().toString();

        if (useCode.isEmpty()) {
            til_Username.setError("请输入用户名");
            valid = false;
        } else {
            til_Username.setErrorEnabled(false);
            userName = useCode;
        }
        if (password.isEmpty()) {
            til_Pwd.setError("请输入密码");
            valid = false;
        } else {
            til_Pwd.setErrorEnabled(false);
            userPwd = password;
        }
        return valid;
    }

    /**
     * 双击返回退出
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Snackbar.make(llLogin, "再按一次退出程序", Snackbar.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
