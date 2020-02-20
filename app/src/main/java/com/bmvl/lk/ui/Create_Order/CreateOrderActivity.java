package com.bmvl.lk.ui.Create_Order;

import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.bmvl.lk.R;
import com.bmvl.lk.ui.order.OrderFragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

        toolbar.setTitle(R.string.new_order);
        setSupportActionBar(toolbar);


        LoadDefaultFields();

        OrderName.setText(OrderFragment.OrderTypes[order_id]);
        //Objects.requireNonNull(getSupportActionBar()).setIcon(R.drawable.logo);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


        FieldsAdapter adapter = new FieldsAdapter(this);
        recyclerView.setAdapter(adapter);


        switch (order_id) {
            case 0:
                addFieldOrderType0();
                break;
            case 1:
                addFieldOrderType1();
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
    }

    private void addFieldOrderType1() {
    }

    private void addFieldOrderType2() {
        Fields.add(new Field(9,1,"", false,"Сумма долга (руб)", InputType.TYPE_CLASS_NUMBER));
        Fields.add(new Field(10,1,"", false,"Номер заявки", InputType.TYPE_CLASS_NUMBER));
        Fields.add(new Field(11,1,"", false,"Срок уплаты", InputType.TYPE_DATETIME_VARIATION_DATE));
        Fields.add(new Field(12,1,"", true,"Текст", InputType.TYPE_TEXT_FLAG_MULTI_LINE));
    }

    private void addFieldOrderType3() {
        Fields.add(new Field(9,1,"", false,"Заголовок", InputType.TYPE_CLASS_TEXT));
        Fields.add(new Field(10,1,"", true,"Текст", InputType.TYPE_TEXT_FLAG_MULTI_LINE));
    }

    private void addFieldOrderType4() {
        Fields.add(new Field(9,1,"", false,"Номер заявки", InputType.TYPE_CLASS_NUMBER));
        Fields.add(new Field(10,1,"", false,"Заголовок", InputType.TYPE_CLASS_TEXT));
        Fields.add(new Field(11,1,"", true,"Текст", InputType.TYPE_TEXT_FLAG_MULTI_LINE));
    }

    private void LoadDefaultFields() {
        Fields.clear();
        Fields.add(new Field(1,1,"Общество с ограниченной ответственностью \"БИЗНЕС ФУД СФЕРА\"", false,"Заявитель", InputType.TYPE_NULL));
        Fields.add(new Field(2,1,"12-19/Ш0015 от 11.01.2019", false,"Договор №", InputType.TYPE_NULL));
        Fields.add(new Field(3,1,"308006, Белгородская область, Белгород город, Производственная улица, дом № Дом 4, Этаж/Офис 1/4", false,"Адрес", InputType.TYPE_NULL));
        Fields.add(new Field(4,1,"3123427599", false,"ИНН", InputType.TYPE_NULL));
        Fields.add(new Field(5,1,"", false,"Телефон", InputType.TYPE_NULL));
        Fields.add(new Field(6,1,"user@mail.ru", false,"E-mail", InputType.TYPE_NULL));
        Fields.add(new Field(7,1,"Иванов Иван Иванович, Должность, действующий на основании устава", false,"Представитель организации", InputType.TYPE_NULL));
        Fields.add(new Field(8,1,String.valueOf(new Date()), false,"Дата протокола", InputType.TYPE_NULL));
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
