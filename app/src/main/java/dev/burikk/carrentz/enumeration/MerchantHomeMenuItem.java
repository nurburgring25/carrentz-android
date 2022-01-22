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
    PROFILE(R.drawable.ic_person, "Profil"),
    RENT_REPORT(R.drawable.ic_bar_chart, "Laporan Rental"),
    VEHICLE_AVAILABILITY(R.drawable.ic_departure_board, "Ketersediaan Mobil");

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