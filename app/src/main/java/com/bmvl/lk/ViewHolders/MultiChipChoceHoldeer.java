package com.bmvl.lk.ViewHolders;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bmvl.lk.R;
import com.bmvl.lk.data.ContactsCompletionView;
import com.google.android.material.textfield.TextInputLayout;

public class MultiChipChoceHoldeer extends RecyclerView.ViewHolder{
    final public TextInputLayout textInputLayout;
    final public ContactsCompletionView contactsCompletionView;
    public MultiChipChoceHoldeer(@NonNull View itemView) {
        super(itemView);

        textInputLayout = itemView.findViewById(R.id.TextInputLayout);
        contactsCompletionView = itemView.findViewById(R.id.ContactsCompletionView);
    }
}
