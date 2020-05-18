package com.bmvl.lk.ui.ProbyMenu;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.bmvl.lk.R;
import com.bmvl.lk.data.models.Proby;
import com.bmvl.lk.ui.create_order.Field;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        // return inflater.inflate(R.layout.fragment_service_info, container, false);
     //   View MyView = inflater.inflate(R.layout.fragment_service_info, container, false);

//        final RecyclerView recyclerView = MyView.findViewById(R.id.List);
//        AddProb();
//
//        AddOrderFieldsType0();
//        final ProbAdapter adapter = new ProbAdapter(getContext(), ProbList, ProbFields, ResearchFields);
//        recyclerView.setAdapter(adapter);
        //NewProbListener(AddProbBtn, adapter, recyclerView);

        return inflater.inflate(R.layout.fragment_service_info, container, false);
    }
}