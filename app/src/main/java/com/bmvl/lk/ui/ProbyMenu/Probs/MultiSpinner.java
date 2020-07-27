package com.bmvl.lk.ui.ProbyMenu.Probs;

import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bmvl.lk.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;
import java.util.Objects;

public class MultiSpinner extends androidx.appcompat.widget.AppCompatSpinner implements DialogInterface.OnMultiChoiceClickListener, DialogInterface.OnCancelListener {

    private List<String> items;
    private boolean[] selected;
    private String defaultText;
    private MultiSpinnerListener listener;
    private String Title;

    public MultiSpinner(Context context) {
        super(context);
    }

    public MultiSpinner(Context arg0, AttributeSet arg1) {
        super(arg0, arg1);
    }

    public MultiSpinner(Context arg0, AttributeSet arg1, int arg2) {
        super(arg0, arg1, arg2);
    }

    @Override
    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
        selected[which] = isChecked;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        // refresh text on spinner
        StringBuilder spinnerBuffer = new StringBuilder();
        boolean someUnselected = false;


        for (int i = 0; i < items.size(); i++) {
            if (selected[i]) {
                spinnerBuffer.append(items.get(i));
                spinnerBuffer.append(",");
            } else {
                someUnselected = true;
            }
        }

        String spinnerText;
        if (someUnselected) {
            spinnerText = spinnerBuffer.toString();
            if (spinnerText.length() > 1)
                spinnerText = spinnerText.substring(0, spinnerText.length() - 1);
        } else {
            spinnerText = defaultText;
        }
       // ArrayAdapter<String> adapter = ;
       // MultiAutoCompleteAdapter adapter = new MultiAutoCompleteAdapter(getContext(), R.layout.item_for_multi_auto_complete, new String[]{spinnerText});

        setAdapter(new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item,
                new String[]{spinnerText}));
        listener.onItemsSelected(selected, spinnerText);
    }

    @Override
    public boolean performClick() {
        final MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(Objects.requireNonNull(getContext()));
        final LayoutInflater inflater = ((AppCompatActivity) getContext()).getLayoutInflater();
        final View MyView = inflater.inflate(R.layout.search_field, null, false);

        final TextView MyTitle = MyView.findViewById(R.id.Title);
        final TextInputEditText SearchField = MyView.findViewById(R.id.TextInput);
        MyTitle.setText(Title);

//        SearchField.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void afterTextChanged(Editable s) {
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//            }
//        });

        builder
                .setCustomTitle(MyView)
                .setMultiChoiceItems(items.toArray(new CharSequence[items.size()]), selected, this)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> dialog.cancel())
                .setOnCancelListener(this)
                .show();

        //new MultiSpinerDialog(items, selected, this).show(((AppCompatActivity) getContext()).getSupportFragmentManager(), "MultiSpinnerDialog");

        return true;
    }

    public void setItems(List<String> items, String allText, MultiSpinnerListener listener,String Title) {
        this.items = items;
        this.defaultText = allText;
        this.listener = listener;
        this.Title = Title;

        String[] strArr = allText.split(",");
        int[] numArr = new int[strArr.length];
        int index;
        if (!allText.equals(""))
            for (int i = 0; i < strArr.length; i++) {
                index = 0;
                for (String name : items) {
                    if (name.equals(strArr[i]))
                        numArr[i] = index;
                    index++;
                }
            }

        // all selected by default
        selected = new boolean[items.size()];
        for (int i = 0; i < selected.length; i++) {
            selected[i] = false;
            if (!allText.equals(""))
                for (int value : numArr) {
                    if (i == value) {
                        selected[i] = true;
                        break;
                    }
                }
        }


        // all text on the spinner

      //  MultiAutoCompleteAdapter adapter = new MultiAutoCompleteAdapter(Objects.requireNonNull(getContext()), R.layout.item_for_multi_auto_complete, new String[]{allText});
     //    ArrayAdapter<String> adapter = ;
        setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, new String[]{allText}));
    }

    public interface MultiSpinnerListener {
        public void onItemsSelected(boolean[] selected, String selectedID);
    }
}