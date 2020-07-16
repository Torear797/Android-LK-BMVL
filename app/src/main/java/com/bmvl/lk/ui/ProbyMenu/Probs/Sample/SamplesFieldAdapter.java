package com.bmvl.lk.ui.ProbyMenu.Probs.Sample;

import android.app.DatePickerDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bmvl.lk.R;
import com.bmvl.lk.Rest.Order.ResearchRest;
import com.bmvl.lk.Rest.Order.SamplesRest;
import com.bmvl.lk.Rest.Suggestion;
import com.bmvl.lk.ViewHolders.DataFieldHolder;
import com.bmvl.lk.ViewHolders.MultiSpinerHolder;
import com.bmvl.lk.ViewHolders.ResearchPanelHolder;
import com.bmvl.lk.ViewHolders.SpinerHolder;
import com.bmvl.lk.ViewHolders.TextViewHolder;
import com.bmvl.lk.data.Field;
import com.bmvl.lk.ui.ProbyMenu.Probs.MultiSpinner;
import com.bmvl.lk.ui.ProbyMenu.Probs.Research.ResearhAdapter;
import com.daimajia.swipe.util.Attributes;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SamplesFieldAdapter extends RecyclerView.Adapter {
    //   private LayoutInflater inflater;
    private RecyclerView.RecycledViewPool viewPool;
    private List<Field> SamplesField; //Поля образцов

    private SamplesRest CurrentSample; //Текущий образец
    private ResearhAdapter Adapter;

    private static Calendar dateAndTime = Calendar.getInstance();

    private List<Suggestion> buffer_sug;
    private Integer buffer_id;

    private Map<String, String> ProbFields; //Поля пробы

    SamplesFieldAdapter(List<Field> SamFields, SamplesRest Sample, Map<String, String> ProbFields) {
        //   this.inflater = LayoutInflater.from(context);
        this.ProbFields = ProbFields;
        viewPool = new RecyclerView.RecycledViewPool();
        SamplesField = SamFields;
        CurrentSample = Sample;
    }

    @Override
    public int getItemViewType(int position) {
        return SamplesField.get(position).getType();
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
                        CurrentSample.getFields().put(GetColumn_id(holder1.getLayoutPosition()), String.valueOf(item));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                };
                holder1.spiner.setOnItemSelectedListener(itemSelectedListener);

                return holder1;
            case 5:
                View view5 = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_multi_spinner, parent, false);
                return new MultiSpinerHolder(view5);
            case 6:
                View view6 = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_research_list, parent, false);
                return new ResearchPanelHolder(view6);
            case 8: {
                View view8 = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_data_field, parent, false);
                return new DataFieldHolder(view8);
            }//DataField
            default:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_field, parent, false);
                final TextViewHolder holder = new TextViewHolder(view);

                holder.field.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void afterTextChanged(Editable s) {
                        if (!String.valueOf(s).equals(""))
                            CurrentSample.getFields().put(GetColumn_id(holder.getLayoutPosition()), String.valueOf(s));
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

    private short GetColumn_id(int position) {
        return (short) (SamplesField.get(position).getColumn_id());
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        final Field f = SamplesField.get(position);
        switch (f.getType()) {
            case 0: {
                ((TextViewHolder) holder).Layout.setHint(f.getHint());
                ((TextViewHolder) holder).field.setInputType(f.getInputType());
                if (CurrentSample.getFields().containsKey((short) f.getColumn_id()))
                    ((TextViewHolder) holder).field.setText(CurrentSample.getFields().get((short) f.getColumn_id()));
                break;
            }//Edittext
            case 1: {
                if (f.getEntries() != -1) {
                    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(((SpinerHolder) holder).spiner.getContext(), f.getEntries(), android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    ((SpinerHolder) holder).spiner.setAdapter(adapter);
                    if (CurrentSample.getFields().containsKey((short) (f.getColumn_id())))
                        ((SpinerHolder) holder).spiner.setSelection(adapter.getPosition(CurrentSample.getFields().get((short) (f.getColumn_id()))));
                } else {
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(((SpinerHolder) holder).spiner.getContext(), android.R.layout.simple_spinner_item, f.getSpinerData());
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    ((SpinerHolder) holder).spiner.setAdapter(adapter);
                    if (CurrentSample.getFields().containsKey((short) (f.getColumn_id())))
                        ((SpinerHolder) holder).spiner.setSelection(adapter.getPosition(CurrentSample.getFields().get((short) (f.getColumn_id()))));
                }


                ((SpinerHolder) holder).txtHint.setText(f.getHint());
                break;
            }//Spiner
            case 5: {
                final List<String> items = Arrays.asList(((MultiSpinerHolder) holder).spiner.getContext().getResources().getStringArray(f.getEntries()));
                MultiSpinner.MultiSpinnerListener onSelectedListener = new MultiSpinner.MultiSpinnerListener() {
                    public void onItemsSelected(boolean[] selected, String id) {
                        CurrentSample.getFields().put((short) f.getColumn_id(), id);
                    }
                };
                String selected = "";
                if (CurrentSample.getFields().containsKey((short) f.getColumn_id()))
                    selected = CurrentSample.getFields().get((short) f.getColumn_id());

                assert selected != null;
                ((MultiSpinerHolder) holder).spiner.setItems(
                        items,
                        selected,
                        onSelectedListener
                );

                ((MultiSpinerHolder) holder).txtHint.setText(f.getHint());
                break;
            }//Мультиспинер
            case 6: {
                Adapter = new ResearhAdapter(CurrentSample.getResearches());
                (Adapter).setMode(Attributes.Mode.Single);
                ((ResearchPanelHolder) holder).ResearchList.setAdapter(Adapter);
                ((ResearchPanelHolder) holder).ResearchList.setRecycledViewPool(viewPool);

                if (buffer_id != null && buffer_sug != null) {
                    UpdateAdapter(buffer_sug, buffer_id);
                    buffer_sug = null;
                    buffer_id = null;
                }

                //Добавляет исследование
                ((ResearchPanelHolder) holder).btnAddReserch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ProbFields.containsKey("5")) {
                            short size = (short) CurrentSample.getResearches().size();
                            short newid = 0;
                            if (size > 0)
                                newid = getPositionKeyR(size - 1, CurrentSample.getResearches());

                            Map<Short, ResearchRest> insertlist = new HashMap<>();
                            insertlist.put((short) (newid + 1), new ResearchRest(newid));
                            Adapter.insertdata(insertlist);
                            Adapter.notifyDataSetChanged();
                            ((ResearchPanelHolder) holder).ResearchList.smoothScrollToPosition(Adapter.getItemCount() - 1);
                        } else
                            Toast.makeText(((ResearchPanelHolder) holder).ResearchList.getContext(), ((ResearchPanelHolder) holder).ResearchList.getContext().getString(R.string.MaterialNoSelect), Toast.LENGTH_SHORT).show();
                    }
                });

                break;
            }//Исследования
            case 8: {
                ((DataFieldHolder) holder).Layout.setHint(f.getHint());
                ((DataFieldHolder) holder).field.setText(CurrentSample.getFields().get((short) f.getColumn_id()));

                final DatePickerDialog.OnDateSetListener Datapicker = (view, year, monthOfYear, dayOfMonth) -> ChangeData(year, monthOfYear, dayOfMonth, ((DataFieldHolder) holder).field, f.getColumn_id());
                ((DataFieldHolder) holder).Layout.setEndIconOnClickListener(v -> new DatePickerDialog(Objects.requireNonNull(((DataFieldHolder) holder).Layout.getContext()), Datapicker,
                        dateAndTime.get(Calendar.YEAR),
                        dateAndTime.get(Calendar.MONTH),
                        dateAndTime.get(Calendar.DAY_OF_MONTH))
                        .show());
            }//DataField
        }
    }

    private void ChangeData(int year, int monthOfYear, int dayOfMonth, EditText Edt, int position) {
        monthOfYear = monthOfYear + 1;
//        dateAndTime.set(Calendar.YEAR, year);
//        dateAndTime.set(Calendar.MONTH, monthOfYear);
//        dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        int month = monthOfYear;
        String formattedMonth = "" + month;
        String formattedDayOfMonth = "" + dayOfMonth;

        if (month < 10) {
            formattedMonth = "0" + month;
        }
        if (dayOfMonth < 10) {
            formattedDayOfMonth = "0" + dayOfMonth;
        }

        CurrentSample.getFields().put((short) (position), MessageFormat.format("{0}.{1}.{2}", formattedDayOfMonth, formattedMonth, String.valueOf(year)));
        Edt.setText(MessageFormat.format("{0} . {1} . {2}", formattedDayOfMonth, formattedMonth, String.valueOf(year)));
        Edt.requestFocus();
        Edt.setSelection(Edt.getText().length());
    }

    void UpdateAdapter(List<Suggestion> suggestions, int id) {
        if (Adapter != null) {
            Adapter.UpdateIndicators(suggestions, id);
            Adapter.notifyDataSetChanged();
        } else {
            buffer_sug = suggestions;
            buffer_id = id;
        }
    }

    private Short getPositionKeyR(int position, Map<Short, ResearchRest> List) {
        if (List.size() > 0)
            return new ArrayList<>(List.keySet()).get(position);
        else return 0;
    }

    @Override
    public int getItemCount() {
        return SamplesField.size();
    }
}