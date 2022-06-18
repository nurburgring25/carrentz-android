package dev.burikk.carrentz.api.merchant;

import java.util.List;

import dev.burikk.carrentz.api.merchant.endpoint.account.request.RegisterRequest;
import dev.burikk.carrentz.api.merchant.endpoint.account.request.SignInRequest;
import dev.burikk.carrentz.api.merchant.endpoint.account.request.VerificationRequest;
import dev.burikk.carrentz.api.merchant.endpoint.account.response.SignInResponse;
import dev.burikk.carrentz.api.merchant.endpoint.rent.item.MerchantRentItem;
import dev.burikk.carrentz.api.merchant.endpoint.rent.response.MerchantRentListResponse;
import dev.burikk.carrentz.api.merchant.endpoint.store.item.StoreItem;
import dev.burikk.carrentz.api.merchant.endpoint.store.response.StoreListResponse;
import dev.burikk.carrentz.api.merchant.endpoint.vehicle.item.VehicleItem;
import dev.burikk.carrentz.api.merchant.endpoint.vehicle.response.VehicleListResponse;
import dev.burikk.carrentz.api.merchant.endpoint.vehicle.response.VehicleResourceResponse;
import dev.burikk.carrentz.api.merchant.endpoint.vehicleavailibility.response.VehicleAvailibilityResourceResponse;
import dev.burikk.carrentz.api.merchant.endpoint.vehicleavailibility.response.VehicleAvailibilityResponse;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

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

    @Multipart
    @POST("merchants/stores")
    Observable<Response<Void>> storePost(
            @Part MultipartBody.Part part,
            @Part("data") StoreItem storeItem
    );

    @Multipart
    @PUT("merchants/stores/{id}")
    Observable<Response<Void>> storePut(
            @Path("id") long id,
            @Part MultipartBody.Part part,
            @Part("data") StoreItem storeItem
    );

    @DELETE("merchants/stores/{id}")
    Observable<Response<Void>> storeDelete(@Path("id") long id);

    @GET("merchants/vehicles")
    Observable<Response<VehicleListResponse>> vehicleGet();

    @Multipart
    @POST("merchants/vehicles")
    Observable<Response<Void>> vehiclePost(
            @Part List<MultipartBody.Part> parts,
            @Part("data") VehicleItem vehicleItem
    );

    @Multipart
    @PUT("merchants/vehicles/{id}")
    Observable<Response<Void>> vehiclePut(
            @Path("id") long id,
            @Part List<MultipartBody.Part> parts,
            @Part("data") VehicleItem vehicleItem
    );

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

    @GET("merchants/vehicle-availibilities")
    Observable<Response<VehicleAvailibilityResponse>> vehicleAvailibilities(@Query("vehicleId") long vehicleId);

    @GET("merchants/vehicle-availibilities/resource")
    Observable<Response<VehicleAvailibilityResourceResponse>> vehicleAvailibilityResources();

    @GET("merchants/rents/{id}")
    Observable<Response<MerchantRentItem>> rentGet(@Path("id") Long id);
}