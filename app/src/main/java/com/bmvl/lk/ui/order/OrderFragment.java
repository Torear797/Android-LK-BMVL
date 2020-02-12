package com.bmvl.lk.ui.order;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import com.bmvl.lk.OnBackPressedListener;
import com.bmvl.lk.R;
import com.bmvl.lk.models.Orders;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.swipe.util.Attributes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View MyView = inflater.inflate(R.layout.fragment_order, container, false);
       // MyActionBar.setTitle(R.string.order);
        OrderTypes = getResources().getStringArray(R.array.order_types);
        OrderStatuses = getResources().getStringArray(R.array.order_statuses);

        final RecyclerView recyclerView = MyView.findViewById(R.id.list);
        final FloatingActionButton fab = MyView.findViewById(R.id.floatingActionButton);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                if (dy > 0 || dy<0 && fab.isShown())
                {
                    //fab.animate().translationY(fab.getHeight() + 40).setInterpolator(new LinearInterpolator()).start();
                    fab.hide();
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState)
            {
                if (newState == RecyclerView.SCROLL_STATE_IDLE)
                {
                    //fab.animate().translationY(0).setInterpolator(new LinearInterpolator()).start();
                    fab.show();
                }

                super.onScrollStateChanged(recyclerView, newState);
            }
        });


        YoYo.with(Techniques.Tada)
                .duration(700)
                .playOn(MyView.findViewById(R.id.floatingActionButton));
        SetTestData();

        // OrdersAdapter OrderAdapter = new OrdersAdapter(getContext(), Orders);
        OrderSwipeAdapter OrderAdapter = new OrderSwipeAdapter(getContext(), Orders);
        (OrderAdapter).setMode(Attributes.Mode.Single);
        recyclerView.setAdapter(OrderAdapter);

        return MyView;
    }

    private void SetTestData() {
        Orders.add(new Orders(1, "", 1, 6, 1, "", "", "2019-12-12", "", "", 0.0, 0, 0, 0, ""));
        Orders.add(new Orders(2, "", 1, 5, 2, "", "", "2019-12-13", "", "", 0.0, 0, 0, 0, ""));
        Orders.add(new Orders(3, "", 1, 2, 3, "", "", "2019-12-14", "", "", 0.0, 0, 0, 0, ""));
        Orders.add(new Orders(4, "", 1, 1, 4, "", "", "2019-12-15", "", "", 0.0, 0, 0, 0, ""));
        Orders.add(new Orders(5, "", 1, 3, 6, "", "", "2019-12-16", "", "", 0.0, 0, 0, 0, ""));
    }

    @Override
    public void onBackPressed() {

    }
}
