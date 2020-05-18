package com.bmvl.lk.ViewHolders;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bmvl.lk.R;
import com.bmvl.lk.ui.create_order.CreateOrderActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class OriginalDocHolder extends RecyclerView.ViewHolder {
    final public Spinner spiner;
    final public TextView txtHint;

    final public TextInputEditText fieldEmail;
    final public TextInputLayout LayoutEmail;

    final public TextInputEditText fieldAdres;
    final public TextInputLayout LayoutAdres;
    public OriginalDocHolder(@NonNull View itemView) {
        super(itemView);

        spiner = itemView.findViewById(R.id.spinner);
        txtHint = itemView.findViewById(R.id.hint);

        fieldEmail = itemView.findViewById(R.id.EmailInput);
        LayoutEmail = itemView.findViewById(R.id.EmailLayout);

        fieldAdres = itemView.findViewById(R.id.AdresInput);
        LayoutAdres = itemView.findViewById(R.id.AdresLayout);


       spiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View itemSelected, int selectedItemPosition, long selectedId) {

                if (selectedItemPosition == 0 || selectedItemPosition == 1) {
                    fieldEmail.setVisibility(View.GONE);
                    LayoutEmail.setVisibility(View.GONE);
                }

                switch (selectedItemPosition) {
                    case 0:
                        LayoutAdres.setHint(parent.getContext().getResources().getString(R.string.Doc_Face));
                        CreateOrderActivity.Fields.get(getLayoutPosition()).setColumn_id(52);
                        break;
                    case 1:
                        LayoutAdres.setHint(parent.getContext().getResources().getString(R.string.adres));
                        CreateOrderActivity.Fields.get(getLayoutPosition()).setColumn_id(63);
                        break;
                    case 2:
                        fieldEmail.setVisibility(View.VISIBLE);
                        LayoutEmail.setVisibility(View.VISIBLE);
                        LayoutAdres.setHint(parent.getContext().getResources().getString(R.string.adres));
                        CreateOrderActivity.Fields.get(getLayoutPosition()).setColumn_id(64);
                        break;
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        fieldEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                CreateOrderActivity.order.getFields().put(GetColumn_id(getLayoutPosition()), String.valueOf(s));
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        fieldAdres.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                CreateOrderActivity.order.getFields().put(GetColumn_id(getLayoutPosition()), String.valueOf(s));
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
    }
    private short GetColumn_id(int position){
        return (short) CreateOrderActivity.Fields.get(position).getColumn_id();
    }
}
