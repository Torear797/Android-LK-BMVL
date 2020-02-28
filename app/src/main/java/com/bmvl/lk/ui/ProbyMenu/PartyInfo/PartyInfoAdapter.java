package com.bmvl.lk.ui.ProbyMenu.PartyInfo;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bmvl.lk.R;
import com.bmvl.lk.ui.Create_Order.Field;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;
import java.util.Objects;

public class PartyInfoAdapter extends RecyclerView.Adapter{
    private LayoutInflater inflater;
    private Context MyContext;
    private static Calendar dateAndTime = Calendar.getInstance();

    PartyInfoAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.MyContext = context;
    }
    @Override
    public int getItemViewType(int position) {
        return PartyInfoFragment.PartyInfoFields.get(position).getType();
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                View view = inflater.inflate(R.layout.item_field, parent, false);
                return new PartyInfoAdapter.ViewHolder(view);
            case 1:
                View view1 = inflater.inflate(R.layout.item_spiner, parent, false);
                return new PartyInfoAdapter.ViewHolderSpiner(view1);

            case 3:
                View view3 = inflater.inflate(R.layout.item_check_button, parent, false);
                return new PartyInfoAdapter.ViewHolderSwitch(view3);

        }

        View view = inflater.inflate(R.layout.item_field, parent, false);
        return new PartyInfoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        final Field f = PartyInfoFragment.PartyInfoFields.get(position);

        switch (f.getType()){
            case 0:{

                ((PartyInfoAdapter.ViewHolder) holder).Layout.setHint(f.getHint());
                ((PartyInfoAdapter.ViewHolder) holder).field.setInputType(f.getInputType());
                ((PartyInfoAdapter.ViewHolder) holder).field.setText(f.getValue());

                if (f.getIcon() != null) {
                    ((PartyInfoAdapter.ViewHolder) holder).Layout.setEndIconMode(TextInputLayout.END_ICON_CUSTOM);
                    ((PartyInfoAdapter.ViewHolder) holder).Layout.setEndIconDrawable(f.getIcon());

                    if (f.isData()) {
                        final DatePickerDialog.OnDateSetListener Datapicker = new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                ChangeData(year, monthOfYear, dayOfMonth, ((PartyInfoAdapter.ViewHolder) holder).field);
                            }
                        };
                        ((PartyInfoAdapter.ViewHolder) holder).Layout.setEndIconOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new DatePickerDialog(Objects.requireNonNull(MyContext), Datapicker,
                                        dateAndTime.get(Calendar.YEAR),
                                        dateAndTime.get(Calendar.MONTH),
                                        dateAndTime.get(Calendar.DAY_OF_MONTH))
                                        .show();
                            }
                        });

                    }
                }

                if (f.isDoubleSize()) {
                    ((PartyInfoAdapter.ViewHolder) holder).field.setGravity(Gravity.START | Gravity.TOP);
                    ((PartyInfoAdapter.ViewHolder) holder).field.setMinLines(4);
                    ((PartyInfoAdapter.ViewHolder) holder).field.setLines(6);
                    ((PartyInfoAdapter.ViewHolder) holder).field.setSingleLine(false);
                    ((PartyInfoAdapter.ViewHolder) holder).field.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
                }
                break;
            }
            case 1:{
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(inflater.getContext(), f.getEntries(), android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                ((PartyInfoAdapter.ViewHolderSpiner) holder).spiner.setAdapter(adapter);
               // ((PartyInfoAdapter.ViewHolderSpiner) holder).txtHint.setText(f.getHint());
                ((PartyInfoAdapter.ViewHolderSpiner) holder).txtHint.setVisibility(View.GONE);
                break;
            }
            case 3:
                ((PartyInfoAdapter.ViewHolderSwitch) holder).switchButton.setText(String.format("%s  ", f.getHint()));
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
        return PartyInfoFragment.PartyInfoFields.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextInputEditText field;
        final TextInputLayout Layout;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            field = itemView.findViewById(R.id.TextInput);
            Layout = itemView.findViewById(R.id.TextLayout);
        }
    }

    public class ViewHolderSpiner extends PartyInfoAdapter.ViewHolder {
        final Spinner spiner;
        final TextView txtHint;

        ViewHolderSpiner(View view1) {
            super(view1);
            spiner = itemView.findViewById(R.id.spinner);
            txtHint = itemView.findViewById(R.id.hint);
        }
    }

    public class ViewHolderSwitch extends RecyclerView.ViewHolder {
        final SwitchMaterial switchButton;
        ViewHolderSwitch(View view3) {
            super(view3);
            switchButton = itemView.findViewById(R.id.my_switch);
        }
    }
}
