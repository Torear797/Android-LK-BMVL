package com.bmvl.lk.ui.ProbyMenu.Probs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bmvl.lk.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;
import java.util.Objects;

public class MultiSpinerDialog extends DialogFragment {
    private TextInputEditText SearchField;

    private List<String> items;
    private DialogInterface.OnMultiChoiceClickListener listener;
    private boolean[] selected;

    public MultiSpinerDialog(List<String> MyItems, boolean[] checkedItems, DialogInterface.OnMultiChoiceClickListener listener ) {
        this.items = MyItems;
        this.selected = checkedItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(Objects.requireNonNull(getContext()));
        final LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        final View MyView = inflater.inflate(R.layout.search_field, null, false);

        SearchField = MyView.findViewById(R.id.TextInput);

        SearchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                //  new ChoceMaterialDialogFragment.MyTask(s.toString().toLowerCase()).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });


        return builder
                .setCustomTitle(MyView)
                .setMultiChoiceItems(items.toArray(new CharSequence[items.size()]), selected, listener)
                .setPositiveButton("Выбрать", (dialog, which) -> {
                    //  autoCompleteTextView.setText(LastValue);
                    dismiss();
                })
                .create();
    }


//    @Override
//    public void onCancel(DialogInterface dialog) {
//        // refresh text on spinner
//        StringBuilder spinnerBuffer = new StringBuilder();
//        boolean someUnselected = false;
//
//
//        for (int i = 0; i < items.size(); i++) {
//            if (selected[i]) {
//                spinnerBuffer.append(items.get(i));
//                spinnerBuffer.append(",");
//            } else {
//                someUnselected = true;
//            }
//        }
//
//        String spinnerText;
//        if (someUnselected) {
//            spinnerText = spinnerBuffer.toString();
//            if (spinnerText.length() > 1)
//                spinnerText = spinnerText.substring(0, spinnerText.length() - 1);
//        } else {
//            spinnerText = defaultText;
//        }
//        // ArrayAdapter<String> adapter = ;
//        // MultiAutoCompleteAdapter adapter = new MultiAutoCompleteAdapter(getContext(), R.layout.item_for_multi_auto_complete, new String[]{spinnerText});
//
//        setAdapter(new ArrayAdapter<>(getContext(),
//                android.R.layout.simple_spinner_item,
//                new String[]{spinnerText}));
//        listener.onItemsSelected(selected, spinnerText);
//    }
}