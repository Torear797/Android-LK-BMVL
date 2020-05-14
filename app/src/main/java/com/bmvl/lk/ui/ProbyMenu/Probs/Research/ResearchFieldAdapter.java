package com.bmvl.lk.ui.ProbyMenu.Probs.Research;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
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
import com.bmvl.lk.ui.Create_Order.Field;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResearchFieldAdapter extends RecyclerView.Adapter {
    private LayoutInflater inflater;
    private RecyclerView.RecycledViewPool viewPool;
    private List<Field> ResearchFields; //Поля исследований

    private ResearchRest CurrentResearch; //текущий образец

    private static String[] Indicators;
    private static List<Suggestion> suggestions;
    private static short materialId;

    private String[] MasMethods, MasTypes;
    private List<SuggestionMethod> suggestionsMethods;
    private List<SuggestionType> suggestionsTypes;
    private Short posInd, posMet, posType;
    private short indicatorId;
    private String indicatorNdId;
    private Spinner SpinIndicator, SpinMethd, SpinType;
    private boolean isCompleteResearch = false;


    public ResearchFieldAdapter(Context context, List<Field> Fields, ResearchRest res, String[] mass, List<Suggestion> sug, short id) {
        this.inflater = LayoutInflater.from(context);
        ResearchFields = Fields;
        CurrentResearch = res;

        Indicators = mass;
        suggestions = sug;
        materialId = id;
    }

    @Override
    public int getItemViewType(int position) {
        return ResearchFields.get(position).getType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_spiner, parent, false);
        final SpinerHolder holder1 = new SpinerHolder(view1);

        switch (viewType) {
            case 1:
                AdapterView.OnItemSelectedListener IndicatorsSelectedListener = new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (isCompleteResearch) {
                            getMethods();
                            return;
                        }
                        final Suggestion CurrentItem = suggestions.get(position);

                        if (CurrentResearch.getIndicatorId() != (short) CurrentItem.getId()) {
                            posInd = (short) position;
                            posMet = null;
                            posType = null;
                            CurrentResearch.ClearAll();

                            CurrentResearch.setIndicatorId((short) CurrentItem.getId());
                            CurrentResearch.setIndicatorVal(String.valueOf(parent.getItemAtPosition(position)));
                            CurrentResearch.setIndicatorNd(CurrentItem.getName_document());
                            CurrentResearch.setIndicatorNdId(CurrentItem.getId_document());

                            indicatorId = (short) CurrentItem.getId();
                            indicatorNdId = String.valueOf(CurrentItem.getId_document());

                            getMethods();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                };
                holder1.spiner.setOnItemSelectedListener(IndicatorsSelectedListener);
                SpinIndicator = holder1.spiner;
                break;
            case 2: {
                AdapterView.OnItemSelectedListener MethodSelectedListener = new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (isCompleteResearch) {
                            getTypes(CurrentResearch.getMethodId(), String.valueOf(CurrentResearch.getMethodNdId()));
                            return;
                        }
                        final SuggestionMethod CurrentItem = suggestionsMethods.get(position);


                        if (CurrentResearch.getMethodId() != CurrentItem.getId()) {
                            posMet = (short) position;
                            posType = -1;
                            CurrentResearch.ClearType();

                            CurrentResearch.setMethodId(CurrentItem.getId());
                            CurrentResearch.setMethodVal(String.valueOf(parent.getItemAtPosition(position)));
                            CurrentResearch.setMethodNd(CurrentItem.getName_document());
                            CurrentResearch.setMethodNdId(CurrentItem.getId_document());

                            getTypes(CurrentItem.getId(), String.valueOf(CurrentItem.getId_document()));
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                };
                holder1.spiner.setOnItemSelectedListener(MethodSelectedListener);
                SpinMethd = holder1.spiner;
            }
            break;
            case 3: {
                AdapterView.OnItemSelectedListener TypeSelectedListener = new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (isCompleteResearch) return;
                        final SuggestionType CurrentItem = suggestionsTypes.get(position);
                        if (CurrentResearch.getTypeId() != CurrentItem.getId()) {
                            posType = (short) position;
                            CurrentResearch.setTypeId(CurrentItem.getId());
                            CurrentResearch.setTypeVal(String.valueOf(parent.getItemAtPosition(position)));
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                };
                holder1.spiner.setOnItemSelectedListener(TypeSelectedListener);
                SpinType = holder1.spiner;
            }
            break;
        }


        return holder1;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final Field f = ResearchFields.get(position);
        ((SpinerHolder) holder).txtHint.setText(f.getHint());

        if (CurrentResearch.getIndicatorVal() != null && CurrentResearch.getMethodVal() != null && CurrentResearch.getTypeVal() != null) {
            indicatorId = CurrentResearch.getIndicatorId();
            indicatorNdId = String.valueOf(CurrentResearch.getIndicatorNdId());
            isCompleteResearch = true;
            if (f.getColumn_id() == 0)
            InitAdapter(Indicators, ((SpinerHolder) holder).spiner,CurrentResearch.getIndicatorVal());
        } else
        if (f.getColumn_id() == 0)
            InitAdapter(Indicators, ((SpinerHolder) holder).spiner);

    }

    private void InitAdapter(String[] mass, Spinner spiner) {
        if (mass != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(inflater.getContext(), android.R.layout.simple_spinner_item, mass);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spiner.setAdapter(adapter);
        }
    }

    private void InitAdapter(String[] mass, Spinner spiner, String text) {
        if (mass != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(inflater.getContext(), android.R.layout.simple_spinner_item, mass);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spiner.setAdapter(adapter);
            spiner.setSelection(adapter.getPosition(text));
        }
    }

    @Override
    public int getItemCount() {
        return ResearchFields.size();
    }

    private void getMethods() {
        MasTypes = null;
        MasMethods = null;
        if (indicatorNdId.equals("0")) indicatorNdId = "";
        NetworkService.getInstance()
                .getJSONApi()
                .getMethods(App.UserAccessData.getToken(), "", materialId, indicatorId, indicatorNdId)
                .enqueue(new Callback<AnswerMethods>() {
                    @Override
                    public void onResponse(@NonNull Call<AnswerMethods> call, @NonNull Response<AnswerMethods> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            MasMethods = new String[response.body().getSuggestions().size()];
                            for (int i = 0; i < response.body().getSuggestions().size(); i++) {
                                MasMethods[i] = response.body().getSuggestions().get(i).getValue();
                            }
                            suggestionsMethods = response.body().getSuggestions();
                            InitAdapter(MasMethods, SpinMethd, CurrentResearch.getMethodVal());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<AnswerMethods> call, @NonNull Throwable t) {
                    }
                });
    }

    private void getTypes(short methodId, String methodNdId) {
        MasTypes = null;
        if (indicatorNdId.equals("0")) indicatorNdId = "";
        if (methodNdId.equals("0")) methodNdId = "";
        NetworkService.getInstance()
                .getJSONApi()
                .getTypes(App.UserAccessData.getToken(), "", materialId, indicatorId, indicatorNdId, methodId, methodNdId)
                .enqueue(new Callback<AnswerTypes>() {
                    @Override
                    public void onResponse(@NonNull Call<AnswerTypes> call, @NonNull Response<AnswerTypes> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            MasTypes = new String[response.body().getSuggestions().size()];
                            for (int i = 0; i < response.body().getSuggestions().size(); i++) {
                                MasTypes[i] = response.body().getSuggestions().get(i).getValue();
                            }
                            suggestionsTypes = response.body().getSuggestions();
                            InitAdapter(MasTypes, SpinType, CurrentResearch.getTypeVal());
                            isCompleteResearch = false;
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<AnswerTypes> call, @NonNull Throwable t) {
                    }
                });
    }
}
