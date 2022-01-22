package dev.burikk.carrentz.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import dev.burikk.carrentz.enumeration.SharedPreferenceKey;
import dev.burikk.carrentz.helper.Generals;
import dev.burikk.carrentz.helper.Preferences;

/**
 * @author Muhammad Irfan
 * @since 07/04/2019 19.06
 */
@SuppressLint("CustomSplashScreen")
public class LaunchScreenActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Preferences.contain(SharedPreferenceKey.SESSION_ID)) {
            Generals.move(this, MerchantHomeActivity.class, true);
        } else {
            Generals.move(this, WelcomeActivity.class, true);
        }

        this.finish();
    }
}