package com.bmvl.lk.ui.Create_Order.OrderProbs;

import android.content.Context;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
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
import com.bmvl.lk.ui.ProbyMenu.Probs.ProbDiffUtilCallback;
import com.bmvl.lk.ui.ProbyMenu.Probs.ProbFieldAdapter;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProbAdapter2 extends RecyclerSwipeAdapter<ProbAdapter2.SimpleViewHolder> {
    private static Map<Short, ProbyRest> Probs; //Пробы
    private LayoutInflater inflater;
    private RecyclerView.RecycledViewPool viewPool;

    private List<Field> ProbFields; //Поля пробы
    private List<Field> ResearchFields; //Поля исследований
    private List<Field> SampleFields; //Поля Образцов

    private OnProbClickListener OnProbClickListener; //Слушатель нажатий кнопок

    public ProbAdapter2(Context context, List<Field> Fields, List<Field> ResFields, OnProbClickListener Listener) {
        this.inflater = LayoutInflater.from(context);
        Probs = CreateOrderActivity.order.getProby();
        this.OnProbClickListener = Listener;
        viewPool = new RecyclerView.RecycledViewPool();

        ProbFields = Fields;
        ResearchFields = ResFields;
    }

    public ProbAdapter2(Context context, List<Field> Fields, List<Field> ResFields, List<Field> sampleFields, OnProbClickListener Listener) {
        this.inflater = LayoutInflater.from(context);
        Probs = CreateOrderActivity.order.getProby();
        this.OnProbClickListener = Listener;
        viewPool = new RecyclerView.RecycledViewPool();

        ProbFields = Fields;
        ResearchFields = ResFields;
        SampleFields = sampleFields;
    }

    public interface OnProbClickListener {
        void onDeletedProb();

        void onCopyProb();
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_prob, parent, false);
        return new ProbAdapter2.SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder simpleViewHolder, int i) {
        final ProbyRest CurrentProb = Probs.get(getPositionKey(i));

        initRecyclerView(simpleViewHolder, CurrentProb, getPositionKey(i));

        simpleViewHolder.NameProb.setText(MessageFormat.format("Проба № {0}", getPositionKey(i)));
        String nameMaterial = "не выбран";
        assert CurrentProb != null;
        if (CurrentProb.getFields().containsKey((short) 5))
            nameMaterial = CurrentProb.getFields().get((short) 5);
        simpleViewHolder.infoProb.setText(MessageFormat.format("Вид материала: {0} Образцов: {1}", nameMaterial, CurrentProb.getSamples().size()));
        simpleViewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);

        mItemManger.bindView(simpleViewHolder.itemView, i);
    }

    @Override
    public int getItemCount() {
        return Probs.size();
    }

    private Short getPositionKey(int position) {
        return new ArrayList<Short>(Probs.keySet()).get(position);
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    private void initRecyclerView(SimpleViewHolder simpleViewHolder, ProbyRest CurrentProb, Short sam_id) {
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


        switch (CreateOrderActivity.order_id) {
            case 1:
                final ProbFieldAdapter adapter = new ProbFieldAdapter(
                        inflater.getContext(),
                        ProbFields,
                        ResearchFields,
                        CurrentProb,
                        simpleViewHolder.infoProb
                );
                simpleViewHolder.ProbList.setAdapter(adapter);
                break;
            case 4:
                final ProbFieldAdapter adapter2 = new ProbFieldAdapter(
                        inflater.getContext(),
                        ProbFields,
                        ResearchFields,
                        SampleFields,
                        CurrentProb,
                        simpleViewHolder.infoProb
                );
                simpleViewHolder.ProbList.setAdapter(adapter2);
                break;
        }
    }

    public class SimpleViewHolder extends RecyclerView.ViewHolder {
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
                    OnProbClickListener.onDeletedProb();
                }
            });

            buttonCopy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    OnProbClickListener.onCopyProb();
                }
            });
        }
    }

    public void insertdata(Map<Short, ProbyRest> insertList) {
        ProbDiffUtilCallback diffUtilCallback = new ProbDiffUtilCallback(Probs, insertList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtilCallback);
        Probs.putAll(insertList);
        diffResult.dispatchUpdatesTo(this);
    }
}
