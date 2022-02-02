package dev.burikk.carrentz.api.user.endpoint.vehicle.response;

import java.util.ArrayList;
import java.util.List;

import dev.burikk.carrentz.api.user.endpoint.vehicle.item.UserVehicleItem;

public class UserVehicleListResponse {
    private List<UserVehicleItem> details;

    {
        this.details = new ArrayList<>();
    }

    public List<UserVehicleItem> getDetails() {
        return details;
    }

    public void setDetails(List<UserVehicleItem> details) {
        this.details = details;
    }
}