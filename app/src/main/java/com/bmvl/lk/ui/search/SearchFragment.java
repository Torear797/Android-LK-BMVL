package com.bmvl.lk.ui.search;


import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bmvl.lk.R;
import com.bmvl.lk.data.OnBackPressedListener;
import com.bmvl.lk.data.SpacesItemDecoration;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment implements OnBackPressedListener {

    public static List<SearchField> Fields = new ArrayList<>();
    private GridLayoutManager mng_layout = new GridLayoutManager(getContext(), 2);
    private static SearchFieldsAdapter adapter;
 //   private AsyncTask mAsyncTask;

    public SearchFragment() {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        mAsyncTask.cancel(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mng_layout.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 4 || position == 5) {
                    return 2;
                } else {
                    return 1;
                }
            }
        });
        adapter = new SearchFieldsAdapter(getContext());
//        mAsyncTask = new AsyncTask<Void, Void, Void>() {
//            @Override
//            protected Void doInBackground(Void... voids) {
//
//                return null;
//            }
//        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View MyView = inflater.inflate(R.layout.fragment_search, container, false);

        final RecyclerView recyclerView = MyView.findViewById(R.id.recyclerView);
        final Button SearchButton = MyView.findViewById(R.id.search_button);

        recyclerView.addItemDecoration(new SpacesItemDecoration((byte) 20, (byte) 15));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(mng_layout);
        recyclerView.setAdapter(adapter);
//
//        mAsyncTask = new AsyncTask<Void, Void, Void>() {
//            @Override
//            protected Void doInBackground(Void... voids) {
//                recyclerView.setAdapter(adapter);
//                return null;
//            }
//        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


        SearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Выполняется поиск ...", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
        return MyView;
    }

    @Override
    public void onBackPressed() {
    }

    public void CreateSerchFields() {
        Fields.clear();
        Fields.add(new SearchField("", "Номер заявки", InputType.TYPE_CLASS_NUMBER));
        Fields.add(new SearchField("", "Номер протокола", InputType.TYPE_CLASS_NUMBER));

        Fields.add(new SearchField("", "От", InputType.TYPE_CLASS_NUMBER, getContext().getDrawable(R.drawable.ic_date_range_black_24dp), true));
        Fields.add(new SearchField("", "До", InputType.TYPE_CLASS_NUMBER, getContext().getDrawable(R.drawable.ic_date_range_black_24dp), true));

        Fields.add(new SearchField("", "Контактное лицо", InputType.TYPE_CLASS_TEXT, true));
        Fields.add(new SearchField("", "Вид заказа", true, R.array.order_name, true));

        Fields.add(new SearchField("", "От", InputType.TYPE_CLASS_NUMBER, getContext().getDrawable(R.drawable.rub), false));
        Fields.add(new SearchField("", "До", InputType.TYPE_CLASS_NUMBER, getContext().getDrawable(R.drawable.rub), false));

        Fields.add(new SearchField("", "Статус", true, R.array.order_statuses));
        Fields.add(new SearchField("", "Порядок сортировки", true, R.array.sort_types));
    }
}