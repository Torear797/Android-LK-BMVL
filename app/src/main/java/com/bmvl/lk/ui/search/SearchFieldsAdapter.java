package com.bmvl.lk.ui.search;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bmvl.lk.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;

import java.util.Calendar;
import java.util.Objects;

class SearchFieldsAdapter extends RecyclerView.Adapter{
    private LayoutInflater inflater;
    private static Calendar dateAndTime = Calendar.getInstance();
     SearchFieldsAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemViewType(int position) {
         final SearchField sf = SearchFragment.Fields.get(position);
         if(sf.isSpener()){
             return 0;
         } else return 1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         if(viewType == 0){
             View view = inflater.inflate(R.layout.item_spiner, parent, false);
             return new SearchFieldsAdapter.SpinerViewHolder(view);
         } else {
             View view = inflater.inflate(R.layout.item_field, parent, false);
             return new SearchFieldsAdapter.EditTextViewHolder(view);
         }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        final SearchField f = SearchFragment.Fields.get(position);
       if(f.isSpener()){
           ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(inflater.getContext(), f.getEntries(), android.R.layout.simple_spinner_item);
           adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
           ((SpinerViewHolder)holder).spiner.setAdapter(adapter);
           ((SpinerViewHolder)holder).txtHint.setText(f.getHint());
       } else  {

           ((EditTextViewHolder)holder).Layout.setHint(f.getHint());
           ((EditTextViewHolder)holder).field.setInputType(f.getInputType());
           ((EditTextViewHolder)holder).field.setText(f.getValue());

           if(f.getIcon() != null){
               ((EditTextViewHolder)holder).Layout.setEndIconMode(TextInputLayout.END_ICON_CUSTOM);
               ((EditTextViewHolder)holder).Layout.setEndIconDrawable(f.getIcon());

               if(f.isData()){
                   final DatePickerDialog.OnDateSetListener Datapicker = new DatePickerDialog.OnDateSetListener() {
                       public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                           ChangeData(year,monthOfYear,dayOfMonth,  ((EditTextViewHolder)holder).field);
                       }
                   };
                   ((EditTextViewHolder)holder).Layout.setEndIconOnClickListener(new View.OnClickListener() {
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
    private void ChangeData(int year, int monthOfYear, int dayOfMonth, EditText Edt){
        monthOfYear=monthOfYear+1;
        dateAndTime.set(Calendar.YEAR, year);
        dateAndTime.set(Calendar.MONTH, monthOfYear);
        dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        int month = monthOfYear;
        String formattedMonth = "" + month;
        String formattedDayOfMonth = "" + dayOfMonth;

        if(month < 10){
            formattedMonth = "0" + month;
        }
        if(dayOfMonth < 10){
            formattedDayOfMonth = "0" + dayOfMonth;
        }
        Edt.setText(formattedDayOfMonth + "-" + formattedMonth + "-" + year);
    }

    @Override
    public int getItemCount() {
        return SearchFragment.Fields.size();
    }

    public class SpinerViewHolder extends RecyclerView.ViewHolder {
         final Spinner spiner;
         final MaterialTextView txtHint;
         SpinerViewHolder(@NonNull View itemView) {
            super(itemView);
            spiner = itemView.findViewById(R.id.spinner);
            txtHint = itemView.findViewById(R.id.hint);
        }
    }

    public class EditTextViewHolder extends RecyclerView.ViewHolder {
        final TextInputEditText field;
        final TextInputLayout Layout;
         EditTextViewHolder(@NonNull View itemView) {
            super(itemView);
            field = itemView.findViewById(R.id.TextInput);
            Layout = itemView.findViewById(R.id.TextLayout);
        }
    }
}
