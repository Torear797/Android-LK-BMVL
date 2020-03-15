package com.bmvl.lk.ui.ProbyMenu.Probs.Sample;

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
import com.bmvl.lk.models.Proby;
import com.bmvl.lk.models.Research;
import com.bmvl.lk.models.Samples;
import com.bmvl.lk.models.SamplesResearch;
import com.bmvl.lk.ui.Create_Order.Field;
import com.bmvl.lk.ui.ProbyMenu.Probs.ProbDiffUtilCallback;
import com.bmvl.lk.ui.ProbyMenu.Probs.ProbsFragment;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import java.text.MessageFormat;
import java.util.List;

public class SamplesAdapter extends RecyclerSwipeAdapter<SamplesAdapter.SimpleViewHolder> {
    private LayoutInflater inflater;

    private  List<SamplesResearch> Researchs;
    private  List<Samples> Samples;

    private static List<Field> ResearchsField;
    private static List<Field> SamplesField;
    private int ProbID;

    public SamplesAdapter(Context context, List<SamplesResearch> ResearchList, List<Field> ResFields, List<Samples> SamList, List<Field> SamFields, int id) {
        this.inflater = LayoutInflater.from(context);
        Researchs = ResearchList;
        ResearchsField = ResFields;
        Samples = SamList;
        SamplesField = SamFields;
        ProbID = id;
    }

    @Override
    public SamplesAdapter.SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view1 = inflater.inflate(R.layout.item_prob, parent, false);
            return new SimpleViewHolder(view1);
    }

    @Override
    public void onBindViewHolder(final SamplesAdapter.SimpleViewHolder simpleViewHolder, int i) {
        final Samples f = Samples.get(i);

        final SamplesFieldAdapter adapter = new SamplesFieldAdapter(inflater.getContext(), Researchs, ResearchsField,Samples,SamplesField , i, ProbID);
        simpleViewHolder.SampleList.setAdapter(adapter);

        simpleViewHolder.NumberSample.setText(MessageFormat.format("№ {0}", i + 1));
        simpleViewHolder.Info.setText("Образец");

        simpleViewHolder.head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (simpleViewHolder.SampleList.getVisibility() == View.VISIBLE) {
                   // simpleViewHolder.swipeLayout.setSwipeEnabled(true);
                    simpleViewHolder.SampleList.setVisibility(View.GONE);
                } else {
                    //simpleViewHolder.swipeLayout.setSwipeEnabled(false);
                    simpleViewHolder.SampleList.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return Samples.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    static class SimpleViewHolder extends RecyclerView.ViewHolder {
        final ConstraintLayout head;
        final TextView NumberSample,Info;
      //  final SwipeLayout swipeLayout;
     //   final ImageView buttonDelete;
        final RecyclerView SampleList;
        SimpleViewHolder(@NonNull View itemView) {
            super(itemView);
            NumberSample = itemView.findViewById(R.id.NumberProb);
            SampleList = itemView.findViewById(R.id.ProbFields);
            head = itemView.findViewById(R.id.Header);
            Info = itemView.findViewById(R.id.InfoProb);

            //swipeLayout = itemView.findViewById(R.id.swipe);
           // buttonDelete = itemView.findViewById(R.id.trash);
        }
    }

    public void insertdata(List<Samples> insertList) {
        SamplesDiffUtilCallback diffUtilCallback = new SamplesDiffUtilCallback(Samples, insertList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtilCallback,false);
        Samples.addAll(insertList);
        ProbsFragment.getSampleList().addAll(insertList);
        diffResult.dispatchUpdatesTo(this);

    }

    public void updateList(List<Samples> newList) {
        SamplesDiffUtilCallback diffUtilCallback = new SamplesDiffUtilCallback(Samples, newList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtilCallback);
        Samples.clear();
        Samples.addAll(newList);
        diffResult.dispatchUpdatesTo(this);
    }
}