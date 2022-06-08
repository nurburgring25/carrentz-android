package dev.burikk.carrentz.dialog;

import androidx.appcompat.app.AppCompatActivity;

import dev.burikk.carrentz.api.user.endpoint.rent.item.UserRentItem;

public class Dialogs {
    public static UserTakeTheCarDialog userTakeTheCar(
            AppCompatActivity appCompatActivity,
            UserRentItem userRentItem,
            UserTakeTheCarDialog.UserTakeTheCarDialogCallback userTakeTheCarDialogCallback
    ) {
        UserTakeTheCarDialog userTakeTheCarDialog = UserTakeTheCarDialog.newInstance(userRentItem);

        if (userTakeTheCarDialogCallback != null) {
            userTakeTheCarDialog.callback(userTakeTheCarDialogCallback);
        }

        userTakeTheCarDialog.setCancelable(false);
        userTakeTheCarDialog.show(appCompatActivity.getSupportFragmentManager(), UserTakeTheCarDialog.class.getSimpleName());

        return userTakeTheCarDialog;
    }

    public static UserReturnTheCarDialog userReturnTheCar(
            AppCompatActivity appCompatActivity,
            UserRentItem userRentItem,
            UserReturnTheCarDialog.UserReturnTheCarDialogCallback userReturnTheCarDialogCallback
    ) {
        UserReturnTheCarDialog userReturnTheCarDialog = UserReturnTheCarDialog.newInstance(userRentItem);

        if (userReturnTheCarDialogCallback != null) {
            userReturnTheCarDialog.callback(userReturnTheCarDialogCallback);
        }

        userReturnTheCarDialog.setCancelable(false);
        userReturnTheCarDialog.show(appCompatActivity.getSupportFragmentManager(), UserReturnTheCarDialog.class.getSimpleName());

        return userReturnTheCarDialog;
    }
}