package dev.burikk.carrentz.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.burikk.carrentz.R;
import dev.burikk.carrentz.api.merchant.endpoint.report.item.RentByVehicleTypeItem;
import dev.burikk.carrentz.helper.Formats;

/**
 * @author Muhammad Irfan
 * @since 22/12/2021 10.29
 */
@SuppressLint("NonConstantResourceId")
public class MerchantRentByVehicleTypeAdapter extends RecyclerView.Adapter<MerchantRentByVehicleTypeAdapter.ViewHolder> {
    private final List<RentByVehicleTypeItem> rentByVehicleTypeItems;

    {
        this.rentByVehicleTypeItems = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_merchant_rent_by_vehicle_type, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        if (i != RecyclerView.NO_POSITION) {
            RentByVehicleTypeItem rentByVehicleTypeItem = this.rentByVehicleTypeItems.get(i);

            if (rentByVehicleTypeItem != null) {
                viewHolder.txvName.setText(rentByVehicleTypeItem.getName());
                viewHolder.txvDuration.setText(Formats.getCurrencyFormat(rentByVehicleTypeItem.getDuration(), false) + " hari");
                viewHolder.txvAmount.setText(Formats.getCurrencyFormat(rentByVehicleTypeItem.getAmount().longValue()));
            }
        }
    }

    @Override
    public int getItemCount() {
        return this.rentByVehicleTypeItems.size();
    }

    public List<RentByVehicleTypeItem> getRentByVehicleTypeItems() {
        return rentByVehicleTypeItems;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txvName)
        public TextView txvName;
        @BindView(R.id.txvDuration)
        public TextView txvDuration;
        @BindView(R.id.txvAmount)
        public TextView txvAmount;

        ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}