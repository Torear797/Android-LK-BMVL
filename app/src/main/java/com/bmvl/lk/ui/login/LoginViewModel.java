package com.bmvl.lk.ui.login;

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
import com.bmvl.lk.Rest.UserInfo.UserInfoCall;
import com.bmvl.lk.data.models.LoggedInUser;

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

    public void login(String username, String password, String device_id) {

        NetworkService.getInstance()
                .getJSONApi()
                .getTestUser(username, password, device_id, true)
                .enqueue(new Callback<UserAccess>() {
                    @Override
                    public void onResponse(@NonNull Call<UserAccess> call, @NonNull Response<UserAccess> response) {
                        if (response.isSuccessful()) {
                            //App.UserAccessData = response.body();
                            if (response.body().getUser_id() != null)
                                getUserInfo(response.body());

                            else { //Пользователь ввел не верный лог/пас
                                loginResult.setValue(new LoginResult(response.body().getText()));
                                // loginResult.setValue(new LoginResult("Не верный логин/пароль!"));
                            }
                        } else
                            loginResult.setValue(new LoginResult("Ошибка авторизации!"));
                    }

                    @Override
                    public void onFailure(@NonNull Call<UserAccess> call, @NonNull Throwable t) {
                        loginResult.setValue(new LoginResult("Сервер не доступен!"));
                    }
                });
    }

    private void getUserInfo(final UserAccess accessData) {
        NetworkService.getInstance()
                .getJSONApi()
                .getUserInfo(accessData.getToken())
                .enqueue(new Callback<UserInfoCall>() {
                    @Override
                    public void onResponse(@NonNull Call<UserInfoCall> call, @NonNull Response<UserInfoCall> response) {
                        if (response.isSuccessful()) {
                            //  App.UserInfo = response.body().getUserInfo();
                            assert response.body() != null;
                            getOriginalDocInfo(accessData, response.body().getUserInfo());
                        } else
                            loginResult.setValue(new LoginResult("Ошибка авторизации!"));
                    }

                    @Override
                    public void onFailure(@NonNull Call<UserInfoCall> call, @NonNull Throwable t) {
                        loginResult.setValue(new LoginResult("Сервер не доступен!"));
                    }
                });
    }

    private void getOriginalDocInfo(final UserAccess accessData, final LoggedInUser UserInfo) {
        NetworkService.getInstance()
                .getJSONApi()
                .OrderNew(accessData.getToken())
                .enqueue(new Callback<AnswerOrderNew>() {
                    @Override
                    public void onResponse(@NonNull Call<AnswerOrderNew> call, @NonNull Response<AnswerOrderNew> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {
                               // final List<PairData> defaultFields = response.body().getDefaultFields();
                                final Map<Short, String> defaultFields = response.body().getDefaultFields();

//                                for (PairData entry : defaultFields) {
//                                    if (!entry.getData().equals("")) {
//                                        loginResult.setValue(new LoginResult(new LoggedInUserView(accessData, UserInfo, new OrderInfo(entry.getId(), entry.getData()))));
//                                        break;
//                                    }
//                                }

                                for(Map.Entry<Short, String> entry: defaultFields.entrySet()){

                                    if(!entry.getValue().equals("")){
                                        loginResult.setValue(new LoginResult(new LoggedInUserView(accessData, UserInfo, new OrderInfo(entry.getKey(), entry.getValue()))));
                                        break;
                                    }
                                }

                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<AnswerOrderNew> call, @NonNull Throwable t) {
                        loginResult.setValue(new LoginResult("Сервер не доступен2!"));
                        Log.d("MAX","MSG",t);
                    }
                });
    }

    public void loginDataChanged(String username, String password) {
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
