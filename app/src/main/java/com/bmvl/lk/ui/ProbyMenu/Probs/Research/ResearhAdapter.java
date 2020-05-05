package com.bmvl.lk.ui.ProbyMenu.Probs.Research;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Spinner;
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
   // private List<Field> ResearchField; //Поля Исследований
    private RecyclerView.RecycledViewPool viewPool;
    private OnResearchClickListener onResearchClickListener;

    private String[] Indicators;

    public ResearhAdapter(Context context, TreeMap<Short, ResearchRest> ResearchesLise, OnResearchClickListener Listener, String[] ind) {
        this.inflater = LayoutInflater.from(context);
        this.onResearchClickListener = Listener;
    //    ResearchField = Fields;
        researches = ResearchesLise;
        this.Indicators = ind;
    }

    public interface OnResearchClickListener {
        void onUpdateResearch();
    }

    @NonNull
    @Override
    public ResearchItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_research, parent, false);
        return new ResearchItemHolder(view);
    }

    @Override
    public void onBindViewHolder(ResearchItemHolder researchItemHolder, int i) {
        final ResearchRest CurrentResearch = researches.get(getPositionKey(i));
       // final ResearchFieldAdapter adapter = new ResearchFieldAdapter(inflater.getContext(), ResearchField, Indicators);
       // researchItemHolder.ResearchList.setAdapter(adapter);
      //  researchItemHolder.ResearchList.addItemDecoration(new SpacesItemDecoration((byte) 20, (byte) 15));
       // researchItemHolder.ResearchList.setRecycledViewPool(viewPool);

        setTestResearchData(CurrentResearch);

        researchItemHolder.NumberResearch.setText(MessageFormat.format("№ {0}", getPositionKey(i)));
        
        researchItemHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        mItemManger.bindView(researchItemHolder.itemView, i);
    }

    private void setTestResearchData(ResearchRest CurrentResearch){
        CurrentResearch.setIndicatorId((short)499);
        CurrentResearch.setIndicatorNd("ГОСТ  Р 520");
        CurrentResearch.setIndicatorNdId((short)682);

        CurrentResearch.setMethodId((short)53);
        CurrentResearch.setMethodNd("ГОСТ  Р 520");
        CurrentResearch.setMethodNdId((short)682);

        CurrentResearch.setTypeId((short)3);

        CurrentResearch.setIndicatorVal("Консистенция");
        CurrentResearch.setMethodVal("Органолептический");
        CurrentResearch.setTypeVal("Классика");
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
        final ConstraintLayout HeaderResearch, Content;
        final TextView NumberResearch;
        final SwipeLayout swipeLayout;
        final ImageView buttonDelete, ignorBtn;
        final Spinner Indicators, Methods, Types;

        ResearchItemHolder(@NonNull View itemView) {
            super(itemView);
            HeaderResearch = itemView.findViewById(R.id.Header);
            NumberResearch = itemView.findViewById(R.id.NumberProb);
            Content = itemView.findViewById(R.id.Content);

            swipeLayout = itemView.findViewById(R.id.swipe);
            buttonDelete = itemView.findViewById(R.id.trash);
            ignorBtn = itemView.findViewById(R.id.create);

            ignorBtn.setVisibility(View.GONE);

            //Spiners
            Indicators = itemView.findViewById(R.id.Indicator);
            Methods = itemView.findViewById(R.id.Method);
            Types = itemView.findViewById(R.id.Type);

            HeaderResearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Content.getVisibility() == View.VISIBLE)
                        swipeLayout.setSwipeEnabled(true);
                    else if (swipeLayout.getOpenStatus() == SwipeLayout.Status.Close)
                        swipeLayout.setSwipeEnabled(false);
                    Content.setVisibility(Content.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
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
