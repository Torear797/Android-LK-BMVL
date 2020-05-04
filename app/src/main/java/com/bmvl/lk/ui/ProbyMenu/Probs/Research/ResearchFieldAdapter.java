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

    private String[] Indicators;
    private String[] Methods;
    private String[] Types;

    public ResearchFieldAdapter(Context context, List<Field> Fields, String[] ind) {
        this.inflater = LayoutInflater.from(context);
        Researchs = Fields;
        this.Indicators = ind;
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

       // String[] cities = {"Москва", "Самара", "Вологда", "Волгоград", "Саратов", "Воронеж"};
       // ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(inflater.getContext(), f.getEntries(), android.R.layout.simple_spinner_item);

        switch (f.getColumn_id()){
            case 0:
                InitAdapter(Indicators, holder);
                break;
            case 1:
                InitAdapter(Methods, holder);
                break;
            case 2:
                InitAdapter(Types, holder);
                break;
        }



        ((SpinerHolder) holder).txtHint.setText(f.getHint());
    }
    private void InitAdapter(String[] mass, RecyclerView.ViewHolder holder ){
        if(mass != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(inflater.getContext(), android.R.layout.simple_spinner_item, Indicators);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            ((SpinerHolder) holder).spiner.setAdapter(adapter);
        }
    }

    @Override
    public int getItemCount() {
        return Researchs.size();
    }
}