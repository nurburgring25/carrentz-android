package dev.burikk.carrentz.api.user;

import dev.burikk.carrentz.api.user.endpoint.account.request.RegisterRequest;
import dev.burikk.carrentz.api.user.endpoint.account.request.SignInRequest;
import dev.burikk.carrentz.api.user.endpoint.account.request.VerificationRequest;
import dev.burikk.carrentz.api.user.endpoint.account.response.SignInResponse;
import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;

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
}