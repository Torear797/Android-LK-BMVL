package com.bmvl.lk.ui.Notification;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bmvl.lk.R;
import com.bmvl.lk.data.models.Notifications;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

class NotifiSwipeAdapter extends RecyclerSwipeAdapter<NotifiSwipeAdapter.SimpleViewHolder> {
    private static List<Notifications> Notifi;
    //private LayoutInflater inflater;

    private OnNotifyClickListener onNotifyClickListener;

    NotifiSwipeAdapter(List<Notifications> notifi, OnNotifyClickListener onNotifyClickListener) {
        //  this.inflater = LayoutInflater.from(context);
        this.onNotifyClickListener = onNotifyClickListener;
        Notifi = notifi;
    }

    public interface OnNotifyClickListener {
        void onNotifyClick(int position, int id);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @NonNull
    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //View view = inflater.inflate(R.layout.item_notifi, parent, false);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notifi, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotifiSwipeAdapter.SimpleViewHolder simpleViewHolder, int i) {

        final Notifications notifi = Notifi.get(i);
        //simpleViewHolder.Data.setText(notifi.getDate());
        try {
            simpleViewHolder.Data.setText(new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()).format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(notifi.getDate())));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        simpleViewHolder.Event.setText(notifi.getEvent());

        simpleViewHolder.Order.setText(notifi.getOrder_type());

        if (notifi.getStatus() == 0) {
            simpleViewHolder.status.setImageResource(R.drawable.ic_old_notifi);
            simpleViewHolder.Data.setTextColor(simpleViewHolder.Data.getContext().getResources().getColor(R.color.notify_old_color));
            simpleViewHolder.Event.setTextColor(simpleViewHolder.Event.getContext().getResources().getColor(R.color.notify_old_color));
            simpleViewHolder.Order.setTextColor(simpleViewHolder.Order.getContext().getResources().getColor(R.color.notify_old_color));
            simpleViewHolder.swipeLayout.setSwipeEnabled(false);
        } else {
            simpleViewHolder.status.setImageResource(R.drawable.ic_new_notifi);
            simpleViewHolder.Data.setTextColor(simpleViewHolder.Data.getContext().getResources().getColor(R.color.text_order_field_color));
            simpleViewHolder.Event.setTextColor(simpleViewHolder.Event.getContext().getResources().getColor(R.color.text_order_field_color));
            simpleViewHolder.Order.setTextColor(simpleViewHolder.Order.getContext().getResources().getColor(R.color.text_order_field_color));
        }


        simpleViewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        simpleViewHolder.swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
                YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.read));
            }
        });

        mItemManger.bindView(simpleViewHolder.itemView, i);
    }

    @Override
    public int getItemCount() {
        return Notifi.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipeNotifi;
    }

    public void insertdata(List<Notifications> insertList) {
        NotifyDiffUtilCallback diffUtilCallback = new NotifyDiffUtilCallback(Notifi, insertList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtilCallback, false);
        Notifi.addAll(insertList);
        // CheckEmpty();
        diffResult.dispatchUpdatesTo(this);
    }

    public void updateList(List<Notifications> newList) {
        NotifyDiffUtilCallback diffUtilCallback = new NotifyDiffUtilCallback(Notifi, newList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtilCallback, false);
        Notifi.clear();
        Notifi.addAll(newList);
        diffResult.dispatchUpdatesTo(this);
    }

    class SimpleViewHolder extends RecyclerView.ViewHolder {
        final TextView Data, Event, Order;
        final ImageView status, buttonRead;
        final SwipeLayout swipeLayout;

        SimpleViewHolder(@NonNull View itemView) {
            super(itemView);
            Data = itemView.findViewById(R.id.Data_notifi);
            Event = itemView.findViewById(R.id.event);
            Order = itemView.findViewById(R.id.Order_name);
            status = itemView.findViewById(R.id.status_notifi);

            swipeLayout = itemView.findViewById(R.id.swipeNotifi);
            buttonRead = itemView.findViewById(R.id.read);

            buttonRead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Notifications Notify = Notifi.get(getLayoutPosition());
                    closeAllItems();
                    swipeLayout.close();
                    if (Notify.getStatus() == 1) {
                        Notify.setStatus(0);
                        onNotifyClickListener.onNotifyClick(getLayoutPosition(), Notify.getId());
                    }
                }
            });


        }
    }
}
