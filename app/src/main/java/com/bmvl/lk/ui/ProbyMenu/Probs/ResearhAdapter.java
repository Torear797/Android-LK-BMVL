package com.bmvl.lk.ui.ProbyMenu.Probs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bmvl.lk.R;
import com.bmvl.lk.models.Research;
import com.bmvl.lk.ui.Create_Order.Field;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import java.text.MessageFormat;
import java.util.List;

class ResearhAdapter  extends RecyclerSwipeAdapter<ResearhAdapter.SimpleViewHolder> {
    private LayoutInflater inflater;
    private static List<Research> Researchs;
    private static List<Field> ResearchField;
    public ResearhAdapter(Context context,List<Research> ResearchList, List<Field> Fields) {
        this.inflater = LayoutInflater.from(context);
        Researchs = ResearchList;
        ResearchField = Fields;
    }

    @NonNull
    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_research, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SimpleViewHolder simpleViewHolder, int i) {

        final ResearchFieldAdapter adapter = new ResearchFieldAdapter(inflater.getContext(), ResearchField);
        simpleViewHolder.ResearchList.setAdapter(adapter);

        simpleViewHolder.NumberResearch.setText(MessageFormat.format("№ {0}", i+1));

//        simpleViewHolder.slResearh.setShowMode(SwipeLayout.ShowMode.LayDown);
//        simpleViewHolder.slResearh.addSwipeListener(new SimpleSwipeListener() {
//            @Override
//            public void onOpen(SwipeLayout layout) {
//                YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.trash));
//                //Toast.makeText(layout.getContext(), "Открыто", Toast.LENGTH_SHORT).show();
//            }
//        });
//        simpleViewHolder.swipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
//            @Override
//            public void onDoubleClick(SwipeLayout layout, boolean surface) {
//                Toast.makeText(MyContext, "DoubleClick", Toast.LENGTH_SHORT).show();
//            }
//        });
//        simpleViewHolder.buttonDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(view.getContext(), "Deleted !", Toast.LENGTH_SHORT).show();
//            }
//        });

        //mItemManger.bindView(simpleViewHolder.itemView, i);

        simpleViewHolder.HeaderInfo.setText(String.format("Цена: %d руб.", 0));
        simpleViewHolder.HeaderResearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (simpleViewHolder.ResearchList.getVisibility() == View.VISIBLE) {
                  //  simpleViewHolder.swipeLayout.setSwipeEnabled(true);
                    simpleViewHolder.ResearchList.setVisibility(View.GONE);
                } else {
                    //simpleViewHolder.swipeLayout.setSwipeEnabled(false);
                    simpleViewHolder.ResearchList.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return Researchs.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    static class SimpleViewHolder extends RecyclerView.ViewHolder {
        final ConstraintLayout HeaderResearch;
        final TextView NumberResearch, HeaderInfo;
        final RecyclerView ResearchList;
      //  final SwipeLayout slResearh;
      //  final ImageView buttonDelete;

        SimpleViewHolder(@NonNull View itemView) {
            super(itemView);
            HeaderResearch = itemView.findViewById(R.id.HeaderResearch);
            NumberResearch = itemView.findViewById(R.id.NumberResearch);
            ResearchList = itemView.findViewById(R.id.ResearchList);
            HeaderInfo = itemView.findViewById(R.id.InfoResearch);

          //  slResearh = itemView.findViewById(R.id.swipeResearch);
           // buttonDelete = itemView.findViewById(R.id.trashResearch);
        }
    }

    void insertdata(List<Research> insertList){
        ResearchDiffUtilCallback diffUtilCallback = new ResearchDiffUtilCallback(Researchs, insertList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtilCallback);
        Researchs.addAll(insertList);
        diffResult.dispatchUpdatesTo(this);
    }
    public void updateList(List<Research> newList){
        ResearchDiffUtilCallback diffUtilCallback = new ResearchDiffUtilCallback(Researchs,newList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtilCallback);
        Researchs.clear();
        Researchs.addAll(newList);
        diffResult.dispatchUpdatesTo(this);
    }
}