package dev.burikk.carrentz.bottomsheet;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dev.burikk.carrentz.R;
import dev.burikk.carrentz.api.user.endpoint.vehicle.item.UserVehicleItem;
import dev.burikk.carrentz.helper.Formats;
import dev.burikk.carrentz.helper.Keyboards;
import io.reactivex.disposables.Disposable;

/**
 * @author Muhammad Irfan
 * @since 19/02/2019 22.45
 */
@SuppressLint("NonConstantResourceId")
public class RentBottomSheet extends BottomSheetDialogFragment {
    @BindView(R.id.nestedScrollView)
    public NestedScrollView nestedScrollView;
    @BindView(R.id.txvStore)
    public TextView txvStore;
    @BindView(R.id.txvName)
    public TextView txvName;
    @BindView(R.id.txvDescription)
    public TextView txvDescription;
    @BindView(R.id.txvVehicleType)
    public TextView txvVehicleType;
    @BindView(R.id.txvLicensePlate)
    public TextView txvLicensePlate;
    @BindView(R.id.txvCostPerDay)
    public TextView txvCostPerDay;
    @BindView(R.id.txvLateReturnFinePerDay)
    public TextView txvLateReturnFinePerDay;

    public UserVehicleItem userVehicleItem;
    public Disposable disposable;
    public AppCompatActivity appCompatActivity;
    public Unbinder unbinder;

    public static RentBottomSheet newInstance(UserVehicleItem userVehicleItem) {
        RentBottomSheet rentBottomSheet = new RentBottomSheet();

        Bundle bundle = new Bundle();

        bundle.putSerializable("USER_VEHICLE_ITEM", userVehicleItem);

        rentBottomSheet.setArguments(bundle);

        return rentBottomSheet;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        this.appCompatActivity = (AppCompatActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_rent, container, false);

        this.unbinder = ButterKnife.bind(this, view);

        this.extract();
        this.init();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        BottomSheets.initialize(this, view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (this.disposable != null) {
            this.disposable.dispose();
        }

        if (this.unbinder != null) {
            this.unbinder.unbind();
        }
    }

    private void extract() {
        if (this.getArguments() != null) {
            this.userVehicleItem = (UserVehicleItem) this.getArguments().getSerializable("USER_VEHICLE_ITEM");
        }
    }

    private void init() {
        new Keyboards(this.appCompatActivity, this.nestedScrollView);

        this.txvStore.setText(this.userVehicleItem.getStoreName());
        this.txvName.setText(this.userVehicleItem.getName());
        this.txvDescription.setText(this.userVehicleItem.getDescription());
        this.txvVehicleType.setText(this.userVehicleItem.getVehicleTypeName());
        this.txvLicensePlate.setText(this.userVehicleItem.getLicenseNumber());
        this.txvCostPerDay.setText(Formats.getCurrencyFormat(this.userVehicleItem.getCostPerDay()));
        this.txvLateReturnFinePerDay.setText(Formats.getCurrencyFormat(this.userVehicleItem.getLateReturnFinePerDay()));
    }
}