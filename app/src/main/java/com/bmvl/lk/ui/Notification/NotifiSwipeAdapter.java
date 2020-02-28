package com.bmvl.lk.ui.Notification;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bmvl.lk.R;
import com.bmvl.lk.models.Notifications;
import com.bmvl.lk.ui.order.OrderFragment;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import java.util.List;

class NotifiSwipeAdapter extends RecyclerSwipeAdapter<NotifiSwipeAdapter.SimpleViewHolder> {
    private List<Notifications> Notifi;
    private LayoutInflater inflater;
    NotifiSwipeAdapter(Context context, List<Notifications> notifi) {
        this.Notifi = notifi;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_notifi, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotifiSwipeAdapter.SimpleViewHolder simpleViewHolder, int i) {

        Notifications notifi = Notifi.get(i);
        simpleViewHolder.Data.setText(notifi.getDate());
        simpleViewHolder.Event.setText(notifi.getEvent());

        int j = 1; //НЕ ВЕРНО. Уведомление содердит ИД заявки. По ИД надо найти тип.
        for (String itVar : OrderFragment.OrderTypes)
        {
            if(j == notifi.getOrder_id()){
                simpleViewHolder.Order.setText(itVar);
                break;
            }
            j++;
        }

        if(notifi.getStatus() == 0) simpleViewHolder.status.setImageResource(R.drawable.ic_old_notifi);
        else simpleViewHolder.status.setImageResource(R.drawable.ic_new_notifi);

        simpleViewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        simpleViewHolder.swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
                YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.trash_notifi));
            }
        });
        simpleViewHolder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(view.getContext(), "Deleted Notifi", Toast.LENGTH_SHORT).show();
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

    class SimpleViewHolder extends RecyclerView.ViewHolder {
        final TextView Data,Event,Order;
        final ImageView status, buttonDelete;
        final SwipeLayout swipeLayout;
        SimpleViewHolder(@NonNull View itemView) {
            super(itemView);
            Data = itemView.findViewById(R.id.Data_notifi);
            Event = itemView.findViewById(R.id.event);
            Order = itemView.findViewById(R.id.Order_name);
            status = itemView.findViewById(R.id.status_notifi);

            swipeLayout = itemView.findViewById(R.id.swipeNotifi);
            buttonDelete = itemView.findViewById(R.id.trash_notifi);
        }
    }
}
