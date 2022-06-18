package dev.burikk.carrentz.api.merchant.endpoint.report.response;

import java.util.ArrayList;
import java.util.List;

import dev.burikk.carrentz.api.merchant.endpoint.report.item.RentByVehicleItem;

public class RentByVehicleResponse {
    private List<RentByVehicleItem> rentByVehicleItems;

    {
        this.rentByVehicleItems = new ArrayList<>();
    }

    public List<RentByVehicleItem> getRentByVehicleItems() {
        return rentByVehicleItems;
    }

    public void setRentByVehicleItems(List<RentByVehicleItem> rentByVehicleItems) {
        this.rentByVehicleItems = rentByVehicleItems;
    }
}