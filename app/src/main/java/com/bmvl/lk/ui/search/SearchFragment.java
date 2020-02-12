package com.bmvl.lk.ui.search;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import androidx.fragment.app.Fragment;
import com.bmvl.lk.OnBackPressedListener;
import com.bmvl.lk.R;
import com.google.android.material.textfield.TextInputLayout;
import java.util.Calendar;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment implements OnBackPressedListener {

    private static Calendar dateAndTime = Calendar.getInstance();

    public SearchFragment() {
    }

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View MyView = inflater.inflate(R.layout.fragment_search, container, false);
       // MyActionBar.setTitle(R.string.search);

        final EditText Edit_ot = MyView.findViewById(R.id.Data_ot);
        final EditText Edit_Do = MyView.findViewById(R.id.Data_do);

        final TextInputLayout textInputCustomEndIcon = MyView.findViewById(R.id.textInputLayout3);
        final TextInputLayout textInputCustomEndIcon2 = MyView.findViewById(R.id.textInputLayout4);

        final DatePickerDialog.OnDateSetListener sd = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                ChangeData(year,monthOfYear,dayOfMonth,Edit_ot);
            }
        };
        final DatePickerDialog.OnDateSetListener sd2 = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                ChangeData(year,monthOfYear,dayOfMonth,Edit_Do);
            }
        };
        textInputCustomEndIcon.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowCalendar(sd);
            }
        });
        textInputCustomEndIcon2.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowCalendar(sd2);
            }
        });

        return MyView;
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
    private void ShowCalendar(DatePickerDialog.OnDateSetListener sd){
        new DatePickerDialog(Objects.requireNonNull(getContext()), sd,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
                .show();
    }
    @Override
    public void onBackPressed() {

    }
}
