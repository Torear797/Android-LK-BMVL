package com.bmvl.lk.ui.patterns;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class PatternsPagerAdapter extends FragmentStateAdapter {
    public PatternsPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position == 0) return PatternsFragment.newInstance();
        else return StandartPatternsFragment.newInstance();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
