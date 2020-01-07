package com.bmvl.lk.ui.order;


import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.bmvl.lk.R;

import java.util.Objects;

import static com.bmvl.lk.MenuActivity.MyActionBar;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderFragment extends Fragment {


    public OrderFragment() {
    }

    public static OrderFragment newInstance() {
        return new OrderFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View MyView = inflater.inflate(R.layout.fragment_order, container, false);
//        Toast toast = Toast.makeText(getContext(),
//                "Страница: " + MenuActivity.CurrentPage, Toast.LENGTH_SHORT);
//        toast.show();
//        ActionBar MyActionBar = ((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar();
//        assert MyActionBar != null;
//        MyActionBar.setTitle("wef");

        MyActionBar.setTitle(R.string.order);
        return MyView;
    }

}
