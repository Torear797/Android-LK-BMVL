package com.bmvl.lk.ui.order;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bmvl.lk.App;
import com.bmvl.lk.R;
import com.bmvl.lk.Rest.AnswerCopyOrder;
import com.bmvl.lk.Rest.AnswerDownloadOrder;
import com.bmvl.lk.Rest.AnswerOrderEdit;
import com.bmvl.lk.Rest.AnswerOrderNew;
import com.bmvl.lk.Rest.NetworkService;
import com.bmvl.lk.Rest.Order.OrdersAnswer;
import com.bmvl.lk.Rest.Order.SendOrder;
import com.bmvl.lk.Rest.StandardAnswer;
import com.bmvl.lk.Rest.UserInfo.OrderInfo;
import com.bmvl.lk.data.OnBackPressedListener;
import com.bmvl.lk.data.SpacesItemDecoration;
import com.bmvl.lk.data.models.Orders;
import com.bmvl.lk.ui.create_order.CreateOrderActivity;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.swipe.util.Attributes;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.orhanobut.hawk.Hawk;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.TreeMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderFragment extends Fragment implements OnBackPressedListener {
    public static List<Orders> Orders = new ArrayList<>();
    public static TreeMap<Short, SendOrder> offlineOrders = new TreeMap<>();

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    public static OrderSwipeAdapter OrderAdapter;
    private FloatingActionButton fab;
    private ProgressBar ProgresBar;

    private boolean loading = true;

    private int pastVisiblesItems;
    private int visibleItemCount;
    private int totalItemCount;

    private static byte TotalPage;
    private static byte CurrentPage = 0;
    private TextView message;

    @Override
    public void onResume() {
        super.onResume();
        if (App.isOnline(Objects.requireNonNull(getContext()))) {
            if (offlineOrders.size() > 0) SynchronizationOrders();
            else if (Orders.size() == 0) LoadOrders(Orders, (byte) 0);
            else UpdateOrders();
        }
    }

    public OrderFragment() {
    }

    private void SynchronizationOrders() {
        List<SendOrder> orders = new ArrayList<>();
        final Gson gson = new Gson();
        for (int i = 0; i < offlineOrders.size(); i++) {
            orders.add(offlineOrders.get(getPositionKey(i)));
        }

        NetworkService.getInstance()
                .getJSONApi()
                .sendOrders(App.UserAccessData.getToken(), gson.toJson(orders))
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Toast.makeText(getContext(), "Заявки успешно синхронизированы!", Toast.LENGTH_SHORT).show();
                            offlineOrders.clear();
                            Hawk.delete("OfflineOrders");
                            if (Orders.size() == 0) LoadOrders(Orders, (byte) 0);
                            else UpdateOrders();
                        } else
                            Toast.makeText(getContext(), "Ошибка синхронизации", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    }
                });
    }

    private Short getPositionKey(int position) {
        return new ArrayList<>(offlineOrders.keySet()).get(position);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermissions(); //Запрос разрешений
    }

    public static OrderFragment newInstance() {
        return new OrderFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View MyView = inflater.inflate(R.layout.fragment_order, container, false);
        if (Hawk.contains("OrdersList")) Orders = Hawk.get("OrdersList");
        if (Hawk.contains("OfflineOrders"))
            offlineOrders = new TreeMap<Short, SendOrder>((HashMap<Short, SendOrder>) Hawk.get("OfflineOrders"));

        recyclerView = MyView.findViewById(R.id.list);
        ProgresBar = MyView.findViewById(R.id.progressBar);
        swipeRefreshLayout = MyView.findViewById(R.id.SwipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(MyRefresh);
        fab = MyView.findViewById(R.id.floatingActionButton);
        message = MyView.findViewById(R.id.empty_msg);

        if (Orders.size() == 0) message.setVisibility(View.VISIBLE);
        else message.setVisibility(View.GONE);


        initRecyclerView();
        recyclerView.scrollToPosition(0);

        YoYo.with(Techniques.Tada)
                .duration(700)
                .playOn(fab);

        FabLisener(); //Слушатель нажатия кнопки меню
        RecyclerViewEndLisener(); //Слушатель конца списка
        return MyView;
    }

    private void LoadOrders(final List<Orders> NewList, final byte Type) {
        NetworkService.getInstance()
                .getJSONApi()
                .LoadOrders(App.UserAccessData.getToken(), (byte) (CurrentPage + 1))
                .enqueue(new Callback<OrdersAnswer>() {
                    @Override
                    public void onResponse(@NonNull Call<OrdersAnswer> call, @NonNull Response<OrdersAnswer> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {
                                TotalPage = response.body().getOrders().getTotal_pages();
                                CurrentPage = response.body().getOrders().getCurrent();
                                NewList.addAll(response.body().getOrders().getOrders());
                                Hawk.put("OrdersList", NewList);
                                switch (Type) {
                                    case 0:
                                        OrderAdapter.notifyDataSetChanged();
                                        recyclerView.scrollToPosition(0);
                                        break;
                                    case 1:
                                        OrderAdapter.updateList(NewList);
                                        swipeRefreshLayout.setRefreshing(false);
                                        recyclerView.scrollToPosition(0);
                                        break;
                                    case 2:
                                        // OrderAdapter.insertdata(NewList, false);
                                        Orders.addAll(NewList);
                                        OrderAdapter.notifyDataSetChanged();
                                        loading = true;
                                        break;
                                }
                            }
                        } else swipeRefreshLayout.setRefreshing(false);
                        if (Orders.size() == 0) message.setVisibility(View.VISIBLE);
                        else message.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(@NonNull Call<OrdersAnswer> call, @NonNull Throwable t) {
                        swipeRefreshLayout.setRefreshing(false);
                        if (Orders.size() == 0) message.setVisibility(View.VISIBLE);
                        else message.setVisibility(View.GONE);
                    }
                });
    }

    private void initRecyclerView() {
        OrderSwipeAdapter.OnOrderClickListener onClickListener = new OrderSwipeAdapter.OnOrderClickListener() {
            @Override
            public void onDeleteOrder(int id, final int position, int status) {
                if (status != 11) {
                    NetworkService.getInstance()
                            .getJSONApi()
                            .DeleteOrder(App.UserAccessData.getToken(), id)
                            .enqueue(new Callback<StandardAnswer>() {
                                @Override
                                public void onResponse(@NonNull Call<StandardAnswer> call, @NonNull Response<StandardAnswer> response) {
                                    if (response.isSuccessful() && response.body() != null) {
                                        if (response.body().getStatus() == 200) {
                                            List<Orders> insertlist = new ArrayList<>(Orders);
                                            insertlist.remove(position);
                                            OrderAdapter.updateList(insertlist);
                                            Hawk.put("OrdersList", Orders);
                                            Snackbar.make(Objects.requireNonNull(getView()), R.string.order_deleted, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(@NonNull Call<StandardAnswer> call, @NonNull Throwable t) {
                                    Toast.makeText(getContext(), R.string.server_lost, Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    List<Orders> insertlist = new ArrayList<>(Orders);
                    insertlist.remove(position);
                    OrderAdapter.updateList(insertlist);
                    offlineOrders.remove((short) id);
                    Hawk.put("OfflineOrders", OrderFragment.offlineOrders);
                    Hawk.put("OrdersList", OrderFragment.Orders);
                    Snackbar.make(Objects.requireNonNull(getView()), R.string.order_deleted, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
            }

            @Override
            public void onCopyOrder(final com.bmvl.lk.data.models.Orders order) {
                NetworkService.getInstance()
                        .getJSONApi()
                        .CopyOrder(App.UserAccessData.getToken(), order.getId(), (byte) 0)
                        .enqueue(new Callback<AnswerCopyOrder>() {
                            @Override
                            public void onResponse(@NonNull Call<AnswerCopyOrder> call, @NonNull Response<AnswerCopyOrder> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    if (response.body().getStatus() == 200) {
                                        List<Orders> insertlist = new ArrayList<>();
                                        insertlist.add(new Orders(response.body().getOrderId(), order.getUser_id(), order.getType_id(), order.getStatus_id(), new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()).format(new Date())));
                                        OrderAdapter.insertdata(insertlist, true);
                                        Hawk.put("OrdersList", Orders);
                                        Snackbar.make(Objects.requireNonNull(getView()), R.string.order_copy, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<AnswerCopyOrder> call, @NonNull Throwable t) {
                                Toast.makeText(getContext(), R.string.server_lost, Toast.LENGTH_SHORT).show();
                            }
                        });
            }

            @Override
            public void onDownloadOrder(final int id) {
                ProgresBar.setVisibility(View.VISIBLE);
                if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    NetworkService.getInstance()
                            .getJSONApi()
                            .DOWNLOAD_ORDER_CALL(App.UserAccessData.getToken(), id)
                            .enqueue(new Callback<AnswerDownloadOrder>() {
                                @Override
                                public void onResponse(@NonNull Call<AnswerDownloadOrder> call, @NonNull Response<AnswerDownloadOrder> response) {
                                    if (response.isSuccessful() && response.body() != null) {
                                        if (response.body().getStatus() == 200) {
                                            if (SavePhpWordFile(response.body().getDocx(), id))
                                                Toast.makeText(getContext(), R.string.download_successfully, Toast.LENGTH_SHORT).show();
                                            else
                                                Toast.makeText(getContext(), R.string.error_download, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    ProgresBar.setVisibility(View.GONE);
                                }

                                @Override
                                public void onFailure(@NonNull Call<AnswerDownloadOrder> call, @NonNull Throwable t) {
                                    ProgresBar.setVisibility(View.GONE);
                                    Toast.makeText(getContext(), R.string.server_lost, Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    ProgresBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), R.string.no_permisssion_write, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onEditOrder(final com.bmvl.lk.data.models.Orders order) {
                if (order.getStatus_id() != 11) {
                    final SendOrder OpenOrder = new SendOrder(order.getType_id());
                    NetworkService.getInstance()
                            .getJSONApi()
                            .getOrderInfo(App.UserAccessData.getToken(), order.getId())
                            .enqueue(new Callback<AnswerOrderEdit>() {
                                @Override
                                public void onResponse(@NonNull Call<AnswerOrderEdit> call, @NonNull Response<AnswerOrderEdit> response) {
                                    if (response.isSuccessful() && response.body() != null) {
                                        if (response.body().getStatus() == 200) {
                                            OpenOrder.setId(order.getId());
                                            OpenOrder.setFields(response.body().getOrderFields());
                                            OpenOrder.setProby(response.body().getProby());

                                            Intent intent = new Intent(getActivity(), CreateOrderActivity.class);
                                            intent.putExtra("type_id", order.getType_id());
                                            intent.putExtra("isEdit", true);
                                            if (order.getAct_of_selection() != null)
                                                intent.putExtra("ACT", order.getAct_of_selection());
                                            intent.putExtra(SendOrder.class.getSimpleName(), OpenOrder);
                                            startActivity(intent);

                                        }
                                    }
                                }

                                @Override
                                public void onFailure(@NonNull Call<AnswerOrderEdit> call, @NonNull Throwable t) {
                                    Toast.makeText(getContext(), R.string.server_lost, Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Intent intent = new Intent(getActivity(), CreateOrderActivity.class);
                    intent.putExtra("type_id", order.getType_id());
                    intent.putExtra("isEdit", true);
                    intent.putExtra("status", (byte) 11);
                    intent.putExtra(SendOrder.class.getSimpleName(), offlineOrders.get((short) order.getId()));
                    startActivity(intent);
                }
            }

            @Override
            public void onScrollToOrder(int position) {
                recyclerView.smoothScrollToPosition(position + 2);
            }

        };
        recyclerView.addItemDecoration(new SpacesItemDecoration((byte) 10, (byte) 10));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        OrderAdapter = new OrderSwipeAdapter(Orders, onClickListener);
        (OrderAdapter).setMode(Attributes.Mode.Single);
        recyclerView.setAdapter(OrderAdapter);
    }

    private boolean checkPermissions() {
        int result;
        String[] permissions = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE}; // Here i used multiple permission check

        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()), p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()), listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 0);
            return false;
        }
        return true;
    }

    private boolean SavePhpWordFile(String base64String, int id) {

        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Order_" + id + ".docx");
        //String Coded = "UEsDBBQABgAIAAAAIQAekRq37wAAAE4CAAALAAgCX3JlbHMvLnJlbHMgogQCKKAAAgAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAArJLBasMwDEDvg\\/2D0b1R2sEYo04vY9DbGNkHCFtJTBPb2GrX\\/v082NgCXelhR8vS05PQenOcRnXglF3wGpZVDYq9Cdb5XsNb+7x4AJWFvKUxeNZw4gyb5vZm\\/cojSSnKg4tZFYrPGgaR+IiYzcAT5SpE9uWnC2kiKc\\/UYySzo55xVdf3mH4zoJkx1dZqSFt7B6o9Rb6GHbrOGX4KZj+xlzMtkI\\/C3rJdxFTqk7gyjWop9SwabDAvJZyRYqwKGvC80ep6o7+nxYmFLAmhCYkv+3xmXBJa\\/ueK5hk\\/Nu8hWbRf4W8bnF1B8wEAAP\\/\\/AwBQSwMEFAAGAAgAAAAhAOGLTSY6BgAAhhoAABUAAAB3b3JkL3RoZW1lL3RoZW1lMS54bWzsWd2KGzcUvi\\/0HcTcO\\/6b8c8Sb7DHdtNmNwnZTUou5Rl5RrFmZDTy7poQCJur3rQU0tKLBtre9KKULjSloTT0FZxnWEho04eoRuPxjGy5m3QdCCUbyOjnO0efzpE+aTwXLx0FBBwgFmEatozyhZIBUOhQF4dey7i53y80DBBxGLqQ0BC1jCmKjEvb7793EW5xHwUICPsw2oItw+d8vFUsRo5ohtEFOkah6BtSFkAuqswrugweCr8BKVZKpVoxgDg0QAgD4Xb23eyX2e+zE3BtOMQOMrZT\\/z0i\\/gt5FDc4hO3F3lFq9O2z49nJ7Ons8ezk2X1Rfiqen0lbd1SOH9E0sgkDB5C0DDG0Sw\\/30RE3AIERFx0toyT\\/jOL2xeLCiPA1tjm7vvyb280N3FFF2jFvsDA0TcustRf+JYDwVVyv3qv1agt\\/EgAdR8w84ZLHWp1mp2vNsTlQUtT47ta71bKCz\\/mvruDbVvxPwUtQUjRX8P2+ncUwB0qKliYm9YptKngJSoq1FXy91O6adQUvQT7B4WgFXbJqVTud7QIypOSyFt60zH69ModnqGJutSX2IX\\/VtRfAO5T1hYFMNuQ4BHw6RkPoCDsbEjxgGOxgz+fxsHALwVx\\/0uREK00xAxA5DI95y\\/hoDMXWySDPnzw5PX58evzr6YMHp8c\\/5b0rdpdh6OXtXn7\\/+d+P7oO\\/fv7m5cMv9Pgoj3\\/x4ycvfvvj39xzhdaXJy8enzz\\/6tM\\/f3iogbcZHOTh+zhAEbiKDsENGogJagZAA\\/Z6Fvs+xHmLduhFMISxjQbd476CvjqFBGpwHaTG8RYTAqEDfjC5oxDe89mEYw3wih8owF1KSYcy7ZyuxGPlozAJPf3gbJLH3YDwQDe2vZTl3mQsVjrWubR9pNC8TkTKoYdCxEHcR0cIacxuY6zEdRc7jEZ0yMFtDDoQa0OyjwfKasqMLuNA5GWqIyjyrcRm9xboUKJz30UHKlLsDUh0LhFRwvgBnHAYaBnDgOSRO5D7OpJ7U+YoAY+4yLSHCAU9F0WRzuYamyp0rwgh0ad9l0wDFck4HumQO5DSPLJLR7YPg7GWMw79PPbDaCSWKATXKdeSoOoOiesiDzBcm+5bGCnpPntv3xQypF8gcc+E6bYEoup+nJIhRNJ5cUm5AxyeKePJCJsRcCGTz79+pFfVDYi2HngeuW4zrN0vyyK9DrcszTZlLn77lbkLJ+F1JDaDBvpOmN8J8\\/9emNft583LcabA8nKeXsGlm+CV7+NDTMgenxK0E0ktj8R03b5olBXpZPE6MPZFcT68gvMYlGXAKP8Yc3\\/Ph2MxbFmO4EVz114ExjQSLyGyWes77iCTYJe6SWu5nL6BCgPIs\\/aStWgXZw9PWmv17FVr4V7WPPmKnBKIbV+HRG4wlURVQ6KeNp5BQs5sIyyaGhaN2P1aFvIxz4rYjwDGv2dYZsJIrD9IkBvnKbFPs7vxTK8LpjrtimZ6zZjrZjKtkMgtN5VEbhn60EXLzRvOdTNLqUIvDsUqjXrjTeQ6FpUlbSChWgOHYs9VLeHGgeOWMRT3QFEMxsJfFOsoJF7YMhw+D\\/R\\/UZYxi3gXRn4Ck13J\\/APMEQMEB2Kt59NAwoxbuVKP5\\/iWkmuW3r7IyUc+yWg4RA5f05JVRV\\/iRNt7TnBcoRNBes93D8GATNgNKAJl1ctxAF0c8UU0XcxyizuL4pJczbei8stYtkUhGftwfqLkxTyBy\\/KCTm4ekunyrNT6fDIDL07SuU\\/ds43ijpxorjlA4lNTrx9v7pDPscp0X2GVSPey1jVTrVt3Spz\\/QMhRywZTqMWMNdSyVpXaBi8EueEWS3PdGbHp02B51cYHRHrPlLWVTxJ0cEes\\/K64vk4IjyRVdCTeGez0x+NECWRrqi5HHEwYbhl3S1bbtCuWXSg1rF7BrJqlQsNqVwtty6qWe1a51O1U7omgcD8oW8nYffF+Q6bzjy6yfeXDS5Beuy84NChS+T2lKI3lh5dyRfnwknxvAftxvwGwiMzdWqXfrDY7tUKz2u4XzG6nUWjatU6hW7Pr3X7XthrN\\/j0DHEiw2a7aZq3XKNTKtl0wa6WYfqNZqJuVStustxs9s31vHmsx8\\/SZhlfy2v4HAAD\\/\\/wMAUEsDBBQABgAIAAAAIQA\\/wzC84gwAAOt4AAAPAAAAd29yZC9zdHlsZXMueG1svJ3NctvIEcfvqco7oHhKDrJEifqwa+UtSbYj1VqyVpTW5yEwFGcFYBh8WNLeNtc8wN73CVKpSlXKVckzyG+UmQFAgmoMiB50dLFFgP3DzHT\\/e6bxQXz3\\/UMUel94kgoZHw6Gr7YGHo99GYj49nBwc\\/1h42DgpRmLAxbKmB8OHnk6+P7tH\\/\\/w3f2bNHsMeeopQJy+ifzDwSzL5m82N1N\\/xiOWvpJzHqudU5lELFMfk9vNiCV3+XzDl9GcZWIiQpE9bm5vbe0NSkzShSKnU+Hzd9LPIx5nxn4z4aEiyjidiXla0e670O5lEswT6fM0VZ2OwoIXMREvMMMRAEXCT2Qqp9kr1ZmyRQalzIdb5q8oXAJ2cYBtANjzRYBj7JWMTWVZ46Qch9mtMOljxB8GXuS\\/ObuNZcImoSKpofFU7zwD1v\\/qg71VwRFI\\/x2fsjzMUv0xuUzKj+Un898HGWepd\\/+Gpb4Q16oxihgJBT89ilMxUHs4S7OjVLDGnTP9R+MeP81qm49FIAab+ojpL2rnFxYeDra3qy0nugUr20IW31bbknzj6qbeksMBjzduxnrTRHEPByzZGB9pw82yY8X\\/te7On38yB54zX5jjsGnGVdwP97Y0NBRaZtu7r6sPV7keaJZnsjyIART\\/L7CbYMSVHJQ4xoVG1V4+\\/Sj9Ox6MM7XjcGCOpTbenF0mQiZKh4eD1+aYauOYR+JUBAGPa1+MZyLgn2c8vkl5sNz+4wejpXKDL\\/NY\\/b2zv2eiIEyD9w8+n2tlqr0x0z650Aah\\/nYulgc35n+tYMPSE032M850evKGzxGm+SjEtrZIa71tZubP+m6+hTrQzksdaPRSB9p9qQPtvdSB9l\\/qQAcvdSCD+X8eSMQBfyiECA8DqOs4FjWiORaxoTkWLaE5FqmgORYloDmWQEdzLHGM5ljCFMHJpG+Lwlqw71iivZ27fo5w466fEty462cAN+76hO\\/GXZ\\/f3bjr07kbd332duOuT9Z4brHU8s6UzOKst8qmUmaxzLiX8Yf+NBYrlqnZaHh60uMJSScJMEVmKyfi3jSfmc\\/rI8SI1H0+z3RV58mpNxW3eaJK\\/b4N5\\/EXHqqi22NBoHiEwIRneWIZEZeYTviUJzz2OWVg00F1JejFeTQhiM05uyVj8TggHr6KSJIUFgGt6ueZFokgCOqI+Yns3zTJyPLDR5H2HysN8Y7zMORErAuaEDOs\\/rWBwfQvDQymf2VgMP0Lg5rPqIaopBGNVEkjGrCSRjRuRXxSjVtJIxq3kkY0biWt\\/7hdiyw0Kb6+6hh2P3d3Ekp9lr13O8biNmZqAdB\\/uinPmXqXLGG3CZvPPH1Wuhlb7zP2OMcyePSuKea0BYlqXW9C5ET1WsR5\\/wFdoVGJa8EjkteCRySwBa+\\/xM7VMlkv0E5p6plxPskaRWtInUQ7ZmFeLGj7q41l\\/SNsKYAPIknJZNCMJYjgC72c1e6kyHzLVvZv2JLVX1bPsxJp80okQStD6d\\/RpOHTxzlPVFl215v0QYahvOcBHXGcJbKItbrkt41LOkn+fTSfsVSYWmkF0X2qr67Pe+ds3rtDlyETMY3f3m9ETIQe3Qri9Pr8o3ct57rM1ANDAzyWWSYjMmZ5JvBPn\\/nkzzQNPFJFcPxI1NsjotNDBnYiCCaZgiQDIpJaZopYkMyhhvcDf5xIlgQ0tMuEF7fEZJyIOGbRvFh0EGhL5cV7lX8IVkOG9xNLhD4vRCWqaxJY7bRhmk9+5n7\\/VHchPZIzQ5\\/yzJx\\/NEtdY02H679MWMH1XyIYb6rpQccvQWdXcP07u4Kj6uxJyNJUWC+hOvOoulvxqPvbv\\/greTKUyTQP6QawApKNYAUkG0IZ5lGcUvbY8Ag7bHjU\\/SUMGcMjOCVneH9JREDmDAOj8oSBUbnBwKh8YGCkDuh\\/h04N1v82nRqs\\/706BYxoCVCDUcUZ6fRPdJWnBqOKMwOjijMDo4ozA6OKs513Hp9O1SKYboqpIaliroakm2jijEdzmbDkkQj5PuS3jOAEaUG7TORUPysh4+ImbgKkPkcdEi62CxyVkz\\/zCVnTNIuyXQRnRFkYSkl0bm054RjL1XvX1pmZJzl6N+EyZD6fyTDgiaVPdltVL4+LxzKeN980o9Npz4\\/idpZ549nibH8ds7e11rIq2FfM1h+wacz3qudZmszOeSDyqGoofJhib6e7sYnoFePReuPlSmLFcrejJTzm3nrL5Sp5xXK\\/oyU85kFHS6PTFcs2PbxjyV1jIOy3xc+ixrME335bFC2MGw\\/bFkgLy6YQ3G+LohWpeEe+r68WQO9004zdvpt47PYYFdkpGDnZKZ11ZUe0CeyKfxF6ZsckTXO8xd0TIO+bRXSnzPljLovz9isXnLo\\/1HWmFk5xyr1Gzk73C1crWcY+jp3TjR3ROe\\/YEZ0TkB3RKRNZzVEpyU7pnJvsiM5Jyo5AZys4I+CyFbTHZSto75KtIMUlW\\/VYBdgRnZcDdgRaqBCBFmqPlYIdgRIqMHcSKqSghQoRaKFCBFqocAGGEyq0xwkV2rsIFVJchAopaKFCBFqoEIEWKkSghQoRaKE6ru2t5k5ChRS0UCECLVSIQAvVrBd7CBXa44QK7V2ECikuQoUUtFAhAi1UiEALFSLQQoUItFAhAiVUYO4kVEhBCxUi0EKFCLRQi0cN3YUK7XFChfYuQoUUF6FCClqoEIEWKkSghQoRaKFCBFqoEIESKjB3EiqkoIUKEWihQgRaqOZiYQ+hQnucUKG9i1AhxUWokIIWKkSghQoRaKFCBFqoEIEWKkSghArMnYQKKWihQgRaqBDRFp\\/lJUrbbfZD\\/FlP6x373S9dlY26qj\\/KXUftdEdVrbKzuj+LcCzlndf44OGOqTe6QcQkFNKcorZcVq9zzS0RqAufn07an\\/Cp03v+6FL5LIS5Zgrgo66W4JzKqC3k65agyBu1RXrdEqw6R23Zt24JpsFRW9I1uqxuSlHTETBuSzM146HFvC1b18zhELfl6JohHOG2zFwzhAPclo9rhrueTs7PrXc7jtPe4v5SQGgLxxph305oC0voqyodQ2F0dZqd0NV7dkJXN9oJKH9aMXjH2lFoD9tRbq6GMsO62l2odgLW1ZDg5GqAcXc1RDm7GqLcXA0TI9bVkIB1tXtythOcXA0w7q6GKGdXQ5Sbq+FUhnU1JGBdDQlYV\\/eckK0Yd1dDlLOrIcrN1XBxh3U1JGBdDQlYV0OCk6sBxt3VEOXsaohyczWoktGuhgSsqyEB62pIcHI1wLi7GqKcXQ1Rba42Z1FWXI3ycM0ctwirGeIm5JohLjnXDB2qpZq1Y7VUIzhWS9BXlc9x1VLdaXZCV+\\/ZCV3daCeg\\/GnF4B1rR6E9bEe5uRpXLTW52l2odgLW1bhqyepqXLXU6mpctdTqaly1ZHc1rlpqcjWuWmpytXtythOcXI2rllpdjauWWl2Nq5bsrsZVS02uxlVLTa7GVUtNru45IVsx7q7GVUutrsZVS3ZX46qlJlfjqqUmV+OqpSZX46olq6tx1VKrq3HVUqurcdWS3dW4aqnJ1bhqqcnVuGqpydW4asnqaly11OpqXLXU6mpctXSuTATBT0CNI5ZkHt3vxZ2ydJax\\/j9OeBMnPJXhFx54tF39iOrl5v3K668027ysTn0\\/U2OmfwG99rhSUPwCbAk0XzxTJGbeYKUb4ZXvAitfXGXaWl6pNX8nqSqny+9sbR2Mto4PSp2W7\\/q6F4G81w92JzJcfLH4hn6vl37mlL97b91z8XxP8HOeZlc6U5zFyyMXO8F7xepvFRstPjS\\/VczyarbDwbWIeOpd8HvvSkbMeHX5QrSGnebVbI17\\/BRuLhq+fDdb1ZX6u9kqsRfvZlsevHhBW9GH6vVrxofQ6\\/5Mud0vf0bM5vUt4HbLLwSb5iyVX327jOVloBbfWwnTorWWVmY607S1cGgJzCJH2dr1uky66xqmmjEJiyBQfxTxdV9GUdHA4IEVKLX\\/hIfhOSu+Lef2r4Z8mhV7h1vmJyKe7Z8Uv3ZotU\\/MtGgFbK42pvjYHgzF+w\\/K+zVsQ73dMNTmxqG+o9zB\\/ctW7IBWFLORPo1XDh9T2E86aZrdrAxC2L7qMfy1KatnGqnH0LFMAp6YLFzEiDmq\\/hnwsqO\\/qCWB+UNnu8WLBNU8uyQvIsjJdhFdTtZV7DkZC5UHA37az\\/wnN\\/NCBovh76KKlYlxGYEjEIErPxvRGIOl29TEvdhU3rPTJzmtBu7Rwej1cRXSzTPXmN9K7t2c6Q6Vs1J9k56OFp+LNizmoWG50q3PQ8U25Fzjq+laRmYxAlLMLhjap9+f\\/vX09duv3\\/7mPf3z29+f\\/vP032+\\/Pn19+rf39Jv68I+nr80DXs2Q9REv76\\/qPOK9hrfLiqD\\/2OPWANVf6dv\\/AQAA\\/\\/8DAFBLAwQUAAYACAAAACEAbZqQOzEBAACdAgAAFAAAAHdvcmQvd2ViU2V0dGluZ3MueG1slNJLbwIhEADge5P+B8JdWU01zcbVpGlsmvSV9HFHdlZJgdkw2HX76ztutY940QthgPlggMls4534gEgWQyEH\\/UwKCAZLG5aFfH2Z9y6loKRDqR0GKGQLJGfT87NJkzeweIaUeCUJVgLl3hRylVKdK0VmBV5TH2sIPFlh9DpxGJfK6\\/i+rnsGfa2TXVhnU6uGWTaWOyYeo2BVWQPXaNYeQuryVQTHIgZa2Zr2WnOM1mAs64gGiLge7749r234YQYXB5C3JiJhlfpczO5EHcXpg6zrefcLjE4DhgfA2NjyNGO8MxRn\\/nEITmNGe4ZaDxspvMlvlwGjXjiW+GoEVyc6eNtuN5vyD8E6WW8\\/YY7xKmJDENV2mJ+pfQxv93ddpJ3D5unhhgP171NNvwAAAP\\/\\/AwBQSwMEFAAGAAgAAAAhAOdYbdP8AQAAbQYAABIAAAB3b3JkL2ZvbnRUYWJsZS54bWy8k99u2yAUxu8n7R0Q942x86+16lRZlkiVpl1s7QMQgm00AxbHiZu33wE7WbeoUtxpsyUE3+H84HzA\\/cOLrshBOlDWZDQeMUqkEXanTJHR56fNzS0l0HCz45U1MqNHCfRh8fHDfZvm1jRAMN9AqkVGy6ap0ygCUUrNYWRraTCYW6d5g0NXRJq7H\\/v6Rlhd80ZtVaWaY5QwNqM9xl1DsXmuhPxsxV5L04T8yMkKidZAqWo40dpraK11u9pZIQGwZl11PM2VOWPiyQVIK+Es2LwZYTH9jgIK02MWerr6BZgOAyQXgJlQu2GMWc+IMPMVB+QwzPSEgaOWL5RokT4Wxjq+rZCE1hCsjgSwb\\/1ii\\/5ukDY1XOOsFa\\/U1qkQqLmxIGOMHXiVUZawDZti6\\/8JG\\/uWRn6iKLkD6SFh4mrVyTnXqjqeVGgVQBeoVSPKk37gTvkddiFQBQb2sGUZXceMsWSzoZ0SZ3SCwnJ1VhLcVPfd9cr4rDCviMAJw\\/guZInAOc\\/BNaPOgQsnnpSWQL7Klnyzmps3HEnYDJ2Yoh\\/emfEgR1zgDnLEG7J87cgKlfntJH6PIx3neke+y8JK8vwYrPhXZ45HnKz\\/rHA9mb\\/rzIdW2N9+8kUVZfPmG\\/A3\\/3+9gaXfMhry+xtI2PzThR+h+r98A30HFj8BAAD\\/\\/wMAUEsDBBQABgAIAAAAIQDltgUYigEAANMCAAARAAgBZG9jUHJvcHMvY29yZS54bWwgogQBKKAAAQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACEkstKxDAUhveC71Cy7ySdDl5Cp+IFVwqC4wV3ITmOwSYpSbTO1rXv4CuIIC4En6HzRqadab2Cq3I43\\/l68ifZ1p0qoluwTho9RsmAoAg0N0Lq6RidTPbjDRQ5z7RghdEwRjNwaCtfXcl4SbmxcGRNCdZLcFEwaUd5OUZX3pcUY8evQDE3CIQOzUtjFfOhtFNcMn7NpoCHhKxhBZ4J5hluhHHZG9FSKXivLG9s0QoEx1CAAu0dTgYJ\\/mQ9WOX+HGg7X0gl\\/ayEP9Gu2dN3TvZgVVWDKm3RsH+Czw8PjtujxlI3WXFAeSY45RaYNzavH+v3+m3+UL+G73P9NL+vX5o6OpNamMpl+AvcBFsw5w\\/DHVxKEDuzfNt6UNEpc7IIv9HXJsO\\/oWbOwq1sLjJfb4m+7JxHVmoPIh+SIYlJEg\\/JhKzRNKWEXPTODsqWUS42AxGFCOgisK5zlu7uTfbR0pfGafBt0hFZ+H7MfwrVcut\\/jaOYrE+SEQ3Sb8ZOkLdLf3+G+QcAAAD\\/\\/wMAUEsDBBQABgAIAAAAIQDLS\\/uN9wEAAOsDAAAQAAgBZG9jUHJvcHMvYXBwLnhtbCCiBAEooAABAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAJxTwW4TMRC9I\\/EPK98bJxtU0shxhVKhHoBWyrY9G+9sYuG1LduNGv6Ff0BICC78w34S412yOMCJPazevLGfn2fG7PKp1cUefFDWrMhsMiUFGGlrZbYrcle9PluQIkRhaqGtgRU5QCCX\\/PkzduutAx8VhAIlTFiRXYxuSWmQO2hFmGDaYKaxvhURQ7+ltmmUhCsrH1swkZbT6TmFpwimhvrMjYJkUFzu4\\/+K1lYmf+G+OjjU46yC1mkRgb9LO\\/WktrFldGRZZaPQlWqBlxfIjxG7FVsIfMboANiD9XXg5eIlowNk653wQkYsIZ+dzxeMZgR75ZxWUkSsLn+rpLfBNrG46S0XSYDRfAnDa2xAPnoVDxyl8pC9USZZmTM6IPTmxdYLtwt8ngyOEdtIoWGNFeCN0AEY\\/U2waxCpu7dCJYP7uNyDjNYXQX3E\\/pakeC8CpLqtyF54JUwkw7Ih6LF2IXrefeo+d9+6L\\/j\\/0X3vvjI6pnqY78ixepEKOoDThX3Q20F8arRSUUO4afCa8R++Z7nv3sPgOrOTOzue8Yfq2rZOGKw1HRHW+kO4c5W9SoPyq5ynZDYCDyruNk7I1KiLssyHIUuxDbJQY3fH\\/owEu8YreJ0OwL1mC\\/Vxzd+JNF73w9vF2ZtM8evn6cjhUIyPiv8EAAD\\/\\/wMAUEsDBBQAAgAIAOFqolDbQpVIiAsAANxOAAARAAAAd29yZC9kb2N1bWVudC54bWztHFtPG9n5vVL\\/w8hPQQI8vgDGDWxDAmmkVkKbtPuIhvFgphnPWDNjCPsEueyqTRTSbLIbZZNsSVbqQ6sWCDQOFyPtLzj+C\\/tL9vu+c2Y8vmbGGIKzEJHxnMt3vvvlnGMufnarYEhLmu3oljkRSwzLMUkzVSunm\\/mJ2J9vzAxlYpLjKmZOMSxTm4itaE7ss8nf\\/ubicjZnqaWCZroSgDCd7HJRnYgtum4xG4876qJWUJzhgq7almMtuMOqVYhbCwu6qsWXLTsXT8oJmT4VbUvVHAfWu6yYS4oTE+DUW+Gg5WxlGSYjwHRcXVRsV7tVg5GIDGQkPh7PNANKdgEIKEwmmkGlIoMajSNWTYDSXQECrJogjXQHqQVxo91BSjZDGusOUqoZUqY7SE3qVGhWcKuomdC5YNkFxYVXOx8vKPbNUnEIABcVV5\\/XDd1dAZjyqAdG0c2bXWAEs3wIhVQuMoSxeMHKaUYq50GxJmIl28yK+UP+fEQ9y+eLhzfDDkM\\/n3JFOAeiPG5rBvDCMp1FvehbeKFbaNC56AFZ6kTEUsGI+d4pkT6ee7rCWVkDGAZ9wf+CwTHvDDEhh5AIgvBnhEGhfk0PkwJoYW3hrlgTYG5iJBqAZBOAUVXPRYMxKmDEYWYAjqNFAzPigXFWCjVTXy7mj6ctV22rVKxB048H7VrN9pfNaATKo428LjrHQ+b6olIEl1BQs9fypmUr8wZgBDokgRpIJAGJy1NCo4tNQqowb+VW8FmUlrOQauQ+n4jJciYtT2XAK4qmWRsb05cSmdSU33hFW1BKhts8fDbQRJBnbXz8VYX+JcWYiNl6ftGNxScvxv3Oecu6id75ugtuHcahxskI0FQKQMLcVWtKUW\\/inNrYaTPnj6QOuyW+2ONOskdsj+1VV9ku22Hl6m1WYdtskx3Cv83qugRNa+yoeh86NuF3l+1XH3hd+zBui1VgLnbhs1xdR9xdosDmdHx8HnYk\\/0f2DfsHeyP99G94AHnsLVFSYTtA+B4n9AA6\\/g9NW0hydQ2IJQ70G6nbbBdFiHIiAlY7CvKn\\/TNInwP67d5Q8jCkZOshQg9NcJW8g6tqhoYReSJW0FxbV1XLhCrC1exYEDRfSHFd27eyWdvKlVT32pWYh1pKzsjJ1KDE3gr86md3EkNtaiN\\/PRgfgjAsNarqoFS9w\\/ahfaN6Gz7tQO9h9WsQtVDhQSmVHPzY4lSB9cDr8Pr6Hfim\\/7HH7Dl7JP1891tpLvjTd9RUqrfrcA4Mx8EzaXlGTjZgKxoJAobCrFNUVFDIoq05mr2kxSYlOSmxF2C\\/76WknJQl1AmQ\\/2bjUkJzPjbX5i1Ig+t51om4yAxLj6egh9a2RQC97FAMLHkoYDpgaAIJMQjE84ptVf+GFgMecJtVpOqaBJx8iz4R3GQZrYmCYgU4jbLk3lQMF134Vn1QfSjFwECfgf6+ZE\\/ZhgQx5g17AsYJH56yf7JHsW7FMJ2UryR6KAafAegHLxl63vS5VCpqtqPaetENsqpBbhFnI9EX2sm0jsQugWNAg+B2QALxEpky25XIUb4Vr+\\/AJX4Fz\\/IQvbwlceLosshwwF9CzrPKLYltw+BN6QJ93od+gIYQ1gMTHgy0srcuCBg4gwba3qm1tlr2X8owdrjRUAgqg9E0cAhyc2th2kbo7koRAORtpUBZ7gdz1jbLbkLKtooLSi2d38m5mA+5lh+B\\/n34BfWREl0KuAGV0\\/LA4MVespenwKRUIplKJ8dGxsePEyRDrRVREcLqH3cFoH93wW8cSidORhSMJwGpTajy1upzqDlJG\\/qQWTpFzTDC2eVkQdGNUPCgQu3Syk+BryVY6vdIyrBd+hTc8TYWeGUIYrun4eyeiahbwXXFixRsxbAA8fUJfN6HqtpPmz6FnKiHuUwb9cc0pIFxUPyhdR\\/wKEPJC6RA9ITEhsdgL6ldhepwj6RQEds9lO90m3YkxtIz06nTj0pY4L7nVFXvVB9i4o5pnIT5H1K9Vpf+laPr\\/ehYciQzHUHvAQ1EZxMX7ZKZmcvJxHhoTeaDe8FMynSBZaANXtq7LXSmXLf\\/R3XQerYjNxuI4JRNy+nMjBwltEHW0SaR3+FqT5ze92syEv4B36Kk7BNT9U1pUOoVJDC859B7hByQEgMnBDg50HMvKCcS0+mpPqgM2zi8E\\/rpqMMdONxt1du+MvUNDFzzLgwQmlJhW9W\\/Q8MeKhK48u3qfRh2h+\\/oiYHvwV5x7DfsFdtgr2HUa\\/YmhAolUqnkaLqVttT3zAaaerGdw35AbAFlouGdf6Kwzg6yPa+L6ntCkxLGRbWN+JEdHdn+ITnfNWLGIe5MdH+00KtkqLa7eUbSoXYWhMcXTQqF2QG64X3q9SIa3xbnUxqimm9JLXWzW6cstOEEUtNudLN+N\\/oxEPoOnAod+XBvgzud70D9gFknsVEdGWZ0YzoEfwp+FM8yt9Cb0v7s+nlh0WBJzSwalNrw7syofki15u70Npk8nVRD1HwfUG+2G0INO23yfHUaelwh5t\\/xjhe4OM7VuDEg8GBQU+GWbOuvHXU89Gw4WuDauw6u2bfKbH\\/RNEQk0FHAEfz\\/NdKGdB2JWyeN1RI\\/sKmPSFAm8UC+NSDVtkDAug+C1l2hVzrN2RInd+LEDitZcS4OZW31Lm2IlUVqEBgnjn1wm4SfEQ1KgDZd\\/ag+xCMgyB4oC98Xe1mYTMAQmraHywTpgYbf9aGsjuiey21PTttALgSHVbr7co\\/Yc9\\/Ll5qSq4etk6tGtlLAwTSLmLhGtlwh2Um0QXVIigICrt6TSE1Q\\/cEaxD0kWPPMnGXXjpojpmEtppEL+IHbha9FQVfQ6hizYVMf9\\/UvzLX5GfhkuUb6uCM2r1aDR\\/LAoZ\\/vftuSHahwcx1\\/+ix8PCHzRfd4WNMbcpS76MjqyqAQYaRTsh+OtkBOdSKnG2\\/qfXGD9\\/U8zxbEj4aIMpCNnsrJI4lLI+koqdwrHj04\\/++Ew\\/WsqVyfasYrYO0e8fuwVvi3VwcRfA7Jxx7g7izddOXbcShFklUFMrEQ2+A9054OJETDty+U6vi1ZM+UapK9oOuyIttpymmOUwa20djs6dSWXgZRoXT7iPK8wEWdPgt4GzzzDrvPN3eMn7N6eXKSvQbx3fOuKfo3rtqc9j447k0XXmLt0E7rpkQ3jGuHzJvoe3bx\\/BnP\\/cv0BoiIoosGU9LaZ5fnO6hF22S737YeNnj+3Dp9POCyrLMvaqSNZNybR0074CW8EDzGIvrGBLZlz+u183rtDNZrJ8yalihcyqTHp8ZboVDf86tC4UOKe5J4ufOGeAj9nDe+AEDL4st2\\/OqiUnItDhG6\\/6isWCXX71vQb2k5v\\/OyZhh\\/UgiSoS24HNJYAFTuliJwwzDUtj\\/eCA0XtqybHo1QU9CkBd123M8tD11DCb5R52XLKBXMQH9dg2n9YUox\\/a8WWn\\/x3nwcfLZctfUcfszDE2Bw1NOZzDinp2VzvG6ma0f0Xjeo7VJiZGZknMcqlf8vkFK\\/CCzXzOKluhOFuu\\/teCB67E8DqIY5We+VR33GNtgP7BX7nr2Ez6\\/ZU\\/j0n9OIHx+J3jBf3XzBHrPvT+erjM1ciOoQ+1USLQPxhQ5fiQkFIHSZ0gbowGmcmHYU+nLW+dLjfmI0JlouO\\/VtzQKIfAm\\/6SA1+upkUQfD7Oh4F\\/h7hEmrw514zfFHcf+uOmXZOc12AtGYVjV1ww8DgSG\\/mnCBX699Dr\\/fsX9BuHjeJ4HiHLOP5tIv9KQwPfeuZ9m74sP2MvaTK52bdt88BXI01Z1tVSAQmPz1L7mvTyTGZVptEenLpDKc7GIe6iTkpVXEMSl+kxnXmohlRqi4gbLQtQq1XowI8DYmk04uagrEAazFMlQ6WZYbeM2XXHoVf1ZFtQxkstgfxTHUnLNULHUQtm5qs7qrApKpUa+Y4hTSR\\/4nZuK1P0s3+QtQSwMEFAACAAgA4WqiUNZks1HtAAAAMQMAABwAAAB3b3JkL19yZWxzL2RvY3VtZW50LnhtbC5yZWxzrZLLTsMwEEX3SPyDNXvipDyEqjrdoErdQvgA15k8hGNbnimQv8cqAlJRVV1kOdeac89IXq0\\/ByveMVLvnYIiy0GgM77uXavgtdrcPIIg1q7W1jtUMCLBury+Wj2j1ZyWqOsDiURxpKBjDkspyXQ4aMp8QJdeGh8HzWmMrQzavOkW5SLPH2ScMqA8YoptrSBu61sQ1RjwErZvmt7gkzf7AR2fqJAfuHtB5nQcJayOLbKCSZglIsjTIos5ReifBV2gUMyqwKPFqcBhPld\\/P2c9p138az+M32FxzuFuTofGO670zk48fqMfCXn00csvUEsDBBQAAgAIAOFqolDc4512BwcAAPIWAAARAAAAd29yZC9zZXR0aW5ncy54bWy1WFtz27gVfu9M\\/4NGr60jAgRAUV1nh9fY2TjORk49+wiRsMSaJBgQkqzt9L\\/vISlaTn28ddquH2zyfOeGczMPfvjxoSonO2XaQtfnU\\/LGmU5Unem8qNfn0y836dl8OmmtrHNZ6lqdTw+qnf749s9\\/+mG\\/aJW1wNZOQEXdLqrsfLqxtlnMZm22UZVs3+hG1QDeaVNJC69mPaukud82Z5muGmmLVVEW9jCjjiOmRzX6fLo19eKo4qwqMqNbfWc7kYW+uysydfwzSpjX2B1EYp1tK1Xb3uLMqBJ80HW7KZp21Fb9t9oA3IxKdr93iF1Vjnx74rziuHtt8keJ17jXCTRGZ6ptIUFVOTpY1CfD7JmiR9tvwPbxiL0qECdO\\/\\/TUc\\/59CugzBSIr8u\\/TIY46ZiD5RE+rvk8NH9W0h0o9jIra8jWhHaAPxcpIc3ga1ypbXK5rbeSqBHcgvhMI0aT3bjKcdfoWmuZXravJftEok0HlQMc5znTWAZAvfbe00gL7om1UWfYtmJVKgvb9Ym1kBc0zUnqZ\\/FiAn4y2KutKGRhVXoBeo2R+XZeHTlTVcIJMVYPBjpKZQ9NJ7YpcmZtDAx6bVgbJ8hEMyrU2hd1UUSnbFiIj281zcBC18DuoD8\\/hZZdgwh6BZVPUkd4ezw0\\/HdJpPp++u7S3hF\\/Lh\\/Lwy9fby79\\/2amZulEPyx2\\/2DhXS6PqlX\\/7UXye73+5Tm8fyNf5Mgpvby\\/Lff2wmr37lPHtx5+3u\\/swpib8y6H4kvKL4Py8s9DKEiy+J\\/9Yc\\/a18ZX+dGN\\/shfZV3O1Bo4hlOpObkt7I1dLqxsQ2kmoB8+ZD3C2kUZmVpllIzNITKRra3Q58uX6o7YRzDQDLXeU6Cfc6Wk5TEuQqGUFQftmAl7pXHWebk3x+lKejtYhwrOXDWmY7gbyfNNV5tIeSpWC88viV8hZ\\/n7b2gI09nPwf\\/Dg9xxQdWf5GnqpK5dUSbuFMP1BxvpMpGXRXBXGaHNZ51D1f5SxbatugRlGinsD5XEfamt1dXFoNnDm\\/0NEZ0\\/LCP5F5+348FlrO7I6DvWYL46edugJcQhJWIgilLiujyIu9ZmDIpwEnOGIcDwcEU4Q4doE4Q7utWCCeygS0SRIUCR2\\/GCOIYQQl7so4rpUoF6TOXiNawtcEePaYpfzEEe8wMXtJC6lAYZQwikXKOJSPkc9oMwJHTQ6lIMIro2LmFMUebGqaCA8ip6Uhq7vo765xAlCD0dIiJ\\/UJR4PYxQJ3ShCvXZjGgW4B0lXvyiSeiSJMIQxRgI0c0ww7qPamOckbowjLqEcRSJwD40oi11foH3KEpGKFEfABe8FJA5QGe6INEXtcOJwPD+cMpai0eGuG1G0djhj1ENjwAOoNzQGPGJzvK47JMRlUpcHaH4EeD1HfRPc8Ty060XERIjWm0hdF+8sj4sErzdPEI+g2jyPRilaO17IHHyOzl0ifPQ8c5gHc9S3OWdRhEZ0Dnl7QVviMB\\/3IBVBgtaB7zhhimrzCU8J2qe+ByWCVojvczf2cETECW4ncn0P7W0\\/gQEjcIQKfB74KfUi1LcAxhjecwEjEYlxhKUErbdgzoTnvYD4IdqnQQBZQCMaJI5HX0BI5KJISMQLMySkMMrRbIeMzFNchgmaonGD0c9CNAZhDAMWl4H5insQuV35oAjjEUWrN5o7PkU9iBKY8SgSh9BcKY5AhUQvICHuQZxSHqFTDFpunqIyicu8gOCI50ZoXSfwVUPQ2kkCL3FxO5FH8ZmYxMxjaH6SlMYRGp20++JBeyFlAKIzMZ0TB\\/8KSAPBHPQ8aegmeMWnMfyrRydfmjjBUL2zx6\\/tatFd63wy41O3Ok2qQSKS1coUcnLVXfzMOo6VuQ+LesRXCtZu9RRZblcjeHY2AG0lyzKF5WEEnIGeF20Tq7v+ubySZn3Se+QwKBX22PePurorBmXeGb1tBnRvZDOsRCMLYewoWdT2Q1GN9Ha7Wo5StTSHJ9C2zq93po\\/TKTz7hYXVpl8tP8h+Rep5zfbs85fjClWaZbf+qCvZNMMWtVqT82lZrDe2v5Ww8JZLc9+\\/rNb0iNEeowPWv8isOxlwHx9ONDrSnvC5I8090dhIYycaH2n8RBMjTfRXFbC\\/mrKo72GhGx87+p0uS71X+cUJf0YaggDpNvZGrrtN+LhGto3MVL8k\\/seLwF7aynW\\/zw4raKWsKbJM17DlQ66PVjayUfFwowFFrAfC8YqjnewW6sHCXp4XdjppmyKv5EN3I0P7oXHkLuVBb+03vB3WMTffasillePC+o1w30j\\/5kt305IVUPTLQ7U6XaD8dXC8LFpYshtppNVmxP7WY4R3112XeXetNtD\\/CZ2cpLEfnnHhx2cs5t5Z4HD\\/LIZPXCemASUe+9exn8fL6re\\/AVBLAwQUAAIACADhaqJQ36TSbFQBAAAgBQAAEwAAAFtDb250ZW50X1R5cGVzXS54bWy1lMtqwzAQRfeF\\/oPRNthKuiilxMmij2UbaPoBqjRORGVJaCavv+84TkMpaQxNsjHYM\\/feM8Kj4Xhdu2wJCW3wpRgUfZGB18FYPyvF+\\/Q5vxMZkvJGueChFBtAMR5dXw2nmwiYsdpjKeZE8V5K1HOoFRYhgudKFVKtiF\\/TTEalP9UM5E2\\/fyt18ASecmo8xGj4CJVaOMqe1vy5JUngUGQPbWOTVQoVo7NaEdfl0ptfKfkuoWDltgfnNmKPG4Q8mNBU\\/g7Y6V75aJI1kE1UohdVc5dchWSkCXpRs7I4bnOAM1SV1bDXN24xBQ2IfOa1K\\/aVWlnf6+JA2jjA81O0vt3xQMSCSwDsnDsRVvDxdjGKH+adIBXnTtWHg\\/Nj7K07IYg3ENrn4GSOrc2xSO6cpBCRNzr9Y+zvlW3UOQ8cIZE9\\/tftE9n65PmguQ0MmAPZcnu\\/jb4AUEsBAi0AFAAGAAgAAAAhAB6RGrfvAAAATgIAAAsAAAAAAAAAAAAAAAAAAAAAAF9yZWxzLy5yZWxzUEsBAi0AFAAGAAgAAAAhAOGLTSY6BgAAhhoAABUAAAAAAAAAAAAAAAAAIAMAAHdvcmQvdGhlbWUvdGhlbWUxLnhtbFBLAQItABQABgAIAAAAIQA\\/wzC84gwAAOt4AAAPAAAAAAAAAAAAAAAAAI0JAAB3b3JkL3N0eWxlcy54bWxQSwECLQAUAAYACAAAACEAbZqQOzEBAACdAgAAFAAAAAAAAAAAAAAAAACcFgAAd29yZC93ZWJTZXR0aW5ncy54bWxQSwECLQAUAAYACAAAACEA51ht0\\/wBAABtBgAAEgAAAAAAAAAAAAAAAAD\\/FwAAd29yZC9mb250VGFibGUueG1sUEsBAi0AFAAGAAgAAAAhAOW2BRiKAQAA0wIAABEAAAAAAAAAAAAAAAAAKxoAAGRvY1Byb3BzL2NvcmUueG1sUEsBAi0AFAAGAAgAAAAhAMtL+433AQAA6wMAABAAAAAAAAAAAAAAAAAA7BwAAGRvY1Byb3BzL2FwcC54bWxQSwECPwMUAAIACADhaqJQ20KVSIgLAADcTgAAEQAAAAAAAAAAAAAAtoEZIAAAd29yZC9kb2N1bWVudC54bWxQSwECPwMUAAIACADhaqJQ1mSzUe0AAAAxAwAAHAAAAAAAAAAAAAAAtoHQKwAAd29yZC9fcmVscy9kb2N1bWVudC54bWwucmVsc1BLAQI\\/AxQAAgAIAOFqolDc4512BwcAAPIWAAARAAAAAAAAAAAAAAC2gfcsAAB3b3JkL3NldHRpbmdzLnhtbFBLAQI\\/AxQAAgAIAOFqolDfpNJsVAEAACAFAAATAAAAAAAAAAAAAAC2gS00AABbQ29udGVudF9UeXBlc10ueG1sUEsFBgAAAAALAAsAwQIAALI1AAAAAA==";

        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(Base64.decode(base64String, Base64.NO_WRAP));
            fos.flush();
            fos.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void onBackPressed() {
    }

    private SwipeRefreshLayout.OnRefreshListener MyRefresh = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            swipeRefreshLayout.setRefreshing(true);
            if (App.isOnline(Objects.requireNonNull(getContext()))) {
                if (offlineOrders.size() > 0) SynchronizationOrders();
                else UpdateOrders();
            }
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
                if (dy > 0 || dy < 0 && fab.isShown())
                    fab.hide();

                if (dy > 0 && (CurrentPage + 1) <= TotalPage) //check for scroll down
                {
                    assert lm != null;
                    visibleItemCount = lm.getChildCount();
                    totalItemCount = lm.getItemCount();
                    pastVisiblesItems = ((LinearLayoutManager) lm).findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) == totalItemCount) {
                            loading = false;
                            LoadOrders(new ArrayList<>(), (byte) 2);
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
                                if (!App.isOnline(getContext())) {
                                    Intent intent = new Intent(getActivity(), CreateOrderActivity.class);
                                    if (which == 0)
                                        intent.putExtra("type_id", 1);
                                    else
                                        intent.putExtra("type_id", (byte) (which + 2));
                                    startActivity(intent);
                                } else UpdateOrderInfo(which);
                            }
                        })
                        .create()
                        .show();
            }
        });
    }

    private void UpdateOrderInfo(final int which) {
        ProgresBar.setVisibility(View.VISIBLE);
        NetworkService.getInstance()
                .getJSONApi()
                .OrderNew(App.UserAccessData.getToken())
                .enqueue(new Callback<AnswerOrderNew>() {
                    @Override
                    public void onResponse(@NonNull Call<AnswerOrderNew> call, @NonNull Response<AnswerOrderNew> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {


                                if (!response.body().getDefaultFields().get((short) 52).equals(""))
                                    App.OrderInfo = new OrderInfo((short) 52, response.body().getDefaultFields().get((short) 52), response.body().getFieldValues(), true);
                                else if (!response.body().getDefaultFields().get((short) 63).equals("") && !response.body().getDefaultFields().get((short) 64).equals(""))
                                    App.OrderInfo = new OrderInfo((short) 64, response.body().getDefaultFields().get((short) 63), response.body().getDefaultFields().get((short) 64), response.body().getFieldValues());
                                else if (!response.body().getDefaultFields().get((short) 63).equals(""))
                                    App.OrderInfo = new OrderInfo((short) 63, response.body().getDefaultFields().get((short) 63), response.body().getFieldValues(), false);


                                if (response.body().getDefaultFields().containsKey((short) 128) && !response.body().getDefaultFields().get((short) 128).equals(""))
                                    App.OrderInfo.setURL_SCAN_FILE(response.body().getDefaultFields().get((short) 128));
//                                for (Map.Entry<Short, String> entry : response.body().getDefaultFields().entrySet()) {
//
//                                    if (!entry.getValue().equals("")) {
//                                        App.OrderInfo.setOD_ID(entry.getKey());
//
//                                        if(entry.getKey() == (short) 63)
//                                        App.OrderInfo.setOD_Value(entry.getValue());
//
//                                        App.OrderInfo.setFieldValues(response.body().getFieldValues());
//                                        App.UserInfo = response.body().getUserInfo();
//
//                                        Hawk.put("UserInfo", App.UserInfo);
//                                        Hawk.put("OrderInfo",  App.OrderInfo);
//
//                                        if(!response.body().getDefaultFields().entrySet().contains((short)63)
//                                                || !response.body().getDefaultFields().entrySet().contains((short)64))
//                                        break;
//                                    }
//                                }
                                App.UserInfo = response.body().getUserInfo();
                                Hawk.put("UserInfo", App.UserInfo);
                                Hawk.put("OrderInfo", App.OrderInfo);

                                Intent intent = new Intent(getActivity(), CreateOrderActivity.class);
                                if (which == 0)
                                    intent.putExtra("type_id", 1);
                                else
                                    intent.putExtra("type_id", (byte) (which + 2));
                                ProgresBar.setVisibility(View.GONE);
                                startActivity(intent);
                            } else {
                                ProgresBar.setVisibility(View.GONE);
                                Toast.makeText(getContext(), R.string.auth_error, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            ProgresBar.setVisibility(View.GONE);
                            Toast.makeText(getContext(), R.string.auth_error, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<AnswerOrderNew> call, @NonNull Throwable t) {
                        ProgresBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), R.string.server_lost, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}