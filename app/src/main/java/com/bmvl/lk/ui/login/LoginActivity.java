package com.bmvl.lk.ui.login;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bmvl.lk.App;
import com.bmvl.lk.R;
import com.bmvl.lk.ui.MenuActivity;
import com.bmvl.lk.ui.reset_password.ResetPasswordActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.iid.FirebaseInstanceId;
import com.orhanobut.hawk.Hawk;

import java.util.Objects;

import br.com.sapereaude.maskedEditText.MaskedEditText;

public class LoginActivity extends AppCompatActivity {

    public static LoginViewModel loginViewModel;
    public Button loginButton;
    //  private ProgressBar loadingProgressBar;
    private static String FireBase_id = "no token";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        @SuppressLint("HardwareIds") final String ANDROID_ID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        final MaterialToolbar toolbar = findViewById(R.id.toolbar);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);
        toolbar.setTitle(R.string.Login_title);
        setSupportActionBar(toolbar);


        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(Objects.requireNonNull(this), instanceIdResult -> FireBase_id = instanceIdResult.getToken());

        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory()).get(LoginViewModel.class);

        final MaskedEditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.Create);

        loginViewModel.getLoginFormState().observe(this, loginFormState -> {        //Меняем состояние кнопки
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
        });

        //Обрабокта результата
        loginViewModel.getLoginResult().observe(this, loginResult -> {

            if (loginResult == null) {
                loadingProgressBar.setVisibility(View.GONE);
                loginButton.setEnabled(true);
                return;
            }

            loadingProgressBar.setVisibility(View.GONE);

            if (loginResult.getError() != null) {
                showLoginFailed(loginResult.getError());
                return;
            }

            if (loginResult.getSuccess() != null) {
                updateUiWithUser(loginResult.getSuccess());
                //  updateUiWithUser();
            }
            setResult(Activity.RESULT_OK);

            finish();
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(String.valueOf(usernameEditText.getText()),
                        passwordEditText.getText().toString());
            }
        };

        TextWatcher afterTextChangedListenerLogin = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 0 && usernameEditText.length() == 3 && usernameEditText.getMask().equals("+7(###)###-##-##")) {
                    usernameEditText.setMask("####################");
                    usernameEditText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                    usernameEditText.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals("+")) {
                    usernameEditText.setMask("+7(###)###-##-##");
                    usernameEditText.setInputType(InputType.TYPE_CLASS_PHONE);
                    usernameEditText.setSelection(Objects.requireNonNull(usernameEditText.getText()).length());
                }
                loginViewModel.loginDataChanged(String.valueOf(usernameEditText.getText()),
                        passwordEditText.getText().toString());
            }
        };

        usernameEditText.addTextChangedListener(afterTextChangedListenerLogin);
        passwordEditText.addTextChangedListener(afterTextChangedListener);

        passwordEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loginViewModel.login(String.valueOf(usernameEditText.getText()),
                        passwordEditText.getText().toString(), ANDROID_ID, getApplicationContext(), FireBase_id);
            }
            return false;
        });

        loginButton.setOnClickListener(v -> {
            loginButton.setEnabled(false);
            loadingProgressBar.setVisibility(View.VISIBLE);
            loginViewModel.login(String.valueOf(usernameEditText.getText()), passwordEditText.getText().toString(), ANDROID_ID, getApplicationContext(), FireBase_id);
            // loginViewModel.login(usernameEditText.getText().toString(), passwordEditText.getText().toString(), ANDROID_ID, getApplicationContext());
        });
    }

    private void updateUiWithUser(LoggedInUserView User) {
        Hawk.deleteAll();

        Hawk.put("UserAccessData", User.getAccessData());
        Hawk.put("UserInfo", User.getUserInfo());
        Hawk.put("OrderInfo", User.getOrderInfo());

        App.setUserData(User.getAccessData(), User.getUserInfo(), User.getOrderInfo());

        AccessIsObtained();
    }

    private void showLoginFailed(String Error) {
        loginButton.setEnabled(true);
        Toast.makeText(getApplicationContext(), Error, Toast.LENGTH_SHORT).show();
    }

    private void AccessIsObtained() {
        //   Intent intent = ;
        startActivity(new Intent(this, MenuActivity.class));
        finish();
    }

    public void resetPassword(View view) {
        //Intent intent = ;
//        intent.putExtra("type_id", (byte) 2);
//        intent.putExtra("name", R.string.change_password_desc);
//        intent.putExtra("needChangePassword", true);
//        intent.putExtra("token", accessData.getToken());
        startActivity(new Intent(this, ResetPasswordActivity.class));
    }
}
