package com.bmvl.lk.ui.SplashScreen;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.bmvl.lk.App;
import com.bmvl.lk.MenuActivity;
import com.bmvl.lk.ui.login.LoginActivity;
import com.orhanobut.hawk.Hawk;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // Hawk.deleteAll();
        Intent intent;
        if (isAuth())
            intent = new Intent(this, MenuActivity.class);

        else intent = new Intent(this, LoginActivity.class);

        startActivity(intent);
        finish();

    }

    private boolean isAuth() {
        App.UserInfo = Hawk.get("UserInfo", null);
        App.UserAccessData = Hawk.get("UserAccessData", null);
        return App.UserInfo != null && App.UserAccessData != null;
    }
}