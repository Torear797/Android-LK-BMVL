package com.bmvl.lk.ui.order;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bmvl.lk.App;
import com.bmvl.lk.R;
import com.bmvl.lk.data.models.Orders;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.google.android.material.card.MaterialCardView;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class OrderSwipeAdapter extends RecyclerSwipeAdapter<OrderSwipeAdapter.SimpleViewHolder> {
    private static List<Orders> Orders;
    // private LayoutInflater inflater;
    private OnOrderClickListener onOrderClickListener;

    public OrderSwipeAdapter(List<Orders> Contents, OnOrderClickListener onOrderClickListener) {
        //   this.inflater = LayoutInflater.from(context);
        Orders = Contents;
        this.onOrderClickListener = onOrderClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public interface OnOrderClickListener {
        void onDeleteOrder(int id, int position, int Status);

        void onCopyOrder(Orders order);

        void onDownloadOrder(int id);

        void onEditOrder(Orders order);

        void onScrollToOrder(int position);
    }

    @NonNull
    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //  View view = inflater.inflate(R.layout.item_order, parent, false);
     //   View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new SimpleViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false));
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onBindViewHolder(SimpleViewHolder simpleViewHolder, int i) {
        final Orders Order = Orders.get(i);
        simpleViewHolder.Number.setText(MessageFormat.format("№ {0}", Order.getId()));
        simpleViewHolder.Name.setText(simpleViewHolder.Name.getContext().getResources().getStringArray(R.array.order_name)[Order.getType_id() - 1]);
        simpleViewHolder.Status.setText(simpleViewHolder.Status.getContext().getResources().getStringArray(R.array.order_statuses)[Order.getStatus_id() - 1]);
        simpleViewHolder.Adres.setText(App.UserInfo.getAdress());
        simpleViewHolder.Person.setText(App.UserInfo.getFIO());
        simpleViewHolder.PersonStatus.setText(App.UserInfo.getPosition());


        try {
            simpleViewHolder.Data.setText(new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Objects.requireNonNull(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(Order.getDate()))));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (Order.getStatus_id() == 11) {
            simpleViewHolder.buttonDownload.setVisibility(View.GONE);
            simpleViewHolder.buttonCopy.setVisibility(View.GONE);
            simpleViewHolder.Card.setCardBackgroundColor(simpleViewHolder.Card.getContext().getResources().getColor(R.color.notify_old_color));
        }

       // simpleViewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        simpleViewHolder.swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
                YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.edit));
            }
        });


        if (Order.getType_id() > (byte) 4)
            simpleViewHolder.buttonDownload.setVisibility(View.GONE);
        else simpleViewHolder.buttonDownload.setVisibility(View.VISIBLE);

        mItemManger.bindView(simpleViewHolder.itemView, i);
    }

    @Override
    public int getItemCount() {
        return Orders.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    class SimpleViewHolder extends RecyclerView.ViewHolder {

        final TextView Number, Name, Status, Adres, Person, PersonStatus, Data;
        final SwipeLayout swipeLayout;
        final ImageView buttonDelete, buttonCopy, buttonOpen, buttonDownload;
        final Group group;
        final MaterialCardView Card;
        final LinearLayout btnLinear;

        SimpleViewHolder(@NonNull View itemView) {
            super(itemView);
            Number = itemView.findViewById(R.id.nomer);
            Name = itemView.findViewById(R.id.name);
            Status = itemView.findViewById(R.id.status);
            Adres = itemView.findViewById(R.id.adres);
            Person = itemView.findViewById(R.id.person);
            PersonStatus = itemView.findViewById(R.id.person_status);
            Data = itemView.findViewById(R.id.Data);

            group = itemView.findViewById(R.id.MyGroup);
            swipeLayout = itemView.findViewById(R.id.swipe);
            Card = itemView.findViewById(R.id.Card);
            btnLinear = itemView.findViewById(R.id.bottom_wrapper);

            buttonDelete = itemView.findViewById(R.id.trash);
            buttonCopy = itemView.findViewById(R.id.create);
            buttonOpen = itemView.findViewById(R.id.edit);
            buttonDownload = itemView.findViewById(R.id.download);


            Card.setOnClickListener(view -> {
                if (group.getVisibility() == View.VISIBLE) {
                    group.setVisibility(View.GONE);
                    btnLinear.setOrientation(LinearLayout.HORIZONTAL);
                } else {
                    group.setVisibility(View.VISIBLE);
                    btnLinear.setOrientation(LinearLayout.VERTICAL);
                    onOrderClickListener.onScrollToOrder(getLayoutPosition());
                }
            });

            buttonOpen.setOnClickListener(view -> {
                closeAllItems();
                onOrderClickListener.onEditOrder(Orders.get(getLayoutPosition()));
            });


            buttonCopy.setOnClickListener(view -> {
                closeAllItems();
                onOrderClickListener.onCopyOrder(Orders.get(getLayoutPosition()));
            });

            buttonDelete.setOnClickListener(view -> {
                closeAllItems();
               // onOrderClickListener.onDeleteOrder(Orders.get(getLayoutPosition()).getId(), getLayoutPosition(), Orders.get(getLayoutPosition()).getStatus_id());
                Orders.remove(getLayoutPosition());
                notifyItemRemoved(getLayoutPosition());
            });

            buttonDownload.setOnClickListener(view -> {
                closeAllItems();
                onOrderClickListener.onDownloadOrder(Orders.get(getLayoutPosition()).getId());
            });

            swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        }
    }

    public void insertdata(List<Orders> insertList, boolean isCopy) {
        OrdersDiffUtilCallback diffUtilCallback = new OrdersDiffUtilCallback(Orders, insertList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtilCallback);
        if (isCopy)
            Orders.addAll(0, insertList);
        else Orders.addAll(insertList);
        diffResult.dispatchUpdatesTo(this);
    }

    public void updateList(List<Orders> newList) {
        OrdersDiffUtilCallback diffUtilCallback = new OrdersDiffUtilCallback(Orders, newList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtilCallback);
        Orders.clear();
        Orders.addAll(newList);
        diffResult.dispatchUpdatesTo(this);
    }
}