package com.bmvl.lk.ui.settings.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import com.bmvl.lk.R;
import com.bmvl.lk.data.OnBackPressedListener;
import com.bmvl.lk.data.SpacesItemDecoration;
import com.bmvl.lk.ui.settings.SettingsAdapter;
import com.bmvl.lk.data.models.SettingsGroup;

import java.util.ArrayList;
import java.util.List;

public class GroupListFragment extends Fragment implements OnBackPressedListener {
    private RecyclerView SettingsList;
    private List<SettingsGroup> SettingsFields = new ArrayList<>();

    public static Fragment newInstance() {
        return new GroupListFragment();
    }

    public GroupListFragment() {
    }

    @Override
    public void onBackPressed() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View MyView = inflater.inflate(R.layout.only_recyclerview, container, false);
        SettingsList = MyView.findViewById(R.id.RecyclerView);

        InitList();
        initRecyclerView();
        return MyView;
    }

    private void InitList(){
        SettingsFields.add(new SettingsGroup((byte) 0,R.drawable.ic_baseline_notifications_24, R.color.orange, R.string.Notice,R.string.desc_notify));
        SettingsFields.add(new SettingsGroup((byte) 0,R.drawable.ic_baseline_work_24, Color.BLUE, R.string.orig_doc,R.string.orig_doc_desc));
    }

    private void initRecyclerView() {
        SettingsList.addItemDecoration(new SpacesItemDecoration((byte) 30, (byte) 10));
        SettingsList.setItemAnimator(new DefaultItemAnimator());
        SettingsList.setHasFixedSize(true);

        SettingsAdapter.OnClickListener onClickListener = group -> {
        };

        SettingsAdapter adapter = new SettingsAdapter(SettingsFields, onClickListener);
        SettingsList.setAdapter(adapter);
    }
}
