package com.bmvl.lk.ui.ProbyMenu.Probs.Sample;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bmvl.lk.R;
import com.bmvl.lk.Rest.Order.ProbyRest;
import com.bmvl.lk.Rest.Order.SamplesRest;
import com.bmvl.lk.Rest.Suggestion;
import com.bmvl.lk.data.Field;
import com.bmvl.lk.data.SpacesItemDecoration;
import com.bmvl.lk.ui.ProbyMenu.Probs.ProbAdapter;
import com.bmvl.lk.ui.ProbyMenu.Probs.ProbFieldAdapter;
import com.bmvl.lk.ui.create_order.CreateOrderActivity;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class SamplesAdapter extends RecyclerSwipeAdapter<SamplesAdapter.SimpleViewHolder> {
    private TreeMap<Short, SamplesRest> Samples; //Образцы
    private List<Field> SamplesField; //Поля образцов
    //  private RecyclerView.RecycledViewPool viewPool;
    private Map<String, String> ProbFields; //Поля пробы
    private SamplesFieldAdapter adapter;
    private ProbyRest CurrentProb;

    public SamplesAdapter(List<Field> Samples, ProbyRest CurrentProb) {
        SamplesField = Samples;
        this.Samples = CurrentProb.getSamples();
        this.ProbFields = CurrentProb.getFields();
        this.CurrentProb = CurrentProb;
        //  viewPool = new RecyclerView.RecycledViewPool();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //  View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_prob, parent, false);
        return new SimpleViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_prob, parent, false));
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder simpleViewHolder, int i) {
        final SamplesRest CurrentSample = Samples.get(getPositionKey(i));
        adapter = new SamplesFieldAdapter(SamplesField, CurrentSample, ProbFields, CurrentProb, Byte.parseByte(String.valueOf(i)));
        simpleViewHolder.SampleList.setAdapter(adapter);

        if (CreateOrderActivity.order_id != 1 && CreateOrderActivity.order_id != 8) {
            simpleViewHolder.NumberSample.setText(MessageFormat.format("№ {0}", i + 1));
            simpleViewHolder.Info.setText("Образец");
        } else {
            simpleViewHolder.GreenHeader.setVisibility(View.GONE);
            simpleViewHolder.SampleList.setVisibility(View.VISIBLE);
            simpleViewHolder.swipeLayout.setSwipeEnabled(false);

            if (!CreateOrderActivity.IsPattern) {
                final GridLayoutManager mng_layout = new GridLayoutManager(simpleViewHolder.SampleList.getContext(), 2);
                mng_layout.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        if (position == 2 || position == 3)
                            return 1;
                        return 2;
                    }
                });
                simpleViewHolder.SampleList.setLayoutManager(mng_layout);
            }
        }

        mItemManger.bindView(simpleViewHolder.itemView, i);
    }

    @Override
    public int getItemCount() {
        return Samples.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    private Short getPositionKey(int position) {
        if (Samples.size() > 0)
            return new ArrayList<>(Samples.keySet()).get(position);
        else return 0;
    }

    class SimpleViewHolder extends RecyclerView.ViewHolder {
        final ConstraintLayout head;
        final LinearLayout GreenHeader;
        final TextView NumberSample, Info;
        final SwipeLayout swipeLayout;
        final ImageView buttonDelete, createBtn;
        final RecyclerView SampleList;
        final ImageView arrow;

        SimpleViewHolder(@NonNull View itemView) {
            super(itemView);
            NumberSample = itemView.findViewById(R.id.NumberProb);
            SampleList = itemView.findViewById(R.id.ProbFields);
            head = itemView.findViewById(R.id.Header);
            Info = itemView.findViewById(R.id.InfoProb);
            GreenHeader = itemView.findViewById(R.id.linearLayout);

            swipeLayout = itemView.findViewById(R.id.swipe);
            buttonDelete = itemView.findViewById(R.id.trash);
            createBtn = itemView.findViewById(R.id.create);
            arrow = itemView.findViewById(R.id.arrow);

            head.setOnClickListener(view -> {
                if (SampleList.getVisibility() == View.VISIBLE)
                    swipeLayout.setSwipeEnabled(true);
                else if (swipeLayout.getOpenStatus() == SwipeLayout.Status.Close)
                    swipeLayout.setSwipeEnabled(false);
                SampleList.setVisibility(SampleList.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                arrow.setImageResource(SampleList.getVisibility() == View.VISIBLE ? R.drawable.ic_w_arrow_ap : R.drawable.ic_w_arrow_down);
            });

            buttonDelete.setOnClickListener(view -> {
                swipeLayout.close();
                closeAllItems();
                TreeMap<Short, SamplesRest> insertlist = new TreeMap<>(Samples);
                insertlist.remove(getPositionKey(getLayoutPosition()));


                if (CurrentProb.getFields().containsKey("144")) {
                    CurrentProb.getFields().put("144", ArrayToString(getLayoutPosition(), Objects.requireNonNull(CurrentProb.getFields().get("144")).split(", ")));
                    ProbAdapter.adapter.notifyItemChanged(ProbFieldAdapter.id_field_144);
                }

                updateList(insertlist);
            });

            createBtn.setOnClickListener(view -> {
                swipeLayout.close();
                closeAllItems();
                TreeMap<Short, SamplesRest> insertlist = new TreeMap<>();
                short newid = getPositionKey(Samples.size() - 1);
                SamplesRest insertSample = new SamplesRest(newid);
                insertSample.setData(Objects.requireNonNull(Samples.get(getPositionKey(getLayoutPosition()))).getFields(), Objects.requireNonNull(Samples.get(getPositionKey(getLayoutPosition()))).getResearches());
                insertlist.put((short) (newid + 1), insertSample);
                insertdata(insertlist);
            });

            SampleList.addItemDecoration(new SpacesItemDecoration((byte) 20, (byte) 5));
            SampleList.setItemAnimator(new DefaultItemAnimator());

            swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);

            // SampleList.setRecycledViewPool(viewPool);
            //SampleList.setHasFixedSize(true);
        }
    }

    private String ArrayToString(int delete_item, String[] mass) {
        StringBuilder string = new StringBuilder();
       // StringBuilder NewItem;
        boolean ItemIsDelete = false;
        for (int i = 0; i < mass.length; i++) {
            if (i != delete_item) {
                if (string.length() > 0) string.append(", ");
                if(ItemIsDelete)
                mass[i] = mass[i].replace("Образец №"+(i+1),"Образец №"+(i));
                string.append(mass[i]);
            } else
                ItemIsDelete = true;
        }
        return string.toString();
    }

    public void UpdateAdapter(List<Suggestion> suggestions, int id) {
        if (adapter != null)
            adapter.UpdateAdapter(suggestions, id);
    }

    public void insertdata(Map<Short, SamplesRest> insertList) {
        SamplesDiffUtilCallback diffUtilCallback = new SamplesDiffUtilCallback(Samples, insertList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtilCallback);
        Samples.putAll(insertList);
        diffResult.dispatchUpdatesTo(this);
    }

    public void updateList(Map<Short, SamplesRest> newList) {
        SamplesDiffUtilCallback diffUtilCallback = new SamplesDiffUtilCallback(Samples, newList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtilCallback);
        Samples.clear();
        Samples.putAll(newList);
        diffResult.dispatchUpdatesTo(this);
    }

    public void AddSample(short newid) {
        Samples.put((short) (newid + 1), new SamplesRest(newid));
        notifyDataSetChanged();
    }
}