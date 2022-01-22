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
import dev.burikk.carrentz.activity.StoreFormActivity;
import dev.burikk.carrentz.activity.StoreListActivity;
import dev.burikk.carrentz.api.merchant.endpoint.store.item.StoreItem;
import dev.burikk.carrentz.helper.Generals;

/**
 * @author Muhammad Irfan
 * @since 22/12/2021 10.29
 */
@SuppressLint("NonConstantResourceId")
public class StoreListAdapter extends RecyclerView.Adapter<StoreListAdapter.ViewHolder> {
    private final StoreListActivity deliverySheetListActivity;
    private final List<StoreItem> storeItems;

    {
        this.storeItems = new ArrayList<>();
    }

    public StoreListAdapter(StoreListActivity storeListActivity) {
        this.deliverySheetListActivity = storeListActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_store_list, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        if (i != RecyclerView.NO_POSITION) {
            StoreItem storeItem = this.storeItems.get(i);

            if (storeItem != null) {
                viewHolder.txvName.setText(storeItem.getName());
                viewHolder.txvPhoneNumber.setText(storeItem.getPhoneNumber());
                viewHolder.txvCity.setText(storeItem.getCity());
                viewHolder.txvAddress.setText(storeItem.getAddress());
            }
        }
    }

    @Override
    public int getItemCount() {
        return this.storeItems.size();
    }

    public List<StoreItem> getStoreItems() {
        return this.storeItems;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.txvName)
        public TextView txvName;
        @BindView(R.id.txvPhoneNumber)
        public TextView txvPhoneNumber;
        @BindView(R.id.txvCity)
        public TextView txvCity;
        @BindView(R.id.txvAddress)
        public TextView txvAddress;

        ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (this.getBindingAdapterPosition() != RecyclerView.NO_POSITION) {
                StoreItem storeItem = StoreListAdapter.this.storeItems.get(this.getBindingAdapterPosition());

                if (storeItem != null) {
                    Bundle bundle = new Bundle();

                    bundle.putSerializable("STORE_ITEM", storeItem);

                    Generals.move(StoreListAdapter.this.deliverySheetListActivity, StoreFormActivity.class, bundle, false);
                }
            }
        }
    }
}