package com.bmvl.lk.data;

import android.app.Activity;
import android.content.Context;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bmvl.lk.R;
import com.google.android.material.chip.Chip;
import com.tokenautocomplete.TokenCompleteTextView;

import java.util.Objects;

public class ContactsCompletionView extends TokenCompleteTextView<String> {

    public ContactsCompletionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected View getViewForObject(String text) {

        LayoutInflater l = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        Chip view = (Chip) Objects.requireNonNull(l).inflate(R.layout.item_chip, (ViewGroup) getParent(), false);
        view.setText(text);

//        view.setOnCloseIconClickListener(v -> {
//            Toast.makeText(view.getContext(), "Deleted !", Toast.LENGTH_SHORT).show();
//           removeObjectAsync(text);
//        });

        return view;
    }

    @Override
    protected String defaultObject(String completionText) {
        return completionText;
    }

}