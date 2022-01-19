package dev.burikk.carrentz.bottomsheet;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.burikk.carrentz.R;
import dev.burikk.carrentz.helper.Views;

/**
 * @author Muhammad Irfan
 * @since 19/02/2019 22.45
 */
@SuppressLint("NonConstantResourceId")
public class MessageBottomSheet extends BottomSheetDialogFragment implements View.OnClickListener {
    @BindView(R.id.txvTitle)
    public TextView txvTitle;
    @BindView(R.id.txvMessage)
    public TextView txvMessage;
    @BindView(R.id.btnDismiss)
    public MaterialButton btnDismiss;

    private boolean hasCallback;
    private MessageBottomSheetCallback messageBottomSheetCallback;

    public static MessageBottomSheet newInstance(
            CharSequence title,
            CharSequence message,
            CharSequence dismiss,
            boolean showButton,
            boolean hasCallback
    ) {
        MessageBottomSheet messageBottomSheet = new MessageBottomSheet();

        Bundle bundle = new Bundle();

        bundle.putCharSequence("TITLE", title);
        bundle.putCharSequence("MESSAGE", message);
        bundle.putCharSequence("DISMISS", dismiss);
        bundle.putBoolean("SHOW_BUTTON", showButton);
        bundle.putBoolean("HAS_CALLBACK", hasCallback);

        messageBottomSheet.setArguments(bundle);

        return messageBottomSheet;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_message, container, false);

        ButterKnife.bind(this, view);

        this.extract();
        this.init();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (this.messageBottomSheetCallback == null && this.hasCallback) {
            this.dismiss();
        } else {
            BottomSheets.initialize(this, view);
        }
    }

    @Override
    public void onClick(View view) {
        if (view == this.btnDismiss) {
            this.dismiss();
        }
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);

        if (this.messageBottomSheetCallback != null) {
            this.messageBottomSheetCallback.dismiss();
        }
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);

        if (this.messageBottomSheetCallback != null) {
            this.messageBottomSheetCallback.cancel();
        }
    }

    public void callback(MessageBottomSheetCallback messageBottomSheetCallback) {
        this.messageBottomSheetCallback = messageBottomSheetCallback;
    }

    private void extract() {
        if (this.getArguments() != null) {
            CharSequence title = this.getArguments().getCharSequence("TITLE");
            CharSequence message = this.getArguments().getCharSequence("MESSAGE");
            CharSequence dismiss = this.getArguments().getCharSequence("DISMISS");
            boolean showButton = this.getArguments().getBoolean("SHOW_BUTTON", true);

            if (title != null) {
                this.txvTitle.setText(title);

                Views.visible(this.txvTitle);
            }

            if (message != null) {
                this.txvMessage.setText(message);
            }

            if (dismiss != null) {
                this.btnDismiss.setText(dismiss);
            }

            Views.gone(!showButton, this.btnDismiss);

            this.hasCallback = this.getArguments().getBoolean("HAS_CALLBACK");
        }
    }

    private void init() {
        this.btnDismiss.setOnClickListener(this);
    }

    public interface MessageBottomSheetCallback {
        void dismiss();

        void cancel();
    }
}