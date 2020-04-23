package com.bmvl.lk.ui.ProbyMenu.Probs.Sample;

import androidx.recyclerview.widget.DiffUtil;

import com.bmvl.lk.data.models.Samples;

import java.util.List;

public class SamplesDiffUtilCallback extends DiffUtil.Callback{
    private final List<Samples> oldList;
    private final List<Samples> newList;

    public SamplesDiffUtilCallback(List<Samples> oldList, List<Samples> newList) {
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
        Samples oldProduct = oldList.get(oldItemPosition);
        Samples newProduct = newList.get(newItemPosition);
        return oldProduct.getId() == newProduct.getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Samples oldProduct = oldList.get(oldItemPosition);
        Samples newProduct = newList.get(newItemPosition);
        return oldProduct.getProby_id() == newProduct.getProby_id()
                && oldProduct.getNumber()== newProduct.getNumber();
    }
}
