package com.bmvl.lk.TestRest;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ServerApi {
  //  @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST("/session/login")
  //  Call<TestUser> getTestUser(@Header("Content-Type") String content_type, @Body OutputData data);
//    Call<TestUser> getTestUser(
//            @Query("login") String login,
//            @Query("password") String password,
//            @Query("device_id") String device_id,
//            @Query("getToken") boolean getToken);

        Call<TestUser> getTestUser(
          @Field("login") String login,
          @Field("password") String password,
          @Field("device_id") String device_id,
          @Field("getToken") boolean getToken);
}
