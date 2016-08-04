package panda.li.leavemanage.service;

import panda.li.leavemanage.entity.LoginUserInfo;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by xueli on 2016/7/19.
 */
public class LoginService {


    public interface Login {
        @FormUrlEncoded
        @POST("SingleLoginAction.action")
        Call<Respon> login(@Field("userName") String userName, @Field("userPwd") String userPwd);
    }

    public interface LoadUserInfo {
        @FormUrlEncoded
        @POST("LoadUserDetailAction.action")
        Call<LoginUserInfo> loadUserInfo(@Field("userName") String userName);
    }

    public interface LoadUserLeaveInfo {
        @FormUrlEncoded
        @POST("LoadMyLeaveInfo.action")
        Call<Respon> loadUserLeaveInfo(@Field("userCode") String userCode, @Field("time") Long
                time);
    }

    public interface LoadApprovalLeaveInfo {
        @GET("LoadApprovalLeaveInfo.action")
        Call<Respon> loadApprovalLeaveInfo(@Query("userCode") int approval, @Query("time") Long
                time);
    }

    public interface PublishUserLeaveInfo {
        @FormUrlEncoded
        @POST("PublishLeaveInfosAction.action")
        Call<Respon> publishUserLeaveInfo(@Field("userCode") String userCode, @Field("userName")
        String
                userName, @Field("userDepart") String userDepart, @Field("leaveWhy") String
                                                  leaveWhy, @Field("startTime") String startTime,
                                          @Field
                                                  ("endTime") String endTime,
                                          @Field("leaveInfo") String leaveInfo);
    }

    public interface ModifyApprovalLeaveInfo {
        @GET("ModifyApproval.action")
        Call<Respon> modifyApprovalLeaveInfo(@Query("id") long id, @Query("approval") int approval);
    }
}
