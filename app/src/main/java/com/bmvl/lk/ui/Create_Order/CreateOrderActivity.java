package com.bmvl.lk.ui.Create_Order;

import android.os.Bundle;
import android.text.InputType;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bmvl.lk.App;
import com.bmvl.lk.R;
import com.bmvl.lk.Rest.AnswerOrderEdit;
import com.bmvl.lk.Rest.NetworkService;
import com.bmvl.lk.Rest.Order.AnswerSendOrder;
import com.bmvl.lk.Rest.Order.ProbyRest;
import com.bmvl.lk.Rest.Order.SamplesRest;
import com.bmvl.lk.Rest.Order.SendOrder;
import com.bmvl.lk.Rest.StandardAnswer;
import com.bmvl.lk.data.SpacesItemDecoration;
import com.bmvl.lk.ui.ProbyMenu.ProbyMenuFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.orhanobut.hawk.Hawk;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateOrderActivity extends AppCompatActivity {

    public static List<Field> Fields = new ArrayList<>(); //Поля заявки
    public static byte order_id;
    private ProgressBar bar;
    private MaterialCheckBox cbox;

    public static SendOrder order;
    private boolean Edit; //Флаг, истина - заявка редактируется.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_order);

        final TextView OrderName = findViewById(R.id.Order_name);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        final RecyclerView recyclerView = findViewById(R.id.FieldList);
        cbox = findViewById(R.id.AcceptcheckBox);
        final FrameLayout Frame = findViewById(R.id.Menu_proby_fragment);
        bar = findViewById(R.id.ProgressBar);

        Edit = getIntent().getBooleanExtra("isEdit", false);
        if (!Edit) {
            order = new SendOrder(getIntent().getByteExtra("type_id",(byte)1));
            toolbar.setTitle(R.string.new_order);
        }
        else {
            order = (SendOrder) getIntent().getSerializableExtra(SendOrder.class.getSimpleName());
            toolbar.setTitle(R.string.edit_order);
            final MaterialButton btn = findViewById(R.id.Create);
            btn.setText("Сохранить");
        }
        order_id = order.getType_id();

        setSupportActionBar(toolbar);

        recyclerView.addItemDecoration(new SpacesItemDecoration((byte) 20, (byte) 15));
        LoadDefaultFields();

        OrderName.setText(getResources().getStringArray(R.array.order_name)[order_id - 1]);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        final GridLayoutManager mng_layout = new GridLayoutManager(this, 2);
        mng_layout.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 1 || position == 2) return 1;
                if (order_id == 1 && (position == 14 || position == 15))
                    return 1;
                if (order_id == 4 && (position == 13 || position == 14))
                    return 1;
                return 2;
            }
        });
        recyclerView.setLayoutManager(mng_layout);

        final FieldsAdapter adapter = new FieldsAdapter(this);
        recyclerView.setAdapter(adapter);

        switch (order_id) {
            case 1:
                addFieldOrderType1();
                cbox.setVisibility(View.VISIBLE);
                cbox.setMovementMethod(LinkMovementMethod.getInstance());
                Frame.setVisibility(View.VISIBLE);
                loadFragment(ProbyMenuFragment.newInstance());
                break;
            case 4:
                addFieldOrderType4();
                cbox.setVisibility(View.VISIBLE);
                cbox.setMovementMethod(LinkMovementMethod.getInstance());
                Frame.setVisibility(View.VISIBLE);
                loadFragment(ProbyMenuFragment.newInstance());
                break;
            case 5:
                addFieldOrderType5();
                break;
            case 6:
                addFieldOrderType6();
                break;
            case 7:
                addFieldOrderType7();
                break;
        }
    }
    private void StandartFieldPart1(){
        Fields.add(new Field((byte) 1, R.array.target_research, 3, "", "Цель исследования/категория"));
        Fields.add(new Field((byte) 2, R.array.DocList, App.OrderInfo.getOD_ID(), App.OrderInfo.getOD_Value(), "Оригиналы документов предоставлять")); //52. 63. 64
        Fields.add(new Field((byte) 3, 66, "", "Возврат образцов"));
        Fields.add(new Field((byte) 4, 0, "", "Акт отбора"));
        Fields.add(new Field((byte) 5, 59, "", "Контрольный образец"));
        Fields.add(new Field(11, "", "Акт отбора от", InputType.TYPE_CLASS_NUMBER, getDrawable(R.drawable.ic_date_range_black_24dp), true));
        Fields.add(new Field(10, "", "№", InputType.TYPE_CLASS_TEXT));
    }

    private void addFieldOrderType1() {
        StandartFieldPart1();
        Fields.add(new Field(49, "", "Площадка", InputType.TYPE_CLASS_TEXT));
    } //Заявка на исследования пищевых продуктов

    private void addFieldOrderType4() {
        Fields.add(new Field((byte) 1, R.array.target_research2, 3, "", "Цель исследования/категория"));
        Fields.add(new Field((byte) 2, R.array.DocList, App.OrderInfo.getOD_ID(), App.OrderInfo.getOD_Value(), "Оригиналы документов предоставлять")); //52. 63. 64
        Fields.add(new Field((byte) 1, R.array.Reserch_start, 97, "", "Исследование проводится")); //act_of_selection
        Fields.add(new Field((byte) 4, 0, "", "Акт отбора"));
        Fields.add(new Field(11, "", "Акт отбора от", InputType.TYPE_CLASS_NUMBER, getDrawable(R.drawable.ic_date_range_black_24dp), true));
        Fields.add(new Field(10, "", "№", InputType.TYPE_CLASS_TEXT));

        Fields.add(new Field(24, "", "Сопроводительный документ", InputType.TYPE_CLASS_TEXT));
        Fields.add(new Field(9, "", "Владелец образцов", InputType.TYPE_CLASS_TEXT));
        Fields.add(new Field(49, "", "Площадка", InputType.TYPE_CLASS_TEXT));
        Fields.add(new Field(7, "", "Количество проб", InputType.TYPE_NULL));
        order.getFields().put((short)7,"1");
        Fields.add(new Field(93, "", "Общее поголовье", InputType.TYPE_CLASS_TEXT));
        Fields.add(new Field(98, "", "Дата предыдущего исследоваия", InputType.TYPE_DATETIME_VARIATION_DATE, getDrawable(R.drawable.ic_date_range_black_24dp), true));
        Fields.add(new Field(99, "", "Результат предыдущего исследования", InputType.TYPE_CLASS_TEXT));
        Fields.add(new Field((byte) 1, R.array.hoz_zab, 97, "", "Хозяйство по вышеуказанному заболеванию"));
        Fields.add(new Field(106, "", "Дата заболевания животного(ных)", InputType.TYPE_DATETIME_VARIATION_DATE, getDrawable(R.drawable.ic_date_range_black_24dp), true));
        Fields.add(new Field(107, "", "Дата падежа", InputType.TYPE_DATETIME_VARIATION_DATE, getDrawable(R.drawable.ic_date_range_black_24dp), true));
        Fields.add(new Field(108, "", "Клиническая картина", InputType.TYPE_CLASS_TEXT));
        Fields.add(new Field(109, "", "Данные патологического вскрытия", InputType.TYPE_CLASS_TEXT));
        Fields.add(new Field(110, "", "Предположительный диагноз", InputType.TYPE_CLASS_TEXT));

    } //Сопроводительное письмо

    private void addFieldOrderType5() {
        Fields.add(new Field(123, "", "Сумма долга (руб)", InputType.TYPE_CLASS_NUMBER, getDrawable(R.drawable.rub)));
        Fields.add(new Field(117, "", "Номер заявки", InputType.TYPE_CLASS_NUMBER));
        Fields.add(new Field(134, "", "Счет на оплату №", InputType.TYPE_CLASS_TEXT));
        Fields.add(new Field(124, "", "Срок уплаты", InputType.TYPE_CLASS_NUMBER, getDrawable(R.drawable.ic_date_range_black_24dp), true));
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
        Fields.clear();
        Fields.add(new Field(135,false, "Заявитель", InputType.TYPE_NULL));
        Fields.add(new Field(136,false, "Договор №", InputType.TYPE_NULL));
        Fields.add(new Field(137,false, "от", InputType.TYPE_NULL));
        Fields.add(new Field(138,false, "Адрес", InputType.TYPE_NULL));
        Fields.add(new Field(139,false, "ИНН", InputType.TYPE_NULL));
        Fields.add(new Field(140,false, "Телефон", InputType.TYPE_NULL));
        Fields.add(new Field(141,false, "E-mail", InputType.TYPE_NULL));
        Fields.add(new Field(142,false, "Представитель организации", InputType.TYPE_NULL));
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

    } //Базовые поля

    private void loadFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.Menu_proby_fragment, fragment);
        ft.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean isFieldCorrect() {
        if (cbox.getVisibility() == View.VISIBLE && !cbox.isChecked()) {
            Toast.makeText(getApplicationContext(), "Для сохранения заявки вы должны согласиться с условиям.", Toast.LENGTH_SHORT).show();
            return false;
        }

        for (TreeMap.Entry<Short, ProbyRest> prob : order.getProby().entrySet()) {
            if (!prob.getValue().isResearchCorrect()) {
                Toast.makeText(getApplicationContext(), "У образца должно быть заполено хотя бы одно исследование.", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        return true;
    }

    public void sendAction(View view) {
        if (isFieldCorrect()) {
            view.setEnabled(false);
            bar.setVisibility(View.VISIBLE);
            DeleteEmptyFields();
            if (!Edit)
                SendOrder(view);
            else
                SaveOrder(view);
        }
    }

    private void SaveOrder(final View view) {
        //String json = order.getJsonOrder();
     //   Hawk.put("obraz",json);
        Log.d("JSON", order.getJsonOrder());

        NetworkService.getInstance()
                .getJSONApi()
                .SaveOrder(App.UserAccessData.getToken(), order.getJsonOrder())
                .enqueue(new Callback<StandardAnswer>() {
                    @Override
                    public void onResponse(@NonNull Call<StandardAnswer> call, @NonNull Response<StandardAnswer> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {
                                Toast.makeText(view.getContext(), "Заказ успешно изменен!", Toast.LENGTH_SHORT).show();
                                CreateOrderActivity.this.finish();
                            }
                        } else {
                            view.setEnabled(true);
                            bar.setVisibility(View.GONE);
                            Toast.makeText(view.getContext(), "Ошибка 1", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<StandardAnswer> call, @NonNull Throwable t) {
                        view.setEnabled(true);
                        bar.setVisibility(View.GONE);
                        Toast.makeText(view.getContext(), "Ошибка 2", Toast.LENGTH_SHORT).show();
                        Log.d("Проблема в ", String.valueOf(t));
                    }
                });
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
        if (order.getProby().get(getPositionKeyProbs(prob)).getSamples().size() > 0)
            return new ArrayList<Short>(order.getProby().get(getPositionKeyProbs(prob)).getSamples().keySet()).get(position);
        else return 0;
    }

    private Short getPositionKeyProbs(int position) {
        if (order.getProby().size() > 0)
            return new ArrayList<Short>(order.getProby().keySet()).get(position);
        else return 0;
    }

    private void DeleteEmptyFields() {
        final int ProbSize = order.getProby().size();
        int SamplesSize;
        for (int i = 0; i < ProbSize; i++) {
            SamplesSize = order.getProby().get(getPositionKeyProbs((short) i)).getSamples().size();
            for (int j = 0; j < SamplesSize; j++) {
                if (order.getProby().get(getPositionKeyProbs((short) i)).getSamples().get(getPositionKeySamples(j, i)).getFields().size() <= 0) {
                    order.getProby().get(getPositionKeyProbs((short) i)).getSamples().get(getPositionKeySamples(j, i)).DeleteSamplesFields();
                    break;
                }
            }
        }

        if (order.getProby().size() <= 0) {
            order.DeleteProb();
        }
    }

    private void SendOrder(final View view) {
       // String json = Hawk.get("obraz");
        Log.d("JSON", order.getJsonOrder());

     //   view.setEnabled(true);
      //  bar.setVisibility(View.GONE);

        NetworkService.getInstance()
                .getJSONApi()
                .sendOrder(App.UserAccessData.getToken(), order.getJsonOrder())
                .enqueue(new Callback<AnswerSendOrder>() {
                    @Override
                    public void onResponse(@NonNull Call<AnswerSendOrder> call, @NonNull Response<AnswerSendOrder> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getStatus() == 200) {
                                Toast.makeText(view.getContext(), "Заказ успешно создан!", Toast.LENGTH_SHORT).show();
                                CreateOrderActivity.this.finish();
                            }
                        } else {
                            view.setEnabled(true);
                            bar.setVisibility(View.GONE);
                            Toast.makeText(view.getContext(), "Ошибка 1", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<AnswerSendOrder> call, @NonNull Throwable t) {
                        view.setEnabled(true);
                        bar.setVisibility(View.GONE);
                        Toast.makeText(view.getContext(), "Ошибка 2", Toast.LENGTH_SHORT).show();
                        Log.d("Проблема в ", String.valueOf(t));
                    }
                });
    }

}