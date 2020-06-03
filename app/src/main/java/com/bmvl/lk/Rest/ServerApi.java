package com.bmvl.lk.Rest;

import com.bmvl.lk.Rest.Notify.NotificationsAnswer;
import com.bmvl.lk.Rest.Order.AnswerSendOrder;
import com.bmvl.lk.Rest.Order.OrdersAnswer;
import com.bmvl.lk.Rest.Order.SendOrder;
import com.bmvl.lk.Rest.UserInfo.UserAccess;
import com.bmvl.lk.Rest.UserInfo.UserInfoCall;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ServerApi {
    @FormUrlEncoded
    @POST("/session/login")
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

    @FormUrlEncoded
    @POST("/orders")
    Call<OrdersAnswer> LoadOrders(@Field("token") String token, @Field("page") byte page);

    @FormUrlEncoded
    @POST("orders/create")
    Call<AnswerSendOrder> sendOrder(@Field("token") String token, @Field("orders") String order);

    @FormUrlEncoded
    @POST("orders/create")
    Call<ResponseBody> sendOrders(@Field("token") String token, @Field("orders") String order);

    @FormUrlEncoded
    @POST("orders/new")
    Call<AnswerOrderNew> OrderNew(@Field("token") String token);

    @FormUrlEncoded
    @POST("notifications/hide")
    Call<StandardAnswer> HideNotify(@Field("token") String token, @Field("id") int id);

    @FormUrlEncoded
    @POST("orders/delete")
    Call<StandardAnswer> DeleteOrder(@Field("token") String token, @Field("id") int id);

    @FormUrlEncoded
    @POST("orders/copyOrder")
    Call<AnswerCopyOrder> CopyOrder(@Field("token") String token, @Field("id") int id, @Field("pattern") byte pattern);

    @FormUrlEncoded
    @POST("orders/phpWord")
    Call<AnswerDownloadOrder> DOWNLOAD_ORDER_CALL(@Field("token") String token, @Field("id") int id);

    @FormUrlEncoded
    @POST("ajax/getIndicators")
    Call<AnswerIndicators> getIndicators(@Field("token") String token, @Field("materialId") int materialId, @Field("query") String query);

    @FormUrlEncoded
    @POST("ajax/getMethods")
    Call<AnswerMethods> getMethods(
            @Field("token") String token,
            @Field("query") String query,
            @Field("materialId") short materialId,
            @Field("indicatorId") short indicatorId,
            @Field("indicatorNdId") String indicatorNdId);

    @FormUrlEncoded
    @POST("ajax/getTypes")
    Call<AnswerTypes> getTypes(
            @Field("token") String token,
            @Field("query") String query,
            @Field("materialId") short materialId,
            @Field("indicatorId") short indicatorId,
            @Field("indicatorNdId") String indicatorNdId,
            @Field("methodId") short methodId,
            @Field("methodNdId") String methodNdId
    );

    @FormUrlEncoded
    @POST("orders/edit")
    Call<AnswerOrderEdit> getOrderInfo(@Field("token") String token, @Field("id") int id);

    @FormUrlEncoded
    @POST("orders/save")
    Call<StandardAnswer> SaveOrder(@Field("token") String token, @Field("order") String order);

    @FormUrlEncoded
    @POST("orders/save")
    Call<ResponseBody> DownloadProtocol(@Field("token") String token);

    @FormUrlEncoded
    @POST("session/checkTokenStatus")
    Call<CheckTokenAnswer> CheckToken(@Field("token_val") String token);

    @FormUrlEncoded
    @POST("orders/find")
    Call<AnswerSearch> Search(
            @Field("token") String token,
            @Field("id") String id,
            @Field("protocolNumber") String protocolNumber,
            @Field("date[from]") String date_from,
            @Field("date[to]") String date_to,
            @Field("contactPersons") String contactPersons,
            @Field("orderType") byte orderType,
            @Field("price[from]") String price_from,
            @Field("price[to]") String price_to,
            @Field("orderStatus") byte orderStatus,
            @Field("sort") String sort,
            @Field("limit") short limit,
            @Field("page") short page
    );

    @FormUrlEncoded
    @POST("orders/patterns")
    Call<AnswerPatterns> getPatterns(@Field("token") String token, @Field("standart") byte standart);
}