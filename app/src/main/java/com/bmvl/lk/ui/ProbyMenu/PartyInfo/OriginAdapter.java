package com.bmvl.lk.ui.ProbyMenu.PartyInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bmvl.lk.R;
import com.bmvl.lk.ViewHolders.SwitchHolder;
import com.bmvl.lk.ViewHolders.TextViewHolder;
import com.bmvl.lk.ui.Create_Order.Field;

import java.util.List;

class OriginAdapter extends RecyclerView.Adapter {
    private LayoutInflater inflater;
    private static List<Field> OriginFields;

    OriginAdapter(Context context, List<Field> Fields) {
        this.inflater = LayoutInflater.from(context);
        OriginFields = Fields;
    }

    @Override
    public int getItemViewType(int position) {
        return OriginFields.get(position).getType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                View view = inflater.inflate(R.layout.item_field, parent, false);
                return new TextViewHolder(view);

            case 3:
                View view3 = inflater.inflate(R.layout.item_check_button, parent, false);
                return new SwitchHolder(view3);
        }

        View view = inflater.inflate(R.layout.item_field, parent, false);
        return new TextViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final Field f = OriginFields.get(position);
        switch (f.getType()) {
            case 0: {
                ((TextViewHolder) holder).Layout.setHint(f.getHint());
                ((TextViewHolder) holder).field.setInputType(f.getInputType());
                ((TextViewHolder) holder).field.setText(f.getValue());
                break;
            }
            case 3:
                ((SwitchHolder) holder).switchButton.setText(String.format("%s  ", f.getHint()));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return OriginFields.size();
    }
}