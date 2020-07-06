package com.bmvl.lk.ui.ProbyMenu.Probs.Sample;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bmvl.lk.R;
import com.bmvl.lk.Rest.Order.ResearchRest;
import com.bmvl.lk.Rest.Order.SamplesRest;
import com.bmvl.lk.Rest.Suggestion;
import com.bmvl.lk.ViewHolders.MultiSpinerHolder;
import com.bmvl.lk.ViewHolders.ResearchPanelHolder;
import com.bmvl.lk.ViewHolders.SpinerHolder;
import com.bmvl.lk.ViewHolders.TextViewHolder;
import com.bmvl.lk.data.Field;
import com.bmvl.lk.ui.ProbyMenu.Probs.MultiSpinner;
import com.bmvl.lk.ui.ProbyMenu.Probs.Research.ResearhAdapter;
import com.daimajia.swipe.util.Attributes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SamplesFieldAdapter extends RecyclerView.Adapter {
    private LayoutInflater inflater;
    private RecyclerView.RecycledViewPool viewPool;
    private List<Field> SamplesField; //Поля образцов

    private SamplesRest CurrentSample; //Текущий образец
    private ResearhAdapter Adapter;

    private List<Suggestion> buffer_sug;
    private Integer buffer_id;

    private Map<String, String> ProbFields; //Поля пробы

    SamplesFieldAdapter(Context context, List<Field> SamFields, SamplesRest Sample,Map<String, String> ProbFields) {
        this.inflater = LayoutInflater.from(context);
        this.ProbFields = ProbFields;
        viewPool = new RecyclerView.RecycledViewPool();
        SamplesField = SamFields;
        CurrentSample = Sample;
    }

    @Override
    public int getItemViewType(int position) {
        return SamplesField.get(position).getType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case 1:
                View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_spiner, parent, false);
                final SpinerHolder holder1 = new SpinerHolder(view1);

                AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String item = (String) parent.getItemAtPosition(position);
                        CurrentSample.getFields().put(GetColumn_id(holder1.getLayoutPosition()), String.valueOf(item));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                };
                holder1.spiner.setOnItemSelectedListener(itemSelectedListener);

                return holder1;
            case 5:
                View view5 = inflater.inflate(R.layout.item_multi_spinner, parent, false);
                return new MultiSpinerHolder(view5);
            case 6:
                View view6 = inflater.inflate(R.layout.item_research_list, parent, false);
                return new ResearchPanelHolder(view6);
            default:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_field, parent, false);
                final TextViewHolder holder = new TextViewHolder(view);

                holder.field.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void afterTextChanged(Editable s) {
                        if (!String.valueOf(s).equals(""))
                            CurrentSample.getFields().put(GetColumn_id(holder.getLayoutPosition()), String.valueOf(s));
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }
                });

                return holder;
        }
    }

    private short GetColumn_id(int position) {
        return (short) (SamplesField.get(position).getColumn_id());
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        final Field f = SamplesField.get(position);
        switch (f.getType()) {
            case 0:
                ((TextViewHolder) holder).Layout.setHint(f.getHint());
                ((TextViewHolder) holder).field.setInputType(f.getInputType());
                if (CurrentSample.getFields().containsKey((short) f.getColumn_id()))
                    ((TextViewHolder) holder).field.setText(CurrentSample.getFields().get((short) f.getColumn_id()));
                break;
            case 1:
                if (f.getEntries() != -1) {
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(inflater.getContext(), f.getEntries(), android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    ((SpinerHolder) holder).spiner.setAdapter(adapter);
                    if (CurrentSample.getFields().containsKey((short) (f.getColumn_id())))
                        ((SpinerHolder) holder).spiner.setSelection(adapter.getPosition(CurrentSample.getFields().get((short) (f.getColumn_id()))));
                } else {
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(inflater.getContext(), android.R.layout.simple_spinner_item, f.getSpinerData());
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    ((SpinerHolder) holder).spiner.setAdapter(adapter);
                    if (CurrentSample.getFields().containsKey((short) (f.getColumn_id())))
                        ((SpinerHolder) holder).spiner.setSelection(adapter.getPosition(CurrentSample.getFields().get((short) (f.getColumn_id()))));
                }


                ((SpinerHolder) holder).txtHint.setText(f.getHint());
                break;
            case 5:
                final List<String> items = Arrays.asList(inflater.getContext().getResources().getStringArray(f.getEntries()));
                MultiSpinner.MultiSpinnerListener onSelectedListener = new MultiSpinner.MultiSpinnerListener() {
                    public void onItemsSelected(boolean[] selected, String id) {
                        CurrentSample.getFields().put((short) f.getColumn_id(), id);
                    }
                };
                String selected = "";
                if (CurrentSample.getFields().containsKey((short) f.getColumn_id()))
                    selected = CurrentSample.getFields().get((short) f.getColumn_id());

                assert selected != null;
                ((MultiSpinerHolder) holder).spiner.setItems(
                        items,
                        selected,
                        onSelectedListener
                );

                ((MultiSpinerHolder) holder).txtHint.setText(f.getHint());
                break;
            case 6:
                Adapter = new ResearhAdapter(inflater.getContext(), CurrentSample.getResearches());
                (Adapter).setMode(Attributes.Mode.Single);
                ((ResearchPanelHolder) holder).ResearchList.setAdapter(Adapter);
                ((ResearchPanelHolder) holder).ResearchList.setRecycledViewPool(viewPool);

                if (buffer_id != null && buffer_sug != null) {
                    UpdateAdapter(buffer_sug, buffer_id);
                    buffer_sug = null;
                    buffer_id = null;
                }

                //Добавляет исследование
                ((ResearchPanelHolder) holder).btnAddReserch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(ProbFields.containsKey("5")) {
                            short size = (short) CurrentSample.getResearches().size();
                            short newid = 0;
                            if (size > 0)
                                newid = getPositionKeyR(size - 1, CurrentSample.getResearches());

                            Map<Short, ResearchRest> insertlist = new HashMap<>();
                            insertlist.put((short) (newid + 1), new ResearchRest(newid));
                            Adapter.insertdata(insertlist);
                            ((ResearchPanelHolder) holder).ResearchList.smoothScrollToPosition(Adapter.getItemCount() - 1);
                        } else
                            Toast.makeText(inflater.getContext(), inflater.getContext().getString(R.string.MaterialNoSelect), Toast.LENGTH_SHORT).show();
                    }
                });

                break;
        }
    }

    void UpdateAdapter(List<Suggestion> suggestions, int id) {
        if (Adapter != null) {
            Adapter.UpdateIndicators(suggestions, id);
            Adapter.notifyDataSetChanged();
        } else {
            buffer_sug = suggestions;
            buffer_id = id;
        }
    }

    private Short getPositionKeyR(int position, Map<Short, ResearchRest> List) {
        if (List.size() > 0)
            return new ArrayList<>(List.keySet()).get(position);
        else return 0;
    }

    @Override
    public int getItemCount() {
        return SamplesField.size();
    }
}