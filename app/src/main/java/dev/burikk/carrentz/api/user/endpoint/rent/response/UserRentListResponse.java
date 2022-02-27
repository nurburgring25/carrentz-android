package dev.burikk.carrentz.api.user.endpoint.rent.response;

import java.util.ArrayList;
import java.util.List;

import dev.burikk.carrentz.api.user.endpoint.rent.item.UserRentItem;

public class UserRentListResponse {
    private List<UserRentItem> details;

    {
        this.details = new ArrayList<>();
    }

    public List<UserRentItem> getDetails() {
        return details;
    }

    public void setDetails(List<UserRentItem> details) {
        this.details = details;
    }
}