package com.bmvl.lk.ui.Notification;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bmvl.lk.App;
import com.bmvl.lk.R;
import com.bmvl.lk.Rest.NetworkService;
import com.bmvl.lk.Rest.Notify.NotificationsAnswer;
import com.bmvl.lk.Rest.StandardAnswer;
import com.bmvl.lk.data.OnBackPressedListener;
import com.bmvl.lk.data.SpacesItemDecoration;
import com.bmvl.lk.data.models.Notifications;
import com.daimajia.swipe.util.Attributes;
import com.google.android.material.snackbar.Snackbar;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NoticeFragment extends Fragment implements OnBackPressedListener {

    private static List<Notifications> Notifi = new ArrayList<>();
    private NotifiSwipeAdapter NotifiAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private boolean loading = true;

    private int pastVisiblesItems;
    private int visibleItemCount;
    private int totalItemCount;

    private static byte TotalPage = 1;
    private static byte CurrentPage = 0;

    private RecyclerView recyclerView;
    private TextView Message;

    public NoticeFragment() {
    }

    public static NoticeFragment newInstance() {
        return new NoticeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View MyView = inflater.inflate(R.layout.fragment_notice, container, false);
        if (Hawk.contains("NotifyList")) Notifi = Hawk.get("NotifyList");

        recyclerView = MyView.findViewById(R.id.Notifi_list);
        swipeRefreshLayout = MyView.findViewById(R.id.SwipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(MyRefresh);
        Message = MyView.findViewById(R.id.msg);

        if (Notifi.size() == 0) Message.setVisibility(View.VISIBLE);
        else Message.setVisibility(View.GONE);

        initRecyclerView();

        RecyclerViewEndLisener();
        return MyView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Notifi.size() == 0) InsertNotifications(Notifi, (byte) 0);
        else UpdateNotify();
    }

    public void initRecyclerView() {
        NotifiSwipeAdapter.OnNotifyClickListener onClickListener = new NotifiSwipeAdapter.OnNotifyClickListener() {
            @Override
            public void onNotifyClick(final int pos, final int id) {
                NotifiAdapter.closeAllItems();
                NotifiAdapter.notifyItemChanged(pos);
                NetworkService.getInstance()
                        .getJSONApi()
                        .HideNotify(App.UserAccessData.getToken(), id)
                        .enqueue(new Callback<StandardAnswer>() {
                            @Override
                            public void onResponse(@NonNull Call<StandardAnswer> call, @NonNull Response<StandardAnswer> response) {
                                if (response.isSuccessful() && response.body() != null) {

                                    if (response.body().getStatus() == 200)
                                        Snackbar.make(Objects.requireNonNull(getView()), "Уведомление прочтено!", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<StandardAnswer> call, @NonNull Throwable t) {
                            }
                        });
            }
        };

        recyclerView.addItemDecoration(new SpacesItemDecoration((byte) 10, (byte) 10));
        recyclerView.setHasFixedSize(true);
        NotifiAdapter = new NotifiSwipeAdapter(getContext(), Notifi, onClickListener);
        (NotifiAdapter).setMode(Attributes.Mode.Single);
        recyclerView.setAdapter(NotifiAdapter);
    }

    private void UpdateNotify() {
        List<Notifications> insertlist = new ArrayList<>();
        CurrentPage = 0;
        InsertNotifications(insertlist, (byte) 1);
    }

    private SwipeRefreshLayout.OnRefreshListener MyRefresh = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            swipeRefreshLayout.setRefreshing(true);
            List<Notifications> insertlist = new ArrayList<>();
            CurrentPage = 0;
            InsertNotifications(insertlist, (byte) 1);
        }
    };

    private void InsertNotifications(final List<Notifications> NewList, final byte Type) {
        NetworkService.getInstance()
                .getJSONApi()
                .getNotificationsOnPage(App.UserAccessData.getToken(), (byte) (CurrentPage + 1))
                .enqueue(new Callback<NotificationsAnswer>() {
                    @Override
                    public void onResponse(@NonNull Call<NotificationsAnswer> call, @NonNull Response<NotificationsAnswer> response) {
                        if (response.isSuccessful()) {
                            TotalPage = response.body().getNotifications().getTotal_pages();
                            CurrentPage = response.body().getNotifications().getCurrent();
                            NewList.addAll(response.body().getNotifications().getNotifications());
                            Hawk.put("NotifyList", NewList);
                            switch (Type) {
                                case 0:
                                    NotifiAdapter.notifyDataSetChanged();
                                    break;
                                case 1:
                                    NotifiAdapter.updateList(NewList);
                                    swipeRefreshLayout.setRefreshing(false);
                                    break;
                                case 2:
                                    // NotifiAdapter.insertdata(NewList);
                                    //recyclerView.smoothScrollToPosition((CurrentPage - 1) * 10 - 1);
                                    Notifi.addAll(NewList);
                                    NotifiAdapter.notifyDataSetChanged();
                                    loading = true;
                                    break;
                            }
                        }
                        if (Notifi.size() == 0) Message.setVisibility(View.VISIBLE);
                        else Message.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(@NonNull Call<NotificationsAnswer> call, @NonNull Throwable t) {
                        swipeRefreshLayout.setRefreshing(false);
                        if (Notifi.size() == 0) Message.setVisibility(View.VISIBLE);
                        else Message.setVisibility(View.GONE);
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
                            List<Notifications> insertlist = new ArrayList<>();
                            InsertNotifications(insertlist, (byte) 2);
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
    }
}