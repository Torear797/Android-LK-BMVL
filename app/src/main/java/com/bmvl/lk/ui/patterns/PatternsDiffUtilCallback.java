package com.bmvl.lk.ui.patterns;

import androidx.recyclerview.widget.DiffUtil;

import com.bmvl.lk.data.models.Orders;
import com.bmvl.lk.data.models.Pattern;

import java.util.List;

public class PatternsDiffUtilCallback extends DiffUtil.Callback{
    private final List<Pattern> oldList;
    private final List<Pattern> newList;

    public PatternsDiffUtilCallback(List<Pattern> oldList, List<Pattern> newList) {
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
        Pattern oldProduct = oldList.get(oldItemPosition);
        Pattern newProduct = newList.get(newItemPosition);
        return oldProduct.getId() == newProduct.getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Pattern oldProduct = oldList.get(oldItemPosition);
        Pattern newProduct = newList.get(newItemPosition);
        return oldProduct.getDate().equals(newProduct.getDate())
                && oldProduct.getType_id() == newProduct.getType_id()
                && oldProduct.getStatus_id() == newProduct.getStatus_id()
                && oldProduct.getPatternName().equals(newProduct.getPatternName())
                && oldProduct.getUser_id() == newProduct.getUser_id();
    }
}
