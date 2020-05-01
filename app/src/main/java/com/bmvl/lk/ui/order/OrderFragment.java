package com.bmvl.lk.ui.order;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bmvl.lk.App;
import com.bmvl.lk.Rest.AnswerCopyOrder;
import com.bmvl.lk.Rest.StandardAnswer;
import com.bmvl.lk.data.OnBackPressedListener;
import com.bmvl.lk.R;
import com.bmvl.lk.Rest.NetworkService;
import com.bmvl.lk.Rest.Order.OrdersAnswer;
import com.bmvl.lk.data.SpacesItemDecoration;
import com.bmvl.lk.data.models.Orders;
import com.bmvl.lk.ui.Create_Order.CreateOrderActivity;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.swipe.util.Attributes;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.orhanobut.hawk.Hawk;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class OrderFragment extends Fragment implements OnBackPressedListener {
    private static List<Orders> Orders = new ArrayList<>();
    public static String[] OrderStatuses;
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

    public OrderFragment() {
    }

    public static OrderFragment newInstance() {
        return new OrderFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View MyView = inflater.inflate(R.layout.fragment_order, container, false);
        if (Hawk.contains("OrdersList")) Orders = Hawk.get("OrdersList");

      //  OrderTypes = getResources().getStringArray(R.array.order_name);
        OrderStatuses = getResources().getStringArray(R.array.order_statuses);

        recyclerView = MyView.findViewById(R.id.list);


        swipeRefreshLayout = MyView.findViewById(R.id.SwipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(MyRefresh);
        fab = MyView.findViewById(R.id.floatingActionButton);
        final TextView message = MyView.findViewById(R.id.empty_msg);

        initRecyclerView(message);

        fab.setColorFilter(Color.argb(255, 255, 255, 255));

        YoYo.with(Techniques.Tada)
                .duration(700)
                .playOn(fab);

        if (Orders.size() == 0) LoadOrders(Orders, (byte) 0);
        else UpdateOrders();

        FabLisener();
        RecyclerViewEndLisener();
        return MyView;
    }

    private void LoadOrders(final List<Orders> NewList, final byte Type) {
        NetworkService.getInstance()
                .getJSONApi()
                .LoadOrders(App.UserAccessData.getToken(), (byte) (CurrentPage + 1))
                .enqueue(new Callback<OrdersAnswer>() {
                    @Override
                    public void onResponse(@NonNull Call<OrdersAnswer> call, @NonNull Response<OrdersAnswer> response) {
                        if (response.isSuccessful()) {
                            TotalPage = response.body().getOrders().getTotal_pages();
                            CurrentPage = response.body().getOrders().getCurrent();
                            NewList.addAll(response.body().getOrders().getOrders());
                            Hawk.put("OrdersList", NewList);
                            switch (Type) {
                                case 0:
                                    OrderAdapter.notifyDataSetChanged();
                                    break;
                                case 1:
                                    OrderAdapter.updateList(NewList);
                                    swipeRefreshLayout.setRefreshing(false);
                                    break;
                                case 2:
                                    // OrderAdapter.insertdata(NewList, false);
                                    //  recyclerView.smoothScrollToPosition((CurrentPage - 1) * 10 - 1);
                                    Orders.addAll(NewList);
                                    OrderAdapter.notifyDataSetChanged();
                                    loading = true;
                                    break;
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<OrdersAnswer> call, @NonNull Throwable t) {
                    }
                });
    }

    private void initRecyclerView(TextView message) {
        OrderSwipeAdapter.OnOrderClickListener onClickListener = new OrderSwipeAdapter.OnOrderClickListener() {

            @Override
            public void onDeleteOrder(int id, final int position) {
                NetworkService.getInstance()
                        .getJSONApi()
                        .DeleteOrder(App.UserAccessData.getToken(), id)
                        .enqueue(new Callback<StandardAnswer>() {
                            @Override
                            public void onResponse(@NonNull Call<StandardAnswer> call, @NonNull Response<StandardAnswer> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    if (response.body().getStatus() == 200) {
                                        // UpdateOrders();
                                        OrderAdapter.closeAllItems();
                                        List<Orders> insertlist = new ArrayList<>(Orders);
                                        insertlist.remove(position);
                                        OrderAdapter.updateList(insertlist);
                                        Snackbar.make(Objects.requireNonNull(getView()), "Заявка удалена!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<StandardAnswer> call, @NonNull Throwable t) {
                            }
                        });
            }

            @Override
            public void onCopyOrder(final com.bmvl.lk.data.models.Orders order) {
                NetworkService.getInstance()
                        .getJSONApi()
                        .CopyOrder(App.UserAccessData.getToken(), order.getId())
                        .enqueue(new Callback<AnswerCopyOrder>() {
                            @Override
                            public void onResponse(@NonNull Call<AnswerCopyOrder> call, @NonNull Response<AnswerCopyOrder> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    if (response.body().getStatus() == 200) {
                                        OrderAdapter.closeAllItems();
                                        // UpdateOrders();
                                        List<Orders> insertlist = new ArrayList<>();
                                        Date currentDate = new Date();
                                        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
                                        insertlist.add(new Orders(response.body().getOrderId(),order.getUser_id(),order.getType_id(),order.getStatus_id(), dateFormat.format(currentDate)));
                                        OrderAdapter.insertdata(insertlist, true);
                                        Snackbar.make(Objects.requireNonNull(getView()), "Заявка скопирована!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<AnswerCopyOrder> call, @NonNull Throwable t) {
                            }
                        });
            }

            @Override
            public void onDownloadOrder(com.bmvl.lk.data.models.Orders order) {
                Snackbar.make(Objects.requireNonNull(getView()), "Загрузка", Snackbar.LENGTH_LONG).setAction("Action", null).show();

            }

            @Override
            public void onEditOrder(com.bmvl.lk.data.models.Orders order) {
                Snackbar.make(Objects.requireNonNull(getView()), "Изменение", Snackbar.LENGTH_LONG).setAction("Action", null).show();

            }
        };
        recyclerView.addItemDecoration(new SpacesItemDecoration((byte) 10, (byte) 10));
        recyclerView.setHasFixedSize(true);
        OrderAdapter = new OrderSwipeAdapter(getContext(), Orders, onClickListener);
        (OrderAdapter).setMode(Attributes.Mode.Single);
        recyclerView.setAdapter(OrderAdapter);
    }

    @Override
    public void onBackPressed() {

    }

    private SwipeRefreshLayout.OnRefreshListener MyRefresh = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            swipeRefreshLayout.setRefreshing(true);
            UpdateOrders();
        }
    };

    private void UpdateOrders() {
        List<Orders> insertlist = new ArrayList<>();
        CurrentPage = 0;
        LoadOrders(insertlist, (byte) 1);
    }

    private void RecyclerViewEndLisener() {
        final RecyclerView.LayoutManager lm = recyclerView.getLayoutManager();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 || dy < 0 && fab.isShown()) {
                    fab.hide();
                }

                if (dy > 0 && (CurrentPage + 1) <= TotalPage) //check for scroll down
                {
                    assert lm != null;
                    visibleItemCount = lm.getChildCount();
                    totalItemCount = lm.getItemCount();
                    pastVisiblesItems = ((LinearLayoutManager) lm).findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) == totalItemCount) {
                            loading = false;
                            List<Orders> insertlist = new ArrayList<>();
                            LoadOrders(insertlist, (byte) 2);
                        }
                    }
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
    }

    private void FabLisener() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                new MaterialAlertDialogBuilder(Objects.requireNonNull(getContext()))
                        .setTitle(R.string.new_order_title)
                        .setItems(R.array.order_types, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                byte choce = 1;
                                switch (which) {
                                    case 0:
                                        choce = 1;
                                        break;
                                    case 1:
                                        choce = 4;
                                        break;
                                    case 2:
                                        choce = 5;
                                        break;
                                    case 3:
                                        choce = 6;
                                        break;
                                    case 4:
                                        choce = 7;
                                        break;
                                }
                                Intent intent = new Intent(getActivity(), CreateOrderActivity.class);
                                intent.putExtra("id", choce);
                                startActivity(intent);
                            }
                        })
                        .create()
                        .show();
            }
        });
    }
}