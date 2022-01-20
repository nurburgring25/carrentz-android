package dev.burikk.carrentz.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dev.burikk.carrentz.R;
import dev.burikk.carrentz.activity.MerchantRegisterActivity;
import dev.burikk.carrentz.helper.Validators;

/**
 * @author Muhammad Irfan
 * @since 18/01/2022 12.13
 */
@SuppressLint("NonConstantResourceId")
public class MerchantRegisterOwnerFragment extends Fragment implements Step {
    @BindView(R.id.edtName)
    public TextInputEditText edtName;

    public MerchantRegisterActivity merchantRegisterActivity;
    public Unbinder unbinder;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        this.merchantRegisterActivity = (MerchantRegisterActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_merchant_register_owner, container, false);

        this.unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (this.unbinder != null) {
            this.unbinder.unbind();
        }
    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        if (!this.valid()) {
            return new VerificationError(null);
        }

        return null;
    }

    @Override
    public void onSelected() {}

    @Override
    public void onError(@NonNull VerificationError error) {}

    private boolean valid() {
        List<Boolean> booleans = Arrays.asList(
                Validators.mandatory(this.merchantRegisterActivity, this.edtName, "Nama"),
                Validators.maxLength(this.edtName, 128)
        );

        return !booleans.contains(false);
    }
}
