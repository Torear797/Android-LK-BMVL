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
import java.util.Objects;


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
        ProbFields.add(new Field((byte)1,R.array.type_material,0,"","Вид материала"));
        ProbFields.add(new Field((byte)5,R.array.documents,0,"","На соответствие требованиям"));
        ProbFields.add(new Field(1,"","Номер сейф пакета", InputType.TYPE_CLASS_NUMBER));
        ProbFields.add(new Field((byte)1,R.array.sample_states,0,"","Состояние образца"));
        ProbFields.add(new Field((byte)1,R.array.transport,0,"","Транспорт"));
        ProbFields.add(new Field(1,"","", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field(1,"","Широта", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field(1,"","Долгота", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field((byte)1,R.array.packaging_type,0,"","Вид упаковки, наличие маркировки"));
        ProbFields.add(new Field(1,"","Наименование организации, проводившей отбор проб", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field(1,"","Адрес организации, проводившей отбор проб", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field(1,"","Страна отбора", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field(1,"","Регион отбора", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field(1,"","Район отбора", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field(1,"","Место отбора", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field(1,"","План и метод отбора образца", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field(1,"","Должность лица,проводившего отбор", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field(1,"","ФИО лица, проводившего отбор", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field(1,"","В присутствии", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field(1,"","Дата и время отбора", InputType.TYPE_CLASS_TEXT));

        ProbFields.add(new Field(1,"","Наименование образца, термическое состояние", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field(1,"","Дата выработки", InputType.TYPE_CLASS_NUMBER, Objects.requireNonNull(getActivity()).getDrawable(R.drawable.ic_date_range_black_24dp),true));

        ProbFields.add(new Field(1, "", "Масса/объем образца", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field((byte) 1, R.array.units_of_measure, 0, "", " "));

        ProbFields.add(new Field((byte)5,R.array.documents,0,"","НД на продукцию"));
    }
    private void AddOrderFieldsType1(){
        ProbFields.clear();
        ProbFields.add(new Field(1,"","Вид материала", InputType.TYPE_CLASS_TEXT));
    }
}