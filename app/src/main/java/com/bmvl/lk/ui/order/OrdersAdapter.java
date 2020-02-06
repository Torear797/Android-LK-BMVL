package com.bmvl.lk.ui.order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bmvl.lk.R;
import com.bmvl.lk.models.Orders;
import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder>{
    private List<Orders> Orders;
    private LayoutInflater inflater;

    OrdersAdapter(Context context, List<Orders> Contents) {
        this.Orders = Contents;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.one_order, parent, false);
        return new OrdersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Orders Order = Orders.get(position);
        holder.Number.setText("№ " + Order.getId());

        int i = 1;
        for (String itVar : OrderFragment.OrderTypes)
        {
           if(i == Order.getType_id()){
               holder.Name.setText(itVar);
               break;
           }
           i++;
        }
        i = 1;
        for (String itVar : OrderFragment.OrderStatuses)
        {
            if(i == Order.getStatus_id()){
                holder.Status.setText(itVar);
                break;
            }
            i++;
        }
        holder.Adres.setText("г. Белгород, ул. Ленина, д. 1");
        holder.Person.setText("Иванов Иван Петрович1");
        holder.PersonStatus.setText("Начальник отдела1");
        holder.Data.setText(String.valueOf(Order.getDate()));
    }

    @Override
    public int getItemCount() {
        return Orders.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView Number,Name,Status,Adres,Person,PersonStatus,Data;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            Number = itemView.findViewById(R.id.nomer);
            Name = itemView.findViewById(R.id.name);
            Status = itemView.findViewById(R.id.status);
            Adres = itemView.findViewById(R.id.adres);
            Person = itemView.findViewById(R.id.person);
            PersonStatus = itemView.findViewById(R.id.person_status);
            Data = itemView.findViewById(R.id.Data);
        }
    }
}
