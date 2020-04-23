package com.bmvl.lk;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.InputType;

import androidx.recyclerview.widget.GridLayoutManager;

import com.bmvl.lk.Rest.UserAccess;
import com.bmvl.lk.data.models.LoggedInUser;
import com.bmvl.lk.ui.search.SearchField;
import com.bmvl.lk.ui.search.SearchFragment;
import com.orhanobut.hawk.Hawk;

public class App extends Application {
    public static UserAccess UserAccessData = null;
    public static LoggedInUser UserInfo = null;

    private GridLayoutManager mng_layout;
    //  App application = (App) getApplication();

    @Override
    public void onCreate() {
        super.onCreate();
        Hawk.init(this).build();
        // CreateSerchFields();

        CreateSerchFields();
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    public static void setUserData(UserAccess Access, LoggedInUser Data) {
        UserAccessData = Access;
        UserInfo = Data;
    }

    public void CreateSerchFields() {
        SearchFragment.Fields.clear();
        SearchFragment.Fields.add(new SearchField("", "Номер заявки", InputType.TYPE_CLASS_NUMBER));
        SearchFragment.Fields.add(new SearchField("", "Номер протокола", InputType.TYPE_CLASS_NUMBER));

        SearchFragment.Fields.add(new SearchField("", "От", InputType.TYPE_CLASS_NUMBER, getDrawable(R.drawable.ic_date_range_black_24dp), true));
        SearchFragment.Fields.add(new SearchField("", "До", InputType.TYPE_CLASS_NUMBER, getDrawable(R.drawable.ic_date_range_black_24dp), true));

        SearchFragment.Fields.add(new SearchField("", "Контактное лицо", InputType.TYPE_CLASS_TEXT, true));
        SearchFragment.Fields.add(new SearchField("", "Вид заказа", true, R.array.order_name, true));

        SearchFragment.Fields.add(new SearchField("", "От", InputType.TYPE_CLASS_NUMBER, getDrawable(R.drawable.rub), false));
        SearchFragment.Fields.add(new SearchField("", "До", InputType.TYPE_CLASS_NUMBER, getDrawable(R.drawable.rub), false));

        SearchFragment.Fields.add(new SearchField("", "Статус", true, R.array.order_statuses));
        SearchFragment.Fields.add(new SearchField("", "Порядок сортировки", true, R.array.sort_types));

    }
}
