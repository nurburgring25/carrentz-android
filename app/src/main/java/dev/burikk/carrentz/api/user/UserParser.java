package dev.burikk.carrentz.api.user;

import dev.burikk.carrentz.api.user.endpoint.account.request.RegisterRequest;
import dev.burikk.carrentz.api.user.endpoint.account.request.SignInRequest;
import dev.burikk.carrentz.api.user.endpoint.account.request.VerificationRequest;
import dev.burikk.carrentz.api.user.endpoint.account.response.SignInResponse;
import dev.burikk.carrentz.api.user.endpoint.home.response.HomeVehicleTypeResponse;
import dev.burikk.carrentz.api.user.endpoint.rent.request.RentRequest;
import dev.burikk.carrentz.api.user.endpoint.rent.response.UserRentListResponse;
import dev.burikk.carrentz.api.user.endpoint.store.response.UserStoreListResponse;
import dev.burikk.carrentz.api.user.endpoint.vehicle.response.UserVehicleListResponse;
import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @author Muhammad Irfan
 * @since 19/01/2022 14.05
 */
public interface UserParser {
    @POST("users/register")
    Observable<Response<Void>> register(@Body RegisterRequest registerRequest);

    @POST("users/sign-in")
    Observable<Response<SignInResponse>> signIn(@Body SignInRequest signInRequest);

    @POST("users/verify")
    Observable<Response<Void>> verify(@Body VerificationRequest verificationRequest);

    @GET("users/homes/vehicle-type")
    Observable<Response<HomeVehicleTypeResponse>> homeVehicleType();

    @GET("users/stores")
    Observable<Response<UserStoreListResponse>> userStoreList();

    @GET("users/vehicles")
    Observable<Response<UserVehicleListResponse>> userVehicleList(@Query("storeId") long id);

    @POST("users/rents")
    Observable<Response<Void>> rent(@Body RentRequest rentRequest);

    @GET("users/rents")
    Observable<Response<UserRentListResponse>> rentGet();

    @GET("users/rents/{id}/take-the-car")
    Observable<Response<Void>> rentTakeTheCar(@Path("id") Long id, @Query("code") String code);

    @GET("users/rents/{id}/return-the-car")
    Observable<Response<Void>> rentReturnTheCar(@Path("id") Long id, @Query("code") String code);
}