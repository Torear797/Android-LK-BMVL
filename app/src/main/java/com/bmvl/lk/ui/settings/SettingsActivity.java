package com.bmvl.lk.ui.settings;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import com.bmvl.lk.R;
import com.bmvl.lk.data.SpacesItemDecoration;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {
    private RecyclerView SettingsList;
    private List<SettingsGroup> SettingsFields = new ArrayList<>();
    private SettingsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        final MaterialToolbar toolbar = findViewById(R.id.toolbar);

        toolbar.setTitle(R.string.settings);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        SettingsList = findViewById(R.id.RecyclerView);

        InitList();
        initRecyclerView();
    }

    private void InitList() {
        SettingsFields.add(new SettingsGroup((byte) 0, R.drawable.ic_baseline_notifications_24, R.color.orange, R.string.Notice, R.string.desc_notify));
        SettingsFields.add(new SettingsGroup((byte) 1, R.drawable.ic_baseline_work_24, Color.BLUE, R.string.orig_doc, R.string.orig_doc_desc));
        SettingsFields.add(new SettingsGroup((byte) 2, R.drawable.ic_baseline_security_24, Color.DKGRAY, R.string.change_password, R.string.change_password_desc));
    }

    private void initRecyclerView() {
        SettingsList.addItemDecoration(new SpacesItemDecoration((byte) 10, (byte) 5));
        SettingsList.setItemAnimator(new DefaultItemAnimator());
        SettingsList.setHasFixedSize(true);

        SettingsAdapter.OnClickListener onClickListener = group -> {
            Intent intent = new Intent(SettingsActivity.this, SettingItemActivity.class);
            intent.putExtra("type_id", group.getId());
            if (group.getId() != (byte) 2)
                intent.putExtra("name", group.getName());
            else
                intent.putExtra("name", group.getDescription());
            startActivity(intent);
        };

        adapter = new SettingsAdapter(SettingsFields, onClickListener);
        SettingsList.setAdapter(adapter);
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
}