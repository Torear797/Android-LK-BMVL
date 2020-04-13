package com.bmvl.lk.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bmvl.lk.App;
import com.bmvl.lk.MenuActivity;
import com.bmvl.lk.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.orhanobut.hawk.Hawk;

public class LoginActivity extends AppCompatActivity {

    public static LoginViewModel loginViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_login);

        if (isAuth()) AccessIsObtained();

        final MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.Login_title);
        setSupportActionBar(toolbar);

        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory()).get(LoginViewModel.class);

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.Create);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);
        final String ANDROID_ID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);


        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {        //Меняем состояние кнопки
                if (loginFormState == null) {
                    return;
                }

                loginButton.setEnabled(loginFormState.isDataValid());

                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {         //Обрабокта результата
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {

                if (loginResult == null) {
                    return;
                }

                loadingProgressBar.setVisibility(View.GONE);

                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                    return;
                }

                if (loginResult.getSuccess() != null) {
                    // updateUiWithUser(loginResult.getSuccess());
                    updateUiWithUser();
                }
                setResult(Activity.RESULT_OK);

                finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };

        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);

        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString(), ANDROID_ID);
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                loginViewModel.login(usernameEditText.getText().toString(), passwordEditText.getText().toString(), ANDROID_ID);
            }
        });
    }

    private void updateUiWithUser() {
//        String welcome = getString(R.string.welcome) + model.getDisplayName();
        //Toast.makeText(getApplicationContext(), model.getDisplayName(), Toast.LENGTH_LONG).show();
        Hawk.put("UserAccessData", App.UserAccessData);
        Hawk.put("UserInfo", App.UserInfo);

        AccessIsObtained();
    }

    private void showLoginFailed(String Error) {
        Toast.makeText(getApplicationContext(), Error, Toast.LENGTH_SHORT).show();
    }

    private boolean isAuth() {
        App.UserInfo = Hawk.get("UserInfo", null);
        App.UserAccessData = Hawk.get("UserAccessData", null);

        return App.UserInfo != null && App.UserAccessData != null;
    }

    private void AccessIsObtained() {
        Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
        startActivity(intent);
        finish();
    }
}
