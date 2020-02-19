package com.bmvl.lk.ui.order;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bmvl.lk.OnBackPressedListener;
import com.bmvl.lk.R;
import com.bmvl.lk.models.Orders;
import com.bmvl.lk.ui.Create_Order.CreateOrderActivity;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.swipe.util.Attributes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.provider.AlarmClock.EXTRA_MESSAGE;


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

        OrderTypes = getResources().getStringArray(R.array.order_types);
        OrderStatuses = getResources().getStringArray(R.array.order_statuses);

        final RecyclerView recyclerView = MyView.findViewById(R.id.list);
        final FloatingActionButton fab = MyView.findViewById(R.id.floatingActionButton);
        fab.setColorFilter(Color.argb(255, 255, 255, 255));


        YoYo.with(Techniques.Tada)
                .duration(700)
                .playOn(fab);
        SetTestData();

        OrderSwipeAdapter OrderAdapter = new OrderSwipeAdapter(getContext(), Orders);
        (OrderAdapter).setMode(Attributes.Mode.Single);
        recyclerView.setAdapter(OrderAdapter);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(Objects.requireNonNull(getContext()))
                        .setTitle(R.string.new_order_title)
                        .setItems(R.array.order_types, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                byte choce = 0;
                                String order_name;
                                switch (which) {
                                    case 1:
                                        choce = 1;
                                        break;
                                    case 2:
                                        choce = 2;
                                        break;
                                    case 3:
                                        choce = 3;
                                        break;
                                    case 4:
                                        choce = 4;
                                        break;
                                }
                                Intent intent = new Intent(getActivity(),CreateOrderActivity.class);
                                intent.putExtra("id", choce);
                                startActivity(intent);
                            }
                        })
                        .create();
                alertDialog.show();
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 || dy < 0 && fab.isShown()) {
                    fab.hide();
                }
            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    fab.show();
                }

                super.onScrollStateChanged(recyclerView, newState);
            }
        });

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
