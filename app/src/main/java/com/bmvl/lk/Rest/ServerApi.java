package com.bmvl.lk.Rest;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ServerApi {
    @FormUrlEncoded
    @POST("/session/login")
    //  Call<TestUser> getTestUser(@Header("Content-Type") String content_type, @Body OutputData data);
//    Call<TestUser> getTestUser(
//            @Query("login") String login,
//            @Query("password") String password,
//            @Query("device_id") String device_id,
//            @Query("getToken") boolean getToken);

    Call<UserAccess> getTestUser(
            @Field("login") String login,
            @Field("password") String password,
            @Field("device_id") String device_id,
            @Field("getToken") boolean getToken);


    @FormUrlEncoded
    @POST("account/info")
    Call<UserInfoCall> getUserInfo(@Field("token") String token);

    @FormUrlEncoded
    @POST("session/logout")
    Call<StandardAnswer> logout(@Field("token") String token, @Field("device_id") String device_id);

    @FormUrlEncoded
    @POST("session/releaseToken")
    Call<UserAccess> releaseToken(@Field("token") String token, @Field("device_id") String device_id);

    @FormUrlEncoded
    @POST("notifications/get")
    Call<NotificationsAnswer> getNotificationsOnPage(@Field("token") String token, @Field("page") byte page);
}