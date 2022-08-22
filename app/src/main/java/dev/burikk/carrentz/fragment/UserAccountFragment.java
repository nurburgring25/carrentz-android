package dev.burikk.carrentz.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dev.burikk.carrentz.R;
import dev.burikk.carrentz.activity.UserHomeActivity;
import dev.burikk.carrentz.activity.WelcomeActivity;
import dev.burikk.carrentz.bottomsheet.BottomSheets;
import dev.burikk.carrentz.enumeration.SharedPreferenceKey;
import dev.burikk.carrentz.helper.Generals;
import dev.burikk.carrentz.helper.Preferences;

/**
 * @author Muhammad Irfan
 * @since 18/01/2022 12.13
 */
@SuppressLint("NonConstantResourceId")
public class UserAccountFragment extends Fragment {
    @BindView(R.id.txvName)
    public TextView txvName;
    @BindView(R.id.txvEmail)
    public TextView txvEmail;

    public Unbinder unbinder;
    public UserHomeActivity userHomeActivity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        this.userHomeActivity = (UserHomeActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_account, container, false);

        this.unbinder = ButterKnife.bind(this, view);

        this.widget();

        return view;
    }

    @OnClick(R.id.llChangePassword)
    public void changePassword() {
        BottomSheets.changePassword(this.userHomeActivity);
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

        Generals.move(this.userHomeActivity, WelcomeActivity.class, true);
    }

    private void widget() {
        this.txvName.setText(Preferences.get(SharedPreferenceKey.NAME, String.class));
        this.txvEmail.setText(Preferences.get(SharedPreferenceKey.EMAIL, String.class));
    }
}