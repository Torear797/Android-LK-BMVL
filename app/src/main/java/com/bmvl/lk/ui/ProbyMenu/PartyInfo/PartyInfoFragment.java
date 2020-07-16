package com.bmvl.lk.ui.ProbyMenu.PartyInfo;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bmvl.lk.App;
import com.bmvl.lk.R;
import com.bmvl.lk.data.Field;
import com.bmvl.lk.data.SpacesItemDecoration;
import com.bmvl.lk.ui.create_order.CreateOrderActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PartyInfoFragment extends Fragment {
    private static List<Field> PartyInfoFields = new ArrayList<>();
    private static List<Field> OriginFields = new ArrayList<>();
    private static byte TypeTabs;

    private GridLayoutManager mng_layout; //Задает табличную разметку

    private PartyInfoAdapter adapter;
    private OriginAdapter adapter2;

    public PartyInfoFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null)
            adapter.notifyDataSetChanged();
        if (adapter2 != null)
            adapter2.notifyDataSetChanged();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (TypeTabs == 2) {
            AddPartyInfoFields();
            mng_layout = new GridLayoutManager(getContext(), 2);
            mng_layout.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (position == 0 || position == 1 || position == 3 || position == 4 || position == 5 || position == 6)
                        return 1;
                    return 2;
                }
            });
            adapter = new PartyInfoAdapter(getContext(), PartyInfoFields);
        } else {
            AddOriginField(OriginFields);
            OriginAdapter.OnOriginClickListener onClickListener = new OriginAdapter.OnOriginClickListener() {
                @Override
                public void onChangeOrigin(boolean isChecked) {
                    List<Field> NewList = new ArrayList<>();
                    if (isChecked)
                        AddOriginFieldNoOrigin(NewList);
                    else
                        AddOriginField(NewList);

                    adapter2.updateList(NewList);
                }
            };
            adapter2 = new OriginAdapter(getContext(), OriginFields, onClickListener);
        }
    }

    public static PartyInfoFragment newInstance(byte Type) {
        TypeTabs = Type;
        return new PartyInfoFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View MyView = inflater.inflate(R.layout.fragment_recyclerview, container, false);

        final RecyclerView recyclerView = MyView.findViewById(R.id.List);
        recyclerView.addItemDecoration(new SpacesItemDecoration((byte) 20, (byte) 15));

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        if (TypeTabs == 2) {
            recyclerView.setHasFixedSize(true);
            if(!CreateOrderActivity.IsPattern)
            recyclerView.setLayoutManager(mng_layout);
            recyclerView.setAdapter(adapter);
        } else
            recyclerView.setAdapter(adapter2);

        return MyView;
    }

    private void AddOriginField(List<Field> NewList) {
        NewList.clear();
        NewList.add(new Field((byte) 3, 56, "", "Происхождение неизвестно"));
        if (CreateOrderActivity.order_id != 4 && !CreateOrderActivity.IsPattern)
            NewList.add(new Field(19, "", "НД на производство", InputType.TYPE_CLASS_TEXT));
        NewList.add(new Field(8, "", "Производитель", InputType.TYPE_CLASS_TEXT));
        NewList.add(new Field(29, "", "Страна происхождения", InputType.TYPE_CLASS_TEXT));
        NewList.add(new Field(30, "", "Регион происхождения", InputType.TYPE_CLASS_TEXT));
        NewList.add(new Field(46, "", "Адрес производства", InputType.TYPE_CLASS_TEXT));
        NewList.add(new Field(130, "", "Страна экспортер", InputType.TYPE_CLASS_TEXT));
        NewList.add(new Field(129, "", "Страна импортер", InputType.TYPE_CLASS_TEXT));
        NewList.add(new Field((byte) 3, 120, "", "Применить для всех проб"));
    }

    private void AddOriginFieldNoOrigin(List<Field> NewList) {
        NewList.clear();
        NewList.add(new Field((byte) 3, 56, "", "Происхождение неизвестно"));
        if (CreateOrderActivity.order_id != 4 && !CreateOrderActivity.IsPattern)
            NewList.add(new Field(19, "", "НД на производство", InputType.TYPE_CLASS_TEXT));
        NewList.add(new Field(58, "", "Номер и дата протокола изъятия/досмотра", InputType.TYPE_CLASS_TEXT));
        NewList.add(new Field((byte) 3, 120, "", "Применить для всех проб"));
    }

    private void AddPartyInfoFields() {
        PartyInfoFields.clear();
        if (!CreateOrderActivity.IsPattern) {
            PartyInfoFields.add(new Field(43, "", "Ветеринарный документ от", InputType.TYPE_CLASS_NUMBER, Objects.requireNonNull(getContext()).getDrawable(R.drawable.ic_date_range_black_24dp), true));
            PartyInfoFields.add(new Field(42, "", "№", InputType.TYPE_CLASS_TEXT));
            PartyInfoFields.add(new Field(34, "", "Номер партии", InputType.TYPE_CLASS_TEXT));
            PartyInfoFields.add(new Field(36, "", "Масса (объем) партии", InputType.TYPE_CLASS_TEXT));
            PartyInfoFields.add(new Field((byte) 1, App.OrderInfo.getFieldValues().get((short)37), 37, " "));
            PartyInfoFields.add(new Field(38, "", "Количество в партии", InputType.TYPE_CLASS_TEXT));
            PartyInfoFields.add(new Field((byte) 1, App.OrderInfo.getFieldValues().get((short)39), 39, " "));
        }
        PartyInfoFields.add(new Field(44, "", "Упаковка партии", InputType.TYPE_CLASS_TEXT));
        PartyInfoFields.add(new Field(35, "", "Срок годности", InputType.TYPE_CLASS_TEXT));
        PartyInfoFields.add(new Field(33, "", true, "Примечание", InputType.TYPE_TEXT_FLAG_MULTI_LINE));
        PartyInfoFields.add(new Field((byte) 3, 121, "", "Применить для всех проб"));
    }
}