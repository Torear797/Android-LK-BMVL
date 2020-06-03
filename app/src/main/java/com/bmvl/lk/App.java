package com.bmvl.lk;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.bmvl.lk.Rest.UserInfo.OrderInfo;
import com.bmvl.lk.Rest.UserInfo.UserAccess;
import com.bmvl.lk.data.models.LoggedInUser;
import com.orhanobut.hawk.Hawk;

public class App extends Application {
    public static UserAccess UserAccessData = null;
    public static LoggedInUser UserInfo = null;
    public static OrderInfo OrderInfo = null;

    @Override
    public void onCreate() {
        super.onCreate();
        Hawk.init(this).build();
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static void setUserData(UserAccess Access, LoggedInUser Data, OrderInfo Info) {
        UserAccessData = Access;
        UserInfo = Data;
        OrderInfo = Info;
    }
}