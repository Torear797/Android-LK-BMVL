package com.bmvl.lk.ui.ProbyMenu.Probs;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bmvl.lk.R;
import com.bmvl.lk.Rest.Order.ProbyRest;
import com.bmvl.lk.data.Field;
import com.bmvl.lk.data.SpacesItemDecoration;
import com.bmvl.lk.ui.create_order.CreateOrderActivity;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ProbAdapter extends RecyclerSwipeAdapter<ProbAdapter.SimpleViewHolder> {
    private static Map<Short, ProbyRest> Probs; //Пробы
    private RecyclerView.RecycledViewPool viewPool;

    private List<Field> ProbFields; //Поля пробы
    private List<Field> SampleFields; //Поля Образцов

    private OnProbClickListener OnProbClickListener; //Слушатель нажатий кнопок
    public static ProbFieldAdapter adapter;

    ProbAdapter(List<Field> Fields, List<Field> sampleFields, OnProbClickListener Listener) {
        Probs = CreateOrderActivity.order.getProby();
        this.OnProbClickListener = Listener;
        viewPool = new RecyclerView.RecycledViewPool();
        ProbFields = Fields;
        SampleFields = sampleFields;
    }

    public interface OnProbClickListener {
        void onDeletedProb(short id);

        void onDownloadProtocol(String adres, short id);
    }

    @NonNull
    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_prob, parent, false);
        return new ProbAdapter.SimpleViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull SimpleViewHolder simpleViewHolder, int i) {
        final ProbyRest CurrentProb = Probs.get(getPositionKey(i));

        if(simpleViewHolder.ProbList.getAdapter() == null) //под вопросом. Будут проблемы - убрать.
        initRecyclerView(simpleViewHolder, CurrentProb);

        simpleViewHolder.NameProb.setText(MessageFormat.format("Проба № {0}", i + 1));

        assert CurrentProb != null;
        if (CurrentProb.getProtocol() != null)
            simpleViewHolder.btnDownload.setVisibility(View.VISIBLE);

        String nameMaterial = simpleViewHolder.infoProb.getContext().getString(R.string.MaterialNoSelectHeader);
        if (CurrentProb.getFields().containsKey("5"))
            nameMaterial = CurrentProb.getFields().get("materialName");

        simpleViewHolder.infoProb.setText(MessageFormat.format("Вид материала: {0} Образцов: {1}", nameMaterial, CurrentProb.getSamples().size()));
      //  simpleViewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);

        mItemManger.bindView(simpleViewHolder.itemView, i);
    }

    @Override
    public int getItemCount() {
        return Probs.size();
    }

    private Short getPositionKey(int position) {
        if (Probs.size() > 0)
            return new ArrayList<>(Probs.keySet()).get(position);
        else return 0;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    private void initRecyclerView(SimpleViewHolder simpleViewHolder, ProbyRest CurrentProb) {
        adapter = new ProbFieldAdapter(
                ProbFields,
                SampleFields,
                CurrentProb,
                simpleViewHolder.infoProb
        );
        simpleViewHolder.ProbList.setAdapter(adapter);
    }

    class SimpleViewHolder extends RecyclerView.ViewHolder {
        final ConstraintLayout head;
        final TextView NameProb, infoProb;
        final SwipeLayout swipeLayout;
        final ImageView buttonDelete, buttonCopy, btnDownload;
        final RecyclerView ProbList;
        final ImageView arrow;

        SimpleViewHolder(@NonNull View itemView) {
            super(itemView);
            NameProb = itemView.findViewById(R.id.NumberProb);
            infoProb = itemView.findViewById(R.id.InfoProb);
            ProbList = itemView.findViewById(R.id.ProbFields);
            head = itemView.findViewById(R.id.Header);

            swipeLayout = itemView.findViewById(R.id.swipe);
            buttonDelete = itemView.findViewById(R.id.trash);
            buttonCopy = itemView.findViewById(R.id.create);
            btnDownload = itemView.findViewById(R.id.download);
            arrow = itemView.findViewById(R.id.arrow);

            head.setOnClickListener(view -> {
                if (ProbList.getVisibility() == View.VISIBLE)
                    swipeLayout.setSwipeEnabled(true);
                else if (swipeLayout.getOpenStatus() == SwipeLayout.Status.Close)
                    swipeLayout.setSwipeEnabled(false);

                ProbList.setVisibility(ProbList.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                arrow.setImageResource(ProbList.getVisibility() == View.VISIBLE ? R.drawable.ic_w_arrow_ap : R.drawable.ic_w_arrow_down);
            });

            buttonDelete.setOnClickListener(view -> {
                swipeLayout.close();
                closeAllItems();
                OnProbClickListener.onDeletedProb(getPositionKey(getLayoutPosition()));
            });

            btnDownload.setOnClickListener(view -> {
                swipeLayout.close();
                closeAllItems();
                OnProbClickListener.onDownloadProtocol(Objects.requireNonNull(Probs.get(getPositionKey(getLayoutPosition()))).getProtocol(), Objects.requireNonNull(Probs.get(getPositionKey(getLayoutPosition()))).getId());
            });

            buttonCopy.setVisibility(View.GONE);

            ProbList.addItemDecoration(new SpacesItemDecoration((byte) 20, (byte) 5));
            ProbList.setItemAnimator(new DefaultItemAnimator());
            ProbList.setRecycledViewPool(viewPool);
           // ProbList.setHasFixedSize(true);
            final GridLayoutManager mng_layout = new GridLayoutManager(ProbList.getContext(), 2);
            mng_layout.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (position >= 4 && position <= 7) return 1;

                    if(!CreateOrderActivity.IsPattern) {
                        if ((CreateOrderActivity.order_id == 3) && (position == 22 || position == 23))
                            return 1;
                    } else if ((CreateOrderActivity.order_id == 1 || CreateOrderActivity.order_id == 3) && (position == 21 || position == 22))
                        return 1;

                    return 2;
                }
            });

            ProbList.setLayoutManager(mng_layout);
            swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
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