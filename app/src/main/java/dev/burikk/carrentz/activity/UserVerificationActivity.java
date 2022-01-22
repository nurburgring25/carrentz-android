package dev.burikk.carrentz.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.textfield.TextInputEditText;

import org.apache.commons.lang3.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dev.burikk.carrentz.R;
import dev.burikk.carrentz.api.user.UserApi;
import dev.burikk.carrentz.api.user.endpoint.account.request.VerificationRequest;
import dev.burikk.carrentz.api.user.endpoint.account.response.SignInResponse;
import dev.burikk.carrentz.enumeration.SharedPreferenceKey;
import dev.burikk.carrentz.enumeration.UserType;
import dev.burikk.carrentz.helper.Generals;
import dev.burikk.carrentz.helper.Preferences;
import dev.burikk.carrentz.helper.Strings;
import dev.burikk.carrentz.protocol.MainProtocol;
import io.reactivex.disposables.Disposable;
import retrofit2.Response;

/**
 * @author Muhammad Irfan
 * @since 18/01/2022 11.05
 */
@SuppressLint("NonConstantResourceId")
public class UserVerificationActivity extends AppCompatActivity implements MainProtocol<Response<Void>> {
    @BindView(R.id.coordinatorLayout)
    public CoordinatorLayout coordinatorLayout;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.txvName)
    public TextView txvName;
    @BindView(R.id.txvEmail)
    public TextView txvEmail;
    @BindView(R.id.progressBar)
    public ProgressBar progressBar;
    @BindView(R.id.edtPassword)
    public TextInputEditText edtPassword;

    public Disposable disposable;
    public SignInResponse signInResponse;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_user_verification);

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
    public void result(Response<Void> data) {
        String authorizationHeader = data.headers().get("Authorization");

        if (StringUtils.isNotBlank(authorizationHeader)) {
            String jwtToken = authorizationHeader.substring("Bearer".length()).trim();

            try (Preferences preferences = Preferences.getInstance()) {
                preferences.begin();
                preferences.put(SharedPreferenceKey.SESSION_ID, jwtToken);
                preferences.put(SharedPreferenceKey.USER_TYPE, UserType.USER.name());
                preferences.put(SharedPreferenceKey.EMAIL, this.signInResponse.getEmail());
                preferences.put(SharedPreferenceKey.NAME, this.signInResponse.getName());
                preferences.commit();
            }

            Generals.move(this, UserHomeActivity.class, true);
        }
    }

    @OnClick(R.id.btnSignIn)
    public void signIn() {
        VerificationRequest verificationRequest = new VerificationRequest();

        verificationRequest.setEmail(this.signInResponse.getEmail());
        verificationRequest.setPassword(Strings.value(this.edtPassword.getText()));

        this.disposable = UserApi.verify(this, verificationRequest);
    }

    private void extract() {
        Bundle bundle = this.getIntent().getBundleExtra("BUNDLE");

        if (bundle != null) {
            this.signInResponse = (SignInResponse) bundle.getSerializable("SIGN_IN_RESPONSE");
        }
    }

    private void toolbar() {
        this.setSupportActionBar(this.toolbar);

        if (this.getSupportActionBar() != null) {
            this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            this.getSupportActionBar().setHomeButtonEnabled(true);
            this.getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    private void widget() {
        this.txvName.setText(this.signInResponse.getName());
        this.txvEmail.setText(this.signInResponse.getEmail());
    }
}