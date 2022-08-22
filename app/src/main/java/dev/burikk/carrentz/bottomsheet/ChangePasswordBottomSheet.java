package dev.burikk.carrentz.bottomsheet;

import android.annotation.SuppressLint;
import android.app.Dialog;
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

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dev.burikk.carrentz.R;
import dev.burikk.carrentz.helper.Keyboards;
import dev.burikk.carrentz.protocol.MainProtocol;
import io.reactivex.disposables.Disposable;

/**
 * @author Muhammad Irfan
 * @since 19/02/2019 22.45
 */
@SuppressLint("NonConstantResourceId")
public class ChangePasswordBottomSheet extends BottomSheetDialogFragment implements MainProtocol<Void> {
    @BindView(R.id.linearLayout)
    public LinearLayout linearLayout;
    @BindView(R.id.progressBar)
    public ProgressBar progressBar;
    @BindView(R.id.edtOldPassword)
    public TextInputEditText edtOldPassword;
    @BindView(R.id.edtNewPassword)
    public TextInputEditText edtNewPassword;
    @BindView(R.id.edtConfirmNewPassword)
    public TextInputEditText edtConfirmNewPassword;

    public AppCompatActivity appCompatActivity;
    public Unbinder unbinder;
    public Disposable disposable;

    public static ChangePasswordBottomSheet newInstance() {
        return new ChangePasswordBottomSheet();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        dialog.setOnShowListener(dialogInterface -> {
            BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialogInterface;

            BottomSheets.fullHeight(bottomSheetDialog);
        });

        return  dialog;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        this.appCompatActivity = (AppCompatActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_change_password, container, false);

        this.unbinder = ButterKnife.bind(this, view);

        this.init();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        BottomSheets.initialize(this, view);
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
        return this.appCompatActivity;
    }

    @Override
    public void result(Void data) {
        BottomSheets.message(
                this.appCompatActivity,
                "Kata sandi berhasil terupdate."
        );

        this.close();
    }

    @OnClick(R.id.btnClose)
    public void close() {
        this.dismiss();
    }

    @OnClick(R.id.btnSave)
    public void save() {

    }

    @SuppressLint("SetTextI18n")
    private void init() {
        new Keyboards(this.appCompatActivity, this.linearLayout);
    }
}