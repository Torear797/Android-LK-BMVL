package com.bmvl.lk.ui.create_order;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
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
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bmvl.lk.App;
import com.bmvl.lk.R;
import com.bmvl.lk.Rest.NetworkService;
import com.bmvl.lk.ViewHolders.DataFieldHolder;
import com.bmvl.lk.ViewHolders.OriginalDocHolder;
import com.bmvl.lk.ViewHolders.SelectButtonHolder;
import com.bmvl.lk.ViewHolders.SpinerHolder;
import com.bmvl.lk.ViewHolders.SwitchHolder;
import com.bmvl.lk.ViewHolders.TextViewHolder;
import com.bmvl.lk.data.Field;
import com.bmvl.lk.data.StringSpinnerAdapter;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class FieldsAdapter extends RecyclerView.Adapter {
    private static Calendar dateAndTime = Calendar.getInstance();
    private static Map<Short, String> OrderFields;

    private FieldsAdapter.ClickFieldListener onClickFieldListener;

    FieldsAdapter(ClickFieldListener Listener) {
        this.onClickFieldListener = Listener;
        OrderFields = CreateOrderActivity.order.getFields();
    }

    public interface ClickFieldListener {
        void onLoadActOfSelection(TextView path);
    }

    @Override
    public int getItemViewType(int position) {
        return CreateOrderActivity.Fields.get(position).getType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case 1: {
                View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_spiner, parent, false);
                final SpinerHolder holder1 = new SpinerHolder(view1);

                AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        OrderFields.put(GetColumn_id(holder1.getLayoutPosition()), String.valueOf(parent.getItemAtPosition(position)));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                };
                holder1.spiner.setOnItemSelectedListener(itemSelectedListener);

                return holder1;
            }//Спинер
            case 2: {
                View view2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_original_doc, parent, false);
                return new OriginalDocHolder(view2);
            }//DocOriginal
            case 3: {
                View view3 = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_check_button, parent, false);
                final SwitchHolder holder3 = new SwitchHolder(view3);

                holder3.switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        OrderFields.put(GetColumn_id(holder3.getLayoutPosition()), String.valueOf(isChecked));
                    }
                });

                return holder3;
            } //item_check_button
            case 4: {
                View view4 = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_button_select, parent, false);
                return new SelectButtonHolder(view4);
            }//select btn
            case 5: {
                View view5 = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_box_text, parent, false);
                return new BoxAndTextHolder(view5);
            }//item_box_text
            case 6: {
                View view6 = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_data_field, parent, false);

                final DataFieldHolder holder = new DataFieldHolder(view6);
                    holder.field.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void afterTextChanged(Editable s) {
                            if (!String.valueOf(s).equals("") && s.toString().length() <=10)
                            try {
                                OrderFields.put(GetColumn_id(holder.getLayoutPosition()), new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Objects.requireNonNull(new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).parse(s.toString()))));
                            } catch (ParseException e) {
                                e.printStackTrace();
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
            }//DataField
            default: {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_field, parent, false);
                final TextViewHolder holder = new TextViewHolder(view);

                holder.field.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void afterTextChanged(Editable s) {
                        if ((CreateOrderActivity.Fields.get(holder.getLayoutPosition()).getInputType() != InputType.TYPE_NULL
                                || CreateOrderActivity.Fields.get(holder.getLayoutPosition()).getColumn_id() == 7)
                                && !String.valueOf(s).equals(""))
                            OrderFields.put(GetColumn_id(holder.getLayoutPosition()), String.valueOf(s));

                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }
                });

                return holder;
            }//EditText
        }
    }

    private short GetColumn_id(int position) {
        return (short) CreateOrderActivity.Fields.get(position).getColumn_id();
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        final Field f = CreateOrderActivity.Fields.get(position);
        switch (f.getType()) {
            case 0: {
                ((TextViewHolder) holder).Layout.setHint(f.getHint());

                if (f.getInputType() == InputType.TYPE_NULL)
                    ((TextViewHolder) holder).Layout.setBoxBackgroundColor(((TextViewHolder) holder).Layout.getContext().getResources().getColor(R.color.field_inactive));

                ((TextViewHolder) holder).field.setInputType(f.getInputType());

                if (f.getColumn_id() != -1)
                    ((TextViewHolder) holder).field.setText(OrderFields.get((short) f.getColumn_id()));
                else ((TextViewHolder) holder).field.setText(f.getValue());

                if (f.getIcon() != null) {
                    ((TextViewHolder) holder).Layout.setEndIconMode(TextInputLayout.END_ICON_CUSTOM);
                    ((TextViewHolder) holder).Layout.setEndIconDrawable(f.getIcon());
                } else

//                    if (f.isData()) {
//                        final DatePickerDialog.OnDateSetListener Datapicker = new DatePickerDialog.OnDateSetListener() {
//                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                                ChangeData(year, monthOfYear, dayOfMonth, ((TextViewHolder) holder).field, f.getColumn_id());
//                            }
//                        };
//                        ((TextViewHolder) holder).Layout.setEndIconOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                new DatePickerDialog(Objects.requireNonNull(inflater.getContext()), Datapicker,
//                                        dateAndTime.get(Calendar.YEAR),
//                                        dateAndTime.get(Calendar.MONTH),
//                                        dateAndTime.get(Calendar.DAY_OF_MONTH))
//                                        .show();
//                            }
//                        });
//
//                    }
//                } else
                    if (f.isDoubleSize()) {
                        ((TextViewHolder) holder).field.setGravity(Gravity.START | Gravity.TOP);
                        ((TextViewHolder) holder).field.setMinLines(4);
                        ((TextViewHolder) holder).field.setLines(6);
                        ((TextViewHolder) holder).field.setSingleLine(false);
                        ((TextViewHolder) holder).field.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
                    }
                break;
            } //Текстовое поле
            case 1: {
               // ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(((SpinerHolder) holder).spiner.getContext(), f.getEntries(), android.R.layout.simple_spinner_item);
                StringSpinnerAdapter adapter = new StringSpinnerAdapter(((SpinerHolder) holder).spiner.getContext(), android.R.layout.simple_spinner_item,((SpinerHolder) holder).spiner.getContext().getResources().getStringArray(f.getEntries()) , ((SpinerHolder) holder).spiner);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                ((SpinerHolder) holder).spiner.setAdapter(adapter);
                ((SpinerHolder) holder).txtHint.setText(f.getHint());

               // if (OrderFields.containsKey((short) f.getColumn_id()))
                    ((SpinerHolder) holder).spiner.setSelection(adapter.getPosition(OrderFields.get((short) f.getColumn_id())));

                break;
            } //Одиночный спинер
            case 2: {
//                ArrayAdapter<CharSequence> adapterOriginalDoc = ArrayAdapter.createFromResource(((OriginalDocHolder) holder).spiner.getContext(), f.getEntries(), android.R.layout.simple_spinner_item);
//                adapterOriginalDoc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                ((OriginalDocHolder) holder).spiner.setAdapter(adapterOriginalDoc);
//                ((OriginalDocHolder) holder).txtHint.setText(f.getHint());
//
//                switch (App.OrderInfo.getOD_ID()) {
//                    case 52:
//                        ((OriginalDocHolder) holder).spiner.setSelection(0);
//                        ((OriginalDocHolder) holder).fieldAdres.setText(App.OrderInfo.getFIO());
//                        break;
//                    case 63:
//                        ((OriginalDocHolder) holder).spiner.setSelection(1);
//                        ((OriginalDocHolder) holder).fieldAdres.setText(App.OrderInfo.getOD_Adres());
//                       // ((OriginalDocHolder) holder).fieldEmail.setText(App.OrderInfo.getOD_Email());
//                        break;
//                    case 64:
//                        ((OriginalDocHolder) holder).spiner.setSelection(2);
//                        ((OriginalDocHolder) holder).fieldAdres.setText(App.OrderInfo.getOD_Adres());
//                        ((OriginalDocHolder) holder).fieldEmail.setText(App.OrderInfo.getOD_Email());
//                        break;
//                }
                break;
            } //Оригиналы документов
            case 3: {
                ((SwitchHolder) holder).switchButton.setText(String.format("%s  ", f.getHint()));
                if (OrderFields.containsKey((short) f.getColumn_id()))
                    ((SwitchHolder) holder).switchButton.setChecked(Boolean.parseBoolean(OrderFields.get((short) f.getColumn_id())));
                else
                    OrderFields.put((short) f.getColumn_id(), String.valueOf(((SwitchHolder) holder).switchButton.isChecked()));

                break;
            } //Свич
            case 4: {
                // ((SelectButtonHolder) holder).hint.setText(f.getHint());
                if (f.getValue() != null && !f.getValue().equals("")) {
                    //  ((SelectButtonHolder) holder).path.setText(MessageFormat.format("{0}/{1}", NetworkService.getServerUrl(), f.getValue()));

                    ((SelectButtonHolder) holder).path.setText(android.text.Html.fromHtml("<u>Загруженный файл</u>"));
                    ((SelectButtonHolder) holder).path.setTextColor(Color.parseColor("#0066cc"));
                    ((SelectButtonHolder) holder).path.setOnClickListener(v -> ((SelectButtonHolder) holder).path.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(MessageFormat.format("{0}/{1}", NetworkService.getServerUrl(), f.getValue())))));
                    // ((SelectButtonHolder) holder).path.setText(MessageFormat.format("<a href=\"{0}", MessageFormat.format("{0}/{1}", NetworkService.getServerUrl(), f.getValue() + "\">Загруженный файл</a>")));
                }

                ((SelectButtonHolder) holder).select_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickFieldListener.onLoadActOfSelection(((SelectButtonHolder) holder).path);
                    }
                });

                break;
            }//Акт
            case 6: {
                ((DataFieldHolder) holder).Layout.setHint(f.getHint());

                if(OrderFields.containsKey((short) f.getColumn_id()))
                try {
                    ((DataFieldHolder) holder).field.setText(new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Objects.requireNonNull(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(Objects.requireNonNull(OrderFields.get((short) f.getColumn_id()))))));
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                final DatePickerDialog.OnDateSetListener Datapicker = (view, year, monthOfYear, dayOfMonth) -> ChangeData(year, monthOfYear, dayOfMonth, ((DataFieldHolder) holder).field);
                if (f.getColumn_id() != 137) {
                    ((DataFieldHolder) holder).Layout.setEndIconOnClickListener(v -> new DatePickerDialog(Objects.requireNonNull(((DataFieldHolder) holder).Layout.getContext()), Datapicker,
                            dateAndTime.get(Calendar.YEAR),
                            dateAndTime.get(Calendar.MONTH),
                            dateAndTime.get(Calendar.DAY_OF_MONTH))
                            .show());
                } else {
                    ((DataFieldHolder) holder).field.setInputType(f.getInputType());
                    ((DataFieldHolder) holder).Layout.setBoxBackgroundColor(((DataFieldHolder) holder).Layout.getContext().getResources().getColor(R.color.field_inactive));
                }
                break;
            }//DataField
        }
    }

    private void ChangeData(int year, int monthOfYear, int dayOfMonth, EditText Edt) {
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
        //OrderFields.put((short) position, MessageFormat.format("{2}-{1}-{0}", formattedDayOfMonth, formattedMonth, String.valueOf(year)));
        Edt.setText(MessageFormat.format("{0}.{1}.{2}", formattedDayOfMonth, formattedMonth, String.valueOf(year)));
        Edt.requestFocus();
        Edt.setSelection(Edt.getText().length());
    }

    @Override
    public int getItemCount() {
        return CreateOrderActivity.Fields.size();
    }

    private class BoxAndTextHolder extends RecyclerView.ViewHolder {
        final SwitchMaterial switchButton;
        public TextInputEditText field;
        final TextInputLayout Layout;

        BoxAndTextHolder(View view5) {
            super(view5);
            switchButton = itemView.findViewById(R.id.my_switch);
            field = itemView.findViewById(R.id.TextInput);
            Layout = itemView.findViewById(R.id.TextLayout);

            switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    OrderFields.put(GetColumn_id(getLayoutPosition()), String.valueOf(isChecked));
                    if (isChecked) Layout.setVisibility(View.VISIBLE);
                    else Layout.setVisibility(View.GONE);
                }
            });

            field.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                    if (!String.valueOf(s).equals("") && Layout.getVisibility() == View.VISIBLE)
                        OrderFields.put((short) 67, String.valueOf(s));
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }
            });
        }
    }
}