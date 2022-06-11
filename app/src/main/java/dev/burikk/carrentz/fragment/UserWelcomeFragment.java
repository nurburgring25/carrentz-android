package dev.burikk.carrentz.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dev.burikk.carrentz.R;
import dev.burikk.carrentz.activity.UserRegisterActivity;
import dev.burikk.carrentz.activity.UserVerificationActivity;
import dev.burikk.carrentz.activity.WelcomeActivity;
import dev.burikk.carrentz.api.user.UserApi;
import dev.burikk.carrentz.api.user.endpoint.account.request.SignInRequest;
import dev.burikk.carrentz.api.user.endpoint.account.response.SignInResponse;
import dev.burikk.carrentz.helper.Generals;
import dev.burikk.carrentz.helper.Strings;
import dev.burikk.carrentz.protocol.MainProtocol;
import io.reactivex.disposables.Disposable;

/**
 * @author Muhammad Irfan
 * @since 18/01/2022 12.13
 */
@SuppressLint("NonConstantResourceId")
public class UserWelcomeFragment extends Fragment implements MainProtocol<SignInResponse> {
    @BindView(R.id.edtEmail)
    public TextInputEditText edtEmail;

    public WelcomeActivity welcomeActivity;
    public Unbinder unbinder;
    public Disposable disposable;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        this.welcomeActivity = (WelcomeActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_welcome, container, false);

        this.unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (this.unbinder != null) {
            this.unbinder.unbind();
        }
    }

    @Override
    public ProgressBar getProgressBar() {
        return this.welcomeActivity.progressBar;
    }

    @Override
    public AppCompatActivity getAppCompatActivity() {
        return this.welcomeActivity;
    }

    @Override
    public void result(SignInResponse data) {
        Bundle bundle = new Bundle();

        bundle.putSerializable("SIGN_IN_RESPONSE", data);

        Generals.move(this.welcomeActivity, UserVerificationActivity.class, bundle, false);
    }

    @OnClick(R.id.btnSignIn)
    public void signIn() {
        SignInRequest signInRequest = new SignInRequest();

        signInRequest.setEmail(Strings.value(this.edtEmail.getText()));

        this.disposable = UserApi.signIn(this, signInRequest);
    }

    @OnClick(R.id.btnRegister)
    public void register() {
        Generals.move(this.welcomeActivity, UserRegisterActivity.class, false);
    }
}