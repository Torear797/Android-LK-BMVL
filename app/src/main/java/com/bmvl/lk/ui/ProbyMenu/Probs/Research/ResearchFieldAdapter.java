package com.bmvl.lk.ui.ProbyMenu.Probs.Research;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bmvl.lk.App;
import com.bmvl.lk.R;
import com.bmvl.lk.Rest.AnswerMethods;
import com.bmvl.lk.Rest.AnswerTypes;
import com.bmvl.lk.Rest.NetworkService;
import com.bmvl.lk.Rest.Order.ProbyRest;
import com.bmvl.lk.Rest.Order.ResearchRest;
import com.bmvl.lk.Rest.Order.SamplesRest;
import com.bmvl.lk.Rest.Suggestion;
import com.bmvl.lk.Rest.SuggestionMethod;
import com.bmvl.lk.Rest.SuggestionType;
import com.bmvl.lk.ViewHolders.AutoCompleteFieldHolder;
import com.bmvl.lk.ui.ProbyMenu.Probs.ProbAdapter;
import com.bmvl.lk.ui.create_order.CreateOrderActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.text.MessageFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResearchFieldAdapter extends RecyclerView.Adapter<com.bmvl.lk.ViewHolders.AutoCompleteFieldHolder> {
    private ResearchRest CurrentResearch; //текущий образец
    private short materialId;
    private String[] Indicators, MasMethods, MasTypes;
    private List<Suggestion> suggestions;
    private List<SuggestionMethod> suggestionsMethods;
    private List<SuggestionType> suggestionsTypes;
    private short indicatorId;
    private String indicatorNdId;
    private AutoCompleteFieldHolder SpinIndicator, SpinMethd, SpinType;
    private int ResearchPosition; // Номер исследования
    private ResearhAdapter.ResearchItemHolder ResearchHolder;
    private SamplesRest CurrentSample; //Текущий образец
    private TextView SamplesHeader;

    private TextView ProbHeader;
    private ProbyRest CurrentProb; //текущая проба

    ResearchFieldAdapter(ResearchRest res, List<Suggestion> sug, int pos, ResearhAdapter.ResearchItemHolder hol,
                         TextView s_header, SamplesRest Sample, TextView p_header, ProbyRest CurrentProb) {
        this.ResearchPosition = pos;
        this.CurrentResearch = res;
        this.suggestions = sug;
        this.materialId = Short.parseShort(CurrentProb.getFields().get("5"));
        this.ResearchHolder = hol;
        this.SamplesHeader = s_header;
        this.CurrentSample = Sample;
        this.ProbHeader = p_header;
        this.CurrentProb = CurrentProb;

        if (sug != null) {
            Indicators = new String[sug.size()];
            for (int i = 0; i < sug.size(); i++)
                Indicators[i] = sug.get(i).getValue();
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public AutoCompleteFieldHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_auto_compiete_field, parent, false);
        final AutoCompleteFieldHolder holder1 = new AutoCompleteFieldHolder(view1);

        switch (viewType) {
            case 0: {
                if (!CreateOrderActivity.ReadOnly)
                    holder1.TextView.setOnItemClickListener((parent1, view, position, id) -> {
                        if (!CurrentResearch.getIndicatorVal().equals(suggestions.get(position).getValue())) {
                            if (CurrentResearch.isComplete()) OpenDialog(false, position, "");
                            else {
                                ClearTextFields(true, false);
                                final Suggestion CurrentItem = suggestions.get(position);

                                CurrentResearch.setIndicatorId((short) CurrentItem.getId());
                                CurrentResearch.setIndicatorVal(String.valueOf(parent1.getItemAtPosition(position)));
                                CurrentResearch.setIndicatorNd(CurrentItem.getName_document());
                                CurrentResearch.setIndicatorNdId(CurrentItem.getId_document());

                                indicatorId = (short) CurrentItem.getId();
                                indicatorNdId = String.valueOf(CurrentItem.getId_document());

                                getMethods();
                            }
                        } else if (MasMethods == null) getMethods();
                        holder1.TextView.clearFocus();
                    });
                SpinIndicator = holder1;

                if (!CreateOrderActivity.ReadOnly)
                    holder1.TextView.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void afterTextChanged(Editable s) {
                            if (!CurrentResearch.getIndicatorVal().equals(String.valueOf(s))) {
                                if (isNoStandardValue((byte) 0, s.toString())) {
                                    if (CurrentResearch.isComplete())
                                        OpenDialog(true, 0, s.toString());
                                    else {
                                        ClearTextFields(true, true);
                                        CurrentResearch.setIndicatorVal(s.toString());
                                    }
                                }
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
                if (!CreateOrderActivity.ReadOnly)
                    holder1.TextView.setOnItemClickListener((parent13, view, position, id) -> {
//                    if (isCompleteResearch) {
//                        getTypes(CurrentResearch.getMethodId(), String.valueOf(CurrentResearch.getMethodNdId()));
//                        return;
//                    }

                        //  if (CurrentResearch.getMethodVal() != null && !CurrentResearch.getMethodVal().equals(parent.getItemAtPosition(position))) {
                        if (!CurrentResearch.getMethodVal().equals(suggestionsMethods.get(position).getValue())) {
                            ClearTextFields(false, false);
                            final SuggestionMethod CurrentItem = suggestionsMethods.get(position);

                            CurrentResearch.setMethodId(CurrentItem.getId());
                            CurrentResearch.setMethodVal(String.valueOf(parent13.getItemAtPosition(position)));
                            CurrentResearch.setMethodNd(CurrentItem.getName_document());
                            if (CurrentItem.getId_document() != null)
                                CurrentResearch.setMethodNdId(CurrentItem.getId_document());
                            else
                                CurrentResearch.setMethodNdId((short) 0);

                            getTypes(CurrentItem.getId(), String.valueOf(CurrentResearch.getMethodNdId()));
                        } else if (CurrentResearch.getMethodId() != 0)
                            getTypes(CurrentResearch.getMethodId(), String.valueOf(CurrentResearch.getMethodNdId()));
                        holder1.TextView.clearFocus();
                    });
                SpinMethd = holder1;

                if (!CreateOrderActivity.ReadOnly)
                    holder1.TextView.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void afterTextChanged(Editable s) {
                            if (isNoStandardValue((byte) 1, s.toString())) {
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
                if (!CreateOrderActivity.ReadOnly)
                    holder1.TextView.setOnItemClickListener((parent12, view, position, id) -> {
                        //  if (isCompleteResearch) return;

                        if (!CurrentResearch.getTypeVal().equals(suggestionsTypes.get(position).getValue())) {
                            final SuggestionType CurrentItem = suggestionsTypes.get(position);
                            CurrentResearch.setTypeId(CurrentItem.getId());
                            CurrentResearch.setTypeVal(String.valueOf(parent12.getItemAtPosition(position)));
                            CurrentResearch.setPrice(CurrentItem.getPrice());
                            CurrentResearch.setAccredited(CurrentItem.getInAccreditationArea());

                            if (CurrentResearch.getAccredited() == (byte) 1)
                                ResearchHolder.Number.setText(MessageFormat.format("№ {0} - {1}", ResearchPosition, holder1.TextView.getContext().getString(R.string.accreditation_ok)));

                            if (ResearchHolder.Info != null)
                                ResearchHolder.Info.setText(MessageFormat.format("Цена: {0}", CurrentResearch.getPrice()));

                            if (SamplesHeader != null)
                                SamplesHeader.setText(MessageFormat.format("Цена: {0} руб.", CurrentSample.getNewPrice()));
                            else
                                ProbAdapter.adapter.notifyItemChanged(ProbAdapter.adapter.getIdForField("SampleList"));

                            if (ProbHeader != null)
                                ProbHeader.setText(MessageFormat.format("Вид материала: {0} Образцов: {1} шт. Цена {2} руб.", CurrentProb.getFields().get("materialName"), CurrentProb.getSamples().size(),
                                        CurrentProb.getPrice()
                                ));
                            else ProbAdapter.probAdapter.notifyDataSetChanged();
                        }
                        holder1.TextView.clearFocus();
                    });
                SpinType = holder1;

                if (!CreateOrderActivity.ReadOnly)
                    holder1.TextView.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void afterTextChanged(Editable s) {
                            if (isNoStandardValue((byte) 2, s.toString())) {
                                CurrentResearch.setTypeVal(s.toString());
                                // HeaderResearch.setText(MessageFormat.format("№ {0} - {1}", ResearchPosition, holder1.TextView.getContext().getString(R.string.accreditation_ok)));
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

    private boolean isNoStandardValue(byte TypeField, String value) {

        switch (TypeField) {
            case 0:
                if (suggestions != null)
                    for (Suggestion val : suggestions) {
                        if (val.getValue().equals(value)) return false;
                    }
                break;
            case 1:
                if (suggestionsMethods != null)
                    for (SuggestionMethod val : suggestionsMethods) {
                        if (val.getValue().equals(value)) return false;
                    }
                break;
            case 2:
                if (suggestionsTypes != null)
                    for (SuggestionType val : suggestionsTypes) {
                        if (val.getValue().equals(value)) return false;
                    }
                break;
        }

        return true;
    }

    private void ClearTextFields(boolean method, boolean isEnabled) {
        if (method && SpinMethd != null) {
            CurrentResearch.ClearAll();
            SpinMethd.Layout.setEnabled(isEnabled);
            SpinMethd.TextView.setText("");
            SpinMethd.TextView.setAdapter(null);
            //  notifyItemChanged(1);
        }
        if (SpinType != null) {
            CurrentResearch.ClearType();
            SpinType.Layout.setEnabled(isEnabled);
            SpinType.TextView.setText("");
            SpinType.TextView.setAdapter(null);
            //  notifyItemChanged(2);
        }
        if (CurrentResearch.getAccredited() == (byte) 0)
            ResearchHolder.Number.setText(MessageFormat.format("№ {0} - {1}", ResearchPosition, ResearchHolder.Number.getContext().getString(R.string.accreditation_bad)));

        ResearchHolder.Info.setText(MessageFormat.format("Цена: {0} руб.", CurrentResearch.getPrice()));
    }

    private void OpenDialog(boolean isEnabled, int position, String Value) {
        if (SpinIndicator.TextView != null)
            new MaterialAlertDialogBuilder(SpinIndicator.TextView.getContext())
                    .setTitle(R.string.attention)
                    .setMessage(R.string.drop_research_text)
                    .setNegativeButton(R.string.cancel,
                            (dialog, which) -> SpinIndicator.TextView.setText(CurrentResearch.getIndicatorVal(), false))
                    .setPositiveButton(R.string.Continue,
                            (dialog, which) -> {
                                ClearTextFields(true, isEnabled);

                                if (isEnabled) {
                                    CurrentResearch.setIndicatorVal(Value);
                                } else {
                                    final Suggestion CurrentItem = suggestions.get(position);

                                    CurrentResearch.setIndicatorId((short) CurrentItem.getId());
                                    CurrentResearch.setIndicatorVal(CurrentItem.getValue());
                                    CurrentResearch.setIndicatorNd(CurrentItem.getName_document());
                                    CurrentResearch.setIndicatorNdId(CurrentItem.getId_document());

                                    indicatorId = (short) CurrentItem.getId();
                                    indicatorNdId = String.valueOf(CurrentItem.getId_document());
                                    SpinIndicator.TextView.setSelection(position);

                                    getMethods();
                                }
                            })
                    .show();
    }

    @Override
    public void onBindViewHolder(@NonNull AutoCompleteFieldHolder holder, int position) {
        switch (position) {
            case 0:
                holder.Layout.setHint("Показатель");
                break;
            case 1:
                holder.Layout.setHint("Метод испытаний");
                break;
            case 2:
                holder.Layout.setHint("Тип исследования");
                break;
        }
        holder.Layout.setEnabled(false);

        if (CurrentResearch.getIndicatorVal() != null && CurrentResearch.getMethodVal() != null && CurrentResearch.getTypeVal() != null) {
            indicatorId = CurrentResearch.getIndicatorId();
            indicatorNdId = String.valueOf(CurrentResearch.getIndicatorNdId());
            // isCompleteResearch = true;

            switch (position) {
                case 0:
                    InitAdapter(Indicators, holder);
                    holder.TextView.setText(CurrentResearch.getIndicatorVal(), false);
                    break;
                case 1:
                    holder.TextView.setText(CurrentResearch.getMethodVal(), false);
                    break;
                case 2:
                    holder.TextView.setText(CurrentResearch.getTypeVal(), false);
                    break;
            }

        } else if (position == 0)
            InitAdapter(Indicators, holder);
    }

    private void InitAdapter(String[] mass, RecyclerView.ViewHolder holder) {
        if (mass != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(((AutoCompleteFieldHolder) holder).TextView.getContext(), android.R.layout.simple_dropdown_item_1line, mass);
            // adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            ((AutoCompleteFieldHolder) holder).TextView.setAdapter(adapter);
            ((AutoCompleteFieldHolder) holder).Layout.setEnabled(true);

            // spiner.
        } else ((AutoCompleteFieldHolder) holder).Layout.setEnabled(false);
    }

    @Override
    public int getItemCount() {
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
                            //   isCompleteResearch = false;
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<AnswerTypes> call, @NonNull Throwable t) {
                    }
                });
    }
}