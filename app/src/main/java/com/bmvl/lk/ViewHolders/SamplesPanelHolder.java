package com.bmvl.lk.ViewHolders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bmvl.lk.R;
import com.google.android.material.button.MaterialButton;

public class SamplesPanelHolder extends RecyclerView.ViewHolder {
    final public MaterialButton btnAddSample;
    final public RecyclerView SampleList;
    final public TextView SamplePrice;
    public SamplesPanelHolder(@NonNull View itemView) {
        super(itemView);
        btnAddSample = itemView.findViewById(R.id.addBtn);
        SampleList = itemView.findViewById(R.id.List);
        SamplePrice = itemView.findViewById(R.id.Price);
    }
}
