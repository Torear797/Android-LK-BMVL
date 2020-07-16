package com.bmvl.lk.ui.settings.fragments.EditNotify;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.RecyclerView;

import com.bmvl.lk.R;
import com.bmvl.lk.ui.settings.ItemNotify;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.checkbox.MaterialCheckBox;

import java.util.List;

public class ItemSettingAdapter extends RecyclerView.Adapter {
    private List<ItemNotify> SettingsFields;
    private LayoutInflater inflater;

    public ItemSettingAdapter(Context context, List<ItemNotify> Contents) {
        this.inflater = LayoutInflater.from(context);
        SettingsFields = Contents;
    }

    @Override
    public int getItemViewType(int position) {
        if (SettingsFields.get(position).getType() == (byte) 0)
            return R.layout.item_setting_field;
        else return R.layout.item_setting_head;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        if (viewType == R.layout.item_setting_field) {
            return new FieldHolder(view);
        } else {
            return new HeaderHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final ItemNotify item = SettingsFields.get(position);
        if (item.getType() == (byte) 0) {
            ((FieldHolder) holder).Name.setText(item.getName());
            ((FieldHolder) holder).Email.setChecked(item.isEmail());
            ((FieldHolder) holder).LK.setChecked(item.isLK());
            ((FieldHolder) holder).SMS.setChecked(item.isSMS());
        }
    }


    @Override
    public int getItemCount() {
        return SettingsFields.size();
    }

    private class HeaderHolder extends RecyclerView.ViewHolder {
        public TextView Name, LK, Email, SMS;

        public HeaderHolder(@NonNull View itemView) {
            super(itemView);

            Name = itemView.findViewById(R.id.txt_type);
            LK = itemView.findViewById(R.id.txt_LK);
            Email = itemView.findViewById(R.id.txt_email);
            SMS = itemView.findViewById(R.id.txt_SMS);
        }
    }

    private class FieldHolder extends RecyclerView.ViewHolder {
        public TextView Name;
        public MaterialCheckBox LK, Email, SMS;
        public MaterialCardView Card;
        public Group group;

        public FieldHolder(@NonNull View itemView) {
            super(itemView);

            Name = itemView.findViewById(R.id.txt_type);
            LK = itemView.findViewById(R.id.LK);
            Email = itemView.findViewById(R.id.Email);
            SMS = itemView.findViewById(R.id.SMS);
            Card = itemView.findViewById(R.id.Card);
            group = itemView.findViewById(R.id.MaterialCheckBoxes);

            LK.setOnCheckedChangeListener((buttonView, isChecked) -> {
                SettingsFields.get(getLayoutPosition()).setLK(isChecked);
            });

            Email.setOnCheckedChangeListener((buttonView, isChecked) -> {
                SettingsFields.get(getLayoutPosition()).setEmail(isChecked);
            });

            SMS.setOnCheckedChangeListener((buttonView, isChecked) -> {
                SettingsFields.get(getLayoutPosition()).setSMS(isChecked);
            });

            Card.setOnClickListener(v -> {
                group.setVisibility(group.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            });

        }
    }
}
