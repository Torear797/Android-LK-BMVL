package com.bmvl.lk.data;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bmvl.lk.R;

public class StringSpinnerAdapter extends ArrayAdapter<String> {
    public StringSpinnerAdapter(Context context, int textViewResourceId, String[] objects, Spinner spiner) {
        super(context, textViewResourceId, objects);
        this.mySpinner = spiner;
    }
    private Spinner mySpinner;

//    private int mSelectedIndex = -1;
//
//    public void setSelection(int position) {
//        mSelectedIndex = position;
//        notifyDataSetChanged();
//    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent){
//        View spinnerItem = LayoutInflater.from(mySpinner.getContext()).inflate(spinerItem, null);
////
////        TextView mytext = spinnerItem.findViewById(android.R.id.text1);
////        mytext.setText(myArray[position]);
////
////        //int selected = Spinner.

////        if(position == selected){
////            spinnerItem.setBackgroundColor(Color.rgb(56, 184, 226));
////        }
////        return spinnerItem;

        View itemView = super.getDropDownView(position, convertView, parent);

        if (position == mySpinner.getSelectedItemPosition()) {
            itemView.setBackgroundColor(getContext().getResources().getColor(R.color.field_inactive));
        } else {
            itemView.setBackgroundColor(Color.TRANSPARENT);
        }
        return itemView;

    }
}