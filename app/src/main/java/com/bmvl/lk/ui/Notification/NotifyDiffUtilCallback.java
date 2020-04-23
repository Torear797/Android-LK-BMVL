package com.bmvl.lk.ui.Notification;

import androidx.recyclerview.widget.DiffUtil;

import com.bmvl.lk.data.models.Notifications;

import java.util.List;

class NotifyDiffUtilCallback extends DiffUtil.Callback{
    private final List<Notifications> oldList;
    private final List<Notifications> newList;

    public NotifyDiffUtilCallback(List<Notifications> oldList, List<Notifications> newList) {
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
        Notifications oldProduct = oldList.get(oldItemPosition);
        Notifications newProduct = newList.get(newItemPosition);
        return oldProduct.getId() == newProduct.getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        Notifications oldProduct = oldList.get(oldItemPosition);
        Notifications newProduct = newList.get(newItemPosition);
        return     oldProduct.getDate().equals(newProduct.getDate())
                && oldProduct.getOrder_id() == newProduct.getOrder_id()
                && oldProduct.getStatus() == newProduct.getStatus()
                && oldProduct.getEvent().equals(newProduct.getEvent())
                ;
    }
}
