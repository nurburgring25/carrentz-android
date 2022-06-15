package dev.burikk.carrentz.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.burikk.carrentz.R;
import dev.burikk.carrentz.adapter.MerchantHomeMenuAdapter;
import dev.burikk.carrentz.enumeration.MerchantHomeMenuItem;
import dev.burikk.carrentz.enumeration.SharedPreferenceKey;
import dev.burikk.carrentz.helper.Generals;
import dev.burikk.carrentz.helper.Preferences;

@SuppressLint("NonConstantResourceId")
public class MerchantHomeActivity extends AppCompatActivity implements MerchantHomeMenuAdapter.MerchantHomeMenuSelectedListener {
    @BindView(R.id.txvName)
    public TextView txvName;
    @BindView(R.id.rcvMenu)
    public RecyclerView rcvMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_merchant_home);

        ButterKnife.bind(this);

        this.widget();
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
        }
    }

    private void widget() {
        this.txvName.setText(Preferences.get(SharedPreferenceKey.NAME, String.class));

        this.rcvMenu.setItemAnimator(new DefaultItemAnimator());
        this.rcvMenu.setLayoutManager(new GridLayoutManager(this, 4));
        this.rcvMenu.setAdapter(new MerchantHomeMenuAdapter(this));
    }
}