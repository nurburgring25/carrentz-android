package dev.burikk.carrentz.api.merchant.endpoint.rent.response;

import java.util.ArrayList;
import java.util.List;

import dev.burikk.carrentz.api.merchant.endpoint.rent.item.MerchantRentItem;

public class MerchantRentListResponse {
    private List<MerchantRentItem> details;

    {
        this.details = new ArrayList<>();
    }

    public List<MerchantRentItem> getDetails() {
        return details;
    }

    public void setDetails(List<MerchantRentItem> details) {
        this.details = details;
    }
}