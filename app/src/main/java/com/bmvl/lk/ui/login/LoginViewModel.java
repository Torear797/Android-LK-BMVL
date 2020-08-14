package com.bmvl.lk.ui.login;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.Patterns;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bmvl.lk.App;
import com.bmvl.lk.R;
import com.bmvl.lk.Rest.AnswerOrderNew;
import com.bmvl.lk.Rest.NetworkService;
import com.bmvl.lk.Rest.UserInfo.OrderInfo;
import com.bmvl.lk.Rest.UserInfo.UserAccess;
import com.bmvl.lk.ui.settings.SettingItemActivity;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Objects;

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

    public void login(String username, String password, String device_id, final Context context, String Firebase_id) {

        NetworkService.getInstance()
                .getJSONApi()
                .login(username, password, device_id, true, Firebase_id)
                .enqueue(new Callback<UserAccess>() {
                    @Override
                    public void onResponse(@NonNull Call<UserAccess> call, @NonNull Response<UserAccess> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            switch (response.body().getStatus()) {
                                case 200:
                                  //  App.UserAccessData.setToken(response.body().getToken());
                                    getOrderNewInfo(response.body(), context);
                                    break;
                                case 403:
                                    //  if (response.body().isNeedChangePassword()) {
                                    loginResult.setValue(new LoginResult(context.getString(R.string.accountNotActive)));
//                                        Intent intent = new Intent(context, SettingItemActivity.class);
//                                        intent.putExtra("type_id", (byte) 2);
//                                        intent.putExtra("name", R.string.change_password_desc);
//                                        intent.putExtra("needChangePassword", true);
//                                        context.startActivity(intent);
                                    //  }
                                    break;
                                default:
                                    loginResult.setValue(new LoginResult(context.getString(R.string.auth_error)));
                                    break;
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

    private void getOrderNewInfo(final UserAccess accessData, final Context context) {
        NetworkService.getInstance()
                .getJSONApi()
                .OrderNew(accessData.getToken())
                .enqueue(new Callback<AnswerOrderNew>() {
                    @Override
                    public void onResponse(@NonNull Call<AnswerOrderNew> call, @NonNull Response<AnswerOrderNew> response) {
                        if (response.isSuccessful() && response.body() != null) {

                            switch (response.body().getStatus()) {
                                case 200: {
                                    if (!response.body().getDefaultFields().get((short) 52).equals(""))
                                        loginResult.setValue(new LoginResult(new LoggedInUserView(accessData, response.body().getUserInfo(), new OrderInfo((short) 52, response.body().getDefaultFields().get((short) 52), response.body().getFieldValues(), true))));
                                    else if (!response.body().getDefaultFields().get((short) 63).equals("") && !response.body().getDefaultFields().get((short) 64).equals(""))
                                        loginResult.setValue(new LoginResult(new LoggedInUserView(accessData, response.body().getUserInfo(), new OrderInfo((short) 64, response.body().getDefaultFields().get((short) 63), response.body().getDefaultFields().get((short) 64), response.body().getFieldValues()))));
                                    else if (!response.body().getDefaultFields().get((short) 63).equals(""))
                                        loginResult.setValue(new LoginResult(new LoggedInUserView(accessData, response.body().getUserInfo(), new OrderInfo((short) 63, response.body().getDefaultFields().get((short) 63), response.body().getFieldValues(), false))));
                                    break;
                                }
                                case 403: {
                                    if (response.body().isAccountNotActive())
                                        loginResult.setValue(new LoginResult(context.getString(R.string.accountNotActive)));
                                    else if (response.body().isNeedChangePassword()) {
                                        Intent intent = new Intent(context, SettingItemActivity.class);
                                        intent.putExtra("type_id", (byte) 2);
                                        intent.putExtra("name", R.string.change_password_desc);
                                        intent.putExtra("needChangePassword", true);
                                        intent.putExtra("token", accessData.getToken());
                                        context.startActivity(intent);
                                        loginResult.setValue(null);
                                    }
                                    break;
                                }
                            }

                        } else
                            loginResult.setValue(new LoginResult(context.getString(R.string.auth_error)));
                    }

                    @Override
                    public void onFailure(@NonNull Call<AnswerOrderNew> call, @NonNull Throwable t) {
                        Log.d("TAG", "MSG", t);
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
            if (username.length() == 16)
                return Patterns.PHONE.matcher(username).matches();
            return false;
        }
    }

    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 1;
    }
}