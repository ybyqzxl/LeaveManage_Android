package panda.li.leavemanage.entity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;

import panda.li.leavemanage.activity.Base.BaseApp;
import panda.li.leavemanage.constans.Constants;
import panda.li.leavemanage.utils.JsonUtil;

public class AccountManager {
    private static AccountManager accountManager;

    private AccountManager() {
        super();
    }

    public static synchronized AccountManager getInstance() {
        if (accountManager == null) {
            accountManager = new AccountManager();
        }
        return accountManager;
    }

    public LoginConfig getLoginConfig() {
        LoginConfig loginConfig = new LoginConfig();
        SharedPreferences preferences = BaseApp.getInstance()
                .getSharedPreferences(Constants.SHARED_PREFERENCE_ACCOUNT,
                        Context.MODE_PRIVATE);
        loginConfig.setLogin(preferences
                .getBoolean(Constants.IS_LOGINED, false));
        loginConfig.setUploadUserInfo(preferences.getBoolean(
                Constants.IS_UPLOAD_USER_INFO, false));
        loginConfig.setUploadBluetoothInfo(preferences.getBoolean(
                Constants.IS_UPLOAD_BLUETOOTH_INFO, false));
        loginConfig.setUserId(preferences.getString(Constants.USER_ID, ""));
        loginConfig.setUserName(preferences.getString(Constants.USER_NAME, ""));
        loginConfig.setUserPwd(preferences.getString(Constants.USER_PWD, ""));
        loginConfig.setUserDept(preferences.getString(Constants.USER_DEPT, ""));
        loginConfig
                .setDeptId(preferences.getString(Constants.USER_DEPT_ID, ""));
        loginConfig.setUserRealName(preferences.getString(
                Constants.USER_REAL_NAME, ""));
        loginConfig.setUserType(preferences.getInt(Constants.USER_TYPE, 0));
        loginConfig.setUserGender(preferences.getInt(Constants.USER_GENDER, 0));

        List<String> roleList = new ArrayList<String>();
        String userRoleStr = preferences.getString(Constants.USER_ROLE, "");
        if (TextUtils.isEmpty(userRoleStr)) {
            loginConfig.setRoleList(roleList);
        } else {
            roleList = JsonUtil.toList(userRoleStr,
                    new TypeToken<List<String>>() {
                    });
            loginConfig.setRoleList(roleList);
        }
        return loginConfig;

    }

    public void saveLoginConfig(LoginConfig loginConfig) {
        SharedPreferences sharedPreferences = BaseApp.getInstance()
                .getSharedPreferences(Constants.SHARED_PREFERENCE_ACCOUNT,
                        Context.MODE_PRIVATE);
        sharedPreferences
                .edit()
                .putBoolean(Constants.IS_LOGINED, loginConfig.isLogin())
                .putBoolean(Constants.IS_UPLOAD_USER_INFO,
                        loginConfig.isUploadUserInfo())
                .putBoolean(Constants.IS_UPLOAD_BLUETOOTH_INFO,
                        loginConfig.isUploadBluetoothInfo())
                .putString(Constants.USER_DEPT, loginConfig.getUserDept())
                .putString(Constants.USER_DEPT_ID, loginConfig.getDeptId())
                .putString(Constants.USER_ID, loginConfig.getUserId())
                .putString(Constants.USER_NAME, loginConfig.getUserName())
                .putString(Constants.USER_PWD, loginConfig.getUserPwd())
                .putString(Constants.USER_REAL_NAME,
                        loginConfig.getUserRealName())
                .putInt(Constants.USER_TYPE, loginConfig.getUserType())
                .putInt(Constants.USER_GENDER, loginConfig.getUserGender())
                .putString(Constants.USER_ROLE,
                        JsonUtil.toJson(loginConfig.getRoleList())).commit();
    }

    public void logout() {
        SharedPreferences sharedPreferences = BaseApp.getInstance()
                .getSharedPreferences(Constants.SHARED_PREFERENCE_ACCOUNT,
                        Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().commit();
    }

    public SharedPreferences getPersonalSharedPref() {
        return BaseApp.getInstance().getSharedPreferences(
                AccountManager.getInstance().getLoginConfig().getUserName(),
                Context.MODE_PRIVATE);
    }

    public boolean isLogin() {
        LoginConfig loginConfig = getLoginConfig();
        if (loginConfig == null) {
            return false;
        } else {
            return loginConfig.isLogin();
        }
    }

    public List<String> getRoleList() {
        LoginConfig loginConfig = getLoginConfig();
        if (loginConfig == null) {
            return new ArrayList<String>();
        } else {
            return loginConfig.getRoleList();
        }
    }

    public boolean isContainRole(String role) {
        if (getRoleList() != null && getRoleList().size() != 0) {
            for (String userRole : getRoleList()) {
                if (userRole.contains(role)) {
                    return true;
                }
            }
        }
        return false;
    }

    public String getUserRealName() {
        String userRealName = null;
        LoginConfig loginConfig = getLoginConfig();
        if (loginConfig == null) {
            return userRealName;
        } else {
            return loginConfig.getUserRealName();
        }
    }
}
