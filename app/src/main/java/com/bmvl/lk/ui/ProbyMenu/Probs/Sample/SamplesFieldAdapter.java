package com.bmvl.lk.ui.ProbyMenu.Probs.Sample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bmvl.lk.R;
import com.bmvl.lk.models.Samples;
import com.bmvl.lk.models.SamplesResearch;
import com.bmvl.lk.ui.Create_Order.Field;
import com.bmvl.lk.ui.ProbyMenu.Probs.ProbsFragment;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.daimajia.swipe.util.Attributes;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

public class SamplesFieldAdapter extends RecyclerSwipeAdapter<SamplesFieldAdapter.SimpleViewHolder> {
    private LayoutInflater inflater;
    private static List<SamplesResearch> Researchs;
    private static List<Samples> Samples;
    private static List<Field> ResearchsField;
    private static List<Field> SamplesField;
    private int sample_id;
    private int prob_id;

    public SamplesFieldAdapter(Context context, List<SamplesResearch> ResearchList, List<Field> ResFields, List<Samples> SamList, List<Field> SamFields, int id, int probid) {
        this.inflater = LayoutInflater.from(context);
        Researchs = ResearchList;
        ResearchsField = ResFields;
        Samples = SamList;
        SamplesField = SamFields;
        sample_id = id;
        prob_id = probid;
    }

    @Override
    public int getItemViewType(int position) {
        return SamplesField.get(position).getType();
    }

    @Override
    public SamplesFieldAdapter.SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View view = inflater.inflate(R.layout.item_field, parent, false);
            return new SwipeTextView(view);
        } else {
            View view1 = inflater.inflate(R.layout.item_research_list, parent, false);
            return new SimpleViewHolder(view1);
        }
    }

    @Override
    public void onBindViewHolder(final SamplesFieldAdapter.SimpleViewHolder simpleViewHolder, int i) {
        final Field f = SamplesField.get(i);
        if (f.getType() == 0) {
            ((SwipeTextView) simpleViewHolder).Layout.setHint(f.getHint());
            ((SwipeTextView) simpleViewHolder).field.setInputType(f.getInputType());
            ((SwipeTextView) simpleViewHolder).field.setText(f.getValue());
        } else {
            final SampleResearhAdapter adapter = new SampleResearhAdapter(inflater.getContext(), ProbsFragment.getSampleResearchList(sample_id,prob_id), ResearchsField);
            (adapter).setMode(Attributes.Mode.Single);
            simpleViewHolder.ResearchList.setAdapter(adapter);

            simpleViewHolder.AddResearch.setText("Добавить исследование");
            simpleViewHolder.AddResearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<SamplesResearch> insertlist = new ArrayList<>();

                    insertlist.add(new SamplesResearch(Researchs.size()-1,sample_id));
                    adapter.insertdata(insertlist, prob_id);

                    simpleViewHolder.ResearchList.smoothScrollToPosition(adapter.getItemCount() - 1);
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return SamplesField.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    static class SimpleViewHolder extends RecyclerView.ViewHolder {
        final TextView PriceText;
        final RecyclerView ResearchList;
        final MaterialButton AddResearch;

        SimpleViewHolder(@NonNull View itemView) {
            super(itemView);
            PriceText = itemView.findViewById(R.id.Price);
            ResearchList = itemView.findViewById(R.id.List);
            AddResearch = itemView.findViewById(R.id.addBtn);

        }
    }

    private static class SwipeTextView extends SimpleViewHolder {
        public TextInputEditText field;
        final TextInputLayout Layout;

        SwipeTextView(View view) {
            super(view);
            field = itemView.findViewById(R.id.TextInput);
            Layout = itemView.findViewById(R.id.TextLayout);
        }
    }
}