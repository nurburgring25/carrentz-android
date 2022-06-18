package dev.burikk.carrentz.api.merchant.endpoint.report.response;

import java.util.ArrayList;
import java.util.List;

import dev.burikk.carrentz.api.merchant.endpoint.report.item.RentByVehicleTypeItem;

public class RentByVehicleTypeResponse {
    private List<RentByVehicleTypeItem> rentByVehicleTypeItems;

    {
        this.rentByVehicleTypeItems = new ArrayList<>();
    }

    public List<RentByVehicleTypeItem> getRentByVehicleTypeItems() {
        return rentByVehicleTypeItems;
    }

    public void setRentByVehicleTypeItems(List<RentByVehicleTypeItem> rentByVehicleTypeItems) {
        this.rentByVehicleTypeItems = rentByVehicleTypeItems;
    }
}