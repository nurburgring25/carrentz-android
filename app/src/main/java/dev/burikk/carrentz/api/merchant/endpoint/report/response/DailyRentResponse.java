package dev.burikk.carrentz.api.merchant.endpoint.report.response;

import java.util.ArrayList;
import java.util.List;

import dev.burikk.carrentz.api.merchant.endpoint.report.item.DailyRentItem;

public class DailyRentResponse {
    private List<DailyRentItem> dailyRentItems;

    {
        this.dailyRentItems = new ArrayList<>();
    }

    public List<DailyRentItem> getDailyRentItems() {
        return dailyRentItems;
    }

    public void setDailyRentItems(List<DailyRentItem> dailyRentItems) {
        this.dailyRentItems = dailyRentItems;
    }
}