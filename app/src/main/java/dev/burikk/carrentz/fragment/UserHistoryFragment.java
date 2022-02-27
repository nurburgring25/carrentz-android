package dev.burikk.carrentz.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.textfield.TextInputEditText;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dev.burikk.carrentz.R;
import dev.burikk.carrentz.activity.UserHomeActivity;
import dev.burikk.carrentz.adapter.UserHistoryAdapter;
import dev.burikk.carrentz.api.user.UserApi;
import dev.burikk.carrentz.api.user.endpoint.rent.item.UserRentItem;
import dev.burikk.carrentz.api.user.endpoint.rent.response.UserRentListResponse;
import dev.burikk.carrentz.helper.Views;
import dev.burikk.carrentz.protocol.MainProtocol;
import io.reactivex.disposables.Disposable;

/**
 * @author Muhammad Irfan
 * @since 18/01/2022 12.13
 */
@SuppressLint("NonConstantResourceId")
public class UserHistoryFragment extends Fragment implements MainProtocol<UserRentListResponse>, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.edtSearch)
    public TextInputEditText edtSearch;
    @BindView(R.id.swipeRefreshLayout)
    public SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;
    @BindView(R.id.emptyView)
    public View emptyView;

    public Unbinder unbinder;
    public Disposable disposable;
    public CountDownTimer countDownTimer;
    public String searchTerm;
    public List<UserRentItem> userRentItems;
    public UserHomeActivity userHomeActivity;

    {
        this.userRentItems = new ArrayList<>();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        this.userHomeActivity = (UserHomeActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_history, container, false);

        this.unbinder = ButterKnife.bind(this, view);

        this.widget();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        this.onRefresh();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (this.disposable != null) {
            this.disposable.dispose();
        }
    }

    @Override
    public AppCompatActivity getAppCompatActivity() {
        return this.userHomeActivity;
    }

    @Override
    public ProgressBar getProgressBar() {
        return null;
    }

    @Override
    public void begin() {
        this.swipeRefreshLayout.setRefreshing(true);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void result(UserRentListResponse data) {
        this.userRentItems.clear();
        this.userRentItems.addAll(data.getDetails());
        this.repopulate();
    }

    @Override
    public void end() {
        this.swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        this.disposable = UserApi.rentGet(this);
    }

    private void widget() {
        this.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (UserHistoryFragment.this.countDownTimer != null) {
                    UserHistoryFragment.this.countDownTimer.cancel();
                }

                UserHistoryFragment.this.countDownTimer = new CountDownTimer(500, 500) {
                    @Override
                    public void onTick(long millisUntilFinished) {}

                    @Override
                    public void onFinish() {
                        UserHistoryFragment.this.searchTerm = charSequence.toString();
                        UserHistoryFragment.this.repopulate();
                    }
                };

                UserHistoryFragment.this.countDownTimer.start();
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        this.swipeRefreshLayout.setOnRefreshListener(this);

        this.recyclerView.setItemAnimator(new DefaultItemAnimator());
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this.userHomeActivity, RecyclerView.VERTICAL, false));
        this.recyclerView.setAdapter(new UserHistoryAdapter(this.userHomeActivity));
    }

    @SuppressLint("NotifyDataSetChanged")
    public void repopulate() {
        UserHistoryAdapter userHistoryAdapter = (UserHistoryAdapter) this.recyclerView.getAdapter();

        if (userHistoryAdapter != null) {
            userHistoryAdapter.getUserRentItems().clear();

            for (UserRentItem userRentItem : this.userRentItems) {
                String searchKey = userRentItem.getNumber()
                        + userRentItem.getUser().getId()
                        + userRentItem.getUser().getName()
                        + userRentItem.getUser().getPhoneNumber()
                        + userRentItem.getVehicle().getName()
                        + userRentItem.getVehicle().getLicenseNumber()
                        + userRentItem.getVehicle().getVehicleType();

                if (StringUtils.isBlank(this.searchTerm) || StringUtils.containsIgnoreCase(searchKey, this.searchTerm)) {
                    userHistoryAdapter.getUserRentItems().add(userRentItem);
                }
            }

            userHistoryAdapter.notifyDataSetChanged();

            if (!userHistoryAdapter.getUserRentItems().isEmpty()) {
                this.notEmpty();
            } else {
                this.empty();
            }
        }
    }

    private void notEmpty() {
        Views.visible(this.recyclerView);
        Views.gone(this.emptyView);
    }

    private void empty() {
        Views.gone(this.recyclerView);
        Views.visible(this.emptyView);
    }
}