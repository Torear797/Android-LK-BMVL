package com.bmvl.lk.ui.search;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bmvl.lk.R;
import com.bmvl.lk.ViewHolders.SpinerHolder;
import com.bmvl.lk.ViewHolders.TextViewHolder;
import com.google.android.material.textfield.TextInputLayout;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Objects;

class SearchFieldsAdapter extends RecyclerView.Adapter {
    private LayoutInflater inflater;
    private static Calendar dateAndTime = Calendar.getInstance();
    // private AsyncTask mAsyncTask;

    SearchFieldsAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position) {
        if (SearchFragment.Fields.get(position).isSpener()) return R.layout.item_spiner;
        else return R.layout.item_field;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(viewType, parent, false);
        if (viewType == R.layout.item_spiner)
            return new SpinerHolder(view);
        else
            return new TextViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        final SearchField f = SearchFragment.Fields.get(position);

        if (f.isSpener()) {
            final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(inflater.getContext(), f.getEntries(), android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            ((SpinerHolder) holder).spiner.setAdapter(adapter);
            ((SpinerHolder) holder).txtHint.setText(f.getHint());
        } else {

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
        return SearchFragment.Fields.size();
    }
}