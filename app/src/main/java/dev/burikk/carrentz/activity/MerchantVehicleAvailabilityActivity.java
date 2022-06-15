package dev.burikk.carrentz.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.kizitonwose.calendarview.CalendarView;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dev.burikk.carrentz.R;
import dev.burikk.carrentz.adapter.SpinnerAdapter;
import dev.burikk.carrentz.api.merchant.MerchantApi;
import dev.burikk.carrentz.api.merchant.endpoint.rent.item.MerchantRentItem;
import dev.burikk.carrentz.api.merchant.endpoint.vehicleavailibility.item.StoreItem;
import dev.burikk.carrentz.api.merchant.endpoint.vehicleavailibility.item.VehicleItem;
import dev.burikk.carrentz.api.merchant.endpoint.vehicleavailibility.response.VehicleAvailibilityResourceResponse;
import dev.burikk.carrentz.api.merchant.endpoint.vehicleavailibility.response.VehicleAvailibilityResponse;
import dev.burikk.carrentz.binder.CustomDayBinder;
import dev.burikk.carrentz.bottomsheet.BottomSheets;
import dev.burikk.carrentz.bottomsheet.SpinnerBottomSheet;
import dev.burikk.carrentz.protocol.MainProtocol;
import io.reactivex.disposables.Disposable;
import kotlin.Unit;

@SuppressLint("NonConstantResourceId")
public class MerchantVehicleAvailabilityActivity extends AppCompatActivity implements MainProtocol<Object>, SpinnerBottomSheet.SpinnerBottomSheetCallback {
    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.progressBar)
    public ProgressBar progressBar;
    @BindView(R.id.calendarView)
    public CalendarView calendarView;
    @BindView(R.id.actvStore)
    public AutoCompleteTextView actvStore;
    @BindView(R.id.actvVehicle)
    public AutoCompleteTextView actvVehicle;
    @BindView(R.id.txvMonth)
    public TextView txvMonth;
    @BindView(R.id.txvYear)
    public TextView txvYear;

    public VehicleAvailibilityResourceResponse vehicleAvailibilityResourceResponse;
    public VehicleAvailibilityResponse vehicleAvailibilityResponse;
    public Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_merchant_vehicle_availability);

        ButterKnife.bind(this);

        this.toolbar();
        this.widget();
        this.resource();
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
        return this.progressBar;
    }

    @Override
    public AppCompatActivity getAppCompatActivity() {
        return this;
    }

    @Override
    public void result(Object data) {
        if (data instanceof VehicleAvailibilityResourceResponse) {
            this.vehicleAvailibilityResourceResponse = (VehicleAvailibilityResourceResponse) data;
        } else if (data instanceof VehicleAvailibilityResponse) {
            this.vehicleAvailibilityResponse = (VehicleAvailibilityResponse) data;
            this.calendarView.setDayBinder(new CustomDayBinder(this));
        } else if (data instanceof MerchantRentItem) {
            MerchantRentItem merchantRentItem = (MerchantRentItem) data;

            BottomSheets.merchantRent(
                    this,
                    merchantRentItem
            );
        }
    }

    @Override
    public void select(SpinnerAdapter.Item selectedItem) {
        int tag = (int) selectedItem.getTag();

        if (tag == R.id.actvStore) {
            this.actvStore.setText(selectedItem.getDescription());
            this.actvStore.setTag(selectedItem.getIdentifier());
        } else if (tag == R.id.actvVehicle) {
            this.actvVehicle.setText(selectedItem.getDescription());
            this.actvVehicle.setTag(selectedItem.getIdentifier());
        }
    }

    @OnClick(R.id.actvStore)
    public void store() {
        ArrayList<SpinnerAdapter.Item> items = new ArrayList<>();

        for (StoreItem storeItem : this.vehicleAvailibilityResourceResponse.getStoreItems()) {
            SpinnerAdapter.Item item = new SpinnerAdapter.Item();

            item.setIdentifier(storeItem.getId());
            item.setDescription(storeItem.getDescription());

            item.setTag(R.id.actvStore);

            items.add(item);
        }

        BottomSheets.spinner(this, "Cabang", items, this);
    }

    @OnClick(R.id.actvVehicle)
    public void vehicle() {
        if (this.actvStore.getTag() != null) {
            long storeId = (long) this.actvStore.getTag();

            ArrayList<SpinnerAdapter.Item> items = new ArrayList<>();

            for (VehicleItem vehicleItem : this.vehicleAvailibilityResourceResponse.getVehicleItems()) {
                if (vehicleItem.getStoreId() == storeId) {
                    SpinnerAdapter.Item item = new SpinnerAdapter.Item();

                    item.setIdentifier(vehicleItem.getId());
                    item.setDescription(vehicleItem.getDescription());

                    item.setTag(R.id.actvVehicle);

                    items.add(item);
                }
            }

            BottomSheets.spinner(this, "Kendaraan", items, this);
        }
    }

    @OnClick(R.id.btnReloadData)
    public void reloadData() {
        if (this.actvVehicle.getTag() != null) {
            long vehicleId = (long) this.actvVehicle.getTag();

            this.disposable = MerchantApi.vehicleAvailibilities(this, vehicleId);
        } else {
            BottomSheets.message(
                    this,
                    "Harap pilih dahulu kendaraan."
            );
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
        YearMonth yearMonth = YearMonth.now();

        this.calendarView.setDayBinder(new CustomDayBinder(this));
        this.calendarView.setup(
                yearMonth,
                yearMonth.plusYears(1),
                WeekFields.of(Locale.getDefault()).getFirstDayOfWeek()
        );
        this.calendarView.scrollToMonth(yearMonth);

        this.calendarView.setMonthScrollListener(calendarMonth -> {
            MerchantVehicleAvailabilityActivity.this.txvMonth.setText(DateTimeFormatter.ofPattern("MMMM").format(calendarMonth.getYearMonth()));
            MerchantVehicleAvailabilityActivity.this.txvYear.setText(String.valueOf(calendarMonth.getYearMonth().getYear()));

            return Unit.INSTANCE;
        });
    }

    private void resource() {
        this.disposable = MerchantApi.vehicleAvailibilityResources(this);
    }
}