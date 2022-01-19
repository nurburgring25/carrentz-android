package dev.burikk.carrentz.api;

import static java.util.concurrent.TimeUnit.MINUTES;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import com.google.gson.GsonBuilder;

import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import dev.burikk.carrentz.common.Constant;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author Muhammad Irfan
 * @since 09/07/2018 16.07
 */
public class RestManager {
    private static final transient Boolean ALLOW_INSECURE = true;
    private static List<Cookie> COOKIES = new ArrayList<>();

    @SuppressLint("BadHostnameVerifier")
    private static HostnameVerifier GET_HOSTNAME_VERIFIER() {
        return (s, sslSession) -> true;
    }

    @SuppressLint("TrustAllX509TrustManager")
    private static X509TrustManager GET_X509_TRUST_MANAGER() {
        return new X509TrustManager() {
            @Override public void checkClientTrusted(X509Certificate[] chain, String authType) {}

            @Override public void checkServerTrusted(X509Certificate[] chain, String authType) {}

            @Override public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
    }

    private static SSLSocketFactory GET_SSL_SOCKET_FACTORY() {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");

            sslContext.init(null, new TrustManager[]{GET_X509_TRUST_MANAGER()}, null);

            return sslContext.getSocketFactory();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private static OkHttpClient GET_OK_HTTP_CLIENT() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();

        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        if (ALLOW_INSECURE) {
            builder.sslSocketFactory(GET_SSL_SOCKET_FACTORY(), GET_X509_TRUST_MANAGER());
            builder.hostnameVerifier(GET_HOSTNAME_VERIFIER());
        }

        builder.connectTimeout(3, MINUTES);
        builder.readTimeout(3, MINUTES);
        builder.addInterceptor(httpLoggingInterceptor);
        builder.cookieJar(new CookieJar() {
            @Override
            public void saveFromResponse(@NonNull HttpUrl url, @NonNull List<Cookie> cookies) {
                COOKIES = cookies;
            }

            @Override
            public List<Cookie> loadForRequest(@NonNull HttpUrl url) {
                return COOKIES;
            }
        });

        return builder.build();
    }

    public static Retrofit GET_RETROFIT() {
        OkHttpClient okHttpClient = GET_OK_HTTP_CLIENT();

        return new Retrofit.Builder()
                .baseUrl(Constant.URL.BASE)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(
                        new GsonBuilder()
                                .setLenient()
                                .create()
                ))
                .build();
    }
}