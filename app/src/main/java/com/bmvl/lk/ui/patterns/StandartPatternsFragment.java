package com.bmvl.lk.ui.patterns;

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
import com.daimajia.swipe.util.Attributes;
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

public class StandartPatternsFragment extends Fragment implements OnBackPressedListener {

    public static StandartPatternsFragment newInstance() {
        return new StandartPatternsFragment();
    }

    @Override
    public void onBackPressed() {
    }

    private static List<Pattern> StandartPatterns = new ArrayList<>();
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView message;
    private PatternAdapter PatternAdapter;

    //Для подгрузки данных
    private boolean loading = true;
    private int pastVisiblesItems;
    private int visibleItemCount;
    private int totalItemCount;
    private static byte TotalPage;
    private static byte CurrentPage = 0;

    @Override
    public void onResume() {
        super.onResume();
        if (App.isOnline(Objects.requireNonNull(getContext()))) {
            if (StandartPatterns.size() == 0) LoadPatterns(StandartPatterns, (byte) 0);
            else UpdatePatterns();
        }
    }

    private void UpdatePatterns() {
        if(PatternAdapter != null)
        PatternAdapter.notifyDataSetChanged();
        List<Pattern> insertlist = new ArrayList<>();
        CurrentPage = 0;
        LoadPatterns(insertlist, (byte) 1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View MyView = inflater.inflate(R.layout.fragment_standart_patterns, container, false);
        if (Hawk.contains("StandardPatternsList"))
            StandartPatterns = Hawk.get("StandardPatternsList");
        recyclerView = MyView.findViewById(R.id.RecyclerView);
        swipeRefreshLayout = MyView.findViewById(R.id.SwipeRefreshLayout);
        message = MyView.findViewById(R.id.empty_msg);

        swipeRefreshLayout.setOnRefreshListener(MyRefresh);

        if (StandartPatterns.size() == 0) message.setVisibility(View.VISIBLE);
        else message.setVisibility(View.GONE);

        initRecyclerView();
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
                                        List<Pattern> insertlist = new ArrayList<>(StandartPatterns);
                                        insertlist.remove(position);
                                        PatternAdapter.updateList(insertlist);
                                        Hawk.put("StandardPatternsList", StandartPatterns);
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
                        .CopyOrder(App.UserAccessData.getToken(), pattern.getId(), (byte)1)
                        .enqueue(new Callback<AnswerCopyOrder>() {
                            @Override
                            public void onResponse(@NonNull Call<AnswerCopyOrder> call, @NonNull Response<AnswerCopyOrder> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    if (response.body().getStatus() == 200) {
                                        List<Pattern> insertlist = new ArrayList<>();
                                        insertlist.add(new Pattern(response.body().getOrderId(), pattern.getUser_id(), pattern.getType_id(), pattern.getStatus_id(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()),pattern.getPatternName()));
                                        PatternAdapter.insertdata(insertlist, true);
                                        Hawk.put("StandardPatternsList", StandartPatterns);
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

        PatternAdapter = new PatternAdapter(getContext(), StandartPatterns, onClickListener);
        (PatternAdapter).setMode(Attributes.Mode.Single);
        recyclerView.setAdapter(PatternAdapter);
    }

    private void LoadPatterns(final List<Pattern> NewList, final byte Type) {
        NetworkService.getInstance()
                .getJSONApi()
                .getPatterns(App.UserAccessData.getToken(), (byte) 1)
                .enqueue(new Callback<AnswerPatterns>() {
                    @Override
                    public void onResponse(@NonNull Call<AnswerPatterns> call, @NonNull Response<AnswerPatterns> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {
                                TotalPage = response.body().getOrders().getTotal_pages();
                                CurrentPage = response.body().getOrders().getCurrent();
                                PatternAdapter.notifyDataSetChanged();
                                NewList.addAll(response.body().getOrders().getPatterns());
                                Hawk.put("StandardPatternsList", NewList);
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
                                        StandartPatterns.addAll(NewList);
                                        PatternAdapter.notifyDataSetChanged();
                                        loading = true;
                                        break;
                                }
                            }
                        }else swipeRefreshLayout.setRefreshing(false);
                        if (StandartPatterns.size() == 0) message.setVisibility(View.VISIBLE);
                        else message.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(@NonNull Call<AnswerPatterns> call, @NonNull Throwable t) {
                        swipeRefreshLayout.setRefreshing(false);
                        if (StandartPatterns.size() == 0) message.setVisibility(View.VISIBLE);
                        else message.setVisibility(View.GONE);
                    }
                });
    }

    private void RecyclerViewEndLisener() {
        final RecyclerView.LayoutManager lm = recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 && (CurrentPage + 1) <= TotalPage) //check for scroll down
                {
                    assert lm != null;
                    visibleItemCount = lm.getChildCount();
                    totalItemCount = lm.getItemCount();
                    pastVisiblesItems = ((LinearLayoutManager) lm).findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) == totalItemCount) {
                            loading = false;
                            List<Pattern> insertlist = new ArrayList<>();
                            LoadPatterns(insertlist, (byte) 2);
                        }
                    }
                }
            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }
}