package com.bmvl.lk.ui.ProbyMenu.PartyInfo;

import android.app.DatePickerDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bmvl.lk.R;
import com.bmvl.lk.ViewHolders.DataFieldHolder;
import com.bmvl.lk.ViewHolders.SpinerHolder;
import com.bmvl.lk.ViewHolders.SwitchHolder;
import com.bmvl.lk.ViewHolders.TextViewHolder;
import com.bmvl.lk.data.Field;
import com.bmvl.lk.ui.ProbyMenu.Probs.ProbAdapter;
import com.bmvl.lk.ui.create_order.CreateOrderActivity;
import com.google.android.material.textfield.TextInputLayout;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PartyInfoAdapter extends RecyclerView.Adapter {
    private static Calendar dateAndTime = Calendar.getInstance();
    private List<Field> PartyInfoFields;
    private Map<Short, String> fields;
    private boolean ReadOnly;
    private Map<String, String> fieldsProb;

    PartyInfoAdapter(List<Field> PartyFields, boolean read, Map<String, String> FieldsProb) {
        this.ReadOnly = read;
        PartyInfoFields = PartyFields;
        this.fieldsProb = FieldsProb;
        fields = CreateOrderActivity.order.getFields();
    }

    @Override
    public int getItemViewType(int position) {
        //return PartyInfoFields.get(position).getType();

        switch (PartyInfoFields.get(position).getType()) {
            case 1:
                return R.layout.item_spiner;
            case 2:
                return R.layout.item_data_field;
            case 3:
                return R.layout.item_check_button;
            default:
                return R.layout.item_field;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        switch (viewType) {
            case R.layout.item_spiner: {
                //   View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_spiner, parent, false);
                final SpinerHolder holder1 = new SpinerHolder(view);

                AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (fieldsProb == null)
                            fields.put(GetColumn_id(holder1.getLayoutPosition()), String.valueOf(parent.getItemAtPosition(position)));
                        else
                            fieldsProb.put(String.valueOf(GetColumn_id(holder1.getLayoutPosition())), String.valueOf(parent.getItemAtPosition(position)));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                };
                if (!ReadOnly)
                    holder1.spiner.setOnItemSelectedListener(itemSelectedListener);
                return holder1;
            }
            case R.layout.item_data_field: {
                final DataFieldHolder holder = new DataFieldHolder(view);
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
            case R.layout.item_check_button: {
                final SwitchHolder holder3 = new SwitchHolder(view);

                if (!ReadOnly)
                    holder3.switchButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
                        fields.put(GetColumn_id(holder3.getLayoutPosition()), String.valueOf(isChecked));

                        if (GetColumn_id(holder3.getLayoutPosition()) == (byte) 121)
                            ProbAdapter.adapter.notifyDataSetChanged();
                    });


                holder3.switchButton.setChecked(false);
                return holder3;
            }
            default: {
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
        }
    }

    private short GetColumn_id(int position) {
        return (short) PartyInfoFields.get(position).getColumn_id();
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        final Field f = PartyInfoFields.get(position);

        switch (f.getType()) {
            case 0: {

                ((TextViewHolder) holder).Layout.setHint(f.getHint());
                ((TextViewHolder) holder).field.setInputType(f.getInputType());
                // if (fields.containsKey((short) f.getColumn_id()))
                if (fieldsProb == null || ReadOnly)
                    ((TextViewHolder) holder).field.setText(fields.get((short) f.getColumn_id()));
                else
                    ((TextViewHolder) holder).field.setText(fieldsProb.get(String.valueOf(f.getColumn_id())));

                if (f.getIcon() != null) {
                    ((TextViewHolder) holder).Layout.setEndIconMode(TextInputLayout.END_ICON_CUSTOM);
                    ((TextViewHolder) holder).Layout.setEndIconDrawable(f.getIcon());

                } else if (f.isDoubleSize()) {
                    ((TextViewHolder) holder).field.setGravity(Gravity.START | Gravity.TOP);
                    ((TextViewHolder) holder).field.setMinLines(4);
                    ((TextViewHolder) holder).field.setLines(6);
                    ((TextViewHolder) holder).field.setSingleLine(false);
                    ((TextViewHolder) holder).field.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
                }

                if (ReadOnly) ((TextViewHolder) holder).field.setEnabled(false);
                else ((TextViewHolder) holder).field.setEnabled(true);

                break;
            } //Текст
            case 1: {
                //   ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(((SpinerHolder) holder).spiner.getContext(), f.getSpinerData(), android.R.layout.simple_spinner_item);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(((SpinerHolder) holder).spiner.getContext(), android.R.layout.simple_spinner_item, f.getSpinerData());

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                ((SpinerHolder) holder).spiner.setAdapter(adapter);
                ((SpinerHolder) holder).txtHint.setVisibility(View.GONE);
                if (fieldsProb == null || ReadOnly)
                    ((SpinerHolder) holder).spiner.setSelection(adapter.getPosition(fields.get((short) f.getColumn_id())));
                else
                    ((SpinerHolder) holder).spiner.setSelection(adapter.getPosition(fieldsProb.get(String.valueOf(f.getColumn_id()))));

                if (ReadOnly) ((SpinerHolder) holder).spiner.setEnabled(false);
                else ((SpinerHolder) holder).spiner.setEnabled(true);
                break;
            } //Спинер
            case 2: {
                ((DataFieldHolder) holder).Layout.setHint(f.getHint());

                if (fieldsProb == null || ReadOnly)
                    ((DataFieldHolder) holder).field.setText(fields.get((short) f.getColumn_id()));
                else
                    ((DataFieldHolder) holder).field.setText(fieldsProb.get(String.valueOf(f.getColumn_id())));

                if (ReadOnly) ((DataFieldHolder) holder).field.setEnabled(false);
                else ((DataFieldHolder) holder).field.setEnabled(true);



                final DatePickerDialog.OnDateSetListener Datapicker = (view, year, monthOfYear, dayOfMonth) -> ChangeData(year, monthOfYear, dayOfMonth, ((DataFieldHolder) holder).field, f.getColumn_id());
                if(!ReadOnly)
                ((DataFieldHolder) holder).Layout.setEndIconOnClickListener(v -> new DatePickerDialog(Objects.requireNonNull(((DataFieldHolder) holder).Layout.getContext()), Datapicker,
                        dateAndTime.get(Calendar.YEAR),
                        dateAndTime.get(Calendar.MONTH),
                        dateAndTime.get(Calendar.DAY_OF_MONTH))
                        .show());
                break;
            } //DataField
            case 3: {
                ((SwitchHolder) holder).switchButton.setText(String.format("%s  ", f.getHint()));
                if (fields.containsKey((short) f.getColumn_id()))
                    ((SwitchHolder) holder).switchButton.setChecked(Boolean.parseBoolean(fields.get((short) f.getColumn_id())));

                if (ReadOnly) ((SwitchHolder) holder).switchButton.setEnabled(false);
                else ((SwitchHolder) holder).switchButton.setEnabled(true);
            } //Чекбокс
            break;
        }
    }

    private void ChangeData(int year, int monthOfYear, int dayOfMonth, EditText Edt, int position) {
        monthOfYear = monthOfYear + 1;
//        dateAndTime.set(Calendar.YEAR, year);
//        dateAndTime.set(Calendar.MONTH, monthOfYear);
//        dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        int month = monthOfYear;
        String formattedMonth = "" + month;
        String formattedDayOfMonth = "" + dayOfMonth;

        if (month < 10) {
            formattedMonth = "0" + month;
        }
        if (dayOfMonth < 10) {
            formattedDayOfMonth = "0" + dayOfMonth;
        }

        if (fieldsProb == null)
            fields.put((short) position, MessageFormat.format("{0}.{1}.{2}", formattedDayOfMonth, formattedMonth, String.valueOf(year)));
        else
            fieldsProb.put(String.valueOf(position), MessageFormat.format("{0}.{1}.{2}", formattedDayOfMonth, formattedMonth, String.valueOf(year)));


        Edt.setText(MessageFormat.format("{0} . {1} . {2}", formattedDayOfMonth, formattedMonth, String.valueOf(year)));
        Edt.requestFocus();
        Edt.setSelection(Edt.getText().length());
    }

    @Override
    public int getItemCount() {
        return PartyInfoFields.size();
    }
}