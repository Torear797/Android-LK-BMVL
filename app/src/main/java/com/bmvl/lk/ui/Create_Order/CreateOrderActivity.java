package com.bmvl.lk.ui.Create_Order;

import android.os.Bundle;
import android.text.InputType;
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
import com.bmvl.lk.Rest.AnswerOrderNew;
import com.bmvl.lk.Rest.NetworkService;
import com.bmvl.lk.Rest.Order.AnswerSendOrder;
import com.bmvl.lk.Rest.Order.SendOrder;
import com.bmvl.lk.data.SpacesItemDecoration;
import com.bmvl.lk.ui.ProbyMenu.ProbyMenuFragment;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateOrderActivity extends AppCompatActivity {

    public static List<Field> Fields = new ArrayList<>();
    private static byte order_id;
    private ProgressBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_order);

        order_id = getIntent().getByteExtra("id", (byte) 0);

        final TextView OrderName = findViewById(R.id.Order_name);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        final RecyclerView recyclerView = findViewById(R.id.FieldList);
        final MaterialCheckBox cbox = findViewById(R.id.AcceptcheckBox);
        final FrameLayout Frame = findViewById(R.id.Menu_proby_fragment);
        bar = findViewById(R.id.ProgressBar);
        //  final MaterialButton sendButton = findViewById(R.id.Create);
        toolbar.setTitle(R.string.new_order);
        setSupportActionBar(toolbar);


        recyclerView.addItemDecoration(new SpacesItemDecoration((byte) 20, (byte) 15));
        // recyclerView.setHasFixedSize(true);
      //  getOriginalDocInfo();
        LoadDefaultFields();

        OrderName.setText(getResources().getStringArray(R.array.order_name)[order_id]);
        //Objects.requireNonNull(getSupportActionBar()).setIcon(R.drawable.logo);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        final GridLayoutManager mng_layout = new GridLayoutManager(this, 2);
        mng_layout.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 1 || position == 2) return 1;
                if (order_id == 0 && (position == 14 || position == 15))
                    return 1;
                if (order_id == 1 && (position == 13 || position == 14))
                    return 1;
                return 2;
            }
        });
        recyclerView.setLayoutManager(mng_layout);

        final FieldsAdapter adapter = new FieldsAdapter(this);
        recyclerView.setAdapter(adapter);


        switch (order_id) {
            case 0:
                addFieldOrderType0();
                cbox.setVisibility(View.VISIBLE);
                Frame.setVisibility(View.VISIBLE);
                loadFragment(ProbyMenuFragment.newInstance(order_id));
                break;
            case 3:
                addFieldOrderType1();
                cbox.setVisibility(View.VISIBLE);
                Frame.setVisibility(View.VISIBLE);
                loadFragment(ProbyMenuFragment.newInstance(order_id));
                break;
            case 4:
                addFieldOrderType2();
                break;
            case 5:
                addFieldOrderType3();
                break;
            case 6:
                addFieldOrderType4();
                break;
        }
    }

    private void addFieldOrderType0() {
        Fields.add(new Field((byte) 1, R.array.target_research, 3, "", "Цель исследования/категория"));
        Fields.add(new Field((byte) 2, R.array.DocList, App.OrderInfo.getOD_ID(), App.OrderInfo.getOD_Value(), "Оригиналы документов предоставлять")); //52. 63. 64
        Fields.add(new Field((byte) 3, 66, "", "Возврат образцов"));
        Fields.add(new Field((byte) 4, 0, "", "Акт отбора"));
        Fields.add(new Field((byte) 3, 59, "", "Контрольный образец"));
        Fields.add(new Field(11, "", "Акт отбора от", InputType.TYPE_CLASS_NUMBER, getDrawable(R.drawable.ic_date_range_black_24dp), true));
        Fields.add(new Field(10, "", "№", InputType.TYPE_CLASS_TEXT));
        Fields.add(new Field(49, "", "Площадка", InputType.TYPE_CLASS_TEXT));
    } //Заявка на исследования пищевых продуктов

    private void addFieldOrderType1() {
        Fields.add(new Field((byte) 1, R.array.target_research2, 0, "", "Цель исследования/категория"));
        Fields.add(new Field((byte) 2, R.array.DocList, 0, "", "Оригиналы документов предоставлять"));
        Fields.add(new Field((byte) 1, R.array.Reserch_start, 0, "", "Исследование проводится"));
        Fields.add(new Field((byte) 4, 0, "", "Акт отбора"));
        Fields.add(new Field(1, "", "Акт отбора от", InputType.TYPE_CLASS_NUMBER, getDrawable(R.drawable.ic_date_range_black_24dp), true));
        Fields.add(new Field(1, "", "№", InputType.TYPE_CLASS_TEXT));

        Fields.add(new Field(1, "", "Сопроводительный документ", InputType.TYPE_CLASS_TEXT));
        Fields.add(new Field(1, "", "Владелец образцов", InputType.TYPE_CLASS_TEXT));
        Fields.add(new Field(1, "", "Площадка", InputType.TYPE_CLASS_TEXT));
        Fields.add(new Field(1, "", "Количество проб", InputType.TYPE_NULL));
        Fields.add(new Field(1, "", "Общее поголовье", InputType.TYPE_CLASS_TEXT));
        Fields.add(new Field(1, "", "Дата предыдущего исследоваия", InputType.TYPE_CLASS_NUMBER, getDrawable(R.drawable.ic_date_range_black_24dp), true));
        Fields.add(new Field(1, "", "Результат предыдущего исследования", InputType.TYPE_CLASS_TEXT));
        Fields.add(new Field((byte) 1, R.array.hoz_zab, 0, "", "Хозяйство по вышеуказанному заболеванию"));
        Fields.add(new Field(1, "", "Дата заболевания животного(ных)", InputType.TYPE_CLASS_NUMBER, getDrawable(R.drawable.ic_date_range_black_24dp), true));
        Fields.add(new Field(1, "", "Дата падежа", InputType.TYPE_CLASS_NUMBER, getDrawable(R.drawable.ic_date_range_black_24dp), true));
        Fields.add(new Field(1, "", "Клиническая картина", InputType.TYPE_CLASS_TEXT));
        Fields.add(new Field(1, "", "Данные патологического вскрытия", InputType.TYPE_CLASS_TEXT));
        Fields.add(new Field(1, "", "Предположительный диагноз", InputType.TYPE_CLASS_TEXT));

    } //Сопроводительное письмо

    private void addFieldOrderType2() {
        Fields.add(new Field(123, "", "Сумма долга (руб)", InputType.TYPE_CLASS_NUMBER, getDrawable(R.drawable.rub)));
        Fields.add(new Field(117, "", "Номер заявки", InputType.TYPE_CLASS_NUMBER));
        Fields.add(new Field(134, "", "Счет на оплату №", InputType.TYPE_CLASS_TEXT));
        Fields.add(new Field(124, "", "Срок уплаты", InputType.TYPE_CLASS_NUMBER, getDrawable(R.drawable.ic_date_range_black_24dp), true));
        Fields.add(new Field(119, "", true, "Текст", InputType.TYPE_TEXT_FLAG_MULTI_LINE));
    } //Гарантийное письмо

    private void addFieldOrderType3() {
        Fields.add(new Field(118, "", "Заголовок", InputType.TYPE_CLASS_TEXT));
        Fields.add(new Field(119, "", true, "Текст", InputType.TYPE_TEXT_FLAG_MULTI_LINE));
    } //Информационное письмо

    private void addFieldOrderType4() { //Запрос
        Fields.add(new Field(117, "", "Номер заявки", InputType.TYPE_CLASS_NUMBER));
        Fields.add(new Field(118, "", "Заголовок", InputType.TYPE_CLASS_TEXT));
        Fields.add(new Field(119, "", true, "Текст", InputType.TYPE_TEXT_FLAG_MULTI_LINE));
    } //Запрос

    private void LoadDefaultFields() {
        Fields.clear();
        Fields.add(new Field(135, App.UserInfo.getClientName(), false, "Заявитель", InputType.TYPE_NULL));
        Fields.add(new Field(136, App.UserInfo.getContract_number(), false, "Договор №", InputType.TYPE_NULL));
        Fields.add(new Field(137, App.UserInfo.getContract_date(), false, "от", InputType.TYPE_NULL));
        Fields.add(new Field(138, App.UserInfo.getAdress(), false, "Адрес", InputType.TYPE_NULL));
        Fields.add(new Field(139, App.UserInfo.getInn(), false, "ИНН", InputType.TYPE_NULL));
        Fields.add(new Field(140, App.UserInfo.getPhone(), false, "Телефон", InputType.TYPE_NULL));
        Fields.add(new Field(141, App.UserInfo.getEmail(), false, "E-mail", InputType.TYPE_NULL));
        Fields.add(new Field(142, App.UserInfo.getFIO() + ", " + App.UserInfo.getPosition() + ", действующий на основании " + getBasisString(), false, "Представитель организации", InputType.TYPE_NULL));

        Date currentDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());

        Fields.add(new Field(-1, dateFormat.format(currentDate), false, "Дата создания заявки", InputType.TYPE_NULL));
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

    public void sendAction(View view) {
        view.setEnabled(false);
        bar.setVisibility(View.VISIBLE);
        SendOrder(view);
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

    private void SendOrder(final View view) {
        Map<Short, String> fields = new HashMap<>();   //Поля заявки

        for (Field f : Fields) {
            if (f.getColumn_id() != -1)
                fields.put((short)(f.getColumn_id()), f.getValue());
        }
        Gson gson = new Gson();
        String order = gson.toJson(new SendOrder((byte) (order_id + 1), fields));

        NetworkService.getInstance()
                .getJSONApi()
                .sendOrder(App.UserAccessData.getToken(), order)
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
                    }
                });
    }

}