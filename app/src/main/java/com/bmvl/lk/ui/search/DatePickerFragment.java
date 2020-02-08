package com.bmvl.lk.ui.search;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Objects;

class DatePickerFragment extends DialogFragment  implements DatePickerDialog.OnDateSetListener{
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Используем по умолчанию текущую дату для диалога выбора
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Создаем экземпляр класса DatePickerDialog и возвращаем его
        return new DatePickerDialog(Objects.requireNonNull(getActivity()), this, year, month, day);
    }
}
