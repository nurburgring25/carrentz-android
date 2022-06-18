package dev.burikk.carrentz.api.merchant.endpoint.report.response;

import java.util.ArrayList;
import java.util.List;

import dev.burikk.carrentz.api.merchant.endpoint.report.item.RentByStoreItem;

public class RentByStoreResponse {
    private List<RentByStoreItem> rentByStoreItems;

    {
        this.rentByStoreItems = new ArrayList<>();
    }

    public List<RentByStoreItem> getRentByStoreItems() {
        return rentByStoreItems;
    }

    public void setRentByStoreItems(List<RentByStoreItem> rentByStoreItems) {
        this.rentByStoreItems = rentByStoreItems;
    }
}