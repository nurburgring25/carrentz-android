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
import dev.burikk.carrentz.api.merchant.endpoint.report.item.RentByCustomerItem;
import dev.burikk.carrentz.helper.Formats;

/**
 * @author Muhammad Irfan
 * @since 22/12/2021 10.29
 */
@SuppressLint("NonConstantResourceId")
public class MerchantRentByCustomerAdapter extends RecyclerView.Adapter<MerchantRentByCustomerAdapter.ViewHolder> {
    private final List<RentByCustomerItem> rentByCustomerItems;

    {
        this.rentByCustomerItems = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_merchant_rent_by_customer, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        if (i != RecyclerView.NO_POSITION) {
            RentByCustomerItem rentByCustomerItem = this.rentByCustomerItems.get(i);

            if (rentByCustomerItem != null) {
                viewHolder.txvName.setText(rentByCustomerItem.getName());
                viewHolder.txvPhoneNumber.setText(rentByCustomerItem.getPhoneNumber());
                viewHolder.txvEmail.setText(rentByCustomerItem.getEmailAddress());
                viewHolder.txvAmount.setText(Formats.getCurrencyFormat(rentByCustomerItem.getAmount().longValue()));
            }
        }
    }

    @Override
    public int getItemCount() {
        return this.rentByCustomerItems.size();
    }

    public List<RentByCustomerItem> getRentByCustomerItems() {
        return rentByCustomerItems;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txvName)
        public TextView txvName;
        @BindView(R.id.txvPhoneNumber)
        public TextView txvPhoneNumber;
        @BindView(R.id.txvEmail)
        public TextView txvEmail;
        @BindView(R.id.txvAmount)
        public TextView txvAmount;

        ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}