package com.bmvl.lk.ViewHolders;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bmvl.lk.R;

public class PartyInfoHolder extends RecyclerView.ViewHolder {
    final public LinearLayout head;
    final public ImageView arrow;
    final public FrameLayout Frame;
    final public TextView LeftTxt, RightTxt;

    public PartyInfoHolder(@NonNull View itemView) {
        super(itemView);

        head = itemView.findViewById(R.id.Header);
        arrow = itemView.findViewById(R.id.arrow);
        Frame = itemView.findViewById(R.id.FramePatryInfo);
        LeftTxt = itemView.findViewById(R.id.Title_left);
        RightTxt = itemView.findViewById(R.id.Title_right);

        head.setOnClickListener(v -> {
            Frame.setVisibility(Frame.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            arrow.setImageResource(Frame.getVisibility() == View.VISIBLE ? R.drawable.ic_w_arrow_ap : R.drawable.ic_w_arrow_down);
        });
    }
}
