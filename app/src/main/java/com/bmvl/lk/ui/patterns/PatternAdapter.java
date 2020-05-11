package com.bmvl.lk.ui.patterns;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

public class PatternAdapter  extends RecyclerSwipeAdapter<PatternAdapter.SimpleViewHolder> {
    private LayoutInflater inflater;

    PatternAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder simpleViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return 0;
    }

    public class SimpleViewHolder extends RecyclerView.ViewHolder {
        public SimpleViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
