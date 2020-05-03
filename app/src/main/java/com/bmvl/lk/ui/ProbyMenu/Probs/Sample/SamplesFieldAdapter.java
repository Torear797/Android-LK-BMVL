package com.bmvl.lk.ui.ProbyMenu.Probs.Sample;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bmvl.lk.R;
import com.bmvl.lk.Rest.Order.ResearchRest;
import com.bmvl.lk.Rest.Order.SamplesRest;
import com.bmvl.lk.ViewHolders.ResearchPanelHolder;
import com.bmvl.lk.ViewHolders.TextViewHolder;
import com.bmvl.lk.data.SpacesItemDecoration;
import com.bmvl.lk.ui.Create_Order.Field;
import com.bmvl.lk.ui.ProbyMenu.Probs.Research.ResearhAdapter;
import com.daimajia.swipe.util.Attributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SamplesFieldAdapter extends RecyclerView.Adapter {
    private LayoutInflater inflater;
    private RecyclerView.RecycledViewPool viewPool;
    private List<Field> ResearchsField; //Поля исследвоаний
    private List<Field> SamplesField; //Поля образцов

    private SamplesRest CurrentSample; //Текущий образец
    private ResearhAdapter Adapter;

    public SamplesFieldAdapter(Context context, List<Field> ResFields, List<Field> SamFields, SamplesRest Sample) {
        this.inflater = LayoutInflater.from(context);
        ResearchsField = ResFields;
        SamplesField = SamFields;
        CurrentSample = Sample;
    }

    @Override
    public int getItemViewType(int position) {
        return SamplesField.get(position).getType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View view = inflater.inflate(R.layout.item_field, parent, false);
            return new TextViewHolder(view);
        } else {
            View view1 = inflater.inflate(R.layout.item_research_list, parent, false);
            return new ResearchPanelHolder(view1);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        final Field f = SamplesField.get(position);
        switch (f.getType()) {
            case 0:
                ((TextViewHolder) holder).Layout.setHint(f.getHint());
                ((TextViewHolder) holder).field.setInputType(f.getInputType());

                if(CurrentSample.getFields().containsKey((short)f.getColumn_id()))
                ((TextViewHolder) holder).field.setText(CurrentSample.getFields().get((short)f.getColumn_id()));

                ((TextViewHolder) holder).field.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void afterTextChanged(Editable s) {
                        CurrentSample.getFields().put((short) f.getColumn_id(), String.valueOf(s));
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }
                });
                break;
            case 6:
                Adapter = new ResearhAdapter(inflater.getContext(), ResearchsField, CurrentSample.getResearches(), Listener);
                (Adapter).setMode(Attributes.Mode.Single);
                ((ResearchPanelHolder) holder).ResearchList.setAdapter(Adapter);
                ((ResearchPanelHolder) holder).ResearchList.addItemDecoration(new SpacesItemDecoration((byte) 20, (byte) 0));
                ((ResearchPanelHolder) holder).ResearchList.setRecycledViewPool(viewPool);

                //Добавляет исследование
                ((ResearchPanelHolder) holder).btnAddReserch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        short size = (short) CurrentSample.getResearches().size();
                        short newid = 0;
                        if (size > 0)
                            newid = getPositionKeyR(size - 1, CurrentSample.getResearches());

                        Map<Short, ResearchRest> insertlist = new HashMap<>();
                        insertlist.put((short) (newid + 1), new ResearchRest(newid));
                        Adapter.insertdata(insertlist);
                        ((ResearchPanelHolder) holder).ResearchList.smoothScrollToPosition(Adapter.getItemCount() - 1);
                    }
                });

                break;
        }
    }
    ResearhAdapter.OnResearchClickListener Listener = new ResearhAdapter.OnResearchClickListener(){

        @Override
        public void onUpdateResearch() {
            Adapter.closeAllItems();
        }
    };

    private Short getPositionKeyR(int position, Map<Short, ResearchRest> List) {
        if(List.size() > 0)
        return new ArrayList<Short>(List.keySet()).get(position);
        else return 0;
    }

    @Override
    public int getItemCount() {
        return SamplesField.size();
    }
}