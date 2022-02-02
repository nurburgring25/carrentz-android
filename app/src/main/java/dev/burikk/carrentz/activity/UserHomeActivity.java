package dev.burikk.carrentz.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ViewListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dev.burikk.carrentz.R;
import dev.burikk.carrentz.adapter.UserHomeVehicleTypeAdapter;
import dev.burikk.carrentz.api.user.UserApi;
import dev.burikk.carrentz.api.user.endpoint.home.item.HomeVehicleTypeItem;
import dev.burikk.carrentz.api.user.endpoint.home.response.HomeVehicleTypeResponse;
import dev.burikk.carrentz.enumeration.SharedPreferenceKey;
import dev.burikk.carrentz.helper.Generals;
import dev.burikk.carrentz.helper.Preferences;
import dev.burikk.carrentz.protocol.MainProtocol;
import io.reactivex.disposables.Disposable;

@SuppressLint("NonConstantResourceId")
public class UserHomeActivity extends AppCompatActivity implements MainProtocol<Object>, ViewListener, UserHomeVehicleTypeAdapter.UserHomeVehicleTypeSelectedListener {
    @BindView(R.id.txvName)
    public TextView txvName;
    @BindView(R.id.carouselView)
    public CarouselView carouselView;
    @BindView(R.id.rcvVehicleType)
    public RecyclerView rcvVehicleType;

    public static final int[] PROMO_IMAGES = {
            R.drawable.img_promo_1,
            R.drawable.img_promo_2,
            R.drawable.img_promo_3,
            R.drawable.img_promo_4,
            R.drawable.img_promo_5
    };

    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_user_home);

        ButterKnife.bind(this);

        this.widget();
        this.loadVehicleType();
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
    public void begin() {}

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void result(Object data) {
        if (data instanceof HomeVehicleTypeResponse) {
            HomeVehicleTypeResponse homeVehicleTypeResponse = (HomeVehicleTypeResponse) data;

            UserHomeVehicleTypeAdapter userHomeVehicleTypeAdapter = (UserHomeVehicleTypeAdapter) this.rcvVehicleType.getAdapter();

            if (userHomeVehicleTypeAdapter != null) {
                userHomeVehicleTypeAdapter.getHomeVehicleTypeItems().clear();
                userHomeVehicleTypeAdapter.getHomeVehicleTypeItems().addAll(homeVehicleTypeResponse.getDetails());
                userHomeVehicleTypeAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void end() {}

    @Override
    public View setViewForPosition(int position) {
        @SuppressLint("InflateParams")
        View customView = getLayoutInflater().inflate(R.layout.adapter_user_home_carousel, null);

        ImageView imageView = customView.findViewById(R.id.imageView);

        imageView.setImageResource(PROMO_IMAGES[position]);

        return customView;
    }

    @Override
    public void onSelected(HomeVehicleTypeItem homeVehicleTypeItem) {

    }

    @OnClick(R.id.llNearest)
    public void nearest() {
        Generals.move(this, UserStoreListActivity.class, false);
    }

    private void widget() {
        this.txvName.setText(Preferences.get(SharedPreferenceKey.NAME, String.class));

        this.carouselView.setPageCount(PROMO_IMAGES.length);
        this.carouselView.setViewListener(this);

        this.rcvVehicleType.setItemAnimator(new DefaultItemAnimator());
        this.rcvVehicleType.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.rcvVehicleType.setAdapter(new UserHomeVehicleTypeAdapter(this, this));
    }

    private void loadVehicleType() {
        this.disposable = UserApi.homeVehicleType(this);
    }
}