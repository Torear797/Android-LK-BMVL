package com.bmvl.lk.ui.login;

import android.util.Patterns;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bmvl.lk.App;
import com.bmvl.lk.R;
import com.bmvl.lk.TestRest.NetworkService;
import com.bmvl.lk.TestRest.TestUser;
import com.bmvl.lk.data.LoginRepository;
import com.bmvl.lk.data.Result;
import com.bmvl.lk.data.model.LoggedInUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password, String device_id) {
        // can be launched in a separate asynchronous job
        final Result<LoggedInUser> result = loginRepository.login(username, password);

        if (result instanceof Result.Success) {
            NetworkService.getInstance()
                    .getJSONApi()
                    .getTestUser(username, password, device_id, true)
                    .enqueue(new Callback<TestUser>() {
                        @Override
                        public void onResponse(@NonNull Call<TestUser> call, @NonNull Response<TestUser> response) {
                            if (response.isSuccessful()) {
                                App.MyUser = response.body();

                                if (App.MyUser.getUser_id() != null) {

                                    LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
                                    loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));

                                } else  { //Пользователь ввел не верный лог/пас
                                  //  loginResult.setValue(new LoginResult(0));
                                    loginResult.setValue(new LoginResult("Не верный логин/пароль!"));
                                }
                            } else
                                loginResult.setValue(new LoginResult("Ошибка авторизации!"));
                           // loginResult.setValue(new LoginResult(1));
                        }

                        @Override
                        public void onFailure(@NonNull Call<TestUser> call, @NonNull Throwable t) {
                            loginResult.setValue(new LoginResult("Ошибка авторизации!"));
                           // loginResult.setValue(new LoginResult(2));
                        }
                    });

        } else {
            loginResult.setValue(new LoginResult("Логин или пароль введены не верно!"));
          //  loginResult.setValue(new LoginResult(3));
        }

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
