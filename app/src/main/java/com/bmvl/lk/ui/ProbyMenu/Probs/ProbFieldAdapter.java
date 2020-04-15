package com.bmvl.lk.ui.ProbyMenu.Probs;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bmvl.lk.R;
import com.bmvl.lk.ViewHolders.MultiSpinerHolder;
import com.bmvl.lk.ViewHolders.ResearchPanelHolder;
import com.bmvl.lk.ViewHolders.SamplesPanelHolder;
import com.bmvl.lk.ViewHolders.SelectButtonHolder;
import com.bmvl.lk.ViewHolders.SpinerHolder;
import com.bmvl.lk.ViewHolders.SwitchHolder;
import com.bmvl.lk.ViewHolders.TextViewHolder;
import com.bmvl.lk.models.Research;
import com.bmvl.lk.models.Samples;
import com.bmvl.lk.ui.Create_Order.Field;
import com.bmvl.lk.ui.ProbyMenu.Probs.Research.ResearhAdapter;
import com.bmvl.lk.ui.ProbyMenu.Probs.Sample.SamplesAdapter;
import com.daimajia.swipe.util.Attributes;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class ProbFieldAdapter extends RecyclerView.Adapter {
    private LayoutInflater inflater;
    private static Calendar dateAndTime = Calendar.getInstance();
    private static List<Field> ProbFields;
    private static List<Field> ResearchFields;
    private static List<Field> SampleFields; //Поля Образцов
    //private List<Research> Researchs; //Исследования конкретной пробы

    private int ProbID;

    //  private static List<Samples> Samples = new ArrayList<>();

    ProbFieldAdapter(Context context, List<Field> Fields, List<Field> ResFields, int id) {
        this.inflater = LayoutInflater.from(context);
        ProbFields = Fields;
        ResearchFields = ResFields;
        ProbID = id;
    }

    ProbFieldAdapter(Context context, List<Field> probFields, List<Field> researchFields, List<Field> sampleFields, int id) {
        this.inflater = LayoutInflater.from(context);
        ProbFields = probFields;
        ResearchFields = researchFields;
        SampleFields = sampleFields;
        ProbID = id;
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
                View view1 = inflater.inflate(R.layout.item_spiner, parent, false);
                return new SpinerHolder(view1);
            case 3:
                View view3 = inflater.inflate(R.layout.item_check_button, parent, false);
                return new SwitchHolder(view3);
            case 4:
                View view4 = inflater.inflate(R.layout.item_button_select, parent, false);
                return new SelectButtonHolder(view4);
            case 5:
                View view5 = inflater.inflate(R.layout.item_multi_spinner, parent, false);
                return new MultiSpinerHolder(view5);
            case 6:
                View view6 = inflater.inflate(R.layout.item_research_list, parent, false);
                return new ResearchPanelHolder(view6);
            case 7:
                View view7 = inflater.inflate(R.layout.item_research_list, parent, false);
                return new SamplesPanelHolder(view7);
            default:
                View view = inflater.inflate(R.layout.item_field, parent, false);
                return new TextViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        final Field f = ProbFields.get(position);
        // final Field f = ProbsFragment.ProbFields.get(position);
        if (f.getType() == 0) {

            ((TextViewHolder) holder).Layout.setHint(f.getHint());
            ((TextViewHolder) holder).field.setInputType(f.getInputType());
            ((TextViewHolder) holder).field.setText(f.getValue());

            if (f.getIcon() != null) {
                ((TextViewHolder) holder).Layout.setEndIconMode(TextInputLayout.END_ICON_CUSTOM);
                ((TextViewHolder) holder).Layout.setEndIconDrawable(f.getIcon());

                if (f.isData()) {
                    final DatePickerDialog.OnDateSetListener Datapicker = new DatePickerDialog.OnDateSetListener() {
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            ChangeData(year, monthOfYear, dayOfMonth, ((TextViewHolder) holder).field);
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

//            if (f.isDoubleSize()) {
//                ((TextViewHolder) holder).field.setGravity(Gravity.START | Gravity.TOP);
//                ((TextViewHolder) holder).field.setMinLines(4);
//                ((TextViewHolder) holder).field.setLines(6);
//                ((TextViewHolder) holder).field.setSingleLine(false);
//                ((TextViewHolder) holder).field.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
//            }
        } else if (f.getType() == 1) {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(inflater.getContext(), f.getEntries(), android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            ((SpinerHolder) holder).spiner.setAdapter(adapter);
            ((SpinerHolder) holder).txtHint.setText(f.getHint());
        }

        switch (f.getType()) {
            case 3:
                ((SwitchHolder) holder).switchButton.setText(String.format("%s  ", f.getHint()));
                break;
            case 4:
                ((SelectButtonHolder) holder).hint.setText(f.getHint());
                break;
            case 5:
                List<String> items = Arrays.asList(inflater.getContext().getResources().getStringArray(f.getEntries()));
                MultiSpinner.MultiSpinnerListener onSelectedListener = new MultiSpinner.MultiSpinnerListener() {
                    public void onItemsSelected(boolean[] selected) {
                        // Do something here with the selected items
                    }
                };
                ((MultiSpinerHolder) holder).spiner.setItems(items, inflater.getContext().getString(R.string.for_all), onSelectedListener);
                ((MultiSpinerHolder) holder).txtHint.setText(f.getHint());
                break;
            case 6:
                //  Researchs = ProbsFragment.getResearchsList(ProbID);
                // ((ViewHolderResearchPanel) holder).ResearchList.setHasFixedSize(true);

                final ResearhAdapter Adapter = new ResearhAdapter(inflater.getContext(), ProbsFragment.getResearchsList(ProbID), ResearchFields);
                (Adapter).setMode(Attributes.Mode.Single);
                ((ResearchPanelHolder) holder).ResearchList.setAdapter(Adapter);

                ((ResearchPanelHolder) holder).btnAddReserch.setText("Добавить исследование ");
                ((ResearchPanelHolder) holder).btnAddReserch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //((ResearchPanelHolder) holder).btnAddReserch.setText("Добавить исследование " + ProbID);
                        List<Research> insertlist = new ArrayList<>();
                        //insertlist.add(new Research());
                        ProbsFragment.getResearchsList(ProbID).add(new Research());
                        // Adapter.notifyDataSetChanged();
                        Adapter.insertdata(insertlist);
                        ((ResearchPanelHolder) holder).ResearchList.smoothScrollToPosition(Adapter.getItemCount() - 1);
                    }
                });

                break;
            case 7:
                // Samples.add(new Samples(0, Samples.size()));
                final List<Samples> Sample = ProbsFragment.getSampleList(ProbID);

                final SamplesAdapter SamAdapter = new SamplesAdapter(inflater.getContext(), ProbsFragment.getSampleResearchList(ProbID), ResearchFields, Sample, SampleFields, ProbID);
                (SamAdapter).setMode(Attributes.Mode.Single);
                ((SamplesPanelHolder) holder).SampleList.setAdapter(SamAdapter);

                ((SamplesPanelHolder) holder).btnAddSample.setText("Добавить образец");
                ((SamplesPanelHolder) holder).btnAddSample.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        List<Samples> insertlist = new ArrayList<>();
                        insertlist.add(new Samples(ProbID, Sample.size() + 1));
                      //  Sample.add(new Samples(ProbID, Sample.size() + 1));
                       // ProbsFragment.getSampleList().add(new Samples(ProbID, Sample.size() + 1));
                        SamAdapter.insertdata(insertlist);
                        ((SamplesPanelHolder) holder).SampleList.smoothScrollToPosition(SamAdapter.getItemCount() - 1);
                    }
                });

                break;
        }
    }

    private void ChangeData(int year, int monthOfYear, int dayOfMonth, EditText Edt) {
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
        Edt.setText(formattedDayOfMonth + "-" + formattedMonth + "-" + year);
    }

    @Override
    public int getItemCount() {
        return ProbFields.size();
    }
}