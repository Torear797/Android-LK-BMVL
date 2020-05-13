package com.bmvl.lk.ui.ProbyMenu.Probs.Research;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bmvl.lk.App;
import com.bmvl.lk.R;
import com.bmvl.lk.Rest.AnswerMethods;
import com.bmvl.lk.Rest.NetworkService;
import com.bmvl.lk.Rest.Order.ResearchRest;
import com.bmvl.lk.Rest.Suggestion;
import com.bmvl.lk.Rest.SuggestionMethod;
import com.bmvl.lk.ViewHolders.SpinerHolder;
import com.bmvl.lk.data.SpacesItemDecoration;
import com.bmvl.lk.ui.Create_Order.Field;
import com.bmvl.lk.ui.ProbyMenu.Probs.ProbFieldAdapter;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResearhAdapter extends RecyclerSwipeAdapter<ResearhAdapter.ResearchItemHolder> {
    private LayoutInflater inflater;
    private TreeMap<Short, ResearchRest> researches; //Исследования
    private RecyclerView.RecycledViewPool viewPool;
    private OnResearchClickListener onResearchClickListener;

    private String[] Methods, Types, Indicators;
    private List<Suggestion> suggestions;
    private List<SuggestionMethod> suggestionsMethods;
    private short materialId;
    private short posInd = -1, posMet = -1, posType = -1;

    public ResearhAdapter(Context context, TreeMap<Short, ResearchRest> ResearchesLise, OnResearchClickListener Listener) {
        this.inflater = LayoutInflater.from(context);
        this.onResearchClickListener = Listener;
        researches = ResearchesLise;
    }

    public interface OnResearchClickListener {
        void onUpdateResearch();
    }

    @NonNull
    @Override
    public ResearchItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_research, parent, false);
        return new ResearchItemHolder(view);
    }

    @Override
    public void onBindViewHolder(ResearchItemHolder researchItemHolder, int i) {
        final ResearchRest CurrentResearch = researches.get(getPositionKey(i));
        // final ResearchFieldAdapter adapter = new ResearchFieldAdapter(inflater.getContext(), ResearchField, Indicators);
        // researchItemHolder.ResearchList.setAdapter(adapter);
        //  researchItemHolder.ResearchList.addItemDecoration(new SpacesItemDecoration((byte) 20, (byte) 15));
        // researchItemHolder.ResearchList.setRecycledViewPool(viewPool);

        researchItemHolder.NumberResearch.setText(MessageFormat.format("№ {0}", i + 1));

        if (CurrentResearch.getTypeId() != 0 &&
                        CurrentResearch.getTypeVal() != null && !CurrentResearch.getTypeVal().equals("")) {
//            Indicators = new String[1];
//            Methods = new String[1];
//            Types = new String[1];
//
//            Indicators[0] = CurrentResearch.getIndicatorVal();
//            Methods[0] = CurrentResearch.getMethodVal();
//            Types[0] = CurrentResearch.getTypeVal();
//
//            InitAdapter(Indicators, researchItemHolder.Indicators);
//            InitAdapter(Methods, researchItemHolder.Methods);
//            InitAdapter(Types, researchItemHolder.Types);
            if(posInd == -1 || posMet == -1 || posType == -1)
                getPositionForSpiners(CurrentResearch.getIndicatorVal(), CurrentResearch.getMethodVal(), CurrentResearch.getTypeVal());

            researchItemHolder.Indicators.setSelection(posInd);
            researchItemHolder.Methods.setSelection(posMet);
            researchItemHolder.Types.setSelection(posType);
        } else {
            if(CurrentResearch.getMethodVal() != null && !CurrentResearch.getMethodVal().equals("")){

                    researchItemHolder.Indicators.setSelection(posInd);
                    researchItemHolder.Methods.setSelection(posMet);

            } else if(CurrentResearch.getIndicatorVal() != null && !CurrentResearch.getIndicatorVal().equals("")){
                researchItemHolder.Indicators.setSelection(posInd);

                if(Methods != null)
                    InitAdapter(Methods, researchItemHolder.Methods);

                if(Types != null)
                    InitAdapter(Types, researchItemHolder.Types);
            } else
                InitAdapter(Indicators, researchItemHolder.Indicators);

        }


        researchItemHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        mItemManger.bindView(researchItemHolder.itemView, i);
    }

    private void getPositionForSpiners(String indicator, String Method, String Type){
        int position = 0;
        if(posInd == -1)
        for (String name: Indicators) {
            if(name.equals(Indicators[position])) {
                posInd = (short) position;
                break;
            }
        }


    }

    public void UpdateIndicators(String[] ind, List<Suggestion> sug, int id) {
        Indicators = ind;
        suggestions = sug;
        materialId = (short) id;
    }

    private void setTestResearchData(ResearchRest CurrentResearch) {

        CurrentResearch.setIndicatorId((short) 499);
        CurrentResearch.setIndicatorVal("Консистенция");
        CurrentResearch.setIndicatorNd("ГОСТ Р\\n52054-2003");
        CurrentResearch.setIndicatorNdId((short) 682);


        CurrentResearch.setMethodId((short) 53);
        CurrentResearch.setMethodVal("органолептический");
        CurrentResearch.setMethodNd("ГОСТ Р\\n52054-2003");
        CurrentResearch.setMethodNdId((short) 682);

        CurrentResearch.setTypeId((short) 3);

        CurrentResearch.setTypeVal("Классика");
    }

    private void InitAdapter(String[] mass, Spinner spiner) {
        if (mass != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(inflater.getContext(), android.R.layout.simple_spinner_item, mass);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spiner.setAdapter(adapter);
        }
    }

    private Short getPositionKey(int position) {
        if (researches.size() > 0)
            return new ArrayList<Short>(researches.keySet()).get(position);
        else return 0;
    }

    @Override
    public int getItemCount() {
        return researches.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    class ResearchItemHolder extends RecyclerView.ViewHolder {
        final ConstraintLayout HeaderResearch, Content;
        final TextView NumberResearch;
        final SwipeLayout swipeLayout;
        final ImageView buttonDelete, ignorBtn;
        final Spinner Indicators, Methods, Types;

        ResearchItemHolder(@NonNull View itemView) {
            super(itemView);
            HeaderResearch = itemView.findViewById(R.id.Header);
            NumberResearch = itemView.findViewById(R.id.NumberProb);
            Content = itemView.findViewById(R.id.Content);

            swipeLayout = itemView.findViewById(R.id.swipe);
            buttonDelete = itemView.findViewById(R.id.trash);
            ignorBtn = itemView.findViewById(R.id.create);

            ignorBtn.setVisibility(View.GONE);

            //Spiners
            Indicators = itemView.findViewById(R.id.Indicator);
            Methods = itemView.findViewById(R.id.Method);
            Types = itemView.findViewById(R.id.Type);

            HeaderResearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Content.getVisibility() == View.VISIBLE)
                        swipeLayout.setSwipeEnabled(true);
                    else if (swipeLayout.getOpenStatus() == SwipeLayout.Status.Close)
                        swipeLayout.setSwipeEnabled(false);
                    Content.setVisibility(Content.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                }
            });

            buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    swipeLayout.close();
                    onResearchClickListener.onUpdateResearch();
                    TreeMap<Short, ResearchRest> insertlist = new TreeMap<>(researches);
                    insertlist.remove(getPositionKey(getLayoutPosition()));
                    updateList(insertlist);
                    // Toast.makeText(view.getContext(), "Удаление ииследования", Toast.LENGTH_SHORT).show();
                }
            });


            AdapterView.OnItemSelectedListener IndicatorsSelectedListener = new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    final ResearchRest CurrentReseacrh = researches.get(getPositionKey(getLayoutPosition()));
                    final Suggestion CurrentItem = suggestions.get(position);

                    if (CurrentReseacrh.getIndicatorId() != (short) CurrentItem.getId()) {
                        posInd = (short) position;
                        CurrentReseacrh.ClearAll();

                        CurrentReseacrh.setIndicatorId((short) CurrentItem.getId());
                        CurrentReseacrh.setIndicatorVal(String.valueOf(parent.getItemAtPosition(position)));
                        CurrentReseacrh.setIndicatorNd(CurrentItem.getName_document());
                        CurrentReseacrh.setIndicatorNdId(CurrentItem.getId_document());

                        getMethods((short) CurrentItem.getId(), String.valueOf(CurrentItem.getId_document()));
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            };
            Indicators.setOnItemSelectedListener(IndicatorsSelectedListener);
        }
    }

    private void getMethods(short indicatorId, String indicatorNdId) {
        if(indicatorNdId.equals("0")) indicatorNdId = "";
        NetworkService.getInstance()
                .getJSONApi()
                .getMethods(App.UserAccessData.getToken(), "", materialId, indicatorId, indicatorNdId)
                .enqueue(new Callback<AnswerMethods>() {
                    @Override
                    public void onResponse(@NonNull Call<AnswerMethods> call, @NonNull Response<AnswerMethods> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Methods = new String[response.body().getSuggestions().size()];
                            for (int i = 0; i < response.body().getSuggestions().size(); i++) {
                                Methods[i] = response.body().getSuggestions().get(i).getValue();
                            }
                            suggestionsMethods = response.body().getSuggestions();
                            notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<AnswerMethods> call, @NonNull Throwable t) {
                    }
                });
    }

    public void insertdata(Map<Short, ResearchRest> insertList) {
        ResearchDiffUtilCallback diffUtilCallback = new ResearchDiffUtilCallback(researches, insertList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtilCallback);
        researches.putAll(insertList);
        diffResult.dispatchUpdatesTo(this);
    }

    public void updateList(Map<Short, ResearchRest> newList) {
        ResearchDiffUtilCallback diffUtilCallback = new ResearchDiffUtilCallback(researches, newList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffUtilCallback);
        researches.clear();
        researches.putAll(newList);
        diffResult.dispatchUpdatesTo(this);
    }

}
