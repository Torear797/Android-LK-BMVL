package com.bmvl.lk.ViewHolders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bmvl.lk.R;
import com.google.android.material.button.MaterialButton;

public class ResearchPanelHolder extends RecyclerView.ViewHolder {
     public final MaterialButton btnAddReserch;
     public final RecyclerView ResearchList;
     public final TextView Header;
    public ResearchPanelHolder(@NonNull View itemView) {
        super(itemView);
        btnAddReserch = itemView.findViewById(R.id.addBtn);
        ResearchList = itemView.findViewById(R.id.List);
        Header = itemView.findViewById(R.id.HeaderRecyclerview);
        Header.setText("Исследования");
    }
}
