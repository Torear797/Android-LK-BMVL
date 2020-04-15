package com.bmvl.lk;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.bmvl.lk.Rest.UserAccess;
import com.bmvl.lk.data.model.LoggedInUser;
import com.orhanobut.hawk.Hawk;

public class App extends Application {
    public static UserAccess UserAccessData = null;
    public static LoggedInUser UserInfo = null;

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
        if (netInfo != null && netInfo.isConnectedOrConnecting())
        {
            return true;
        }
        return false;
    }

    public static void  setUserData(UserAccess Access, LoggedInUser Data){
        UserAccessData = Access;
        UserInfo = Data;
    }
}
