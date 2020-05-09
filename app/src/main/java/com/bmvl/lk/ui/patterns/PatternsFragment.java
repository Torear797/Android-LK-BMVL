package com.bmvl.lk.ui.patterns;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bmvl.lk.R;
import com.bmvl.lk.data.OnBackPressedListener;
import com.bmvl.lk.data.models.Orders;
import com.bmvl.lk.ui.Create_Order.CreateOrderActivity;
import com.bmvl.lk.ui.order.OrderSwipeAdapter;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PatternsFragment extends Fragment implements OnBackPressedListener {

    private static List<com.bmvl.lk.data.models.Orders> Orders = new ArrayList<>();

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private OrderSwipeAdapter OrderAdapter;
    private FloatingActionButton fab;

    private boolean loading = true;

    private int pastVisiblesItems;
    private int visibleItemCount;
    private int totalItemCount;

    private static byte TotalPage;
    private static byte CurrentPage = 0;
    private TextView message;

    @Override
    public void onBackPressed() {
    }

    public static PatternsFragment newInstance() {
        return new PatternsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View MyView = inflater.inflate(R.layout.fragment_order, container, false);
    //    if (Hawk.contains("OrdersList")) Orders = Hawk.get("OrdersList");

        recyclerView = MyView.findViewById(R.id.list);
        swipeRefreshLayout = MyView.findViewById(R.id.SwipeRefreshLayout);
     //   swipeRefreshLayout.setOnRefreshListener(MyRefresh);
        fab = MyView.findViewById(R.id.floatingActionButton);
        message = MyView.findViewById(R.id.empty_msg);

      //  initRecyclerView();
        recyclerView.scrollToPosition(0);

        fab.setColorFilter(Color.argb(255, 255, 255, 255));

        YoYo.with(Techniques.Tada)
                .duration(700)
                .playOn(fab);

        FabLisener(); //Слушатель нажатия кнопки меню
        //RecyclerViewEndLisener(); //Слушатель конца списка
        return MyView;
    }

    private void FabLisener() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                new MaterialAlertDialogBuilder(Objects.requireNonNull(getContext()))
                        .setTitle(R.string.new_pattern_title)
                        .setItems(R.array.menu_patterns, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                byte choce = 1;
                                switch (which) {
                                    case 0:
                                        choce = 8;
                                        break;
                                    case 1:
                                        choce = 9;
                                        break;
                                    case 2:
                                        choce = 10;
                                        break;
                                }
                                Intent intent = new Intent(getActivity(), CreateOrderActivity.class);
                                intent.putExtra("type_id", choce);
                                startActivity(intent);
                            }
                        })
                        .create()
                        .show();
            }
        });
    }

}
