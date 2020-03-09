package com.bmvl.lk.ViewHolders;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bmvl.lk.R;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class SwitchHolder extends RecyclerView.ViewHolder {
    final public SwitchMaterial switchButton;
    public SwitchHolder(@NonNull View itemView) {
        super(itemView);
        switchButton = itemView.findViewById(R.id.my_switch);
    }
}
