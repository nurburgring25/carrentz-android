package dev.burikk.carrentz.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dev.burikk.carrentz.R;
import dev.burikk.carrentz.activity.WelcomeActivity;

/**
 * @author Muhammad Irfan
 * @since 18/01/2022 12.13
 */
@SuppressLint("NonConstantResourceId")
public class UserWelcomeFragment extends Fragment {
    @BindView(R.id.edtUsername)
    public TextInputEditText edtUsername;

    public WelcomeActivity welcomeActivity;
    public Unbinder unbinder;

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

    @OnClick(R.id.btnSignIn)
    public void signIn() {

    }

    @OnClick(R.id.btnRegister)
    public void register() {

    }

    @OnClick(R.id.btnFacebook)
    public void facebook() {

    }

    @OnClick(R.id.btnGoogle)
    public void google() {

    }
}
