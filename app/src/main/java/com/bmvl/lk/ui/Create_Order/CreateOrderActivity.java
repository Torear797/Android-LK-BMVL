package com.bmvl.lk.ui.Create_Order;

import android.content.pm.ActivityInfo;
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

import static java.security.AccessController.getContext;

public class CreateOrderActivity extends AppCompatActivity {

    public static List<Field> Fields = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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


//        switch (order_id) {
//            case 0:
//                break;
//            case 1:
//                break;
//            case 2:
//                break;
//            case 3:
//                break;
//            case 4:
//                break;
//        }
    }

    private void LoadDefaultFields() {
        Fields.clear();
        Fields.add(new Field(1,1,"Общество с ограниченной ответственностью \"БИЗНЕС ФУД СФЕРА\"", true,"Заявитель", InputType.TYPE_NULL));
        Fields.add(new Field(2,1,"12-19/Ш0015 от 11.01.2019", true,"Договор №", InputType.TYPE_NULL));
        Fields.add(new Field(3,1,"308006, Белгородская область, Белгород город, Производственная улица, дом № Дом 4, Этаж/Офис 1/4", true,"Адрес", InputType.TYPE_NULL));
        Fields.add(new Field(4,1,"3123427599", true,"ИНН", InputType.TYPE_NULL));
        Fields.add(new Field(5,1,"", true,"Телефон", InputType.TYPE_NULL));
        Fields.add(new Field(6,1,"user@mail.ru", true,"E-mail", InputType.TYPE_NULL));
        Fields.add(new Field(7,1,"Иванов Иван Иванович, Должность, действующий на основании устава", true,"Представитель организации", InputType.TYPE_NULL));
        Fields.add(new Field(8,1,String.valueOf(new Date()), true,"Дата протокола", InputType.TYPE_NULL));
        Fields.add(new Field(9,1,"", true,"Тест", InputType.TYPE_CLASS_TEXT));

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
