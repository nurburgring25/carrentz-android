package dev.burikk.carrentz.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ViewListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dev.burikk.carrentz.R;
import dev.burikk.carrentz.activity.UserHomeActivity;
import dev.burikk.carrentz.activity.UserStoreListActivity;
import dev.burikk.carrentz.adapter.UserHomeVehicleTypeAdapter;
import dev.burikk.carrentz.api.user.UserApi;
import dev.burikk.carrentz.api.user.endpoint.home.item.HomeVehicleTypeItem;
import dev.burikk.carrentz.api.user.endpoint.home.response.HomeVehicleTypeResponse;
import dev.burikk.carrentz.enumeration.SharedPreferenceKey;
import dev.burikk.carrentz.helper.Generals;
import dev.burikk.carrentz.helper.Preferences;
import dev.burikk.carrentz.protocol.MainProtocol;
import io.reactivex.disposables.Disposable;

/**
 * @author Muhammad Irfan
 * @since 18/01/2022 12.13
 */
@SuppressLint("NonConstantResourceId")
public class UserHomeFragment extends Fragment implements MainProtocol<Object>, ViewListener, UserHomeVehicleTypeAdapter.UserHomeVehicleTypeSelectedListener {
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

    public UserHomeActivity userHomeActivity;
    public Unbinder unbinder;
    public Disposable disposable;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        this.userHomeActivity = (UserHomeActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_home, container, false);

        this.unbinder = ButterKnife.bind(this, view);

        this.widget();
        this.loadVehicleType();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (this.unbinder != null) {
            this.unbinder.unbind();
        }
    }

    @Override
    public ProgressBar getProgressBar() {
        return null;
    }

    @Override
    public AppCompatActivity getAppCompatActivity() {
        return this.userHomeActivity;
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
        Generals.move(this.userHomeActivity, UserStoreListActivity.class, false);
    }

    private void widget() {
        this.txvName.setText(Preferences.get(SharedPreferenceKey.NAME, String.class));

        this.carouselView.setPageCount(PROMO_IMAGES.length);
        this.carouselView.setViewListener(this);

        this.rcvVehicleType.setItemAnimator(new DefaultItemAnimator());
        this.rcvVehicleType.setLayoutManager(new LinearLayoutManager(this.userHomeActivity, RecyclerView.HORIZONTAL, false));
        this.rcvVehicleType.setAdapter(new UserHomeVehicleTypeAdapter(this.userHomeActivity, this));
    }

    private void loadVehicleType() {
        this.disposable = UserApi.homeVehicleType(this);
    }
}