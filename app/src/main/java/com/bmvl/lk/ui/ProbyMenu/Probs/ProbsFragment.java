package com.bmvl.lk.ui.ProbyMenu.Probs;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bmvl.lk.R;
import com.bmvl.lk.Rest.Order.ProbyRest;
import com.bmvl.lk.Rest.Order.SamplesRest;
import com.bmvl.lk.data.SpacesItemDecoration;
import com.bmvl.lk.ui.Create_Order.CreateOrderActivity;
import com.bmvl.lk.ui.Create_Order.Field;
import com.bmvl.lk.ui.Create_Order.OrderProbs.ProbAdapter2;
import com.daimajia.swipe.util.Attributes;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ProbsFragment extends Fragment {
    private List<Field> ProbFields = new ArrayList<>(); //Поля пробы
    private List<Field> ResearchFields = new ArrayList<>(); //Поля исследований
    private List<Field> SampleFields = new ArrayList<>(); //Поля Образцов

    public ProbsFragment() {
    }

    public static ProbsFragment newInstance() {
        return new ProbsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View MyView = inflater.inflate(R.layout.fragment_recyclerview, container, false);
        final RecyclerView recyclerView = MyView.findViewById(R.id.List);
        recyclerView.addItemDecoration(new SpacesItemDecoration((byte) 20, (byte) 15));
        final MaterialButton AddProbBtn = MyView.findViewById(R.id.addProb);

        AddProb();

        ProbAdapter2.OnProbClickListener onClickListener = new ProbAdapter2.OnProbClickListener() {
            @Override
            public void onDeletedProb() {
                Toast.makeText(getContext(), "Удаление пробы", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCopyProb() {
                Toast.makeText(getContext(), "Копирование пробы", Toast.LENGTH_SHORT).show();
            }
        };

        switch (CreateOrderActivity.order_id) {
            case 0:
                AddProbFieldsType0();
                final ProbAdapter2 adapter = new ProbAdapter2(getContext(), ProbFields, ResearchFields, onClickListener);
                (adapter).setMode(Attributes.Mode.Single);
                recyclerView.setAdapter(adapter);
                NewProbListener(AddProbBtn, adapter, recyclerView);
                break;
            case 2:
                break;
            case 3:
                AddProbFieldsType2();
                final ProbAdapter2 adapter2 = new ProbAdapter2(getContext(), ProbFields, ResearchFields, SampleFields, onClickListener);
                (adapter2).setMode(Attributes.Mode.Single);
                recyclerView.setAdapter(adapter2);
                NewProbListener(AddProbBtn, adapter2, recyclerView);
                break;
        }

        return MyView;
    }

    private void NewProbListener(final MaterialButton AddProbBtn, final ProbAdapter2 adapter, final RecyclerView recyclerView) {
        AddProbBtn.setVisibility(View.VISIBLE);
        AddProbBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<Short, ProbyRest> insertlist = new HashMap<>();
                short newid = getPositionKey(CreateOrderActivity.order.getProby().size() - 1, CreateOrderActivity.order.getProby());
                insertlist.put((short) (newid + 1), new ProbyRest(newid));
                adapter.insertdata(insertlist);
                CreateOrderActivity.order.getProby().get((short) (newid + 1)).addSample((short) 1, new SamplesRest((short) 0));
                recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
            }
        });
    }

    private Short getPositionKey(int position, Map<Short, ProbyRest> Probs) {
        return new ArrayList<Short>(Probs.keySet()).get(position);
    }

    private void AddProb() {
        CreateOrderActivity.order.addProb((short) 1, new ProbyRest((short) 0));
        CreateOrderActivity.order.getProby().get((short) (1)).addSample((short) 1, new SamplesRest((short) 0));
    } //Создает первую пробу

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
    } //Поля образцов

    private void AddResearchFields() {
        ResearchFields.clear();
        ResearchFields.add(new Field((byte) 1, R.array.documents, 0, "", "Показатель"));
        ResearchFields.add(new Field((byte) 1, R.array.documents, 0, "", "Метод испытаний"));
        ResearchFields.add(new Field((byte) 1, R.array.documents, 0, "", "Тип исследования"));
    } //Поля исследвоаний

    private void AddProbFieldsType0() {
        ProbFields.clear();
        ProbFields.add(new Field((byte) 1, R.array.type_material, 5, "", "Вид материала"));
        ProbFields.add(new Field((byte) 5, R.array.documents, 116, "", "На соответствие требованиям"));
        ProbFields.add(new Field(15, "", "Номер сейф пакета", InputType.TYPE_CLASS_NUMBER));
        ProbFields.add(new Field((byte) 1, R.array.sample_states, 32, "", "Состояние образца"));
        ProbFields.add(new Field((byte) 1, R.array.transport, 60, "", "Транспорт"));
        ProbFields.add(new Field(61, "", "", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field(54, "", "Широта", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field(55, "", "Долгота", InputType.TYPE_CLASS_TEXT));

        ProbFields.add(new Field((byte) 1, R.array.packaging_type, 25, "", "Вид упаковки, наличие маркировки"));

        ProbFields.add(new Field(74, "", "Наименование организации, проводившей отбор проб", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field(75, "", "Адрес организации, проводившей отбор проб", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field(27, "", "Страна отбора", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field(28, "", "Регион отбора", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field(57, "", "Район отбора", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field(21, "", "Место отбора", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field(18, "", "План и метод отбора образца", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field(125, "", "Должность лица,проводившего отбор", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field(126, "", "ФИО лица, проводившего отбор", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field(17, "", "В присутствии", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field(12, "", "Дата и время отбора", InputType.TYPE_CLASS_TEXT));

        ProbFields.add(new Field(6, "", "Наименование образца, термическое состояние", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field(22, "", "Дата выработки", InputType.TYPE_CLASS_NUMBER, Objects.requireNonNull(getActivity()).getDrawable(R.drawable.ic_date_range_black_24dp), true));
        ProbFields.add(new Field(40, "", "Масса/объем образца", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field((byte) 1, R.array.units_of_measure, 41, "", " "));
        ProbFields.add(new Field((byte) 5, R.array.documents, 19, "", "НД на продукцию"));

        ProbFields.add(new Field((byte) 6));

        AddResearchFields();
    } //Поля пробы на исследование пищевых продуктов

    private void AddProbFieldsType2() {
        ProbFields.clear();
        ProbFields.add(new Field((byte) 1, R.array.type_material, 5, "", "Вид материала"));
        ProbFields.add(new Field((byte) 5, R.array.documents, 116, "", "На соответствие требованиям"));
        ProbFields.add(new Field(15, "", "Номер сейф пакета", InputType.TYPE_CLASS_NUMBER));
        ProbFields.add(new Field((byte) 1, R.array.sample_states, 32, "", "Состояние образца"));
        ProbFields.add(new Field((byte) 1, R.array.transport, 60, "", "Транспорт"));
        ProbFields.add(new Field(61, "", "", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field(54, "", "Широта", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field(55, "", "Долгота", InputType.TYPE_CLASS_TEXT));

        ProbFields.add(new Field(131, "", "Вид животного", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field((byte) 1, R.array.packaging_type, 25, "", "Пробы упакованы"));

        ProbFields.add(new Field(74, "", "Наименование организации, проводившей отбор проб", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field(75, "", "Адрес организации, проводившей отбор проб", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field(27, "", "Страна отбора", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field(28, "", "Регион отбора", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field(57, "", "Район отбора", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field(21, "", "Место отбора", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field(18, "", "План и метод отбора образца", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field(125, "", "Должность лица,проводившего отбор", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field(126, "", "ФИО лица, проводившего отбор", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field(17, "", "В присутствии", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field(12, "", "Дата и время отбора", InputType.TYPE_CLASS_TEXT));

        ProbFields.add(new Field((byte) 7));
        AddSampleFields();
    } //Поля пробы сопроводительного письма
}