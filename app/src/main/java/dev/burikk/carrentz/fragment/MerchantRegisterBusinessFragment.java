package dev.burikk.carrentz.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dev.burikk.carrentz.R;
import dev.burikk.carrentz.activity.MerchantRegisterActivity;
import dev.burikk.carrentz.adapter.SpinnerAdapter;
import dev.burikk.carrentz.bottomsheet.BottomSheets;
import dev.burikk.carrentz.bottomsheet.SpinnerBottomSheet;
import dev.burikk.carrentz.common.City;
import dev.burikk.carrentz.common.DialCode;
import dev.burikk.carrentz.helper.Validators;

/**
 * @author Muhammad Irfan
 * @since 18/01/2022 12.13
 */
@SuppressLint("NonConstantResourceId")
public class MerchantRegisterBusinessFragment extends Fragment implements Step, SpinnerBottomSheet.SpinnerBottomSheetCallback {
    @BindView(R.id.edtBusinessName)
    public TextInputEditText edtBusinessName;
    @BindView(R.id.actvDialCode)
    public AutoCompleteTextView actvDialCode;
    @BindView(R.id.edtPhoneNumber)
    public TextInputEditText edtPhoneNumber;
    @BindView(R.id.edtBusinessAddress)
    public TextInputEditText edtBusinessAddress;
    @BindView(R.id.actvCity)
    public AutoCompleteTextView actvCity;

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
        View view = inflater.inflate(R.layout.fragment_merchant_register_business, container, false);

        this.unbinder = ButterKnife.bind(this, view);

        this.widget();

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

    @Override
    public void select(SpinnerAdapter.Item selectedItem) {
        int tag = (int) selectedItem.getTag();

        if (tag == R.id.actvCity) {
            this.actvCity.setText(selectedItem.getDescription());
        } else if (tag == R.id.actvDialCode) {
            this.actvDialCode.setText(selectedItem.getSelectedDescription());
        }
    }

    @OnClick(R.id.actvDialCode)
    public void dialCode() {
        ArrayList<SpinnerAdapter.Item> items = new ArrayList<>();

        for (DialCode dialCode : DialCode.DIAL_CODES) {
            SpinnerAdapter.Item item = new SpinnerAdapter.Item();

            item.setIdentifier(dialCode);
            item.setDescription(dialCode.getCountryCode() + " - " + dialCode.getCountryName() + " (" + dialCode.getDialCode() + ")");
            item.setSelectedDescription(dialCode.getDialCode());
            item.setTag(R.id.actvDialCode);

            items.add(item);
        }

        BottomSheets.spinner(this.merchantRegisterActivity, "Kode Telepon", items, this);
    }

    @OnClick(R.id.actvCity)
    public void city() {
        ArrayList<SpinnerAdapter.Item> items = new ArrayList<>();

        for (String city : City.CITIES) {
            SpinnerAdapter.Item item = new SpinnerAdapter.Item();

            item.setIdentifier(city);
            item.setDescription(city);
            item.setTag(R.id.actvCity);

            items.add(item);
        }

        BottomSheets.spinner(this.merchantRegisterActivity, "Kota", items, this);
    }

    @SuppressLint("SetTextI18n")
    private void widget() {
        this.actvDialCode.setText("+62");
    }

    private boolean valid() {
        List<Boolean> booleans = Arrays.asList(
                Validators.mandatory(this.merchantRegisterActivity, this.edtBusinessName, "Nama usaha"),
                Validators.mandatory(this.merchantRegisterActivity, this.actvDialCode, "Kode telepon"),
                Validators.mandatory(this.merchantRegisterActivity, this.edtPhoneNumber, "Nomor ponsel"),
                Validators.mandatory(this.merchantRegisterActivity, this.edtBusinessAddress, "Alamat usaha"),
                Validators.mandatory(this.merchantRegisterActivity, this.actvCity, "Kota")
        );

        return !booleans.contains(false);
    }
}