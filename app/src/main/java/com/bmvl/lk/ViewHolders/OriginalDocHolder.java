package com.bmvl.lk.ViewHolders;

import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bmvl.lk.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class OriginalDocHolder extends RecyclerView.ViewHolder {
    final public Spinner spiner;
    final public TextView txtHint;

    final public TextInputEditText fieldEmail;
    final public TextInputLayout LayoutEmail;

    final public TextInputEditText fieldAdres;
    final public TextInputLayout LayoutAdres;
    public OriginalDocHolder(@NonNull View itemView) {
        super(itemView);

        spiner = itemView.findViewById(R.id.spinner);
        txtHint = itemView.findViewById(R.id.hint);

        fieldEmail = itemView.findViewById(R.id.EmailInput);
        LayoutEmail = itemView.findViewById(R.id.EmailLayout);

        fieldAdres = itemView.findViewById(R.id.AdresInput);
        LayoutAdres = itemView.findViewById(R.id.AdresLayout);
    }
}
