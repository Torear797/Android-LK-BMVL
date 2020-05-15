package com.bmvl.lk.ui.ProbyMenu;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.bmvl.lk.ui.ProbyMenu.PartyInfo.PartyInfoFragment;
import com.bmvl.lk.ui.ProbyMenu.Probs.ProbsFragment;

public class FragmentPagerAdapter extends FragmentStateAdapter {
    private static final int CARD_ITEM_SIZE = 3;

    FragmentPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
            case 2:
                return PartyInfoFragment.newInstance((byte) position);
            default:
                return ProbsFragment.newInstance();
        }
    }

    @Override
    public int getItemCount() {
        return CARD_ITEM_SIZE;
    }

}