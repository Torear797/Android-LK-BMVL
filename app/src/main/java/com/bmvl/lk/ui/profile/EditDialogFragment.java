package com.bmvl.lk.ui.profile;

import android.app.Dialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.bmvl.lk.App;
import com.bmvl.lk.R;
import com.bmvl.lk.Rest.NetworkService;
import com.bmvl.lk.Rest.StandardAnswer;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import br.com.sapereaude.maskedEditText.MaskedEditText;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditDialogFragment extends DialogFragment {
    private TextView text;
    private ToggleButton edit_btn;

    public EditDialogFragment(TextView textView, ToggleButton changebtn) {
        this.text = textView;
        this.edit_btn = changebtn;
    }

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        final View MyView = inflater.inflate(R.layout.edit_dialog, null, false);
        final MaskedEditText editText = MyView.findViewById(R.id.TextInput);
        final TextInputLayout layout = MyView.findViewById(R.id.TextInputLayout);

        StringBuilder str = new StringBuilder();
        if(getArguments().getBoolean("phone", true)) {
            str.append("Введите номер телефона");
            editText.setMask("+7(###)###-##-##");
            editText.setInputType(InputType.TYPE_CLASS_PHONE);
            layout.setHint(getResources().getString(R.string.phone));
        }
        else {
            str.append("Введите email");
            layout.setHint(getResources().getString(R.string.prompt_email));
        }

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(Objects.requireNonNull(getActivity()));
        return builder
                .setTitle("Редактирование данных")
                .setMessage(str.toString())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setView(MyView)
                .setPositiveButton("OK", (dialog, which) -> {
                    text.setText(editText.getText().toString());

                    if(getArguments().getBoolean("phone", true)) {
                        NetworkService.getInstance()
                                .getJSONApi()
                                .saveAccountPhone(App.UserAccessData.getToken(), editText.getText().toString())
                                .enqueue(new Callback<StandardAnswer>() {
                                    @Override
                                    public void onResponse(@NonNull Call<StandardAnswer> call, @NonNull Response<StandardAnswer> response) {
                                        if (response.isSuccessful() && response.body() != null) {
                                            if (response.body().getStatus() == 200) {
                                                Toast.makeText(inflater.getContext(), R.string.data_save_ok, Toast.LENGTH_SHORT).show();
                                                edit_btn.setVisibility(View.GONE);
                                            } else
                                                Toast.makeText(inflater.getContext(), R.string.data_save_error, Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(@NonNull Call<StandardAnswer> call, @NonNull Throwable t) {
                                        Toast.makeText(inflater.getContext(), R.string.server_lost, Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                    else {
                        NetworkService.getInstance()
                                .getJSONApi()
                                .saveAccountEmail(App.UserAccessData.getToken(),editText.getText().toString())
                                .enqueue(new Callback<StandardAnswer>() {
                                    @Override
                                    public void onResponse(@NonNull Call<StandardAnswer> call, @NonNull Response<StandardAnswer> response) {
                                        if (response.isSuccessful() && response.body() != null) {
                                            if (response.body().getStatus() == 200) {
                                                Toast.makeText(inflater.getContext(), R.string.data_save_ok, Toast.LENGTH_SHORT).show();
                                                edit_btn.setVisibility(View.GONE);
                                            } else
                                                Toast.makeText(inflater.getContext(), R.string.data_save_error, Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(@NonNull Call<StandardAnswer> call, @NonNull Throwable t) {
                                        Toast.makeText(inflater.getContext(), R.string.server_lost, Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }

                })
                .setNegativeButton("Отмена", null)
                .create();
    }
}
