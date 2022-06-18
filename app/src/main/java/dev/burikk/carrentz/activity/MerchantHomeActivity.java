package dev.burikk.carrentz.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.math.BigDecimal;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.burikk.carrentz.R;
import dev.burikk.carrentz.adapter.MerchantHomeMenuAdapter;
import dev.burikk.carrentz.api.merchant.MerchantApi;
import dev.burikk.carrentz.api.merchant.endpoint.dashboard.response.DashboardResponse;
import dev.burikk.carrentz.enumeration.MerchantHomeMenuItem;
import dev.burikk.carrentz.enumeration.SharedPreferenceKey;
import dev.burikk.carrentz.helper.Formats;
import dev.burikk.carrentz.helper.Generals;
import dev.burikk.carrentz.helper.Preferences;
import dev.burikk.carrentz.protocol.MainProtocol;
import io.reactivex.disposables.Disposable;

@SuppressLint("NonConstantResourceId")
public class MerchantHomeActivity extends AppCompatActivity implements MerchantHomeMenuAdapter.MerchantHomeMenuSelectedListener, MainProtocol<DashboardResponse> {
    @BindView(R.id.txvName)
    public TextView txvName;
    @BindView(R.id.rcvMenu)
    public RecyclerView rcvMenu;
    @BindView(R.id.txvIncomingPayment)
    public TextView txvIncomingPayment;
    @BindView(R.id.txvIncomingPaymentDifferencePercentage)
    public TextView txvIncomingPaymentDifferencePercentage;
    @BindView(R.id.txvIncomingPaymentDifferenceAmount)
    public TextView txvIncomingPaymentDifferenceAmount;
    @BindView(R.id.lcIncomingPayment)
    public LineChart lcIncomingPayment;
    @BindView(R.id.txvMostFavoriteVehicle)
    public TextView txvMostFavoriteVehicle;
    @BindView(R.id.txvMostFavoriteVehicleValue)
    public TextView txvMostFavoriteVehicleValue;
    @BindView(R.id.txvMostFavoriteVehicleType)
    public TextView txvMostFavoriteVehicleType;
    @BindView(R.id.txvMostFavoriteVehicleTypeValue)
    public TextView txvMostFavoriteVehicleTypeValue;
    @BindView(R.id.txvMostFavoriteStore)
    public TextView txvMostFavoriteStore;
    @BindView(R.id.txvMostFavoriteStoreValue)
    public TextView txvMostFavoriteStoreValue;
    @BindView(R.id.txvMostFavoriteCustomer)
    public TextView txvMostFavoriteCustomer;
    @BindView(R.id.txvMostFavoriteCustomerValue)
    public TextView txvMostFavoriteCustomerValue;

    public Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_merchant_home);

        ButterKnife.bind(this);

        this.widget();
        this.reload();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (this.disposable != null) {
            this.disposable.dispose();
        }
    }

    @Override
    public void onSelected(MerchantHomeMenuItem merchantHomeMenuItem) {
        if (merchantHomeMenuItem == MerchantHomeMenuItem.STORE) {
            Generals.move(this, StoreListActivity.class, false);
        } else if (merchantHomeMenuItem == MerchantHomeMenuItem.VEHICLE) {
            Generals.move(this, VehicleListActivity.class, false);
        } else if (merchantHomeMenuItem == MerchantHomeMenuItem.RENT) {
            Generals.move(this, MerchantRentListActivity.class, false);
        } else if (merchantHomeMenuItem == MerchantHomeMenuItem.ACCOUNT) {
            Generals.move(this, MerchantAccountActivity.class, false);
        } else if (merchantHomeMenuItem == MerchantHomeMenuItem.VEHICLE_AVAILABILITY) {
            Generals.move(this, MerchantVehicleAvailabilityActivity.class, false);
        } else if (merchantHomeMenuItem == MerchantHomeMenuItem.RENT_BY_VEHICLE) {
            Generals.move(this, MerchantRentByVehicleActivity.class, false);
        } else if (merchantHomeMenuItem == MerchantHomeMenuItem.RENT_BY_VEHICLE_TYPE) {
            Generals.move(this, MerchantRentByVehicleTypeActivity.class, false);
        } else if (merchantHomeMenuItem == MerchantHomeMenuItem.RENT_BY_STORE) {
            Generals.move(this, MerchantRentByStoreActivity.class, false);
        } else if (merchantHomeMenuItem == MerchantHomeMenuItem.RENT_BY_CUSTOMER) {
            Generals.move(this, MerchantRentByCustomerActivity.class, false);
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
    public void result(DashboardResponse data) {
        this.txvIncomingPayment.setText(Formats.getCurrencyFormat(data.getIncomingPayment().longValue()));

        if (data.getIncomingPaymentDifferenceAmount().compareTo(BigDecimal.ZERO) >= 0) {
            this.txvIncomingPaymentDifferencePercentage.setText("+" + Formats.getCurrencyFormat(data.getIncomingPaymentDifferencePercentage().longValue(), false) + "%");
            this.txvIncomingPaymentDifferenceAmount.setText("+" + Formats.getCurrencyFormat(data.getIncomingPaymentDifferenceAmount().longValue()));
        } else {
            this.txvIncomingPaymentDifferencePercentage.setText("-" + Formats.getCurrencyFormat(data.getIncomingPaymentDifferencePercentage().longValue(), false) + "%");
            this.txvIncomingPaymentDifferenceAmount.setText("-" + Formats.getCurrencyFormat(data.getIncomingPaymentDifferenceAmount().longValue()));
        }

        this.txvMostFavoriteVehicle.setText(data.getMostFavoriteVehicle());
        this.txvMostFavoriteVehicleValue.setText(Formats.getCurrencyFormat(data.getMostFavoriteVehicleValue(), false));

        this.txvMostFavoriteVehicleType.setText(data.getMostFavoriteVehicleType());
        this.txvMostFavoriteVehicleTypeValue.setText(Formats.getCurrencyFormat(data.getMostFavoriteVehicleTypeValue(), false));

        this.txvMostFavoriteStore.setText(data.getMostFavoriteStore());
        this.txvMostFavoriteStoreValue.setText(Formats.getCurrencyFormat(data.getMostFavoriteStoreValue().longValue()));

        this.txvMostFavoriteCustomer.setText(data.getMostFavoriteCustomer());
        this.txvMostFavoriteCustomerValue.setText(Formats.getCurrencyFormat(data.getMostFavoriteCustomerValue().longValue()));

        {
            ArrayList<Entry> entries = new ArrayList<>();

            int index = 0;

            for (BigDecimal bigDecimal : data.getLast7DaysIncomingPayment().values()) {
                entries.add(new Entry(index, bigDecimal.floatValue()));

                index++;
            }

            LineDataSet lineDataSet;

            if (this.lcIncomingPayment.getData() != null && this.lcIncomingPayment.getData().getDataSetCount() > 0) {
                lineDataSet = (LineDataSet) this.lcIncomingPayment.getData().getDataSetByIndex(0);
                lineDataSet.setValues(entries);
                lineDataSet.notifyDataSetChanged();

                this.lcIncomingPayment.getData().notifyDataChanged();
                this.lcIncomingPayment.notifyDataSetChanged();
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

                this.lcIncomingPayment.setData(lineData);
            }

            this.lcIncomingPayment.getLegend().setEnabled(false);
            this.lcIncomingPayment.getAxisLeft().setEnabled(false);
            this.lcIncomingPayment.getAxisLeft().setSpaceTop(64);
            this.lcIncomingPayment.getAxisLeft().setSpaceBottom(0);

            this.lcIncomingPayment.getAxisRight().setEnabled(false);
            this.lcIncomingPayment.getXAxis().setEnabled(false);

            this.lcIncomingPayment.animateX(1500, Easing.EaseInOutQuad);
        }
    }

    private void widget() {
        this.txvName.setText(Preferences.get(SharedPreferenceKey.NAME, String.class));

        this.rcvMenu.setItemAnimator(new DefaultItemAnimator());
        this.rcvMenu.setLayoutManager(new GridLayoutManager(this, 4));
        this.rcvMenu.setAdapter(new MerchantHomeMenuAdapter(this));

        this.lcIncomingPayment.getDescription().setEnabled(false);
        this.lcIncomingPayment.setDrawGridBackground(false);
        this.lcIncomingPayment.setTouchEnabled(false);
        this.lcIncomingPayment.setDragEnabled(false);
        this.lcIncomingPayment.setScaleEnabled(false);
        this.lcIncomingPayment.setPinchZoom(false);
        this.lcIncomingPayment.setViewPortOffsets(0, 0, 0, 0);
    }

    private void reload() {
        this.disposable = MerchantApi.dashboard(this);
    }
}