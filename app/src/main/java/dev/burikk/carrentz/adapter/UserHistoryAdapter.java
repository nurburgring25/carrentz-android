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
import dev.burikk.carrentz.activity.UserHomeActivity;
import dev.burikk.carrentz.api.user.endpoint.rent.item.UserRentItem;
import dev.burikk.carrentz.bottomsheet.BottomSheets;
import dev.burikk.carrentz.helper.DataTypes;

/**
 * @author Muhammad Irfan
 * @since 22/12/2021 10.29
 */
@SuppressLint("NonConstantResourceId")
public class UserHistoryAdapter extends RecyclerView.Adapter<UserHistoryAdapter.ViewHolder> {
    private final UserHomeActivity userHomeActivity;
    private final List<UserRentItem> userRentItems;

    {
        this.userRentItems = new ArrayList<>();
    }

    public UserHistoryAdapter(UserHomeActivity userHomeActivity) {
        this.userHomeActivity = userHomeActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_user_history, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        if (i != RecyclerView.NO_POSITION) {
            UserRentItem userRentItem = this.userRentItems.get(i);

            if (userRentItem != null) {
                viewHolder.txvNumber.setText(userRentItem.getNumber());
                viewHolder.txvStatus.setText(userRentItem.getStatus());
                viewHolder.txvCustomer.setText(userRentItem.getUser().getId());
                viewHolder.txvVehicle.setText(userRentItem.getVehicle().getName());
                viewHolder.txvDate.setText(DataTypes.translate(userRentItem.getStart()) + " - " + DataTypes.translate(userRentItem.getUntil()));
            }
        }
    }

    @Override
    public int getItemCount() {
        return this.userRentItems.size();
    }

    public List<UserRentItem> getUserRentItems() {
        return userRentItems;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.txvNumber)
        public TextView txvNumber;
        @BindView(R.id.txvStatus)
        public TextView txvStatus;
        @BindView(R.id.txvCustomer)
        public TextView txvCustomer;
        @BindView(R.id.txvVehicle)
        public TextView txvVehicle;
        @BindView(R.id.txvDate)
        public TextView txvDate;

        ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (this.getBindingAdapterPosition() != RecyclerView.NO_POSITION) {
                UserRentItem userRentItem = UserHistoryAdapter.this.userRentItems.get(this.getBindingAdapterPosition());

                if (userRentItem != null) {
                    BottomSheets.userRent(UserHistoryAdapter.this.userHomeActivity, userRentItem);
                }
            }
        }
    }
}