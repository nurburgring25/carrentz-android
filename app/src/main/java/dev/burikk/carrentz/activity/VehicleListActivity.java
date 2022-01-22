package dev.burikk.carrentz.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dev.burikk.carrentz.R;
import dev.burikk.carrentz.adapter.VehicleListAdapter;
import dev.burikk.carrentz.api.merchant.MerchantApi;
import dev.burikk.carrentz.api.merchant.endpoint.vehicle.item.VehicleItem;
import dev.burikk.carrentz.api.merchant.endpoint.vehicle.response.VehicleListResponse;
import dev.burikk.carrentz.helper.Generals;
import dev.burikk.carrentz.helper.Views;
import dev.burikk.carrentz.protocol.MainProtocol;
import io.reactivex.disposables.Disposable;

@SuppressLint("NonConstantResourceId")
public class VehicleListActivity extends AppCompatActivity implements MainProtocol<VehicleListResponse>, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.swipeRefreshLayout)
    public SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;
    @BindView(R.id.emptyView)
    public View emptyView;


    public Disposable disposable;
    public CountDownTimer countDownTimer;
    public String searchTerm;
    public List<VehicleItem> vehicleItems;

    {
        this.vehicleItems = new ArrayList<>();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_vehicle_list);

        ButterKnife.bind(this);

        this.toolbar();
        this.widget();
    }

    @Override
    protected void onResume() {
        super.onResume();

        this.reload();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.vehicle_list, menu);

        MenuItem mniSearch = menu.findItem(R.id.mniSearch);

        SearchView searchView = (SearchView) mniSearch.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                if (VehicleListActivity.this.countDownTimer != null) {
                    VehicleListActivity.this.countDownTimer.cancel();
                }

                VehicleListActivity.this.countDownTimer = new CountDownTimer(500, 500) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                    }

                    @Override
                    public void onFinish() {
                        VehicleListActivity.this.searchTerm = newText;
                        VehicleListActivity.this.repopulate();
                    }
                };

                VehicleListActivity.this.countDownTimer.start();

                return false;
            }
        });

        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (this.disposable != null) {
            this.disposable.dispose();
        }
    }

    @Override
    public AppCompatActivity getAppCompatActivity() {
        return this;
    }

    @Override
    public ProgressBar getProgressBar() {
        return null;
    }

    @Override
    public void begin() {
        this.swipeRefreshLayout.setRefreshing(true);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void result(VehicleListResponse data) {
        this.vehicleItems.clear();
        this.vehicleItems.addAll(data.getDetails());
        this.repopulate();
    }

    @Override
    public void end() {
        this.swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        this.reload();
    }

    @OnClick(R.id.efabAdd)
    public void add() {
        Generals.move(this, VehicleFormActivity.class, false);
    }

    private void toolbar() {
        this.setSupportActionBar(this.toolbar);

        if (this.getSupportActionBar() != null) {
            this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            this.getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    private void widget() {
        this.swipeRefreshLayout.setOnRefreshListener(this);

        this.recyclerView.setItemAnimator(new DefaultItemAnimator());
        this.recyclerView.addItemDecoration(new DividerItemDecoration(this, RecyclerView.VERTICAL));
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        this.recyclerView.setAdapter(new VehicleListAdapter(this));
    }

    private void reload() {
        this.disposable = MerchantApi.vehicleGet(this);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void repopulate() {
        VehicleListAdapter vehicleListAdapter = (VehicleListAdapter) this.recyclerView.getAdapter();

        if (vehicleListAdapter != null) {
            vehicleListAdapter.getVehicleItems().clear();

            for (VehicleItem vehicleItem : this.vehicleItems) {
                String searchKey = vehicleItem.getName()
                        + vehicleItem.getVehicleTypeName()
                        + vehicleItem.getStoreName();

                if (StringUtils.isBlank(this.searchTerm) || StringUtils.containsIgnoreCase(searchKey, this.searchTerm)) {
                    vehicleListAdapter.getVehicleItems().add(vehicleItem);
                }
            }

            vehicleListAdapter.notifyDataSetChanged();

            if (!vehicleListAdapter.getVehicleItems().isEmpty()) {
                this.notEmpty();
            } else {
                this.empty();
            }
        }
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