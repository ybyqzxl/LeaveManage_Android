package panda.li.leavemanage.entity;

import java.util.List;

public class LoginConfig {

    private String userId;
    private String deptId;
    private boolean isLogin;
    private List<String> roleList;
    private int userType;
    private String userName;
    private String userPwd;
    private String userDept;
    private String userRealName;
    private int userGender;
    private boolean isUploadUserInfo;
    private boolean isUploadBluetoothInfo;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean isLogin) {
        this.isLogin = isLogin;
    }

    public List<String> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<String> roleList) {
        this.roleList = roleList;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public String getUserDept() {
        return userDept;
    }

    public void setUserDept(String userDept) {
        this.userDept = userDept;
    }

    public String getUserRealName() {
        return userRealName;
    }

    public void setUserRealName(String userRealName) {
        this.userRealName = userRealName;
    }

    public int getUserGender() {
        return userGender;
    }

    public void setUserGender(int userGender) {
        this.userGender = userGender;
    }

    public boolean isUploadUserInfo() {
        return isUploadUserInfo;
    }

    public void setUploadUserInfo(boolean isUploadUserInfo) {
        this.isUploadUserInfo = isUploadUserInfo;
    }

    public boolean isUploadBluetoothInfo() {
        return isUploadBluetoothInfo;
    }

    public void setUploadBluetoothInfo(boolean isUploadBluetoothInfo) {
        this.isUploadBluetoothInfo = isUploadBluetoothInfo;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }


}
