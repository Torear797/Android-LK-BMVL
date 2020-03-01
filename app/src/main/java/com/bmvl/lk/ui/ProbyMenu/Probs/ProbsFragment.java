package com.bmvl.lk.ui.ProbyMenu.Probs;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bmvl.lk.R;
import com.bmvl.lk.models.Proby;
import com.bmvl.lk.ui.Create_Order.Field;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;


public class ProbsFragment extends Fragment {

    public ProbsFragment() {
        // Required empty public constructor
    }
    private static byte order_id;
    private static List<Field> ProbFields = new ArrayList<>();
    public static List<Proby> ProbList = new ArrayList<>();

    public static ProbsFragment newInstance(byte id) {
        order_id = id;
        return new ProbsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View MyView = inflater.inflate(R.layout.fragment_recyclerview, container, false);
        final RecyclerView recyclerView = MyView.findViewById(R.id.List);
        final MaterialButton AddProbBtn = MyView.findViewById(R.id.addProb);

        AddProb();

        if(order_id == 0) AddOrderFieldsType0();
        else if(order_id == 1) AddOrderFieldsType1();

        final ProbAdapter adapter = new ProbAdapter(getContext(),ProbList, ProbFields);
        recyclerView.setAdapter(adapter);



        if(order_id == 0 || order_id == 1) {
            AddProbBtn.setVisibility(View.VISIBLE);
            AddProbBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   // List<Proby> ProbListOld = ProbList;
                    List<Proby> insertlist = new ArrayList<>();
                    ProbList.add(new Proby(ProbList.size() + 1, "", order_id, 0));

                  //  adapter.notifyItemChanged(ProbList.size());

                    adapter.insertdata(insertlist);
                    recyclerView.smoothScrollToPosition(adapter.getItemCount()-1);
                }
            });
        }
        return MyView;
    }
    public void updateList() {

    }
    private void AddProb(){
        ProbList.clear();
        ProbList.add(new Proby(1,"",order_id,0));
    }
    private void AddOrderFieldsType0(){
        ProbFields.clear();
        ProbFields.add(new Field(1,"","Вид материала", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field(1,"","Вид материала", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field(1,"","Вид материала", InputType.TYPE_CLASS_TEXT));
    }
    private void AddOrderFieldsType1(){
        ProbFields.clear();
        ProbFields.add(new Field(1,"","Вид материала", InputType.TYPE_CLASS_TEXT));
    }
}