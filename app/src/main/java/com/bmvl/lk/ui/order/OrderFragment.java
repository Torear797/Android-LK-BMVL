package com.bmvl.lk.ui.order;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bmvl.lk.OnBackPressedListener;
import com.bmvl.lk.R;
import com.bmvl.lk.models.Orders;

import java.util.ArrayList;
import java.util.List;

import static com.bmvl.lk.MenuActivity.MyActionBar;

public class OrderFragment extends Fragment implements OnBackPressedListener {
    private List<Orders> Orders = new ArrayList<>();
    public static String[] OrderTypes;
    public static String[] OrderStatuses;
    public OrderFragment() {
    }

    public static OrderFragment newInstance() {
        return new OrderFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View MyView = inflater.inflate(R.layout.fragment_order, container, false);
        MyActionBar.setTitle(R.string.order);
        OrderTypes = getResources().getStringArray(R.array.order_types);
        OrderStatuses = getResources().getStringArray(R.array.order_statuses);

        final RecyclerView recyclerView =  MyView.findViewById(R.id.list);


        SetTestData();

        OrdersAdapter OrderAdapter = new OrdersAdapter(getContext(), Orders);
        recyclerView.setAdapter(OrderAdapter);

        return MyView;
    }
    private void SetTestData(){
        Orders.add(new Orders(1,"",1,6,1,"","","2019-12-12","","",0.0,0,0,0,""));
        Orders.add(new Orders(2,"",1,5,2,"","","2019-12-13","","",0.0,0,0,0,""));
        Orders.add(new Orders(3,"",1,2,3,"","","2019-12-14","","",0.0,0,0,0,""));
        Orders.add(new Orders(4,"",1,1,4,"","","2019-12-15","","",0.0,0,0,0,""));
        Orders.add(new Orders(5,"",1,3,6,"","","2019-12-16","","",0.0,0,0,0,""));
    }

    @Override
    public void onBackPressed() {

    }
}
