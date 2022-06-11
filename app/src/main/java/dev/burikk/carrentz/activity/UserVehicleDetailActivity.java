package dev.burikk.carrentz.activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.util.Pair;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ViewListener;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dev.burikk.carrentz.R;
import dev.burikk.carrentz.api.user.UserApi;
import dev.burikk.carrentz.api.user.endpoint.rent.request.RentRequest;
import dev.burikk.carrentz.api.user.endpoint.vehicle.item.UserVehicleImageItem;
import dev.burikk.carrentz.api.user.endpoint.vehicle.item.UserVehicleItem;
import dev.burikk.carrentz.bottomsheet.BottomSheets;
import dev.burikk.carrentz.bottomsheet.MessageBottomSheet;
import dev.burikk.carrentz.helper.Formats;
import dev.burikk.carrentz.helper.Generals;
import dev.burikk.carrentz.protocol.MainProtocol;
import io.reactivex.disposables.Disposable;

/**
 * @author Muhammad Irfan
 * @since 18/01/2022 11.05
 */
@SuppressLint("NonConstantResourceId")
public class UserVehicleDetailActivity extends AppCompatActivity implements MainProtocol<Object>, ViewListener {
    @BindView(R.id.coordinatorLayout)
    public CoordinatorLayout coordinatorLayout;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.progressBar)
    public ProgressBar progressBar;
    @BindView(R.id.carouselView)
    public CarouselView carouselView;
    @BindView(R.id.txvStore)
    public TextView txvStore;
    @BindView(R.id.txvName)
    public TextView txvName;
    @BindView(R.id.txvDescription)
    public TextView txvDescription;
    @BindView(R.id.txvVehicleType)
    public TextView txvVehicleType;
    @BindView(R.id.txvLicensePlate)
    public TextView txvLicensePlate;
    @BindView(R.id.txvCostPerDay)
    public TextView txvCostPerDay;
    @BindView(R.id.txvLateReturnFinePerDay)
    public TextView txvLateReturnFinePerDay;

    public Calendar cldFrom;
    public Calendar cldTo;
    public UserVehicleItem userVehicleItem;
    public Disposable disposable;
    public List<Bitmap> bitmaps;

    {
        this.cldFrom = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        this.cldFrom.set(Calendar.HOUR, 0);
        this.cldFrom.set(Calendar.MINUTE, 0);
        this.cldFrom.set(Calendar.SECOND, 0);
        this.cldFrom.set(Calendar.MILLISECOND, 0);

        this.cldTo = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        this.cldTo.set(Calendar.HOUR, 0);
        this.cldTo.set(Calendar.MINUTE, 0);
        this.cldTo.set(Calendar.SECOND, 0);
        this.cldTo.set(Calendar.MILLISECOND, 0);

        this.bitmaps = new ArrayList<>();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_user_vehicle_detail);

        ButterKnife.bind(this);

        this.extract();
        this.toolbar();
        this.widget();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (this.disposable != null) {
            this.disposable.dispose();
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            this.finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
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
        BottomSheets.message(
                this,
                "Mobil berhasil disewa.",
                new MessageBottomSheet.MessageBottomSheetCallback() {
                    @Override
                    public void dismiss() {
                        Generals.move(UserVehicleDetailActivity.this, UserHomeActivity.class, true);
                    }

                    @Override
                    public void cancel() {
                        Generals.move(UserVehicleDetailActivity.this, UserHomeActivity.class, true);
                    }
                });
    }

    @OnClick(R.id.btnRentThisCar)
    public void rentThisCar() {
        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();

        builder.setSelection(new Pair<>(this.cldFrom.getTimeInMillis(), this.cldTo.getTimeInMillis()));

        MaterialDatePicker<Pair<Long, Long>> materialDatePicker = builder.build();

        materialDatePicker.addOnPositiveButtonClickListener(selection -> {
            if (selection != null) {
                if (selection.first != null) {
                    UserVehicleDetailActivity.this.cldFrom.setTimeInMillis(selection.first);
                    UserVehicleDetailActivity.this.cldFrom.set(Calendar.HOUR, 0);
                    UserVehicleDetailActivity.this.cldFrom.set(Calendar.MINUTE, 0);
                    UserVehicleDetailActivity.this.cldFrom.set(Calendar.SECOND, 0);
                    UserVehicleDetailActivity.this.cldFrom.set(Calendar.MILLISECOND, 0);
                }

                if (selection.second != null) {
                    UserVehicleDetailActivity.this.cldTo.setTimeInMillis(selection.second);
                    UserVehicleDetailActivity.this.cldTo.set(Calendar.HOUR, 0);
                    UserVehicleDetailActivity.this.cldTo.set(Calendar.MINUTE, 0);
                    UserVehicleDetailActivity.this.cldTo.set(Calendar.SECOND, 0);
                    UserVehicleDetailActivity.this.cldTo.set(Calendar.MILLISECOND, 0);
                }

                RentRequest rentRequest = new RentRequest();

                rentRequest.setVehicleId(UserVehicleDetailActivity.this.userVehicleItem.getId());
                rentRequest.setStart(UserVehicleDetailActivity.this.cldFrom.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                rentRequest.setUntil(UserVehicleDetailActivity.this.cldTo.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());

                UserVehicleDetailActivity.this.disposable = UserApi.rent(UserVehicleDetailActivity.this, rentRequest);
            }
        });

        materialDatePicker.show(this.getSupportFragmentManager(), MaterialDatePicker.class.getSimpleName());
    }

    @Override
    public View setViewForPosition(int position) {
        @SuppressLint("InflateParams")
        View customView = getLayoutInflater().inflate(R.layout.adapter_user_vehicle_detail, null);

        ImageView imageView = customView.findViewById(R.id.imageView);

        imageView.setImageBitmap(this.bitmaps.get(position));

        return customView;
    }

    private void extract() {
        Bundle bundle = this.getIntent().getBundleExtra("BUNDLE");

        if (bundle != null) {
            this.userVehicleItem = (UserVehicleItem) bundle.getSerializable("USER_VEHICLE_ITEM");
        }
    }

    private void toolbar() {
        this.setSupportActionBar(this.toolbar);

        if (this.getSupportActionBar() != null) {
            this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            this.getSupportActionBar().setHomeButtonEnabled(true);
            this.getSupportActionBar().setTitle("Rental Mobil");
        }
    }

    @SuppressLint("SetTextI18n")
    private void widget() {
        this.bitmaps = new ArrayList<>();

        this.carouselView.setViewListener(this);

        this.txvStore.setText(this.userVehicleItem.getStoreName());
        this.txvName.setText(this.userVehicleItem.getName());
        this.txvDescription.setText(this.userVehicleItem.getDescription());
        this.txvVehicleType.setText(this.userVehicleItem.getVehicleTypeName());
        this.txvLicensePlate.setText(this.userVehicleItem.getLicenseNumber());
        this.txvCostPerDay.setText(Formats.getCurrencyFormat(this.userVehicleItem.getCostPerDay()));
        this.txvLateReturnFinePerDay.setText(Formats.getCurrencyFormat(this.userVehicleItem.getLateReturnFinePerDay()));

        for (UserVehicleImageItem userVehicleImageItem : this.userVehicleItem.getImages()) {
            Glide
                    .with(this)
                    .asBitmap()
                    .load(userVehicleImageItem.getUrl())
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            UserVehicleDetailActivity.this.bitmaps.add(resource);
                            UserVehicleDetailActivity.this.carouselView.setPageCount(UserVehicleDetailActivity.this.bitmaps.size());
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {}
                    });
        }
    }
}