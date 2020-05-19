package com.bmvl.lk.ui.ProbyMenu.Probs.Research;

import android.content.Context;
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
import com.bmvl.lk.data.Field;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ResearhAdapter extends RecyclerSwipeAdapter<ResearhAdapter.ResearchItemHolder> {
    private LayoutInflater inflater;
    private TreeMap<Short, ResearchRest> researches; //Исследования
    private List<Field> ResearchFields = new ArrayList<>();
    private RecyclerView.RecycledViewPool viewPool;
    private OnResearchClickListener onResearchClickListener;

    private ResearchFieldAdapter adapter;
    private static String[] Indicators;
    private static List<Suggestion> suggestions;
    private static short materialId;

    public ResearhAdapter(Context context, TreeMap<Short, ResearchRest> ResearchesLise, OnResearchClickListener Listener) {
        this.inflater = LayoutInflater.from(context);
        viewPool = new RecyclerView.RecycledViewPool();
        this.onResearchClickListener = Listener;
        researches = ResearchesLise;
        AddResearchFields();
    }

    public interface OnResearchClickListener {
        void onUpdateResearch();
    }

    private void AddResearchFields() {
        ResearchFields.add(new Field((byte) 1, 0, "", "Показатель"));
        ResearchFields.add(new Field((byte) 2, 1, "", "Метод испытаний"));
        ResearchFields.add(new Field((byte) 3, 2, "", "Тип исследования"));
    }

    @NonNull
    @Override
    public ResearchItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_prob, parent, false);
        return new ResearchItemHolder(view);
    }

    @Override
    public void onBindViewHolder(ResearchItemHolder researchItemHolder, int i) {
        final ResearchRest CurrentResearch = researches.get(getPositionKey(i));

        adapter = new ResearchFieldAdapter(inflater.getContext(), ResearchFields, CurrentResearch, Indicators, suggestions, materialId);
        researchItemHolder.List.setAdapter(adapter);
        researchItemHolder.List.addItemDecoration(new SpacesItemDecoration((byte) 20, (byte) 5));
        researchItemHolder.List.setItemAnimator(new DefaultItemAnimator());
        researchItemHolder.List.setRecycledViewPool(viewPool);
        researchItemHolder.List.setHasFixedSize(true);

        researchItemHolder.Number.setText(MessageFormat.format("№ {0}", i + 1));

        researchItemHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
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

            head.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (List.getVisibility() == View.VISIBLE)
                        swipeLayout.setSwipeEnabled(true);
                    else if (swipeLayout.getOpenStatus() == SwipeLayout.Status.Close)
                        swipeLayout.setSwipeEnabled(false);
                    List.setVisibility(List.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                }
            });

            buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    swipeLayout.close();
                    onResearchClickListener.onUpdateResearch();
                    TreeMap<Short, ResearchRest> insertlist = new TreeMap<>(researches);
                    insertlist.remove(getPositionKey(getLayoutPosition()));
                    updateList(insertlist);
                }
            });

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