package dev.burikk.carrentz.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.burikk.carrentz.R;
import dev.burikk.carrentz.adapter.MerchantRegisterStepperAdapter;
import dev.burikk.carrentz.api.merchant.MerchantApi;
import dev.burikk.carrentz.api.merchant.endpoint.account.request.RegisterRequest;
import dev.burikk.carrentz.bottomsheet.BottomSheets;
import dev.burikk.carrentz.bottomsheet.MessageBottomSheet;
import dev.burikk.carrentz.fragment.MerchantRegisterAccountFragment;
import dev.burikk.carrentz.fragment.MerchantRegisterBusinessFragment;
import dev.burikk.carrentz.fragment.MerchantRegisterOwnerFragment;
import dev.burikk.carrentz.helper.Strings;
import dev.burikk.carrentz.protocol.MainProtocol;
import io.reactivex.disposables.Disposable;

/**
 * @author Muhammad Irfan
 * @since 18/01/2022 11.05
 */
@SuppressLint("NonConstantResourceId")
public class MerchantRegisterActivity extends AppCompatActivity implements StepperLayout.StepperListener, MainProtocol<Void> {
    @BindView(R.id.coordinatorLayout)
    public CoordinatorLayout coordinatorLayout;
    @BindView(R.id.progressBar)
    public ProgressBar progressBar;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.stepperLayout)
    public StepperLayout stepperLayout;

    public Disposable disposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_merchant_register);

        ButterKnife.bind(this);

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
    public void onCompleted(View completeButton) {
        MerchantRegisterStepperAdapter merchantRegisterStepperAdapter = (MerchantRegisterStepperAdapter) this.stepperLayout.getAdapter();

        if (merchantRegisterStepperAdapter != null) {
            RegisterRequest registerRequest = new RegisterRequest();

            // Dapatkan isian dari tab usaha
            MerchantRegisterBusinessFragment merchantRegisterBusinessFragment = (MerchantRegisterBusinessFragment) merchantRegisterStepperAdapter.createStep(0);

            registerRequest.setBusinessName(Strings.value(merchantRegisterBusinessFragment.edtBusinessName.getText()));
            registerRequest.setPhoneNumber(Strings.value(merchantRegisterBusinessFragment.actvDialCode.getText()) + Strings.value(merchantRegisterBusinessFragment.edtPhoneNumber.getText()));
            registerRequest.setAddress(Strings.value(merchantRegisterBusinessFragment.edtBusinessAddress.getText()));
            registerRequest.setCity(Strings.value(merchantRegisterBusinessFragment.actvCity.getText()));

            // Dapatkan isian dari tab pemilik
            MerchantRegisterOwnerFragment merchantRegisterOwnerFragment = (MerchantRegisterOwnerFragment) merchantRegisterStepperAdapter.createStep(1);

            registerRequest.setName(Strings.value(merchantRegisterOwnerFragment.edtName.getText()));

            // Dapatkan isian dari tab akun
            MerchantRegisterAccountFragment merchantRegisterAccountFragment = (MerchantRegisterAccountFragment) merchantRegisterStepperAdapter.createStep(2);

            registerRequest.setEmail(Strings.value(merchantRegisterAccountFragment.edtEmail.getText()));
            registerRequest.setPassword(Strings.value(merchantRegisterAccountFragment.edtPassword.getText()));

            this.disposable = MerchantApi.register(this, registerRequest);
        }
    }

    @Override
    public void onError(VerificationError verificationError) {}

    @Override
    public void onStepSelected(int newStepPosition) {
        System.err.println("test");
    }

    @Override
    public void onReturn() {}

    @Override
    public ProgressBar getProgressBar() {
        return this.progressBar;
    }

    @Override
    public AppCompatActivity getAppCompatActivity() {
        return this;
    }

    @Override
    public void result(Void data) {
        BottomSheets.message(this, "Pendaftaran merchant berhasil, sekarang kamu bisa langsung mencoba aplikasi Carrentz yah.", new MessageBottomSheet.MessageBottomSheetCallback() {
            @Override
            public void dismiss() {
                MerchantRegisterActivity.this.finish();
            }

            @Override
            public void cancel() {
                MerchantRegisterActivity.this.finish();
            }
        });
    }

    private void toolbar() {
        this.setSupportActionBar(this.toolbar);

        if (this.getSupportActionBar() != null) {
            this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            this.getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    private void widget() {
        this.stepperLayout.setOffscreenPageLimit(3);
        this.stepperLayout.setAdapter(new MerchantRegisterStepperAdapter(this.getSupportFragmentManager(), this));
        this.stepperLayout.setListener(this);
    }
}