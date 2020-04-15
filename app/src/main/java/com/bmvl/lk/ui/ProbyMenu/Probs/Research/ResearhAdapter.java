package com.bmvl.lk.ui.ProbyMenu.Probs.Research;

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
import com.bmvl.lk.models.Research;
import com.bmvl.lk.ui.Create_Order.Field;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import java.text.MessageFormat;
import java.util.List;

public class ResearhAdapter extends RecyclerSwipeAdapter<ResearhAdapter.ResearchItemHolder> {
    private LayoutInflater inflater;
    private List<Research> Researchs;
    private static List<Field> ResearchField;

    public ResearhAdapter(Context context, List<Research> ResearchList, List<Field> Fields) {
        this.inflater = LayoutInflater.from(context);
        Researchs = ResearchList;
        ResearchField = Fields;
    }

    @NonNull
    @Override
    public ResearchItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_research_s, parent, false);
        return new ResearchItemHolder(view);
    }

    @Override
    public void onBindViewHolder(final ResearchItemHolder ResearchItemHolder, int i) {

        final ResearchFieldAdapter adapter = new ResearchFieldAdapter(inflater.getContext(), ResearchField);
        ResearchItemHolder.ResearchList.setAdapter(adapter);

        ResearchItemHolder.NumberResearch.setText(MessageFormat.format("№ {0}", i + 1));

        ResearchItemHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        ResearchItemHolder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Удаление ииследования", Toast.LENGTH_SHORT).show();
            }
        });

        ResearchItemHolder.HeaderInfo.setText(String.format("Цена: %d руб.", 0));
        ResearchItemHolder.HeaderResearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ResearchItemHolder.ResearchList.getVisibility() == View.VISIBLE) {
                    ResearchItemHolder.swipeLayout.setSwipeEnabled(true);
                    ResearchItemHolder.ResearchList.setVisibility(View.GONE);
                } else if(ResearchItemHolder.swipeLayout.getOpenStatus() == SwipeLayout.Status.Close){
                    ResearchItemHolder.swipeLayout.setSwipeEnabled(false);
                    ResearchItemHolder.ResearchList.setVisibility(View.VISIBLE);
                }
            }
        });

       // SwipeItemRecyclerMangerImpl mItemManger2 = new SwipeItemRecyclerMangerImpl(this);
        mItemManger.bindView(ResearchItemHolder.itemView, i);
    }

    @Override
    public int getItemCount() {
        return Researchs.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipeResearch;
    }

    public static class ResearchItemHolder extends RecyclerView.ViewHolder {
        final ConstraintLayout HeaderResearch;
        final TextView NumberResearch, HeaderInfo;
        final RecyclerView ResearchList;
        final SwipeLayout swipeLayout;
        final ImageView buttonDelete;

        public ResearchItemHolder(@NonNull View itemView) {
            super(itemView);
            HeaderResearch = itemView.findViewById(R.id.HeaderResearch);
            NumberResearch = itemView.findViewById(R.id.NumberResearch);
            ResearchList = itemView.findViewById(R.id.ResearchList);
            HeaderInfo = itemView.findViewById(R.id.InfoResearch);

            swipeLayout = itemView.findViewById(R.id.swipeResearch);
            buttonDelete = itemView.findViewById(R.id.trashResearch);
        }
    }

    public void insertdata(List<Research> insertList) {
        ResearchDiffUtilCallback diffUtilCallback = new ResearchDiffUtilCallback(Researchs, insertList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtilCallback, false);
        Researchs.addAll(insertList);
        diffResult.dispatchUpdatesTo(this);
    }

    public void updateList(List<Research> newList) {
        ResearchDiffUtilCallback diffUtilCallback = new ResearchDiffUtilCallback(Researchs, newList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtilCallback);
        Researchs.clear();
        Researchs.addAll(newList);
        diffResult.dispatchUpdatesTo(this);
    }
}