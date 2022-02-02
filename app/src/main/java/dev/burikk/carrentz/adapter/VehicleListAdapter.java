package dev.burikk.carrentz.adapter;

import android.annotation.SuppressLint;
import android.os.Bundle;
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
import dev.burikk.carrentz.activity.VehicleFormActivity;
import dev.burikk.carrentz.activity.VehicleListActivity;
import dev.burikk.carrentz.api.merchant.endpoint.vehicle.item.VehicleItem;
import dev.burikk.carrentz.helper.Formats;
import dev.burikk.carrentz.helper.Generals;

/**
 * @author Muhammad Irfan
 * @since 22/12/2021 10.29
 */
@SuppressLint("NonConstantResourceId")
public class VehicleListAdapter extends RecyclerView.Adapter<VehicleListAdapter.ViewHolder> {
    private final VehicleListActivity vehicleListActivity;
    private final List<VehicleItem> vehicleItems;

    {
        this.vehicleItems = new ArrayList<>();
    }

    public VehicleListAdapter(VehicleListActivity vehicleListActivity) {
        this.vehicleListActivity = vehicleListActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_vehicle_list, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        if (i != RecyclerView.NO_POSITION) {
            VehicleItem vehicleItem = this.vehicleItems.get(i);

            if (vehicleItem != null) {
                viewHolder.txvName.setText(vehicleItem.getName());
                viewHolder.txvVehicleType.setText(vehicleItem.getVehicleTypeName());
                viewHolder.txvStore.setText(vehicleItem.getStoreName());
                viewHolder.txvPrice.setText(Formats.getCurrencyFormat(vehicleItem.getCostPerDay()));
            }
        }
    }

    @Override
    public int getItemCount() {
        return this.vehicleItems.size();
    }

    public List<VehicleItem> getVehicleItems() {
        return this.vehicleItems;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.txvName)
        public TextView txvName;
        @BindView(R.id.txvVehicleType)
        public TextView txvVehicleType;
        @BindView(R.id.txvStore)
        public TextView txvStore;
        @BindView(R.id.txvPrice)
        public TextView txvPrice;

        ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (this.getBindingAdapterPosition() != RecyclerView.NO_POSITION) {
                VehicleItem vehicleItem = VehicleListAdapter.this.vehicleItems.get(this.getBindingAdapterPosition());

                if (vehicleItem != null) {
                    Bundle bundle = new Bundle();

                    bundle.putSerializable("VEHICLE_ITEM", vehicleItem);

                    Generals.move(VehicleListAdapter.this.vehicleListActivity, VehicleFormActivity.class, bundle, false);
                }
            }
        }
    }
}