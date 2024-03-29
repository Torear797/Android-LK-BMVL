package com.bmvl.lk.ui.ProbyMenu.Probs;

import android.app.DatePickerDialog;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bmvl.lk.App;
import com.bmvl.lk.R;
import com.bmvl.lk.Rest.AnswerCountries;
import com.bmvl.lk.Rest.AnswerIndicators;
import com.bmvl.lk.Rest.Material;
import com.bmvl.lk.Rest.NetworkService;
import com.bmvl.lk.Rest.Order.ProbyRest;
import com.bmvl.lk.Rest.Order.SamplesRest;
import com.bmvl.lk.Rest.Suggestion;
import com.bmvl.lk.ViewHolders.AutoCompleteFieldHolder;
import com.bmvl.lk.ViewHolders.DataFieldHolder;
import com.bmvl.lk.ViewHolders.MaterialFieldHolder;
import com.bmvl.lk.ViewHolders.MultiAutoCompleteHolder;
import com.bmvl.lk.ViewHolders.MultiChipChoceHoldeer;
import com.bmvl.lk.ViewHolders.MultiSpinerHolder;
import com.bmvl.lk.ViewHolders.OriginHolder;
import com.bmvl.lk.ViewHolders.PartyInfoHolder;
import com.bmvl.lk.ViewHolders.SamplesPanelHolder;
import com.bmvl.lk.ViewHolders.SelectButtonHolder;
import com.bmvl.lk.ViewHolders.SpinerHolder;
import com.bmvl.lk.ViewHolders.SwitchHolder;
import com.bmvl.lk.ViewHolders.TextViewHolder;
import com.bmvl.lk.data.ContactsCompletionView;
import com.bmvl.lk.data.Field;
import com.bmvl.lk.data.StringSpinnerAdapter;
import com.bmvl.lk.data.models.Document;
import com.bmvl.lk.ui.ProbyMenu.PartyInfo.PartyInfoFragment;
import com.bmvl.lk.ui.ProbyMenu.Probs.Sample.SamplesAdapter;
import com.bmvl.lk.ui.create_order.ChoceMaterialDialogFragment;
import com.bmvl.lk.ui.create_order.CreateOrderActivity;
import com.daimajia.swipe.util.Attributes;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bmvl.lk.ui.ProbyMenu.Probs.ProbsFragment.Materials;

public class ProbFieldAdapter extends RecyclerView.Adapter {
    private static Calendar dateAndTime = Calendar.getInstance();
    private List<Field> ProbFields;
    private List<Field> SampleFields; //Поля Образцов
    // private RecyclerView.RecycledViewPool viewPool;
    private TextView ProbHeader;

    private ProbyRest CurrentProb;
    private SamplesAdapter SamAdapter;

    private String[] Regions, Districts, Depth_of_selection;
    private int RegionPos, DistrictPos;


    // public static int id_field_144 = 0; //id Глубины отбора. Для обновления поля.

    private Map<String, Integer> id_fields = new HashMap<>(); //id полей для обновлений

    private List<Suggestion> suggestions; //Материалы;

    ProbFieldAdapter(List<Field> probFields, List<Field> sampleFields, ProbyRest prob, ProbAdapter.SimpleViewHolder holder) {
        this.ProbHeader = holder.infoProb;
        ProbFields = probFields;
        SampleFields = sampleFields;
        CurrentProb = prob;
    }

    @Override
    public int getItemViewType(int position) {
        switch (ProbFields.get(position).getType()) {
            case 1:
                return R.layout.item_spiner;
            case 3:
                return R.layout.item_check_button;
            case 4:
                return R.layout.item_button_select;
            case 5:
                //return R.layout.item_multi_spinner;
                return R.layout.item_multi_chip_choce;
            case 6:
                return R.layout.item_material_field;
            case 7:
                return R.layout.item_research_list;
            case 8:
                return R.layout.item_data_field;
            case 9:
                return R.layout.item_origin;
            case 10:
                return R.layout.item_party_info;
            case 11:
                return R.layout.item_auto_compiete_field;
            case 12:
                return R.layout.item_multi_auto_complete;
            default:
                return R.layout.item_field;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);

        switch (viewType) {
            case R.layout.item_spiner: {
                final SpinerHolder holder1 = new SpinerHolder(view);

//                AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
//                    @Override
//                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                        CurrentProb.getFields().put(GetColumn_id(holder1.getLayoutPosition()), String.valueOf(parent.getItemAtPosition(position)));
//
//                        // ((StringSpinnerAdapter)holder1.spiner.getAdapter()).setSelection(position);
//                    }
//
//                    @Override
//                    public void onNothingSelected(AdapterView<?> parent) {
//                    }
//                };
                // holder1.spiner.setOnItemSelectedListener(itemSelectedListener);

                if(!CreateOrderActivity.ReadOnly)
                holder1.spiner.post(() -> holder1.spiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent12, View view1, int position, long id) {
                        //    textView.setText(spinner.getSelectedItem().toString());
                        //  CurrentProb.getFields().put(GetColumn_id(holder1.getLayoutPosition()), String.valueOf(parent12.getItemAtPosition(position)));
                        CurrentProb.getFields().put(GetColumn_id(holder1.getLayoutPosition()), holder1.spiner.getSelectedItem().toString());
                        //  Log.d("SPINER", holder1.spiner.getSelectedItem().toString());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent12) {
                    }
                }));

                return holder1;
            } //Spiner
            case R.layout.item_check_button: {
                final SwitchHolder holder3 = new SwitchHolder(view);
                if(!CreateOrderActivity.ReadOnly)
                holder3.switchButton.setOnCheckedChangeListener((buttonView, isChecked) -> CurrentProb.getFields().put(GetColumn_id(holder3.getLayoutPosition()), String.valueOf(isChecked)));
                return holder3;
            }//Swith
            case R.layout.item_button_select: {
                return new SelectButtonHolder(view);
            }//SelectButton
            case R.layout.item_multi_spinner: {
                return new MultiSpinerHolder(view);
            }//MultiSpinerHolder
            case R.layout.item_material_field: {
                final MaterialFieldHolder holder6 = new MaterialFieldHolder(view);
                if(!CreateOrderActivity.ReadOnly)
                holder6.TextView.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void afterTextChanged(Editable s) {
                        if (!Objects.equals(CurrentProb.getFields().get("materialName"), s.toString())) {
                            short id_m = -1;
                            for (Material m : Materials) {
                                if (m.getText().contentEquals(s)) {
                                    id_m = m.getId();
                                    break;
                                }
                            }

                            if (CheckIndicators() && CurrentProb.getFields().containsKey("materialName") && id_m != -1) {
                                OpenDialog(holder6.TextView, id_m);
                            } else {

                                if (GetColumn_id(holder6.getLayoutPosition()).equals("5") && id_m != -1) {
                                    CurrentProb.getFields().put("materialName", String.valueOf(s));
                                    CurrentProb.getFields().put("5", String.valueOf(id_m));
                                    // ProbHeader.setText(MessageFormat.format("Вид материала: {0} Образцов: {1}", s, CurrentProb.getSamples().size()));
                                    ProbHeader.setText(MessageFormat.format("Вид материала: {0} Образцов: {1} шт. Цена {2} руб.", s, CurrentProb.getSamples().size(),
                                            CurrentProb.getPrice()
                                    ));
                                    getIndicators(id_m);
                                }
//                                else {
//                                    CurrentProb.getFields().remove("5");
//                                    CurrentProb.getFields().remove("materialName");
//                                    ProbHeader.setText(MessageFormat.format("Вид материала: {0} Образцов: {1}", ProbHeader.getContext().getString(R.string.MaterialNoSelectHeader), CurrentProb.getSamples().size()));
//                                }
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

                if(!CreateOrderActivity.ReadOnly)
                holder6.TextView.setOnItemClickListener((parent1, MeView, position, id) -> holder6.TextView.clearFocus());

                return holder6;
            }// Material
            case R.layout.item_research_list: {
                return new SamplesPanelHolder(view);
            }//Список Образцов
            case R.layout.item_data_field: {
                final DataFieldHolder holder = new DataFieldHolder(view);
                if(!CreateOrderActivity.ReadOnly)
                holder.field.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void afterTextChanged(Editable s) {
                        if (!String.valueOf(s).equals("") && s.toString().length() <= 10)
                            try {
                                CurrentProb.getFields().put(GetColumn_id(holder.getLayoutPosition()), new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Objects.requireNonNull(new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).parse(s.toString()))));
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
            case R.layout.item_origin: {
                return new OriginHolder(view);
            } //DopPanels Origin
            case R.layout.item_party_info: {
                return new PartyInfoHolder(view);
            } //DopPanels Info
            case R.layout.item_multi_chip_choce: {
                return new MultiChipChoceHoldeer(view);
            }
            case R.layout.item_auto_compiete_field: {
                final AutoCompleteFieldHolder holder = new AutoCompleteFieldHolder(view);
                holder.TextView.setThreshold(2);
                if(!CreateOrderActivity.ReadOnly)
                holder.TextView.setOnItemClickListener((parent1, MeView, position, id) -> {
                    CurrentProb.getFields().put(GetColumn_id(holder.getLayoutPosition()), holder.TextView.getAdapter().getItem(position).toString());

                    switch (ProbFields.get(holder.getLayoutPosition()).getColumn_id()) {
                        case 27: {
                            Regions = null;
                            Districts = null;
                            CurrentProb.getFields().remove("28");
                            CurrentProb.getFields().remove("57");
                            notifyItemChanged(RegionPos);
                            notifyItemChanged(DistrictPos);
                            getRegions(holder.TextView.getAdapter().getItem(position).toString());
                            break;
                        }
                        case 28: {
                            Districts = null;
                            CurrentProb.getFields().remove("57");
                            notifyItemChanged(DistrictPos);
                            getDistricts(holder.TextView.getAdapter().getItem(position).toString(), CurrentProb.getFields().get("27"));
                            break;
                        }
                    }

                    holder.TextView.clearFocus();
                });
                return holder;
            }
            case R.layout.item_multi_auto_complete: {
                return new MultiAutoCompleteHolder(view);
            }
            default: {
                final TextViewHolder holder = new TextViewHolder(view);
                if(!CreateOrderActivity.ReadOnly)
                holder.field.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void afterTextChanged(Editable s) {
                        if (!String.valueOf(s).equals("")) {
                            //&& !Objects.equals(CurrentProb.getFields().get(GetColumn_id(holder.getLayoutPosition())), String.valueOf(s))
                            CurrentProb.getFields().put(GetColumn_id(holder.getLayoutPosition()), String.valueOf(s));
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

    public Map<String, Integer> getPositionList() {
        return id_fields;
    }

    public int getIdForField(String id) {
        if (id_fields.containsKey(id))
            return id_fields.get(id);
        else return -1;
    }

    public List<Suggestion> getIndicatorList() {
        return suggestions;
    }

    public List<Field> getSamplesFields() {
        return SampleFields;
    }

    private void getRegions(String country_name) {
        NetworkService.getInstance()
                .getJSONApi()
                .getRegions(App.UserAccessData.getToken(), country_name, "")
                .enqueue(new Callback<AnswerCountries>() {
                    @Override
                    public void onResponse(@NonNull Call<AnswerCountries> call, @NonNull Response<AnswerCountries> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Regions = new String[response.body().getSuggestions().size()];
                            for (int i = 0; i < response.body().getSuggestions().size(); i++) {
                                Regions[i] = response.body().getSuggestions().get(i).getValue();
                            }
                            notifyItemChanged(RegionPos);
                            //notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<AnswerCountries> call, @NonNull Throwable t) {
                    }
                });
    }

    private void getDistricts(String region_name, String country_name) {
        NetworkService.getInstance()
                .getJSONApi()
                .getDistricts(App.UserAccessData.getToken(), country_name, region_name, "")
                .enqueue(new Callback<AnswerCountries>() {
                    @Override
                    public void onResponse(@NonNull Call<AnswerCountries> call, @NonNull Response<AnswerCountries> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Districts = new String[response.body().getSuggestions().size()];
                            for (int i = 0; i < response.body().getSuggestions().size(); i++) {
                                Districts[i] = response.body().getSuggestions().get(i).getValue();
                            }
                            notifyItemChanged(DistrictPos);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<AnswerCountries> call, @NonNull Throwable t) {
                    }
                });
    }

    private boolean CheckIndicators() {
        if (CurrentProb.getSamples().size() > 0) return true;

        for (short i = 0; i < CurrentProb.getSamples().size(); i++) {
            if (Objects.requireNonNull(CurrentProb.getSamples().get(getPositionKey(i, CurrentProb.getSamples()))).getResearches().size() > 0)
                return true;
        }

        return false;
    }

    private void OpenDialog(AutoCompleteTextView TextView, int id) {
        new MaterialAlertDialogBuilder(TextView.getContext())
                .setTitle(R.string.attention)
                .setMessage(R.string.drop_material_text)
                .setNegativeButton(R.string.cancel,
                        (dialog, which) -> {
                            TextView.setText(CurrentProb.getFields().get("materialName"));
                            TextView.clearFocus();
                        })
                .setPositiveButton(R.string.Continue,
                        (dialog, which) -> {
                            CurrentProb.getSamples().clear();
                            CurrentProb.getFields().remove("144");
                            notifyItemChanged(id_fields.get("144"));

                            if (CreateOrderActivity.order_id == 1)
                                CurrentProb.getSamples().put((short) 1, new SamplesRest((short) 0));

                            SamAdapter.notifyDataSetChanged();

                            CurrentProb.getFields().put("materialName", TextView.getText().toString());
                            CurrentProb.getFields().put("5", String.valueOf(id));
                            ProbHeader.setText(MessageFormat.format("Вид материала: {0} Образцов: {1}", TextView.getText().toString(), CurrentProb.getSamples().size()));
                            getIndicators(id);
                            TextView.clearFocus();
                        })
                .show();
    }

    private String GetColumn_id(int position) {
        return String.valueOf(ProbFields.get(position).getColumn_id());
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        final Field f = ProbFields.get(position);

        switch (f.getType()) {
            case (byte) 0: {
                if (f.getInputType() == InputType.TYPE_NULL)
                    ((TextViewHolder) holder).Layout.setBoxBackgroundColor(((TextViewHolder) holder).Layout.getContext().getResources().getColor(R.color.field_inactive));

                ((TextViewHolder) holder).Layout.setHint(f.getHint());
                ((TextViewHolder) holder).field.setInputType(f.getInputType());
                ((TextViewHolder) holder).field.setText(CurrentProb.getFields().get(String.valueOf(f.getColumn_id())));

                if (f.getIcon() != null) {
                    ((TextViewHolder) holder).Layout.setEndIconMode(TextInputLayout.END_ICON_CUSTOM);
                    ((TextViewHolder) holder).Layout.setEndIconDrawable(f.getIcon());
                }

                if (f.getColumn_id() == 144) {
                    id_fields.put("144", position);
                    //id_field_144 = position;
                }

//                if (position == ProbFields.size() - 2)
//                    id_fields.put("LastField", position);
//
//                if (CreateOrderActivity.NoChoiceSamples) {
//                    CreateOrderActivity.NoChoiceSamples = false;
//                    ((TextViewHolder) holder).field.setFocusableInTouchMode(true);
//                    ((TextViewHolder) holder).field.requestFocus();
//                }
                if(CreateOrderActivity.ReadOnly)((TextViewHolder) holder).Layout.setEnabled(false);
                break;
            } //Текстовое поле
            case (byte) 1: {
                StringSpinnerAdapter adapter;
                if (f.getEntries() != -1) {
                    // CharSpinnerAdapter adapter = (CharSpinnerAdapter)ArrayAdapter.createFromResource(((SpinerHolder) holder).spiner.getContext(), f.getEntries(), android.R.layout.simple_spinner_item);
                    // CharSpinnerAdapter adapter = new CharSpinnerAdapter(((SpinerHolder) holder).spiner.getContext(), android.R.layout.simple_spinner_item, f.getEntries(), ((SpinerHolder) holder).spiner);
                    adapter = new StringSpinnerAdapter(((SpinerHolder) holder).spiner.getContext(), android.R.layout.simple_spinner_item, ((SpinerHolder) holder).spiner.getContext().getResources().getStringArray(f.getEntries()), ((SpinerHolder) holder).spiner);
                } else {
                    adapter = new StringSpinnerAdapter(((SpinerHolder) holder).spiner.getContext(), android.R.layout.simple_spinner_item, f.getSpinerData(), ((SpinerHolder) holder).spiner);

                    // ((SpinerHolder) holder).spiner.setAdapter(new StringSpinnerAdapter(((SpinerHolder) holder).spiner.getContext(),android.R.layout.simple_spinner_item, f.getSpinerData(), ((SpinerHolder) holder).spiner));
                    //StringSpinnerAdapter adapter =;

                }
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                ((SpinerHolder) holder).spiner.setAdapter(adapter);

                if (CurrentProb.getFields().containsKey(String.valueOf(f.getColumn_id())))
                    ((SpinerHolder) holder).spiner.setSelection(adapter.getPosition(CurrentProb.getFields().get(String.valueOf(f.getColumn_id()))));


                ((SpinerHolder) holder).txtHint.setText(f.getHint());
                // ((SpinerHolder) holder).layout.setHint(f.getHint());
                if(CreateOrderActivity.ReadOnly)((SpinerHolder) holder).spiner.setEnabled(false);
                break;
            } //Spiner
            case (byte) 3: {
                ((SwitchHolder) holder).switchButton.setText(String.format("%s  ", f.getHint()));

                if (CurrentProb.getFields().containsKey(String.valueOf(f.getColumn_id())))
                    ((SwitchHolder) holder).switchButton.setEnabled(Boolean.parseBoolean(CurrentProb.getFields().get(String.valueOf(f.getColumn_id()))));

                if(CreateOrderActivity.ReadOnly)((SwitchHolder) holder).switchButton.setEnabled(false);
                break;
            } //Swith
            case (byte) 4: {
                ((SelectButtonHolder) holder).hint.setText(f.getHint());
                break;
            } //SelectButtonHolder
            case (byte) 5: {
//                final List<String> items = Arrays.asList(((MultiSpinerHolder) holder).spiner.getContext().getResources().getStringArray(f.getEntries()));
//                MultiSpinner.MultiSpinnerListener onSelectedListener = (selected, id) -> CurrentProb.getFields().put(String.valueOf(f.getColumn_id()), id);
//                String selected = "";
//                if (CurrentProb.getFields().containsKey(String.valueOf(f.getColumn_id())))
//                    selected = CurrentProb.getFields().get(String.valueOf(f.getColumn_id()));
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

                //  final String[] items = ((MultiChipChoceHoldeer) holder).contactsCompletionView.getContext().getResources().getStringArray(f.getEntries());
                ((MultiChipChoceHoldeer) holder).contactsCompletionView.setAdapter(new ArrayAdapter<>(((MultiChipChoceHoldeer) holder).contactsCompletionView.getContext(), android.R.layout.simple_list_item_1, getStringMassivFromList(App.OrderInfo.getDocumentName())));

                if (CurrentProb.getFields().containsKey(String.valueOf(f.getColumn_id())))
                    SelectElements(Objects.requireNonNull(CurrentProb.getFields().get(String.valueOf(f.getColumn_id()))), ((MultiChipChoceHoldeer) holder).contactsCompletionView);
                ((MultiChipChoceHoldeer) holder).textInputLayout.setHint(f.getHint());

                if(!CreateOrderActivity.ReadOnly) {
                    ((MultiChipChoceHoldeer) holder).contactsCompletionView.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void afterTextChanged(Editable s) {
                            if (s.length() > 0) {
                                StringBuilder str = new StringBuilder(((MultiChipChoceHoldeer) holder).contactsCompletionView.getContentText());
                                str.delete(str.length() - 2, str.length());
                                CurrentProb.getFields().put(String.valueOf(f.getColumn_id()), str.toString());
                            }
                        }

                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                        }
                    });
                } else ((MultiChipChoceHoldeer) holder).textInputLayout.setEnabled(false);
                break;
            } //Мультиспинер
            case (byte) 6: {
                ((MaterialFieldHolder) holder).Layout.setHint(f.getHint());
                id_fields.put("materialName", position);

                String[] mMaterials = new String[ProbsFragment.Materials.size()];

                for (int i = 0; i < mMaterials.length; i++) {
                    mMaterials[i] = ProbsFragment.Materials.get(i).getText();
                }
                ((MaterialFieldHolder) holder).TextView.setAdapter(new ArrayAdapter<>(((MaterialFieldHolder) holder).TextView.getContext(), android.R.layout.simple_spinner_item, mMaterials));
                ((MaterialFieldHolder) holder).TextView.setThreshold(2);

                if (CurrentProb.getFields().containsKey(String.valueOf(f.getColumn_id())))
                    ((MaterialFieldHolder) holder).TextView.setText(CurrentProb.getFields().get("materialName"));

                if (CreateOrderActivity.NoChoiceMaterial) {
                    CreateOrderActivity.NoChoiceMaterial = false;
                    ((MaterialFieldHolder) holder).TextView.requestFocus();
                }

                if(!CreateOrderActivity.ReadOnly)
                ((MaterialFieldHolder) holder).ChoceBtn.setOnClickListener(v -> new ChoceMaterialDialogFragment(((MaterialFieldHolder) holder).TextView).show(((AppCompatActivity) v.getContext()).getSupportFragmentManager(), "ChoceMaterialDialogFragment"));
                else{
                    ((MaterialFieldHolder) holder).ChoceBtn.setVisibility(View.GONE);
                    ((MaterialFieldHolder) holder).Layout.setEnabled(false);
                }
                break;
            } //Materials Field
            case (byte) 7: {
                SamAdapter = new SamplesAdapter(SampleFields, CurrentProb, suggestions, id_fields, ProbHeader);
                (SamAdapter).setMode(Attributes.Mode.Single);
                ((SamplesPanelHolder) holder).SampleList.setAdapter(SamAdapter);
                id_fields.put("SampleList", position);
                // ((SamplesPanelHolder) holder).SampleList.setRecycledViewPool(viewPool);

                if (CreateOrderActivity.order_id != 1 && CreateOrderActivity.order_id != 8) {
                    //Добавление образца
                    ((SamplesPanelHolder) holder).btnAddSample.setOnClickListener(v -> {
                        if (CurrentProb.getFields().containsKey("5")) {
                            short newid = getPositionKey(CurrentProb.getSamples().size() - 1, CurrentProb.getSamples());
                            Map<Short, SamplesRest> insertlist = new HashMap<>();
                            insertlist.put((short) (newid + 1), new SamplesRest(newid));
                            SamAdapter.insertdata(insertlist);
                            //SamAdapter.AddSample(newid);
                            // SamAdapter.notifyDataSetChanged();

                            //Инициализация глубины отбора
                            if (CreateOrderActivity.order_id == 3) {
                                //Образец №" + 1 + ": "

                                if (CurrentProb.getFields().containsKey("144") && Objects.requireNonNull(CurrentProb.getFields().get("144")).length() > 2)
                                    CurrentProb.getFields().put("144", CurrentProb.getFields().get("144") + ", " + "Образец №" + (CurrentProb.getSamples().size()) + ": ");
                                else
                                    CurrentProb.getFields().put("144", "Образец №" + (CurrentProb.getSamples().size()) + ": ");

                                notifyItemChanged(id_fields.get("144"));
                            }

                            //  AddSamples();
                            //  ((SamplesPanelHolder) holder).SampleList.smoothScrollToPosition(SamAdapter.getItemCount() - 1);
                        } else
                            Toast.makeText(((SamplesPanelHolder) holder).SampleList.getContext(), ((SamplesPanelHolder) holder).SampleList.getContext().getString(R.string.MaterialNoSelect), Toast.LENGTH_SHORT).show();
                    });
                } else {
                    ((SamplesPanelHolder) holder).Header.setVisibility(View.GONE);
                    ((SamplesPanelHolder) holder).btnAddSample.setVisibility(View.GONE);
                }

                break;
            } // Образцы
            case (byte) 8: {
                ((DataFieldHolder) holder).Layout.setHint(f.getHint());
                //   ((DataFieldHolder) holder).field.setText(CurrentProb.getFields().get(String.valueOf(f.getColumn_id())));

                if (CurrentProb.getFields().containsKey(String.valueOf(f.getColumn_id())))
                    try {
                        ((DataFieldHolder) holder).field.setText(new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Objects.requireNonNull(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(Objects.requireNonNull(CurrentProb.getFields().get(String.valueOf(f.getColumn_id())))))));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                final DatePickerDialog.OnDateSetListener Datapicker = (view, year, monthOfYear, dayOfMonth) -> ChangeData(year, monthOfYear, dayOfMonth, ((DataFieldHolder) holder).field);
                if(!CreateOrderActivity.ReadOnly)
                ((DataFieldHolder) holder).Layout.setEndIconOnClickListener(v -> new DatePickerDialog(Objects.requireNonNull(((DataFieldHolder) holder).Layout.getContext()), Datapicker,
                        dateAndTime.get(Calendar.YEAR),
                        dateAndTime.get(Calendar.MONTH),
                        dateAndTime.get(Calendar.DAY_OF_MONTH))
                        .show());
                else ((DataFieldHolder) holder).Layout.setEnabled(false);
                break;
            }//DataField
            case (byte) 9: {
                //   PartyInfoFragment yfc = ;
                //   Bundle bundle = new Bundle();
                // bundle.putInt("tag", intInformation);
                //  yfc.setArguments(bundle);
                boolean ReadOnly = Boolean.parseBoolean(CreateOrderActivity.order.getFields().get((short) 120));
                if (ReadOnly) ((OriginHolder) holder).RightTxt.setText(R.string.no_read_dop_menu);
                else ((OriginHolder) holder).RightTxt.setText("");
                ((CreateOrderActivity) ((OriginHolder) holder).head.getContext()).getSupportFragmentManager().beginTransaction()
                        .replace(((OriginHolder) holder).Frame.getId(), new PartyInfoFragment(
                                (byte) 1,
                                ReadOnly,
                                CurrentProb.getFields()
                        )).commit();

                break;
            }//DopPanel Origin
            case (byte) 10: {
                boolean ReadOnly = Boolean.parseBoolean(CreateOrderActivity.order.getFields().get((short) 121));
                if (ReadOnly)
                    ((PartyInfoHolder) holder).RightTxt.setText(R.string.no_read_dop_menu);
                else ((PartyInfoHolder) holder).RightTxt.setText("");
                ((CreateOrderActivity) ((PartyInfoHolder) holder).head.getContext()).getSupportFragmentManager().beginTransaction()
                        .replace(((PartyInfoHolder) holder).Frame.getId(), new PartyInfoFragment(
                                (byte) 2,
                                ReadOnly,
                                CurrentProb.getFields()
                        )).commit();
                break;
            }//DopPanel Info
            case (byte) 11: {
                ((AutoCompleteFieldHolder) holder).Layout.setHint(f.getHint());
                ((AutoCompleteFieldHolder) holder).TextView.setText(CurrentProb.getFields().get(String.valueOf(f.getColumn_id())));

                switch (f.getColumn_id()) {
                    case 27: {
                        ((AutoCompleteFieldHolder) holder).TextView.setAdapter(new ArrayAdapter<>(((AutoCompleteFieldHolder) holder).TextView.getContext(), android.R.layout.simple_spinner_item, ProbsFragment.Countries));
                        break;
                    }
                    case 28: {
                        RegionPos = position;
                        if (Regions != null) {
                            ((AutoCompleteFieldHolder) holder).TextView.setAdapter(new ArrayAdapter<>(((AutoCompleteFieldHolder) holder).TextView.getContext(), android.R.layout.simple_spinner_item, Regions));
                            ((AutoCompleteFieldHolder) holder).Layout.setEnabled(true);
                        } else
                            ((AutoCompleteFieldHolder) holder).Layout.setEnabled(false);
                        break;
                    }
                    case 57: {
                        DistrictPos = position;
                        if (Districts != null) {
                            ((AutoCompleteFieldHolder) holder).TextView.setAdapter(new ArrayAdapter<>(((AutoCompleteFieldHolder) holder).TextView.getContext(), android.R.layout.simple_spinner_item, Districts));
                            ((AutoCompleteFieldHolder) holder).Layout.setEnabled(true);
                        } else
                            ((AutoCompleteFieldHolder) holder).Layout.setEnabled(false);
                        break;
                    }
                }
                if(CreateOrderActivity.ReadOnly)((AutoCompleteFieldHolder) holder).Layout.setEnabled(false);
                break;
            } //AutoCompleteFieldHolder
//            case (byte)12:{
//                ((MultiAutoCompleteHolder) holder).Layout.setHint(f.getHint());
//                ((MultiAutoCompleteHolder) holder).TextView.setText(CurrentProb.getFields().get(String.valueOf(f.getColumn_id())));
//                ((MultiAutoCompleteHolder) holder).TextView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
//                //((MultiAutoCompleteHolder) holder).TextView.setAdapter(new ArrayAdapter<>(, android.R.layout.simple_spinner_item, Regions));
//
//                if(CurrentProb.getFields().containsKey(String.valueOf(f.getColumn_id()))) {
//                    Depth_of_selection = Objects.requireNonNull(CurrentProb.getFields().get(String.valueOf(f.getColumn_id()))).split(", ");
//
//                    ((MultiAutoCompleteHolder) holder).TextView.setAdapter(new ArrayAdapter<>(((MultiAutoCompleteHolder) holder).TextView.getContext(),
//                            android.R.layout.simple_dropdown_item_1line, Depth_of_selection));
//
//                    ((MultiAutoCompleteHolder) holder).TextView.setText(CurrentProb.getFields().get(String.valueOf(f.getColumn_id())));
//                }
//
//                if(f.getColumn_id() == 144) {
//                    id_field_144 = position;
//                    //((MultiAutoCompleteHolder) holder).Layout.setEnabled(false);
//                    ((MultiAutoCompleteHolder) holder).TextView.setThreshold(1000);
//                    ((MultiAutoCompleteHolder) holder).TextView.setInputType(InputType.TYPE_NULL);
//                    ((MultiAutoCompleteHolder) holder).Layout.setEndIconActivated(false);
//                    ((MultiAutoCompleteHolder) holder).Layout.setEndIconCheckable(false);
//                    //((MultiAutoCompleteHolder) holder).TextView.selectAll();
//
//                }
//                break;
//            }
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

    private Short getPositionKey(int position, Map<Short, SamplesRest> Samples) {
        if (Samples.size() > 0)
            return new ArrayList<>(Samples.keySet()).get(position);
        else return 0;
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

        //CurrentProb.getFields().put(String.valueOf(position), MessageFormat.format("{2}-{1}-{0}", formattedDayOfMonth, formattedMonth, String.valueOf(year)));
        Edt.setText(MessageFormat.format("{0} . {1} . {2}", formattedDayOfMonth, formattedMonth, String.valueOf(year)));
        Edt.requestFocus();
        Edt.setSelection(Edt.getText().length());
    }

    @Override
    public int getItemCount() {
        return ProbFields.size();
    }

    private void getIndicators(final int id) {
        NetworkService.getInstance()
                .getJSONApi()
                .getIndicators(App.UserAccessData.getToken(), id, "")
                .enqueue(new Callback<AnswerIndicators>() {
                    @Override
                    public void onResponse(@NonNull Call<AnswerIndicators> call, @NonNull Response<AnswerIndicators> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            suggestions = response.body().getSuggestions();
                            notifyItemChanged(id_fields.get("SampleList"));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<AnswerIndicators> call, @NonNull Throwable t) {
                    }
                });
    }
}