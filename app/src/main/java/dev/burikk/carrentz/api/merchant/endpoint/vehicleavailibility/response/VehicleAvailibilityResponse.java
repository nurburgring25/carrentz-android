package dev.burikk.carrentz.api.merchant.endpoint.vehicleavailibility.response;

import java.util.ArrayList;
import java.util.List;

import dev.burikk.carrentz.api.merchant.endpoint.vehicleavailibility.item.RentItem;

public class VehicleAvailibilityResponse {
    private List<RentItem> rentItems;

    {
        this.rentItems = new ArrayList<>();
    }

    public List<RentItem> getRentItems() {
        return rentItems;
    }

    public void setRentItems(List<RentItem> rentItems) {
        this.rentItems = rentItems;
    }
}