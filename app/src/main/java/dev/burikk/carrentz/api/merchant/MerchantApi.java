package dev.burikk.carrentz.api.merchant;

import android.util.Log;

import org.apache.commons.lang3.StringUtils;

import dev.burikk.carrentz.api.RestManager;
import dev.burikk.carrentz.api.merchant.endpoint.account.request.RegisterRequest;
import dev.burikk.carrentz.api.merchant.endpoint.account.request.SignInRequest;
import dev.burikk.carrentz.api.merchant.endpoint.account.request.VerificationRequest;
import dev.burikk.carrentz.api.merchant.endpoint.account.response.SignInResponse;
import dev.burikk.carrentz.api.merchant.endpoint.rent.response.MerchantRentListResponse;
import dev.burikk.carrentz.api.merchant.endpoint.store.item.StoreItem;
import dev.burikk.carrentz.api.merchant.endpoint.store.response.StoreListResponse;
import dev.burikk.carrentz.api.merchant.endpoint.vehicle.item.VehicleItem;
import dev.burikk.carrentz.api.merchant.endpoint.vehicle.response.VehicleListResponse;
import dev.burikk.carrentz.api.merchant.endpoint.vehicle.response.VehicleResourceResponse;
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
public class MerchantApi {
    private static final transient String TAG = MerchantApi.class.getSimpleName();

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
                .create(MerchantParser.class)
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
                .create(MerchantParser.class)
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
                .create(MerchantParser.class)
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

    public static Disposable storeGet(MainProtocol<StoreListResponse> mainProtocol) {
        return RestManager
                .GET_RETROFIT()
                .create(MerchantParser.class)
                .storeGet()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            if (response.isSuccessful()) {
                                StoreListResponse storeListResponse = response.body();

                                if (storeListResponse != null) {
                                    mainProtocol.result(storeListResponse);
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

    public static Disposable storePost(
            MainProtocol<Void> mainProtocol,
            StoreItem storeItem
    ) {
        return RestManager
                .GET_RETROFIT()
                .create(MerchantParser.class)
                .storePost(storeItem)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            if (response.isSuccessful()) {
                                mainProtocol.result(storeItem, null);
                            } else {
                                error(mainProtocol, new HttpException(response));
                            }
                        },
                        throwable -> error(mainProtocol, throwable),
                        () -> finish(mainProtocol),
                        disposable -> process(mainProtocol)
                );
    }

    public static Disposable storePut(
            MainProtocol<Void> mainProtocol,
            StoreItem storeItem,
            long id
    ) {
        return RestManager
                .GET_RETROFIT()
                .create(MerchantParser.class)
                .storePut(storeItem, id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            if (response.isSuccessful()) {
                                mainProtocol.result(storeItem, null);
                            } else {
                                error(mainProtocol, new HttpException(response));
                            }
                        },
                        throwable -> error(mainProtocol, throwable),
                        () -> finish(mainProtocol),
                        disposable -> process(mainProtocol)
                );
    }

    public static Disposable storeDelete(
            MainProtocol<Void> mainProtocol,
            long id
    ) {
        return RestManager
                .GET_RETROFIT()
                .create(MerchantParser.class)
                .storeDelete(id)
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

    public static Disposable vehicleGet(MainProtocol<VehicleListResponse> mainProtocol) {
        return RestManager
                .GET_RETROFIT()
                .create(MerchantParser.class)
                .vehicleGet()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            if (response.isSuccessful()) {
                                VehicleListResponse vehicleListResponse = response.body();

                                if (vehicleListResponse != null) {
                                    mainProtocol.result(vehicleListResponse);
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

    public static Disposable vehiclePost(
            MainProtocol<Object> mainProtocol,
            VehicleItem vehicleItem
    ) {
        return RestManager
                .GET_RETROFIT()
                .create(MerchantParser.class)
                .vehiclePost(vehicleItem)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            if (response.isSuccessful()) {
                                mainProtocol.result(vehicleItem, null);
                            } else {
                                error(mainProtocol, new HttpException(response));
                            }
                        },
                        throwable -> error(mainProtocol, throwable),
                        () -> finish(mainProtocol),
                        disposable -> process(mainProtocol)
                );
    }

    public static Disposable vehiclePut(
            MainProtocol<Object> mainProtocol,
            VehicleItem vehicleItem,
            long id
    ) {
        return RestManager
                .GET_RETROFIT()
                .create(MerchantParser.class)
                .vehiclePut(vehicleItem, id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            if (response.isSuccessful()) {
                                mainProtocol.result(vehicleItem, null);
                            } else {
                                error(mainProtocol, new HttpException(response));
                            }
                        },
                        throwable -> error(mainProtocol, throwable),
                        () -> finish(mainProtocol),
                        disposable -> process(mainProtocol)
                );
    }

    public static Disposable vehicleDelete(
            MainProtocol<Object> mainProtocol,
            long id
    ) {
        return RestManager
                .GET_RETROFIT()
                .create(MerchantParser.class)
                .vehicleDelete(id)
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

    public static Disposable vehicleResource(MainProtocol<Object> mainProtocol) {
        return RestManager
                .GET_RETROFIT()
                .create(MerchantParser.class)
                .vehicleResource()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            if (response.isSuccessful()) {
                                VehicleResourceResponse vehicleResourceResponse = response.body();

                                if (vehicleResourceResponse != null) {
                                    mainProtocol.result(vehicleResourceResponse);
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

    public static Disposable rentGet(MainProtocol<MerchantRentListResponse> mainProtocol) {
        return RestManager
                .GET_RETROFIT()
                .create(MerchantParser.class)
                .rentGet()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        response -> {
                            if (response.isSuccessful()) {
                                MerchantRentListResponse merchantRentListResponse = response.body();

                                if (merchantRentListResponse != null) {
                                    mainProtocol.result(merchantRentListResponse);
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
}