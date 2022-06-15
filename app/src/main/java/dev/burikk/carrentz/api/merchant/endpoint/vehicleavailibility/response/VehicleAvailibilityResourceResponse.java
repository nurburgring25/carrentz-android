package dev.burikk.carrentz.api.merchant.endpoint.vehicleavailibility.response;

import java.util.ArrayList;
import java.util.List;

import dev.burikk.carrentz.api.merchant.endpoint.vehicleavailibility.item.StoreItem;
import dev.burikk.carrentz.api.merchant.endpoint.vehicleavailibility.item.VehicleItem;

public class VehicleAvailibilityResourceResponse {
    private List<StoreItem> storeItems;
    private List<VehicleItem> vehicleItems;

    {
        this.storeItems = new ArrayList<>();
        this.vehicleItems = new ArrayList<>();
    }

    public List<StoreItem> getStoreItems() {
        return storeItems;
    }

    public void setStoreItems(List<StoreItem> storeItems) {
        this.storeItems = storeItems;
    }

    public List<VehicleItem> getVehicleItems() {
        return vehicleItems;
    }

    public void setVehicleItems(List<VehicleItem> vehicleItems) {
        this.vehicleItems = vehicleItems;
    }
}