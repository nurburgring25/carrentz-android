package dev.burikk.carrentz.api.merchant.endpoint.store.response;

import java.util.ArrayList;
import java.util.List;

import dev.burikk.carrentz.api.merchant.endpoint.store.item.StoreItem;

public class StoreListResponse {
    private List<StoreItem> details;

    {
        this.details = new ArrayList<>();
    }

    public List<StoreItem> getDetails() {
        return details;
    }

    public void setDetails(List<StoreItem> details) {
        this.details = details;
    }
}