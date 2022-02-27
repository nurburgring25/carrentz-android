package dev.burikk.carrentz.bottomsheet;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dev.burikk.carrentz.R;
import dev.burikk.carrentz.activity.UserHomeActivity;
import dev.burikk.carrentz.api.user.endpoint.rent.item.UserRentItem;
import dev.burikk.carrentz.helper.DataTypes;
import dev.burikk.carrentz.helper.Formats;
import dev.burikk.carrentz.helper.Keyboards;
import io.reactivex.disposables.Disposable;

/**
 * @author Muhammad Irfan
 * @since 19/02/2019 22.45
 */
@SuppressLint("NonConstantResourceId")
public class UserRentBottomSheet extends BottomSheetDialogFragment {
    @BindView(R.id.linearLayout)
    public LinearLayout linearLayout;
    @BindView(R.id.txvNumber)
    public TextView txvNumber;
    @BindView(R.id.progressBar)
    public ProgressBar progressBar;
    @BindView(R.id.txvStore)
    public TextView txvStore;
    @BindView(R.id.txvStart)
    public TextView txvStart;
    @BindView(R.id.txvUntil)
    public TextView txvUntil;
    @BindView(R.id.txvName)
    public TextView txvName;
    @BindView(R.id.txvEmail)
    public TextView txvEmail;
    @BindView(R.id.txvPhoneNumber)
    public TextView txvPhoneNumber;
    @BindView(R.id.txvVehicleName)
    public TextView txvVehicleName;
    @BindView(R.id.txvVehicleType)
    public TextView txvVehicleType;
    @BindView(R.id.txvLicensePlate)
    public TextView txvLicensePlate;
    @BindView(R.id.txvDuration)
    public TextView txvDuration;
    @BindView(R.id.txvCostPerDay)
    public TextView txvCostPerDay;
    @BindView(R.id.txvTotal)
    public TextView txvTotal;

    public UserHomeActivity userHomeActivity;
    public UserRentItem userRentItem;
    public Unbinder unbinder;
    public Disposable disposable;

    public static UserRentBottomSheet newInstance(UserRentItem userRentItem) {
        UserRentBottomSheet userRentBottomSheet = new UserRentBottomSheet();

        Bundle bundle = new Bundle();

        bundle.putSerializable("USER_RENT_ITEM", userRentItem);

        userRentBottomSheet.setArguments(bundle);

        return userRentBottomSheet;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        dialog.setOnShowListener(dialogInterface -> {
            BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialogInterface;

            BottomSheets.fullHeight(bottomSheetDialog);
        });

        return  dialog;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        this.userHomeActivity = (UserHomeActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_user_rent, container, false);

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

    @OnClick(R.id.btnClose)
    public void close() {
        this.dismiss();
    }

    private void extract() {
        if (this.getArguments() != null) {
            this.userRentItem = (UserRentItem) this.getArguments().getSerializable("USER_RENT_ITEM");
        }
    }

    @SuppressLint("SetTextI18n")
    private void init() {
        new Keyboards(this.userHomeActivity, this.linearLayout);

        this.txvNumber.setText(this.userRentItem.getNumber());
        this.txvStore.setText(this.userRentItem.getStore().getName());
        this.txvStart.setText(DataTypes.translate(this.userRentItem.getStart()));
        this.txvUntil.setText(DataTypes.translate(this.userRentItem.getUntil()));
        this.txvName.setText(this.userRentItem.getUser().getName());
        this.txvEmail.setText(this.userRentItem.getUser().getId());
        this.txvPhoneNumber.setText(this.userRentItem.getUser().getPhoneNumber());
        this.txvVehicleName.setText(this.userRentItem.getVehicle().getName());
        this.txvVehicleType.setText(this.userRentItem.getVehicle().getVehicleType());
        this.txvLicensePlate.setText(this.userRentItem.getVehicle().getLicenseNumber());
        this.txvDuration.setText(Formats.getCurrencyFormat(this.userRentItem.getDuration().longValue(), false) + " Hari");
        this.txvCostPerDay.setText(Formats.getCurrencyFormat(this.userRentItem.getCostPerDay().longValue()));
        this.txvTotal.setText(Formats.getCurrencyFormat(this.userRentItem.getTotal().longValue()));
    }
}