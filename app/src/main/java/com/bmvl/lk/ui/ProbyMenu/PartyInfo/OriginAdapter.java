package com.bmvl.lk.ui.ProbyMenu.PartyInfo;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bmvl.lk.R;
import com.bmvl.lk.ViewHolders.SwitchHolder;
import com.bmvl.lk.ViewHolders.TextViewHolder;
import com.bmvl.lk.data.Field;
import com.bmvl.lk.ui.ProbyMenu.Probs.ProbAdapter;
import com.bmvl.lk.ui.create_order.CreateOrderActivity;

import java.util.List;
import java.util.Map;

class OriginAdapter extends RecyclerView.Adapter {
    private List<Field> OriginFields;
    private Map<Short, String> fields;
    private OnOriginClickListener onOriginClickListener;
    private boolean ReadOnly;
    private Map<String, String> fieldsProb;

    OriginAdapter(List<Field> Fields, OnOriginClickListener Listener, boolean read, Map<String, String> FieldsProb) {
        this.ReadOnly = read;
        this.onOriginClickListener = Listener;
        this.fieldsProb = FieldsProb;
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

            if (!ReadOnly)
                holder3.switchButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (fieldsProb == null) {
                        fields.put(GetColumn_id(holder3.getLayoutPosition()), String.valueOf(isChecked));
                    } else {
                        fieldsProb.put(String.valueOf(GetColumn_id(holder3.getLayoutPosition())), String.valueOf(isChecked));
                    }

                    if (GetColumn_id(holder3.getLayoutPosition()) == (byte) 56)
                        onOriginClickListener.onChangeOrigin(isChecked);
                    else
                    if (GetColumn_id(holder3.getLayoutPosition()) == (byte) 120)
                        ProbAdapter.adapter.notifyDataSetChanged();
                });
            return holder3;
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_field, parent, false);
        final TextViewHolder holder = new TextViewHolder(view);

        if (!ReadOnly)
            holder.field.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                    if (!String.valueOf(s).equals(""))
                        if (fieldsProb == null) {
                            fields.put(GetColumn_id(holder.getLayoutPosition()), String.valueOf(s));
                        } else {
                            fieldsProb.put(String.valueOf(GetColumn_id(holder.getLayoutPosition())), String.valueOf(s));
                        }
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

    private short GetColumn_id(int position) {
        return (short) OriginFields.get(position).getColumn_id();
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final Field f = OriginFields.get(position);
        switch (f.getType()) {
            case 0: {
                ((TextViewHolder) holder).Layout.setHint(f.getHint());
                ((TextViewHolder) holder).field.setInputType(f.getInputType());
                //  if (fields.containsKey((short) f.getColumn_id()))
                if (fieldsProb == null || ReadOnly)
                    ((TextViewHolder) holder).field.setText(fields.get((short) f.getColumn_id()));
                else
                    ((TextViewHolder) holder).field.setText(fieldsProb.get(String.valueOf(f.getColumn_id())));

                if (ReadOnly) ((TextViewHolder) holder).field.setEnabled(false);
                else ((TextViewHolder) holder).field.setEnabled(true);

                break;
            }
            case 3:
                ((SwitchHolder) holder).switchButton.setText(String.format("%s  ", f.getHint()));

                if (ReadOnly) ((SwitchHolder) holder).switchButton.setEnabled(false);
                else ((SwitchHolder) holder).switchButton.setEnabled(true);

                // if (fields.containsKey((short) f.getColumn_id()))
               //  ((SwitchHolder) holder).switchButton.setChecked(false);
                if (fieldsProb == null || ReadOnly)
                    ((SwitchHolder) holder).switchButton.setChecked(Boolean.parseBoolean(fields.get((short) f.getColumn_id())));
                else
                    ((SwitchHolder) holder).switchButton.setChecked(Boolean.parseBoolean(fieldsProb.get(String.valueOf(f.getColumn_id()))));
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