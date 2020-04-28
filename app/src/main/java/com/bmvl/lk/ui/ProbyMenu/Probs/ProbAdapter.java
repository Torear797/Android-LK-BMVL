package com.bmvl.lk.ui.ProbyMenu.Probs;

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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bmvl.lk.R;
import com.bmvl.lk.data.SpacesItemDecoration;
import com.bmvl.lk.data.models.Proby;
import com.bmvl.lk.data.models.Research;
import com.bmvl.lk.ui.Create_Order.Field;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import java.text.MessageFormat;
import java.util.List;

public class ProbAdapter extends RecyclerSwipeAdapter<ProbAdapter.SimpleViewHolder> {
    private static List<Proby> Probs;
    private static List<Field> ProbFields;
    private static List<Field> ResearchFields;
    private static List<Field> SampleFields; //Поля Образцов
    private static List<List<Research>> Researchs; //Исследования
    private LayoutInflater inflater;

    private RecyclerView.RecycledViewPool viewPool;

    public ProbAdapter(Context context, List<Proby> Contents, List<Field> Fields, List<Field> ResFields) {
        this.inflater = LayoutInflater.from(context);
        Probs = Contents;
        ProbFields = Fields;
        ResearchFields = ResFields;
      //  Researchs = ProbsFragment.getResearchsList();

        viewPool = new RecyclerView.RecycledViewPool();
    }

    ProbAdapter(Context context, List<Proby> Contents, List<Field> Fields, List<Field> ResFields, List<Field> SamFields) {
        this.inflater = LayoutInflater.from(context);
        Probs = Contents;
        ProbFields = Fields;
        ResearchFields = ResFields;
        SampleFields = SamFields;

        viewPool = new RecyclerView.RecycledViewPool();
    }

    @NonNull
    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_prob, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SimpleViewHolder simpleViewHolder, int i) {
        final Proby Prob = Probs.get(i);

        final GridLayoutManager mng_layout = new GridLayoutManager(inflater.getContext(), 2);
        mng_layout.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (Prob.getOrder_id() == 0 && (position >= 4 && position <= 7 || position == 22 || position == 23))
                    return 1;
                if (Prob.getOrder_id() == 1 && (position >= 4 && position <= 7))
                    return 1;
                return 2;
            }
        });
        simpleViewHolder.ProbList.setLayoutManager(mng_layout);
        simpleViewHolder.ProbList.addItemDecoration(new SpacesItemDecoration((byte) 20, (byte) 15));
        simpleViewHolder.ProbList.setRecycledViewPool(viewPool);

        if (Prob.getOrder_id() == 0) {
           // final ProbFieldAdapter adapter = new ProbFieldAdapter(inflater.getContext(), ProbFields, ResearchFields, i);
        //    simpleViewHolder.ProbList.setAdapter(adapter);
        } else if (Prob.getOrder_id() == 1) {
            final ProbFieldAdapter adapter = new ProbFieldAdapter(inflater.getContext(), ProbFields, ResearchFields, SampleFields, i);
            simpleViewHolder.ProbList.setAdapter(adapter);
        }

        simpleViewHolder.NameProb.setText(MessageFormat.format("Проба № {0}", Prob.getId()));
        simpleViewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);

        mItemManger.bindView(simpleViewHolder.itemView, i);
    }

    @Override
    public int getItemCount() {
        return Probs.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    static class SimpleViewHolder extends RecyclerView.ViewHolder {
        final ConstraintLayout head;
        final TextView NameProb;
        final SwipeLayout swipeLayout;
        final ImageView buttonDelete;
        final RecyclerView ProbList;

        SimpleViewHolder(@NonNull View itemView) {
            super(itemView);
            NameProb = itemView.findViewById(R.id.NumberProb);
            ProbList = itemView.findViewById(R.id.ProbFields);
            head = itemView.findViewById(R.id.Header);

            swipeLayout = itemView.findViewById(R.id.swipe);
            buttonDelete = itemView.findViewById(R.id.trash);

            head.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (ProbList.getVisibility() == View.VISIBLE) {
                        swipeLayout.setSwipeEnabled(true);
                        ProbList.setVisibility(View.GONE);
                    } else if(swipeLayout.getOpenStatus() == SwipeLayout.Status.Close){
                        swipeLayout.setSwipeEnabled(false);
                        ProbList.setVisibility(View.VISIBLE);
                    }
                }
            });

           buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), "Удаление пробы", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void insertdata(List<Proby> insertList) {
//        ProbDiffUtilCallback diffUtilCallback = new ProbDiffUtilCallback(Probs, insertList);
//        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtilCallback,false);
//        Probs.addAll(insertList);
//        diffResult.dispatchUpdatesTo(this);

    }

    public void updateList(List<Proby> newList) {
//        ProbDiffUtilCallback diffUtilCallback = new ProbDiffUtilCallback(Probs, newList);
//        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtilCallback);
//        Probs.clear();
//        Probs.addAll(newList);
//        diffResult.dispatchUpdatesTo(this);
    }
}