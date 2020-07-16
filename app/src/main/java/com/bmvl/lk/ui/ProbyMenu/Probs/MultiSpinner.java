package com.bmvl.lk.ui.ProbyMenu.Probs;

import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

public class MultiSpinner extends androidx.appcompat.widget.AppCompatSpinner implements DialogInterface.OnMultiChoiceClickListener, DialogInterface.OnCancelListener {

    private List<String> items;
    private boolean[] selected;
    private String defaultText;
    private MultiSpinnerListener listener;

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
        StringBuffer spinnerBuffer = new StringBuffer();
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
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item,
                new String[] { spinnerText });
        setAdapter(adapter);
        listener.onItemsSelected(selected, spinnerText);
    }

    @Override
    public boolean performClick() {
        new MaterialAlertDialogBuilder(getContext())
                .setMultiChoiceItems(items.toArray(new CharSequence[items.size()]), selected, this)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setOnCancelListener(this)
                .show();
        return true;
    }

    public void setItems(List<String> items, String allText,
                         MultiSpinnerListener listener) {
        this.items = items;
        this.defaultText = allText;
        this.listener = listener;

        String[] strArr = allText.split(",");
        int[] numArr = new int[strArr.length];
        int index;
        if(!allText.equals(""))
        for (int i = 0; i < strArr.length; i++) {
            //numArr[i] = Integer.parseInt(strArr[i]);
            index = 0;
            for (String name: items) {
                if(name.equals(strArr[i]))
                    numArr[i] = index;
                index++;
            }
        }

        // all selected by default
        selected = new boolean[items.size()];
        for (int i = 0; i < selected.length; i++) {
            selected[i] = false;
            if(!allText.equals(""))
            for(int j = 0; j < numArr.length; j++){
                if(i == numArr[j]) {
                    selected[i] = true;
                    break;
                }
            }
        }


        // all text on the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, new String[] { allText });
        setAdapter(adapter);
    }

    public interface MultiSpinnerListener {
        public void onItemsSelected(boolean[] selected, String selectedID);
    }
}