package com.bmvl.lk.ui.order;

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
import com.bmvl.lk.models.Orders;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;


import java.util.List;

public class OrderSwipeAdapter extends RecyclerSwipeAdapter<OrderSwipeAdapter.SimpleViewHolder> {
    private List<Orders> Orders;
    private LayoutInflater inflater;
  //  private Context MyContext;

    OrderSwipeAdapter(Context context, List<Orders> Contents) {
        this.Orders = Contents;
        this.inflater = LayoutInflater.from(context);
        //this.MyContext = context;
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.one_order, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder simpleViewHolder, int i) {
        Orders Order = Orders.get(i);
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
        simpleViewHolder.Adres.setText("г. Белгород, ул. Ленина, д. 1");
        simpleViewHolder.Person.setText("Иванов Иван Петрович1");
        simpleViewHolder.PersonStatus.setText("Начальник отдела1");
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
        simpleViewHolder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(view.getContext(), "Deleted !", Toast.LENGTH_SHORT).show();
            }
        });

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
        final ImageView buttonDelete;
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
        }
    }
}
