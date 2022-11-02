package dev.burikk.carrentz.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.burikk.carrentz.R;
import dev.burikk.carrentz.adapter.MerchantRentListPagerAdapter;
import dev.burikk.carrentz.api.merchant.MerchantApi;
import dev.burikk.carrentz.api.merchant.endpoint.rent.item.MerchantRentItem;
import dev.burikk.carrentz.api.merchant.endpoint.rent.response.MerchantRentListResponse;
import dev.burikk.carrentz.enumeration.DocumentStatus;
import dev.burikk.carrentz.protocol.MainProtocol;
import io.reactivex.disposables.Disposable;

@SuppressLint("NonConstantResourceId")
public class MerchantRentListActivity extends AppCompatActivity implements MainProtocol<MerchantRentListResponse>, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.swipeRefreshLayout)
    public SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.tabLayout)
    public TabLayout tabLayout;
    @BindView(R.id.viewPager)
    public ViewPager2 viewPager;

    public Disposable disposable;
    public CountDownTimer countDownTimer;
    public String searchTerm;
    public List<MerchantRentItem> merchantRentItems;

    {
        this.merchantRentItems = new ArrayList<>();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_merchant_rent_list);

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
        this.getMenuInflater().inflate(R.menu.merchant_rent_list, menu);

        MenuItem mniSearch = menu.findItem(R.id.mniSearch);

        SearchView searchView = (SearchView) mniSearch.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                if (MerchantRentListActivity.this.countDownTimer != null) {
                    MerchantRentListActivity.this.countDownTimer.cancel();
                }

                MerchantRentListActivity.this.countDownTimer = new CountDownTimer(500, 500) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                    }

                    @Override
                    public void onFinish() {
                        MerchantRentListActivity.this.searchTerm = newText;

                        MerchantRentListPagerAdapter merchantRentListPagerAdapter = (MerchantRentListPagerAdapter) MerchantRentListActivity.this.viewPager.getAdapter();

                        if (merchantRentListPagerAdapter != null) {
                            merchantRentListPagerAdapter.repopulate();
                        }
                    }
                };

                MerchantRentListActivity.this.countDownTimer.start();

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
    public void result(MerchantRentListResponse data) {
        this.merchantRentItems.clear();
        this.merchantRentItems.addAll(data.getDetails());

        MerchantRentListPagerAdapter merchantRentListPagerAdapter = (MerchantRentListPagerAdapter) MerchantRentListActivity.this.viewPager.getAdapter();

        if (merchantRentListPagerAdapter != null) {
            merchantRentListPagerAdapter.repopulate();
        }
    }

    @Override
    public void end() {
        this.swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        this.reload();
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

        this.viewPager.setOffscreenPageLimit(4);
        this.viewPager.setAdapter(new MerchantRentListPagerAdapter(this));

        new TabLayoutMediator(this.tabLayout, this.viewPager, (tab, position) -> {
            if (position == 0) {
                tab.setText(DocumentStatus.OPENED.name());
            } else if (position == 1) {
                tab.setText(DocumentStatus.ONGOING.name());
            } else if (position == 2) {
                tab.setText(DocumentStatus.CLOSED.name());
            } else if (position == 3) {
                tab.setText(DocumentStatus.CANCELLED.name());
            }
        }).attach();
    }

    private void reload() {
        this.disposable = MerchantApi.rentGet(this);
    }
}