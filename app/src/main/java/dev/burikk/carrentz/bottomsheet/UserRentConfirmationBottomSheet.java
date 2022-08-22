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
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dev.burikk.carrentz.R;
import dev.burikk.carrentz.activity.UserHomeActivity;
import dev.burikk.carrentz.activity.UserVehicleDetailActivity;
import dev.burikk.carrentz.api.user.UserApi;
import dev.burikk.carrentz.api.user.endpoint.rent.request.RentRequest;
import dev.burikk.carrentz.api.user.endpoint.vehicle.item.UserVehicleItem;
import dev.burikk.carrentz.helper.DataTypes;
import dev.burikk.carrentz.helper.Formats;
import dev.burikk.carrentz.helper.Generals;
import dev.burikk.carrentz.helper.Keyboards;
import dev.burikk.carrentz.protocol.MainProtocol;
import io.reactivex.disposables.Disposable;

/**
 * @author Muhammad Irfan
 * @since 19/02/2019 22.45
 */
@SuppressLint("NonConstantResourceId")
public class UserRentConfirmationBottomSheet extends BottomSheetDialogFragment implements MainProtocol<Object> {
    @BindView(R.id.linearLayout)
    public LinearLayout linearLayout;
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
    @BindView(R.id.txvDescription)
    public TextView txvDescription;
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
    @BindView(R.id.txvDownPayment)
    public TextView txvDownPayment;

    public UserVehicleDetailActivity userVehicleDetailActivity;
    public UserVehicleItem userVehicleItem;
    public LocalDate start;
    public LocalDate until;
    public Unbinder unbinder;
    public Disposable disposable;

    public static UserRentConfirmationBottomSheet newInstance(
            UserVehicleItem userVehicleItem,
            LocalDate start,
            LocalDate until
    ) {
        UserRentConfirmationBottomSheet userRentConfirmationBottomSheet = new UserRentConfirmationBottomSheet();

        Bundle bundle = new Bundle();

        bundle.putSerializable("USER_VEHICLE_ITEM", userVehicleItem);
        bundle.putSerializable("START", start);
        bundle.putSerializable("UNTIL", until);

        userRentConfirmationBottomSheet.setArguments(bundle);

        return userRentConfirmationBottomSheet;
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

        this.userVehicleDetailActivity = (UserVehicleDetailActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_user_rent_confirmation, container, false);

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

    @Override
    public ProgressBar getProgressBar() {
        return this.progressBar;
    }

    @Override
    public AppCompatActivity getAppCompatActivity() {
        return this.userVehicleDetailActivity;
    }

    @Override
    public void result(Object data) {
        BottomSheets.message(
                this.userVehicleDetailActivity,
                "Kendaraan berhasil disewa.",
                new MessageBottomSheet.MessageBottomSheetCallback() {
                    @Override
                    public void dismiss() {
                        Generals.move(UserRentConfirmationBottomSheet.this.userVehicleDetailActivity, UserHomeActivity.class, true);
                    }

                    @Override
                    public void cancel() {
                        Generals.move(UserRentConfirmationBottomSheet.this.userVehicleDetailActivity, UserHomeActivity.class, true);
                    }
                });
    }

    @OnClick(R.id.btnClose)
    public void close() {
        this.dismiss();
    }

    @OnClick(R.id.btnRentThisVehicle)
    public void rentThisVehicle() {
        RentRequest rentRequest = new RentRequest();

        rentRequest.setVehicleId(this.userVehicleItem.getId());
        rentRequest.setStart(this.start);
        rentRequest.setUntil(this.until);

        this.disposable = UserApi.rent(this, rentRequest);
    }

    private void extract() {
        if (this.getArguments() != null) {
            this.userVehicleItem = (UserVehicleItem) this.getArguments().getSerializable("USER_VEHICLE_ITEM");
            this.start = (LocalDate) this.getArguments().getSerializable("START");
            this.until = (LocalDate) this.getArguments().getSerializable("UNTIL");
        }
    }

    @SuppressLint("SetTextI18n")
    private void init() {
        long duration = ChronoUnit.DAYS.between(this.start, this.until) + 1;

        BigDecimal total = new BigDecimal(this.userVehicleItem.getCostPerDay() * duration);
        BigDecimal downPayment = total.multiply(new BigDecimal(20)).divide(new BigDecimal(100), 0, RoundingMode.CEILING);

        new Keyboards(this.userVehicleDetailActivity, this.linearLayout);

        this.txvStore.setText(this.userVehicleItem.getStoreName());
        this.txvStart.setText(DataTypes.translate(this.start));
        this.txvUntil.setText(DataTypes.translate(this.until));
        this.txvName.setText(this.userVehicleItem.getName());
        this.txvDescription.setText(this.userVehicleItem.getDescription());
        this.txvVehicleType.setText(this.userVehicleItem.getVehicleTypeName());
        this.txvLicensePlate.setText(this.userVehicleItem.getLicenseNumber());
        this.txvDuration.setText(Formats.getCurrencyFormat(duration, false) + " Hari");
        this.txvCostPerDay.setText(Formats.getCurrencyFormat(this.userVehicleItem.getCostPerDay()));
        this.txvTotal.setText(Formats.getCurrencyFormat(total.longValue()));
        this.txvDownPayment.setText(Formats.getCurrencyFormat(downPayment.longValue()));
    }
}