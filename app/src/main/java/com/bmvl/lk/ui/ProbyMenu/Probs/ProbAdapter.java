package com.bmvl.lk.ui.ProbyMenu.Probs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bmvl.lk.R;
import com.bmvl.lk.Rest.Order.ProbyRest;
import com.bmvl.lk.data.SpacesItemDecoration;
import com.bmvl.lk.ui.Create_Order.CreateOrderActivity;
import com.bmvl.lk.ui.Create_Order.Field;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProbAdapter extends RecyclerSwipeAdapter<ProbAdapter.SimpleViewHolder> {
    private static Map<Short, ProbyRest> Probs; //Пробы
    private LayoutInflater inflater;
    private RecyclerView.RecycledViewPool viewPool;

    private List<Field> ProbFields; //Поля пробы
    private List<Field> SampleFields; //Поля Образцов

    private OnProbClickListener OnProbClickListener; //Слушатель нажатий кнопок
   // private GridLayoutManager mng_layout;

    public ProbAdapter(Context context, List<Field> Fields, OnProbClickListener Listener) {
        this.inflater = LayoutInflater.from(context);
        Probs = CreateOrderActivity.order.getProby();
        this.OnProbClickListener = Listener;
        viewPool = new RecyclerView.RecycledViewPool();

        ProbFields = Fields;
       // ResearchFields = ResFields;
    }

    public ProbAdapter(Context context, List<Field> Fields, List<Field> sampleFields, OnProbClickListener Listener) {
        this.inflater = LayoutInflater.from(context);
        Probs = CreateOrderActivity.order.getProby();
        this.OnProbClickListener = Listener;
        viewPool = new RecyclerView.RecycledViewPool();

        ProbFields = Fields;
       // ResearchFields = ResFields;
        SampleFields = sampleFields;
    }

    public interface OnProbClickListener {
        void onDeletedProb(short id);

        void onCopyProb();
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view = inflater.inflate(R.layout.item_prob, parent, false);
        return new ProbAdapter.SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder simpleViewHolder, int i) {
        final ProbyRest CurrentProb = Probs.get(getPositionKey(i));

        initRecyclerView(simpleViewHolder, CurrentProb);

        simpleViewHolder.NameProb.setText(MessageFormat.format("Проба № {0}", i+1));

        String nameMaterial = "Молоко сырое";
        assert CurrentProb != null;
        if (CurrentProb.getFields().containsKey("5"))
            nameMaterial = CurrentProb.getFields().get("materialName");

        simpleViewHolder.infoProb.setText(MessageFormat.format("Вид материала: {0} Образцов: {1}", nameMaterial, CurrentProb.getSamples().size()));
        simpleViewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);

        mItemManger.bindView(simpleViewHolder.itemView, i);
    }

    @Override
    public int getItemCount() {
        return Probs.size();
    }

    private Short getPositionKey(int position) {
        if (Probs.size() > 0)
            return new ArrayList<Short>(Probs.keySet()).get(position);
        else return 0;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    private void initRecyclerView(SimpleViewHolder simpleViewHolder, ProbyRest CurrentProb) {

        final GridLayoutManager mng_layout = new GridLayoutManager(inflater.getContext(), 2);
        mng_layout.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (CreateOrderActivity.order_id == 1 && (position >= 4 && position <= 7 || position == 22 || position == 23))
                    return 1;
                if (CreateOrderActivity.order_id == 4 && (position >= 4 && position <= 7))
                    return 1;
                return 2;
            }
        });

        simpleViewHolder.ProbList.setLayoutManager(mng_layout);
        simpleViewHolder.ProbList.addItemDecoration(new SpacesItemDecoration((byte) 20, (byte) 5));
        simpleViewHolder.ProbList.setRecycledViewPool(viewPool);

        final ProbFieldAdapter adapter2 = new ProbFieldAdapter(
                inflater.getContext(),
                ProbFields,
                SampleFields,
                CurrentProb,
                simpleViewHolder.infoProb
        );
        simpleViewHolder.ProbList.setAdapter(adapter2);
//
//        switch (CreateOrderActivity.order_id) {
//            case 1:
//                final ProbFieldAdapter adapter = new ProbFieldAdapter(
//                        inflater.getContext(),
//                        ProbFields,
//                        CurrentProb,
//                        simpleViewHolder.infoProb
//                );
//                simpleViewHolder.ProbList.setAdapter(adapter);
//                break;
//            case 4:
//                final ProbFieldAdapter adapter2 = new ProbFieldAdapter(
//                        inflater.getContext(),
//                        ProbFields,
//                        SampleFields,
//                        CurrentProb,
//                        simpleViewHolder.infoProb
//                );
//                simpleViewHolder.ProbList.setAdapter(adapter2);
//                break;
//        }
    }

    class SimpleViewHolder extends RecyclerView.ViewHolder {
        final ConstraintLayout head;
        final TextView NameProb, infoProb;
        final SwipeLayout swipeLayout;
        final ImageView buttonDelete, buttonCopy;
        final RecyclerView ProbList;

        SimpleViewHolder(@NonNull View itemView) {
            super(itemView);
            NameProb = itemView.findViewById(R.id.NumberProb);
            infoProb = itemView.findViewById(R.id.InfoProb);
            ProbList = itemView.findViewById(R.id.ProbFields);
            head = itemView.findViewById(R.id.Header);

            swipeLayout = itemView.findViewById(R.id.swipe);
            buttonDelete = itemView.findViewById(R.id.trash);
            buttonCopy = itemView.findViewById(R.id.create);

            head.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (ProbList.getVisibility() == View.VISIBLE)
                        swipeLayout.setSwipeEnabled(true);
                    else if (swipeLayout.getOpenStatus() == SwipeLayout.Status.Close)
                        swipeLayout.setSwipeEnabled(false);

                    ProbList.setVisibility(ProbList.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                }
            });

            buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    swipeLayout.close();
                    OnProbClickListener.onDeletedProb(getPositionKey(getLayoutPosition()));
                }
            });

//            buttonCopy.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    OnProbClickListener.onCopyProb();
//                }
//            });
            buttonCopy.setVisibility(View.GONE);
        }
    }

    public void insertdata(Map<Short, ProbyRest> insertList) {
        ProbDiffUtilCallback diffUtilCallback = new ProbDiffUtilCallback(Probs, insertList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtilCallback);
        Probs.putAll(insertList);
        diffResult.dispatchUpdatesTo(this);
    }

    public void updateList(Map<Short, ProbyRest> newList) {
        ProbDiffUtilCallback diffUtilCallback = new ProbDiffUtilCallback(Probs, newList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtilCallback);
        Probs.clear();
        Probs.putAll(newList);
        diffResult.dispatchUpdatesTo(this);
    }
}