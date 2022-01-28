package dev.burikk.carrentz.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.synnapps.carouselview.CarouselView;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.burikk.carrentz.R;

@SuppressLint("NonConstantResourceId")
public class UserHomeActivity extends AppCompatActivity {
    @BindView(R.id.carouselView)
    public CarouselView carouselView;

    public static final int[] PROMO_IMAGES = {
            R.drawable.img_promo_1,
            R.drawable.img_promo_2,
            R.drawable.img_promo_3,
            R.drawable.img_promo_4,
            R.drawable.img_promo_5
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_user_home);

        ButterKnife.bind(this);

        this.widget();
    }

    private void widget() {

    }
}