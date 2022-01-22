package dev.burikk.carrentz.api.merchant.endpoint.vehicle.response;

import java.util.ArrayList;
import java.util.List;

import dev.burikk.carrentz.api.item.LovItem;

public class VehicleResourceResponse {
    private List<LovItem> stores;
    private List<LovItem> vehicleTypes;

    {
        this.stores = new ArrayList<>();
        this.vehicleTypes = new ArrayList<>();
    }

    public List<LovItem> getStores() {
        return stores;
    }

    public void setStores(List<LovItem> stores) {
        this.stores = stores;
    }

    public List<LovItem> getVehicleTypes() {
        return vehicleTypes;
    }

    public void setVehicleTypes(List<LovItem> vehicleTypes) {
        this.vehicleTypes = vehicleTypes;
    }
}