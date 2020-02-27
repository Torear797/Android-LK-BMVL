package com.bmvl.lk.ui.ProbyMenu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.bmvl.lk.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Objects;

public class ProbyMenuFragment extends Fragment {

    public ProbyMenuFragment() {
        // Required empty public constructor
    }

    public static ProbyMenuFragment newInstance() {
        return new ProbyMenuFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View MyView = inflater.inflate(R.layout.fragment_proby_menu, container, false);

        final ViewPager2 viewPager = MyView.findViewById(R.id.viewPager);
        final TabLayout tabLayout = MyView.findViewById(R.id.tabLayout);

        final String[] tabTitles = getResources().getStringArray(R.array.Menu_proby);

        viewPager.setAdapter(createAdapter());
        new TabLayoutMediator(tabLayout, viewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        tab.setText(tabTitles[position]);
                       // tab.setText("rere");
                    }
                }).attach();

        return MyView;
    }

    private FragmentPagerAdapter createAdapter() {
        return new FragmentPagerAdapter(Objects.requireNonNull(getActivity()));
    }
}