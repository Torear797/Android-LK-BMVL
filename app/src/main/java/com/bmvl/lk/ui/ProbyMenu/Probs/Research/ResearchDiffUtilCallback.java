package com.bmvl.lk.ui.ProbyMenu.Probs.Research;

import androidx.recyclerview.widget.DiffUtil;

import com.bmvl.lk.Rest.Order.ResearchRest;
import com.bmvl.lk.data.models.Research;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ResearchDiffUtilCallback extends DiffUtil.Callback {
    private final Map<Short, ResearchRest> oldList;
    private final Map<Short, ResearchRest> newList;

    public ResearchDiffUtilCallback(Map<Short, ResearchRest> oldList, Map<Short, ResearchRest> newList) {
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
        ResearchRest oldProduct = oldList.get(getPositionKey(oldItemPosition,oldList));
        ResearchRest newProduct = newList.get(getPositionKey(newItemPosition,newList));
        return oldProduct.getId() == newProduct.getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        ResearchRest oldProduct = oldList.get(getPositionKey(oldItemPosition,oldList));
        ResearchRest newProduct = newList.get(getPositionKey(newItemPosition,newList));
        return oldProduct.getPrice() == newProduct.getPrice()
                && oldProduct.getTypeId() == newProduct.getTypeId() ;
    }
    private Short getPositionKey(int position, Map<Short, ResearchRest> Research){
        return new ArrayList<Short>(Research.keySet()).get(position);
    }
}
