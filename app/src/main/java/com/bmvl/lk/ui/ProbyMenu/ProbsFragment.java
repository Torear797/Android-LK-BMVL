package com.bmvl.lk.ui.ProbyMenu;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bmvl.lk.R;


public class ProbsFragment extends Fragment {

    public ProbsFragment() {
        // Required empty public constructor
    }

    public static ProbsFragment newInstance() {
        return new ProbsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View MyView = inflater.inflate(R.layout.fragment_probs, container, false);
        //final RecyclerView recyclerView = MyView.findViewById(R.id.PartyInfoList);
      //  recyclerView.setHasFixedSize(true);
        return MyView;
    }
}