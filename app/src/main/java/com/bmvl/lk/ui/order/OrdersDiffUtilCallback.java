package com.bmvl.lk.ui.order;

import androidx.recyclerview.widget.DiffUtil;

import com.bmvl.lk.data.models.Orders;

import java.util.List;

class OrdersDiffUtilCallback extends DiffUtil.Callback{
    private final List<Orders> oldList;
    private final List<Orders> newList;

    public OrdersDiffUtilCallback(List<Orders> oldList, List<Orders> newList) {
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
        Orders oldProduct = oldList.get(oldItemPosition);
        Orders newProduct = newList.get(newItemPosition);
        return oldProduct.getId() == newProduct.getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Orders oldProduct = oldList.get(oldItemPosition);
        Orders newProduct = newList.get(newItemPosition);
        return oldProduct.getDate().equals(newProduct.getDate())
                && oldProduct.getType_id() == newProduct.getType_id();
    }
}
