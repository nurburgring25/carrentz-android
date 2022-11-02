package dev.burikk.carrentz.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import dev.burikk.carrentz.activity.MerchantRentListActivity;
import dev.burikk.carrentz.fragment.MerchantRentListFragment;

public class MerchantRentListPagerAdapter extends FragmentStateAdapter {
    private final Fragment[] fragments;

    {
        this.fragments = new Fragment[4];
    }

    public MerchantRentListPagerAdapter(MerchantRentListActivity merchantRentListActivity) {
        super(merchantRentListActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (this.fragments[position] == null) {
            Bundle bundle = new Bundle();

            bundle.putInt("MODE", position);

            this.fragments[position] = new MerchantRentListFragment();
            this.fragments[position].setArguments(bundle);
        }

        return this.fragments[position];
    }

    @Override
    public int getItemCount() {
        return this.fragments.length;
    }

    public void repopulate() {
        for (Fragment fragment : this.fragments) {
            if (fragment != null) {
                ((MerchantRentListFragment) fragment).repopulate();
            }
        }
    }
}