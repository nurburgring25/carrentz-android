package dev.burikk.carrentz.adapter;

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
import dev.burikk.carrentz.activity.UserHomeActivity;
import dev.burikk.carrentz.api.user.endpoint.home.item.HomeVehicleTypeItem;

/**
 * @author Muhammad Irfan
 * @since 16/09/2019 17.21
 */
public class UserHomeVehicleTypeAdapter extends RecyclerView.Adapter<UserHomeVehicleTypeAdapter.ViewHolder> {
    private List<HomeVehicleTypeItem> homeVehicleTypeItems;
    private UserHomeActivity userHomeActivity;
    private UserHomeVehicleTypeSelectedListener userHomeVehicleTypeSelectedListener;

    {
        this.homeVehicleTypeItems = new ArrayList<>();
    }

    public UserHomeVehicleTypeAdapter(
            UserHomeActivity userHomeActivity,
            UserHomeVehicleTypeSelectedListener userHomeVehicleTypeSelectedListener
    ) {
        this.userHomeActivity = userHomeActivity;
        this.userHomeVehicleTypeSelectedListener = userHomeVehicleTypeSelectedListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_user_home_vehicle_type, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position != RecyclerView.NO_POSITION) {
            HomeVehicleTypeItem homeVehicleTypeItem = this.homeVehicleTypeItems.get(position);

            holder.ivImage.setImageResource(this.userHomeActivity.getResources().getIdentifier(homeVehicleTypeItem.getImage(), "drawable", userHomeActivity.getPackageName()));
            holder.txvName.setText(homeVehicleTypeItem.getName());
        }
    }

    @Override
    public int getItemCount() {
        return this.homeVehicleTypeItems.size();
    }

    public List<HomeVehicleTypeItem> getHomeVehicleTypeItems() {
        return homeVehicleTypeItems;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.ivImage)
        public ImageView ivImage;
        @BindView(R.id.txvName)
        public TextView txvName;

        ViewHolder(View view) {
            super(view);

            ButterKnife.bind(this, view);

            this.itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (this.getBindingAdapterPosition() != RecyclerView.NO_POSITION) {
                HomeVehicleTypeItem homeVehicleTypeItem = UserHomeVehicleTypeAdapter.this.homeVehicleTypeItems.get(this.getBindingAdapterPosition());

                if (homeVehicleTypeItem != null) {
                    if (UserHomeVehicleTypeAdapter.this.userHomeVehicleTypeSelectedListener != null) {
                        UserHomeVehicleTypeAdapter.this.userHomeVehicleTypeSelectedListener.onSelected(homeVehicleTypeItem);
                    }
                }
            }
        }
    }

    public interface UserHomeVehicleTypeSelectedListener {
        void onSelected(HomeVehicleTypeItem homeVehicleTypeItem);
    }
}