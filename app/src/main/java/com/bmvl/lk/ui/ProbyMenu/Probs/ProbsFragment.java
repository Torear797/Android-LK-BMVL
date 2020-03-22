package com.bmvl.lk.ui.ProbyMenu.Probs;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bmvl.lk.R;
import com.bmvl.lk.models.Proby;
import com.bmvl.lk.models.Research;
import com.bmvl.lk.models.Samples;
import com.bmvl.lk.models.SamplesData;
import com.bmvl.lk.models.SamplesResearch;
import com.bmvl.lk.ui.Create_Order.Field;
import com.daimajia.swipe.util.Attributes;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class ProbsFragment extends Fragment {
    private static byte order_id;
    private static List<Proby> ProbList = new ArrayList<>();  //Пробы
    private static List<Field> ProbFields = new ArrayList<>(); //Поля пробы
    private static List<Field> ResearchFields = new ArrayList<>(); //Поля исследований
    private static List<Field> SampleFields = new ArrayList<>(); //Поля Образцов

    private static List<List<Research>> Researchs = new ArrayList<>(); //Исследования.

    private static List<Samples> SamplesList = new ArrayList<>(); //Образцы.
    private static List<List<SamplesResearch>> SamplesResearchs = new ArrayList<>(); //Данные исследований для образцов
    private static List<SamplesData> SamplesData = new ArrayList<>(); //Данные полей образцов.

    public ProbsFragment() {
    }

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

        if (order_id == 0) {
            AddOrderFieldsType0();
            final ProbAdapter adapter = new ProbAdapter(getContext(), ProbList, ProbFields, ResearchFields);
            (adapter).setMode(Attributes.Mode.Single);
            recyclerView.setAdapter(adapter);
            NewProbListener(AddProbBtn, adapter, recyclerView);
        } else if (order_id == 1) {
            AddOrderFieldsType1();
            final ProbAdapter adapter = new ProbAdapter(getContext(), ProbList, ProbFields, ResearchFields, SampleFields);
            (adapter).setMode(Attributes.Mode.Single);
            recyclerView.setAdapter(adapter);
            NewProbListener(AddProbBtn, adapter, recyclerView);
        }

        return MyView;
    }

    public static List<List<Research>> getResearchsList() {
        return Researchs;
    }
    public static List<Research> getResearchsList(int i) {
        return Researchs.get(i);
    }
    public static List<Samples> getSampleList(int i) {
        List<Samples> SamplesForBrobID = new ArrayList<>();
        for (Samples sam : SamplesList){
           if(sam.getProby_id() == i) SamplesForBrobID.add(sam);
        }
        return SamplesForBrobID;
    }
    public static List<Samples> getSampleList() {
        return SamplesList;
    }

   // public stati

    public static List<SamplesResearch> getSampleResearchList(int ProbID) {
        return SamplesResearchs.get(ProbID);
    }
    public static List<SamplesResearch> getSampleResearchList(int i, int ProbId) {
        List<SamplesResearch> SamplesResearchListForID = new ArrayList<>();
        for (SamplesResearch sam : SamplesResearchs.get(ProbId)){
            if(sam.getSample_id() == i) SamplesResearchListForID.add(sam);
        }
        return SamplesResearchListForID;
    }

    private void NewProbListener(final MaterialButton AddProbBtn, final ProbAdapter adapter, final RecyclerView recyclerView) {
        AddProbBtn.setVisibility(View.VISIBLE);
        AddProbBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Proby> insertlist = new ArrayList<>();
              //  ProbList.add(new Proby(ProbList.size() + 1, "", order_id, 0));
                insertlist.add(new Proby(ProbList.size() + 1, "", order_id, 0));
                adapter.insertdata(insertlist);
                recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);

                if(order_id == 0) {
                    List<Research> ResItems = new ArrayList<>();
                    ResItems.add(new Research());
                    Researchs.add(ResItems);
                } else {
                    SamplesList.add(new Samples(ProbList.size()-1, 1));

                    List<SamplesResearch> ResItems = new ArrayList<>();
                    ResItems.add(new SamplesResearch(SamplesResearchs.get(ProbList.size()-2).size()-1, SamplesList.size()-1));
                    SamplesResearchs.add(ResItems);

                    SamplesData.add(new SamplesData(SamplesData.size()-1, SamplesList.size()-1));
                }
            }
        });
    }

    private void AddProb() {
        ProbList.clear();
        ProbList.add(new Proby(1, "", order_id, 0));

        Researchs.clear();

        SamplesList.clear();
        SamplesResearchs.clear();
        SamplesData.clear();

        if(order_id == 0) {
            List<Research> ResItems = new ArrayList<>();
            ResItems.add(new Research());
            Researchs.add(ResItems);
        } else {
            SamplesList.add(new Samples(ProbList.size()-1, 1));
            List<SamplesResearch> ResItems = new ArrayList<>();
            ResItems.add(new SamplesResearch(0, SamplesList.size()-1));
            SamplesResearchs.add(ResItems);
            SamplesData.add(new SamplesData(SamplesData.size()-1, SamplesList.size()-1));
        }
    }

    private void AddSampleFields() {
        SampleFields.clear();
        SampleFields.add(new Field(1, "", "Наименование доставленного биоматериала", InputType.TYPE_CLASS_TEXT));
        SampleFields.add(new Field(1, "", "Инвентарный номер животного, кличка и т.д.", InputType.TYPE_CLASS_TEXT));
        SampleFields.add(new Field(1, "", "Группа", InputType.TYPE_CLASS_TEXT));
        SampleFields.add(new Field(1, "", "Возраст, масть", InputType.TYPE_CLASS_TEXT));
        SampleFields.add(new Field(1, "", "Номер корпуса", InputType.TYPE_CLASS_TEXT));
        SampleFields.add(new Field(1, "", "Вакцинация поголовья", InputType.TYPE_CLASS_TEXT));
        SampleFields.add(new Field((byte) 6, 0, "", ""));
        AddResearchFields();
    }

    private void AddResearchFields() {
        ResearchFields.clear();
        ResearchFields.add(new Field((byte) 1, R.array.documents, 0, "", "Показатель"));
        ResearchFields.add(new Field((byte) 1, R.array.documents, 0, "", "Метод испытаний"));
        ResearchFields.add(new Field((byte) 1, R.array.documents, 0, "", "Тип исследования"));
    }

    private void AddOrderFieldsType0() {
        ProbFields.clear();
        ProbFields.add(new Field((byte) 1, R.array.type_material, 0, "", "Вид материала"));
        ProbFields.add(new Field((byte) 5, R.array.documents, 0, "", "На соответствие требованиям"));
        ProbFields.add(new Field(1, "", "Номер сейф пакета", InputType.TYPE_CLASS_NUMBER));
        ProbFields.add(new Field((byte) 1, R.array.sample_states, 0, "", "Состояние образца"));
        ProbFields.add(new Field((byte) 1, R.array.transport, 0, "", "Транспорт"));
        ProbFields.add(new Field(1, "", "", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field(1, "", "Широта", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field(1, "", "Долгота", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field((byte) 1, R.array.packaging_type, 0, "", "Вид упаковки, наличие маркировки"));
        ProbFields.add(new Field(1, "", "Наименование организации, проводившей отбор проб", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field(1, "", "Адрес организации, проводившей отбор проб", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field(1, "", "Страна отбора", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field(1, "", "Регион отбора", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field(1, "", "Район отбора", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field(1, "", "Место отбора", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field(1, "", "План и метод отбора образца", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field(1, "", "Должность лица,проводившего отбор", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field(1, "", "ФИО лица, проводившего отбор", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field(1, "", "В присутствии", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field(1, "", "Дата и время отбора", InputType.TYPE_CLASS_TEXT));

        ProbFields.add(new Field(1, "", "Наименование образца, термическое состояние", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field(1, "", "Дата выработки", InputType.TYPE_CLASS_NUMBER, Objects.requireNonNull(getActivity()).getDrawable(R.drawable.ic_date_range_black_24dp), true));

        ProbFields.add(new Field(1, "", "Масса/объем образца", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field((byte) 1, R.array.units_of_measure, 0, "", " "));

        ProbFields.add(new Field((byte) 5, R.array.documents, 0, "", "НД на продукцию"));
        ProbFields.add(new Field((byte) 6));

        AddResearchFields();
    }

    private void AddOrderFieldsType1() {
        ProbFields.clear();
        ProbFields.add(new Field((byte) 1, R.array.type_material, 0, "", "Вид материала"));
        ProbFields.add(new Field((byte) 5, R.array.documents, 0, "", "На соответствие требованиям"));
        ProbFields.add(new Field(1, "", "Номер сейф пакета", InputType.TYPE_CLASS_NUMBER));
        ProbFields.add(new Field((byte) 1, R.array.sample_states, 0, "", "Состояние образца"));
        ProbFields.add(new Field((byte) 1, R.array.transport, 0, "", "Транспорт"));
        ProbFields.add(new Field(1, "", "", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field(1, "", "Широта", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field(1, "", "Долгота", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field(1, "", "Вид животного", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field((byte) 1, R.array.packaging_type, 0, "", "Пробы упакованы"));
        ProbFields.add(new Field(1, "", "Наименование организации, проводившей отбор проб", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field(1, "", "Адрес организации, проводившей отбор проб", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field(1, "", "Страна отбора", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field(1, "", "Регион отбора", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field(1, "", "Район отбора", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field(1, "", "Место отбора", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field(1, "", "План и метод отбора образца", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field(1, "", "Должность лица,проводившего отбор", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field(1, "", "ФИО лица, проводившего отбор", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field(1, "", "В присутствии", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field(1, "", "Дата и время отбора", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field((byte) 7));
        AddSampleFields();
    }
}