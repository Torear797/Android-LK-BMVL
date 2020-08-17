package com.bmvl.lk.ViewHolders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import com.bmvl.lk.R;
import com.bmvl.lk.data.SpacesItemDecoration;
import com.bmvl.lk.ui.create_order.CreateOrderActivity;
import com.google.android.material.button.MaterialButton;

public class ResearchPanelHolder extends RecyclerView.ViewHolder {
    public final MaterialButton btnAddReserch;
    public final RecyclerView ResearchList;

    public ResearchPanelHolder(@NonNull View itemView) {
        super(itemView);
        btnAddReserch = itemView.findViewById(R.id.addBtn);
        ResearchList = itemView.findViewById(R.id.List);
        final TextView header = itemView.findViewById(R.id.HeaderRecyclerview);
        header.setText("Исследования");

        btnAddReserch.setText("Добавить исследование");
        ResearchList.addItemDecoration(new SpacesItemDecoration((byte) 20, (byte) 0));
        ResearchList.setItemAnimator(new DefaultItemAnimator());

        if(CreateOrderActivity.ReadOnly)btnAddReserch.setVisibility(View.GONE);
    }
}
