package com.bmvl.lk.ui.patterns;

import android.content.DialogInterface;
import android.content.Intent;
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
import com.bmvl.lk.data.SpacesItemDecoration;
import com.bmvl.lk.ui.create_order.CreateOrderActivity;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.swipe.util.Attributes;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

public class PatternsFragment extends Fragment implements OnBackPressedListener {

  //  private static List<com.bmvl.lk.data.models.Orders> Orders = new ArrayList<>();

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private PatternAdapter PatternAdapter;
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

        initRecyclerView();
        recyclerView.scrollToPosition(0);

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
                                intent.putExtra("Pattern", true);
                                startActivity(intent);
                            }
                        })
                        .create()
                        .show();
            }
        });
    }

    private void initRecyclerView() {
//        OrderSwipeAdapter.OnOrderClickListener onClickListener = new OrderSwipeAdapter.OnOrderClickListener() {
//
//            @Override
//            public void onDeleteOrder(int id, final int position) {
//                OrderAdapter.closeAllItems();
//                NetworkService.getInstance()
//                        .getJSONApi()
//                        .DeleteOrder(App.UserAccessData.getToken(), id)
//                        .enqueue(new Callback<StandardAnswer>() {
//                            @Override
//                            public void onResponse(@NonNull Call<StandardAnswer> call, @NonNull Response<StandardAnswer> response) {
//                                if (response.isSuccessful() && response.body() != null) {
//                                    if (response.body().getStatus() == 200) {
//                                        // UpdateOrders();
//                                        List<Orders> insertlist = new ArrayList<>(Orders);
//                                        insertlist.remove(position);
//                                        OrderAdapter.updateList(insertlist);
//                                        Snackbar.make(Objects.requireNonNull(getView()), "Заявка удалена!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
//                                    }
//                                }
//                            }
//
//                            @Override
//                            public void onFailure(@NonNull Call<StandardAnswer> call, @NonNull Throwable t) {
//                            }
//                        });
//            }
//
//            @Override
//            public void onCopyOrder(final com.bmvl.lk.data.models.Orders order) {
//                OrderAdapter.closeAllItems();
//                NetworkService.getInstance()
//                        .getJSONApi()
//                        .CopyOrder(App.UserAccessData.getToken(), order.getId())
//                        .enqueue(new Callback<AnswerCopyOrder>() {
//                            @Override
//                            public void onResponse(@NonNull Call<AnswerCopyOrder> call, @NonNull Response<AnswerCopyOrder> response) {
//                                if (response.isSuccessful() && response.body() != null) {
//                                    if (response.body().getStatus() == 200) {
//                                        // UpdateOrders();
//                                        List<Orders> insertlist = new ArrayList<>();
//                                        Date currentDate = new Date();
//                                        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
//                                        insertlist.add(new Orders(response.body().getOrderId(), order.getUser_id(), order.getType_id(), order.getStatus_id(), dateFormat.format(currentDate)));
//                                        OrderAdapter.insertdata(insertlist, true);
//                                        Snackbar.make(Objects.requireNonNull(getView()), "Заявка скопирована!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
//                                    }
//                                }
//                            }
//
//                            @Override
//                            public void onFailure(@NonNull Call<AnswerCopyOrder> call, @NonNull Throwable t) {
//                            }
//                        });
//            }
//
//            @Override
//            public void onDownloadOrder(final int id) {
//                OrderAdapter.closeAllItems();
//                if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//                    NetworkService.getInstance()
//                            .getJSONApi()
//                            .DOWNLOAD_ORDER_CALL(App.UserAccessData.getToken(), id)
//                            .enqueue(new Callback<AnswerDownloadOrder>() {
//                                @Override
//                                public void onResponse(@NonNull Call<AnswerDownloadOrder> call, @NonNull Response<AnswerDownloadOrder> response) {
//                                    if (response.isSuccessful() && response.body() != null) {
//                                        if (response.body().getStatus() == 200) {
//                                            if (SavePhpWordFile(response.body().getDocx(), id))
//                                                Toast.makeText(getContext(), "Документ успешно скачан!", Toast.LENGTH_SHORT).show();
//                                            else
//                                                Toast.makeText(getContext(), "Ошибка скачивания!", Toast.LENGTH_SHORT).show();
//                                        }
//                                    }
//                                }
//
//                                @Override
//                                public void onFailure(@NonNull Call<AnswerDownloadOrder> call, @NonNull Throwable t) {
//                                }
//                            });
//                } else
//                    Toast.makeText(getContext(), "Нет разрешения на запись файла!", Toast.LENGTH_SHORT).show();
//
////                if(ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()),Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
////                    SavePhpWordFile("");
////                    Toast.makeText(getContext(), "Все ОК", Toast.LENGTH_SHORT).show();
////                }
////                else Toast.makeText(getContext(), "Нет разрешения на запись файла!", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onEditOrder(final com.bmvl.lk.data.models.Orders order) {
//                //         Snackbar.make(Objects.requireNonNull(getView()), "Изменение " + order.getType_id(), Snackbar.LENGTH_LONG).setAction("Action", null).show();
//
//                OrderAdapter.closeAllItems();
//                final SendOrder OpenOrder = new SendOrder(order.getType_id());
//
//                NetworkService.getInstance()
//                        .getJSONApi()
//                        .getOrderInfo(App.UserAccessData.getToken(), order.getId())
//                        .enqueue(new Callback<AnswerOrderEdit>() {
//                            @Override
//                            public void onResponse(@NonNull Call<AnswerOrderEdit> call, @NonNull Response<AnswerOrderEdit> response) {
//                                if (response.isSuccessful() && response.body() != null) {
//                                    if (response.body().getStatus() == 200) {
//                                        //Toast.makeText(view.getContext(), "Заказ успешно создан!", Toast.LENGTH_SHORT).show();
//                                        // CreateOrderActivity.this.finish();
//                                        OpenOrder.setId(order.getId());
//                                        OpenOrder.setFields(response.body().getOrderFields());
//                                        OpenOrder.setProby(response.body().getProby());
//                                        // String json = OpenOrder.getJsonOrder();
//                                        //  Log.d("JSON", json);
//
//                                        Intent intent = new Intent(getActivity(), CreateOrderActivity.class);
//                                        intent.putExtra("type_id", order.getType_id());
//                                        intent.putExtra("isEdit", true);
//                                        intent.putExtra(SendOrder.class.getSimpleName(), OpenOrder);
//                                        startActivity(intent);
//
//                                    }
//                                }
//                            }
//
//                            @Override
//                            public void onFailure(@NonNull Call<AnswerOrderEdit> call, @NonNull Throwable t) {
//                                Toast.makeText(getContext(), "Сервер не доступен!", Toast.LENGTH_SHORT).show();
//                                Log.d("ОШИБКА","ТЕКСТ",t);
//                            }
//                        });
//            }
//        };
        recyclerView.addItemDecoration(new SpacesItemDecoration((byte) 10, (byte) 10));
        recyclerView.setHasFixedSize(true);
        PatternAdapter = new PatternAdapter(getContext());
        (PatternAdapter).setMode(Attributes.Mode.Single);
        recyclerView.setAdapter(PatternAdapter);
    }

}
