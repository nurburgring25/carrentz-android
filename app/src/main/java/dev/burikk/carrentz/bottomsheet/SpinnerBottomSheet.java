package dev.burikk.carrentz.bottomsheet;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dev.burikk.carrentz.R;
import dev.burikk.carrentz.adapter.SpinnerAdapter;
import dev.burikk.carrentz.helper.Keyboards;
import dev.burikk.carrentz.helper.Strings;
import dev.burikk.carrentz.helper.Views;

/**
 * @author Muhammad Irfan
 * @since 19/02/2019 22.45
 */
@SuppressLint("NonConstantResourceId")
public class SpinnerBottomSheet extends BottomSheetDialogFragment {
    @BindView(R.id.linearLayout)
    public LinearLayout linearLayout;
    @BindView(R.id.txvTitle)
    public TextView txvTitle;
    @BindView(R.id.edtSearch)
    public TextInputEditText edtSearch;
    @BindView(R.id.recyclerView)
    public RecyclerView recyclerView;
    @BindView(R.id.emptyView)
    public LinearLayout emptyView;

    public AppCompatActivity appCompatActivity;
    public Unbinder unbinder;
    public SpinnerBottomSheetCallback spinnerBottomSheetCallback;
    public CountDownTimer countDownTimer;
    public String searchTerm;
    public ArrayList<SpinnerAdapter.Item> allItems;

    {
        this.allItems = new ArrayList<>();
    }

    public static SpinnerBottomSheet newInstance(
            CharSequence title,
            ArrayList<SpinnerAdapter.Item> items
    ) {
        SpinnerBottomSheet spinnerBottomSheet = new SpinnerBottomSheet();

        Bundle bundle = new Bundle();

        bundle.putCharSequence("TITLE", title);
        bundle.putSerializable("ITEMS", items);

        spinnerBottomSheet.setArguments(bundle);

        return spinnerBottomSheet;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setOnShowListener(dialogInterface -> {
            BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialogInterface;
            setupFullHeight(bottomSheetDialog);
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
        View view = inflater.inflate(R.layout.bottom_sheet_spinner, container, false);

        this.unbinder = ButterKnife.bind(this, view);

        this.init();
        this.extract();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (this.spinnerBottomSheetCallback == null) {
            this.dismiss();
        } else {
            BottomSheets.initialize(this, view);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (this.countDownTimer != null) {
            this.countDownTimer.cancel();
        }

        if (this.unbinder != null) {
            this.unbinder.unbind();
        }
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);

        if (this.spinnerBottomSheetCallback != null) {
            this.spinnerBottomSheetCallback.onDismiss();
        }
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);

        if (this.spinnerBottomSheetCallback != null) {
            this.spinnerBottomSheetCallback.onDismiss();
        }
    }

    public void callback(SpinnerBottomSheetCallback spinnerBottomSheetCallback) {
        this.spinnerBottomSheetCallback = spinnerBottomSheetCallback;
    }

    @SuppressWarnings("unchecked")
    private void extract() {
        if (this.getArguments() != null) {
            CharSequence title = this.getArguments().getCharSequence("TITLE");

            if (title != null) {
                this.txvTitle.setText(title);

                Views.visible(this.txvTitle);
            }

            ArrayList<SpinnerAdapter.Item> items = (ArrayList<SpinnerAdapter.Item>) this.getArguments().getSerializable("ITEMS");

            if (items != null) {
                this.allItems.addAll(items);
                this.reload();
            }
        }
    }

    private void init() {
        new Keyboards(this.appCompatActivity, this.linearLayout);

        this.recyclerView.setItemAnimator(new DefaultItemAnimator());
        this.recyclerView.addItemDecoration(new DividerItemDecoration(this.appCompatActivity, RecyclerView.VERTICAL));
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this.appCompatActivity, RecyclerView.VERTICAL, false));
        this.recyclerView.setAdapter(new SpinnerAdapter(this));

        this.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (SpinnerBottomSheet.this.countDownTimer != null) {
                    SpinnerBottomSheet.this.countDownTimer.cancel();
                }

                SpinnerBottomSheet.this.countDownTimer = new CountDownTimer(500, 500) {
                    @Override
                    public void onTick(long millisUntilFinished) {}

                    @Override
                    public void onFinish() {
                        SpinnerBottomSheet.this.searchTerm = charSequence.toString();
                        SpinnerBottomSheet.this.reload();
                    }
                };

                SpinnerBottomSheet.this.countDownTimer.start();
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    private void reload() {
        if (this.recyclerView != null) {
            SpinnerAdapter spinnerAdapter = (SpinnerAdapter) this.recyclerView.getAdapter();

            if (spinnerAdapter != null) {
                spinnerAdapter.getItems().clear();

                for (SpinnerAdapter.Item item : this.allItems) {
                    if (StringUtils.containsIgnoreCase(item.getDescription(), Strings.blankIfNull(this.searchTerm))) {
                        spinnerAdapter.getItems().add(item);
                    }
                }

                if (spinnerAdapter.getItems().isEmpty()) {
                    this.empty();
                } else {
                    this.notEmpty();
                }

                spinnerAdapter.notifyDataSetChanged();
            }
        }
    }

    private void empty() {
        Views.gone(this.recyclerView);
        Views.visible(this.emptyView);
    }

    private void notEmpty() {
        Views.visible(this.recyclerView);
        Views.gone(this.emptyView);
    }

    private void setupFullHeight(BottomSheetDialog bottomSheetDialog) {
        FrameLayout bottomSheet = (FrameLayout) bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();

        int windowHeight = getWindowHeight();
        if (layoutParams != null) {
            layoutParams.height = windowHeight;
        }
        bottomSheet.setLayoutParams(layoutParams);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private int getWindowHeight() {
        // Calculate window height for fullscreen use
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    public interface SpinnerBottomSheetCallback {
        default void onDismiss() {}

        void select(SpinnerAdapter.Item selectedItem);
    }
}