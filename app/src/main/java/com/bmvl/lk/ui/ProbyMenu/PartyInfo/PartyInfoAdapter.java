package com.bmvl.lk.ui.ProbyMenu.PartyInfo;

import android.app.DatePickerDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bmvl.lk.R;
import com.bmvl.lk.ViewHolders.SpinerHolder;
import com.bmvl.lk.ViewHolders.SwitchHolder;
import com.bmvl.lk.ViewHolders.TextViewHolder;
import com.bmvl.lk.ui.create_order.CreateOrderActivity;
import com.bmvl.lk.data.Field;
import com.google.android.material.textfield.TextInputLayout;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PartyInfoAdapter extends RecyclerView.Adapter {
    private LayoutInflater inflater;
    private static Calendar dateAndTime = Calendar.getInstance();
    private static List<Field> PartyInfoFields;
    private static Map<Short, String> fields;

    PartyInfoAdapter(Context context, List<Field> PartyFields) {
        this.inflater = LayoutInflater.from(context);
        PartyInfoFields = PartyFields;
        fields = CreateOrderActivity.order.getFields();
    }

    @Override
    public int getItemViewType(int position) {
        return PartyInfoFields.get(position).getType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case 1:
                View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_spiner, parent, false);
                final SpinerHolder holder1 = new SpinerHolder(view1);

                AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String item = (String) parent.getItemAtPosition(position);
                        fields.put(GetColumn_id(holder1.getLayoutPosition()), String.valueOf(item));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                };
                holder1.spiner.setOnItemSelectedListener(itemSelectedListener);
                return holder1;
            case 3:
                View view3 = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_check_button, parent, false);
                final SwitchHolder holder3 = new SwitchHolder(view3);

                holder3.switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        fields.put(GetColumn_id(holder3.getLayoutPosition()), String.valueOf(isChecked));
                    }
                });
                return holder3;
            default:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_field, parent, false);
                final TextViewHolder holder = new TextViewHolder(view);

                holder.field.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void afterTextChanged(Editable s) {
                        if(!String.valueOf(s).equals(""))
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
    }
    private short GetColumn_id(int position){
        return (short)PartyInfoFields.get(position).getColumn_id();
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        final Field f = PartyInfoFields.get(position);

        switch (f.getType()) {
            case 0: {

                ((TextViewHolder) holder).Layout.setHint(f.getHint());
                ((TextViewHolder) holder).field.setInputType(f.getInputType());
                if(fields.containsKey((short)f.getColumn_id()))
                    ((TextViewHolder) holder).field.setText(fields.get((short)f.getColumn_id()));

                if (f.getIcon() != null) {
                    ((TextViewHolder) holder).Layout.setEndIconMode(TextInputLayout.END_ICON_CUSTOM);
                    ((TextViewHolder) holder).Layout.setEndIconDrawable(f.getIcon());

                    if (f.isData()) {
                        final DatePickerDialog.OnDateSetListener Datapicker = new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                ChangeData(year, monthOfYear, dayOfMonth, ((TextViewHolder) holder).field);
                            }
                        };
                        ((TextViewHolder) holder).Layout.setEndIconOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new DatePickerDialog(Objects.requireNonNull(inflater.getContext()), Datapicker,
                                        dateAndTime.get(Calendar.YEAR),
                                        dateAndTime.get(Calendar.MONTH),
                                        dateAndTime.get(Calendar.DAY_OF_MONTH))
                                        .show();
                            }
                        });

                    }
                } else

                if (f.isDoubleSize()) {
                    ((TextViewHolder) holder).field.setGravity(Gravity.START | Gravity.TOP);
                    ((TextViewHolder) holder).field.setMinLines(4);
                    ((TextViewHolder) holder).field.setLines(6);
                    ((TextViewHolder) holder).field.setSingleLine(false);
                    ((TextViewHolder) holder).field.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
                }
                break;
            } //Текст
            case 1: {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(inflater.getContext(), f.getEntries(), android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                ((SpinerHolder) holder).spiner.setAdapter(adapter);
                // ((PartyInfoAdapter.ViewHolderSpiner) holder).txtHint.setText(f.getHint());
                ((SpinerHolder) holder).txtHint.setVisibility(View.GONE);

                if (fields.containsKey((short) f.getColumn_id())) {

                    final String[] id = inflater.getContext().getResources().getStringArray(f.getEntries());
                    int CurrentID = 0;
                    for (String name : id) {
                        if (name.equals(fields.get((short) f.getColumn_id())))
                            break;
                        CurrentID++;
                    }
                    if(CurrentID < id.length)
                        ((SpinerHolder) holder).spiner.setSelection(CurrentID, true);
                }
                break;
            } //Спинер
            case 3: {
                ((SwitchHolder) holder).switchButton.setText(String.format("%s  ", f.getHint()));
                if(fields.containsKey((short)f.getColumn_id()))
                    ((SwitchHolder) holder).switchButton.setChecked(Boolean.parseBoolean(fields.get((short)f.getColumn_id())));
            } //Чекбокс
                break;
        }
    }

    private void ChangeData(int year, int monthOfYear, int dayOfMonth, EditText Edt) {
        monthOfYear = monthOfYear + 1;
        dateAndTime.set(Calendar.YEAR, year);
        dateAndTime.set(Calendar.MONTH, monthOfYear);
        dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        int month = monthOfYear;
        String formattedMonth = "" + month;
        String formattedDayOfMonth = "" + dayOfMonth;

        if (month < 10) {
            formattedMonth = "0" + month;
        }
        if (dayOfMonth < 10) {
            formattedDayOfMonth = "0" + dayOfMonth;
        }
        Edt.setText(MessageFormat.format("{0} . {1} . {2}", formattedDayOfMonth, formattedMonth, String.valueOf(year)));
    }

    @Override
    public int getItemCount() {
        return PartyInfoFields.size();
    }
}