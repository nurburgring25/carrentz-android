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
import dev.burikk.carrentz.activity.MerchantRentListActivity;
import dev.burikk.carrentz.api.merchant.endpoint.rent.item.MerchantRentItem;
import dev.burikk.carrentz.bottomsheet.BottomSheets;

/**
 * @author Muhammad Irfan
 * @since 22/12/2021 10.29
 */
@SuppressLint("NonConstantResourceId")
public class MerchantRentListAdapter extends RecyclerView.Adapter<MerchantRentListAdapter.ViewHolder> {
    private final MerchantRentListActivity merchantRentListActivity;
    private final List<MerchantRentItem> merchantRentItems;

    {
        this.merchantRentItems = new ArrayList<>();
    }

    public MerchantRentListAdapter(MerchantRentListActivity merchantRentListActivity) {
        this.merchantRentListActivity = merchantRentListActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_merchant_rent_list, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        if (i != RecyclerView.NO_POSITION) {
            MerchantRentItem merchantRentItem = this.merchantRentItems.get(i);

            if (merchantRentItem != null) {
                viewHolder.txvNumber.setText(merchantRentItem.getNumber());
            }
        }
    }

    @Override
    public int getItemCount() {
        return this.merchantRentItems.size();
    }

    public List<MerchantRentItem> getMerchantRentItems() {
        return merchantRentItems;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.txvNumber)
        public TextView txvNumber;

        ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (this.getBindingAdapterPosition() != RecyclerView.NO_POSITION) {
                MerchantRentItem merchantRentItem = MerchantRentListAdapter.this.merchantRentItems.get(this.getBindingAdapterPosition());

                if (merchantRentItem != null) {
                    BottomSheets.rent(MerchantRentListAdapter.this.merchantRentListActivity, merchantRentItem);
                }
            }
        }
    }
}