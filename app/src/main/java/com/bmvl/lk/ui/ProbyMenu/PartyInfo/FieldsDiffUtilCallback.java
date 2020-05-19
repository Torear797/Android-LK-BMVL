package com.bmvl.lk.ui.ProbyMenu.PartyInfo;

import androidx.recyclerview.widget.DiffUtil;

import com.bmvl.lk.data.Field;

import java.util.List;

class FieldsDiffUtilCallback extends DiffUtil.Callback {
    private final List<Field> oldList;
    private final List<Field> newList;

    FieldsDiffUtilCallback(List<Field> oldList, List<Field> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        Field oldProduct = oldList.get(oldItemPosition);
        Field newProduct = newList.get(newItemPosition);
        return oldProduct.getColumn_id() == newProduct.getColumn_id();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Field oldProduct = oldList.get(oldItemPosition);
        Field newProduct = newList.get(newItemPosition);
        return oldProduct.getHint().equals(newProduct.getHint())
                && oldProduct.getType() == newProduct.getType();
    }
}
