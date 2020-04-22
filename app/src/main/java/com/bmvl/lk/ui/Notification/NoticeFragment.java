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
import com.bmvl.lk.OnBackPressedListener;
import com.bmvl.lk.R;
import com.bmvl.lk.Rest.NetworkService;
import com.bmvl.lk.Rest.NotificationsAnswer;
import com.bmvl.lk.data.SpacesItemDecoration;
import com.bmvl.lk.models.Notifications;
import com.daimajia.swipe.util.Attributes;

import java.util.ArrayList;
import java.util.List;

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

    private static byte TotalPage;
    private static byte CurrentPage = 0;

    private RecyclerView recyclerView;


    public NoticeFragment() {
        // Required empty public constructor
    }

    public static NoticeFragment newInstance() {
        return new NoticeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View MyView = inflater.inflate(R.layout.fragment_notice, container, false);

        recyclerView = MyView.findViewById(R.id.Notifi_list);
        recyclerView.addItemDecoration(new SpacesItemDecoration(10,10));
        final TextView Message = MyView.findViewById(R.id.msg);
        swipeRefreshLayout = MyView.findViewById(R.id.SwipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(MyRefresh);


        if (Notifi.size() == 0) InsertNotifications(Notifi, (byte) 0);

        recyclerView.setHasFixedSize(true);
        NotifiAdapter = new NotifiSwipeAdapter(getContext(), Notifi, Message);
        (NotifiAdapter).setMode(Attributes.Mode.Single);
        recyclerView.setAdapter(NotifiAdapter);


//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                swipeRefreshLayout.setRefreshing(true);
//                List<Notifications> insertlist = new ArrayList<>();
//                getNotifications(insertlist, false);
//            }
//        }, 60000);

        RecyclerViewEndLisener();
        return MyView;
    }

    private SwipeRefreshLayout.OnRefreshListener MyRefresh = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            swipeRefreshLayout.setRefreshing(true);
            List<Notifications> insertlist = new ArrayList<>();
            CurrentPage = 0;
            // LoadNotifications(insertlist, false);
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

                            switch (Type) {
                                case 0:
                                    NotifiAdapter.notifyDataSetChanged();
                                    break;
                                case 1:
                                    NotifiAdapter.updateList(NewList);
                                    swipeRefreshLayout.setRefreshing(false);
                                    break;
                                case 2:
                                    NotifiAdapter.insertdata(NewList);
                                    recyclerView.smoothScrollToPosition((CurrentPage - 1) * 10 - 1);
                                    loading = true;
                                    break;
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<NotificationsAnswer> call, @NonNull Throwable t) {
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