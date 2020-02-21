package com.bmvl.lk.ui.Notification;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bmvl.lk.OnBackPressedListener;
import com.bmvl.lk.R;
import com.bmvl.lk.models.Notifications;
import com.daimajia.swipe.util.Attributes;

import java.util.ArrayList;
import java.util.List;



public class NoticeFragment extends Fragment implements OnBackPressedListener {

    private List<Notifications> Notifi = new ArrayList<>();

    public NoticeFragment() {
        // Required empty public constructor
    }

    public static NoticeFragment newInstance() {
        return new NoticeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View MyView = inflater.inflate(R.layout.fragment_notice, container, false);
        //MyActionBar.setTitle(R.string.Notice);


        final RecyclerView recyclerView = MyView.findViewById(R.id.Notifi_list);
        setTestData();

        recyclerView.setHasFixedSize(true);
        NotifiSwipeAdapter NotifiAdapter = new NotifiSwipeAdapter(getContext(), Notifi);
        (NotifiAdapter).setMode(Attributes.Mode.Single);
        recyclerView.setAdapter(NotifiAdapter);

        return MyView;
    }

    private void setTestData(){
        Notifi.add((new Notifications(1,1,"2019-12-13",1,1,"Заявка создана")));
        Notifi.add((new Notifications(2,1,"2019-12-14",2,1,"Заявка создана")));
        Notifi.add((new Notifications(3,1,"2019-12-15",3,0,"Удалена заявка")));
        Notifi.add((new Notifications(4,1,"2019-12-16",4,0,"Удалена заявка")));
        Notifi.add((new Notifications(5,1,"2019-12-17",5,0,"Заявка создана")));
        Notifi.add((new Notifications(6,1,"2019-12-18",4,0,"Удалена заявка")));
        Notifi.add((new Notifications(7,1,"2019-12-19",5,0,"Заявка создана")));
    }
    @Override
    public void onBackPressed() {

    }
}
