package com.bmvl.lk.ui.ProbyMenu.PartyInfo;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bmvl.lk.App;
import com.bmvl.lk.R;
import com.bmvl.lk.Rest.AnswerCountries;
import com.bmvl.lk.Rest.NetworkService;
import com.bmvl.lk.ViewHolders.AutoCompleteFieldHolder;
import com.bmvl.lk.ViewHolders.SwitchHolder;
import com.bmvl.lk.ViewHolders.TextViewHolder;
import com.bmvl.lk.data.Field;
import com.bmvl.lk.ui.ProbyMenu.Probs.ProbAdapter;
import com.bmvl.lk.ui.ProbyMenu.Probs.ProbsFragment;
import com.bmvl.lk.ui.create_order.CreateOrderActivity;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class OriginAdapter extends RecyclerView.Adapter {
    private List<Field> OriginFields; //Поля происхождения
    private Map<Short, String> fields; //Данные пробы
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

    private String[] Regions;
    private int RegionPos;

    public interface OnOriginClickListener {
        void onChangeOrigin(boolean isChecked);
    }

    @Override
    public int getItemViewType(int position) {
        switch (OriginFields.get(position).getType()) {
            case 3:
                return R.layout.item_check_button;
            case 11:
                return R.layout.item_auto_compiete_field;
            default:
                return R.layout.item_field;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        switch (viewType) {
            case R.layout.item_check_button: {
                final SwitchHolder holder3 = new SwitchHolder(view);

                if (!ReadOnly && !CreateOrderActivity.ReadOnly)
                    holder3.switchButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
                        if (fieldsProb == null) {
                            fields.put(GetColumn_id(holder3.getLayoutPosition()), String.valueOf(isChecked));
                        } else {
                            fieldsProb.put(String.valueOf(GetColumn_id(holder3.getLayoutPosition())), String.valueOf(isChecked));
                        }

                        if (GetColumn_id(holder3.getLayoutPosition()) == (byte) 56)
                            onOriginClickListener.onChangeOrigin(isChecked);
                        else if (GetColumn_id(holder3.getLayoutPosition()) == (byte) 120)
                            ProbAdapter.adapter.notifyDataSetChanged();
                    });
                return holder3;
            }
            case R.layout.item_auto_compiete_field:{
                final AutoCompleteFieldHolder holder = new AutoCompleteFieldHolder(view);
                holder.TextView.setThreshold(2);

                if(!CreateOrderActivity.ReadOnly)
                holder.TextView.setOnItemClickListener((parent1, MeView, position, id) -> {
                    if (fieldsProb == null) {
                        fields.put(GetColumn_id(holder.getLayoutPosition()), holder.TextView.getAdapter().getItem(position).toString());
                    } else {
                        fieldsProb.put(String.valueOf(GetColumn_id(holder.getLayoutPosition())), holder.TextView.getAdapter().getItem(position).toString());
                    }


                    switch (GetColumn_id(holder.getLayoutPosition())){
                        case 29:{
                            Regions = null;
                            if (fieldsProb == null) {
                                fields.remove((short)30);
                            }
                            else {
                                fieldsProb.remove("30");
                            }

                            notifyItemChanged(RegionPos);
                            getRegions(holder.TextView.getAdapter().getItem(position).toString());
                            break;
                        }
                    }

                    holder.TextView.clearFocus();
                });
                return holder;
            }
            default: {
                final TextViewHolder holder = new TextViewHolder(view);

                if (!ReadOnly && !CreateOrderActivity.ReadOnly)
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
        }

    }

    private void getRegions(String country_name){
        NetworkService.getInstance()
                .getJSONApi()
                .getRegions(App.UserAccessData.getToken(),country_name,"")
                .enqueue(new Callback<AnswerCountries>() {
                    @Override
                    public void onResponse(@NonNull Call<AnswerCountries> call, @NonNull Response<AnswerCountries> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Regions = new String[response.body().getSuggestions().size()];
                            for(int i = 0; i <response.body().getSuggestions().size();i++){
                                Regions[i] = response.body().getSuggestions().get(i).getValue();
                            }
                            notifyItemChanged(RegionPos);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<AnswerCountries> call, @NonNull Throwable t) {
                    }
                });
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

                if (ReadOnly || CreateOrderActivity.ReadOnly) ((TextViewHolder) holder).field.setEnabled(false);
                else ((TextViewHolder) holder).field.setEnabled(true);

                break;
            } //EditText
            case 3: {
                ((SwitchHolder) holder).switchButton.setText(String.format("%s  ", f.getHint()));

                if (ReadOnly) ((SwitchHolder) holder).switchButton.setEnabled(false);
                else ((SwitchHolder) holder).switchButton.setEnabled(true);

                // if (fields.containsKey((short) f.getColumn_id()))
                //  ((SwitchHolder) holder).switchButton.setChecked(false);
                if (fieldsProb == null || ReadOnly)
                    ((SwitchHolder) holder).switchButton.setChecked(Boolean.parseBoolean(fields.get((short) f.getColumn_id())));
                else
                    ((SwitchHolder) holder).switchButton.setChecked(Boolean.parseBoolean(fieldsProb.get(String.valueOf(f.getColumn_id()))));

                if (CreateOrderActivity.ReadOnly) ((SwitchHolder) holder).switchButton.setEnabled(false);
                break;
            } //Свитч
            case (byte)11:{
                ((AutoCompleteFieldHolder) holder).Layout.setHint(f.getHint());
                if (fieldsProb == null || ReadOnly)
                    ((AutoCompleteFieldHolder) holder).TextView.setText(fields.get((short) f.getColumn_id()));
                else
                    ((AutoCompleteFieldHolder) holder).TextView.setText(fieldsProb.get(String.valueOf(f.getColumn_id())));

                switch (f.getColumn_id()){
                    case 130:
                    case 129:
                    case 29:{
                        ((AutoCompleteFieldHolder) holder).TextView.setAdapter(new ArrayAdapter<>(((AutoCompleteFieldHolder) holder).TextView.getContext(), android.R.layout.simple_spinner_item, ProbsFragment.Countries));
                        break;
                    }

                    case 30:{
                        RegionPos = position;
                        if(Regions != null){
                            ((AutoCompleteFieldHolder) holder).TextView.setAdapter(new ArrayAdapter<>(((AutoCompleteFieldHolder) holder).TextView.getContext(), android.R.layout.simple_spinner_item, Regions));
                            ((AutoCompleteFieldHolder) holder).Layout.setEnabled(true);
                        } else
                            ((AutoCompleteFieldHolder) holder).Layout.setEnabled(false);
                        break;
                    }
                }

                if (CreateOrderActivity.ReadOnly) ((AutoCompleteFieldHolder) holder).Layout.setEnabled(false);
                break;
            }
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