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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.burikk.carrentz.R;
import dev.burikk.carrentz.activity.UserStoreListActivity;
import dev.burikk.carrentz.activity.UserVehicleListActivity;
import dev.burikk.carrentz.api.user.endpoint.store.item.UserStoreItem;
import dev.burikk.carrentz.common.Constant;
import dev.burikk.carrentz.helper.Generals;

/**
 * @author Muhammad Irfan
 * @since 22/12/2021 10.29
 */
@SuppressLint("NonConstantResourceId")
public class UserStoreListAdapter extends RecyclerView.Adapter<UserStoreListAdapter.ViewHolder> {
    private final UserStoreListActivity userStoreListActivity;
    private final List<UserStoreItem> userStoreItems;

    {
        this.userStoreItems = new ArrayList<>();
    }

    public UserStoreListAdapter(UserStoreListActivity userStoreListActivity) {
        this.userStoreListActivity = userStoreListActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_user_store_list, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        if (i != RecyclerView.NO_POSITION) {
            UserStoreItem userStoreItem = this.userStoreItems.get(i);

            if (userStoreItem != null) {
                viewHolder.txvName.setText(userStoreItem.getName());
                viewHolder.txvPhoneNumber.setText(userStoreItem.getPhoneNumber());
                viewHolder.txvCity.setText(userStoreItem.getCity());
                viewHolder.txvAddress.setText(userStoreItem.getAddress());

                Glide
                        .with(this.userStoreListActivity)
                        .load(userStoreItem.getImageUrl())
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(viewHolder.imageView);
            }
        }
    }

    @Override
    public int getItemCount() {
        return this.userStoreItems.size();
    }

    public List<UserStoreItem> getUserStoreItems() {
        return userStoreItems;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.imageView)
        public ImageView imageView;
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
                UserStoreItem userStoreItem = UserStoreListAdapter.this.userStoreItems.get(this.getBindingAdapterPosition());

                if (userStoreItem != null) {
                    Bundle bundle = new Bundle();

                    bundle.putSerializable("USER_STORE_ITEM", userStoreItem);

                    Generals.move(UserStoreListAdapter.this.userStoreListActivity, UserVehicleListActivity.class, bundle, false);
                }
            }
        }
    }
}