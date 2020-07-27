package com.bmvl.lk.ViewHolders;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bmvl.lk.R;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;

public class AutoCompleteFieldHolder extends RecyclerView.ViewHolder {
    public final TextInputLayout Layout;
    public final MaterialAutoCompleteTextView TextView;

    public AutoCompleteFieldHolder(@NonNull View itemView) {
        super(itemView);
        Layout = itemView.findViewById(R.id.TextLayout);
        TextView = itemView.findViewById(R.id.AutoCompleteTextView);
    }
}
