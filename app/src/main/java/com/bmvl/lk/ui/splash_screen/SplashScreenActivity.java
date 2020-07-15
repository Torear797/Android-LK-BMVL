package com.bmvl.lk.ui.splash_screen;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bmvl.lk.App;
import com.bmvl.lk.Rest.CheckTokenAnswer;
import com.bmvl.lk.Rest.NetworkService;
import com.bmvl.lk.ui.MenuActivity;
import com.bmvl.lk.ui.login.LoginActivity;
import com.orhanobut.hawk.Hawk;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final boolean isAuth = isAuth();

        if (App.isOnline(this) && isAuth) CheckToken();
        else {
            //    Intent intent;

//            if (isAuth) intent = new Intent(getApplicationContext(), MenuActivity.class);
//            else intent = new Intent(this, LoginActivity.class);

            if (isAuth) startActivity(new Intent(getApplicationContext(), MenuActivity.class));
            else startActivity(new Intent(this, LoginActivity.class));

            //startActivity(intent);
            finish();
        }
    }

    private boolean isAuth() {
        App.UserInfo = Hawk.get("UserInfo", null);
        App.UserAccessData = Hawk.get("UserAccessData", null);
        App.OrderInfo = Hawk.get("OrderInfo", null);

        return App.UserInfo != null && App.UserAccessData != null && App.OrderInfo != null;
    }

    private void CheckToken() {
        NetworkService.getInstance()
                .getJSONApi()
                .CheckToken(App.UserAccessData.getToken())
                .enqueue(new Callback<CheckTokenAnswer>() {
                    @Override
                    public void onResponse(@NonNull Call<CheckTokenAnswer> call, @NonNull Response<CheckTokenAnswer> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200 && response.body().isActive())
                                startActivity(new Intent(getApplicationContext(), MenuActivity.class));
                            else
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));

                            finish();
                        } else GoToLogin();
                    }

                    @Override
                    public void onFailure(@NonNull Call<CheckTokenAnswer> call, @NonNull Throwable t) {
                        GoToLogin();
                    }
                });
    }

    private void GoToLogin() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }
}