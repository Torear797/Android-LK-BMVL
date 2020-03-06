package com.bmvl.lk.ui.ProbyMenu.Probs;

import androidx.recyclerview.widget.DiffUtil;

import com.bmvl.lk.models.Research;

import java.util.List;

public class ResearchDiffUtilCallback extends DiffUtil.Callback {
    private final List<Research> oldList;
    private final List<Research> newList;

    public ResearchDiffUtilCallback(List<Research> oldList, List<Research> newList) {
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
        Research oldProduct = oldList.get(oldItemPosition);
        Research newProduct = newList.get(newItemPosition);
        return oldProduct.getId_indicator() == newProduct.getId_indicator();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Research oldProduct = oldList.get(oldItemPosition);
        Research newProduct = newList.get(newItemPosition);
        return oldProduct.getId_method() == newProduct.getId_method()
                && oldProduct.getId_research() == newProduct.getId_research();
    }
}
