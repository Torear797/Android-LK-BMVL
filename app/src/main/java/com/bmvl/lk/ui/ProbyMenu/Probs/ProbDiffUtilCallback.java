package com.bmvl.lk.ui.ProbyMenu.Probs;

import androidx.recyclerview.widget.DiffUtil;

import com.bmvl.lk.models.Proby;

import java.util.List;

public class ProbDiffUtilCallback extends DiffUtil.Callback{
    private final List<Proby> oldList;
    private final List<Proby> newList;

    public ProbDiffUtilCallback(List<Proby> oldList, List<Proby> newList) {
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
        Proby oldProduct = oldList.get(oldItemPosition);
        Proby newProduct = newList.get(newItemPosition);
        return oldProduct.getId() == newProduct.getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Proby oldProduct = oldList.get(oldItemPosition);
        Proby newProduct = newList.get(newItemPosition);
        return oldProduct.getProtocol().equals(newProduct.getProtocol())
                && oldProduct.getPrice() == newProduct.getPrice();
    }
}
