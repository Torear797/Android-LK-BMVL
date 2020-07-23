package com.bmvl.lk.ui.ProbyMenu.Probs.Research;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bmvl.lk.R;
import com.bmvl.lk.Rest.Order.ResearchRest;
import com.bmvl.lk.Rest.Suggestion;
import com.bmvl.lk.data.SpacesItemDecoration;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ResearhAdapter extends RecyclerSwipeAdapter<ResearhAdapter.ResearchItemHolder> {
    private TreeMap<Short, ResearchRest> researches; //Исследования
    private RecyclerView.RecycledViewPool viewPool;

    private static String[] Indicators;
    private static List<Suggestion> suggestions;
    private static short materialId;

    public ResearhAdapter(TreeMap<Short, ResearchRest> ResearchesLise) {
        viewPool = new RecyclerView.RecycledViewPool();
        researches = ResearchesLise;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public ResearchItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ResearchItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_prob, parent, false));
    }

    @Override
    public void onBindViewHolder(ResearchItemHolder researchItemHolder, int i) {
        final ResearchRest CurrentResearch = researches.get(getPositionKey(i));
        ResearchFieldAdapter adapter = new ResearchFieldAdapter(CurrentResearch, Indicators, suggestions, materialId, i + 1, researchItemHolder.Number);
        researchItemHolder.List.setAdapter(adapter);

        if (CurrentResearch.getIndicatorVal() != null && CurrentResearch.getMethodVal() != null && CurrentResearch.getTypeVal() != null)
            researchItemHolder.Number.setText(MessageFormat.format("№ {0} - {1}", i + 1, researchItemHolder.Number.getContext().getString(R.string.accreditation_ok)));
        else
            researchItemHolder.Number.setText(MessageFormat.format("№ {0} - {1}", i + 1, researchItemHolder.Number.getContext().getString(R.string.accreditation_bad)));

        mItemManger.bindView(researchItemHolder.itemView, i);
    }

    public void UpdateIndicators(List<Suggestion> sug, int id) {
        suggestions = sug;
        materialId = (short) id;

        Indicators = new String[sug.size()];
        for (int i = 0; i < sug.size(); i++)
            Indicators[i] = sug.get(i).getValue();
    }

    private Short getPositionKey(int position) {
        return new ArrayList<>(researches.keySet()).get(position);
    }

    @Override
    public int getItemCount() {
        return researches.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    class ResearchItemHolder extends RecyclerView.ViewHolder {
        final ConstraintLayout head;
        final LinearLayout GreenHeader;
        final TextView Number, Info;
        final SwipeLayout swipeLayout;
        final ImageView buttonDelete, createBtn;
        final RecyclerView List;
        final ImageView arrow;

        ResearchItemHolder(@NonNull View itemView) {
            super(itemView);
            Number = itemView.findViewById(R.id.NumberProb);
            List = itemView.findViewById(R.id.ProbFields);
            head = itemView.findViewById(R.id.Header);
            Info = itemView.findViewById(R.id.InfoProb); //Можно будет убрать
            GreenHeader = itemView.findViewById(R.id.linearLayout);

            swipeLayout = itemView.findViewById(R.id.swipe);
            buttonDelete = itemView.findViewById(R.id.trash);
            createBtn = itemView.findViewById(R.id.create);

            Info.setVisibility(View.GONE);
            createBtn.setVisibility(View.GONE);
            arrow = itemView.findViewById(R.id.arrow);

            head.setOnClickListener(view -> {
                if (List.getVisibility() == View.VISIBLE)
                    swipeLayout.setSwipeEnabled(true);
                else if (swipeLayout.getOpenStatus() == SwipeLayout.Status.Close)
                    swipeLayout.setSwipeEnabled(false);
                List.setVisibility(List.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                arrow.setImageResource(List.getVisibility() == View.VISIBLE ? R.drawable.ic_w_arrow_ap : R.drawable.ic_w_arrow_down);
            });

            buttonDelete.setOnClickListener(view -> {
                swipeLayout.close();
                closeAllItems();
                TreeMap<Short, ResearchRest> insertlist = new TreeMap<>(researches);
                insertlist.remove(getPositionKey(getLayoutPosition()));
                updateList(insertlist);
            });

            List.addItemDecoration(new SpacesItemDecoration((byte) 20, (byte) 5));
            List.setItemAnimator(new DefaultItemAnimator());

            swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);

            List.setRecycledViewPool(viewPool);
            List.setHasFixedSize(true);
        }
    }

    public void insertdata(Map<Short, ResearchRest> insertList) {
        ResearchDiffUtilCallback diffUtilCallback = new ResearchDiffUtilCallback(researches, insertList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtilCallback);
        researches.putAll(insertList);
        diffResult.dispatchUpdatesTo(this);
    }

    public void updateList(Map<Short, ResearchRest> newList) {
        ResearchDiffUtilCallback diffUtilCallback = new ResearchDiffUtilCallback(researches, newList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtilCallback);
        researches.clear();
        researches.putAll(newList);
        diffResult.dispatchUpdatesTo(this);
    }
}