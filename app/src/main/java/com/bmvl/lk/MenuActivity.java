package com.bmvl.lk;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bmvl.lk.ui.order.OrderFragment;
import com.bmvl.lk.ui.people.PeopleFragment;
import com.bmvl.lk.ui.search.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;


public class MenuActivity extends AppCompatActivity {
    private  int CurrentPage = R.id.navigation_order;
    public static  ActionBar MyActionBar;
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
                        loadFragment(OrderFragment.newInstance());
                        return true;
                    case R.id.navigation_search:
                        loadFragment(SearchFragment.newInstance());
                        return true;
                    case R.id.navigation_people:
                        loadFragment(PeopleFragment.newInstance());
                        return true;
                }
            }
            return false;
        }
    };
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.notice:
                Toast.makeText(this, "Menu Item 1 selected", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.nav_host_fragment, fragment);
        ft.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        MyActionBar = (Objects.requireNonNull(this)).getSupportActionBar();

        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        BottomNavigationView navigation = findViewById(R.id.nav_view);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        loadFragment(OrderFragment.newInstance());
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ab_buttons, menu);
        return true;
    }
}
