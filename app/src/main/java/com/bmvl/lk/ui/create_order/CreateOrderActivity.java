package com.bmvl.lk.ui.create_order;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bmvl.lk.App;
import com.bmvl.lk.R;
import com.bmvl.lk.Rest.NetworkService;
import com.bmvl.lk.Rest.Order.AnswerSendOrder;
import com.bmvl.lk.Rest.Order.ProbyRest;
import com.bmvl.lk.Rest.Order.SamplesRest;
import com.bmvl.lk.Rest.Order.SendOrder;
import com.bmvl.lk.Rest.StandardAnswer;
import com.bmvl.lk.data.Field;
import com.bmvl.lk.data.FileUtils;
import com.bmvl.lk.data.SpacesItemDecoration;
import com.bmvl.lk.data.models.Orders;
import com.bmvl.lk.ui.ProbyMenu.Probs.ProbAdapter;
import com.bmvl.lk.ui.ProbyMenu.Probs.ProbsFragment;
import com.bmvl.lk.ui.ProbyMenu.Probs.Sample.SampleItemDialog;
import com.bmvl.lk.ui.ProbyMenu.ProbyMenuFragment;
import com.bmvl.lk.ui.order.OrderFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.orhanobut.hawk.Hawk;

import java.io.File;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateOrderActivity extends AppCompatActivity {

    public static List<Field> Fields = new ArrayList<>(); //Поля заявки
    public static byte order_id;
    private ProgressBar bar;
    private MaterialCheckBox c_box_accept_rules;

    public static SendOrder order;
    private boolean Edit; //Флаг, истина - заявка редактируется.
    public static FieldsAdapter adapter;
    private static String act_of_selection;
    private FrameLayout Frame;
    private byte status;
    public static boolean IsPattern;
    private static final int LOAD_ACT_OF_SELECTION = 1;

    private File FileActOfSelection;
    private TextView pathForActOfSelection;

    private NestedScrollView nestedScrollView;

    public static boolean NoChoiceMaterial = false;
    public static boolean NoChoiceSamples = false;
    public static boolean ReadOnly = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_order);

        final TextView OrderName = findViewById(R.id.Order_name);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        final MaterialButton btn = findViewById(R.id.Create);
        //    final RecyclerView recyclerView = findViewById(R.id.FieldList);
        c_box_accept_rules = findViewById(R.id.AcceptcheckBox);
        Frame = findViewById(R.id.Menu_proby_fragment);
        bar = findViewById(R.id.ProgressBar);

        nestedScrollView = findViewById(R.id.scrollView2);

        status = getIntent().getByteExtra("status", (byte) 0);
        act_of_selection = getIntent().getStringExtra("ACT");
        //  Log.d("STATUS", " " + status);
        ReadOnly = getIntent().getBooleanExtra("ReadOnly", false);
        if(ReadOnly) {
            btn.setVisibility(View.GONE);
        }

        IsPattern = getIntent().getBooleanExtra("Pattern", false);
        Edit = getIntent().getBooleanExtra("isEdit", false);
        if (!Edit) {
            if (getIntent().getBooleanExtra("CreatePattern", false))
                order = (SendOrder) getIntent().getSerializableExtra(SendOrder.class.getSimpleName());
            else
                order = new SendOrder(getIntent().getByteExtra("type_id", (byte) 1));
            toolbar.setTitle(R.string.new_order);
        } else {
            order = (SendOrder) getIntent().getSerializableExtra(SendOrder.class.getSimpleName());
            assert order != null;
            //   Log.d("JSON", order.getJsonOrder());
            toolbar.setTitle(R.string.edit_order);
            btn.setText(R.string.save_text);
        }
        order_id = order.getType_id();
        if (IsPattern) order.setPattern((byte) 1);

        setSupportActionBar(toolbar);
        OrderName.setText(getResources().getStringArray(R.array.order_name)[order_id - 1]);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        new MyTask(this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void EnableTable(FrameLayout Frame) {
        if(!ReadOnly) {
            c_box_accept_rules.setVisibility(View.VISIBLE);
            c_box_accept_rules.setMovementMethod(LinkMovementMethod.getInstance());
        }
        Frame.setVisibility(View.VISIBLE);
        loadFragment(ProbyMenuFragment.newInstance());
    } //Включает отображение таблицы проб

    private void addFieldOrderType1() {
        Fields.add(new Field((byte) 1, R.array.target_research, 3, "Цель исследования/категория"));
        Fields.add(new Field((byte) 2, R.array.DocList, App.OrderInfo.getOD_ID(), "Оригиналы документов предоставлять")); //52. 63. 64
        Fields.add(new Field((byte) 3, 66, "", "Возврат образцов"));
        Fields.add(new Field((byte) 4, 0, act_of_selection, "Акт отбора"));
        Fields.add(new Field((byte) 5, 59, "", "Контрольный образец"));

        Fields.add(new Field((byte) 6, 11, "", "Акт отбора от"));

        Fields.add(new Field(10, "", "№", InputType.TYPE_CLASS_TEXT));
        Fields.add(new Field(49, "", "Площадка", InputType.TYPE_CLASS_TEXT));
    } //Заявка на исследования пищевых продуктов

    private void addFieldOrderType3() {
        Fields.add(new Field((byte) 1, R.array.target_research2, 3, "Цель исследования/категория"));
        Fields.add(new Field((byte) 3, 66, "", "Возврат образцов"));
        Fields.add(new Field((byte) 4, 0, act_of_selection, "Акт отбора"));
        Fields.add(new Field(24, "", "Сопроводительный документ", InputType.TYPE_CLASS_TEXT));
        Fields.add(new Field(9, "", "Владелец образцов", InputType.TYPE_CLASS_TEXT));
        Fields.add(new Field((byte) 5, 59, "", "Контрольный образец"));
        // Fields.add(new Field(11, "", "Акт отбора от", InputType.TYPE_CLASS_NUMBER, getDrawable(R.drawable.ic_date_range_black_24dp), true));
        Fields.add(new Field((byte) 6, 11, "", "Акт отбора от"));
        Fields.add(new Field(10, "", "№", InputType.TYPE_CLASS_TEXT));
    } //Заявка на исследование семян, почв, удобрений

    private void addFieldOrderType4() {
        Fields.add(new Field((byte) 1, R.array.target_research2, 3, "Цель исследования/категория"));
        Fields.add(new Field((byte) 2, R.array.DocList, App.OrderInfo.getOD_ID(), "Оригиналы документов предоставлять")); //52. 63. 64
        Fields.add(new Field((byte) 1, R.array.Reserch_start, 97, "Исследование проводится")); //act_of_selection
        Fields.add(new Field((byte) 4, 0, act_of_selection, "Акт отбора"));
        // Fields.add(new Field(11, "", "Акт отбора от", InputType.TYPE_CLASS_NUMBER, getDrawable(R.drawable.ic_date_range_black_24dp), true));
        Fields.add(new Field((byte) 6, 11, "", "Акт отбора от"));
        Fields.add(new Field(10, "", "№", InputType.TYPE_CLASS_TEXT));

        Fields.add(new Field(24, "", "Сопроводительный документ", InputType.TYPE_CLASS_TEXT));
        Fields.add(new Field(9, "", "Владелец образцов", InputType.TYPE_CLASS_TEXT));
        Fields.add(new Field(49, "", "Площадка", InputType.TYPE_CLASS_TEXT));
        Fields.add(new Field(7, "", "Количество проб", InputType.TYPE_NULL));
        order.getFields().put((short) 7, "1");
        Fields.add(new Field(93, "", "Общее поголовье", InputType.TYPE_CLASS_TEXT));
        //Fields.add(new Field(98, "", "Дата предыдущего исследоваия", InputType.TYPE_DATETIME_VARIATION_DATE, getDrawable(R.drawable.ic_date_range_black_24dp), true));
        Fields.add(new Field((byte) 6, 98, "", "Дата предыдущего исследоваия"));
        Fields.add(new Field(99, "", "Результат предыдущего исследования", InputType.TYPE_CLASS_TEXT));
        Fields.add(new Field((byte) 1, R.array.hoz_zab, 97, "Хозяйство по вышеуказанному заболеванию"));
        // Fields.add(new Field(106, "", "Дата заболевания животного(ных)", InputType.TYPE_DATETIME_VARIATION_DATE, getDrawable(R.drawable.ic_date_range_black_24dp), true));
        Fields.add(new Field((byte) 6, 106, "", "Дата заболевания животного(ных)"));
        //Fields.add(new Field(107, "", "Дата падежа", InputType.TYPE_DATETIME_VARIATION_DATE, getDrawable(R.drawable.ic_date_range_black_24dp), true));
        Fields.add(new Field((byte) 6, 107, "", "Дата падежа"));
        Fields.add(new Field(108, "", "Клиническая картина", InputType.TYPE_CLASS_TEXT));
        Fields.add(new Field(109, "", "Данные патологического вскрытия", InputType.TYPE_CLASS_TEXT));
        Fields.add(new Field(110, "", "Предположительный диагноз", InputType.TYPE_CLASS_TEXT));

    } //Сопроводительное письмо

    private void addFieldOrderType5() {
        Fields.add(new Field(123, "", "Сумма долга (руб)", InputType.TYPE_CLASS_NUMBER, getDrawable(R.drawable.rub)));
        Fields.add(new Field(117, "", "Номер заявки", InputType.TYPE_CLASS_NUMBER));
        Fields.add(new Field(134, "", "Счет на оплату №", InputType.TYPE_CLASS_TEXT));
        Fields.add(new Field((byte) 6, -1, "", "Дата создания заявки"));
        Fields.add(new Field((byte) 6, 124, "", "Срок уплаты"));
        Fields.add(new Field(119, "", true, "Текст", InputType.TYPE_TEXT_FLAG_MULTI_LINE));
    } //Гарантийное письмо

    private void addFieldOrderType6() {
        Fields.add(new Field(118, "", "Заголовок", InputType.TYPE_CLASS_TEXT));
        Fields.add(new Field(119, "", true, "Текст", InputType.TYPE_TEXT_FLAG_MULTI_LINE));
    } //Информационное письмо

    private void addFieldOrderType7() { //Запрос
        Fields.add(new Field(117, "", "Номер заявки", InputType.TYPE_CLASS_NUMBER));
        Fields.add(new Field(118, "", "Заголовок", InputType.TYPE_CLASS_TEXT));
        Fields.add(new Field(119, "", true, "Текст", InputType.TYPE_TEXT_FLAG_MULTI_LINE));
    } //Запрос

    private void LoadDefaultFields() {
        //Заполнение полей на форме
        Fields.add(new Field(135, false, "Заявитель", InputType.TYPE_NULL));
        Fields.add(new Field(136, false, "Договор №", InputType.TYPE_NULL));
        Fields.add(new Field((byte) 6, 137, "от", InputType.TYPE_NULL));
        Fields.add(new Field(138, false, "Адрес", InputType.TYPE_NULL));
        Fields.add(new Field(139, false, "ИНН", InputType.TYPE_NULL));
        Fields.add(new Field(140, false, "Телефон", InputType.TYPE_NULL));
        Fields.add(new Field(141, false, "E-mail", InputType.TYPE_NULL));
        Fields.add(new Field(142, false, "Представитель организации", InputType.TYPE_NULL));
        Fields.add(new Field(-1, new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()).format(new Date()), false, "Дата создания заявки", InputType.TYPE_NULL));

        //Заполнение отправляемого объекта
        order.getFields().put((short) 135, App.UserInfo.getClientName());
        order.getFields().put((short) 136, App.UserInfo.getContract_number());
        order.getFields().put((short) 137, App.UserInfo.getContract_date());
        order.getFields().put((short) 138, App.UserInfo.getAdress());
        order.getFields().put((short) 139, App.UserInfo.getInn());
        order.getFields().put((short) 140, App.UserInfo.getPhone());
        order.getFields().put((short) 141, App.UserInfo.getEmail());
        order.getFields().put((short) 142, App.UserInfo.getFIO() + ", " + App.UserInfo.getPosition() + ", действующий на основании " + getBasisString());

        //Инициализация полей Информация о партии и просихождение
        order.getFields().put((short) 120, "false");
        order.getFields().put((short) 121, "false");
    } //Базовые поля

    private void addFieldPatternType1() {
        Fields.add(new Field(133, "", "Наименование шаблона", InputType.TYPE_CLASS_TEXT));
        Fields.add(new Field((byte) 1, R.array.target_research2, 3, "Цель исследования/категория"));
        Fields.add(new Field((byte) 2, R.array.DocList, App.OrderInfo.getOD_ID(), "Оригиналы документов предоставлять")); //52. 63. 64
        Fields.add(new Field((byte) 3, 66, "", "Возврат образцов"));
        Fields.add(new Field((byte) 5, 59, "", "Контрольный образец"));
        Fields.add(new Field(49, "", "Площадка", InputType.TYPE_CLASS_TEXT));
    }

    private void addFieldPatternType3() {
        Fields.add(new Field(133, "", "Наименование шаблона", InputType.TYPE_CLASS_TEXT));
        Fields.add(new Field((byte) 1, R.array.target_research2, 3, "Цель исследования/категория"));
        Fields.add(new Field((byte) 3, 66, "", "Возврат образцов"));
        Fields.add(new Field(24, "", "Сопроводительный документ", InputType.TYPE_CLASS_TEXT));
        Fields.add(new Field(9, "", "Владелец образцов", InputType.TYPE_CLASS_TEXT));
        Fields.add(new Field((byte) 5, 59, "", "Контрольный образец"));
    }

    private void addFieldPatternType4() {
        Fields.add(new Field(133, "", "Наименование шаблона", InputType.TYPE_CLASS_TEXT));
        Fields.add(new Field((byte) 1, R.array.target_research2, 3, "Цель исследования/категория"));
        Fields.add(new Field((byte) 2, R.array.DocList, App.OrderInfo.getOD_ID(), "Оригиналы документов предоставлять")); //52. 63. 64
        Fields.add(new Field((byte) 1, R.array.Reserch_start, 97, "Исследование проводится")); //act_of_selection
        Fields.add(new Field(24, "", "Сопроводительный документ", InputType.TYPE_CLASS_TEXT));
        Fields.add(new Field(9, "", "Владелец образцов", InputType.TYPE_CLASS_TEXT));
        Fields.add(new Field(49, "", "Площадка", InputType.TYPE_CLASS_TEXT));
        Fields.add(new Field(93, "", "Общее поголовье", InputType.TYPE_CLASS_TEXT));
        Fields.add(new Field((byte) 1, R.array.hoz_zab, 97, "Хозяйство по вышеуказанному заболеванию"));
        Fields.add(new Field(108, "", "Клиническая картина", InputType.TYPE_CLASS_TEXT));
        Fields.add(new Field(109, "", "Данные патологического вскрытия", InputType.TYPE_CLASS_TEXT));
        Fields.add(new Field(110, "", "Предположительный диагноз", InputType.TYPE_CLASS_TEXT));
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.Menu_proby_fragment, fragment);
        ft.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            ClearDate();
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isFieldCorrect() {
        //Проверка на согласие с условиями
        if (c_box_accept_rules.getVisibility() == View.VISIBLE && !c_box_accept_rules.isChecked()) {
            //nestedScrollView.scrollTo(0, nestedScrollView.getBottom());
            Toast.makeText(getApplicationContext(), R.string.accept_error, Toast.LENGTH_SHORT).show();
            return false;
        }

        //Проверка на наличие у заявки проб. Если пробы были удалены (все), вернется 0, а не null.
        if (order.getProby() != null) {
            int ProbPosition = 0;
            for (TreeMap.Entry<Short, ProbyRest> prob : order.getProby().entrySet()) {
                //Проверка на наличие материалла
                if (!prob.getValue().getFields().containsKey("5") && !prob.getValue().getFields().containsKey("materialName")) {
                    if (ProbAdapter.adapter.getPositionList() != null)
                        if (ProbAdapter.adapter.getIdForField("materialName") != -1) {
                            NoChoiceMaterial = true;
                            ProbsFragment.adapter.ExpandProb(ProbPosition);
                            ProbAdapter.adapter.notifyItemChanged(ProbAdapter.adapter.getIdForField("materialName"));
                        }

                    nestedScrollView.post(() -> nestedScrollView.scrollTo(0, 2500));
                    Toast.makeText(getApplicationContext(), R.string.MaterialNoSelect, Toast.LENGTH_SHORT).show();
                    return false;
                }

                //Проверка на заполненность исследований
                if (prob.getValue().getSamples().size() == 0) {
                    ProbsFragment.adapter.ExpandProb(ProbPosition);
                    NoChoiceSamples = true;
                    nestedScrollView.post(() -> nestedScrollView.fullScroll(View.FOCUS_DOWN));
                    Toast.makeText(getApplicationContext(), R.string.no_samples_error, Toast.LENGTH_SHORT).show();
                    return false;
                } else {
                    int ResearchSize;
                    for (short i = 0; i < prob.getValue().getSamples().size(); i++) {
                        ResearchSize = Objects.requireNonNull(prob.getValue().getSamples().get(getPositionKeyS(i, prob.getValue().getSamples()))).getResearches().size();

                        if (ResearchSize == 0) {
                            Toast.makeText(getApplicationContext(), R.string.research_error, Toast.LENGTH_SHORT).show();
                            OpenSampleItemDialog(prob.getValue(), prob.getValue().getSamples().get(getPositionKeyS(i, prob.getValue().getSamples())), Byte.parseByte(String.valueOf(i + 1)));
                            return false;
                        } else
                            for (int j = 0; j < ResearchSize; j++) {
                                if (!Objects.requireNonNull(Objects.requireNonNull(prob.getValue().getSamples().get(getPositionKeyS(i, prob.getValue().getSamples()))).getResearches().get(getPositionKeyR(i, j, prob.getValue().getSamples()))).isComplete()) {
                                    Toast.makeText(getApplicationContext(), R.string.indicatorValError, Toast.LENGTH_SHORT).show();
                                    OpenSampleItemDialog(prob.getValue(), prob.getValue().getSamples().get(getPositionKeyS(i, prob.getValue().getSamples())), Byte.parseByte(String.valueOf(i + 1)));
                                    return false;
                                }
                            }
                    }
                }
                ProbPosition++;
            }
        }

        return true;
    }

    private void OpenSampleItemDialog(ProbyRest prob, SamplesRest Sample, byte pos) {
        new SampleItemDialog(ProbAdapter.adapter.getSamplesFields(), prob, Sample, pos, ProbAdapter.adapter.getIndicatorList())
                .show(getSupportFragmentManager(), "SampleItemDialog");
    }

    private Short getPositionKeyS(int position, TreeMap<Short, SamplesRest> samples) {
        if (samples.size() > 0)
            return new ArrayList<>(samples.keySet()).get(position);
        else return 0;
    }

    private Short getPositionKeyR(int i, int j, TreeMap<Short, SamplesRest> samples) {
        return new ArrayList<>(Objects.requireNonNull(samples.get(getPositionKeyS(i, samples))).getResearches().keySet()).get(j);
    }

    public void sendAction(View view) {
        ///  LoadActOfSelection(358);
//        if (isFieldCorrect()) {
//            view.setEnabled(false);
//            bar.setVisibility(View.VISIBLE);
//            if (order.getProby() != null)
//                DeleteEmptyFields();
//            if (!Edit)
//                SendOrder(view);
//            else
//                SaveOrder(view);
//        }

        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.menu_choice_move)
                .setItems(R.array.menu_create_order, (dialog, which) -> {
                    if (isFieldCorrect()) {
                        view.setEnabled(false);
                        bar.setVisibility(View.VISIBLE);
                        if (order.getProby() != null)
                            DeleteEmptyFields();
                        if (which == 1)
                            order.setSendOrder((byte) 1);
                        if (!Edit)
                            SendOrder(view);
                        else
                            SaveOrder(view);
                    }
                })
                .create()
                .show();
    }

    private void SaveOrder(final View view) {
        //String json = order.getJsonOrder();
        //   Hawk.put("obraz",json);
        //  Log.d("JSON", order.getJsonOrder());

        if (status != 11) {
            NetworkService.getInstance()
                    .getJSONApi()
                    .SaveOrder(App.UserAccessData.getToken(), order.getJsonOrder())
                    .enqueue(new Callback<AnswerSendOrder>() {
                        @Override
                        public void onResponse(@NonNull Call<AnswerSendOrder> call, @NonNull Response<AnswerSendOrder> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                if (response.body().getStatus() == 200) {
                                    // Toast.makeText(view.getContext(), R.string.order_isEdit, Toast.LENGTH_SHORT).show();
                                    if (FileActOfSelection != null)
                                        LoadActOfSelection(response.body().getOrder_id());
//                                    else {
//                                        ClearDate();
//                                        CreateOrderActivity.this.finish();
//                                    }
//                                    ClearDate();
//                                    CreateOrderActivity.this.finish();

                                    StringBuilder msg = new StringBuilder(getString(R.string.order_isEdit));
                                    if (response.body().isHaveNotAccredited())
                                        msg.append(getString(R.string.probIsSort));
                                    OpenDialogMessage(String.valueOf(msg));
                                }
                            } else {
                                view.setEnabled(true);
                                bar.setVisibility(View.GONE);
                                Toast.makeText(view.getContext(), R.string.error, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<AnswerSendOrder> call, @NonNull Throwable t) {
                            view.setEnabled(true);
                            bar.setVisibility(View.GONE);
                            Toast.makeText(view.getContext(), R.string.error, Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(view.getContext(), R.string.order_isEdit, Toast.LENGTH_SHORT).show();
            OrderFragment.offlineOrders.put((short) order.getId(), order);
            Hawk.put("OfflineOrders", OrderFragment.offlineOrders);
            ClearDate();
            CreateOrderActivity.this.finish();
        }
    }

    private String getBasisString() {
        switch (App.UserInfo.getBasis()) {
            case 1:
                return "устава";
            case 2:
                return "приказа";
            case 3:
                return "доверенности";
            case 4:
                return "свидетельства ИП";
            case 5:
                return "паспорта";
            default:
                return "Ошибка";
        }
    }

    private Short getPositionKeySamples(int position, int prob) {
        if (Objects.requireNonNull(order.getProby().get(getPositionKeyProb(prob))).getSamples().size() > 0)
            return new ArrayList<>(Objects.requireNonNull(order.getProby().get(getPositionKeyProb(prob))).getSamples().keySet()).get(position);
        else return 0;
    }

    private Short getPositionKeyProb(int position) {
        if (order.getProby().size() > 0)
            return new ArrayList<>(order.getProby().keySet()).get(position);
        else return 0;
    }

    private void DeleteEmptyFields() {
        final int ProbSize = order.getProby().size();
        int SamplesSize;
        for (int i = 0; i < ProbSize; i++) {
            SamplesSize = Objects.requireNonNull(order.getProby().get(getPositionKeyProb((short) i))).getSamples().size();
            for (int j = 0; j < SamplesSize; j++) {
                if (Objects.requireNonNull(Objects.requireNonNull(order.getProby().get(getPositionKeyProb((short) i))).getSamples().get(getPositionKeySamples(j, i))).getFields().size() <= 0) {
                    Objects.requireNonNull(Objects.requireNonNull(order.getProby().get(getPositionKeyProb((short) i))).getSamples().get(getPositionKeySamples(j, i))).DeleteSamplesFields();
                    break;
                }
            }
        }

        if (order.getProby().size() <= 0) {
            order.DeleteProb();
        }
    }

    private void ClearDate() {
        Fields.clear();
        order = null;
    }

    private void OpenDialogMessage(String msg) {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.attention)
                .setMessage(msg)
                .setPositiveButton(R.string.Continue,
                        (dialog, which) -> {
                            ClearDate();
                            CreateOrderActivity.this.finish();
                        })
                .setOnDismissListener(v -> {
                    ClearDate();
                    CreateOrderActivity.this.finish();
                })
                .show();
    }

    private void SendOrder(final View view) {
        //  Log.d("JSON", order.getJsonOrder());

        //   view.setEnabled(true);
        //  bar.setVisibility(View.GONE);

        if (App.isOnline(getApplicationContext())) {
            //  Map<Short, String> orders = new HashMap<>();
            //  orders.put((short)0, order.getJsonOrder());
            List<SendOrder> orders = new ArrayList<>();
            final Gson gson = new Gson();
            orders.add(order);

            //Log.d("JSON2", gson.toJson(orders));

            NetworkService.getInstance()
                    .getJSONApi()
                    .sendOrder(App.UserAccessData.getToken(), gson.toJson(orders))
                    .enqueue(new Callback<AnswerSendOrder>() {
                        @Override
                        public void onResponse(@NonNull Call<AnswerSendOrder> call, @NonNull Response<AnswerSendOrder> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                if (response.body().getStatus() == 200) {
                                    //  Toast.makeText(view.getContext(), "Заказ успешно создан!", Toast.LENGTH_SHORT).show();
                                    if (FileActOfSelection != null)
                                        LoadActOfSelection(response.body().getOrder_id());
//                                    else {
//                                        ClearDate();
//                                        CreateOrderActivity.this.finish();
//                                    }

                                    StringBuilder msg = new StringBuilder(getString(R.string.orderIsCreate));
                                    if (response.body().isHaveNotAccredited())
                                        msg.append(getString(R.string.probIsSort));
                                    OpenDialogMessage(String.valueOf(msg));
                                }
                            } else {
                                view.setEnabled(true);
                                bar.setVisibility(View.GONE);
                                // Toast.makeText(view.getContext(), "Ошибка 1", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<AnswerSendOrder> call, @NonNull Throwable t) {
                            view.setEnabled(true);
                            bar.setVisibility(View.GONE);
                        }
                    });
        } else {
            //OrderFragment.offlineOrders.add(order);
            // Hawk.put("offlineOrders",);
            final short newid = getPositionKey(OrderFragment.offlineOrders.size() - 1, OrderFragment.offlineOrders);
            order.setId(newid + 1);
            OrderFragment.offlineOrders.put((short) (newid + 1), order);
            // OrderFragment.Orders.add(0, new Orders((short) (newid + 1),0,order_id,11,""));

            List<Orders> insertList = new ArrayList<>();
            insertList.add(new Orders(newid + 1, 0, order_id, 11, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date())));
            OrderFragment.OrderAdapter.insertdata(insertList, true);

            Hawk.put("OfflineOrders", OrderFragment.offlineOrders);
            Hawk.put("OrdersList", OrderFragment.Orders);

            Toast.makeText(view.getContext(), "Заказ успешно создан (offline)", Toast.LENGTH_SHORT).show();
            ClearDate();
            CreateOrderActivity.this.finish();
        }
    }

    private void LoadActOfSelection(int order_id) {
        RequestBody requestBody = RequestBody.create(FileActOfSelection, MediaType.parse("*/*"));
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("act_of_selection", FileActOfSelection.getName(), requestBody);

        RequestBody Token = RequestBody.create(App.UserAccessData.getToken(), okhttp3.MultipartBody.FORM);
        RequestBody id = RequestBody.create(String.valueOf(order_id), okhttp3.MultipartBody.FORM);

        NetworkService.getInstance()
                .getJSONApi()
                .UploadAct(Token, id, fileToUpload)
                .enqueue(new Callback<StandardAnswer>() {
                    @Override
                    public void onResponse(@NonNull Call<StandardAnswer> call, @NonNull Response<StandardAnswer> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {
                                Toast.makeText(getApplicationContext(), "Файл успешно загружен!", Toast.LENGTH_SHORT).show();
                                ClearDate();
                                CreateOrderActivity.this.finish();
                            }
                        } else
                            bar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(@NonNull Call<StandardAnswer> call, @NonNull Throwable t) {
                        // Log.e("Upload error:", t.getMessage());
                        bar.setVisibility(View.GONE);
                    }
                });
    }

    private Short getPositionKey(int position, Map<Short, SendOrder> offlineOrders) {
        if (offlineOrders.size() > 0)
            return new ArrayList<>(offlineOrders.keySet()).get(position);
        else return 0;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == LOAD_ACT_OF_SELECTION) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    FileActOfSelection = FileUtils.getFile(this, data.getData());
                    pathForActOfSelection.setText(Objects.requireNonNull(FileActOfSelection).getName());
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @SuppressLint("StaticFieldLeak")
    private class MyTask extends AsyncTask<Void, Void, GridLayoutManager> {
        //  private RecyclerView recyclerView;
        private Context context;

        MyTask(Context con) {
            this.context = con;
        }

        @Override
        protected GridLayoutManager doInBackground(Void... params) {
            Fields.clear();
            switch (order_id) {
                case 1:
                    if (!IsPattern) {
                        LoadDefaultFields();
                        addFieldOrderType1();
                    } else
                        addFieldPatternType1();
                    EnableTable(Frame);
                    break;
                case 3:
                    if (!IsPattern) {
                        LoadDefaultFields();
                        addFieldOrderType3();
                    } else addFieldPatternType3();
                    EnableTable(Frame);
                    break;
                case 4:
                    if (!IsPattern) {
                        LoadDefaultFields();
                        addFieldOrderType4();
                    } else addFieldPatternType4();
                    EnableTable(Frame);
                    break;
                case 5:
                    LoadDefaultFields();
                    addFieldOrderType5();
                    break;
                case 6:
                    LoadDefaultFields();
                    addFieldOrderType6();
                    break;
                case 7:
                    LoadDefaultFields();
                    addFieldOrderType7();
                    break;
            }
            final GridLayoutManager mng_layout = new GridLayoutManager(context, 2);
            mng_layout.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if ((position == 1 || position == 2) && !IsPattern)
                        return 1;
                    if (order_id == 1 && (position == 14 || position == 15))
                        return 1;
                    if (order_id == 3 && (position == 15 || position == 16))
                        return 1;
                    if (order_id == 4 && (position == 13 || position == 14))
                        return 1;
                    return 2;
                }
            });
            FieldsAdapter.ClickFieldListener onClickFieldListener = path -> {
                pathForActOfSelection = path;
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);

                try {
                    startActivityForResult(Intent.createChooser(intent,
                            getString(R.string.ChoceFile)), LOAD_ACT_OF_SELECTION);
                } catch (android.content.ActivityNotFoundException ex) {

                    Toast.makeText(getApplicationContext(), R.string.NoFileMeneger,
                            Toast.LENGTH_SHORT).show();
                }
            };
            adapter = new FieldsAdapter(onClickFieldListener);
            return mng_layout;
        }

        @Override
        protected void onPostExecute(GridLayoutManager result) {
            final RecyclerView recyclerView = findViewById(R.id.FieldList);
            recyclerView.addItemDecoration(new SpacesItemDecoration((byte) 20, (byte) 15));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            // recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(result);
            recyclerView.setAdapter(adapter);
        }
    }
}