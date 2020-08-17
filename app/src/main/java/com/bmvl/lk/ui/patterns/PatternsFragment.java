package com.bmvl.lk.ui.patterns;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bmvl.lk.App;
import com.bmvl.lk.R;
import com.bmvl.lk.Rest.AnswerCopyOrder;
import com.bmvl.lk.Rest.AnswerOrderEdit;
import com.bmvl.lk.Rest.AnswerPatterns;
import com.bmvl.lk.Rest.NetworkService;
import com.bmvl.lk.Rest.Order.SendOrder;
import com.bmvl.lk.Rest.StandardAnswer;
import com.bmvl.lk.data.OnBackPressedListener;
import com.bmvl.lk.data.SpacesItemDecoration;
import com.bmvl.lk.data.models.Orders;
import com.bmvl.lk.data.models.Pattern;
import com.bmvl.lk.ui.create_order.CreateOrderActivity;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.swipe.util.Attributes;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.orhanobut.hawk.Hawk;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PatternsFragment extends Fragment implements OnBackPressedListener {

    private static List<Pattern> Patterns = new ArrayList<>();
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private PatternAdapter PatternAdapter;
    private FloatingActionButton fab;
    private TextView message;

    //Для подгрузки данных
    private boolean loading = true;
    private int pastVisiblesItems;
    private int visibleItemCount;
    private int totalItemCount;
    private static byte TotalPage;
    private static byte CurrentPage = 0;


    @Override
    public void onBackPressed() {
    }

    @Override
    public void onResume() {
        super.onResume();
//        if (PatternAdapter != null)
//            PatternAdapter.notifyDataSetChanged();
        if (App.isOnline(Objects.requireNonNull(getContext()))) {
            if (Patterns.size() == 0) LoadPatterns(Patterns, (byte) 0);
            else UpdatePatterns();
        }
    }

    private void UpdatePatterns() {
        List<Pattern> insertlist = new ArrayList<>();
        CurrentPage = 0;
        LoadPatterns(insertlist, (byte) 1);
    }

    public static PatternsFragment newInstance() {
        return new PatternsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View MyView = inflater.inflate(R.layout.fragment_order, container, false);
        if (Hawk.contains("PatternsList"))
            Patterns = Hawk.get("PatternsList");

        recyclerView = MyView.findViewById(R.id.list);
        swipeRefreshLayout = MyView.findViewById(R.id.SwipeRefreshLayout);
        fab = MyView.findViewById(R.id.floatingActionButton);
        message = MyView.findViewById(R.id.empty_msg);

        swipeRefreshLayout.setOnRefreshListener(MyRefresh);

        if (Patterns.size() == 0) message.setVisibility(View.VISIBLE);
        else message.setVisibility(View.GONE);

        initRecyclerView();
        // PatternAdapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(0);

        YoYo.with(Techniques.Tada)
                .duration(700)
                .playOn(fab);

        FabLisener(); //Слушатель нажатия кнопки меню
        RecyclerViewEndLisener(); //Слушатель конца списка
        return MyView;
    }

    private SwipeRefreshLayout.OnRefreshListener MyRefresh = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            swipeRefreshLayout.setRefreshing(true);
            if (App.isOnline(Objects.requireNonNull(getContext())))
                UpdatePatterns();
        }
    };

    private void LoadPatterns(final List<Pattern> NewList, final byte Type) {
        NetworkService.getInstance()
                .getJSONApi()
                .getPatterns(App.UserAccessData.getToken(), (byte) 0)
                .enqueue(new Callback<AnswerPatterns>() {
                    @Override
                    public void onResponse(@NonNull Call<AnswerPatterns> call, @NonNull Response<AnswerPatterns> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {
                                TotalPage = response.body().getOrders().getTotal_pages();
                                CurrentPage = response.body().getOrders().getCurrent();
                                //PatternAdapter.notifyDataSetChanged();
                                NewList.addAll(response.body().getOrders().getPatterns());
                                Hawk.put("PatternsList", NewList);
                                switch (Type) {
                                    case 0:
                                        PatternAdapter.notifyDataSetChanged();
                                        recyclerView.scrollToPosition(0);
                                        break;
                                    case 1:
                                        PatternAdapter.updateList(NewList);
                                        swipeRefreshLayout.setRefreshing(false);
                                        recyclerView.scrollToPosition(0);
                                        break;
                                    case 2:
                                        Patterns.addAll(NewList);
                                        PatternAdapter.notifyDataSetChanged();
                                        loading = true;
                                        break;
                                }
                            }
                        } else swipeRefreshLayout.setRefreshing(false);
                        if (Patterns.size() == 0) message.setVisibility(View.VISIBLE);
                        else message.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(@NonNull Call<AnswerPatterns> call, @NonNull Throwable t) {
                        swipeRefreshLayout.setRefreshing(false);
                        if (Patterns.size() == 0) message.setVisibility(View.VISIBLE);
                        else message.setVisibility(View.GONE);
                    }
                });
    }

    private void FabLisener() {
        fab.setOnClickListener(v -> new MaterialAlertDialogBuilder(Objects.requireNonNull(getContext()))
                .setTitle(R.string.new_pattern_title)
                .setItems(R.array.menu_patterns, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        byte choce = 1;
                        switch (which) {
                            case 0:
                                choce = 1;//8
                                break;
                            case 1:
                                choce = 4;//9
                                break;
                            case 2:
                                choce = 3;//10
                                break;
                        }
                        Intent intent = new Intent(getActivity(), CreateOrderActivity.class);
                        intent.putExtra("type_id", choce);
                        intent.putExtra("Pattern", true);
                        startActivity(intent);
                    }
                })
                .create()
                .show());
    }

    private void initRecyclerView() {
        PatternAdapter.OnPatternClickListener onClickListener = new PatternAdapter.OnPatternClickListener() {
            @Override
            public void onDeleteOrder(int id, final int position, int status) {
                NetworkService.getInstance()
                        .getJSONApi()
                        .DeleteOrder(App.UserAccessData.getToken(), id)
                        .enqueue(new Callback<StandardAnswer>() {
                            @Override
                            public void onResponse(@NonNull Call<StandardAnswer> call, @NonNull Response<StandardAnswer> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    if (response.body().getStatus() == 200) {
                                        List<Pattern> insertlist = new ArrayList<>(Patterns);
                                        insertlist.remove(position);
                                        PatternAdapter.updateList(insertlist);
                                        Hawk.put("PatternsList", Patterns);
                                        Snackbar.make(Objects.requireNonNull(getView()), R.string.order_deleted, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<StandardAnswer> call, @NonNull Throwable t) {
                                Toast.makeText(getContext(), R.string.server_lost, Toast.LENGTH_SHORT).show();
                            }
                        });
            }

            @Override
            public void onCopyOrder(final Pattern pattern) {
                NetworkService.getInstance()
                        .getJSONApi()
                        .CopyOrder(App.UserAccessData.getToken(), pattern.getId(), (byte) 1)
                        .enqueue(new Callback<AnswerCopyOrder>() {
                            @Override
                            public void onResponse(@NonNull Call<AnswerCopyOrder> call, @NonNull Response<AnswerCopyOrder> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    if (response.body().getStatus() == 200) {
//                                        List<Pattern> insertlist = new ArrayList<>(Patterns);
//                                        insertlist.add(new Orders(response.body().getOrderId(), order.getUser_id(), order.getType_id(), order.getStatus_id(), new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()).format(new Date())));
//                                        PatternAdapter.insertdata(insertlist, true);

                                        PatternAdapter.notifyDataSetChanged();
                                        Hawk.put("PatternsList", Patterns);

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
            public void onEditOrder(final Pattern pattern) {
                final SendOrder OpenOrder = new SendOrder(pattern.getType_id());
                NetworkService.getInstance()
                        .getJSONApi()
                        .getOrderInfo(App.UserAccessData.getToken(), pattern.getId())
                        .enqueue(new Callback<AnswerOrderEdit>() {
                            @Override
                            public void onResponse(@NonNull Call<AnswerOrderEdit> call, @NonNull Response<AnswerOrderEdit> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    if (response.body().getStatus() == 200) {
                                        OpenOrder.setId(pattern.getId());
                                        OpenOrder.setFields(response.body().getOrderFields());
                                        OpenOrder.setProby(response.body().getProby());

                                        Intent intent = new Intent(getActivity(), CreateOrderActivity.class);
                                        intent.putExtra("type_id", pattern.getType_id());
                                        intent.putExtra("isEdit", true);
                                        intent.putExtra("Pattern", true);
                                        if (pattern.getAct_of_selection() != null)
                                            intent.putExtra("ACT", pattern.getAct_of_selection());
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
            }

            @Override
            public void onCreateOrder(final Pattern pattern) {
                final SendOrder OpenOrder = new SendOrder(pattern.getType_id());
                NetworkService.getInstance()
                        .getJSONApi()
                        .getOrderInfo(App.UserAccessData.getToken(), pattern.getId())
                        .enqueue(new Callback<AnswerOrderEdit>() {
                            @Override
                            public void onResponse(@NonNull Call<AnswerOrderEdit> call, @NonNull Response<AnswerOrderEdit> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    if (response.body().getStatus() == 200) {
                                        OpenOrder.setId(pattern.getId());
                                        OpenOrder.setFields(response.body().getOrderFields());
                                        OpenOrder.setProby(response.body().getProby());

                                        Intent intent = new Intent(getActivity(), CreateOrderActivity.class);
                                        intent.putExtra("type_id", pattern.getType_id());
                                        intent.putExtra("CreatePattern", true);
                                        if (pattern.getAct_of_selection() != null)
                                            intent.putExtra("ACT", pattern.getAct_of_selection());
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
            }

        };
        recyclerView.addItemDecoration(new SpacesItemDecoration((byte) 10, (byte) 10));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        PatternAdapter = new PatternAdapter(Patterns, onClickListener, false);
        (PatternAdapter).setMode(Attributes.Mode.Single);
        recyclerView.setAdapter(PatternAdapter);
    }

    private void RecyclerViewEndLisener() {
        final RecyclerView.LayoutManager lm = recyclerView.getLayoutManager();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                if (dy > 0 || dy < 0 && fab.isShown())
//                    fab.hide();

                if (dy > 0 && (CurrentPage + 1) <= TotalPage) //check for scroll down
                {
                    assert lm != null;
                    visibleItemCount = lm.getChildCount();
                    totalItemCount = lm.getItemCount();
                    pastVisiblesItems = ((LinearLayoutManager) lm).findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) == totalItemCount) {
                            loading = false;
                            // List<Pattern> insertlist = ;
                            LoadPatterns(new ArrayList<>(), (byte) 2);
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

}
