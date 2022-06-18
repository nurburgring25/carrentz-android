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
import dev.burikk.carrentz.api.merchant.endpoint.report.item.DailyRentItem;
import dev.burikk.carrentz.helper.DataTypes;
import dev.burikk.carrentz.helper.Formats;

/**
 * @author Muhammad Irfan
 * @since 22/12/2021 10.29
 */
@SuppressLint("NonConstantResourceId")
public class MerchantDailyRentAdapter extends RecyclerView.Adapter<MerchantDailyRentAdapter.ViewHolder> {
    private final List<DailyRentItem> dailyRentItems;

    {
        this.dailyRentItems = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_merchant_daily_rent, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        if (i != RecyclerView.NO_POSITION) {
            DailyRentItem dailyRentItem = this.dailyRentItems.get(i);

            if (dailyRentItem != null) {
                viewHolder.txvDate.setText(DataTypes.translate(dailyRentItem.getDate()));
                viewHolder.txvDownPayment.setText(Formats.getCurrencyFormat(dailyRentItem.getDownPayment().longValue()));
                viewHolder.txvAmount.setText(Formats.getCurrencyFormat(dailyRentItem.getAmount().longValue()));
                viewHolder.txvLateReturnFine.setText(Formats.getCurrencyFormat(dailyRentItem.getLateReturnFine().longValue()));
            }
        }
    }

    @Override
    public int getItemCount() {
        return this.dailyRentItems.size();
    }

    public List<DailyRentItem> getDailyRentItems() {
        return dailyRentItems;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txvDate)
        public TextView txvDate;
        @BindView(R.id.txvDownPayment)
        public TextView txvDownPayment;
        @BindView(R.id.txvAmount)
        public TextView txvAmount;
        @BindView(R.id.txvLateReturnFine)
        public TextView txvLateReturnFine;

        ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}