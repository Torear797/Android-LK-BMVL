package com.bmvl.lk.ui.login;

import android.content.Context;
import android.util.Log;
import android.util.Patterns;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bmvl.lk.R;
import com.bmvl.lk.Rest.AnswerOrderNew;
import com.bmvl.lk.Rest.NetworkService;
import com.bmvl.lk.Rest.UserInfo.OrderInfo;
import com.bmvl.lk.Rest.UserInfo.UserAccess;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();

    LoginViewModel() {
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password, String device_id, final Context context) {
        NetworkService.getInstance()
                .getJSONApi()
                .getTestUser(username, password, device_id, true)
                .enqueue(new Callback<UserAccess>() {
                    @Override
                    public void onResponse(@NonNull Call<UserAccess> call, @NonNull Response<UserAccess> response) {
                        if (response.isSuccessful()) {
                            //App.UserAccessData = response.body();
                            assert response.body() != null;
                            if (response.body().getUser_id() != null)
                                // getUserInfo(response.body(),context);
                                getOrderNewInfo(response.body(), context);

                            else { //Пользователь ввел не верный лог/пас
                                loginResult.setValue(new LoginResult(response.body().getText()));
                                // loginResult.setValue(new LoginResult("Не верный логин/пароль!"));
                            }
                        } else
                            loginResult.setValue(new LoginResult(context.getString(R.string.auth_error)));
                    }

                    @Override
                    public void onFailure(@NonNull Call<UserAccess> call, @NonNull Throwable t) {
                        loginResult.setValue(new LoginResult(context.getString(R.string.server_lost)));
                    }
                });
    }

//    private void getUserInfo(final UserAccess accessData, final Context context) {
//        NetworkService.getInstance()
//                .getJSONApi()
//                .getUserInfo(accessData.getToken())
//                .enqueue(new Callback<UserInfoCall>() {
//                    @Override
//                    public void onResponse(@NonNull Call<UserInfoCall> call, @NonNull Response<UserInfoCall> response) {
//                        if (response.isSuccessful()) {
//                            //  App.UserInfo = response.body().getUserInfo();
//                            assert response.body() != null;
//                           // getOrderNewInfo(accessData);
//                        } else
//                            loginResult.setValue(new LoginResult(context.getString(R.string.auth_error)));
//                    }
//
//                    @Override
//                    public void onFailure(@NonNull Call<UserInfoCall> call, @NonNull Throwable t) {
//                        loginResult.setValue(new LoginResult(context.getString(R.string.server_lost)));
//                    }
//                });
//    }

    private void getOrderNewInfo(final UserAccess accessData, final Context context) {
        NetworkService.getInstance()
                .getJSONApi()
                .OrderNew(accessData.getToken())
                .enqueue(new Callback<AnswerOrderNew>() {
                    @Override
                    public void onResponse(@NonNull Call<AnswerOrderNew> call, @NonNull Response<AnswerOrderNew> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {
                                for (Map.Entry<Short, String> entry : response.body().getDefaultFields().entrySet()) {

                                    if (!entry.getValue().equals("")) {
                                        loginResult.setValue(new LoginResult(new LoggedInUserView(accessData, response.body().getUserInfo(), new OrderInfo(entry.getKey(), entry.getValue(), response.body().getFieldValues()))));
                                        break;
                                    }
                                }

                            } else
                                loginResult.setValue(new LoginResult(context.getString(R.string.auth_error)));
                        }
                        else
                            loginResult.setValue(new LoginResult(context.getString(R.string.auth_error)));
                    }

                    @Override
                    public void onFailure(@NonNull Call<AnswerOrderNew> call, @NonNull Throwable t) {
                        Log.d("TAG","MSG",t);
                        loginResult.setValue(new LoginResult(context.getString(R.string.server_lost)));
                    }
                });
    }

    void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            if (username.length() == 11)
                return Patterns.PHONE.matcher(username).matches();
            // return !username.trim().isEmpty();
            return false;
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}