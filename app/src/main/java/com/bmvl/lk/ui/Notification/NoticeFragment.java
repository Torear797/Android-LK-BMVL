package com.bmvl.lk.ui.Notification;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bmvl.lk.OnBackPressedListener;
import com.bmvl.lk.R;

import static com.bmvl.lk.MenuActivity.MyActionBar;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoticeFragment extends Fragment implements OnBackPressedListener {


    public NoticeFragment() {
        // Required empty public constructor
    }

    public static NoticeFragment newInstance() {
        return new NoticeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View MyView = inflater.inflate(R.layout.fragment_notice, container, false);
        MyActionBar.setTitle(R.string.Notice);
        return MyView;
    }

    @Override
    public void onBackPressed() {

    }
}
