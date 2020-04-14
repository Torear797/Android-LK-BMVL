package com.bmvl.lk.ui.Notification;


import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bmvl.lk.App;
import com.bmvl.lk.OnBackPressedListener;
import com.bmvl.lk.R;
import com.bmvl.lk.Rest.NetworkService;
import com.bmvl.lk.Rest.NotificationsAnswer;
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
    private  SwipeRefreshLayout swipeRefreshLayout;

    public NoticeFragment() {
        // Required empty public constructor
    }

    public static NoticeFragment newInstance() {
        return new NoticeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View MyView = inflater.inflate(R.layout.fragment_notice, container, false);

        final RecyclerView recyclerView = MyView.findViewById(R.id.Notifi_list);
        final TextView Message = MyView.findViewById(R.id.msg);
        swipeRefreshLayout = MyView.findViewById(R.id.SwipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(MyRefresh);


        if(Notifi.size() == 0) getNotifications(Notifi, true);

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

        return MyView;
    }
    private SwipeRefreshLayout.OnRefreshListener MyRefresh = new SwipeRefreshLayout.OnRefreshListener() {

        @Override
        public void onRefresh() {
            swipeRefreshLayout.setRefreshing(true);
            List<Notifications> insertlist = new ArrayList<>();
            getNotifications(insertlist, false);
        }
    };


    private void getNotifications(final List<Notifications> NewList, final boolean firstUpdate) {
    //    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    //    StrictMode.setThreadPolicy(policy);
//        try {
//            Response<NotificationsAnswer> response =  NetworkService.getInstance()
//                    .getJSONApi()
//                    .getNotifications(App.UserAccessData.getToken())
//                    .execute();
//            test = String.valueOf(response.body().getTotal_pages());
//            Notifi.addAll(response.body().getNotifi());
//
//        } catch (Exception ignored){
//
//           // Notifi.add((new Notifications(1, 1, "2019-12-13", 1, 1, "Тест")));
//        }

        NetworkService.getInstance()
                .getJSONApi()
                .getNotifications(App.UserAccessData.getToken())
                .enqueue(new Callback<NotificationsAnswer>() {
                    @Override
                    public void onResponse(@NonNull Call<NotificationsAnswer> call, @NonNull Response<NotificationsAnswer> response) {
                        if (response.isSuccessful()) {
                            NewList.addAll(response.body().getNotifications().getNotifications());
                            if(firstUpdate)
                            NotifiAdapter.notifyDataSetChanged();
                            else {
                                NotifiAdapter.updateList(NewList);
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<NotificationsAnswer> call, @NonNull Throwable t) {
                    }
                });
    }

    @Override
    public void onBackPressed() {

    }
}
