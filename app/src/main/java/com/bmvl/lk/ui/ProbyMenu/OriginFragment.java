package com.bmvl.lk.ui.ProbyMenu;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bmvl.lk.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class OriginFragment extends Fragment {

    public OriginFragment() {
        // Required empty public constructor
    }
    public static OriginFragment newInstance() {
        return new OriginFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_origin, container, false);
    }
}
