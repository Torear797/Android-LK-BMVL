package com.bmvl.lk.ui.Create_Order.OrderProbs;

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
import com.bmvl.lk.Rest.Order.SamplesRest;
import com.bmvl.lk.data.SpacesItemDecoration;
import com.bmvl.lk.ui.Create_Order.Field;
import com.bmvl.lk.ui.ProbyMenu.Probs.Sample.SamplesDiffUtilCallback;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class SamplesAdapter2 extends RecyclerSwipeAdapter<SamplesAdapter2.SimpleViewHolder> {
    private LayoutInflater inflater;
    private TreeMap<Short, SamplesRest> Samples; //Образцы

    private List<Field> ResearchsField; //Поля исследований
    private List<Field> SamplesField; //Поля образцов

    private RecyclerView.RecycledViewPool viewPool;

    public SamplesAdapter2(Context context, List<Field> Researchs, List<Field> Samples, TreeMap<Short, SamplesRest> SamplesList ) {
        this.inflater = LayoutInflater.from(context);
        ResearchsField = Researchs;
        SamplesField = Samples;
        this.Samples = SamplesList;
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view1 = inflater.inflate(R.layout.item_prob, parent, false);
        return new SimpleViewHolder(view1);
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder simpleViewHolder, int i) {
        final SamplesRest CurrentSample = Samples.get(getPositionKey(i));

        final SamplesFieldAdapter2 adapter = new SamplesFieldAdapter2(inflater.getContext(), ResearchsField, SamplesField, CurrentSample);
        simpleViewHolder.SampleList.setAdapter(adapter);
        simpleViewHolder.SampleList.addItemDecoration(new SpacesItemDecoration((byte) 20, (byte) 0));
        simpleViewHolder.SampleList.setRecycledViewPool(viewPool);

        simpleViewHolder.NumberSample.setText(MessageFormat.format("№ {0}", getPositionKey(i)));
        simpleViewHolder.Info.setText("Образец");
        mItemManger.bindView(simpleViewHolder.itemView, i);
    }

    @Override
    public int getItemCount() {
        return Samples.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    private Short getPositionKey(int position) {
        return new ArrayList<Short>(Samples.keySet()).get(position);
    }

    static class SimpleViewHolder extends RecyclerView.ViewHolder {
        final ConstraintLayout head;
        final TextView NumberSample, Info;
        final SwipeLayout swipeLayout;
        final ImageView buttonDelete, createBtn;
        final RecyclerView SampleList;

        SimpleViewHolder(@NonNull View itemView) {
            super(itemView);
            NumberSample = itemView.findViewById(R.id.NumberProb);
            SampleList = itemView.findViewById(R.id.ProbFields);
            head = itemView.findViewById(R.id.Header);
            Info = itemView.findViewById(R.id.InfoProb);

            swipeLayout = itemView.findViewById(R.id.swipe);
            buttonDelete = itemView.findViewById(R.id.trash);
            createBtn = itemView.findViewById(R.id.create);

            createBtn.setVisibility(View.GONE);

            head.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (SampleList.getVisibility() == View.VISIBLE) {
                        swipeLayout.setSwipeEnabled(true);
                        SampleList.setVisibility(View.GONE);
                    } else if(swipeLayout.getOpenStatus() == SwipeLayout.Status.Close){
                        swipeLayout.setSwipeEnabled(false);
                        SampleList.setVisibility(View.VISIBLE);
                    }
                }
            });

            buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), "Удаление Образца", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void insertdata(Map<Short, SamplesRest> insertList) {
        SamplesDiffUtilCallback diffUtilCallback = new SamplesDiffUtilCallback(Samples, insertList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtilCallback,false);
        Samples.putAll(insertList);
        diffResult.dispatchUpdatesTo(this);

    }
}
