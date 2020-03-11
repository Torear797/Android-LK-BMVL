package com.bmvl.lk.ui.Create_Order;

import android.app.DatePickerDialog;
import android.content.Context;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bmvl.lk.R;
import com.bmvl.lk.ViewHolders.OriginalDocHolder;
import com.bmvl.lk.ViewHolders.SelectButtonHolder;
import com.bmvl.lk.ViewHolders.SpinerHolder;
import com.bmvl.lk.ViewHolders.SwitchHolder;
import com.bmvl.lk.ViewHolders.TextViewHolder;
import com.google.android.material.textfield.TextInputLayout;

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
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        final Field f = CreateOrderActivity.Fields.get(position);

        if (f.getType() == 0) {
            if (f.getInputType() == InputType.TYPE_NULL) {
                ((TextViewHolder) holder).Layout.setBoxBackgroundColor(inflater.getContext().getResources().getColor(R.color.field_inactive));
            }

            ((TextViewHolder) holder).Layout.setHint(f.getHint());
            ((TextViewHolder) holder).field.setInputType(f.getInputType());
            ((TextViewHolder) holder).field.setText(f.getValue());

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
            }

            if (f.isDoubleSize()) {
                ((TextViewHolder) holder).field.setGravity(Gravity.START | Gravity.TOP);
                ((TextViewHolder) holder).field.setMinLines(4);
                ((TextViewHolder) holder).field.setLines(6);
                ((TextViewHolder) holder).field.setSingleLine(false);
                ((TextViewHolder) holder).field.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
            }
        } else if (f.getType() == 1) {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(inflater.getContext(), f.getEntries(), android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            ((SpinerHolder) holder).spiner.setAdapter(adapter);
            ((SpinerHolder) holder).txtHint.setText(f.getHint());
        } else if (f.getType() == 2) {

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(inflater.getContext(), f.getEntries(), android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            ((OriginalDocHolder) holder).spiner.setAdapter(adapter);
            ((OriginalDocHolder) holder).txtHint.setText(f.getHint());

            ((OriginalDocHolder) holder).spiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent,
                                           View itemSelected, int selectedItemPosition, long selectedId) {

                    if (selectedItemPosition == 0 || selectedItemPosition == 1) {
                        ((OriginalDocHolder) holder).fieldEmail.setVisibility(View.GONE);
                        ((OriginalDocHolder) holder).LayoutEmail.setVisibility(View.GONE);

                        if (selectedItemPosition == 0)
                            ((OriginalDocHolder) holder).LayoutAdres.setHint(inflater.getContext().getResources().getString(R.string.Doc_Face));
                        else
                            ((OriginalDocHolder) holder).LayoutAdres.setHint(inflater.getContext().getResources().getString(R.string.adres));

                    } else {
                        ((OriginalDocHolder) holder).fieldEmail.setVisibility(View.VISIBLE);
                        ((OriginalDocHolder) holder).LayoutEmail.setVisibility(View.VISIBLE);
                        ((OriginalDocHolder) holder).LayoutAdres.setHint(inflater.getContext().getResources().getString(R.string.adres));
                    }

                }

                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }

        switch (f.getType()) {
            case 3:
                ((SwitchHolder) holder).switchButton.setText(String.format("%s  ", f.getHint()));
                break;
            case 4:
                ((SelectButtonHolder) holder).hint.setText(f.getHint());
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
        Edt.setText(formattedDayOfMonth + "-" + formattedMonth + "-" + year);
    }

    @Override
    public int getItemCount() {
        return CreateOrderActivity.Fields.size();
    }
}