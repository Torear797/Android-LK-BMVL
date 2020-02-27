package com.bmvl.lk.ui.ProbyMenu;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bmvl.lk.R;


public class PartyInfoFragment extends Fragment {

    public PartyInfoFragment() {
        // Required empty public constructor
    }

    public static PartyInfoFragment newInstance() {
        return new PartyInfoFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_party_info, container, false);
    }
}
