package dev.burikk.carrentz.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.ButterKnife;
import dev.burikk.carrentz.R;

@SuppressLint("NonConstantResourceId")
public class UserHomeActivity extends AppCompatActivity {
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