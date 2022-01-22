package dev.burikk.carrentz.api.merchant.endpoint.vehicle.response;

import java.util.ArrayList;
import java.util.List;

import dev.burikk.carrentz.api.merchant.endpoint.vehicle.item.VehicleItem;

public class VehicleListResponse {
    private List<VehicleItem> details;

    {
        this.details = new ArrayList<>();
    }

    public List<VehicleItem> getDetails() {
        return details;
    }

    public void setDetails(List<VehicleItem> details) {
        this.details = details;
    }
}