package com.bmvl.lk.Rest;

import com.bmvl.lk.Rest.Notify.NotificationsAnswer;
import com.bmvl.lk.Rest.Order.AnswerSendOrder;
import com.bmvl.lk.Rest.Order.OrdersAnswer;
import com.bmvl.lk.Rest.UserInfo.UserAccess;
import com.bmvl.lk.Rest.UserInfo.UserInfoCall;
import com.bmvl.lk.data.models.Document;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ServerApi {
    @FormUrlEncoded
    @POST("/session/login")
    Call<UserAccess> login(
            @Field("login") String login,
            @Field("password") String password,
            @Field("device_id") String device_id,
            @Field("getToken") boolean getToken,
            @Field("firebase_id") String firebase_id);

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
    Call<AnswerSendOrder> SaveOrder(@Field("token") String token, @Field("order") String order);

    @FormUrlEncoded
    @POST("orders/saveProtocol")
    Call<ResponseBody> DownloadProtocol(@Field("token") String token, @Field("id") int id);

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

    @FormUrlEncoded
    @POST("ajax/getMaterialsTree")
    Call<List<Material>> getMaterials(@Field("token") String token);

    @Multipart
    @POST("orders/uploadAct")
    Call<StandardAnswer> UploadAct(
            @Part("token") RequestBody token,
            @Part("order_id") RequestBody order_id,
            @Part MultipartBody.Part act_of_selection
    );

    @FormUrlEncoded
    @POST("orders/search")
    Call<AnswerContactPersons> getContactPersons(@Field("token") String token);

    @FormUrlEncoded
    @POST("account/setDefaultFields")
    Call<StandardAnswer> setDefaultFields(
            @Field("token") String token,
            @Field("defaultFields") String defaultFields
    );

    @Multipart
    @POST("account/setDefaultFields")
    Call<StandardAnswer> setDefaultFieldsWithScanFile(
            @Part("token") RequestBody token,
            @Part("defaultFields") RequestBody defaultFields,
            @Part MultipartBody.Part doverennost
    );

    @FormUrlEncoded
    @POST("account/setNewPassword")
    Call<StandardAnswer> setNewPassword(
            @Field("token") String token,
            @Field("oldPassword") String oldPassword,
            @Field("newPassword") String newPassword,
            @Field("repeatNewPassword") String repeatNewPassword
    );

    @FormUrlEncoded
    @POST("account/saveAccount")
    Call<StandardAnswer> saveAccountEmail(
            @Field("token") String token,
            @Field("email") String value
    );

    @FormUrlEncoded
    @POST("account/saveAccount")
    Call<StandardAnswer> saveAccountPhone(
            @Field("token") String token,
            @Field("phone") String value
    );

    @FormUrlEncoded
    @POST("account/notificationSettings")
    Call<AnswerNotifySettings> getNotifySetting(@Field("token") String token);

    @FormUrlEncoded
    @POST("account/saveNotificationSettings")
    Call<StandardAnswer> saveNotificationSettings(@Field("token") String token, @Field("userNotifications") String userNotifications);

    @FormUrlEncoded
    @POST("resetPassword")
    Call<StandardAnswer> resetPassword(@Field("email") String email);

    @FormUrlEncoded
    @POST("ajax/getDocumentNames")
    Call<List<Document>> getDocumentNames(@Field("token") String token);

    @FormUrlEncoded
    @POST("ajax/getCountries")
    Call<AnswerCountries> getCountries(@Field("token") String token, @Field("query") String query);

    @FormUrlEncoded
    @POST("ajax/getRegions")
    Call<AnswerCountries> getRegions(@Field("token") String token, @Field("country_name") String country_name, @Field("query") String query);

    @FormUrlEncoded
    @POST("ajax/getDistricts")
    Call<AnswerCountries> getDistricts(@Field("token") String token, @Field("country_name") String country_name, @Field("region_name") String region_name, @Field("query") String query);

    @FormUrlEncoded
    @POST("orders/sendFileToEmail")
    Call<StandardAnswer> SendFileToEmail(@Field("token") String token, @Field("id") int id, @Field("Type") String Type);

    @FormUrlEncoded
    @POST("orders/sendFileToEmail")
    Call<StandardAnswer> SendFileToEmail(@Field("token") String token, @Field("id") int id, @Field("Type") String Type, @Field("probId") int probId);

    @FormUrlEncoded
    @POST("orders/downloadArchive")
    Call<ResponseBody> downloadArchive(@Field("token") String token, @Field("id") int id);
}