package com.bmvl.lk.ui.ProbyMenu.PartyInfo;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bmvl.lk.R;
import com.bmvl.lk.ViewHolders.SwitchHolder;
import com.bmvl.lk.ViewHolders.TextViewHolder;
import com.bmvl.lk.data.Field;
import com.bmvl.lk.ui.create_order.CreateOrderActivity;

import java.util.List;
import java.util.Map;

class OriginAdapter extends RecyclerView.Adapter {
    private LayoutInflater inflater;
    private static List<Field> OriginFields;
    private static Map<Short, String> fields;
    private OnOriginClickListener onOriginClickListener;

    OriginAdapter(Context context, List<Field> Fields, OnOriginClickListener Listener) {
        this.inflater = LayoutInflater.from(context);
        this.onOriginClickListener = Listener;
        OriginFields = Fields;
        fields = CreateOrderActivity.order.getFields();
    }
    public interface OnOriginClickListener {
        void onChangeOrigin(boolean isChecked);
    }

    @Override
    public int getItemViewType(int position) {
        return OriginFields.get(position).getType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 3) {
            View view3 = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_check_button, parent, false);
            final SwitchHolder holder3 = new SwitchHolder(view3);

            holder3.switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    fields.put(GetColumn_id(holder3.getLayoutPosition()), String.valueOf(isChecked));
                    if (GetColumn_id(holder3.getLayoutPosition()) == (byte) 56)
                        onOriginClickListener.onChangeOrigin(isChecked);
                }
            });
            return holder3;
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_field, parent, false);
        final TextViewHolder holder = new TextViewHolder(view);

        holder.field.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (!String.valueOf(s).equals(""))
                    fields.put(GetColumn_id(holder.getLayoutPosition()), String.valueOf(s));
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        return holder;
    }
    private short GetColumn_id(int position){
        return (short)OriginFields.get(position).getColumn_id();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final Field f = OriginFields.get(position);
        switch (f.getType()) {
            case 0: {
                ((TextViewHolder) holder).Layout.setHint(f.getHint());
                ((TextViewHolder) holder).field.setInputType(f.getInputType());
                if(fields.containsKey((short)f.getColumn_id()))
                ((TextViewHolder) holder).field.setText(fields.get((short)f.getColumn_id()));
                break;
            }
            case 3:
                ((SwitchHolder) holder).switchButton.setText(String.format("%s  ", f.getHint()));

                if(fields.containsKey((short)f.getColumn_id()))
                    ((SwitchHolder) holder).switchButton.setChecked(Boolean.parseBoolean(fields.get((short)f.getColumn_id())));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return OriginFields.size();
    }

    public void updateList(List<Field> newList) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new FieldsDiffUtilCallback(OriginFields, newList));
        OriginFields.clear();
        OriginFields.addAll(newList);
        diffResult.dispatchUpdatesTo(this);
    }
}