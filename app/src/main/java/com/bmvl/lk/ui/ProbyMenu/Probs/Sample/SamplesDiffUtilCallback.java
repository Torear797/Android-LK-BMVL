package com.bmvl.lk.ui.ProbyMenu.Probs.Sample;

import androidx.recyclerview.widget.DiffUtil;

import com.bmvl.lk.Rest.Order.SamplesRest;

import java.util.ArrayList;
import java.util.Map;

public class SamplesDiffUtilCallback extends DiffUtil.Callback{
    private final Map<Short, SamplesRest> oldList;
    private final Map<Short, SamplesRest> newList;

    public SamplesDiffUtilCallback(Map<Short, SamplesRest> oldList, Map<Short, SamplesRest> newList) {
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
        SamplesRest oldProduct = oldList.get(getPositionKey(oldItemPosition,oldList));
        SamplesRest newProduct = newList.get(getPositionKey(newItemPosition,newList));
        return oldProduct.getId() == newProduct.getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        SamplesRest oldProduct = oldList.get(getPositionKey(oldItemPosition,oldList));
        SamplesRest newProduct = newList.get(getPositionKey(newItemPosition,newList));
        return oldProduct.getFields() == newProduct.getFields()
                && oldProduct.getResearches() == newProduct.getResearches();
    }

    private Short getPositionKey(int position, Map<Short, SamplesRest> Research){
        return new ArrayList<Short>(Research.keySet()).get(position);
    }
}
