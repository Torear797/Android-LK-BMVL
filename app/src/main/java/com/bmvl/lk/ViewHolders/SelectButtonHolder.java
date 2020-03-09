package com.bmvl.lk.ViewHolders;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bmvl.lk.R;

public class SelectButtonHolder extends RecyclerView.ViewHolder {
    final public TextView hint,path;
    final public Button select_btn;
    public SelectButtonHolder(@NonNull View itemView) {
        super(itemView);
        hint = itemView.findViewById(R.id.hint);
        path = itemView.findViewById(R.id.path);
        select_btn = itemView.findViewById(R.id.select);
    }
}
