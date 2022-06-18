package dev.burikk.carrentz.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dev.burikk.carrentz.R;
import dev.burikk.carrentz.adapter.MerchantDailyRentAdapter;
import dev.burikk.carrentz.api.merchant.MerchantApi;
import dev.burikk.carrentz.api.merchant.endpoint.report.item.DailyRentItem;
import dev.burikk.carrentz.api.merchant.endpoint.report.response.DailyRentResponse;
import dev.burikk.carrentz.helper.DataTypes;
import dev.burikk.carrentz.helper.Views;
import dev.burikk.carrentz.protocol.MainProtocol;
import io.reactivex.disposables.Disposable;

@SuppressLint("NonConstantResourceId")
public class MerchantDailyRentActivity extends AppCompatActivity implements MainProtocol<DailyRentResponse>, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.edtFrom)
    public TextInputEditText edtFrom;
    @BindView(R.id.edtUntil)
    public TextInputEditText edtUntil;
    @BindView(R.id.lineChart)
    public LineChart lineChart;
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
        super.setContentView(R.layout.activity_merchant_daily_rent);

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
    public void result(DailyRentResponse data) {
        MerchantDailyRentAdapter merchantDailyRentAdapter = (MerchantDailyRentAdapter) this.recyclerView.getAdapter();

        if (merchantDailyRentAdapter != null) {
            merchantDailyRentAdapter.getDailyRentItems().clear();
            merchantDailyRentAdapter.getDailyRentItems().addAll(data.getDailyRentItems());
            merchantDailyRentAdapter.notifyDataSetChanged();

            if (!merchantDailyRentAdapter.getDailyRentItems().isEmpty()) {
                this.notEmpty();
            } else {
                this.empty();
            }
        }

        {
            ArrayList<Entry> entries = new ArrayList<>();

            int index = 0;

            for (DailyRentItem dailyRentItem : data.getDailyRentItems()) {
                BigDecimal total = dailyRentItem.getDownPayment()
                        .add(dailyRentItem.getAmount())
                        .add(dailyRentItem.getLateReturnFine());

                entries.add(new Entry(index, total.floatValue()));

                index++;
            }

            LineDataSet lineDataSet;

            if (this.lineChart.getData() != null && this.lineChart.getData().getDataSetCount() > 0) {
                lineDataSet = (LineDataSet) this.lineChart.getData().getDataSetByIndex(0);
                lineDataSet.setValues(entries);
                lineDataSet.notifyDataSetChanged();

                this.lineChart.getData().notifyDataChanged();
                this.lineChart.notifyDataSetChanged();
            } else {
                lineDataSet = new LineDataSet(entries, "DataSet");

                lineDataSet.setDrawIcons(false);
                lineDataSet.setDrawValues(false);
                lineDataSet.setColor(Color.BLACK);
                lineDataSet.setLineWidth(0);
                lineDataSet.setDrawCircleHole(false);
                lineDataSet.setDrawCircles(false);
                lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
                lineDataSet.disableDashedLine();
                lineDataSet.setDrawFilled(true);
                lineDataSet.setFillDrawable(ContextCompat.getDrawable(this, R.drawable.fade_red));

                ArrayList<ILineDataSet> iLineDataSets = new ArrayList<>();

                iLineDataSets.add(lineDataSet);

                LineData lineData = new LineData(iLineDataSets);

                lineData.setHighlightEnabled(false);

                this.lineChart.setData(lineData);
            }

            this.lineChart.getLegend().setEnabled(false);
            this.lineChart.getAxisLeft().setEnabled(false);
            this.lineChart.getAxisLeft().setSpaceTop(64);
            this.lineChart.getAxisLeft().setSpaceBottom(0);

            this.lineChart.getAxisRight().setEnabled(false);
            this.lineChart.getXAxis().setEnabled(false);

            this.lineChart.animateX(1500, Easing.EaseInOutQuad);
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
                    MerchantDailyRentActivity.this.cldFrom.setTimeInMillis(selection.first);
                    MerchantDailyRentActivity.this.cldFrom.set(Calendar.HOUR, 0);
                    MerchantDailyRentActivity.this.cldFrom.set(Calendar.MINUTE, 0);
                    MerchantDailyRentActivity.this.cldFrom.set(Calendar.SECOND, 0);
                    MerchantDailyRentActivity.this.cldFrom.set(Calendar.MILLISECOND, 0);
                }

                if (selection.second != null) {
                    MerchantDailyRentActivity.this.cldUntil.setTimeInMillis(selection.second);
                    MerchantDailyRentActivity.this.cldUntil.set(Calendar.HOUR, 0);
                    MerchantDailyRentActivity.this.cldUntil.set(Calendar.MINUTE, 0);
                    MerchantDailyRentActivity.this.cldUntil.set(Calendar.SECOND, 0);
                    MerchantDailyRentActivity.this.cldUntil.set(Calendar.MILLISECOND, 0);
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
        this.recyclerView.setAdapter(new MerchantDailyRentAdapter());

        {
            this.lineChart.getDescription().setEnabled(false);
            this.lineChart.setDrawGridBackground(false);
            this.lineChart.setTouchEnabled(false);
            this.lineChart.setDragEnabled(false);
            this.lineChart.setScaleEnabled(false);
            this.lineChart.setPinchZoom(false);
            this.lineChart.setViewPortOffsets(0, 0, 0, 0);
        }
    }

    private void dateChanged() {
        this.edtFrom.setText(DataTypes.date(this.cldFrom.getTime()));
        this.edtUntil.setText(DataTypes.date(this.cldUntil.getTime()));

        this.disposable = MerchantApi.dailyRents(
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