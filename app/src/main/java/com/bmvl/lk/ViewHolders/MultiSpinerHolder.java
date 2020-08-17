package com.bmvl.lk.ViewHolders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bmvl.lk.R;
import com.bmvl.lk.ui.ProbyMenu.Probs.MultiSpinner;
import com.bmvl.lk.ui.create_order.CreateOrderActivity;

public class MultiSpinerHolder extends RecyclerView.ViewHolder  {
    final public MultiSpinner spiner;
    final public TextView txtHint;
    public MultiSpinerHolder(@NonNull View itemView) {
        super(itemView);
        spiner = itemView.findViewById(R.id.spinner);
        txtHint = itemView.findViewById(R.id.hint);

        if(CreateOrderActivity.ReadOnly)spiner.setEnabled(false);
    }
}
