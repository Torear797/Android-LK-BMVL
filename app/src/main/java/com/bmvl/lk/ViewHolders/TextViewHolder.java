package com.bmvl.lk.ViewHolders;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bmvl.lk.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class TextViewHolder extends RecyclerView.ViewHolder  {
    public TextInputEditText field;
    public final TextInputLayout Layout;
    public TextViewHolder(@NonNull View itemView) {
        super(itemView);
        field = itemView.findViewById(R.id.TextInput);
        Layout = itemView.findViewById(R.id.TextLayout);
    }
}
