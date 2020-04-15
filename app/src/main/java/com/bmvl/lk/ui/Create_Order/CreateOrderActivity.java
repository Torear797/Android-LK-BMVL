package com.bmvl.lk.ui.Create_Order;

import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bmvl.lk.App;
import com.bmvl.lk.R;
import com.bmvl.lk.ui.ProbyMenu.ProbyMenuFragment;
import com.bmvl.lk.ui.order.OrderFragment;
import com.google.android.material.checkbox.MaterialCheckBox;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class CreateOrderActivity extends AppCompatActivity {

    public static List<Field> Fields = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_order);

        final byte order_id = getIntent().getByteExtra("id", (byte) 0);

        final TextView OrderName = findViewById(R.id.Order_name);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        final RecyclerView recyclerView =  findViewById(R.id.FieldList);
        final MaterialCheckBox cbox = findViewById(R.id.AcceptcheckBox);
        final FrameLayout Frame = findViewById(R.id.Menu_proby_fragment);
        toolbar.setTitle(R.string.new_order);
        setSupportActionBar(toolbar);


       // recyclerView.setHasFixedSize(true);
        LoadDefaultFields();

        OrderName.setText(getResources().getStringArray(R.array.order_types)[order_id]);
        //Objects.requireNonNull(getSupportActionBar()).setIcon(R.drawable.logo);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        final GridLayoutManager mng_layout = new GridLayoutManager(this, 2);
        mng_layout.setSpanSizeLookup( new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if(position == 1 || position == 2) return 1;
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
            case 1:
                addFieldOrderType1();
                cbox.setVisibility(View.VISIBLE);
                Frame.setVisibility(View.VISIBLE);
                loadFragment(ProbyMenuFragment.newInstance(order_id));
                break;
            case 2:
                addFieldOrderType2();
                break;
            case 3:
                addFieldOrderType3();
                break;
            case 4:
                addFieldOrderType4();
                break;
        }
    }
    private void addFieldOrderType0() {
        Fields.add(new Field((byte)1,R.array.target_research,0,"","Цель исследования/категория"));
        Fields.add(new Field((byte)2,R.array.DocList,0,"","Оригиналы документов предоставлять"));
        Fields.add(new Field((byte)3,0,"","Возврат образцов"));
        Fields.add(new Field((byte)4,0,"","Акт отбора"));
        Fields.add(new Field((byte)3,0,"","Контрольный образец"));
        Fields.add(new Field(1,"","Акт отбора от", InputType.TYPE_CLASS_NUMBER, getDrawable(R.drawable.ic_date_range_black_24dp),true));
        Fields.add(new Field(1,"","№", InputType.TYPE_CLASS_TEXT));
        Fields.add(new Field(1,"","Площадка",InputType.TYPE_CLASS_TEXT));
    }

    private void addFieldOrderType1() {
        Fields.add(new Field((byte)1,R.array.target_research2,0,"","Цель исследования/категория"));
        Fields.add(new Field((byte)2,R.array.DocList,0,"","Оригиналы документов предоставлять"));
        Fields.add(new Field((byte)1,R.array.Reserch_start,0,"","Исследование проводится"));
        Fields.add(new Field((byte)4,0,"","Акт отбора"));
        Fields.add(new Field(1,"","Акт отбора от", InputType.TYPE_CLASS_NUMBER, getDrawable(R.drawable.ic_date_range_black_24dp),true));
        Fields.add(new Field(1,"","№", InputType.TYPE_CLASS_TEXT));

        Fields.add(new Field(1,"","Сопроводительный документ",InputType.TYPE_CLASS_TEXT));
        Fields.add(new Field(1,"","Владелец образцов",InputType.TYPE_CLASS_TEXT));
        Fields.add(new Field(1,"","Площадка",InputType.TYPE_CLASS_TEXT));
        Fields.add(new Field(1,"","Количество проб",InputType.TYPE_NULL));
        Fields.add(new Field(1,"","Общее поголовье",InputType.TYPE_CLASS_TEXT));
        Fields.add(new Field(1,"","Дата предыдущего исследоваия", InputType.TYPE_CLASS_NUMBER, getDrawable(R.drawable.ic_date_range_black_24dp),true));
        Fields.add(new Field(1,"","Результат предыдущего исследования",InputType.TYPE_CLASS_TEXT));
        Fields.add(new Field((byte)1,R.array.hoz_zab,0,"","Хозяйство по вышеуказанному заболеванию"));
        Fields.add(new Field(1,"","Дата заболевания животного(ных)", InputType.TYPE_CLASS_NUMBER, getDrawable(R.drawable.ic_date_range_black_24dp),true));
        Fields.add(new Field(1,"","Дата падежа", InputType.TYPE_CLASS_NUMBER, getDrawable(R.drawable.ic_date_range_black_24dp),true));
        Fields.add(new Field(1,"","Клиническая картина",InputType.TYPE_CLASS_TEXT));
        Fields.add(new Field(1,"","Данные патологического вскрытия",InputType.TYPE_CLASS_TEXT));
        Fields.add(new Field(1,"","Предположительный диагноз",InputType.TYPE_CLASS_TEXT));

    }

    private void addFieldOrderType2() {
        Fields.add(new Field(1,"","Сумма долга (руб)", InputType.TYPE_CLASS_NUMBER,getDrawable(R.drawable.rub) ));
        Fields.add(new Field(1,"","Номер заявки", InputType.TYPE_CLASS_NUMBER));
        Fields.add(new Field(1,"","Срок уплаты", InputType.TYPE_CLASS_NUMBER, getDrawable(R.drawable.ic_date_range_black_24dp),true));
        Fields.add(new Field(1,"", true,"Текст", InputType.TYPE_TEXT_FLAG_MULTI_LINE));
    }

    private void addFieldOrderType3() {
        Fields.add(new Field(1,"","Заголовок", InputType.TYPE_CLASS_TEXT));
        Fields.add(new Field(1,"",true,"Текст", InputType.TYPE_TEXT_FLAG_MULTI_LINE));
    }

    private void addFieldOrderType4() {
        Fields.add(new Field(1,"","Номер заявки", InputType.TYPE_CLASS_NUMBER));
        Fields.add(new Field(1,"","Заголовок", InputType.TYPE_CLASS_TEXT));
        Fields.add(new Field(1,"", true,"Текст", InputType.TYPE_TEXT_FLAG_MULTI_LINE));
    }

    private void LoadDefaultFields() {
        Fields.clear();
        Fields.add(new Field(1,"Общество с ограниченной ответственностью \"БИЗНЕС ФУД СФЕРА\"", false,"Заявитель", InputType.TYPE_NULL));
        Fields.add(new Field(1, App.UserInfo.getContract_number(), false,"Договор №", InputType.TYPE_NULL));
        Fields.add(new Field(1, App.UserInfo.getContract_date(), false,"от", InputType.TYPE_NULL));
        Fields.add(new Field(1, App.UserInfo.getAdress(), false,"Адрес", InputType.TYPE_NULL));
        Fields.add(new Field(1, App.UserInfo.getInn(), false,"ИНН", InputType.TYPE_NULL));
        Fields.add(new Field(1, App.UserInfo.getPhone(), false,"Телефон", InputType.TYPE_NULL));
        Fields.add(new Field(1, App.UserInfo.getEmail(), false,"E-mail", InputType.TYPE_NULL));
        Fields.add(new Field(1,App.UserInfo.getFIO() + ", Должность, действующий на основании устава", false,"Представитель организации", InputType.TYPE_NULL));

        Date currentDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());

        Fields.add(new Field(1, String.valueOf(dateFormat.format(currentDate)), false,"Дата протокола", InputType.TYPE_NULL));
    }

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
}