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
import dev.burikk.carrentz.activity.MerchantRentByStoreActivity;
import dev.burikk.carrentz.api.merchant.endpoint.report.item.RentByStoreItem;
import dev.burikk.carrentz.helper.Formats;

/**
 * @author Muhammad Irfan
 * @since 22/12/2021 10.29
 */
@SuppressLint("NonConstantResourceId")
public class MerchantRentByStoreAdapter extends RecyclerView.Adapter<MerchantRentByStoreAdapter.ViewHolder> {
    private final MerchantRentByStoreActivity merchantRentByStoreActivity;
    private final List<RentByStoreItem> rentByStoreItems;

    {
        this.rentByStoreItems = new ArrayList<>();
    }

    public MerchantRentByStoreAdapter(MerchantRentByStoreActivity merchantRentByStoreActivity) {
        this.merchantRentByStoreActivity = merchantRentByStoreActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_merchant_rent_by_store, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        if (i != RecyclerView.NO_POSITION) {
            RentByStoreItem rentByStoreItem = this.rentByStoreItems.get(i);

            if (rentByStoreItem != null) {
                viewHolder.txvName.setText(rentByStoreItem.getName());
                viewHolder.txvPhoneNumber.setText(rentByStoreItem.getPhoneNumber());
                viewHolder.txvCity.setText(rentByStoreItem.getCity());
                viewHolder.txvAmount.setText(Formats.getCurrencyFormat(rentByStoreItem.getAmount().longValue()));

                Glide
                        .with(this.merchantRentByStoreActivity)
                        .load(rentByStoreItem.getImageUrl())
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(viewHolder.imageView);
            }
        }
    }

    @Override
    public int getItemCount() {
        return this.rentByStoreItems.size();
    }

    public List<RentByStoreItem> getRentByStoreItems() {
        return rentByStoreItems;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imageView)
        public ImageView imageView;
        @BindView(R.id.txvName)
        public TextView txvName;
        @BindView(R.id.txvPhoneNumber)
        public TextView txvPhoneNumber;
        @BindView(R.id.txvCity)
        public TextView txvCity;
        @BindView(R.id.txvAmount)
        public TextView txvAmount;

        ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}