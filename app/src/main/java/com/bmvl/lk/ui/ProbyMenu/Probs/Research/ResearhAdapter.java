package com.bmvl.lk.ui.ProbyMenu.Probs.Research;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bmvl.lk.R;
import com.bmvl.lk.Rest.Order.ResearchRest;
import com.bmvl.lk.data.SpacesItemDecoration;
import com.bmvl.lk.ui.Create_Order.Field;
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
    private List<Field> ResearchField; //Поля Исследований
    private RecyclerView.RecycledViewPool viewPool;
    private OnResearchClickListener onResearchClickListener;

    public ResearhAdapter(Context context, List<Field> Fields, TreeMap<Short, ResearchRest> ResearchesLise, OnResearchClickListener Listener) {
        this.inflater = LayoutInflater.from(context);
        this.onResearchClickListener = Listener;
        ResearchField = Fields;
        researches = ResearchesLise;
    }

    public interface OnResearchClickListener {
        void onUpdateResearch();
    }

    @NonNull
    @Override
    public ResearchItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_prob, parent, false);
        return new ResearchItemHolder(view);
    }

    @Override
    public void onBindViewHolder(ResearchItemHolder researchItemHolder, int i) {
        // final ResearchRest CurrentResearch = researches.get(getPositionKey(i));
        final ResearchFieldAdapter adapter = new ResearchFieldAdapter(inflater.getContext(), ResearchField);
        researchItemHolder.ResearchList.setAdapter(adapter);
        researchItemHolder.ResearchList.addItemDecoration(new SpacesItemDecoration((byte) 20, (byte) 15));
        researchItemHolder.ResearchList.setRecycledViewPool(viewPool);

        researchItemHolder.NumberResearch.setText(MessageFormat.format("№ {0}", getPositionKey(i)));
        researchItemHolder.HeaderInfo.setText(String.format("Цена: %d руб.", 0));
        
        researchItemHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        mItemManger.bindView(researchItemHolder.itemView, i);
    }

    private Short getPositionKey(int position) {
        if(researches.size() > 0)
        return new ArrayList<Short>(researches.keySet()).get(position);
        else return 0;
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
        final ConstraintLayout HeaderResearch;
        final TextView NumberResearch, HeaderInfo;
        final RecyclerView ResearchList;
        final SwipeLayout swipeLayout;
        final ImageView buttonDelete, ignorBtn;

        ResearchItemHolder(@NonNull View itemView) {
            super(itemView);
            HeaderResearch = itemView.findViewById(R.id.Header);
            NumberResearch = itemView.findViewById(R.id.NumberProb);
            ResearchList = itemView.findViewById(R.id.ProbFields);
            HeaderInfo = itemView.findViewById(R.id.InfoProb);

            swipeLayout = itemView.findViewById(R.id.swipe);
            buttonDelete = itemView.findViewById(R.id.trash);
            ignorBtn = itemView.findViewById(R.id.create);

            ignorBtn.setVisibility(View.GONE);

            HeaderResearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (ResearchList.getVisibility() == View.VISIBLE)
                        swipeLayout.setSwipeEnabled(true);
                    else if (swipeLayout.getOpenStatus() == SwipeLayout.Status.Close)
                        swipeLayout.setSwipeEnabled(false);
                    ResearchList.setVisibility(ResearchList.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
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
                   // Toast.makeText(view.getContext(), "Удаление ииследования", Toast.LENGTH_SHORT).show();
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
