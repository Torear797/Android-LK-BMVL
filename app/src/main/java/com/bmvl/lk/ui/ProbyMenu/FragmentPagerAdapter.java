package com.bmvl.lk.ui.ProbyMenu;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.bmvl.lk.ui.ProbyMenu.PartyInfo.PartyInfoFragment;
import com.bmvl.lk.ui.ProbyMenu.Probs.ProbsFragment;

public class FragmentPagerAdapter extends FragmentStateAdapter {
    private static final int CARD_ITEM_SIZE = 4;
    private byte order_id;

    FragmentPagerAdapter(@NonNull FragmentActivity fragmentActivity, byte id) {
        super(fragmentActivity);
        order_id = id;
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
            case 2:
                return PartyInfoFragment.newInstance((byte) position);
            case 3:
                return ServiceInfoFragment.newInstance();
            default:
                return ProbsFragment.newInstance(order_id);
        }
    }

    @Override
    public int getItemCount() {
        return CARD_ITEM_SIZE;
    }

}