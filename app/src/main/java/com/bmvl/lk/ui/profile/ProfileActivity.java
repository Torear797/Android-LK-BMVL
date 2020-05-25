package com.bmvl.lk.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bmvl.lk.App;
import com.bmvl.lk.R;
import com.bmvl.lk.Rest.NetworkService;
import com.bmvl.lk.Rest.StandardAnswer;
import com.bmvl.lk.ui.MenuActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.orhanobut.hawk.Hawk;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Log.d("TOKEN: ", App.UserAccessData.getToken());

        final MaterialToolbar toolbar = findViewById(R.id.toolbar);
        final FloatingActionButton fab = findViewById(R.id.fab);

        final TextView FIO = findViewById(R.id.tvName);
        final TextView Email = findViewById(R.id.tvEmail);
        final TextView Phone = findViewById(R.id.tvPhone);
        final TextView Adress = findViewById(R.id.tvAdres);
        final TextView inn = findViewById(R.id.tvInn);
        final TextView bank_details = findViewById(R.id.tvBank);
        final TextView Contract_number = findViewById(R.id.tvNumContract);
        final TextView Contract_date = findViewById(R.id.tvContractData);

        toolbar.setTitle(R.string.profile);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setIcon(R.drawable.logo);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        FIO.setText(App.UserInfo.getFIO());
        Email.setText(App.UserInfo.getEmail());
        Phone.setText(App.UserInfo.getPhone());
        Adress.setText(App.UserInfo.getAdress());
//        Phone.setText("8 999 856 42 14");
//        Adress.setText("ул. Есенина 22");
        inn.setText(App.UserInfo.getInn());
        bank_details.setText(App.UserInfo.getBank_details());
        Contract_number.setText(App.UserInfo.getContract_number());
        Contract_date.setText(App.UserInfo.getContract_date());

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                NetworkService.getInstance()
                        .getJSONApi()
                        .logout(App.UserAccessData.getToken(), Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID))
                        .enqueue(new Callback<StandardAnswer>() {
                            @Override
                            public void onResponse(@NonNull Call<StandardAnswer> call, @NonNull Response<StandardAnswer> response) {
                                if (response.isSuccessful()) {
                                    StandardAnswer answer = response.body();
                                    assert answer != null;
                                    if (answer.getStatus() == 200) {
                                        Hawk.deleteAll();

                                        Intent intent = new Intent(ProfileActivity.this, MenuActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.putExtra("finish", true);
                                        startActivity(intent);
                                        finish();
                                    } else
                                        Snackbar.make(view, "Ошибка: " + answer.getText(), Snackbar.LENGTH_LONG)
                                                .setAction("Action", null).show();
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<StandardAnswer> call, @NonNull Throwable t) {
                                Snackbar.make(view, "Ошибка доступа к Серверу!", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                        });
            }
        });
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