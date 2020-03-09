package com.bmvl.lk.ViewHolders;

import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bmvl.lk.R;

public class SpinerHolder extends RecyclerView.ViewHolder  {
    public final Spinner spiner;
    public final TextView txtHint;
    public SpinerHolder(@NonNull View itemView) {
        super(itemView);
        spiner = itemView.findViewById(R.id.spinner);
        txtHint = itemView.findViewById(R.id.hint);
    }
}
