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
public class ServiceInfoFragment extends Fragment {

    public ServiceInfoFragment() {
        // Required empty public constructor
    }
    public static ServiceInfoFragment newInstance() {
        return new ServiceInfoFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_service_info, container, false);
    }
}
