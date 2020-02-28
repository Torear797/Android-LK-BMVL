package com.bmvl.lk.ui.ProbyMenu.PartyInfo;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bmvl.lk.R;
import com.bmvl.lk.ui.Create_Order.Field;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class PartyInfoFragment extends Fragment {
    public static List<Field> PartyInfoFields = new ArrayList<>();

    public PartyInfoFragment() {
        // Required empty public constructor
    }

    public static PartyInfoFragment newInstance() {
        return new PartyInfoFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View MyView = inflater.inflate(R.layout.fragment_party_info, container, false);
        final RecyclerView recyclerView =  MyView.findViewById(R.id.PartyInfoList);
        recyclerView.setHasFixedSize(true);
        AddPartyInfoFields();
        final GridLayoutManager mng_layout = new GridLayoutManager(getContext(), 2);
        mng_layout.setSpanSizeLookup( new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 0 || position == 1 || position == 3 || position == 4 || position == 5 || position == 6)
                    return 1;
                return 2;
            }
        });
        recyclerView.setLayoutManager(mng_layout);

        final PartyInfoAdapter adapter = new PartyInfoAdapter(getContext());
        recyclerView.setAdapter(adapter);
        return MyView;
    }

    private void AddPartyInfoFields(){
        PartyInfoFields.clear();
        PartyInfoFields.add(new Field(1,"","Ветеринарный документ от", InputType.TYPE_CLASS_NUMBER, Objects.requireNonNull(getContext()).getDrawable(R.drawable.ic_date_range_black_24dp),true));
        PartyInfoFields.add(new Field(1,"","№", InputType.TYPE_CLASS_TEXT));
        PartyInfoFields.add(new Field(1,"","Номер партии",InputType.TYPE_CLASS_TEXT));
        PartyInfoFields.add(new Field(1,"","Масса (объем) партии",InputType.TYPE_CLASS_TEXT));
        PartyInfoFields.add(new Field((byte)1,R.array.dimension_prob,0,""," "));
        PartyInfoFields.add(new Field(1,"","Количество в партии",InputType.TYPE_CLASS_TEXT));
        PartyInfoFields.add(new Field((byte)1,R.array.dimension_prob,0,""," "));
        PartyInfoFields.add(new Field(1,"","Упаковка партии",InputType.TYPE_CLASS_TEXT));
        PartyInfoFields.add(new Field(1,"","Срок годности",InputType.TYPE_CLASS_TEXT));
        PartyInfoFields.add(new Field(1,"", true,"Примечание", InputType.TYPE_TEXT_FLAG_MULTI_LINE));
        PartyInfoFields.add(new Field((byte)3,0,"","Применить для всех проб"));
    }
}