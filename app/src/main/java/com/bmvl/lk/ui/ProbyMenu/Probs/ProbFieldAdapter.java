package com.bmvl.lk.ui.ProbyMenu.Probs;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bmvl.lk.R;
import com.bmvl.lk.models.Research;
import com.bmvl.lk.ui.Create_Order.Field;
import com.daimajia.swipe.util.Attributes;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

class ProbFieldAdapter extends RecyclerView.Adapter{
    private LayoutInflater inflater;
    private static Calendar dateAndTime = Calendar.getInstance();
    private static List<Field> ProbFields;
    private static List<Field> ResearchFields;
    private static List<Research> Researchs = new ArrayList<>();

    ProbFieldAdapter(Context context,List<Field> Fields, List<Field> ResFields) {
        this.inflater = LayoutInflater.from(context);
        ProbFields = Fields;
        ResearchFields = ResFields;
    }

    @Override
    public int getItemViewType(int position) {
        return ProbFields.get(position).getType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                View view = inflater.inflate(R.layout.item_field, parent, false);
                return new ViewHolder(view);
            case 1:
                View view1 = inflater.inflate(R.layout.item_spiner, parent, false);
                return new ViewHolderSpiner(view1);
            case 3:
                View view3 = inflater.inflate(R.layout.item_check_button, parent, false);
                return new ViewHolderSwitch(view3);
            case 4:
                View view4 = inflater.inflate(R.layout.item_button_select, parent, false);
                return new ViewHolderButton(view4);
            case 5:
                View view5 = inflater.inflate(R.layout.item_multi_spinner, parent, false);
                return new ViewHolderMultiSpiner(view5);
            case 6:
                View view6 = inflater.inflate(R.layout.item_research_list, parent, false);
                return new ViewHolderResearchPanel(view6);
        }

        View view = inflater.inflate(R.layout.item_field, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        final Field f = ProbFields.get(position);
        if (f.getType() == 0) {

            ((ViewHolder) holder).Layout.setHint(f.getHint());
            ((ViewHolder) holder).field.setInputType(f.getInputType());
            ((ViewHolder) holder).field.setText(f.getValue());

            if (f.getIcon() != null) {
                ((ViewHolder) holder).Layout.setEndIconMode(TextInputLayout.END_ICON_CUSTOM);
                ((ViewHolder) holder).Layout.setEndIconDrawable(f.getIcon());

                if (f.isData()) {
                    final DatePickerDialog.OnDateSetListener Datapicker = new DatePickerDialog.OnDateSetListener() {
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            ChangeData(year, monthOfYear, dayOfMonth, ((ViewHolder) holder).field);
                        }
                    };
                    ((ViewHolder) holder).Layout.setEndIconOnClickListener(new View.OnClickListener() {
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

            if (f.isDoubleSize()) {
                ((ViewHolder) holder).field.setGravity(Gravity.START | Gravity.TOP);
                ((ViewHolder) holder).field.setMinLines(4);
                ((ViewHolder) holder).field.setLines(6);
                ((ViewHolder) holder).field.setSingleLine(false);
                ((ViewHolder) holder).field.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
            }
        } else if (f.getType() == 1) {
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(inflater.getContext(), f.getEntries(), android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            ((ViewHolderSpiner) holder).spiner.setAdapter(adapter);
            ((ViewHolderSpiner) holder).txtHint.setText(f.getHint());
        }

        switch (f.getType()){
            case 3:
                ((ViewHolderSwitch) holder).switchButton.setText(String.format("%s  ", f.getHint()));
                break;
            case 4:
                ((ViewHolderButton) holder).hint.setText(f.getHint());
                break;
            case 5:
              //  String[] countries = inflater.getContext().getResources().getStringArray(f.getEntries());
               // ArrayAdapter<String> adapter = new ArrayAdapter<String>(inflater.getContext(), android.R.layout.simple_spinner_item, countries);

              //  ((ViewHolderMultiSpiner) holder).spiner.setAdapter(adapter);
              //  ((ViewHolderMultiSpiner) holder).spiner.setItems(items, getString(R.string.documents), this);;



                List<String> items = Arrays.asList(inflater.getContext().getResources().getStringArray(f.getEntries()));

                 MultiSpinner.MultiSpinnerListener onSelectedListener = new MultiSpinner.MultiSpinnerListener() {
                    public void onItemsSelected(boolean[] selected) {
                        // Do something here with the selected items
                    }
                };

                ((ViewHolderMultiSpiner) holder).spiner.setItems(items, inflater.getContext().getString(R.string.for_all), onSelectedListener);
                ((ViewHolderMultiSpiner) holder).txtHint.setText(f.getHint());
                break;
            case 6:
                Researchs.add(new Research());
               // ((ViewHolderResearchPanel) holder).ResearchList.setHasFixedSize(true);

                final ResearhAdapter Adapter = new ResearhAdapter(inflater.getContext(), Researchs, ResearchFields);
                (Adapter).setMode(Attributes.Mode.Single);
                ((ViewHolderResearchPanel) holder).ResearchList.setAdapter(Adapter);


                ((ViewHolderResearchPanel) holder).btnAddReserch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        List<Research> insertlist = new ArrayList<>();
                        Researchs.add(new Research());
                        Adapter.insertdata(insertlist);
                        ((ViewHolderResearchPanel) holder).ResearchList.smoothScrollToPosition(Adapter.getItemCount()-1);
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

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextInputEditText field;
        final TextInputLayout Layout;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            field = itemView.findViewById(R.id.TextInput);
            Layout = itemView.findViewById(R.id.TextLayout);
        }
    }

    public static class ViewHolderSpiner extends RecyclerView.ViewHolder {
        final Spinner spiner;
        final TextView txtHint;

        ViewHolderSpiner(View view1) {
            super(view1);
            spiner = itemView.findViewById(R.id.spinner);
            txtHint = itemView.findViewById(R.id.hint);
        }
    }

    public static class ViewHolderSwitch extends ViewHolder {
        final SwitchMaterial switchButton;
        ViewHolderSwitch(View view3) {
            super(view3);
            switchButton = itemView.findViewById(R.id.my_switch);
        }
    }

    public static class ViewHolderButton extends ViewHolder {
        final TextView hint,path;
        final Button select_btn;
        ViewHolderButton(View view4) {
            super(view4);
            hint = itemView.findViewById(R.id.hint);
            path = itemView.findViewById(R.id.path);
            select_btn = itemView.findViewById(R.id.select);
        }
    }

    private static class ViewHolderMultiSpiner extends RecyclerView.ViewHolder {
        final MultiSpinner  spiner;
        final TextView txtHint;
        ViewHolderMultiSpiner(View view5) {
            super(view5);
            spiner = itemView.findViewById(R.id.spinner);
            txtHint = itemView.findViewById(R.id.hint);
        }
    }

    private static class ViewHolderResearchPanel extends RecyclerView.ViewHolder {
        final MaterialButton btnAddReserch;
        final RecyclerView ResearchList;
        ViewHolderResearchPanel(View view6) {
            super(view6);
            btnAddReserch = itemView.findViewById(R.id.addResearch);
            ResearchList = itemView.findViewById(R.id.researchList);
        }
    }
}
