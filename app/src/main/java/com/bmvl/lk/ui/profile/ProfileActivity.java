package com.bmvl.lk.ui.profile;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bmvl.lk.App;
import com.bmvl.lk.R;
import com.bmvl.lk.Rest.NetworkService;
import com.bmvl.lk.Rest.StandardAnswer;
import com.bmvl.lk.ui.login.LoginActivity;
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
        setContentView(R.layout.activity_profile_material);

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

        final String device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);


        toolbar.setTitle(R.string.profile);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setIcon(R.drawable.logo);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);



        fab.setColorFilter(Color.argb(255, 255, 255, 255));
       // Hawk.deleteAll();


        FIO.setText(App.UserInfo.getFIO());
        Email.setText(App.UserInfo.getEmail());
        Phone.setText(App.UserInfo.getPhone());
        Adress.setText(App.UserInfo.getAdress());
        inn.setText(App.UserInfo.getInn());
        bank_details.setText(App.UserInfo.getBank_details());
        Contract_number.setText(App.UserInfo.getContract_number());
        Contract_date.setText(App.UserInfo.getContract_date());


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                NetworkService.getInstance()
                        .getJSONApi()
                        .logout(App.UserAccessData.getToken(), device_id)
                        .enqueue(new Callback<StandardAnswer>() {
                            @Override
                            public void onResponse(@NonNull Call<StandardAnswer> call, @NonNull Response<StandardAnswer> response) {
                                if (response.isSuccessful()) {
//                            App.UserAccessData = response.body();
//                            if (App.UserAccessData.getUser_id() != null)
//                                getUserInfo(App.UserAccessData.getToken());
//
////                            else  { //Пользователь ввел не верный лог/пас
////                                loginResult.setValue(new LoginResult("Не верный логин/пароль!"));
////                            }
                                    StandardAnswer answer = response.body();
                                    if(answer.isSuccess()) {
                                        LoginActivity.loginViewModel.logout(device_id);
                                        final Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                                        intent.addCategory(Intent.CATEGORY_HOME);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        finish();
                                    } else        Snackbar.make(view, "Ошибка: " + answer.getError(), Snackbar.LENGTH_LONG)
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
