package com.bmvl.lk.ui.ProbyMenu.Probs;

import androidx.recyclerview.widget.DiffUtil;

import com.bmvl.lk.Rest.Order.ProbyRest;
import com.bmvl.lk.data.models.Proby;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProbDiffUtilCallback extends DiffUtil.Callback{
    private final Map<Short, ProbyRest> oldList;
    private final Map<Short, ProbyRest> newList;

    public ProbDiffUtilCallback(Map<Short, ProbyRest> oldList, Map<Short, ProbyRest> newList) {
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
        ProbyRest oldProduct = oldList.get(getPositionKey(oldItemPosition,oldList));
        ProbyRest newProduct = newList.get(getPositionKey(newItemPosition,newList));
        return oldProduct.getId() == newProduct.getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        ProbyRest oldProduct = oldList.get(getPositionKey(oldItemPosition,oldList));
        ProbyRest newProduct = newList.get(getPositionKey(newItemPosition,newList));
        return oldProduct.getSamples() == newProduct.getSamples()
                && oldProduct.getFields() == newProduct.getFields();
    }
    private Short getPositionKey(int position, Map<Short, ProbyRest> Probs){
        return new ArrayList<Short>(Probs.keySet()).get(position);
    }
}
