package dev.burikk.carrentz.api.user.endpoint.store.response;

import java.util.ArrayList;
import java.util.List;

import dev.burikk.carrentz.api.user.endpoint.store.item.UserStoreItem;

public class UserStoreListResponse {
    private List<UserStoreItem> details;

    {
        this.details = new ArrayList<>();
    }

    public List<UserStoreItem> getDetails() {
        return details;
    }

    public void setDetails(List<UserStoreItem> details) {
        this.details = details;
    }
}