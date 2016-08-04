package panda.li.leavemanage.constans;

import java.util.Date;

public class Constants {

    public enum HttpResponseState {
        REQ_PARAMS_ERR, REQ_UNKNOW_ERR, REQ_SYSTEM_ERR, REQ_USERPWD_ERR, REGISTER_EXISTENCE,
        REGISTER_NO_USER, REQ_USERNAME_ERR, REQ_TIMEOUT, REQ_VERIFY_ERR, REQ_VERIFY_SUCCESS,
        REQ_SUCCESS, REGISTER_SUCCESS, ALREADY_SGINED, ALREADY_APPLY;
        public static final String HTTP_RESPONSE_RESULT = "responResult";
        public static final String HTTP_RESPONSE_STATE = "responState";
    }

    public final static String SHARED_PREFERENCE_ACCOUNT = "SHARED_PREFERENCE_ACCOUNT";
    public static final String SHARED_PREFERENCES_ALARM_PREF = "SHARED_PREFERENCES_ALARM_PREF";
    public static final String SHARED_PREFERENCES_COURSE_DATA = "SHARED_PREFERENCES_KEY_USER_DATA";


    public final static String LOGIN_CONFIG = "LOGIN_CONFIG";
    public static final String IS_LOGINED = "IS_LOGINED";
    public static final String USER_ROLE = "USER_ROLE";
    public static final String USER_NAME = "USER_NAME";
    public static final String USER_PWD = "USER_PWD";
    public static final String USER_REAL_NAME = "USER_REAL_NAME";
    public static final String USER_ID = "USER_ID";
    public static final String USER_DEPT = "USER_DEPT";
    public static final String USER_DEPT_ID = "USER_DEPT_ID";
    public static final String IS_SIGN = "IS_SIGN";
    public static final String USER_TYPE = "USER_TYPE";
    public static final String USER_GENDER = "USER_GENDER";
    public static final String IS_UPLOAD_USER_INFO = "IS_UPLOAD_USER_INFO";
    public static final String IS_UPLOAD_BLUETOOTH_INFO = "IS_UPLOAD_BLUETOOTH_INFO";

    // 常用requestcode
    public final static int TO_LOGIN_ACTIVITY = 1;

    public static final String LOGIN_URL = "http://iscs.hebut.edu.cn/SingleLoginService/ajax/";
    public static final String LOAD_USER_INFO_URL = "http://iscs.hebut.edu" +
            ".cn/SingleLoginService/ajax/";
    public static final String LEAVE_MANAGE_URL = "http://10.1.14.96:8080/LeaveManage/ajax/";


}
