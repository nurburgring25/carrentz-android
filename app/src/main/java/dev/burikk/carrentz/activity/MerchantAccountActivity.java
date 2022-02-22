package dev.burikk.carrentz.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dev.burikk.carrentz.R;
import dev.burikk.carrentz.enumeration.SharedPreferenceKey;
import dev.burikk.carrentz.helper.Generals;
import dev.burikk.carrentz.helper.Preferences;

@SuppressLint("NonConstantResourceId")
public class MerchantAccountActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.imageView)
    public ImageView imageView;
    @BindView(R.id.txvMerchantName)
    public TextView txvMerchantName;
    @BindView(R.id.txvName)
    public TextView txvName;
    @BindView(R.id.txvEmail)
    public TextView txvEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_merchant_account);

        ButterKnife.bind(this);

        this.toolbar();
        this.widget();
    }

    @OnClick(R.id.llContactUs)
    public void contactUs() {

    }

    @OnClick(R.id.llSetting)
    public void setting() {

    }

    @OnClick(R.id.llSignOut)
    public void signOut() {
        try (Preferences preferences = Preferences.getInstance()) {
            preferences.begin();
            preferences.remove(SharedPreferenceKey.SESSION_ID);
            preferences.remove(SharedPreferenceKey.USER_TYPE);
            preferences.remove(SharedPreferenceKey.NAME);
            preferences.remove(SharedPreferenceKey.EMAIL);
            preferences.remove(SharedPreferenceKey.MERCHANT_ID);
            preferences.remove(SharedPreferenceKey.MERCHANT_NAME);
            preferences.commit();
        }

        Generals.move(this, WelcomeActivity.class, true);
    }

    private void toolbar() {
        this.setSupportActionBar(this.toolbar);

        if (this.getSupportActionBar() != null) {
            this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            this.getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    private void widget() {
        this.txvMerchantName.setText(Preferences.get(SharedPreferenceKey.MERCHANT_NAME, String.class));
        this.txvName.setText(Preferences.get(SharedPreferenceKey.NAME, String.class));
        this.txvEmail.setText(Preferences.get(SharedPreferenceKey.EMAIL, String.class));
    }
}