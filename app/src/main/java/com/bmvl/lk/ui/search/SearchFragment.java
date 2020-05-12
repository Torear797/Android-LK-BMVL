package com.bmvl.lk.ui.search;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
    private GridLayoutManager mng_layout;
    private static SearchFieldsAdapter adapter;

    public SearchFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mng_layout = new GridLayoutManager(getContext(), 2);
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
        recyclerView.setLayoutManager(mng_layout);
        recyclerView.setAdapter(adapter);

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
}