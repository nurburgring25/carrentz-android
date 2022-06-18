package dev.burikk.carrentz.api.merchant.endpoint.report.response;

import java.util.ArrayList;
import java.util.List;

import dev.burikk.carrentz.api.merchant.endpoint.report.item.RentByCustomerItem;

public class RentByCustomerResponse {
    private List<RentByCustomerItem> rentByCustomerItems;

    {
        this.rentByCustomerItems = new ArrayList<>();
    }

    public List<RentByCustomerItem> getRentByCustomerItems() {
        return rentByCustomerItems;
    }

    public void setRentByCustomerItems(List<RentByCustomerItem> rentByCustomerItems) {
        this.rentByCustomerItems = rentByCustomerItems;
    }
}