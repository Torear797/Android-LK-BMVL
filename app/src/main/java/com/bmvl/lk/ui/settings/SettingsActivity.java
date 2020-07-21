package com.bmvl.lk.ui.settings;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import com.bmvl.lk.App;
import com.bmvl.lk.R;
import com.bmvl.lk.Rest.AnswerOrderNew;
import com.bmvl.lk.Rest.NetworkService;
import com.bmvl.lk.Rest.UserInfo.OrderInfo;
import com.bmvl.lk.data.SpacesItemDecoration;
import com.bmvl.lk.ui.MenuActivity;
import com.bmvl.lk.ui.profile.ProfileActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
//            Intent intent = new Intent(SettingsActivity.this, SettingItemActivity.class);
//            intent.putExtra("type_id", group.getId());
//            if (group.getId() != (byte) 2)
//                intent.putExtra("name", group.getName());
//            else
//                intent.putExtra("name", group.getDescription());
//            startActivity(intent);
            UpdateOrderInfo(group);
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

    private void UpdateOrderInfo(SettingsGroup group) {
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

                                Intent intent = new Intent(SettingsActivity.this, SettingItemActivity.class);
                                intent.putExtra("type_id", group.getId());
                                if (group.getId() != (byte) 2)
                                    intent.putExtra("name", group.getName());
                                else
                                    intent.putExtra("name", group.getDescription());
                                startActivity(intent);

                            } else
                                Toast.makeText(getApplicationContext(), R.string.auth_error, Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(getApplicationContext(), R.string.auth_error, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(@NonNull Call<AnswerOrderNew> call, @NonNull Throwable t) {
                        Toast.makeText(getApplicationContext(), R.string.server_lost, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}