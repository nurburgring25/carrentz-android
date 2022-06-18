package dev.burikk.carrentz.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.burikk.carrentz.R;
import dev.burikk.carrentz.activity.MerchantRentByVehicleActivity;
import dev.burikk.carrentz.api.merchant.endpoint.report.item.RentByVehicleItem;
import dev.burikk.carrentz.helper.Formats;

/**
 * @author Muhammad Irfan
 * @since 22/12/2021 10.29
 */
@SuppressLint("NonConstantResourceId")
public class MerchantRentByVehicleAdapter extends RecyclerView.Adapter<MerchantRentByVehicleAdapter.ViewHolder> {
    private final MerchantRentByVehicleActivity merchantRentByVehicleActivity;
    private final List<RentByVehicleItem> rentByVehicleItems;

    {
        this.rentByVehicleItems = new ArrayList<>();
    }

    public MerchantRentByVehicleAdapter(MerchantRentByVehicleActivity merchantRentByVehicleActivity) {
        this.merchantRentByVehicleActivity = merchantRentByVehicleActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_merchant_rent_by_vehicle, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        if (i != RecyclerView.NO_POSITION) {
            RentByVehicleItem rentByVehicleItem = this.rentByVehicleItems.get(i);

            if (rentByVehicleItem != null) {
                viewHolder.txvName.setText(rentByVehicleItem.getName());
                viewHolder.txvVehicleType.setText(rentByVehicleItem.getVehicleTypeName());
                viewHolder.txvLicensePlate.setText(rentByVehicleItem.getLicenseNumber());
                viewHolder.txvDuration.setText(Formats.getCurrencyFormat(rentByVehicleItem.getDuration(), false) + " hari");
                viewHolder.txvAmount.setText(Formats.getCurrencyFormat(rentByVehicleItem.getAmount().longValue()));

                Glide
                        .with(this.merchantRentByVehicleActivity)
                        .load(rentByVehicleItem.getImageUrl())
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(viewHolder.imageView);
            }
        }
    }

    @Override
    public int getItemCount() {
        return this.rentByVehicleItems.size();
    }

    public List<RentByVehicleItem> getRentByVehicleItems() {
        return rentByVehicleItems;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imageView)
        public ImageView imageView;
        @BindView(R.id.txvName)
        public TextView txvName;
        @BindView(R.id.txvVehicleType)
        public TextView txvVehicleType;
        @BindView(R.id.txvLicensePlate)
        public TextView txvLicensePlate;
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