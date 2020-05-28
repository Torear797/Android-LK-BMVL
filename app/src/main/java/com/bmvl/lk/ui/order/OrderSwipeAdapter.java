package com.bmvl.lk.ui.order;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrderSwipeAdapter extends RecyclerSwipeAdapter<OrderSwipeAdapter.SimpleViewHolder> {
    private static List<Orders> Orders;
    private LayoutInflater inflater;

    private OnOrderClickListener onOrderClickListener;

    OrderSwipeAdapter(Context context, List<Orders> Contents, OnOrderClickListener onOrderClickListener) {
        this.inflater = LayoutInflater.from(context);
        Orders = Contents;
        this.onOrderClickListener = onOrderClickListener;
    }

    public interface OnOrderClickListener {
        void onDeleteOrder(int id, int position);

        void onCopyOrder(Orders order);

        void onDownloadOrder(int id);

        void onEditOrder(Orders order);
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_order, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder simpleViewHolder, int i) {
        final Orders Order = Orders.get(i);
        simpleViewHolder.Number.setText(MessageFormat.format("№ {0}", Order.getId()));
        simpleViewHolder.Name.setText(inflater.getContext().getResources().getStringArray(R.array.order_name)[Order.getType_id() - 1]);
        simpleViewHolder.Status.setText(inflater.getContext().getResources().getStringArray(R.array.order_statuses)[Order.getStatus_id() - 1]);
        simpleViewHolder.Adres.setText(App.UserInfo.getAdress());
        simpleViewHolder.Person.setText(App.UserInfo.getFIO());
        simpleViewHolder.PersonStatus.setText(App.UserInfo.getPosition());


        try {
            simpleViewHolder.Data.setText(new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format( new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(Order.getDate())));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //simpleViewHolder.Data.setText(new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(new Date()));

        simpleViewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        simpleViewHolder.swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
                YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.edit));
            }
        });

        simpleViewHolder.Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (simpleViewHolder.group.getVisibility() == View.VISIBLE) {
                    simpleViewHolder.group.setVisibility(View.GONE);
                    simpleViewHolder.btnLinear.setOrientation(LinearLayout.HORIZONTAL);
                } else {
                    simpleViewHolder.group.setVisibility(View.VISIBLE);
                    simpleViewHolder.btnLinear.setOrientation(LinearLayout.VERTICAL);
                }
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

        //final ConstraintLayout content;
         Group group;
         MaterialCardView Card;
         LinearLayout btnLinear;

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

            buttonOpen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    closeAllItems();
                    onOrderClickListener.onEditOrder(Orders.get(getLayoutPosition()));
                }
            });

//            Card.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (group.getVisibility() == View.VISIBLE) {
//                        group.setVisibility(View.GONE);
//                        btnLinear.setOrientation(LinearLayout.HORIZONTAL);
//                    } else {
//                        group.setVisibility(View.VISIBLE);
//                        btnLinear.setOrientation(LinearLayout.VERTICAL);
//                    }
//                }
//            });

            buttonCopy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    closeAllItems();
                    swipeLayout.close();
                    onOrderClickListener.onCopyOrder(Orders.get(getLayoutPosition()));
                }
            });

            buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    closeAllItems();
                    swipeLayout.close();
                    onOrderClickListener.onDeleteOrder(Orders.get(getLayoutPosition()).getId(), getLayoutPosition());
                }
            });

            buttonDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    closeAllItems();
                    swipeLayout.close();
                    onOrderClickListener.onDownloadOrder(Orders.get(getLayoutPosition()).getId());
                }
            });
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