package dev.burikk.carrentz.protocol;

import android.app.Activity;
import android.widget.ProgressBar;

/**
 * @author Muhammad Irfan
 * @since 09/07/2018 19.02
 */
public interface MainProtocol<T> {
    ProgressBar getProgressBar();

    Activity getActivity();

    default void begin() {}

    default void result(T data) {}

    default void result(Object request, T data) {}

    default void end() {}
}