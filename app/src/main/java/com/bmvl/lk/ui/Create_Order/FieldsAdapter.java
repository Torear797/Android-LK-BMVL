package com.bmvl.lk.ui.Create_Order;

import android.app.DatePickerDialog;
import android.content.Context;
import android.text.Editable;
import android.text.InputType;
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

import com.bmvl.lk.App;
import com.bmvl.lk.R;
import com.bmvl.lk.ViewHolders.OriginalDocHolder;
import com.bmvl.lk.ViewHolders.SelectButtonHolder;
import com.bmvl.lk.ViewHolders.SpinerHolder;
import com.bmvl.lk.ViewHolders.SwitchHolder;
import com.bmvl.lk.ViewHolders.TextViewHolder;
import com.google.android.material.textfield.TextInputLayout;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Objects;

public class FieldsAdapter extends RecyclerView.Adapter {
    private LayoutInflater inflater;
    private static Calendar dateAndTime = Calendar.getInstance();

    FieldsAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position) {
        return CreateOrderActivity.Fields.get(position).getType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case 1:
                View view1 = inflater.inflate(R.layout.item_spiner, parent, false);
                return new SpinerHolder(view1);
            case 2:
                View view2 = inflater.inflate(R.layout.item_original_doc, parent, false);
                return new OriginalDocHolder(view2);
            case 3:
                View view3 = inflater.inflate(R.layout.item_check_button, parent, false);
                return new SwitchHolder(view3);
            case 4:
                View view4 = inflater.inflate(R.layout.item_button_select, parent, false);
                return new SelectButtonHolder(view4);
            default:
                View view = inflater.inflate(R.layout.item_field, parent, false);
                return new TextViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        final Field f = CreateOrderActivity.Fields.get(position);

        switch (f.getType()) {
            case 0://Текстовое поле
                if (f.getInputType() == InputType.TYPE_NULL)
                    ((TextViewHolder) holder).Layout.setBoxBackgroundColor(inflater.getContext().getResources().getColor(R.color.field_inactive));

                ((TextViewHolder) holder).Layout.setHint(f.getHint());
                ((TextViewHolder) holder).field.setInputType(f.getInputType());
                ((TextViewHolder) holder).field.setText(f.getValue());

                if (f.getIcon() != null) {
                    ((TextViewHolder) holder).Layout.setEndIconMode(TextInputLayout.END_ICON_CUSTOM);
                    ((TextViewHolder) holder).Layout.setEndIconDrawable(f.getIcon());

                    if (f.isData()) {
                        final DatePickerDialog.OnDateSetListener Datapicker = new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                ChangeData(year, monthOfYear, dayOfMonth, ((TextViewHolder) holder).field, f.getColumn_id());
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
                }

                if (f.isDoubleSize()) {
                    ((TextViewHolder) holder).field.setGravity(Gravity.START | Gravity.TOP);
                    ((TextViewHolder) holder).field.setMinLines(4);
                    ((TextViewHolder) holder).field.setLines(6);
                    ((TextViewHolder) holder).field.setSingleLine(false);
                    ((TextViewHolder) holder).field.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
                }

                ((TextViewHolder) holder).field.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void afterTextChanged(Editable s) {
                       // CreateOrderActivity.Fields.get(position).setValue(String.valueOf(s));
                        CreateOrderActivity.order.getFields().put((short) f.getColumn_id(), String.valueOf(s));
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }
                });
                break;
            case 1: //Одиночный спинер
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(inflater.getContext(), f.getEntries(), android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                ((SpinerHolder) holder).spiner.setAdapter(adapter);
                ((SpinerHolder) holder).txtHint.setText(f.getHint());
                CreateOrderActivity.order.getFields().put((short) f.getColumn_id(), String.valueOf(((SpinerHolder) holder).spiner.getSelectedItem()));

                AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        // Получаем выбранный объект
                        String item = (String) parent.getItemAtPosition(position);
                       // CreateOrderActivity.Fields.get(position).setValue(String.valueOf(item));
                        CreateOrderActivity.order.getFields().put((short) f.getColumn_id(), String.valueOf(item));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                };
                ((SpinerHolder) holder).spiner.setOnItemSelectedListener(itemSelectedListener);
                break;
            case 2: //Оригиналы документов
                ArrayAdapter<CharSequence> adapterOriginalDoc = ArrayAdapter.createFromResource(inflater.getContext(), f.getEntries(), android.R.layout.simple_spinner_item);
                adapterOriginalDoc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                ((OriginalDocHolder) holder).spiner.setAdapter(adapterOriginalDoc);
                ((OriginalDocHolder) holder).txtHint.setText(f.getHint());

                CreateOrderActivity.order.getFields().put(App.OrderInfo.getOD_ID(), String.valueOf(App.OrderInfo.getOD_Value()));

                switch (App.OrderInfo.getOD_ID()) {
                    case 52:
                        ((OriginalDocHolder) holder).spiner.setSelection(0);
                        ((OriginalDocHolder) holder).fieldAdres.setText(App.OrderInfo.getOD_Value());
                        break;
                    case 63:
                        ((OriginalDocHolder) holder).spiner.setSelection(1);
                        ((OriginalDocHolder) holder).fieldAdres.setText(App.OrderInfo.getOD_Value());
                        break;
                    case 64:
                        ((OriginalDocHolder) holder).spiner.setSelection(2);
                        ((OriginalDocHolder) holder).fieldEmail.setText(App.OrderInfo.getOD_Value());
                        break;
                }

                ((OriginalDocHolder) holder).spiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View itemSelected, int selectedItemPosition, long selectedId) {

                        if (selectedItemPosition == 0 || selectedItemPosition == 1) {
                            ((OriginalDocHolder) holder).fieldEmail.setVisibility(View.GONE);
                            ((OriginalDocHolder) holder).LayoutEmail.setVisibility(View.GONE);
                        }

                        switch (selectedItemPosition) {
                            case 0:
                                ((OriginalDocHolder) holder).LayoutAdres.setHint(inflater.getContext().getResources().getString(R.string.Doc_Face));
                                f.setColumn_id(52);
                                break;
                            case 1:
                                ((OriginalDocHolder) holder).LayoutAdres.setHint(inflater.getContext().getResources().getString(R.string.adres));
                                f.setColumn_id(63);
                                break;
                            case 2:
                                ((OriginalDocHolder) holder).fieldEmail.setVisibility(View.VISIBLE);
                                ((OriginalDocHolder) holder).LayoutEmail.setVisibility(View.VISIBLE);
                                ((OriginalDocHolder) holder).LayoutAdres.setHint(inflater.getContext().getResources().getString(R.string.adres));
                                f.setColumn_id(64);
                                break;
                        }
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });


                ((OriginalDocHolder) holder).fieldEmail.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void afterTextChanged(Editable s) {
                     //   CreateOrderActivity.Fields.get(position).setValue(String.valueOf(s));
                        CreateOrderActivity.order.getFields().put((short) f.getColumn_id(), String.valueOf(s));
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }
                });

                ((OriginalDocHolder) holder).fieldAdres.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void afterTextChanged(Editable s) {
                       // CreateOrderActivity.Fields.get(position).setValue(String.valueOf(s));
                        CreateOrderActivity.order.getFields().put((short) f.getColumn_id(), String.valueOf(s));
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }
                });
                break;
            case 3: //Свич
                ((SwitchHolder) holder).switchButton.setText(String.format("%s  ", f.getHint()));
                CreateOrderActivity.order.getFields().put((short) f.getColumn_id(), String.valueOf(((SwitchHolder) holder).switchButton.isChecked()));

                ((SwitchHolder) holder).switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                     //   CreateOrderActivity.Fields.get(position).setValue(String.valueOf(isChecked));
                        CreateOrderActivity.order.getFields().put((short) f.getColumn_id(), String.valueOf(isChecked));
                    }
                });
                break;
            case 4: //Акт
                ((SelectButtonHolder) holder).hint.setText(f.getHint());
                break;
        }
    }

    private void ChangeData(int year, int monthOfYear, int dayOfMonth, EditText Edt, int position) {
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
        CreateOrderActivity.order.getFields().put((short) position, MessageFormat.format("{0}.{1}.{2}", formattedDayOfMonth, formattedMonth, year));
        //CreateOrderActivity.Fields.get(position).setValue(MessageFormat.format("{0}.{1}.{2}", formattedDayOfMonth, formattedMonth, year));
        Edt.setText(MessageFormat.format("{0} . {1} . {2}", formattedDayOfMonth, formattedMonth, year));
    }

    @Override
    public int getItemCount() {
        return CreateOrderActivity.Fields.size();
    }
}