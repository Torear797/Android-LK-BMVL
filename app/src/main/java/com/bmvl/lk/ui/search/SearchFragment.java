package com.bmvl.lk.ui.search;


import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bmvl.lk.OnBackPressedListener;
import com.bmvl.lk.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SearchFragment extends Fragment implements OnBackPressedListener {

    public static List<SearchField> Fields = new ArrayList<>();

    public SearchFragment() {
    }

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View MyView = inflater.inflate(R.layout.fragment_search2, container, false);

        final RecyclerView recyclerView = MyView.findViewById(R.id.recyclerView);
        final Button SearchButton = MyView.findViewById(R.id.search_button);

        CreateSerchFields();

        recyclerView.setHasFixedSize(true);


        final GridLayoutManager mng_layout = new GridLayoutManager(getContext(), 2);
        mng_layout.setSpanSizeLookup( new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 4 || position == 5) {
                    return 2;
                } else {
                    return 1;
                }
            }
        });
        recyclerView.setLayoutManager(mng_layout);

        SearchFieldsAdapter adapter = new SearchFieldsAdapter(getContext());
        recyclerView.setAdapter(adapter);

        SearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Выполняется поиск ...", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
        return MyView;
    }

    private void CreateSerchFields() {
        Fields.clear();
        Fields.add(new SearchField("", "Номер заявки", InputType.TYPE_CLASS_NUMBER));
        Fields.add(new SearchField("", "Номер протокола", InputType.TYPE_CLASS_NUMBER));

        Fields.add(new SearchField("", "От", InputType.TYPE_CLASS_NUMBER, Objects.requireNonNull(getContext()).getDrawable(R.drawable.ic_date_range_black_24dp), true));
        Fields.add(new SearchField("", "До", InputType.TYPE_CLASS_NUMBER, Objects.requireNonNull(getContext()).getDrawable(R.drawable.ic_date_range_black_24dp), true));

        Fields.add(new SearchField("", "Контактное лицо", InputType.TYPE_CLASS_TEXT, true));
        Fields.add(new SearchField("", "Вид заказа", true, R.array.order_name,true));

        Fields.add(new SearchField("", "От", InputType.TYPE_CLASS_NUMBER, Objects.requireNonNull(getContext()).getDrawable(R.drawable.rub), false));
        Fields.add(new SearchField("", "До", InputType.TYPE_CLASS_NUMBER, Objects.requireNonNull(getContext()).getDrawable(R.drawable.rub), false));

        Fields.add(new SearchField("", "Статус", true, R.array.order_statuses));
        Fields.add(new SearchField("", "Порядок сортировки", true, R.array.sort_types));

    }

    @Override
    public void onBackPressed() {

    }
}