package com.bmvl.lk.ViewHolders;

import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bmvl.lk.R;
import com.google.android.material.textfield.TextInputLayout;

public class MaterialFieldHolder extends RecyclerView.ViewHolder {
    public final TextInputLayout Layout;
    public final AutoCompleteTextView TextView;
    public final ImageButton ChoceBtn;

    public MaterialFieldHolder(@NonNull View itemView) {
        super(itemView);
        Layout = itemView.findViewById(R.id.TextLayout);
        TextView = itemView.findViewById(R.id.AutoCompleteTextView);
        ChoceBtn = itemView.findViewById(R.id.ChoceMaterial);
    }
}
