package dev.burikk.carrentz.api.merchant;

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

    @GET("merchants/vehicles")
    Observable<Response<VehicleListResponse>> vehicleGet();

    @POST("merchants/vehicles")
    Observable<Response<Void>> vehiclePost(@Body VehicleItem vehicleItem);

    @PUT("merchants/vehicles/{id}")
    Observable<Response<Void>> vehiclePut(@Body VehicleItem vehicleItem, @Path("id") long id);

    @DELETE("merchants/vehicles/{id}")
    Observable<Response<Void>> vehicleDelete(@Path("id") long id);

    @GET("merchants/vehicles/resource")
    Observable<Response<VehicleResourceResponse>> vehicleResource();

    @GET("merchants/rents")
    Observable<Response<MerchantRentListResponse>> rentGet();

    @GET("merchants/rents/{id}/get-rent-code")
    Observable<Response<String>> rentGetRentCode(@Path("id") Long id);

    @GET("merchants/rents/{id}/get-return-code")
    Observable<Response<String>> rentGetReturnCode(@Path("id") Long id);

    @DELETE("merchants/rents/{id}")
    Observable<Response<Void>> cancelRent(@Path("id") Long id);
}