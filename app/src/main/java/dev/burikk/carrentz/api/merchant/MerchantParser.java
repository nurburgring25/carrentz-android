package dev.burikk.carrentz.api.merchant;

import dev.burikk.carrentz.api.merchant.endpoint.account.request.RegisterRequest;
import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * @author Muhammad Irfan
 * @since 19/01/2022 14.05
 */
public interface MerchantParser {
    @POST("merchants/register")
    Observable<Response<Void>> register(@Body RegisterRequest registerRequest);
}