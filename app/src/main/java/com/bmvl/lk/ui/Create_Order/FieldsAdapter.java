package com.bmvl.lk.ui.Create_Order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bmvl.lk.R;
import com.google.android.material.datepicker.MaterialTextInputPicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

public class FieldsAdapter extends RecyclerView.Adapter<FieldsAdapter.ViewHolder>{
    private LayoutInflater inflater;

    FieldsAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_field, parent, false);
        return new FieldsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Field f = CreateOrderActivity.Fields.get(position);
        holder.field.setHint(f.getHint());
        holder.field.setInputType(f.getInputType());
        holder.field.setEnabled(f.isEnabled());
        holder.field.setText(f.getValue());
    }

    @Override
    public int getItemCount() {
        return CreateOrderActivity.Fields.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextInputEditText field;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            field = itemView.findViewById(R.id.TextInput);
        }
    }
}
