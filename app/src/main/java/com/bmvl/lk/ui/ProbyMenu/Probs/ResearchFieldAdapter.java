package com.bmvl.lk.ui.ProbyMenu.Probs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bmvl.lk.R;
import com.bmvl.lk.ui.Create_Order.Field;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

class ResearchFieldAdapter extends RecyclerView.Adapter{
    private LayoutInflater inflater;
    private static List<Field> Researchs;
    public ResearchFieldAdapter(Context context, List<Field> Fields) {
        this.inflater = LayoutInflater.from(context);
        Researchs = Fields;
    }
//    @Override
//    public int getItemViewType(int position) {
//        return Researchs.get(position).getType();
//    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        switch (viewType) {
//            case 0:
//                View view = inflater.inflate(R.layout.item_field, parent, false);
//                return new ViewHolder(view);
//            case 1:
//                View view1 = inflater.inflate(R.layout.item_spiner, parent, false);
//                return new ViewHolderSpiner(view1);
//        }
        View view1 = inflater.inflate(R.layout.item_spiner, parent, false);
        return new ViewHolderSpiner(view1);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final Field f = Researchs.get(position);
//        switch (f.getType()){
//            case 0:
//                ((ViewHolder) holder).Layout.setHint(f.getHint());
//                ((ViewHolder) holder).field.setInputType(f.getInputType());
//                ((ViewHolder) holder).field.setText(f.getValue());
//                ((ViewHolder) holder).Layout.setBoxBackgroundColor(inflater.getContext().getResources().getColor(R.color.field_inactive));
//                break;
//            case 1:
//                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(inflater.getContext(), f.getEntries(), android.R.layout.simple_spinner_item);
//                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                ((ViewHolderSpiner) holder).spiner.setAdapter(adapter);
//                ((ViewHolderSpiner) holder).txtHint.setText(f.getHint());
//                break;
//        }
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(inflater.getContext(), f.getEntries(), android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ((ViewHolderSpiner) holder).spiner.setAdapter(adapter);
        ((ViewHolderSpiner) holder).txtHint.setText(f.getHint());
    }

    @Override
    public int getItemCount() {
        return Researchs.size();
    }

    private static class ViewHolderSpiner extends RecyclerView.ViewHolder {
        final Spinner spiner;
        final TextView txtHint;
        ViewHolderSpiner(View view) {
            super(view);
            spiner = itemView.findViewById(R.id.spinner);
            txtHint = itemView.findViewById(R.id.hint);
        }
    }

//    private static class ViewHolder extends RecyclerView.ViewHolder {
//        final TextInputEditText field;
//        final TextInputLayout Layout;
//        ViewHolder(View view) {
//            super(view);
//            field = itemView.findViewById(R.id.TextInput);
//            Layout = itemView.findViewById(R.id.TextLayout);
//        }
//    }
}
