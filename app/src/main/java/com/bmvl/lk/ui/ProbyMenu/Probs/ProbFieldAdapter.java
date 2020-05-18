package com.bmvl.lk.ui.ProbyMenu.Probs;

import android.app.DatePickerDialog;
import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import com.bmvl.lk.App;
import com.bmvl.lk.R;
import com.bmvl.lk.Rest.AnswerIndicators;
import com.bmvl.lk.Rest.NetworkService;
import com.bmvl.lk.Rest.Order.ProbyRest;
import com.bmvl.lk.Rest.Order.ResearchRest;
import com.bmvl.lk.Rest.Order.SamplesRest;
import com.bmvl.lk.ViewHolders.MultiSpinerHolder;
import com.bmvl.lk.ViewHolders.SamplesPanelHolder;
import com.bmvl.lk.ViewHolders.SelectButtonHolder;
import com.bmvl.lk.ViewHolders.SpinerHolder;
import com.bmvl.lk.ViewHolders.SwitchHolder;
import com.bmvl.lk.ViewHolders.TextViewHolder;
import com.bmvl.lk.data.SpacesItemDecoration;
import com.bmvl.lk.ui.create_order.CreateOrderActivity;
import com.bmvl.lk.ui.create_order.Field;
import com.bmvl.lk.ui.ProbyMenu.Probs.Sample.SamplesAdapter;
import com.daimajia.swipe.util.Attributes;
import com.google.android.material.textfield.TextInputLayout;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProbFieldAdapter extends RecyclerView.Adapter {
    private LayoutInflater inflater;
    private static Calendar dateAndTime = Calendar.getInstance();
    private List<Field> ProbFields;
    private List<Field> SampleFields; //Поля Образцов
    private RecyclerView.RecycledViewPool viewPool;
    private TextView ProbHeader;

    private ProbyRest CurrentProb;
    private SamplesAdapter SamAdapter;

    ProbFieldAdapter(Context context, List<Field> probFields, List<Field> sampleFields, ProbyRest prob, TextView header) {
        this.inflater = LayoutInflater.from(context);
        this.ProbHeader = header;
        viewPool = new RecyclerView.RecycledViewPool();
        ProbFields = probFields;
        SampleFields = sampleFields;
        CurrentProb = prob;
    }

    @Override
    public int getItemViewType(int position) {
        return ProbFields.get(position).getType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case 1:
                View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_spiner, parent, false);
                final SpinerHolder holder1 = new SpinerHolder(view1);

                AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String item = (String) parent.getItemAtPosition(position);
                        CurrentProb.getFields().put(GetColumn_id(holder1.getLayoutPosition()), String.valueOf(item));

                        if (GetColumn_id(holder1.getLayoutPosition()).equals("5")) {
                            CurrentProb.getFields().put("materialName", String.valueOf(item));
                            CurrentProb.getFields().put("5", String.valueOf(position + 1));
                            ProbHeader.setText(MessageFormat.format("Вид материала: {0} Образцов: {1}", item, CurrentProb.getSamples().size()));
                            getIndicators(position + 1);
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                };
                holder1.spiner.setOnItemSelectedListener(itemSelectedListener);

                return holder1;
            case 3:
                View view3 = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_check_button, parent, false);
                final SwitchHolder holder3 = new SwitchHolder(view3);
                holder3.switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        CurrentProb.getFields().put(GetColumn_id(holder3.getLayoutPosition()), String.valueOf(isChecked));
                    }
                });
                return holder3;
            case 4:
                View view4 = inflater.inflate(R.layout.item_button_select, parent, false);
                return new SelectButtonHolder(view4);
            case 5:
                View view5 = inflater.inflate(R.layout.item_multi_spinner, parent, false);
                return new MultiSpinerHolder(view5);
            case 7:
                View view7 = inflater.inflate(R.layout.item_research_list, parent, false);
                return new SamplesPanelHolder(view7);
            default:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_field, parent, false);
                final TextViewHolder holder = new TextViewHolder(view);
                holder.field.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void afterTextChanged(Editable s) {
                        if(!String.valueOf(s).equals(""))
                        CurrentProb.getFields().put(GetColumn_id(holder.getLayoutPosition()), String.valueOf(s));
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }
                });

                return holder;
        }
    }

    private String GetColumn_id(int position) {
        return String.valueOf(ProbFields.get(position).getColumn_id());
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        final Field f = ProbFields.get(position);

        switch (f.getType()) {
            case 0: {
                if (f.getInputType() == InputType.TYPE_NULL)
                    ((TextViewHolder) holder).Layout.setBoxBackgroundColor(inflater.getContext().getResources().getColor(R.color.field_inactive));

                ((TextViewHolder) holder).Layout.setHint(f.getHint());
                ((TextViewHolder) holder).field.setInputType(f.getInputType());
                ((TextViewHolder) holder).field.setText(CurrentProb.getFields().get(String.valueOf(f.getColumn_id())));

                if (f.getIcon() != null) {
                    ((TextViewHolder) holder).Layout.setEndIconMode(TextInputLayout.END_ICON_CUSTOM);
                    ((TextViewHolder) holder).Layout.setEndIconDrawable(f.getIcon());

                    if (f.isData()) {
                        final DatePickerDialog.OnDateSetListener Datapicker = new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                ChangeData(year, monthOfYear, dayOfMonth, ((TextViewHolder) holder).field, f);
                            }
                        };
                        ((TextViewHolder) holder).Layout.setEndIconOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new DatePickerDialog(Objects.requireNonNull(inflater.getContext()), Datapicker,
                                        dateAndTime.get(Calendar.YEAR),
                                        dateAndTime.get(Calendar.MONTH),
                                        dateAndTime.get(Calendar.DAY_OF_MONTH))
                                        .show();
                            }
                        });

                    }
                }

                break;
            } //Текстовое поле
            case 1: {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(inflater.getContext(), f.getEntries(), android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                ((SpinerHolder) holder).spiner.setAdapter(adapter);
                ((SpinerHolder) holder).txtHint.setText(f.getHint());

                if (CurrentProb.getFields().containsKey(String.valueOf(f.getColumn_id()))) {

                    if (f.getColumn_id() != 5) {
//                        String[] id = inflater.getContext().getResources().getStringArray(f.getEntries());
//                        int CurrentID = 0;
//                        for (String name : id) {
//                            if (name.equals(CurrentProb.getFields().get(String.valueOf(f.getColumn_id()))))
//                                break;
//                            CurrentID++;
//                        }
//
//                        if (CurrentID < id.length)
//                            ((SpinerHolder) holder).spiner.setSelection(CurrentID);
                        ((SpinerHolder) holder).spiner.setSelection(adapter.getPosition(CurrentProb.getFields().get(String.valueOf(f.getColumn_id()))));
                    } else {
                        ((SpinerHolder) holder).spiner.setSelection(Integer.parseInt(Objects.requireNonNull(CurrentProb.getFields().get("5"))) - 1);
                        ProbHeader.setText(MessageFormat.format("Вид материала: {0} Образцов: {1}", CurrentProb.getFields().get("materialName"), CurrentProb.getSamples().size()));
                        //getIndicators(1);
                    }
                }

                break;
            } //Spiner
            case 3: {
                ((SwitchHolder) holder).switchButton.setText(String.format("%s  ", f.getHint()));

                if (CurrentProb.getFields().containsKey(String.valueOf(f.getColumn_id())))
                    ((SwitchHolder) holder).switchButton.setEnabled(Boolean.parseBoolean(CurrentProb.getFields().get(String.valueOf(f.getColumn_id()))));
                else ((SwitchHolder) holder).switchButton.setEnabled(false);

                break;
            } //Swith
            case 4: {
                ((SelectButtonHolder) holder).hint.setText(f.getHint());
                break;
            }
            case 5: {
                final List<String> items = Arrays.asList(inflater.getContext().getResources().getStringArray(f.getEntries()));
                MultiSpinner.MultiSpinnerListener onSelectedListener = new MultiSpinner.MultiSpinnerListener() {
                    public void onItemsSelected(boolean[] selected, String id) {
                        CurrentProb.getFields().put(String.valueOf(f.getColumn_id()), id);
                    }
                };
                String selected = "";
                if (CurrentProb.getFields().containsKey(String.valueOf(f.getColumn_id())))
                    selected = CurrentProb.getFields().get(String.valueOf(f.getColumn_id()));

                assert selected != null;
                ((MultiSpinerHolder) holder).spiner.setItems(
                        items,
                        //inflater.getContext().getString(R.string.for_all),
                        selected,
                        onSelectedListener
                );

                ((MultiSpinerHolder) holder).txtHint.setText(f.getHint());
                break;
            } //Мультиспинер
//            case 6: {
//                Adapter = new ResearhAdapter(inflater.getContext(), CurrentProb.getSamples().get(getPositionKey(0, CurrentProb.getSamples())).getResearches(), Listener);
//                (Adapter).setMode(Attributes.Mode.Single);
//                ((ResearchPanelHolder) holder).ResearchList.setAdapter(Adapter);
//                ((ResearchPanelHolder) holder).ResearchList.addItemDecoration(new SpacesItemDecoration((byte) 20, (byte) 0));
//                ((ResearchPanelHolder) holder).ResearchList.setRecycledViewPool(viewPool);
//
//
//                //Добавляет исследование
//                ((ResearchPanelHolder) holder).btnAddReserch.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        short size = (short) CurrentProb.getSamples().get((short) 1).getResearches().size();
//                        short newid = 0;
//                        if (size > 0)
//                            newid = getPositionKeyR(size - 1, CurrentProb.getSamples().get((short) 1).getResearches());
//
//                        Map<Short, ResearchRest> insertlist = new HashMap<>();
//                        insertlist.put((short) (newid + 1), new ResearchRest((short) (newid + 1)));
//                        Adapter.insertdata(insertlist);
//                        ((ResearchPanelHolder) holder).ResearchList.smoothScrollToPosition(Adapter.getItemCount() - 1);
//                    }
//                });
//
//                break;
//            }
            case 7: {
                SamAdapter = new SamplesAdapter(inflater.getContext(), SampleFields, CurrentProb.getSamples(), SamListener);
                (SamAdapter).setMode(Attributes.Mode.Single);
                ((SamplesPanelHolder) holder).SampleList.setAdapter(SamAdapter);
                ((SamplesPanelHolder) holder).SampleList.addItemDecoration(new SpacesItemDecoration((byte) 20, (byte) 0));
                ((SamplesPanelHolder) holder).SampleList.setItemAnimator(new DefaultItemAnimator());
                ((SamplesPanelHolder) holder).SampleList.setRecycledViewPool(viewPool);

                if (CreateOrderActivity.order_id != 1 && CreateOrderActivity.order_id != 8) {
                    //Добавление образца
                    ((SamplesPanelHolder) holder).btnAddSample.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            short newid = getPositionKey(CurrentProb.getSamples().size() - 1, CurrentProb.getSamples());
                            Map<Short, SamplesRest> insertlist = new HashMap<>();
                            insertlist.put((short) (newid + 1), new SamplesRest(newid));
                            SamAdapter.insertdata(insertlist);
                            ((SamplesPanelHolder) holder).SampleList.smoothScrollToPosition(SamAdapter.getItemCount() - 1);
                        }
                    });
                } else {
                    ((SamplesPanelHolder) holder).Header.setVisibility(View.GONE);
                    ((SamplesPanelHolder) holder).btnAddSample.setVisibility(View.GONE);
                }

                break;
            } // Иссследования
        }
    }

    private SamplesAdapter.OnSamplesClickListener SamListener = new SamplesAdapter.OnSamplesClickListener() {

        @Override
        public void onUpdateSamples() {
            SamAdapter.closeAllItems();
        }
    };

    private Short getPositionKey(int position, Map<Short, SamplesRest> Samples) {
        if (Samples.size() > 0)
            return new ArrayList<>(Samples.keySet()).get(position);
        else return 0;
    }

    private Short getPositionKeyR(int position, Map<Short, ResearchRest> List) {
        return new ArrayList<>(List.keySet()).get(position);
    }

    private void ChangeData(int year, int monthOfYear, int dayOfMonth, EditText Edt, Field f) {
        monthOfYear = monthOfYear + 1;
        dateAndTime.set(Calendar.YEAR, year);
        dateAndTime.set(Calendar.MONTH, monthOfYear);
        dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        int month = monthOfYear;
        String formattedMonth = "" + month;
        String formattedDayOfMonth = "" + dayOfMonth;

        if (month < 10) {
            formattedMonth = "0" + month;
        }
        if (dayOfMonth < 10) {
            formattedDayOfMonth = "0" + dayOfMonth;
        }

        CurrentProb.getFields().put(String.valueOf(f.getColumn_id()), MessageFormat.format("{0}.{1}.{2}", formattedDayOfMonth, formattedMonth, String.valueOf(year)));
        Edt.setText(MessageFormat.format("{0} . {1} . {2}", formattedDayOfMonth, formattedMonth, String.valueOf(year)));
    }

    @Override
    public int getItemCount() {
        return ProbFields.size();
    }

    private void getIndicators(final int id) {
        NetworkService.getInstance()
                .getJSONApi()
                .getIndicators(App.UserAccessData.getToken(),id,"")
                .enqueue(new Callback<AnswerIndicators>() {
                    @Override
                    public void onResponse(@NonNull Call<AnswerIndicators> call, @NonNull Response<AnswerIndicators> response) {
                        if (response.isSuccessful() && response.body() != null) {
                           SamAdapter.UpdateAdapter(response.body().getSuggestions(), id);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<AnswerIndicators> call, @NonNull Throwable t) {
                    }
                });
    }
}