package com.bmvl.lk.ui.people;


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
public class PeopleFragment extends Fragment {


    public PeopleFragment() {
    }

    public static PeopleFragment newInstance() {
        return new PeopleFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View MyView = inflater.inflate(R.layout.fragment_people, container, false);
                MyActionBar.setTitle(R.string.people);
        return MyView;
    }

}
