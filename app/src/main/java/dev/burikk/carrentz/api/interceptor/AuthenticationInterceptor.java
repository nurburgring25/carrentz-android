package dev.burikk.carrentz.api.interceptor;

import androidx.annotation.NonNull;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

import dev.burikk.carrentz.BuildConfig;
import dev.burikk.carrentz.CarrentzApp;
import dev.burikk.carrentz.enumeration.SharedPreferenceKey;
import dev.burikk.carrentz.helper.Generals;
import dev.burikk.carrentz.helper.Preferences;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author Muhammad Irfan
 * @since 15/02/2019 13.31
 */
public class AuthenticationInterceptor implements Interceptor {
    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();

        String jwtToken = Preferences.get(SharedPreferenceKey.SESSION_ID, String.class);

        if (StringUtils.isNotBlank(jwtToken)) {
            builder.addHeader("Authorization", "Bearer " + jwtToken);
        }

        builder.addHeader("Carrentz-Fingerprint-Id", Generals.getFingerprintId(CarrentzApp.getInstance().getApplicationContext()));
        builder.addHeader("Carrentz-Platform-Type", "Android");
        builder.addHeader("Carrentz-Version", "1.0.0");
        builder.addHeader("Carrentz-Description", BuildConfig.APPLICATION_ID);
        builder.addHeader("Carrentz-Locale", "id-ID");

        return chain.proceed(builder.build());
    }
}