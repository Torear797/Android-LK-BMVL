package com.bmvl.lk.ui.Create_Order;

import android.app.DatePickerDialog;
import android.content.Context;
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
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bmvl.lk.App;
import com.bmvl.lk.R;
import com.bmvl.lk.Rest.NetworkService;
import com.bmvl.lk.ViewHolders.OriginalDocHolder;
import com.bmvl.lk.ViewHolders.SelectButtonHolder;
import com.bmvl.lk.ViewHolders.SpinerHolder;
import com.bmvl.lk.ViewHolders.SwitchHolder;
import com.bmvl.lk.ViewHolders.TextViewHolder;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.Objects;

public class FieldsAdapter extends RecyclerView.Adapter {
    private LayoutInflater inflater;
    private static Calendar dateAndTime = Calendar.getInstance();
    private static Map<Short, String> OrderFields;

    FieldsAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        OrderFields = CreateOrderActivity.order.getFields();
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
            case 2:
                View view2 = inflater.inflate(R.layout.item_original_doc, parent, false);
                return new OriginalDocHolder(view2);
            case 3:
                View view3 = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_check_button, parent, false);
                final SwitchHolder holder3 = new SwitchHolder(view3);

                holder3.switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        OrderFields.put(GetColumn_id(holder3.getLayoutPosition()), String.valueOf(isChecked));
                    }
                });

                return holder3;
            case 4:
                View view4 = inflater.inflate(R.layout.item_button_select, parent, false);
                return new SelectButtonHolder(view4);
            case 5:
                View view5 = inflater.inflate(R.layout.item_box_text, parent, false);
                return new BoxAndTextHolder(view5);
            default:
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
                    ((TextViewHolder) holder).Layout.setBoxBackgroundColor(inflater.getContext().getResources().getColor(R.color.field_inactive));

                ((TextViewHolder) holder).field.setInputType(f.getInputType());

                if (f.getColumn_id() != -1)
                    ((TextViewHolder) holder).field.setText(OrderFields.get((short) f.getColumn_id()));
                else ((TextViewHolder) holder).field.setText(f.getValue());

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
                } else if (f.isDoubleSize()) {
                    ((TextViewHolder) holder).field.setGravity(Gravity.START | Gravity.TOP);
                    ((TextViewHolder) holder).field.setMinLines(4);
                    ((TextViewHolder) holder).field.setLines(6);
                    ((TextViewHolder) holder).field.setSingleLine(false);
                    ((TextViewHolder) holder).field.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
                }
                break;
            } //Текстовое поле
            case 1: {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(inflater.getContext(), f.getEntries(), android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                ((SpinerHolder) holder).spiner.setAdapter(adapter);
                ((SpinerHolder) holder).txtHint.setText(f.getHint());

                if (OrderFields.containsKey((short) f.getColumn_id())) {

//                    String[] id = inflater.getContext().getResources().getStringArray(f.getEntries());
//                    int CurrentID = 0;
//                    for (String name : id) {
//                        if (name.equals(OrderFields.get((short) f.getColumn_id())))
//                            break;
//                        CurrentID++;
//                    }
//                    if(CurrentID < id.length)
                    ((SpinerHolder) holder).spiner.setSelection(adapter.getPosition(OrderFields.get((short) f.getColumn_id())));
                }
                //else
//                    CreateOrderActivity.order.getFields().put((short) f.getColumn_id(), String.valueOf(((SpinerHolder) holder).spiner.getSelectedItem()));

                break;
            } //Одиночный спинер
            case 2: {
                ArrayAdapter<CharSequence> adapterOriginalDoc = ArrayAdapter.createFromResource(inflater.getContext(), f.getEntries(), android.R.layout.simple_spinner_item);
                adapterOriginalDoc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                ((OriginalDocHolder) holder).spiner.setAdapter(adapterOriginalDoc);
                ((OriginalDocHolder) holder).txtHint.setText(f.getHint());

                OrderFields.put(App.OrderInfo.getOD_ID(), String.valueOf(App.OrderInfo.getOD_Value()));

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

//                ((OriginalDocHolder) holder).spiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                    public void onItemSelected(AdapterView<?> parent, View itemSelected, int selectedItemPosition, long selectedId) {
//
//                        if (selectedItemPosition == 0 || selectedItemPosition == 1) {
//                            ((OriginalDocHolder) holder).fieldEmail.setVisibility(View.GONE);
//                            ((OriginalDocHolder) holder).LayoutEmail.setVisibility(View.GONE);
//                        }
//
//                        switch (selectedItemPosition) {
//                            case 0:
//                                ((OriginalDocHolder) holder).LayoutAdres.setHint(inflater.getContext().getResources().getString(R.string.Doc_Face));
//                                f.setColumn_id(52);
//                                break;
//                            case 1:
//                                ((OriginalDocHolder) holder).LayoutAdres.setHint(inflater.getContext().getResources().getString(R.string.adres));
//                                f.setColumn_id(63);
//                                break;
//                            case 2:
//                                ((OriginalDocHolder) holder).fieldEmail.setVisibility(View.VISIBLE);
//                                ((OriginalDocHolder) holder).LayoutEmail.setVisibility(View.VISIBLE);
//                                ((OriginalDocHolder) holder).LayoutAdres.setHint(inflater.getContext().getResources().getString(R.string.adres));
//                                f.setColumn_id(64);
//                                break;
//                        }
//                    }
//
//                    public void onNothingSelected(AdapterView<?> parent) {
//                    }
//                });

//                ((OriginalDocHolder) holder).fieldEmail.addTextChangedListener(new TextWatcher() {
//                    @Override
//                    public void afterTextChanged(Editable s) {
//                        OrderFields.put((short) f.getColumn_id(), String.valueOf(s));
//                    }
//
//                    @Override
//                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                    }
//
//                    @Override
//                    public void onTextChanged(CharSequence s, int start, int before, int count) {
//                    }
//                });
//
//                ((OriginalDocHolder) holder).fieldAdres.addTextChangedListener(new TextWatcher() {
//                    @Override
//                    public void afterTextChanged(Editable s) {
//                        OrderFields.put((short) f.getColumn_id(), String.valueOf(s));
//                    }
//
//                    @Override
//                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                    }
//
//                    @Override
//                    public void onTextChanged(CharSequence s, int start, int before, int count) {
//                    }
//                });
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
            case 4: //Акт
               // ((SelectButtonHolder) holder).hint.setText(f.getHint());
                if(f.getValue() != null && !f.getValue().equals("")){
                  //  ((SelectButtonHolder) holder).path.setText(MessageFormat.format("{0}/{1}", NetworkService.getServerUrl(), f.getValue()));

                    ((SelectButtonHolder) holder).path.setText(android.text.Html.fromHtml("<u>Загруженный файл</u>"));
                    ((SelectButtonHolder) holder).path.setTextColor(Color.parseColor("#0066cc"));
                    ((SelectButtonHolder) holder).path.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            inflater.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(MessageFormat.format("{0}/{1}", NetworkService.getServerUrl(), f.getValue()))));
                        }
                    });
                           // ((SelectButtonHolder) holder).path.setText(MessageFormat.format("<a href=\"{0}", MessageFormat.format("{0}/{1}", NetworkService.getServerUrl(), f.getValue() + "\">Загруженный файл</a>")));
                }
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
        OrderFields.put((short) position, MessageFormat.format("{0}.{1}.{2}", formattedDayOfMonth, formattedMonth, String.valueOf(year)));
        Edt.setText(MessageFormat.format("{0} . {1} . {2}", formattedDayOfMonth, formattedMonth, String.valueOf(year)));
    }

    @Override
    public int getItemCount() {
        return CreateOrderActivity.Fields.size();
    }

    private class BoxAndTextHolder extends RecyclerView.ViewHolder {
        final public SwitchMaterial switchButton;
        public TextInputEditText field;
        public final TextInputLayout Layout;

        public BoxAndTextHolder(View view5) {
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