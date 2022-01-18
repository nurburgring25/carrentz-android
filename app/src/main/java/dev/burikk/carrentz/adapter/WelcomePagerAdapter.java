package dev.burikk.carrentz.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import dev.burikk.carrentz.activity.WelcomeActivity;
import dev.burikk.carrentz.fragment.MerchantWelcomeFragment;
import dev.burikk.carrentz.fragment.UserWelcomeFragment;

public class WelcomePagerAdapter extends FragmentStateAdapter {
    private final Fragment[] fragments;

    {
        this.fragments = new Fragment[2];
    }

    public WelcomePagerAdapter(WelcomeActivity welcomeActivity) {
        super(welcomeActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (this.fragments[position] == null) {
            if (position == 0) {
                this.fragments[position] = new MerchantWelcomeFragment();
            } else if (position == 1) {
                this.fragments[position] = new UserWelcomeFragment();
            }
        }

        return this.fragments[position];
    }

    @Override
    public int getItemCount() {
        return this.fragments.length;
    }
}