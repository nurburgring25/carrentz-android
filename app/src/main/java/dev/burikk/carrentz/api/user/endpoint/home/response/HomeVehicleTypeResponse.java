package dev.burikk.carrentz.api.user.endpoint.home.response;

import java.util.ArrayList;
import java.util.List;

import dev.burikk.carrentz.api.user.endpoint.home.item.HomeVehicleTypeItem;

public class HomeVehicleTypeResponse {
    private List<HomeVehicleTypeItem> details;

    {
        this.details = new ArrayList<>();
    }

    public List<HomeVehicleTypeItem> getDetails() {
        return details;
    }

    public void setDetails(List<HomeVehicleTypeItem> details) {
        this.details = details;
    }
}