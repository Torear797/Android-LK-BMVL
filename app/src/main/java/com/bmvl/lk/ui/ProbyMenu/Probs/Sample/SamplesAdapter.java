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
import com.bmvl.lk.Rest.Order.SamplesRest;
import com.bmvl.lk.data.SpacesItemDecoration;
import com.bmvl.lk.ui.Create_Order.Field;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class SamplesAdapter extends RecyclerSwipeAdapter<SamplesAdapter.SimpleViewHolder> {
    private LayoutInflater inflater;
    private TreeMap<Short, SamplesRest> Samples; //Образцы
    private List<Field> ResearchsField; //Поля исследований
    private List<Field> SamplesField; //Поля образцов
    private RecyclerView.RecycledViewPool viewPool;
    private OnSamplesClickListener onSamplesClickListener;

    private String[] Indicators;

    public SamplesAdapter(Context context, List<Field> Researchs, List<Field> Samples, TreeMap<Short, SamplesRest> SamplesList,OnSamplesClickListener Listener, String[] ind) {
        this.inflater = LayoutInflater.from(context);
        ResearchsField = Researchs;
        SamplesField = Samples;
        this.Samples = SamplesList;
        this.onSamplesClickListener = Listener;
        this.Indicators = ind;
    }

    public interface OnSamplesClickListener {
        void onUpdateSamples();
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view1 = inflater.inflate(R.layout.item_prob, parent, false);
        return new SimpleViewHolder(view1);
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder simpleViewHolder, int i) {
        final SamplesRest CurrentSample = Samples.get(getPositionKey(i));

        final SamplesFieldAdapter adapter = new SamplesFieldAdapter(inflater.getContext(), ResearchsField, SamplesField, CurrentSample,Indicators);
        simpleViewHolder.SampleList.setAdapter(adapter);
        simpleViewHolder.SampleList.addItemDecoration(new SpacesItemDecoration((byte) 20, (byte) 0));
        simpleViewHolder.SampleList.setRecycledViewPool(viewPool);

        simpleViewHolder.NumberSample.setText(MessageFormat.format("№ {0}", getPositionKey(i)));
        simpleViewHolder.Info.setText("Образец");

        simpleViewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
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
        if(Samples.size() > 0)
        return new ArrayList<Short>(Samples.keySet()).get(position);
        else return 0;
    }

     class SimpleViewHolder extends RecyclerView.ViewHolder {
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

            head.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (SampleList.getVisibility() == View.VISIBLE)
                        swipeLayout.setSwipeEnabled(true);
                    else if (swipeLayout.getOpenStatus() == SwipeLayout.Status.Close)
                        swipeLayout.setSwipeEnabled(false);
                    SampleList.setVisibility(SampleList.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                }
            });

            buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    swipeLayout.close();
                    onSamplesClickListener.onUpdateSamples();
                    TreeMap<Short, SamplesRest> insertlist = new TreeMap<>(Samples);
                    insertlist.remove(getPositionKey(getLayoutPosition()));
                    updateList(insertlist);
                  //  Toast.makeText(view.getContext(), "Удаление Образца", Toast.LENGTH_SHORT).show();
                }
            });

            createBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    swipeLayout.close();
                    onSamplesClickListener.onUpdateSamples();
                    TreeMap<Short, SamplesRest> insertlist = new TreeMap<>();
                    short newid = getPositionKey(Samples.size() - 1);
                    SamplesRest insertSample = new SamplesRest(newid);

                    insertSample.setData(Samples.get(getPositionKey(getLayoutPosition())).getFields(),Samples.get(getPositionKey(getLayoutPosition())).getResearches());

                    insertlist.put((short)(newid + 1), insertSample);
                    insertdata(insertlist);


                   // Toast.makeText(view.getContext(), "Копирование Образца", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void insertdata(Map<Short, SamplesRest> insertList) {
        SamplesDiffUtilCallback diffUtilCallback = new SamplesDiffUtilCallback(Samples, insertList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtilCallback);
        Samples.putAll(insertList);
        diffResult.dispatchUpdatesTo(this);
    }

    public void updateList(Map<Short, SamplesRest> newList) {
        SamplesDiffUtilCallback diffUtilCallback = new SamplesDiffUtilCallback(Samples, newList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtilCallback);
        Samples.clear();
        Samples.putAll(newList);
        diffResult.dispatchUpdatesTo(this);
    }
}