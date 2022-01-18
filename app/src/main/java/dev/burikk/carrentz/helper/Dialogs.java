package dev.burikk.carrentz.helper;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Ichsanudin Chairin
 * @since 01/02/2018 10:57
 */
public class Dialogs {
    public static void message(
            Context context,
            CharSequence title,
            CharSequence message,
            CharSequence dismiss,
            boolean showButton,
            boolean cancelable

    ) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(cancelable);

        if (showButton) {
            builder.setPositiveButton(StringUtils.isNotBlank(dismiss) ? dismiss : "Mengerti", null);
        }

        builder.show();
    }

    public static void message(
            Context context,
            CharSequence message,
            CharSequence dismiss
    ) {
        message(context, null, message, dismiss, true, true);
    }

    public static void message(
            Context context,
            CharSequence message
    ) {
        message(context, null, message, null, true, true);
    }
}