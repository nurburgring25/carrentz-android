package dev.burikk.carrentz.bottomsheet;

import android.app.Dialog;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

import dev.burikk.carrentz.CarrentzApp;
import dev.burikk.carrentz.R;
import dev.burikk.carrentz.adapter.SpinnerAdapter;
import dev.burikk.carrentz.api.merchant.endpoint.rent.item.MerchantRentItem;
import dev.burikk.carrentz.api.user.endpoint.rent.item.UserRentItem;
import dev.burikk.carrentz.api.user.endpoint.vehicle.item.UserVehicleItem;
import dev.burikk.carrentz.helper.Dialogs;
import dev.burikk.carrentz.helper.Generals;

/**
 * @author Muhammad Irfan
 * @since 28/12/2021 15.14
 */
public class BottomSheets {
    public static void initialize(final BottomSheetDialogFragment bottomSheetDialogFragment, final View view) {
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                Dialog bottomSheetDialog = bottomSheetDialogFragment.getDialog();

                if (bottomSheetDialog.getWindow() != null) {
                    bottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                }

                FrameLayout frameLayout = bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);

                if (frameLayout != null) {
                    BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(frameLayout);

                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }
        });
    }

    public static void fullHeight(BottomSheetDialog bottomSheetDialog) {
        FrameLayout frameLayout = bottomSheetDialog.findViewById(R.id.design_bottom_sheet);

        if (frameLayout != null) {
            BottomSheetBehavior<FrameLayout> bottomSheetBehavior = BottomSheetBehavior.from(frameLayout);

            ViewGroup.LayoutParams layoutParams = frameLayout.getLayoutParams();

            int windowHeight = Generals.screenHeight(bottomSheetDialog.getContext());

            if (layoutParams != null) {
                layoutParams.height = windowHeight;
            }

            frameLayout.setLayoutParams(layoutParams);

            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }

    public static void message(
            AppCompatActivity appCompatActivity,
            CharSequence title,
            CharSequence message,
            CharSequence dismiss,
            boolean showButton,
            boolean cancelable,
            MessageBottomSheet.MessageBottomSheetCallback messageBottomSheetCallback
    ) {
        MessageBottomSheet messageBottomSheet = MessageBottomSheet.newInstance(title, message, dismiss, showButton, messageBottomSheetCallback != null);

        if (messageBottomSheetCallback != null) {
            messageBottomSheet.callback(messageBottomSheetCallback);
        }

        messageBottomSheet.setCancelable(cancelable);

        if (appCompatActivity != null && !appCompatActivity.getSupportFragmentManager().isStateSaved()) {
            messageBottomSheet.show(appCompatActivity.getSupportFragmentManager(), MessageBottomSheet.class.getSimpleName());
        } else {
            Dialogs.message(CarrentzApp.getInstance().getApplicationContext(), title, message, dismiss, showButton, cancelable);
        }
    }

    public static void message(
            AppCompatActivity appCompatActivity,
            CharSequence message,
            CharSequence dismiss,
            MessageBottomSheet.MessageBottomSheetCallback messageBottomSheetCallback
    ) {
        message(appCompatActivity, null, message, dismiss, true, true, messageBottomSheetCallback);
    }

    public static void message(
            AppCompatActivity appCompatActivity,
            CharSequence title,
            CharSequence message,
            CharSequence dismiss
    ) {
        message(appCompatActivity, title, message, dismiss, true, true, null);
    }

    public static void message(
            AppCompatActivity appCompatActivity,
            CharSequence message,
            CharSequence dismiss
    ) {
        message(appCompatActivity, null, message, dismiss, true, true, null);
    }

    public static void message(
            AppCompatActivity appCompatActivity,
            CharSequence message,
            MessageBottomSheet.MessageBottomSheetCallback messageBottomSheetCallback
    ) {
        message(appCompatActivity, null, message, null, true, true, messageBottomSheetCallback);
    }

    public static void message(
            AppCompatActivity appCompatActivity,
            CharSequence message
    ) {
        message(appCompatActivity, null, message, null, true, true, null);
    }

    public static void message(
            AppCompatActivity appCompatActivity,
            CharSequence message,
            boolean showButton,
            boolean cancelable
    ) {
        message(appCompatActivity, null, message, null, showButton, cancelable, null);
    }

    public static void confirmation(
            AppCompatActivity appCompatActivity,
            CharSequence title,
            CharSequence message,
            CharSequence negative,
            CharSequence positive,
            boolean cancelable,
            ConfirmationBottomSheet.ConfirmationBottomSheetCallback confirmationBottomSheetCallback
    ) {
        ConfirmationBottomSheet confirmationBottomSheet = ConfirmationBottomSheet.newInstance(title, message, negative, positive);

        if (confirmationBottomSheetCallback != null) {
            confirmationBottomSheet.callback(confirmationBottomSheetCallback);
        }

        confirmationBottomSheet.setCancelable(cancelable);
        confirmationBottomSheet.show(appCompatActivity.getSupportFragmentManager(), ConfirmationBottomSheet.class.getSimpleName());
    }

    public static void confirmation(
            AppCompatActivity appCompatActivity,
            CharSequence title,
            CharSequence message,
            CharSequence negative,
            CharSequence positive,
            ConfirmationBottomSheet.ConfirmationBottomSheetCallback confirmationBottomSheetCallback
    ) {
        confirmation(appCompatActivity, title, message, negative, positive, true, confirmationBottomSheetCallback);
    }

    public static void confirmation(
            AppCompatActivity appCompatActivity,
            CharSequence title,
            CharSequence negative,
            CharSequence positive,
            boolean cancelable,
            ConfirmationBottomSheet.ConfirmationBottomSheetCallback confirmationBottomSheetCallback
    ) {
        confirmation(appCompatActivity, title, null, negative, positive, cancelable, confirmationBottomSheetCallback);
    }

    public static void confirmation(
            AppCompatActivity appCompatActivity,
            CharSequence title,
            CharSequence negative,
            CharSequence positive,
            ConfirmationBottomSheet.ConfirmationBottomSheetCallback confirmationBottomSheetCallback
    ) {
        confirmation(appCompatActivity, title, null, negative, positive, true, confirmationBottomSheetCallback);
    }

    public static void spinner(
            AppCompatActivity appCompatActivity,
            CharSequence title,
            ArrayList<SpinnerAdapter.Item> items,
            SpinnerBottomSheet.SpinnerBottomSheetCallback spinnerBottomSheetCallback
    ) {
        BottomSheets.spinner(appCompatActivity, title, items, spinnerBottomSheetCallback, true);
    }

    public static void spinner(
            AppCompatActivity appCompatActivity,
            CharSequence title,
            ArrayList<SpinnerAdapter.Item> items,
            SpinnerBottomSheet.SpinnerBottomSheetCallback spinnerBottomSheetCallback,
            boolean cancelable
    ) {
        SpinnerBottomSheet spinnerBottomSheet = SpinnerBottomSheet.newInstance(title, items);

        if (spinnerBottomSheetCallback != null) {
            spinnerBottomSheet.callback(spinnerBottomSheetCallback);
        }

        spinnerBottomSheet.setCancelable(cancelable);

        spinnerBottomSheet.show(appCompatActivity.getSupportFragmentManager(), SpinnerBottomSheet.class.getSimpleName());
    }

    public static RentBottomSheet rent(
            AppCompatActivity appCompatActivity,
            UserVehicleItem userVehicleItem
    ) {
        RentBottomSheet rentBottomSheet = RentBottomSheet.newInstance(userVehicleItem);

        rentBottomSheet.setCancelable(false);
        rentBottomSheet.show(appCompatActivity.getSupportFragmentManager(), RentBottomSheet.class.getSimpleName());

        return rentBottomSheet;
    }

    public static MerchantRentBottomSheet merchantRent(
            AppCompatActivity appCompatActivity,
            MerchantRentItem merchantRentItem
    ) {
        MerchantRentBottomSheet merchantRentBottomSheet = MerchantRentBottomSheet.newInstance(merchantRentItem);

        merchantRentBottomSheet.setCancelable(false);
        merchantRentBottomSheet.show(appCompatActivity.getSupportFragmentManager(), MerchantRentBottomSheet.class.getSimpleName());

        return merchantRentBottomSheet;
    }

    public static UserRentBottomSheet userRent(
            AppCompatActivity appCompatActivity,
            UserRentItem userRentItem
    ) {
        UserRentBottomSheet userRentBottomSheet = UserRentBottomSheet.newInstance(userRentItem);

        userRentBottomSheet.setCancelable(false);
        userRentBottomSheet.show(appCompatActivity.getSupportFragmentManager(), UserRentBottomSheet.class.getSimpleName());

        return userRentBottomSheet;
    }
}