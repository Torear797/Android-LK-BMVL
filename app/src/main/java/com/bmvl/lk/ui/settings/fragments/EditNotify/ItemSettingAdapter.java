package com.bmvl.lk.ui.settings.fragments.EditNotify;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.RecyclerView;

import com.bmvl.lk.R;
import com.bmvl.lk.data.models.ItemNotify;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.checkbox.MaterialCheckBox;

import java.text.MessageFormat;
import java.util.List;

public class ItemSettingAdapter extends RecyclerView.Adapter {
    private List<ItemNotify> SettingsFields;

    public ItemSettingAdapter(List<ItemNotify> Contents) {
        // LayoutInflater inflater = LayoutInflater.from(context);
        SettingsFields = Contents;
    }

    @Override
    public int getItemViewType(int position) {
//        if (SettingsFields.get(position).getType() == (byte) 0)
//            return R.layout.item_setting_field;
//        else return R.layout.item_setting_head;
        return R.layout.item_setting_field;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new FieldHolder(view);
//        if (viewType == R.layout.item_setting_field) {
//            return new FieldHolder(view);
//        } else {
//            return new HeaderHolder(view);
//        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final ItemNotify item = SettingsFields.get(position);
        //  if (item.getType() == (byte) 0)
        {
            ((FieldHolder) holder).Name.setText(MessageFormat.format("{0}({1})", item.getName(), getChecBoxTextStatus(item)));
            ((FieldHolder) holder).Email.setChecked(item.isEmail());
            ((FieldHolder) holder).LK.setChecked(item.isLK());
            ((FieldHolder) holder).SMS.setChecked(item.isSMS());
        }
    }

    private String getChecBoxTextStatus(ItemNotify item) {
        StringBuilder status = new StringBuilder();
        if (item.isLK()) status.append("в ЛК");

        if (status.length() > 0 && item.isEmail()) status.append(", ");
        if (item.isEmail()) status.append("на Email");

        if (status.length() > 0 && item.isSMS()) status.append(", ");
        if (item.isSMS()) status.append("по SMS");

        return status.toString();
    }


    @Override
    public int getItemCount() {
        return SettingsFields.size();
    }

    private class FieldHolder extends RecyclerView.ViewHolder {
        public TextView Name;
        public MaterialCheckBox LK, Email, SMS;
        public MaterialCardView Card;
        public Group group;
        public ImageView arrow;

        public FieldHolder(@NonNull View itemView) {
            super(itemView);

            Name = itemView.findViewById(R.id.txt_type);
            LK = itemView.findViewById(R.id.LK);
            Email = itemView.findViewById(R.id.Email);
            SMS = itemView.findViewById(R.id.SMS);
            Card = itemView.findViewById(R.id.Card);
            group = itemView.findViewById(R.id.MaterialCheckBoxes);
            arrow = itemView.findViewById(R.id.arrow_icon);

            LK.setOnCheckedChangeListener((buttonView, isChecked) -> {
                SettingsFields.get(getLayoutPosition()).setLK(isChecked);
                Name.setText(MessageFormat.format("{0}({1})", SettingsFields.get(getLayoutPosition()).getName(), getChecBoxTextStatus(SettingsFields.get(getLayoutPosition()))));
            });

            Email.setOnCheckedChangeListener((buttonView, isChecked) -> {
                SettingsFields.get(getLayoutPosition()).setEmail(isChecked);
                Name.setText(MessageFormat.format("{0}({1})", SettingsFields.get(getLayoutPosition()).getName(), getChecBoxTextStatus(SettingsFields.get(getLayoutPosition()))));
            });

            SMS.setOnCheckedChangeListener((buttonView, isChecked) -> {
                SettingsFields.get(getLayoutPosition()).setSMS(isChecked);
                Name.setText(MessageFormat.format("{0}({1})", SettingsFields.get(getLayoutPosition()).getName(), getChecBoxTextStatus(SettingsFields.get(getLayoutPosition()))));
            });

            Card.setOnClickListener(v -> {
                group.setVisibility(group.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                arrow.setImageResource(group.getVisibility() == View.VISIBLE ? R.drawable.ic_baseline_keyboard_arrow_down_24 : R.drawable.ic_baseline_keyboard_arrow_right_24);
            });

        }
    }
}
