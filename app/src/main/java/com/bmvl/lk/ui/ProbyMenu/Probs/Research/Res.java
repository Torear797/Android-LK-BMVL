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
import com.bmvl.lk.Rest.AnswerTypes;
import com.bmvl.lk.Rest.NetworkService;
import com.bmvl.lk.Rest.Order.ResearchRest;
import com.bmvl.lk.Rest.Suggestion;
import com.bmvl.lk.Rest.SuggestionMethod;
import com.bmvl.lk.Rest.SuggestionType;
import com.bmvl.lk.ViewHolders.SpinerHolder;
import com.bmvl.lk.data.CustomSpinerAdapter;
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

public class Res extends RecyclerSwipeAdapter<Res.ResearchItemHolder> {
    private LayoutInflater inflater;
    private TreeMap<Short, ResearchRest> researches; //Исследования
    private OnResearchClickListener onResearchClickListener;

    private String[] Methods, Types;
    private static String[] Indicators;
    private static List<Suggestion> suggestions;
    private List<SuggestionMethod> suggestionsMethods;
    private List<SuggestionType> suggestionsTypes;
    private static short materialId;
    private short posInd = -1, posMet = -1, posType = -1;
    private short indicatorId;
    private String indicatorNdId;
    private ResearchRest CurrentResearch;
    private boolean isCompleteResearch;

    public Res(Context context, TreeMap<Short, ResearchRest> ResearchesLise, OnResearchClickListener Listener) {
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
        CurrentResearch = researches.get(getPositionKey(i));
        posInd = -1;
        posMet = -1;
        posType = -1;
        Methods = null;
        Types = null;
        isCompleteResearch = false;

        researchItemHolder.NumberResearch.setText(MessageFormat.format("№ {0}", i + 1));

        if (CurrentResearch.getIndicatorVal() != null && CurrentResearch.getMethodVal() != null && CurrentResearch.getTypeVal() != null) {
            //  getMassiveValueForSpinner(researchItemHolder);
            indicatorId = CurrentResearch.getIndicatorId();
            indicatorNdId = String.valueOf(CurrentResearch.getIndicatorNdId());
            isCompleteResearch = true;
            getPositionForSpiners(researchItemHolder);


//            researchItemHolder.Indicators.setSelection(posInd);
//            researchItemHolder.Methods.setSelection(posMet);
//            researchItemHolder.Types.setSelection(posType);
        }

        InitAdapter(Indicators, researchItemHolder.Indicators);


        researchItemHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        mItemManger.bindView(researchItemHolder.itemView, i);
    }

    private void getMassiveValueForSpinner(ResearchItemHolder holder) {
        if (Indicators != null) {
            if (Methods == null) {
                indicatorId = CurrentResearch.getIndicatorId();
                indicatorNdId = String.valueOf(CurrentResearch.getIndicatorNdId());
                getMethods(holder.Methods);
            }
            if (Types == null)
                getTypes(CurrentResearch.getMethodId(), String.valueOf(CurrentResearch.getMethodNdId()), holder.Types);
        }
    }

    private void getPositionForSpiners(ResearchItemHolder researchItemHolder) {
        int position = 0;
        if (posInd == -1 && Indicators != null)
            for (String name : Indicators) {
                if (name.equals(CurrentResearch.getIndicatorVal())) {
                    posInd = (short) position;
                    break;
                }
            }
//        final ArrayAdapter adapter = (ArrayAdapter) researchItemHolder.Indicators.getAdapter();
//        posInd = (short) adapter.getPosition(CurrentResearch.getIndicatorVal());
//
//        final ArrayAdapter adapter2 = (ArrayAdapter) researchItemHolder.Methods.getAdapter();
//        posMet = (short) adapter2.getPosition(CurrentResearch.getIndicatorVal());
//
//        final ArrayAdapter adapter3 = (ArrayAdapter) researchItemHolder.Types.getAdapter();
//        posType = (short) adapter3.getPosition(CurrentResearch.getIndicatorVal());

        position = 0;
        if (posMet == -1 && Methods != null)
            for (String name : Methods) {
                if (name.equals(CurrentResearch.getMethodVal())) {
                    posMet = (short) position;
                    break;
                }
            }
        position = 0;
        if (posType == -1 && Types != null)
            for (String name : Types) {
                if (name.equals(CurrentResearch.getTypeVal())) {
                    posType = (short) position;
                    break;
                }
            }
    }

    public void UpdateIndicators(List<Suggestion> sug, int id) {
        suggestions = sug;
        materialId = (short) id;

        Indicators = new String[sug.size()];
        for (int i = 0; i < sug.size(); i++)
            Indicators[i] = sug.get(i).getValue();
    }

    private void InitAdapter(String[] mass, Spinner spiner) {
        if (mass != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(inflater.getContext(), android.R.layout.simple_spinner_item, mass);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spiner.setAdapter(adapter);
//            spiner.setEnabled(true);
//            spiner.setAlpha(1f);

//            CustomSpinerAdapter adapter = new CustomSpinerAdapter(inflater.getContext(), android.R.layout.simple_spinner_item, mass);
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            spiner.setAdapter(adapter);
//            spiner.setEnabled(true);
//            spiner.setAlpha(1f);

        }
    }

    private Short getPositionKey(int position) {
        return new ArrayList<Short>(researches.keySet()).get(position);
    }

    @Override
    public int getItemCount() {
        return researches.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    public class ResearchItemHolder extends RecyclerView.ViewHolder {
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
                    final Suggestion CurrentItem = suggestions.get(position);

                    if (CurrentResearch.getIndicatorId() != (short) CurrentItem.getId()) {
                        if (!isCompleteResearch) {
                            posInd = (short) position;
                            posMet = -1;
                            posType = -1;
                            CurrentResearch.ClearAll();

                            CurrentResearch.setIndicatorId((short) CurrentItem.getId());
                            CurrentResearch.setIndicatorVal(String.valueOf(parent.getItemAtPosition(position)));
                            CurrentResearch.setIndicatorNd(CurrentItem.getName_document());
                            CurrentResearch.setIndicatorNdId(CurrentItem.getId_document());


                            indicatorId = (short) CurrentItem.getId();
                            indicatorNdId = String.valueOf(CurrentItem.getId_document());
                        } else
                            Indicators.setSelection(posInd);
                        getMethods(Methods);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            };
            AdapterView.OnItemSelectedListener MethodSelectedListener = new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    final SuggestionMethod CurrentItem = suggestionsMethods.get(position);

                    if (CurrentResearch.getMethodId() != CurrentItem.getId()) {
                        if(!isCompleteResearch) {
                            posMet = (short) position;
                            posType = -1;
                            CurrentResearch.ClearType();

                            CurrentResearch.setMethodId(CurrentItem.getId());
                            CurrentResearch.setMethodVal(String.valueOf(parent.getItemAtPosition(position)));
                            CurrentResearch.setMethodNd(CurrentItem.getName_document());
                            CurrentResearch.setMethodNdId(CurrentItem.getId_document());
                        } else Methods.setSelection(posMet);

                        getTypes(CurrentItem.getId(), String.valueOf(CurrentItem.getId_document()), Types);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            };
            AdapterView.OnItemSelectedListener TypeSelectedListener = new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    final SuggestionType CurrentItem = suggestionsTypes.get(position);

                    if (CurrentResearch.getTypeId() != CurrentItem.getId()) {
                        if(!isCompleteResearch) {
                            posType = (short) position;
                            CurrentResearch.setTypeId(CurrentItem.getId());
                            CurrentResearch.setTypeVal(String.valueOf(parent.getItemAtPosition(position)));
                        } else Types.setSelection(posType);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            };
            Indicators.setOnItemSelectedListener(IndicatorsSelectedListener);
            Methods.setOnItemSelectedListener(MethodSelectedListener);
            Types.setOnItemSelectedListener(TypeSelectedListener);
        }
    }

    private void getMethods(final Spinner SpinerMethods) {
        Types = null;
        Methods = null;
        if (indicatorNdId.equals("0")) indicatorNdId = "";
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
                            InitAdapter(Methods, SpinerMethods);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<AnswerMethods> call, @NonNull Throwable t) {
                    }
                });
    }

    private void getTypes(short methodId, String methodNdId, final Spinner SpinerTypes) {
        Types = null;
        if (indicatorNdId.equals("0")) indicatorNdId = "";
        if (methodNdId.equals("0")) methodNdId = "";
        NetworkService.getInstance()
                .getJSONApi()
                .getTypes(App.UserAccessData.getToken(), "", materialId, indicatorId, indicatorNdId, methodId, methodNdId)
                .enqueue(new Callback<AnswerTypes>() {
                    @Override
                    public void onResponse(@NonNull Call<AnswerTypes> call, @NonNull Response<AnswerTypes> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Types = new String[response.body().getSuggestions().size()];
                            for (int i = 0; i < response.body().getSuggestions().size(); i++) {
                                Types[i] = response.body().getSuggestions().get(i).getValue();
                            }
                            suggestionsTypes = response.body().getSuggestions();
                            InitAdapter(Types, SpinerTypes);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<AnswerTypes> call, @NonNull Throwable t) {
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