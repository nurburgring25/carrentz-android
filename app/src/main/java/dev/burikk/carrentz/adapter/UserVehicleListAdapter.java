package dev.burikk.carrentz.adapter;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.burikk.carrentz.R;
import dev.burikk.carrentz.activity.UserVehicleDetailActivity;
import dev.burikk.carrentz.activity.UserVehicleListActivity;
import dev.burikk.carrentz.api.user.endpoint.vehicle.item.UserVehicleItem;
import dev.burikk.carrentz.helper.Formats;
import dev.burikk.carrentz.helper.Generals;

/**
 * @author Muhammad Irfan
 * @since 22/12/2021 10.29
 */
@SuppressLint("NonConstantResourceId")
public class UserVehicleListAdapter extends RecyclerView.Adapter<UserVehicleListAdapter.ViewHolder> {
    private final UserVehicleListActivity userVehicleListActivity;
    private final List<UserVehicleItem> userVehicleItems;

    {
        this.userVehicleItems = new ArrayList<>();
    }

    public UserVehicleListAdapter(UserVehicleListActivity userVehicleListActivity) {
        this.userVehicleListActivity = userVehicleListActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_user_vehicle_list, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        if (i != RecyclerView.NO_POSITION) {
            UserVehicleItem userVehicleItem = this.userVehicleItems.get(i);

            if (userVehicleItem != null) {
                viewHolder.txvName.setText(userVehicleItem.getName());
                viewHolder.txvVehicleType.setText(userVehicleItem.getVehicleTypeName());
                viewHolder.txvStore.setText(userVehicleItem.getStoreName());
                viewHolder.txvPrice.setText(Formats.getCurrencyFormat(userVehicleItem.getCostPerDay()));
            }
        }
    }

    @Override
    public int getItemCount() {
        return this.userVehicleItems.size();
    }

    public List<UserVehicleItem> getUserVehicleItems() {
        return userVehicleItems;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.imageView)
        public ImageView imageView;
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
                UserVehicleItem userVehicleItem = UserVehicleListAdapter.this.userVehicleItems.get(this.getBindingAdapterPosition());

                if (userVehicleItem != null) {
                    Bundle bundle = new Bundle();

                    bundle.putSerializable("USER_VEHICLE_ITEM", userVehicleItem);

                    Generals.move(UserVehicleListAdapter.this.userVehicleListActivity, UserVehicleDetailActivity.class, bundle, false);
                }
            }
        }
    }
}