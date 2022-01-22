package dev.burikk.carrentz.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.burikk.carrentz.R;
import dev.burikk.carrentz.adapter.WelcomePagerAdapter;

/**
 * @author Muhammad Irfan
 * @since 18/01/2022 11.05
 */
@SuppressLint("NonConstantResourceId")
public class WelcomeActivity extends AppCompatActivity {
    @BindView(R.id.coordinatorLayout)
    public CoordinatorLayout coordinatorLayout;
    @BindView(R.id.tabLayout)
    public TabLayout tabLayout;
    @BindView(R.id.progressBar)
    public ProgressBar progressBar;
    @BindView(R.id.viewPager)
    public ViewPager2 viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_welcome);

        ButterKnife.bind(this);

        this.widget();
    }

    private void widget() {
        this.viewPager.setOffscreenPageLimit(2);
        this.viewPager.setAdapter(new WelcomePagerAdapter(this));

        new TabLayoutMediator(this.tabLayout, this.viewPager, (tab, position) -> {
            if (position == 0) {
                tab.setText("MERCHANT");
            } else if (position == 1) {
                tab.setText("USER");
            }
        }).attach();
    }
}