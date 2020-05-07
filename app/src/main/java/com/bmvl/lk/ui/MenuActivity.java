package com.bmvl.lk.ui;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bmvl.lk.R;
import com.bmvl.lk.data.OnBackPressedListener;
import com.bmvl.lk.ui.Notification.NoticeFragment;
import com.bmvl.lk.ui.login.LoginActivity;
import com.bmvl.lk.ui.order.OrderFragment;
import com.bmvl.lk.ui.profile.ProfileActivity;
import com.bmvl.lk.ui.search.SearchFragment;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MenuActivity extends AppCompatActivity {
    private  int CurrentPage = R.id.navigation_order;

    private MaterialToolbar MenuToolbar;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            if(CurrentPage == item.getItemId()) {
                return false;
            } else {
                CurrentPage = item.getItemId();
                switch (item.getItemId()) {
                    case R.id.navigation_order:
                        MenuToolbar.setTitle(R.string.order);
                        loadFragment(OrderFragment.newInstance());
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
                    Intent intent = new Intent(MenuActivity.this, ProfileActivity.class);
                    startActivity(intent);
                    break;
            }
            return true;
    }
    public void End(){
        finish();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        OnBackPressedListener backPressedListener = null;
        for (Fragment fragment: fm.getFragments()) {
            if (fragment instanceof  OnBackPressedListener) {
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
            if (MenuToolbar != null) {
                MenuToolbar.setTitle(R.string.order);
                setSupportActionBar(MenuToolbar);
            }

            BottomNavigationView navigation = findViewById(R.id.nav_view);
            navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

            loadFragment(OrderFragment.newInstance());
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ab_buttons, menu);
        return true;
    }
}