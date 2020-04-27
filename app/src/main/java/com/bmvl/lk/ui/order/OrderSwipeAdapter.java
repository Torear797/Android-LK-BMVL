package com.bmvl.lk.ui.order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class OrderSwipeAdapter extends RecyclerSwipeAdapter<OrderSwipeAdapter.SimpleViewHolder> {
    private static List<Orders> Orders;
    private LayoutInflater inflater;
    private TextView message;

    private OnOrderClickListener onOrderClickListener;

    OrderSwipeAdapter(Context context, List<Orders> Contents, TextView msg, OnOrderClickListener onOrderClickListener) {
        this.inflater = LayoutInflater.from(context);
        Orders = Contents;
        this.message = msg;
        this.onOrderClickListener = onOrderClickListener;
    }

    public interface OnOrderClickListener {
        void onDeleteOrder(Orders order);
        void onCopyOrder(Orders order);
        void onDownloadOrder(Orders order);
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
        simpleViewHolder.Number.setText("№ " + Order.getId());

        int j = 1;
        for (String itVar : OrderFragment.OrderTypes)
        {
            if(j == Order.getType_id()){
                simpleViewHolder.Name.setText(itVar);
                break;
            }
            j++;
        }
        j = 1;
        for (String itVar : OrderFragment.OrderStatuses)
        {
            if(j == Order.getStatus_id()){
                simpleViewHolder.Status.setText(itVar);
                break;
            }
            j++;
        }
        simpleViewHolder.Adres.setText(App.UserInfo.getAdress());
        simpleViewHolder.Person.setText(App.UserInfo.getFIO());
        simpleViewHolder.PersonStatus.setText(App.UserInfo.getPosition());
        simpleViewHolder.Data.setText(String.valueOf(Order.getDate()));


        simpleViewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        simpleViewHolder.swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
                YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.trash));
                //Toast.makeText(layout.getContext(), "Открыто", Toast.LENGTH_SHORT).show();
            }
        });
//        simpleViewHolder.swipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
//            @Override
//            public void onDoubleClick(SwipeLayout layout, boolean surface) {
//                Toast.makeText(MyContext, "DoubleClick", Toast.LENGTH_SHORT).show();
//            }
//        });
        //final int id = i;
//        simpleViewHolder.buttonDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//              //  Orders.remove(simpleViewHolder.getLayoutPosition());
//                List<Orders> insertlist = new ArrayList<>(Orders);
//                insertlist.remove(simpleViewHolder.getLayoutPosition());
//                updateList(insertlist);
//              //  simpleViewHolder.swipeLayout.close();
//                Snackbar.make(view, "Заявка удалена!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
//            }
//        });
//
//        simpleViewHolder.buttonDownload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Загрузка!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
//                //Toast.makeText(view.getContext(), "Загрузка!", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        simpleViewHolder.buttonCopy.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//               /// Orders.add(0,new Orders(Orders.size()+1,Order.getUser_id(),Order.getType_id(),Order.getDate()));
//                List<Orders> insertlist = new ArrayList<>();
//                insertlist.add(new Orders(Orders.get(0).getId()+1,Order.getUser_id(),Order.getType_id(),Order.getDate()));
//                insertdata(insertlist, true);
//                //simpleViewHolder.swipeLayout.close();
//                Snackbar.make(view, "Заявка скопирвоана!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
//              //  Toast.makeText(view.getContext(), "Заявка скопирвоана!", Toast.LENGTH_SHORT).show();
//            }
//        });

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

        final TextView Number,Name,Status,Adres,Person,PersonStatus,Data;
        final SwipeLayout swipeLayout;
        final ImageView buttonDelete,buttonCopy,buttonOpen,buttonDownload;

        SimpleViewHolder(@NonNull View itemView) {
            super(itemView);
            Number = itemView.findViewById(R.id.nomer);
            Name = itemView.findViewById(R.id.name);
            Status = itemView.findViewById(R.id.status);
            Adres = itemView.findViewById(R.id.adres);
            Person = itemView.findViewById(R.id.person);
            PersonStatus = itemView.findViewById(R.id.person_status);
            Data = itemView.findViewById(R.id.Data);

            swipeLayout = itemView.findViewById(R.id.swipe);

            buttonDelete = itemView.findViewById(R.id.trash);
            buttonCopy = itemView.findViewById(R.id.create);
            buttonOpen = itemView.findViewById(R.id.edit);
            buttonDownload = itemView.findViewById(R.id.download);


            buttonOpen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Orders order = Orders.get(getLayoutPosition());
                    onOrderClickListener.onEditOrder(order);
                }
            });


            buttonCopy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Orders order = Orders.get(getLayoutPosition());
                    onOrderClickListener.onCopyOrder(order);
                }
            });

            buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Orders order = Orders.get(getLayoutPosition());
                    onOrderClickListener.onDeleteOrder(order);
                }
            });

            buttonDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Orders order = Orders.get(getLayoutPosition());
                    onOrderClickListener.onDownloadOrder(order);
                }
            });
        }
    }
    private void CheckEmpty(){
        if(Orders.size() == 0) message.setVisibility(View.VISIBLE);
        else message.setVisibility(View.GONE);
    }

    public void insertdata(List<Orders> insertList, boolean isCopy){
        OrdersDiffUtilCallback diffUtilCallback = new OrdersDiffUtilCallback(Orders, insertList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtilCallback,false);
        if(isCopy)
        Orders.addAll(0, insertList);
        else  Orders.addAll(insertList);
        CheckEmpty();
        diffResult.dispatchUpdatesTo(this);
    }
    public void updateList(List<Orders> newList){
        OrdersDiffUtilCallback diffUtilCallback = new OrdersDiffUtilCallback(Orders, newList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtilCallback,false);
        Orders.clear();
        Orders.addAll(newList);
        CheckEmpty();
        diffResult.dispatchUpdatesTo(this);
    }
}