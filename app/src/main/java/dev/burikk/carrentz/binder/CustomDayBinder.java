package dev.burikk.carrentz.binder;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.kizitonwose.calendarview.model.CalendarDay;
import com.kizitonwose.calendarview.ui.DayBinder;
import com.kizitonwose.calendarview.ui.ViewContainer;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.burikk.carrentz.R;
import dev.burikk.carrentz.activity.MerchantVehicleAvailabilityActivity;
import dev.burikk.carrentz.api.merchant.MerchantApi;
import dev.burikk.carrentz.api.merchant.endpoint.vehicleavailibility.item.RentItem;
import dev.burikk.carrentz.helper.Views;

@SuppressLint("NonConstantResourceId")
public class CustomDayBinder implements DayBinder<CustomDayBinder.DayViewContainer> {
    private final MerchantVehicleAvailabilityActivity merchantVehicleAvailabilityActivity;

    public CustomDayBinder(MerchantVehicleAvailabilityActivity merchantVehicleAvailabilityActivity) {
        this.merchantVehicleAvailabilityActivity = merchantVehicleAvailabilityActivity;
    }

    @Override
    public void bind(@NonNull DayViewContainer dayViewContainer, @NonNull CalendarDay calendarDay) {
        dayViewContainer.getView().setTag(null);
        dayViewContainer.txvDay.setText(String.valueOf(calendarDay.getDate().getDayOfMonth()));

        Views.gone(dayViewContainer.vwMark);

        if (this.merchantVehicleAvailabilityActivity.vehicleAvailibilityResponse != null) {
            for (RentItem rentItem : this.merchantVehicleAvailabilityActivity.vehicleAvailibilityResponse.getRentItems()) {
                if (calendarDay.getDate().isEqual(rentItem.getDate())) {
                    Views.visible(dayViewContainer.vwMark);

                    dayViewContainer.getView().setTag(rentItem.getId());

                    break;
                }
            }
        }
    }

    @NonNull
    @Override
    public DayViewContainer create(@NonNull View view) {
        return new DayViewContainer(view);
    }

    class DayViewContainer extends ViewContainer implements View.OnClickListener {
        @BindView(R.id.txvDay)
        public TextView txvDay;
        @BindView(R.id.vwMark)
        public View vwMark;

        public DayViewContainer(@NonNull View view) {
            super(view);

            ButterKnife.bind(this, view);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view.getTag() != null) {
                long id = (long) view.getTag();

                CustomDayBinder.this.merchantVehicleAvailabilityActivity.disposable = MerchantApi.rentGet(CustomDayBinder.this.merchantVehicleAvailabilityActivity, id);
            }
        }
    }
}