package com.bmvl.lk.ui.settings;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bmvl.lk.R;
import com.bmvl.lk.ui.settings.fragments.ChangePasswordFragment;
import com.bmvl.lk.ui.settings.fragments.EditNotify.SettingNotifyFragment;
import com.bmvl.lk.ui.settings.fragments.SettingOriginalDocFragment;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.Objects;

public class SettingItemActivity extends AppCompatActivity {
    private byte setting_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setting_id = getIntent().getByteExtra("type_id", (byte)0);
        setContentView(R.layout.activity_setting_item);

        final MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(getIntent().getIntExtra("name", 0)));
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        switch (setting_id){
            case 0:
                loadFragment(SettingNotifyFragment.newInstance());
                break;
            case 1:
                loadFragment(SettingOriginalDocFragment.newInstance());
                break;
            case 2:
                loadFragment(ChangePasswordFragment.newInstance());
                break;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.Frame, fragment);
        ft.commit();
    }

}
