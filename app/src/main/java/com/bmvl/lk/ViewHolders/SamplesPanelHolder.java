package com.bmvl.lk.ViewHolders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import com.bmvl.lk.R;
import com.bmvl.lk.data.SpacesItemDecoration;
import com.google.android.material.button.MaterialButton;

public class SamplesPanelHolder extends RecyclerView.ViewHolder {
    public final MaterialButton btnAddSample;
    public final RecyclerView SampleList;
    public final TextView Header;

    public SamplesPanelHolder(@NonNull View itemView) {
        super(itemView);
        btnAddSample = itemView.findViewById(R.id.addBtn);
        SampleList = itemView.findViewById(R.id.List);
        Header = itemView.findViewById(R.id.HeaderRecyclerview);
        Header.setText("Образцы");

        btnAddSample.setText("Добавить образец");

        SampleList.addItemDecoration(new SpacesItemDecoration((byte) 20, (byte) 0));
        SampleList.setItemAnimator(new DefaultItemAnimator());
    }
}
