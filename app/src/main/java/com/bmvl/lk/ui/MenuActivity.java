package com.bmvl.lk.ui;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bmvl.lk.App;
import com.bmvl.lk.R;
import com.bmvl.lk.Rest.AnswerOrderNew;
import com.bmvl.lk.Rest.NetworkService;
import com.bmvl.lk.Rest.UserInfo.OrderInfo;
import com.bmvl.lk.data.OnBackPressedListener;
import com.bmvl.lk.ui.Notification.NoticeFragment;
import com.bmvl.lk.ui.login.LoginActivity;
import com.bmvl.lk.ui.order.OrderFragment;
import com.bmvl.lk.ui.patterns.PatternsMenu;
import com.bmvl.lk.ui.profile.ProfileActivity;
import com.bmvl.lk.ui.search.SearchFragment;
import com.bmvl.lk.ui.settings.SettingsActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.orhanobut.hawk.Hawk;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MenuActivity extends AppCompatActivity {
    private int CurrentPage;

    private MaterialToolbar MenuToolbar;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            if (CurrentPage == item.getItemId()) {
                return false;
            } else {
                CurrentPage = item.getItemId();
                switch (item.getItemId()) {
                    case R.id.navigation_order:
                        MenuToolbar.setTitle(R.string.order);
                        loadFragment(OrderFragment.newInstance());
                        return true;
                    case R.id.navigation_patterns:
                        MenuToolbar.setTitle(R.string.menu_patterns);
                        //  loadFragment(PatternsFragment.newInstance());
                        loadFragment(PatternsMenu.newInstance());
                        return true;
                    case R.id.navigation_search:
                        MenuToolbar.setTitle(R.string.search);
                        loadFragment(SearchFragment.newInstance());
                        return true;
                    case R.id.notice:
                        MenuToolbar.setTitle(R.string.Notice);
                        loadFragment(NoticeFragment.newInstance());
                        return true;
                }
            }
            return false;
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile:
               // Intent intent =;
                //startActivity(new Intent(MenuActivity.this, ProfileActivity.class));
                UpdateOrderInfo(item.getItemId());
                break;
            case R.id.settings:
               // Intent intentS = ;
              // UpdateOrderInfo(R.id.settings);
                startActivity(new Intent(MenuActivity.this, SettingsActivity.class));
                break;
        }
        //UpdateOrderInfo(item.getItemId());
        return true;
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        OnBackPressedListener backPressedListener = null;
        for (Fragment fragment : fm.getFragments()) {
            if (fragment instanceof OnBackPressedListener) {
                backPressedListener = (OnBackPressedListener) fragment;
                break;
            }
        }

        if (backPressedListener != null) {
            backPressedListener.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.nav_host_fragment, fragment);
        ft.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().getBooleanExtra("finish", false)) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            setContentView(R.layout.activity_menu);
            MenuToolbar = findViewById(R.id.toolbar);

            final BottomNavigationView navigation = findViewById(R.id.nav_view);
            navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
            navigation.setSelectedItemId(R.id.navigation_order);
            setSupportActionBar(MenuToolbar);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ab_buttons, menu);
        return true;
    }

    private void UpdateOrderInfo(int item) {
        NetworkService.getInstance()
                .getJSONApi()
                .OrderNew(App.UserAccessData.getToken())
                .enqueue(new Callback<AnswerOrderNew>() {
                    @Override
                    public void onResponse(@NonNull Call<AnswerOrderNew> call, @NonNull Response<AnswerOrderNew> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {

                                if (!response.body().getDefaultFields().get((short) 52).equals(""))
                                    App.OrderInfo = new OrderInfo((short) 52, response.body().getDefaultFields().get((short) 52), response.body().getFieldValues(), true);
                                else if (!response.body().getDefaultFields().get((short) 63).equals("") && !response.body().getDefaultFields().get((short) 64).equals(""))
                                    App.OrderInfo = new OrderInfo((short) 64, response.body().getDefaultFields().get((short) 63), response.body().getDefaultFields().get((short) 64), response.body().getFieldValues());
                                else if (!response.body().getDefaultFields().get((short) 63).equals(""))
                                    App.OrderInfo = new OrderInfo((short) 63, response.body().getDefaultFields().get((short) 63), response.body().getFieldValues(), false);

                                if (response.body().getDefaultFields().containsKey((short) 128) && !response.body().getDefaultFields().get((short) 128).equals(""))
                                    App.OrderInfo.setURL_SCAN_FILE(response.body().getDefaultFields().get((short) 128));

                                App.UserInfo = response.body().getUserInfo();
                                Hawk.put("UserInfo", App.UserInfo);
                                Hawk.put("OrderInfo", App.OrderInfo);

                                switch (item) {
                                    case R.id.profile:
                                        startActivity(new Intent(MenuActivity.this, ProfileActivity.class));
                                        break;
                                    case R.id.settings:
                                        startActivity(new Intent(MenuActivity.this, SettingsActivity.class));
                                        break;
                                }

                            } else {
                                Toast.makeText(getApplicationContext(), R.string.auth_error, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), R.string.auth_error, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<AnswerOrderNew> call, @NonNull Throwable t) {
                        Toast.makeText(getApplicationContext(), R.string.server_lost, Toast.LENGTH_SHORT).show();
                    }
                });
    }

}