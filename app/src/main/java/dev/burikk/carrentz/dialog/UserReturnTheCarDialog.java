package dev.burikk.carrentz.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dev.burikk.carrentz.R;
import dev.burikk.carrentz.activity.UserHomeActivity;
import dev.burikk.carrentz.api.user.UserApi;
import dev.burikk.carrentz.api.user.endpoint.rent.item.UserRentItem;
import dev.burikk.carrentz.bottomsheet.BottomSheets;
import dev.burikk.carrentz.helper.Strings;
import dev.burikk.carrentz.helper.Validators;
import dev.burikk.carrentz.protocol.MainProtocol;
import io.reactivex.disposables.Disposable;

@SuppressLint("NonConstantResourceId")
public class UserReturnTheCarDialog extends AppCompatDialogFragment implements MainProtocol<Void> {
    @BindView(R.id.linearLayout)
    public LinearLayout linearLayout;
    @BindView(R.id.progressBar)
    public ProgressBar progressBar;
    @BindView(R.id.edtCode)
    public TextInputEditText edtCode;

    public UserHomeActivity userHomeActivity;
    public UserRentItem userRentItem;
    public Unbinder unbinder;
    public Disposable disposable;
    public UserReturnTheCarDialog.UserReturnTheCarDialogCallback userReturnTheCarDialogCallback;

    public static UserReturnTheCarDialog newInstance(UserRentItem userRentItem) {
        UserReturnTheCarDialog userReturnTheCarDialog = new UserReturnTheCarDialog();

        Bundle bundle = new Bundle();

        bundle.putSerializable("USER_RENT_ITEM", userRentItem);

        userReturnTheCarDialog.setArguments(bundle);

        return userReturnTheCarDialog;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        this.userHomeActivity = (UserHomeActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_user_return_the_car, container, false);

        this.unbinder = ButterKnife.bind(this, view);

        this.extract();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (this.userReturnTheCarDialogCallback == null) {
            this.dismiss();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (this.disposable != null) {
            this.disposable.dispose();
        }

        if (this.unbinder != null) {
            this.unbinder.unbind();
        }
    }

    @Override
    public ProgressBar getProgressBar() {
        return this.progressBar;
    }

    @Override
    public AppCompatActivity getAppCompatActivity() {
        return this.userHomeActivity;
    }

    @Override
    public void result(Void data) {
        BottomSheets.message(
                this.userHomeActivity,
                "Status dokumen telah berhasil diperbarui"
        );

        if (this.userReturnTheCarDialogCallback != null) {
            this.userReturnTheCarDialogCallback.success();
        }

        this.close();
    }

    @OnClick(R.id.btnClose)
    public void close() {
        this.dismiss();
    }

    @OnClick(R.id.btnVerification)
    public void verification() {
        if (this.valid()) {
            this.disposable = UserApi.rentReturnTheCar(
                    this,
                    this.userRentItem.getId(),
                    Strings.value(this.edtCode.getText())
            );
        }
    }

    public void callback(UserReturnTheCarDialog.UserReturnTheCarDialogCallback userReturnTheCarDialogCallback) {
        this.userReturnTheCarDialogCallback = userReturnTheCarDialogCallback;
    }

    private void extract() {
        if (this.getArguments() != null) {
            this.userRentItem = (UserRentItem) this.getArguments().getSerializable("USER_RENT_ITEM");
        }
    }

    private boolean valid() {
        List<Boolean> booleans = Collections.singletonList(
                Validators.mandatory(this.userHomeActivity, this.edtCode, "Kode pengembalian")
        );

        return !booleans.contains(false);
    }

    public interface UserReturnTheCarDialogCallback {
        void success();
    }
}