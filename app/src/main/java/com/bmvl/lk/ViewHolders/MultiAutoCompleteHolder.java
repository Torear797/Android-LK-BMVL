package com.bmvl.lk.ViewHolders;

import android.view.View;
import android.widget.MultiAutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bmvl.lk.R;
import com.bmvl.lk.ui.create_order.CreateOrderActivity;
import com.google.android.material.textfield.TextInputLayout;

public class MultiAutoCompleteHolder extends RecyclerView.ViewHolder {
    public final TextInputLayout Layout;
    public final MultiAutoCompleteTextView TextView;

    public MultiAutoCompleteHolder(@NonNull View itemView) {
        super(itemView);
        Layout = itemView.findViewById(R.id.TextLayout);
        TextView = itemView.findViewById(R.id.MultiAutoCompleteTextView);

        if(CreateOrderActivity.ReadOnly)Layout.setEnabled(false);
    }
}
