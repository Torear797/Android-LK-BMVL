package com.bmvl.lk.ViewHolders;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bmvl.lk.R;
import com.google.android.material.textfield.TextInputLayout;

import br.com.sapereaude.maskedEditText.MaskedEditText;

public class DataFieldHolder extends RecyclerView.ViewHolder {
    public final MaskedEditText field;
    public final TextInputLayout Layout;

    public DataFieldHolder(@NonNull View itemView) {
        super(itemView);
        field = itemView.findViewById(R.id.TextInput);
        Layout = itemView.findViewById(R.id.TextInputLayout);
    }
}
