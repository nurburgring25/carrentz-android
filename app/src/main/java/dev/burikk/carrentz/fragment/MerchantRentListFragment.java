package dev.burikk.carrentz.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.commons.lang3.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dev.burikk.carrentz.R;
import dev.burikk.carrentz.activity.MerchantRentListActivity;
import dev.burikk.carrentz.adapter.MerchantRentListAdapter;
import dev.burikk.carrentz.api.merchant.endpoint.rent.item.MerchantRentItem;
import dev.burikk.carrentz.enumeration.DocumentStatus;
import dev.burikk.carrentz.helper.Views;

/**
 * @author Muhammad Irfan
 * @since 22/12/2021 10.05
 */
@SuppressLint("NonConstantResourceId")
public class MerchantRentListFragment extends Fragment {
    //<editor-fold desc="View">
    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;
    @BindView(R.id.emptyView)
    public View emptyView;
    //</editor-fold>

    //<editor-fold desc="Property">
    public MerchantRentListActivity merchantRentListActivity;
    public Unbinder unbinder;
    public int mode;
    //</editor-fold>


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        this.merchantRentListActivity = (MerchantRentListActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_merchant_rent_list, container, false);

        this.unbinder = ButterKnife.bind(this, view);

        this.extract();
        this.init();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (this.unbinder != null) {
            this.unbinder.unbind();
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void repopulate() {
        MerchantRentListAdapter merchantRentListAdapter = (MerchantRentListAdapter) this.recyclerView.getAdapter();

        if (merchantRentListAdapter != null) {
            merchantRentListAdapter.getMerchantRentItems().clear();

            for (MerchantRentItem merchantRentItem : this.merchantRentListActivity.merchantRentItems) {
                String searchKey = merchantRentItem.getNumber()
                        + merchantRentItem.getUser().getId()
                        + merchantRentItem.getUser().getName()
                        + merchantRentItem.getUser().getPhoneNumber()
                        + merchantRentItem.getVehicle().getName()
                        + merchantRentItem.getVehicle().getLicenseNumber()
                        + merchantRentItem.getVehicle().getVehicleType();

                if (StringUtils.isBlank(this.merchantRentListActivity.searchTerm) || StringUtils.containsIgnoreCase(searchKey, this.merchantRentListActivity.searchTerm)) {
                    if (this.mode == 0) {
                        if (StringUtils.equalsIgnoreCase(DocumentStatus.OPENED.name(), merchantRentItem.getStatus())) {
                            merchantRentListAdapter.getMerchantRentItems().add(merchantRentItem);
                        }
                    } else if (this.mode == 1) {
                        if (StringUtils.equalsIgnoreCase(DocumentStatus.ONGOING.name(), merchantRentItem.getStatus())) {
                            merchantRentListAdapter.getMerchantRentItems().add(merchantRentItem);
                        }
                    } else if (this.mode == 2) {
                        if (StringUtils.equalsIgnoreCase(DocumentStatus.CLOSED.name(), merchantRentItem.getStatus())) {
                            merchantRentListAdapter.getMerchantRentItems().add(merchantRentItem);
                        }
                    } else if (this.mode == 3) {
                        if (StringUtils.equalsIgnoreCase(DocumentStatus.CANCELLED.name(), merchantRentItem.getStatus())) {
                            merchantRentListAdapter.getMerchantRentItems().add(merchantRentItem);
                        }
                    }
                }
            }

            merchantRentListAdapter.notifyDataSetChanged();

            if (!merchantRentListAdapter.getMerchantRentItems().isEmpty()) {
                this.notEmpty();
            } else {
                this.empty();
            }
        }
    }

    private void extract() {
        Bundle bundle = this.getArguments();

        if (bundle != null) {
            this.mode = bundle.getInt("MODE");
        }
    }

    private void init() {
        this.recyclerView.setItemAnimator(new DefaultItemAnimator());
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this.merchantRentListActivity, RecyclerView.VERTICAL, false));
        this.recyclerView.setAdapter(new MerchantRentListAdapter(this.merchantRentListActivity));
    }

    private void notEmpty() {
        Views.visible(this.recyclerView);
        Views.gone(this.emptyView);
    }

    private void empty() {
        Views.gone(this.recyclerView);
        Views.visible(this.emptyView);
    }
}