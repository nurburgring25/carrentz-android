package dev.burikk.carrentz.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dev.burikk.carrentz.R;
import dev.burikk.carrentz.adapter.MerchantRentByStoreAdapter;
import dev.burikk.carrentz.api.merchant.MerchantApi;
import dev.burikk.carrentz.api.merchant.endpoint.report.item.RentByStoreItem;
import dev.burikk.carrentz.api.merchant.endpoint.report.response.RentByStoreResponse;
import dev.burikk.carrentz.helper.DataTypes;
import dev.burikk.carrentz.helper.Views;
import dev.burikk.carrentz.protocol.MainProtocol;
import io.reactivex.disposables.Disposable;

@SuppressLint("NonConstantResourceId")
public class MerchantRentByStoreActivity extends AppCompatActivity implements MainProtocol<RentByStoreResponse>, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.edtFrom)
    public TextInputEditText edtFrom;
    @BindView(R.id.edtUntil)
    public TextInputEditText edtUntil;
    @BindView(R.id.pieChart)
    public PieChart pieChart;
    @BindView(R.id.swipeRefreshLayout)
    public SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;
    @BindView(R.id.emptyView)
    public View emptyView;

    public Calendar cldFrom;
    public Calendar cldUntil;
    public Disposable disposable;
    public MaterialDatePicker<Pair<Long, Long>> materialDatePicker;

    {
        this.cldFrom = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

        this.cldFrom.set(Calendar.HOUR, 0);
        this.cldFrom.set(Calendar.MINUTE, 0);
        this.cldFrom.set(Calendar.SECOND, 0);
        this.cldFrom.set(Calendar.MILLISECOND, 0);
        this.cldFrom.add(Calendar.DATE, -6);

        this.cldUntil = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        this.cldUntil.set(Calendar.HOUR, 0);
        this.cldUntil.set(Calendar.MINUTE, 0);
        this.cldUntil.set(Calendar.SECOND, 0);
        this.cldUntil.set(Calendar.MILLISECOND, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_merchant_rent_by_store);

        ButterKnife.bind(this);

        this.toolbar();
        this.widget();
    }

    @Override
    protected void onResume() {
        super.onResume();

        this.dateChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (this.disposable != null) {
            this.disposable.dispose();
        }
    }

    @Override
    public ProgressBar getProgressBar() {
        return null;
    }

    @Override
    public AppCompatActivity getAppCompatActivity() {
        return this;
    }

    @Override
    public void begin() {
        this.swipeRefreshLayout.setRefreshing(true);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void result(RentByStoreResponse data) {
        MerchantRentByStoreAdapter merchantRentByStoreAdapter = (MerchantRentByStoreAdapter) this.recyclerView.getAdapter();

        if (merchantRentByStoreAdapter != null) {
            merchantRentByStoreAdapter.getRentByStoreItems().clear();
            merchantRentByStoreAdapter.getRentByStoreItems().addAll(data.getRentByStoreItems());
            merchantRentByStoreAdapter.notifyDataSetChanged();

            if (!merchantRentByStoreAdapter.getRentByStoreItems().isEmpty()) {
                this.notEmpty();
            } else {
                this.empty();
            }
        }

        {
            ArrayList<PieEntry> pieEntries = new ArrayList<>();

            for (RentByStoreItem rentByStoreItem : data.getRentByStoreItems()) {
                pieEntries.add(new PieEntry(rentByStoreItem.getAmount().floatValue(), rentByStoreItem.getName(), rentByStoreItem));
            }

            PieDataSet pieDataSet = new PieDataSet(pieEntries, "DataSet");
            pieDataSet.setSliceSpace(3f);
            pieDataSet.setSelectionShift(5f);
            pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

            PieData pieData = new PieData(pieDataSet);

            pieData.setValueFormatter(new PercentFormatter());
            pieData.setValueTextSize(11f);
            pieData.setValueTextColor(Color.WHITE);

            this.pieChart.setData(pieData);
            this.pieChart.invalidate();
            this.pieChart.animateY(1400, Easing.EaseInOutQuad);

            Legend legend = this.pieChart.getLegend();
            legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
            legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
            legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
            legend.setDrawInside(false);
            legend.setXEntrySpace(7f);
            legend.setYEntrySpace(0f);
            legend.setYOffset(0f);

            this.pieChart.setEntryLabelColor(Color.WHITE);
            this.pieChart.setEntryLabelTextSize(12f);
        }
    }

    @Override
    public void end() {
        this.swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        this.dateChanged();
    }

    @OnClick(R.id.btnChooseDate)
    public void chooseDate() {
        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();

        builder.setSelection(Pair.create(this.cldFrom.getTimeInMillis(), this.cldUntil.getTimeInMillis()));

        this.materialDatePicker = builder.build();
        this.materialDatePicker.addOnPositiveButtonClickListener(selection -> {
            if (selection != null) {
                if (selection.first != null) {
                    MerchantRentByStoreActivity.this.cldFrom.setTimeInMillis(selection.first);
                    MerchantRentByStoreActivity.this.cldFrom.set(Calendar.HOUR, 0);
                    MerchantRentByStoreActivity.this.cldFrom.set(Calendar.MINUTE, 0);
                    MerchantRentByStoreActivity.this.cldFrom.set(Calendar.SECOND, 0);
                    MerchantRentByStoreActivity.this.cldFrom.set(Calendar.MILLISECOND, 0);
                }

                if (selection.second != null) {
                    MerchantRentByStoreActivity.this.cldUntil.setTimeInMillis(selection.second);
                    MerchantRentByStoreActivity.this.cldUntil.set(Calendar.HOUR, 0);
                    MerchantRentByStoreActivity.this.cldUntil.set(Calendar.MINUTE, 0);
                    MerchantRentByStoreActivity.this.cldUntil.set(Calendar.SECOND, 0);
                    MerchantRentByStoreActivity.this.cldUntil.set(Calendar.MILLISECOND, 0);
                }

                this.dateChanged();
            }
        });

        this.materialDatePicker.show(this.getSupportFragmentManager(), MaterialDatePicker.class.getSimpleName());
    }

    private void toolbar() {
        this.setSupportActionBar(this.toolbar);

        if (this.getSupportActionBar() != null) {
            this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            this.getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    private void widget() {
        Views.disable(
                this.edtFrom,
                this.edtUntil
        );

        this.swipeRefreshLayout.setOnRefreshListener(this);

        this.recyclerView.setItemAnimator(new DefaultItemAnimator());
        this.recyclerView.addItemDecoration(new DividerItemDecoration(this, RecyclerView.VERTICAL));
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        this.recyclerView.setAdapter(new MerchantRentByStoreAdapter(this));

        {
            this.pieChart.setBackgroundColor(Color.WHITE);

            DisplayMetrics displayMetrics = new DisplayMetrics();

            this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

            int height = displayMetrics.heightPixels;

            int offset = (int) (height * 0.20);

            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.pieChart.getLayoutParams();

            layoutParams.setMargins(0, 0, 0, -offset);

            this.pieChart.setLayoutParams(layoutParams);

            this.pieChart.setUsePercentValues(true);
            this.pieChart.getDescription().setEnabled(false);

            this.pieChart.setDrawHoleEnabled(false);

            this.pieChart.setRotationEnabled(false);
            this.pieChart.setHighlightPerTapEnabled(true);

            this.pieChart.setMaxAngle(180f);
            this.pieChart.setRotationAngle(180f);
        }
    }

    private void dateChanged() {
        this.edtFrom.setText(DataTypes.date(this.cldFrom.getTime()));
        this.edtUntil.setText(DataTypes.date(this.cldUntil.getTime()));

        this.disposable = MerchantApi.rentByStores(
                this,
                this.cldFrom.getTimeInMillis(),
                this.cldUntil.getTimeInMillis()
        );
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