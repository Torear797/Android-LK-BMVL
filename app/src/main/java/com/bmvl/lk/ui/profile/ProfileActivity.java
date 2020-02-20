package com.bmvl.lk.ui.profile;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bmvl.lk.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_material);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        final FloatingActionButton fab = findViewById(R.id.fab);

        toolbar.setTitle(R.string.profile);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setIcon(R.drawable.logo);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


        //TextView FIO = findViewById(R.id.FIO);
        String Name = "Имя", Surname = "Фамилия", Patronic = "Отчество";
        // FIO.setText(String.format("Личный кабинет пользователя %s %s %s", Name ,Surname, Patronic));


        fab.setColorFilter(Color.argb(255, 255, 255, 255));





        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Выход", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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
