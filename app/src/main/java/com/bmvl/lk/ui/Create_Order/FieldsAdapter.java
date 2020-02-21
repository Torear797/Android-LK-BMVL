package com.bmvl.lk.ui.Create_Order;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bmvl.lk.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;
import java.util.Objects;

public class FieldsAdapter extends RecyclerView.Adapter<FieldsAdapter.ViewHolder>{
    private LayoutInflater inflater;
    private Context MyContext;
    private static Calendar dateAndTime = Calendar.getInstance();

    FieldsAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.MyContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_field, parent, false);
        return new FieldsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Field f = CreateOrderActivity.Fields.get(position);
        holder.Layout.setHint(f.getHint());
        holder.field.setInputType(f.getInputType());
        holder.field.setText(f.getValue());

        if(f.getIcon() != null){
            holder.Layout.setEndIconMode(TextInputLayout.END_ICON_CUSTOM);
            holder.Layout.setEndIconDrawable(f.getIcon());

            if(f.isData()){
                final DatePickerDialog.OnDateSetListener Datapicker = new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        ChangeData(year,monthOfYear,dayOfMonth, holder.field);
                    }
                };
                holder.Layout.setEndIconOnClickListener(new View.OnClickListener() {
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

        if(f.isDoubleSize()){
            holder.field.setGravity(Gravity.START | Gravity.TOP);
            holder.field.setMinLines(4);
            holder.field.setLines(6);
            holder.field.setSingleLine(false);
            holder.field.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
           // holder.field.setFilters(new InputFilter[] {new InputFilter.LengthFilter(33)});
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
        return CreateOrderActivity.Fields.size();
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
}
