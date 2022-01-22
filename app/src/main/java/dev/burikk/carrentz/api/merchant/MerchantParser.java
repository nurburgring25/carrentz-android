package dev.burikk.carrentz.api.merchant;

import dev.burikk.carrentz.api.merchant.endpoint.account.request.RegisterRequest;
import dev.burikk.carrentz.api.merchant.endpoint.account.request.SignInRequest;
import dev.burikk.carrentz.api.merchant.endpoint.account.request.VerificationRequest;
import dev.burikk.carrentz.api.merchant.endpoint.account.response.SignInResponse;
import dev.burikk.carrentz.api.merchant.endpoint.store.item.StoreItem;
import dev.burikk.carrentz.api.merchant.endpoint.store.response.StoreListResponse;
import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * @author Muhammad Irfan
 * @since 19/01/2022 14.05
 */
public interface MerchantParser {
    @POST("merchants/register")
    Observable<Response<Void>> register(@Body RegisterRequest registerRequest);

    @POST("merchants/sign-in")
    Observable<Response<SignInResponse>> signIn(@Body SignInRequest signInRequest);

    @POST("merchants/verify")
    Observable<Response<Void>> verify(@Body VerificationRequest verificationRequest);

    @GET("merchants/stores")
    Observable<Response<StoreListResponse>> storeGet();

    @POST("merchants/stores")
    Observable<Response<Void>> storePost(@Body StoreItem storeItem);

    @PUT("merchants/stores/{id}")
    Observable<Response<Void>> storePut(@Body StoreItem storeItem, @Path("id") long id);

    @DELETE("merchants/stores/{id}")
    Observable<Response<Void>> storeDelete(@Path("id") long id);
}