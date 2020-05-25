package com.bmvl.lk.ui.patterns;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bmvl.lk.R;
import com.bmvl.lk.data.models.Pattern;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import java.util.List;

public class PatternAdapter  extends RecyclerSwipeAdapter<PatternAdapter.SimpleViewHolder> {
    private LayoutInflater inflater;
    private static List<Pattern> Patterns;

    PatternAdapter(Context context, List<Pattern> list) {
        this.inflater = LayoutInflater.from(context);
        Patterns = list;
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_pattern, parent, false);
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder simpleViewHolder, int i) {
        final Pattern pattern = Patterns.get(i);
        simpleViewHolder.Name.setText(pattern.getPatternName());
        simpleViewHolder.Type.setText(pattern.getTypePattern());
        simpleViewHolder.Date.setText(pattern.getCreateDate());

        simpleViewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
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
        }
    }
}
