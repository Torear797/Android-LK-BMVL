package com.bmvl.lk.ui.settings.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bmvl.lk.App;
import com.bmvl.lk.R;
import com.bmvl.lk.Rest.NetworkService;
import com.bmvl.lk.Rest.StandardAnswer;
import com.bmvl.lk.data.OnBackPressedListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordFragment extends Fragment implements OnBackPressedListener {
    public static Fragment newInstance() {
        return new ChangePasswordFragment();
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View MyView = inflater.inflate(R.layout.fragment_change_password, container, false);

        final TextInputEditText old_pas = MyView.findViewById(R.id.old_password);
        final TextInputEditText new_pas = MyView.findViewById(R.id.new_password);
        final TextInputEditText repeat_pas = MyView.findViewById(R.id.repeat_password);

        final MaterialButton change_btn = MyView.findViewById(R.id.Change);

        StringBuilder token;
        if(Objects.requireNonNull(getActivity()).getIntent().getStringExtra("token") != null)
         token = new StringBuilder(Objects.requireNonNull(Objects.requireNonNull(getActivity()).getIntent().getStringExtra("token")));
        else {
             token = new StringBuilder();
            token.append(App.UserAccessData.getToken());
        }

        change_btn.setOnClickListener(v -> NetworkService.getInstance()
                .getJSONApi()
                .setNewPassword(token.toString(), String.valueOf(old_pas.getText()), String.valueOf(new_pas.getText()), String.valueOf(repeat_pas.getText()))
                .enqueue(new Callback<StandardAnswer>() {
                    @Override
                    public void onResponse(@NonNull Call<StandardAnswer> call, @NonNull Response<StandardAnswer> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {
                                Toast.makeText(getContext(), R.string.change_password_ok, Toast.LENGTH_SHORT).show();

                             //   if (Objects.requireNonNull(getActivity()).getIntent().getBooleanExtra("needChangePassword", false))
                                    Objects.requireNonNull(getActivity()).finish();
                            } else
                                Toast.makeText(getContext(), R.string.change_password_error, Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(getContext(), R.string.change_password_error, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(@NonNull Call<StandardAnswer> call, @NonNull Throwable t) {
                        Toast.makeText(getContext(), R.string.server_lost, Toast.LENGTH_SHORT).show();
                    }
                }));
        return MyView;
    }
}
