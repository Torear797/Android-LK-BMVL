package com.bmvl.lk.ui.settings;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bmvl.lk.R;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.SettingsHolder> {
    private static List<SettingsGroup> SettingsFields;
    private OnClickListener onClickListener;

    public SettingsAdapter(List<SettingsGroup> Contents, OnClickListener onClickListener) {
        SettingsFields = Contents;
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onDeleteOrder(SettingsGroup group);
    }

    @NonNull
    @Override
    public SettingsAdapter.SettingsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    //    View view = inflater.inflate(R.layout.item_settings_group, parent, false);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_settings_group, parent, false);
        return new SettingsAdapter.SettingsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SettingsHolder holder, int position) {
        final SettingsGroup Group = SettingsFields.get(position);
        holder.img.setImageResource(Group.getIMG());
        holder.img.setBackgroundColor(Group.getColor());
        holder.Name.setText(Group.getName());
        holder.Description.setText(Group.getDescription());
    }

    @Override
    public int getItemCount() {
        return SettingsFields.size();
    }

    public class SettingsHolder extends RecyclerView.ViewHolder {
        final ShapeableImageView img;
        final TextView Name, Description;
        final ConstraintLayout group;

        public SettingsHolder(View view) {
            super(view);
            img = view.findViewById(R.id.image_view);
            Name = view.findViewById(R.id.Name);
            Description = view.findViewById(R.id.Description);
            group = view.findViewById(R.id.Group);
            group.setOnClickListener(view1 -> {
                onClickListener.onDeleteOrder(SettingsFields.get(getLayoutPosition()));
            });
        }
    }
}
