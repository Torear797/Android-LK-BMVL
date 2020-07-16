package com.bmvl.lk.ui.ProbyMenu.Probs.Research;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

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
import com.bmvl.lk.ViewHolders.AutoCompleteFieldHolder;

import java.text.MessageFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResearchFieldAdapter extends RecyclerView.Adapter {
   // private LayoutInflater inflater;
   // private List<Field> ResearchFields; //Поля исследований
    private ResearchRest CurrentResearch; //текущий образец

    private String[] Indicators;

    private short materialId;

    private String[] MasMethods, MasTypes;
    private List<Suggestion> suggestions;
    private List<SuggestionMethod> suggestionsMethods;
    private List<SuggestionType> suggestionsTypes;
    private short indicatorId;
    private String indicatorNdId;
    private AutoCompleteTextView SpinIndicator, SpinMethd, SpinType;
    private boolean isCompleteResearch = false;

    private int ResearchPosition; // Номер исследования
    private TextView HeaderResearch; //Текст шапки исследований


    ResearchFieldAdapter(ResearchRest res, String[] mass, List<Suggestion> sug, short id, int pos, TextView txt) {
        //this.inflater = LayoutInflater.from(context);
        this.ResearchPosition = pos;
        this.CurrentResearch = res;
        this.Indicators = mass;
        this.suggestions = sug;
        this.materialId = id;
        this.HeaderResearch = txt;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_research_field, parent, false);
        final AutoCompleteFieldHolder holder1 = new AutoCompleteFieldHolder(view1);

        switch (viewType) {
            case 0: {
                holder1.TextView.setOnItemClickListener((parent1, view, position, id) -> {
                    if (isCompleteResearch) {
                        getMethods();
                        return;
                    }

                    if (CurrentResearch.getIndicatorId() != suggestions.get(position).getId()) {
//                            CurrentResearch.ClearAll();
//                            SpinMethd.clearListSelection();
//                            SpinType.clearListSelection();
//                            SpinType.setText("");
                        ClearTextFields(true, true);
                        final Suggestion CurrentItem = suggestions.get(position);

                        CurrentResearch.setIndicatorId((short) CurrentItem.getId());
                        CurrentResearch.setIndicatorVal(String.valueOf(parent1.getItemAtPosition(position)));
                        CurrentResearch.setIndicatorNd(CurrentItem.getName_document());
                        CurrentResearch.setIndicatorNdId(CurrentItem.getId_document());

                        indicatorId = (short) CurrentItem.getId();
                        indicatorNdId = String.valueOf(CurrentItem.getId_document());

                        getMethods();
                    }
                    holder1.TextView.clearFocus();
                });
                SpinIndicator = holder1.TextView;

                holder1.TextView.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void afterTextChanged(Editable s) {
                        if (!isStandartValue((byte) 0, s.toString())) {
                            ClearTextFields(true, true);
                            CurrentResearch.setIndicatorVal(s.toString());
                        }
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }
                });

                break;
            } //Показатель
            case 1: {
                holder1.TextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (isCompleteResearch) {
                            getTypes(CurrentResearch.getMethodId(), String.valueOf(CurrentResearch.getMethodNdId()));
                            return;
                        }

                        //  if (CurrentResearch.getMethodVal() != null && !CurrentResearch.getMethodVal().equals(parent.getItemAtPosition(position))) {
                        if (CurrentResearch.getMethodId() != suggestionsMethods.get(position).getId()) {
                            ClearTextFields(false, true);
                            final SuggestionMethod CurrentItem = suggestionsMethods.get(position);

                            CurrentResearch.setMethodId(CurrentItem.getId());
                            CurrentResearch.setMethodVal(String.valueOf(parent.getItemAtPosition(position)));
                            CurrentResearch.setMethodNd(CurrentItem.getName_document());
                            if (CurrentItem.getId_document() != null)
                                CurrentResearch.setMethodNdId(CurrentItem.getId_document());
                            else
                                CurrentResearch.setMethodNdId((short) 0);

                            getTypes(CurrentItem.getId(), String.valueOf(CurrentResearch.getMethodNdId()));
                        }
                        holder1.TextView.clearFocus();
                    }
                });
                SpinMethd = holder1.TextView;

                holder1.TextView.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void afterTextChanged(Editable s) {
                        if (!isStandartValue((byte) 1, s.toString())) {
                            ClearTextFields(false, true);
                            CurrentResearch.setMethodVal(s.toString());
                        }
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }
                });


                break;
            } //Метод
            case 2: {
                holder1.TextView.setOnItemClickListener((parent12, view, position, id) -> {
                    if (isCompleteResearch) return;

                    if (CurrentResearch.getTypeId() != suggestionsTypes.get(position).getId()) {
                        final SuggestionType CurrentItem = suggestionsTypes.get(position);
                        CurrentResearch.setTypeId(CurrentItem.getId());
                        CurrentResearch.setTypeVal(String.valueOf(parent12.getItemAtPosition(position)));
                        HeaderResearch.setText(MessageFormat.format("№ {0} - {1}", ResearchPosition, holder1.TextView.getContext().getString(R.string.accreditation_ok)));
                    }
                    holder1.TextView.clearFocus();
                });
                SpinType = holder1.TextView;

                holder1.TextView.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void afterTextChanged(Editable s) {
                        if (!isStandartValue((byte) 2, s.toString())) {
                            CurrentResearch.setTypeVal(s.toString());
                            HeaderResearch.setText(MessageFormat.format("№ {0} - {1}", ResearchPosition, holder1.TextView.getContext().getString(R.string.accreditation_ok)));
                        }
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }
                });

                break;
            }//Тип
        }

        return holder1;
    }

    private boolean isStandartValue(byte TypeField, String value) {

        switch (TypeField) {
            case 0:
                if (suggestions != null)
                    for (Suggestion val : suggestions) {
                        if (val.getValue().equals(value)) return true;
                    }
                break;
            case 1:
                if (suggestionsMethods != null)
                    for (SuggestionMethod val : suggestionsMethods) {
                        if (val.getValue().equals(value)) return true;
                    }
                break;
            case 2:
                if (suggestionsTypes != null)
                    for (SuggestionType val : suggestionsTypes) {
                        if (val.getValue().equals(value)) return true;
                    }
                break;
        }

        return false;
    }

    private void ClearTextFields(boolean method, boolean Type) {
        if (method && SpinMethd != null) {
            CurrentResearch.ClearAll();
            // SpinMethd.clearListSelection();
            SpinMethd.setText("");
        }
        if (Type && SpinType != null) {
            CurrentResearch.ClearType();
            //  SpinType.clearListSelection();
            SpinType.setText("");
        }
        HeaderResearch.setText(MessageFormat.format("№ {0} - {1}", ResearchPosition, HeaderResearch.getContext().getString(R.string.accreditation_bad)));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //final Field f = ResearchFields.get(position);
     //   ((AutoCompleteFieldHolder) holder).Layout.setHint(f.getHint());
        switch (position) {
            case 0:
                ((AutoCompleteFieldHolder) holder).Layout.setHint("Показатель");
                break;
            case 1:
                ((AutoCompleteFieldHolder) holder).Layout.setHint("Метод испытаний");
                break;
            case 2:
                ((AutoCompleteFieldHolder) holder).Layout.setHint("Тип исследования");
                break;
        }

        if (CurrentResearch.getIndicatorVal() != null && CurrentResearch.getMethodVal() != null && CurrentResearch.getTypeVal() != null) {
            indicatorId = CurrentResearch.getIndicatorId();
            indicatorNdId = String.valueOf(CurrentResearch.getIndicatorNdId());
            isCompleteResearch = true;
            if (position == 0)
                InitAdapter(Indicators, ((AutoCompleteFieldHolder) holder).TextView);

            switch (position) {
                case 0:
                    ((AutoCompleteFieldHolder) holder).TextView.setText(CurrentResearch.getIndicatorVal());
                    break;
                case 1:
                    ((AutoCompleteFieldHolder) holder).TextView.setText(CurrentResearch.getMethodVal());
                    break;
                case 2:
                    ((AutoCompleteFieldHolder) holder).TextView.setText(CurrentResearch.getTypeVal());
                    break;
            }

        } else if (position == 0)
            InitAdapter(Indicators, ((AutoCompleteFieldHolder) holder).TextView);

    }

    private void InitAdapter(String[] mass, AutoCompleteTextView spiner) {
        if (mass != null) {
           // ArrayAdapter<String> adapter = ;
           // adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spiner.setAdapter(new ArrayAdapter<>(spiner.getContext(), android.R.layout.simple_dropdown_item_1line, mass));
        }
    }

//    private void InitAdapter(String[] mass, AutoCompleteTextView spiner, String text) {
//        if (mass != null) {
//            ArrayAdapter<String> adapter = new ArrayAdapter<>(inflater.getContext(), android.R.layout.simple_spinner_item, mass);
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            spiner.setAdapter(adapter);
//            spiner.setText(text);
//        }
//    }

    @Override
    public int getItemCount() {
        // return ResearchFields.size();
        return 3;
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
                            InitAdapter(MasMethods, SpinMethd);
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
                            InitAdapter(MasTypes, SpinType);
                            isCompleteResearch = false;
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<AnswerTypes> call, @NonNull Throwable t) {
                    }
                });
    }
}