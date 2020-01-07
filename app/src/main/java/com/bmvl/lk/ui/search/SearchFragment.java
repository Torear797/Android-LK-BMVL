package com.bmvl.lk.ui.search;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.bmvl.lk.R;

import static com.bmvl.lk.MenuActivity.MyActionBar;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {


    public SearchFragment() {
    }

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View MyView = inflater.inflate(R.layout.fragment_search, container, false);
        MyActionBar.setTitle(R.string.search);
        return MyView;
    }

}
