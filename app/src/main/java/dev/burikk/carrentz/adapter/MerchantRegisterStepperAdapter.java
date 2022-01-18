package dev.burikk.carrentz.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;

import dev.burikk.carrentz.fragment.MerchantRegisterAccountFragment;
import dev.burikk.carrentz.fragment.MerchantRegisterBusinessFragment;
import dev.burikk.carrentz.fragment.MerchantRegisterOwnerFragment;

/**
 * @author Muhammad Irfan
 * @since 18/01/2022 14.27
 */
public class MerchantRegisterStepperAdapter extends AbstractFragmentStepAdapter {
    private final Fragment[] fragments;

    {
        this.fragments = new Fragment[3];
    }

    public MerchantRegisterStepperAdapter(@NonNull FragmentManager fm, @NonNull Context context) {
        super(fm, context);
    }

    @Override
    public Step createStep(int position) {
        if (this.fragments[position] == null) {
            if (position == 0) {
                this.fragments[position] = new MerchantRegisterBusinessFragment();
            } else if (position == 1) {
                this.fragments[position] = new MerchantRegisterOwnerFragment();
            } else if (position == 2) {
                this.fragments[position] = new MerchantRegisterAccountFragment();
            }
        }

        return (Step) this.fragments[position];
    }

    @Override
    public int getCount() {
        return 3;
    }

    @NonNull
    @Override
    public StepViewModel getViewModel(int position) {
        String title = null;
        String subtitle = null;

        if (position == 0) {
            title = "Usaha";
            subtitle = "Isi data usaha anda";
        } else if (position == 1) {
            title = "Pemilik";
            subtitle = "Isi data pemilik usaha";
        } else if (position == 2) {
            title = "Akun";
            subtitle = "Isi data akun anda";
        }

        return new StepViewModel.Builder(context)
                .setTitle(title)
                .setSubtitle(subtitle)
                .create();
    }
}
