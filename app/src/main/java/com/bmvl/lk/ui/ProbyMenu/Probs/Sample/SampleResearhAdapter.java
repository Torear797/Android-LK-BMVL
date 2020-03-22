package com.bmvl.lk.ui.ProbyMenu.Probs.Sample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bmvl.lk.R;
import com.bmvl.lk.models.SamplesResearch;
import com.bmvl.lk.ui.Create_Order.Field;
import com.bmvl.lk.ui.ProbyMenu.Probs.ProbsFragment;
import com.bmvl.lk.ui.ProbyMenu.Probs.Research.ResearchFieldAdapter;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import java.text.MessageFormat;
import java.util.List;

class SampleResearhAdapter extends RecyclerSwipeAdapter<SampleResearhAdapter.ResearchItemHolder> {
    private LayoutInflater inflater;
    private List<SamplesResearch> Researchs;
    private static List<Field> ResearchField;

    public SampleResearhAdapter(Context context, List<SamplesResearch> researchs, List<Field> Fields) {
        this.inflater = LayoutInflater.from(context);
        Researchs = researchs;
        ResearchField = Fields;
    }

    @Override
    public ResearchItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_research_s, parent, false);
        return new SampleResearhAdapter.ResearchItemHolder(view);
    }

    @Override
    public void onBindViewHolder(final ResearchItemHolder researchItemHolder, int i) {
        final ResearchFieldAdapter adapter = new ResearchFieldAdapter(inflater.getContext(), ResearchField);
        researchItemHolder.ResearchList.setAdapter(adapter);

        researchItemHolder.NumberResearch.setText(MessageFormat.format("№ {0}", i+1));
        researchItemHolder.HeaderInfo.setText(String.format("Цена: %d руб.", 0));

        researchItemHolder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Удаление ииследования", Toast.LENGTH_SHORT).show();
            }
        });

        researchItemHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        researchItemHolder.HeaderResearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (researchItemHolder.ResearchList.getVisibility() == View.VISIBLE) {
                    researchItemHolder.swipeLayout.setSwipeEnabled(true);
                    researchItemHolder.ResearchList.setVisibility(View.GONE);
                } else if(researchItemHolder.swipeLayout.getOpenStatus() == SwipeLayout.Status.Close){
                    researchItemHolder.swipeLayout.setSwipeEnabled(false);
                    researchItemHolder.ResearchList.setVisibility(View.VISIBLE);
                }
            }
        });
        mItemManger.bindView(researchItemHolder.itemView, i);
    }

    @Override
    public int getItemCount() {
        return Researchs.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipeResearch;
    }

    static class ResearchItemHolder extends RecyclerView.ViewHolder {
        final ConstraintLayout HeaderResearch;
        final TextView NumberResearch;
        final TextView HeaderInfo;
        final RecyclerView ResearchList;
        final SwipeLayout swipeLayout;
        final ImageView buttonDelete;
         ResearchItemHolder(@NonNull View itemView) {
            super(itemView);

            HeaderResearch = itemView.findViewById(R.id.HeaderResearch);
            NumberResearch = itemView.findViewById(R.id.NumberResearch);
            ResearchList = itemView.findViewById(R.id.ResearchList);
            HeaderInfo = itemView.findViewById(R.id.InfoResearch);

            swipeLayout = itemView.findViewById(R.id.swipeResearch);
            buttonDelete = itemView.findViewById(R.id.trashResearch);
        }
    }

    public void insertdata(List<SamplesResearch> insertList, int id){
        SampleResearchDiffUtilCallback diffUtilCallback = new SampleResearchDiffUtilCallback(Researchs, insertList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtilCallback,false);
        Researchs.addAll(insertList);
        ProbsFragment.getSampleResearchList(id).addAll(insertList);
        diffResult.dispatchUpdatesTo(this);
    }
    public void updateList(List<SamplesResearch> newList){
        SampleResearchDiffUtilCallback diffUtilCallback = new SampleResearchDiffUtilCallback(Researchs, newList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtilCallback);
        Researchs.clear();
        Researchs.addAll(newList);
        diffResult.dispatchUpdatesTo(this);
    }
}