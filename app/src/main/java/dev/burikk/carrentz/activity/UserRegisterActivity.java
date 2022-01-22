package dev.burikk.carrentz.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dev.burikk.carrentz.R;
import dev.burikk.carrentz.adapter.SpinnerAdapter;
import dev.burikk.carrentz.api.user.UserApi;
import dev.burikk.carrentz.api.user.endpoint.account.request.RegisterRequest;
import dev.burikk.carrentz.bottomsheet.BottomSheets;
import dev.burikk.carrentz.bottomsheet.MessageBottomSheet;
import dev.burikk.carrentz.bottomsheet.SpinnerBottomSheet;
import dev.burikk.carrentz.common.DialCode;
import dev.burikk.carrentz.helper.Strings;
import dev.burikk.carrentz.helper.Validators;
import dev.burikk.carrentz.protocol.MainProtocol;
import io.reactivex.disposables.Disposable;

/**
 * @author Muhammad Irfan
 * @since 18/01/2022 11.05
 */
@SuppressLint("NonConstantResourceId")
public class UserRegisterActivity extends AppCompatActivity implements MainProtocol<Void>, SpinnerBottomSheet.SpinnerBottomSheetCallback {
    @BindView(R.id.coordinatorLayout)
    public CoordinatorLayout coordinatorLayout;
    @BindView(R.id.progressBar)
    public ProgressBar progressBar;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.edtName)
    public TextInputEditText edtName;
    @BindView(R.id.actvDialCode)
    public AutoCompleteTextView actvDialCode;
    @BindView(R.id.edtPhoneNumber)
    public TextInputEditText edtPhoneNumber;
    @BindView(R.id.edtEmail)
    public TextInputEditText edtEmail;
    @BindView(R.id.edtPassword)
    public TextInputEditText edtPassword;
    @BindView(R.id.edtRetypePassword)
    public TextInputEditText edtRetypePassword;

    public Disposable disposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_user_register);

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
    public ProgressBar getProgressBar() {
        return this.progressBar;
    }

    @Override
    public AppCompatActivity getAppCompatActivity() {
        return this;
    }

    @Override
    public void result(Void data) {
        BottomSheets.message(this, "Pendaftaran pengguna baru berhasil, sekarang kamu bisa langsung mencoba aplikasi Carrentz yah.", new MessageBottomSheet.MessageBottomSheetCallback() {
            @Override
            public void dismiss() {
                UserRegisterActivity.this.finish();
            }

            @Override
            public void cancel() {
                UserRegisterActivity.this.finish();
            }
        });
    }

    @Override
    public void select(SpinnerAdapter.Item selectedItem) {
        int tag = (int) selectedItem.getTag();

        if (tag == R.id.actvDialCode) {
            this.actvDialCode.setText(selectedItem.getSelectedDescription());
        }
    }

    @OnClick(R.id.actvDialCode)
    public void dialCode() {
        ArrayList<SpinnerAdapter.Item> items = new ArrayList<>();

        for (DialCode dialCode : DialCode.DIAL_CODES) {
            SpinnerAdapter.Item item = new SpinnerAdapter.Item();

            item.setIdentifier(dialCode);
            item.setDescription(dialCode.getCountryCode() + " - " + dialCode.getCountryName() + " (" + dialCode.getDialCode() + ")");
            item.setSelectedDescription(dialCode.getDialCode());
            item.setTag(R.id.actvDialCode);

            items.add(item);
        }

        BottomSheets.spinner(this, "Kode Telepon", items, this);
    }

    @OnClick(R.id.btnRegister)
    public void register() {
        if (this.valid()) {
            RegisterRequest registerRequest = new RegisterRequest();

            registerRequest.setName(Strings.value(this.edtName.getText()));
            registerRequest.setPhoneNumber(Strings.value(this.actvDialCode.getText()) + Strings.value(this.edtPhoneNumber.getText()));
            registerRequest.setEmail(Strings.value(this.edtEmail.getText()));
            registerRequest.setPassword(Strings.value(this.edtPassword.getText()));

            this.disposable = UserApi.register(this, registerRequest);
        }
    }

    private void toolbar() {
        this.setSupportActionBar(this.toolbar);

        if (this.getSupportActionBar() != null) {
            this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            this.getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    @SuppressLint("SetTextI18n")
    private void widget() {
        this.actvDialCode.setText("+62");

        ((TextInputLayout) this.actvDialCode.getParent().getParent()).setEndIconOnClickListener(view -> this.dialCode());
    }

    private boolean valid() {
        List<Boolean> booleans = Arrays.asList(
                Validators.mandatory(this, this.edtName, "Nama"),
                Validators.mandatory(this, this.actvDialCode, "Kode telepon"),
                Validators.mandatory(this, this.edtPhoneNumber, "Nomor ponsel"),
                Validators.mandatory(this, this.edtEmail, "Email"),
                Validators.mandatory(this, this.edtPassword, "Kata sandi"),
                Validators.maxLength(this.edtName, 128),
                Validators.maxLength(this.edtEmail, 64),
                Validators.custom(!StringUtils.equals(Strings.value(this.edtPassword.getText()), Strings.value(this.edtRetypePassword.getText())), this.edtRetypePassword, "Ketik ulang kata sandi tidak sesuai.")
        );

        return !booleans.contains(false);
    }
}