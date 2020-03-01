package com.bmvl.lk.ui.ProbyMenu.Probs;

import android.content.Context;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bmvl.lk.R;
import com.bmvl.lk.models.Proby;
import com.bmvl.lk.ui.Create_Order.Field;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import java.text.MessageFormat;
import java.util.List;

class ProbAdapter extends RecyclerSwipeAdapter<ProbAdapter.SimpleViewHolder> {
    private List<Proby> Probs;
    private static List<Field> ProbFields;
    private LayoutInflater inflater;

    ProbAdapter(Context context, List<Proby> Contents,List<Field> Fields) {
        this.Probs = Contents;
        this.inflater = LayoutInflater.from(context);
        ProbFields = Fields;
    }

    @NonNull
    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_prob, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SimpleViewHolder simpleViewHolder, int i) {
        //Proby Prob = Probs.get(i);

        final ProbFieldAdapter adapter = new ProbFieldAdapter(inflater.getContext(), ProbFields);
        simpleViewHolder.ProbList.setAdapter(adapter);

        simpleViewHolder.NameProb.setText(MessageFormat.format("Проба № {0}", i+1));
        //simpleViewHolder.NameProb.setText(MessageFormat.format("Проба № {0}", ProbFields.size()));

        simpleViewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        simpleViewHolder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Deleted !", Toast.LENGTH_SHORT).show();
            }
        });

        simpleViewHolder.head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(simpleViewHolder.ProbList.getVisibility() == View.VISIBLE)
                    simpleViewHolder.ProbList.setVisibility(View.GONE);
                else simpleViewHolder.ProbList.setVisibility(View.VISIBLE);
            }
        });


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
        }
    }

    public void insertdata(List<Proby> insertList){
        ProbDiffUtilCallback diffUtilCallback = new ProbDiffUtilCallback(Probs, insertList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtilCallback);
        Probs.addAll(insertList);
        diffResult.dispatchUpdatesTo(this);

    }
    public void updateList(List<Proby> newList){
        ProbDiffUtilCallback diffUtilCallback = new ProbDiffUtilCallback(Probs,newList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtilCallback);
        Probs.clear();
        Probs.addAll(newList);
        diffResult.dispatchUpdatesTo(this);
    }
}