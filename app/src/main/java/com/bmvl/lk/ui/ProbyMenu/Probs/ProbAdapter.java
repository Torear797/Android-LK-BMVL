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
import com.bmvl.lk.models.Proby;
import com.bmvl.lk.ui.Create_Order.Field;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import java.text.MessageFormat;
import java.util.List;

class ProbAdapter extends RecyclerSwipeAdapter<ProbAdapter.SimpleViewHolder> {
    private static List<Proby> Probs;
    private static List<Field> ProbFields;
    private static List<Field> ResearchFields;
    private LayoutInflater inflater;

    ProbAdapter(Context context, List<Proby> Contents, List<Field> Fields, List<Field> ResFields) {
        this.inflater = LayoutInflater.from(context);
        Probs = Contents;
        ProbFields = Fields;
        ResearchFields = ResFields;
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
                // if (Prob.getOrder_id() == 1 && (position == 1 || position == 2 || position == 13 || position == 14))
                //   return 1;
                return 2;
            }
        });
        simpleViewHolder.ProbList.setLayoutManager(mng_layout);

        final ProbFieldAdapter adapter = new ProbFieldAdapter(inflater.getContext(), ProbFields, ResearchFields);
        simpleViewHolder.ProbList.setAdapter(adapter);

        simpleViewHolder.NameProb.setText(MessageFormat.format("Проба № {0}", i + 1));


        simpleViewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        simpleViewHolder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Deleted !", Toast.LENGTH_SHORT).show();
            }
        });
        mItemManger.bindView(simpleViewHolder.itemView, i);

        simpleViewHolder.head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (simpleViewHolder.ProbList.getVisibility() == View.VISIBLE) {
                    simpleViewHolder.swipeLayout.setSwipeEnabled(true);
                    simpleViewHolder.ProbList.setVisibility(View.GONE);
                } else {
                    simpleViewHolder.swipeLayout.setSwipeEnabled(false);
                    simpleViewHolder.ProbList.setVisibility(View.VISIBLE);
                }
            }
        });
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
        }
    }

    public void insertdata(List<Proby> insertList) {
        ProbDiffUtilCallback diffUtilCallback = new ProbDiffUtilCallback(Probs, insertList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtilCallback);
        Probs.addAll(insertList);
        diffResult.dispatchUpdatesTo(this);

    }

    public void updateList(List<Proby> newList) {
        ProbDiffUtilCallback diffUtilCallback = new ProbDiffUtilCallback(Probs, newList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtilCallback);
        Probs.clear();
        Probs.addAll(newList);
        diffResult.dispatchUpdatesTo(this);
    }
}