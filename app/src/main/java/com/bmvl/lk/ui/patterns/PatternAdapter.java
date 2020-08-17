package com.bmvl.lk.ui.patterns;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bmvl.lk.R;
import com.bmvl.lk.data.models.Pattern;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class PatternAdapter extends RecyclerSwipeAdapter<PatternAdapter.SimpleViewHolder> {
    private List<Pattern> Patterns;
    private PatternAdapter.OnPatternClickListener onPatternClickListener;
    private boolean standartPattern;

    PatternAdapter(List<Pattern> list, OnPatternClickListener onPatternClickListener,boolean isStandart) {
        this.onPatternClickListener = onPatternClickListener;
        this.Patterns = list;
        this.standartPattern = isStandart;
    }

    public interface OnPatternClickListener {
        void onDeleteOrder(int id, int position, int Status);

        void onCopyOrder(Pattern pattern);

        void onEditOrder(Pattern pattern);

        void onCreateOrder(Pattern pattern);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pattern, parent, false);
        return new SimpleViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pattern, parent, false));
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onBindViewHolder(SimpleViewHolder simpleViewHolder, int i) {
        final Pattern pattern = Patterns.get(i);
        simpleViewHolder.Name.setText(pattern.getPatternName());
        simpleViewHolder.Type.setText(LayoutInflater.from(simpleViewHolder.Type.getContext()).getContext().getResources().getStringArray(R.array.order_name)[pattern.getType_id() - 1]);
        // simpleViewHolder.Date.setText(pattern.getDate());

        try {
            simpleViewHolder.Date.setText(new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Objects.requireNonNull(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(pattern.getDate()))));
        } catch (ParseException e) {
            e.printStackTrace();
        }

//        simpleViewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        simpleViewHolder.swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
                YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.edit));
            }
        });
        mItemManger.bindView(simpleViewHolder.itemView, i);
    }

    @Override
    public int getItemCount() {
        return Patterns.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    public class SimpleViewHolder extends RecyclerView.ViewHolder {
        final TextView Name, Type, Date;
        final SwipeLayout swipeLayout;
        final ImageView buttonDelete, buttonCopy, buttonEdit, buttonCreate;

        public SimpleViewHolder(@NonNull View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.PattertName);
            Type = itemView.findViewById(R.id.TypePattern);
            Date = itemView.findViewById(R.id.Data);
            swipeLayout = itemView.findViewById(R.id.swipe);
            buttonDelete = itemView.findViewById(R.id.delete);
            buttonCopy = itemView.findViewById(R.id.copy);
            buttonEdit = itemView.findViewById(R.id.edit);
            buttonCreate = itemView.findViewById(R.id.create);

            if(!standartPattern)
            buttonDelete.setOnClickListener(view -> {
                closeAllItems();
                onPatternClickListener.onDeleteOrder(Patterns.get(getLayoutPosition()).getId(), getLayoutPosition(), Patterns.get(getLayoutPosition()).getStatus_id());
            });
            else buttonDelete.setVisibility(View.GONE);

            buttonCopy.setOnClickListener(view -> {
                closeAllItems();
                onPatternClickListener.onCopyOrder(Patterns.get(getLayoutPosition()));
            });

            buttonEdit.setOnClickListener(view -> {
                closeAllItems();
                onPatternClickListener.onEditOrder(Patterns.get(getLayoutPosition()));
            });

            buttonCreate.setOnClickListener(view -> {
                closeAllItems();
                onPatternClickListener.onCreateOrder(Patterns.get(getLayoutPosition()));
            });

            swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);

        }
    }

    public void insertdata(List<Pattern> insertList, boolean isCopy) {
        PatternsDiffUtilCallback diffUtilCallback = new PatternsDiffUtilCallback(Patterns, insertList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtilCallback);
        if (isCopy)
            Patterns.addAll(0, insertList);
        else Patterns.addAll(insertList);
        diffResult.dispatchUpdatesTo(this);
    }

    public void updateList(List<Pattern> newList) {
        PatternsDiffUtilCallback diffUtilCallback = new PatternsDiffUtilCallback(Patterns, newList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtilCallback);
        Patterns.clear();
        Patterns.addAll(newList);
        diffResult.dispatchUpdatesTo(this);
    }
}