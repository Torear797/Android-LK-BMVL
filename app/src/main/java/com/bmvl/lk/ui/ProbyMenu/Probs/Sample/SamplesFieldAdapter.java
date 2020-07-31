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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bmvl.lk.App;
import com.bmvl.lk.R;
import com.bmvl.lk.Rest.Order.ProbyRest;
import com.bmvl.lk.Rest.Order.ResearchRest;
import com.bmvl.lk.Rest.Order.SamplesRest;
import com.bmvl.lk.Rest.Suggestion;
import com.bmvl.lk.ViewHolders.DataFieldHolder;
import com.bmvl.lk.ViewHolders.MultiChipChoceHoldeer;
import com.bmvl.lk.ViewHolders.ResearchPanelHolder;
import com.bmvl.lk.ViewHolders.SpinerHolder;
import com.bmvl.lk.ViewHolders.TextViewHolder;
import com.bmvl.lk.data.ContactsCompletionView;
import com.bmvl.lk.data.Field;
import com.bmvl.lk.data.StringSpinnerAdapter;
import com.bmvl.lk.data.models.Document;
import com.bmvl.lk.ui.ProbyMenu.Probs.ProbAdapter;
import com.bmvl.lk.ui.ProbyMenu.Probs.ProbFieldAdapter;
import com.bmvl.lk.ui.ProbyMenu.Probs.Research.ResearhAdapter;
import com.daimajia.swipe.util.Attributes;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class SamplesFieldAdapter extends RecyclerView.Adapter {
    private RecyclerView.RecycledViewPool viewPool;
    private List<Field> SamplesField; //Поля образцов

    private SamplesRest CurrentSample; //Текущий образец
    private ResearhAdapter Adapter;

    private static Calendar dateAndTime = Calendar.getInstance();

    private Map<String, String> ProbFields; //Поля пробы
    private ProbyRest CurrentProb; //текущая проба
    private byte id_sample; //Порядковый номер образца - НЕ его id;
    private String[] Depth_of_selection;

    private List<Suggestion> suggestions; //Материалы;

    private Map<String, Integer> ProbFieldIds; //id полей для обновлений

    private TextView SamplesHeader;
    private TextView ProbHeader;


    SamplesFieldAdapter(List<Field> SamFields, SamplesRest Sample,
                        Map<String, String> ProbFields,
                        ProbyRest CurrentProb, byte id, List<Suggestion> list, Map<String, Integer> fields, TextView s_header, TextView p_header) {
        this.ProbFields = ProbFields;
        viewPool = new RecyclerView.RecycledViewPool();
        SamplesField = SamFields;
        CurrentSample = Sample;
        this.CurrentProb = CurrentProb;
        this.id_sample = id;
        this.suggestions = list;
        this.ProbFieldIds = fields;
        this.SamplesHeader = s_header;
        this.ProbHeader = p_header;
    }

    @Override
    public int getItemViewType(int position) {
        return SamplesField.get(position).getType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case 1: {
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
            } //Spiner
            case 5: {
                View view5 = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_multi_chip_choce, parent, false);
                //  return new MultiSpinerHolder(view5);
                return new MultiChipChoceHoldeer(view5);
            }
            case 6: {
                View view6 = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_research_list, parent, false);
                return new ResearchPanelHolder(view6);
            }
            case 8: {
                View view8 = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_data_field, parent, false);
                // return new DataFieldHolder(view8);

                final DataFieldHolder holder = new DataFieldHolder(view8);
                holder.field.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void afterTextChanged(Editable s) {
                        if (!String.valueOf(s).equals("") && s.toString().length() <= 10)
                            try {
                                CurrentSample.getFields().put(GetColumn_id(holder.getLayoutPosition()), new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Objects.requireNonNull(new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).parse(s.toString()))));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }
                });

                return holder;
            }//DataField
            default: {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_field, parent, false);
                final TextViewHolder holder = new TextViewHolder(view);

                holder.field.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void afterTextChanged(Editable s) {
                        if (!String.valueOf(s).equals("")) {
                            if (CurrentSample.getFields().containsKey(GetColumn_id(holder.getLayoutPosition())))
                                if (Objects.equals(CurrentSample.getFields().get(GetColumn_id(holder.getLayoutPosition())), s.toString()))
                                    return;
                            CurrentSample.getFields().put(GetColumn_id(holder.getLayoutPosition()), String.valueOf(s));

                            if (GetColumn_id(holder.getLayoutPosition()) == 144 && ProbFieldIds.containsKey("144")) {

                                if (CurrentProb.getFields().containsKey("144")) {
                                    Depth_of_selection = Objects.requireNonNull(CurrentProb.getFields().get("144")).split(", ");
                                    // if (id_sample <= Depth_of_selection.length - 1)
                                    Depth_of_selection[id_sample] = "Образец №" + (id_sample + 1) + ": " + s.toString();
//                                    else {
//                                        String[] NewDeepMas = new String[CurrentProb.getSamples().size()];
//                                        System.arraycopy(Depth_of_selection, 0, NewDeepMas, 0, Depth_of_selection.length);
//                                        NewDeepMas[id_sample] = "Образец №" + (id_sample + 1) + ": " + s.toString();
//                                        Depth_of_selection = NewDeepMas;
//                                    }
                                }

                                CurrentProb.getFields().put("144", ArrayToString());
                                ProbAdapter.adapter.notifyItemChanged(ProbFieldIds.get("144"));
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

                return holder;
            }//EditText
        }
    }

    private String ArrayToString() {
        StringBuilder string = new StringBuilder();
        for (String deep : Depth_of_selection) {
            if (deep != null && !deep.equals("")) {
                if (string.length() > 0) string.append(", ");
                string.append(deep);
            }
        }
        return string.toString();
    }

    private short GetColumn_id(int position) {
        return (short) (SamplesField.get(position).getColumn_id());
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
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
                StringSpinnerAdapter adapter;
                if (f.getEntries() != -1) {
                    //ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(((SpinerHolder) holder).spiner.getContext(), f.getEntries(), android.R.layout.simple_spinner_item);
                    adapter = new StringSpinnerAdapter(((SpinerHolder) holder).spiner.getContext(), android.R.layout.simple_spinner_item, ((SpinerHolder) holder).spiner.getContext().getResources().getStringArray(f.getEntries()), ((SpinerHolder) holder).spiner);
                } else {
                    adapter = new StringSpinnerAdapter(((SpinerHolder) holder).spiner.getContext(), android.R.layout.simple_spinner_item, f.getSpinerData(), ((SpinerHolder) holder).spiner);
                    //  ArrayAdapter<String> adapter = new ArrayAdapter<>(((SpinerHolder) holder).spiner.getContext(), android.R.layout.simple_spinner_item, f.getSpinerData());
                }
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                ((SpinerHolder) holder).spiner.setAdapter(adapter);
                if (CurrentSample.getFields().containsKey((short) (f.getColumn_id())))
                    ((SpinerHolder) holder).spiner.setSelection(adapter.getPosition(CurrentSample.getFields().get((short) (f.getColumn_id()))));


                ((SpinerHolder) holder).txtHint.setText(f.getHint());
                // ((SpinerHolder) holder).layout.setHint(f.getHint());
                break;
            }//Spiner
            case 5: {
//                final List<String> items = Arrays.asList(((MultiSpinerHolder) holder).spiner.getContext().getResources().getStringArray(f.getEntries()));
//                MultiSpinner.MultiSpinnerListener onSelectedListener = (selected, id) -> CurrentSample.getFields().put((short) f.getColumn_id(), id);
//                String selected = "";
//                if (CurrentSample.getFields().containsKey((short) f.getColumn_id()))
//                    selected = CurrentSample.getFields().get((short) f.getColumn_id());
//
//                assert selected != null;
//                ((MultiSpinerHolder) holder).spiner.setItems(
//                        items,
//                        selected,
//                        onSelectedListener,
//                        f.getHint()
//                );
//
//                ((MultiSpinerHolder) holder).txtHint.setText(f.getHint());
                // final String[] items = ((MultiChipChoceHoldeer) holder).contactsCompletionView.getContext().getResources().getStringArray(f.getEntries());
                //     ((MultiChipChoceHoldeer) holder).contactsCompletionView.setAdapter(new ArrayAdapter<>(((MultiChipChoceHoldeer) holder).contactsCompletionView.getContext() , android.R.layout.simple_list_item_1, items));

                ((MultiChipChoceHoldeer) holder).contactsCompletionView.setAdapter(new ArrayAdapter<>(((MultiChipChoceHoldeer) holder).contactsCompletionView.getContext(), android.R.layout.simple_list_item_1, getStringMassivFromList(App.OrderInfo.getDocumentName())));

                if (CurrentSample.getFields().containsKey((short) (f.getColumn_id())))
                    SelectElements(CurrentSample.getFields().get((short) (f.getColumn_id())), ((MultiChipChoceHoldeer) holder).contactsCompletionView);
                ((MultiChipChoceHoldeer) holder).textInputLayout.setHint(f.getHint());


                ((MultiChipChoceHoldeer) holder).contactsCompletionView.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void afterTextChanged(Editable s) {
                        if (s.length() > 0) {
                            StringBuilder str = new StringBuilder(((MultiChipChoceHoldeer) holder).contactsCompletionView.getContentText().toString());
                            str.delete(str.length() - 2, str.length());
                            CurrentSample.getFields().put((short) (f.getColumn_id()), str.toString());
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
            }//Мультиспинер
            case 6: {
                Adapter = new ResearhAdapter(suggestions, SamplesHeader, CurrentSample, CurrentProb, ProbHeader);
                (Adapter).setMode(Attributes.Mode.Single);
                ((ResearchPanelHolder) holder).ResearchList.setAdapter(Adapter);
                ((ResearchPanelHolder) holder).ResearchList.setRecycledViewPool(viewPool);


                //Добавляет исследование
                ((ResearchPanelHolder) holder).btnAddReserch.setOnClickListener(v -> {
                    if (ProbFields.containsKey("5") && suggestions != null) {
                        short newid = getPositionKeyR(CurrentSample.getResearches());
                        Map<Short, ResearchRest> insertlist = new HashMap<>();
                        insertlist.put((short) (newid + 1), new ResearchRest(newid));
                        Adapter.insertdata(insertlist);
                        // Adapter.notifyDataSetChanged();
                        //((ResearchPanelHolder) holder).ResearchList.smoothScrollToPosition(Adapter.getItemCount() - 1);
                    } else {
                        if (suggestions == null && ProbFields.containsKey("5"))
                            ProbAdapter.adapter.notifyItemChanged(ProbAdapter.adapter.getIdForField("SampleList"));
                        Toast.makeText(((ResearchPanelHolder) holder).ResearchList.getContext(), ((ResearchPanelHolder) holder).ResearchList.getContext().getString(R.string.MaterialNoSelect), Toast.LENGTH_SHORT).show();
                    }
                });

                break;
            }//Исследования
            case 8: {
                ((DataFieldHolder) holder).Layout.setHint(f.getHint());
                //   ((DataFieldHolder) holder).field.setText(CurrentSample.getFields().get((short) f.getColumn_id()));

                if (CurrentSample.getFields().containsKey((short) (f.getColumn_id())))
                    try {
                        ((DataFieldHolder) holder).field.setText(new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Objects.requireNonNull(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(Objects.requireNonNull(CurrentSample.getFields().get((short) (f.getColumn_id())))))));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                final DatePickerDialog.OnDateSetListener Datapicker = (view, year, monthOfYear, dayOfMonth) -> ChangeData(year, monthOfYear, dayOfMonth, ((DataFieldHolder) holder).field);
                ((DataFieldHolder) holder).Layout.setEndIconOnClickListener(v -> new DatePickerDialog(Objects.requireNonNull(((DataFieldHolder) holder).Layout.getContext()), Datapicker,
                        dateAndTime.get(Calendar.YEAR),
                        dateAndTime.get(Calendar.MONTH),
                        dateAndTime.get(Calendar.DAY_OF_MONTH))
                        .show());
            }//DataField
        }
    }

    private String[] getStringMassivFromList(List<Document> list) {
        String[] mass = new String[list.size()];
        for (int i = 0; i < mass.length; i++) {
            mass[i] = list.get(i).getText();
        }
        return mass;
    }

    private void SelectElements(String containsKey, ContactsCompletionView View) {
        for (String s : containsKey.split(",")) {
            View.addObjectAsync(s);
        }
    }

    private void ChangeData(int year, int monthOfYear, int dayOfMonth, EditText Edt) {
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

        //  CurrentSample.getFields().put((short) (position), MessageFormat.format("{2}-{1}-{0}", formattedDayOfMonth, formattedMonth, String.valueOf(year)));
        Edt.setText(MessageFormat.format("{0} . {1} . {2}", formattedDayOfMonth, formattedMonth, String.valueOf(year)));
        Edt.requestFocus();
        Edt.setSelection(Edt.getText().length());
    }

    private Short getPositionKeyR(Map<Short, ResearchRest> List) {
        if (List.size() > 0)
            return new ArrayList<>(List.keySet()).get(List.size() - 1);
        else return 0;
    }

    @Override
    public int getItemCount() {
        return SamplesField.size();
    }
}