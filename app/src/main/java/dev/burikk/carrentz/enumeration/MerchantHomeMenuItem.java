package dev.burikk.carrentz.enumeration;

import androidx.recyclerview.widget.RecyclerView;

import dev.burikk.carrentz.R;

/**
 * @author Muhammad Irfan
 * @since 11/09/2019 09.06
 */
public enum MerchantHomeMenuItem {
    STORE(R.drawable.ic_store, "Cabang"),
    VEHICLE(R.drawable.ic_directions_car, "Mobil"),
    RENT(R.drawable.ic_assignment, "Rental"),
    ACCOUNT(R.drawable.ic_person, "Akun"),
    DAILY_RENT(R.drawable.ic_bar_chart, "Rental per Hari"),
    VEHICLE_AVAILABILITY(R.drawable.ic_departure_board, "Ketersediaan Mobil"),
    RENT_BY_VEHICLE(R.drawable.ic_bar_chart, "Rental per Kendaraan"),
    RENT_BY_VEHICLE_TYPE(R.drawable.ic_bar_chart, "Rental per Jenis Kendaraan"),
    RENT_BY_STORE(R.drawable.ic_bar_chart, "Rental per Cabang"),
    RENT_BY_CUSTOMER(R.drawable.ic_bar_chart, "Rental per Pelanggan");

    private int icon;
    private String text;
    private long badge;
    private RecyclerView.ViewHolder viewHolder;

    MerchantHomeMenuItem(
            int icon,
            String text
    ) {
        this.icon = icon;
        this.text = text;
    }

    public int getIcon() {
        return this.icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getBadge() {
        return this.badge;
    }

    public void setBadge(long badge) {
        this.badge = badge;
    }

    public RecyclerView.ViewHolder getViewHolder() {
        return this.viewHolder;
    }

    public void setViewHolder(RecyclerView.ViewHolder viewHolder) {
        this.viewHolder = viewHolder;
    }
}