package com.bmvl.lk.ui.login;

import android.util.Patterns;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bmvl.lk.App;
import com.bmvl.lk.R;
import com.bmvl.lk.Rest.NetworkService;
import com.bmvl.lk.Rest.StandardAnswer;
import com.bmvl.lk.Rest.UserAccess;
import com.bmvl.lk.Rest.UserInfoCall;
import com.orhanobut.hawk.Hawk;

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
                                App.UserAccessData = response.body();
                                if (App.UserAccessData.getUser_id() != null)
                                    getUserInfo(App.UserAccessData.getToken());

                                else  { //Пользователь ввел не верный лог/пас
                                    loginResult.setValue(new LoginResult(App.UserAccessData.getError()));
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

    private void getUserInfo(String token){
        NetworkService.getInstance()
                .getJSONApi()
                .getUserInfo(token)
                .enqueue(new Callback<UserInfoCall>() {
                    @Override
                    public void onResponse(@NonNull Call<UserInfoCall> call, @NonNull Response<UserInfoCall> response) {
                        if (response.isSuccessful()) {
                            App.UserInfo = response.body().getUserInfo();
                            loginResult.setValue(new LoginResult(new LoggedInUserView(App.UserInfo.getFIO())));
                        } else
                            loginResult.setValue(new LoginResult("Ошибка авторизации!"));
                    }

                    @Override
                    public void onFailure(@NonNull Call<UserInfoCall> call, @NonNull Throwable t) {
                        loginResult.setValue(new LoginResult("Сервер не доступен!"));
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

    public void logout(String device_id){
        Hawk.deleteAll();


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
