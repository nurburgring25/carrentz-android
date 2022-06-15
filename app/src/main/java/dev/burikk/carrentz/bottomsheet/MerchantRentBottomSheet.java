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
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import org.apache.commons.lang3.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dev.burikk.carrentz.R;
import dev.burikk.carrentz.activity.MerchantRentListActivity;
import dev.burikk.carrentz.api.merchant.MerchantApi;
import dev.burikk.carrentz.api.merchant.endpoint.rent.item.MerchantRentItem;
import dev.burikk.carrentz.enumeration.DocumentStatus;
import dev.burikk.carrentz.helper.DataTypes;
import dev.burikk.carrentz.helper.Formats;
import dev.burikk.carrentz.helper.Keyboards;
import dev.burikk.carrentz.helper.Views;
import dev.burikk.carrentz.protocol.MainProtocol;
import io.reactivex.disposables.Disposable;

/**
 * @author Muhammad Irfan
 * @since 19/02/2019 22.45
 */
@SuppressLint("NonConstantResourceId")
public class MerchantRentBottomSheet extends BottomSheetDialogFragment implements MainProtocol<Object> {
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
    @BindView(R.id.mcvRentCode)
    public MaterialCardView mcvRentCode;
    @BindView(R.id.txvRentCode)
    public TextView txvRentCode;
    @BindView(R.id.btnGetRentCode)
    public MaterialButton btnGetRentCode;
    @BindView(R.id.mcvReturnCode)
    public MaterialCardView mcvReturnCode;
    @BindView(R.id.txvReturnCode)
    public TextView txvReturnCode;
    @BindView(R.id.btnGetReturnCode)
    public MaterialButton btnGetReturnCode;
    @BindView(R.id.btnCancel)
    public MaterialButton btnCancel;

    public AppCompatActivity appCompatActivity;
    public MerchantRentItem merchantRentItem;
    public Unbinder unbinder;
    public Disposable disposable;

    public static MerchantRentBottomSheet newInstance(MerchantRentItem merchantRentItem) {
        MerchantRentBottomSheet merchantRentBottomSheet = new MerchantRentBottomSheet();

        Bundle bundle = new Bundle();

        bundle.putSerializable("MERCHANT_RENT_ITEM", merchantRentItem);

        merchantRentBottomSheet.setArguments(bundle);

        return merchantRentBottomSheet;
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

        this.appCompatActivity = (AppCompatActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_merchant_rent, container, false);

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
        return this.appCompatActivity;
    }

    @Override
    public void result(Object request, Object data) {
        int resultCode = (int) request;

        if (resultCode == 1) {
            Views.gone(this.btnGetRentCode);
            Views.visible(this.txvRentCode);

            this.txvRentCode.setText((String) data);
        } else if (resultCode == 2) {
            Views.gone(this.btnGetReturnCode);
            Views.visible(this.txvReturnCode);

            this.txvReturnCode.setText((String) data);
        } else {
            BottomSheets.message(
                    this.appCompatActivity,
                    "Dokumen telah berhasil dibatalkan."
            );

            this.close();
        }
    }

    @OnClick(R.id.btnClose)
    public void close() {
        this.dismiss();
    }

    @OnClick(R.id.btnGetRentCode)
    public void getRentCode() {
        this.disposable = MerchantApi.rentGetRentCode(
                this,
                this.merchantRentItem.getId()
        );
    }

    @OnClick(R.id.btnGetReturnCode)
    public void getReturnCode() {
        this.disposable = MerchantApi.rentGetReturnCode(
                this,
                this.merchantRentItem.getId()
        );
    }

    @OnClick(R.id.btnCancel)
    public void cancel() {
        BottomSheets.confirmation(
                this.appCompatActivity,
                "Apakah anda yakin ingin membatalkan dokumen ini?",
                "TIDAK",
                "BATALKAN DOKUMEN INI",
                new ConfirmationBottomSheet.ConfirmationBottomSheetCallback() {
                    @Override
                    public void negative() {}

                    @Override
                    public void positive() {
                        MerchantRentBottomSheet.this.disposable = MerchantApi.cancelRent(
                                MerchantRentBottomSheet.this,
                                MerchantRentBottomSheet.this.merchantRentItem.getId()
                        );
                    }
                }
        );
    }

    private void extract() {
        if (this.getArguments() != null) {
            this.merchantRentItem = (MerchantRentItem) this.getArguments().getSerializable("MERCHANT_RENT_ITEM");
        }
    }

    @SuppressLint("SetTextI18n")
    private void init() {
        new Keyboards(this.appCompatActivity, this.linearLayout);

        this.txvNumber.setText(this.merchantRentItem.getNumber());
        this.txvStore.setText(this.merchantRentItem.getStore().getName());
        this.txvStart.setText(DataTypes.translate(this.merchantRentItem.getStart()));
        this.txvUntil.setText(DataTypes.translate(this.merchantRentItem.getUntil()));
        this.txvName.setText(this.merchantRentItem.getUser().getName());
        this.txvEmail.setText(this.merchantRentItem.getUser().getId());
        this.txvPhoneNumber.setText(this.merchantRentItem.getUser().getPhoneNumber());
        this.txvVehicleName.setText(this.merchantRentItem.getVehicle().getName());
        this.txvVehicleType.setText(this.merchantRentItem.getVehicle().getVehicleType());
        this.txvLicensePlate.setText(this.merchantRentItem.getVehicle().getLicenseNumber());
        this.txvDuration.setText(Formats.getCurrencyFormat(this.merchantRentItem.getDuration().longValue(), false) + " Hari");
        this.txvCostPerDay.setText(Formats.getCurrencyFormat(this.merchantRentItem.getCostPerDay().longValue()));
        this.txvTotal.setText(Formats.getCurrencyFormat(this.merchantRentItem.getTotal().longValue()));

        Views.gone(
                this.mcvRentCode,
                this.mcvReturnCode,
                this.btnCancel
        );

        if (this.appCompatActivity instanceof MerchantRentListActivity) {
            if (StringUtils.equals(this.merchantRentItem.getStatus(), DocumentStatus.OPENED.name())) {
                Views.visible(
                        this.mcvRentCode,
                        this.btnCancel
                );

                if (StringUtils.isNotBlank(this.merchantRentItem.getRentCode())) {
                    Views.gone(this.btnGetRentCode);
                    Views.visible(this.txvRentCode);

                    this.txvRentCode.setText(this.merchantRentItem.getRentCode());
                } else {
                    Views.gone(this.txvRentCode);
                    Views.visible(this.btnGetRentCode);
                }
            }

            if (StringUtils.equals(this.merchantRentItem.getStatus(), DocumentStatus.ONGOING.name())) {
                Views.visible(this.mcvReturnCode);

                if (StringUtils.isNotBlank(this.merchantRentItem.getReturnCode())) {
                    Views.gone(this.btnGetReturnCode);
                    Views.visible(this.txvReturnCode);

                    this.txvReturnCode.setText(this.merchantRentItem.getReturnCode());
                } else {
                    Views.gone(this.txvReturnCode);
                    Views.visible(this.btnGetReturnCode);
                }
            }
        }
    }
}