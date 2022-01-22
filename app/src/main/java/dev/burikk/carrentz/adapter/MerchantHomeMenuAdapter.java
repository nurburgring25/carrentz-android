package dev.burikk.carrentz.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.burikk.carrentz.R;
import dev.burikk.carrentz.enumeration.MerchantHomeMenuItem;
import dev.burikk.carrentz.helper.Formats;
import dev.burikk.carrentz.helper.Views;

/**
 * @author Muhammad Irfan
 * @since 16/09/2019 17.21
 */
@SuppressLint("NonConstantResourceId")
public class MerchantHomeMenuAdapter extends RecyclerView.Adapter<MerchantHomeMenuAdapter.ViewHolder> {
    private MerchantHomeMenuSelectedListener merchantHomeMenuSelectedListener;

    public MerchantHomeMenuAdapter(MerchantHomeMenuSelectedListener merchantHomeMenuSelectedListener) {
        this.merchantHomeMenuSelectedListener = merchantHomeMenuSelectedListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_merchant_home_menu, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Views.gone(holder.txvBadge);

        if (position != RecyclerView.NO_POSITION) {
            MerchantHomeMenuItem merchantHomeMenuItem = MerchantHomeMenuItem.values()[position];

            if (merchantHomeMenuItem != null) {
                holder.ivImage.setImageResource(merchantHomeMenuItem.getIcon());
                holder.txvName.setText(merchantHomeMenuItem.getText());

                if (merchantHomeMenuItem.getBadge() > 0) {
                    Views.visible(holder.txvBadge);

                    holder.txvBadge.setText(Formats.getCurrencyFormat(merchantHomeMenuItem.getBadge(), false));
                }

                merchantHomeMenuItem.setViewHolder(holder);
            }
        }
    }

    @Override
    public int getItemCount() {
        return MerchantHomeMenuItem.values().length;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.ivImage)
        public ImageView ivImage;
        @BindView(R.id.txvName)
        public TextView txvName;
        @BindView(R.id.txvBadge)
        public TextView txvBadge;

        ViewHolder(View view) {
            super(view);

            ButterKnife.bind(this, view);

            this.itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (this.getBindingAdapterPosition() != RecyclerView.NO_POSITION) {
                MerchantHomeMenuItem merchantHomeMenuItem = MerchantHomeMenuItem.values()[this.getAdapterPosition()];

                if (merchantHomeMenuItem != null) {
                    if (MerchantHomeMenuAdapter.this.merchantHomeMenuSelectedListener != null) {
                        MerchantHomeMenuAdapter.this.merchantHomeMenuSelectedListener.onSelected(merchantHomeMenuItem);
                    }
                }
            }
        }
    }

    public interface MerchantHomeMenuSelectedListener {
        void onSelected(MerchantHomeMenuItem merchantHomeMenuItem);
    }
}