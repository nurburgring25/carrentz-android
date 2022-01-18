package dev.burikk.carrentz;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import dev.burikk.carrentz.common.Constant;
import dev.burikk.carrentz.helper.Preferences;

/**
 * @author Muhammad Irfan
 * @since 18/01/2022 11.50
 */
public class CarrentzApp extends Application implements Application.ActivityLifecycleCallbacks {
    private static CarrentzApp INSTANCE;

    public static CarrentzApp getInstance() {
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        INSTANCE = this;

        Preferences.getInstance().setSharedPreferences(this.getSharedPreferences(Constant.Application.NAME, Context.MODE_PRIVATE));

        this.registerActivityLifecycleCallbacks(this);
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {

    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {

    }
}