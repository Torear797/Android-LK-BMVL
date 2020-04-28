package com.bmvl.lk.ui.ProbyMenu.Probs.Research;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bmvl.lk.R;
import com.bmvl.lk.ViewHolders.SpinerHolder;
import com.bmvl.lk.ui.Create_Order.Field;

import java.util.List;

public class ResearchFieldAdapter extends RecyclerView.Adapter {
    private LayoutInflater inflater;
    private List<Field> Researchs;

    public ResearchFieldAdapter(Context context, List<Field> Fields) {
        this.inflater = LayoutInflater.from(context);
        Researchs = Fields;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view1 = inflater.inflate(R.layout.item_spiner, parent, false);
        return new SpinerHolder(view1);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final Field f = Researchs.get(position);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(inflater.getContext(), f.getEntries(), android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ((SpinerHolder) holder).spiner.setAdapter(adapter);
        ((SpinerHolder) holder).txtHint.setText(f.getHint());
    }

    @Override
    public int getItemCount() {
        return Researchs.size();
    }
}