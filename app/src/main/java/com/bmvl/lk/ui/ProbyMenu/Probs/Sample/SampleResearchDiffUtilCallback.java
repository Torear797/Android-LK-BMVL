package com.bmvl.lk.ui.ProbyMenu.Probs.Sample;

import androidx.recyclerview.widget.DiffUtil;

import com.bmvl.lk.models.SamplesResearch;

import java.util.List;

class SampleResearchDiffUtilCallback extends DiffUtil.Callback{
    private final List<SamplesResearch> oldList;
    private final List<SamplesResearch> newList;

    public SampleResearchDiffUtilCallback(List<SamplesResearch> oldList, List<SamplesResearch> newList) {
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
        SamplesResearch oldProduct = oldList.get(oldItemPosition);
        SamplesResearch newProduct = newList.get(newItemPosition);
        return oldProduct.getId() == newProduct.getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        SamplesResearch oldProduct = oldList.get(oldItemPosition);
        SamplesResearch newProduct = newList.get(newItemPosition);
        return oldProduct.getId_method() == newProduct.getId_method()
                && oldProduct.getId_indicator() == newProduct.getId_indicator()
                && oldProduct.getSample_id() == newProduct.getSample_id();
    }
}
