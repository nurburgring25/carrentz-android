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
import dev.burikk.carrentz.R;
import dev.burikk.carrentz.adapter.UserVehicleListAdapter;
import dev.burikk.carrentz.api.user.UserApi;
import dev.burikk.carrentz.api.user.endpoint.store.item.UserStoreItem;
import dev.burikk.carrentz.api.user.endpoint.vehicle.item.UserVehicleItem;
import dev.burikk.carrentz.api.user.endpoint.vehicle.response.UserVehicleListResponse;
import dev.burikk.carrentz.helper.Views;
import dev.burikk.carrentz.protocol.MainProtocol;
import io.reactivex.disposables.Disposable;

@SuppressLint("NonConstantResourceId")
public class UserVehicleListActivity extends AppCompatActivity implements MainProtocol<UserVehicleListResponse>, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.swipeRefreshLayout)
    public SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;
    @BindView(R.id.emptyView)
    public View emptyView;

    public UserStoreItem userStoreItem;
    public Disposable disposable;
    public CountDownTimer countDownTimer;
    public String searchTerm;
    public List<UserVehicleItem> userVehicleItems;

    {
        this.userVehicleItems = new ArrayList<>();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_user_vehicle_list);

        ButterKnife.bind(this);

        this.extract();
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
        this.getMenuInflater().inflate(R.menu.user_vehicle_list, menu);

        MenuItem mniSearch = menu.findItem(R.id.mniSearch);

        SearchView searchView = (SearchView) mniSearch.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                if (UserVehicleListActivity.this.countDownTimer != null) {
                    UserVehicleListActivity.this.countDownTimer.cancel();
                }

                UserVehicleListActivity.this.countDownTimer = new CountDownTimer(500, 500) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                    }

                    @Override
                    public void onFinish() {
                        UserVehicleListActivity.this.searchTerm = newText;
                        UserVehicleListActivity.this.repopulate();
                    }
                };

                UserVehicleListActivity.this.countDownTimer.start();

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
    public void result(UserVehicleListResponse data) {
        this.userVehicleItems.clear();
        this.userVehicleItems.addAll(data.getDetails());
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

    private void extract() {
        Bundle bundle = this.getIntent().getBundleExtra("BUNDLE");

        if (bundle != null) {
            this.userStoreItem = (UserStoreItem) bundle.getSerializable("USER_STORE_ITEM");
        }
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
        this.recyclerView.setAdapter(new UserVehicleListAdapter(this));
    }

    private void reload() {
        this.disposable = UserApi.userVehicleList(this, this.userStoreItem.getId());
    }

    @SuppressLint("NotifyDataSetChanged")
    private void repopulate() {
        UserVehicleListAdapter userVehicleListAdapter = (UserVehicleListAdapter) this.recyclerView.getAdapter();

        if (userVehicleListAdapter != null) {
            userVehicleListAdapter.getUserVehicleItems().clear();

            for (UserVehicleItem userVehicleItem : this.userVehicleItems) {
                String searchKey = userVehicleItem.getName()
                        + userVehicleItem.getVehicleTypeName()
                        + userVehicleItem.getStoreName();

                if (StringUtils.isBlank(this.searchTerm) || StringUtils.containsIgnoreCase(searchKey, this.searchTerm)) {
                    userVehicleListAdapter.getUserVehicleItems().add(userVehicleItem);
                }
            }

            userVehicleListAdapter.notifyDataSetChanged();

            if (!userVehicleListAdapter.getUserVehicleItems().isEmpty()) {
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