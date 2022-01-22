package dev.burikk.carrentz.api.user;

import android.util.Log;

import org.apache.commons.lang3.StringUtils;

import dev.burikk.carrentz.api.RestManager;
import dev.burikk.carrentz.api.user.endpoint.account.request.RegisterRequest;
import dev.burikk.carrentz.api.user.endpoint.account.request.SignInRequest;
import dev.burikk.carrentz.api.user.endpoint.account.request.VerificationRequest;
import dev.burikk.carrentz.api.user.endpoint.account.response.SignInResponse;
import dev.burikk.carrentz.helper.Dialogs;
import dev.burikk.carrentz.helper.Views;
import dev.burikk.carrentz.protocol.MainProtocol;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;
import retrofit2.Response;

/**
 * @author Muhammad Irfan
 * @since 19/01/2022 15.15
 */
public class UserApi {
    private static final transient String TAG = UserApi.class.getSimpleName();

    public static void process(MainProtocol mainProtocol) {
        mainProtocol.begin();
        Views.visible(mainProtocol.getProgressBar());
    }

    public static void finish(MainProtocol mainProtocol) {
        mainProtocol.end();
        Views.invisible(mainProtocol.getProgressBar());
    }

    public static void error(MainProtocol mainProtocol, Throwable throwable) {
        Views.invisible(mainProtocol.getProgressBar());

        Log.wtf(TAG, throwable);

        String error = "Ada sesuatu yang tidak beres. Mohon untuk mencoba kembali beberapa saat kemudian.";

        if (StringUtils.isNotBlank(throwable.getMessage())) {
            error = throwable.getMessage();
        }

        if (throwable instanceof HttpException) {
            HttpException httpException = (HttpException) throwable;

            try {
                Response response = httpException.response();

                if (response != null) {
                    if (response.errorBody() != null) {
                        Dialogs.message(mainProtocol.getAppCompatActivity(), response.errorBody().string());
                    } else {
                        Dialogs.message(mainProtocol.getAppCompatActivity(), error);
                    }
                }
            } catch (Exception ex) {
                Log.wtf(TAG, ex);

                Dialogs.message(mainProtocol.getAppCompatActivity(), error);
            }
        } else {
            Dialogs.message(mainProtocol.getAppCompatActivity(), error);
        }
    }

    public static Disposable register(
            MainProtocol<Void> mainProtocol,
            RegisterRequest registerRequest
    ) {
        return RestManager
                .GET_RETROFIT()
                .create(UserParser.class)
                .register(registerRequest)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            if (response.isSuccessful()) {
                                mainProtocol.result(null);
                            } else {
                                error(mainProtocol, new HttpException(response));
                            }
                        },
                        throwable -> error(mainProtocol, throwable),
                        () -> finish(mainProtocol),
                        disposable -> process(mainProtocol)
                );
    }

    public static Disposable signIn(
            MainProtocol<SignInResponse> mainProtocol,
            SignInRequest signInRequest
    ) {
        return RestManager
                .GET_RETROFIT()
                .create(UserParser.class)
                .signIn(signInRequest)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            if (response.isSuccessful()) {
                                SignInResponse signInResponse = response.body();

                                if (signInResponse != null) {
                                    mainProtocol.result(signInResponse);
                                } else {
                                    error(mainProtocol, new Exception());
                                }
                            } else {
                                error(mainProtocol, new HttpException(response));
                            }
                        },
                        throwable -> error(mainProtocol, throwable),
                        () -> finish(mainProtocol),
                        disposable -> process(mainProtocol)
                );
    }

    public static Disposable verify(
            MainProtocol<Response<Void>> mainProtocol,
            VerificationRequest verificationRequest
    ) {
        return RestManager
                .GET_RETROFIT()
                .create(UserParser.class)
                .verify(verificationRequest)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            if (response.isSuccessful()) {
                                mainProtocol.result(response);
                            } else {
                                error(mainProtocol, new HttpException(response));
                            }
                        },
                        throwable -> error(mainProtocol, throwable),
                        () -> finish(mainProtocol),
                        disposable -> process(mainProtocol)
                );
    }
}