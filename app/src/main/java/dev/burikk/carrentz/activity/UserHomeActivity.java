package dev.burikk.carrentz.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.burikk.carrentz.R;
import dev.burikk.carrentz.fragment.UserHomeFragment;
import dev.burikk.carrentz.fragment.UserAccountFragment;

@SuppressLint("NonConstantResourceId")
public class UserHomeActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {
    public static final transient String ACTIVE_FRAGMENT_TAG = "ACTIVE_FRAGMENT_TAG";

    @BindView(R.id.frameLayout)
    public FrameLayout frameLayout;
    @BindView(R.id.bottomNavigationView)
    public BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_user_home);

        ButterKnife.bind(this);

        this.widget();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        if (item.getItemId() == R.id.mniHome) {
            fragment = new UserHomeFragment();
        } else if (item.getItemId() == R.id.mniHistory) {

        } else if (item.getItemId() == R.id.mniAccount) {
            fragment = new UserAccountFragment();
        }

        if (fragment != null) {
            FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();

            fragmentTransaction.replace(R.id.frameLayout, fragment, ACTIVE_FRAGMENT_TAG);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

            return true;
        } else {
            return false;
        }
    }

    private void widget() {
        this.bottomNavigationView.setOnItemSelectedListener(this);
        this.bottomNavigationView.setSelectedItemId(R.id.mniHome);
    }
}