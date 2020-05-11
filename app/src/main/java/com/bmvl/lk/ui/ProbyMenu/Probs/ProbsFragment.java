package com.bmvl.lk.ui.ProbyMenu.Probs;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bmvl.lk.R;
import com.bmvl.lk.Rest.Order.ProbyRest;
import com.bmvl.lk.Rest.Order.SamplesRest;
import com.bmvl.lk.data.SpacesItemDecoration;
import com.bmvl.lk.ui.Create_Order.CreateOrderActivity;
import com.bmvl.lk.ui.Create_Order.Field;
import com.daimajia.swipe.util.Attributes;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class ProbsFragment extends Fragment {
    private List<Field> ProbFields = new ArrayList<>(); //Поля пробы
    private List<Field> SampleFields = new ArrayList<>(); //Поля Образцов
    private ProbAdapter adapter;

    public ProbsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (CreateOrderActivity.order.getProby().size() == 0)
            AddProb();

        ProbAdapter.OnProbClickListener onClickListener = new ProbAdapter.OnProbClickListener() {
            @Override
            public void onDeletedProb(short id) {
                adapter.closeAllItems();
                TreeMap<Short, ProbyRest> insertlist = new TreeMap<>(CreateOrderActivity.order.getProby());
                insertlist.remove(id);
                adapter.updateList(insertlist);

                if (CreateOrderActivity.order_id == 4) {
                    CreateOrderActivity.order.getFields().put((short) 7, String.valueOf(CreateOrderActivity.order.getProby().size()));
                    CreateOrderActivity.adapter.notifyItemChanged(18);
                }
            }

            @Override
            public void onCopyProb() {
                Toast.makeText(getContext(), "Копирование пробы", Toast.LENGTH_SHORT).show();
            }
        };
        ProbFields.clear();
        SampleFields.clear();

        switch (CreateOrderActivity.order_id) {
            case 1:
                AddProbFieldsType1();
                break;
            case 3:
                AddProbFieldsType3();
                break;
            case 4:
                AddProbFieldsType4();
                break;
            case 8:
                AddPatternFieldsType1();
                break;
            case 9:
                AddPatternFieldsType4();
                break;
            case 10:
                AddPatternFieldsType3();
                break;
        }
        adapter = new ProbAdapter(getContext(), ProbFields, SampleFields, onClickListener);
        (adapter).setMode(Attributes.Mode.Single);
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

        recyclerView.setAdapter(adapter);
        NewProbListener(AddProbBtn, adapter, recyclerView);

        return MyView;
    }

    private void NewProbListener(final MaterialButton AddProbBtn, final ProbAdapter adapter, final RecyclerView recyclerView) {
        AddProbBtn.setVisibility(View.VISIBLE);
        AddProbBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<Short, ProbyRest> insertlist = new HashMap<>();
                final short newid = getPositionKey(CreateOrderActivity.order.getProby().size() - 1, CreateOrderActivity.order.getProby());
                insertlist.put((short) (newid + 1), new ProbyRest(newid));
                adapter.insertdata(insertlist);
                CreateOrderActivity.order.getProby().get((short) (newid + 1)).addSample((short) 1, new SamplesRest((short) 0));

                if (CreateOrderActivity.order_id == 4) {
                    CreateOrderActivity.order.getFields().put((short) 7, String.valueOf(CreateOrderActivity.order.getProby().size()));
                    CreateOrderActivity.adapter.notifyItemChanged(18);
                }

                recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
            }
        });
    }

    private Short getPositionKey(int position, Map<Short, ProbyRest> Probs) {
        if (Probs.size() > 0)
            return new ArrayList<Short>(Probs.keySet()).get(position);
        else return 0;
    }

    private void AddProb() {
        CreateOrderActivity.order.addProb((short) 1, new ProbyRest((short) 0));
        CreateOrderActivity.order.getProby().get((short) (1)).addSample((short) 1, new SamplesRest((short) 0));
    } //Создает первую пробу

    private void AddSamplesForType4() {
        SampleFields.add(new Field(6, "", "Наименование доставленного биоматериала", InputType.TYPE_CLASS_TEXT));
        SampleFields.add(new Field(112, "", "Инвентарный номер животного, кличка и т.д.", InputType.TYPE_CLASS_TEXT));
        SampleFields.add(new Field(113, "", "Группа", InputType.TYPE_CLASS_TEXT));
        SampleFields.add(new Field(114, "", "Возраст, масть", InputType.TYPE_CLASS_TEXT));
        SampleFields.add(new Field(115, "", "Номер корпуса", InputType.TYPE_CLASS_TEXT));
        SampleFields.add(new Field(102, "", "Вакцинация поголовья", InputType.TYPE_CLASS_TEXT));
        SampleFields.add(new Field((byte) 6, 0, "", ""));
    } //Поля образцов

    private void AddSamplesForType1() {
        SampleFields.add(new Field(6, "", "Наименование образца, термическое состояние", InputType.TYPE_CLASS_TEXT));
        SampleFields.add(new Field(22, "", "Дата выработки", InputType.TYPE_CLASS_NUMBER, Objects.requireNonNull(getActivity()).getDrawable(R.drawable.ic_date_range_black_24dp), true));
        SampleFields.add(new Field(40, "", "Масса/объем образца", InputType.TYPE_CLASS_TEXT));
        SampleFields.add(new Field((byte) 1, R.array.units_of_measure, 41, "", " "));
        SampleFields.add(new Field((byte) 5, R.array.documents, 19, "", "НД на продукцию"));
        SampleFields.add(new Field((byte) 6, 0, "", ""));
    }

    private void AddProbFieldsType1() {
        AddStandartFieldPart1();
        ProbFields.add(new Field((byte) 1, R.array.packaging_type, 25, "", "Вид упаковки, наличие маркировки"));
        AddStandartFieldPart2();
        AddSamplesForType1();
        ProbFields.add(new Field((byte) 7));
    } //Поля пробы на исследование пищевых продуктов

    private void AddProbFieldsType3() {
        AddStandartFieldPart1();
        ProbFields.add(new Field((byte) 1, R.array.packaging_type, 25, "", "Пробы упакованы"));
        AddStandartFieldPart2();
        ProbFields.add(new Field(143, "", "Кадастровый номер участка", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field(144, "", "Глубина отбора", InputType.TYPE_NULL));
        ProbFields.add(new Field(145, "", "Площадь с которой отобрано", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field((byte) 1, R.array.units_of_measure, 146, "", " "));
        ProbFields.add(new Field(68, "", "Особые условия доставки проб", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field(69, "", "Отклонения проб от нормального состояния", InputType.TYPE_CLASS_TEXT));
        AddSamplesForType3();
        ProbFields.add(new Field((byte) 7));
    } //Поля пробы Заявка на исследование семян, почв, удобрений

    private void AddProbFieldsType4() {
        AddStandartFieldPart1();
        ProbFields.add(new Field(131, "", "Вид животного", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field((byte) 1, R.array.packaging_type, 25, "", "Пробы упакованы"));
        AddStandartFieldPart2();
        ProbFields.add(new Field((byte) 7));
        AddSamplesForType4();
    } //Поля пробы сопроводительного письма

    private void AddPatternFieldsType1() {
        AddStandartFieldPart1();
        ProbFields.add(new Field((byte) 1, R.array.packaging_type, 25, "", "Вид упаковки, наличие маркировки"));
        AddStandartFieldPart3();
        AddSamplesForPatternType1();
        ProbFields.add(new Field((byte) 7));
    }

    private void AddPatternFieldsType3() {
        AddStandartFieldPart1();
        ProbFields.add(new Field((byte) 1, R.array.packaging_type, 25, "", "Пробы упакованы"));
        AddStandartFieldPart3();
        ProbFields.add(new Field(143, "", "Кадастровый номер участка", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field(144, "", "Глубина отбора", InputType.TYPE_NULL));
        ProbFields.add(new Field(145, "", "Площадь с которой отобрано", InputType.TYPE_CLASS_TEXT));
        AddSamplesForType3();
        ProbFields.add(new Field((byte) 7));
    }

    private void AddPatternFieldsType4() {
        AddStandartFieldPart1();
        ProbFields.add(new Field(131, "", "Вид животного", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field((byte) 1, R.array.packaging_type, 25, "", "Пробы упакованы"));
        AddStandartFieldPart3();
        AddSamplesForPatternType4();
        ProbFields.add(new Field((byte) 7));
    }

    private void AddStandartFieldPart1() {
        ProbFields.add(new Field((byte) 1, R.array.type_material, 5, "", "Вид материала"));
        ProbFields.add(new Field((byte) 5, R.array.documents, 116, "", "На соответствие требованиям"));
        ProbFields.add(new Field(15, "", "Номер сейф пакета", InputType.TYPE_CLASS_NUMBER));
        ProbFields.add(new Field((byte) 1, R.array.sample_states, 32, "", "Состояние образца"));
        ProbFields.add(new Field((byte) 1, R.array.transport, 60, "", "Транспорт"));
        ProbFields.add(new Field(61, "", "", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field(54, "", "Широта", InputType.TYPE_CLASS_TEXT));
        ProbFields.add(new Field(55, "", "Долгота", InputType.TYPE_CLASS_TEXT));
    }

    private void AddStandartFieldPart2() {
        AddStandartFieldPart3();
        ProbFields.add(new Field(12, "", "Дата и время отбора", InputType.TYPE_CLASS_TEXT));
    }

    private void AddStandartFieldPart3() {
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
    }

    private void AddSamplesForType3() {
        SampleFields.add(new Field(6, "", "Наименование доставленного материала", InputType.TYPE_CLASS_TEXT));
        SampleFields.add(new Field(112, "", "Инвентарный номер, описание", InputType.TYPE_CLASS_TEXT));
        SampleFields.add(new Field(144, "", "Глубина отбора", InputType.TYPE_CLASS_TEXT));
        SampleFields.add(new Field((byte) 6, 0, "", ""));
    }

    private void AddSamplesForPatternType1() {
        SampleFields.add(new Field(6, "", "Наименование образца, термическое состояние", InputType.TYPE_CLASS_TEXT));
        SampleFields.add(new Field((byte) 5, R.array.documents, 19, "", "НД на продукцию"));
        SampleFields.add(new Field((byte) 6, 0, "", ""));
    }

    private void AddSamplesForPatternType4() {
        SampleFields.add(new Field(6, "", "Наименование доставленного биоматериала", InputType.TYPE_CLASS_TEXT));
        SampleFields.add(new Field(102, "", "Вакцинация поголовья", InputType.TYPE_CLASS_TEXT));
        SampleFields.add(new Field((byte) 6, 0, "", ""));
    }
}