package dev.burikk.carrentz.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.burikk.carrentz.R;
import dev.burikk.carrentz.bottomsheet.SpinnerBottomSheet;

/**
 * @author Muhammad Irfan
 * @since 10/12/2018 15.37
 */
public class SpinnerAdapter extends RecyclerView.Adapter<SpinnerAdapter.ViewHolder> {
    private List<Item> items;
    private SpinnerBottomSheet spinnerBottomSheet;

    {
        this.items = new ArrayList<>();
    }

    public SpinnerAdapter(SpinnerBottomSheet spinnerBottomSheet) {
        this.spinnerBottomSheet = spinnerBottomSheet;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_spinner, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = this.items.get(position);

        holder.txvName.setText(item.getDescription());
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public List<Item> getItems() {
        return this.items;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.txvName)
        public TextView txvName;

        ViewHolder(View view) {
            super(view);

            ButterKnife.bind(this, view);

            this.init();
        }

        @Override
        public void onClick(View v) {
            Item item = SpinnerAdapter.this.items.get(this.getAdapterPosition());

            if (item != null) {
                if (v == this.itemView) {
                    SpinnerAdapter.this.spinnerBottomSheet.spinnerBottomSheetCallback.select(item);
                    SpinnerAdapter.this.spinnerBottomSheet.dismiss();
                }
            }
        }

        private void init() {
            this.itemView.setOnClickListener(this);
        }
    }

    public static class Item implements Serializable {
        private Object identifier;
        private String description;
        private String selectedDescription;
        private Object tag;

        public Object getIdentifier() {
            return this.identifier;
        }

        public void setIdentifier(Object identifier) {
            this.identifier = identifier;
        }

        public String getDescription() {
            return this.description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getSelectedDescription() {
            return this.selectedDescription;
        }

        public void setSelectedDescription(String selectedDescription) {
            this.selectedDescription = selectedDescription;
        }

        public Object getTag() {
            return this.tag;
        }

        public void setTag(Object tag) {
            this.tag = tag;
        }
    }
}