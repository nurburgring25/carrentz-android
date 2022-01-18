package dev.burikk.carrentz.helper;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Muhammad Irfan
 * @since 28/08/2019 11.38
 */
public class Validators {
    public static boolean custom(boolean condition, Activity activity, String message) {
        if (condition) {
            Dialogs.message(activity, message);

            return false;
        }

        return true;
    }

    public static boolean custom(boolean condition, TextInputEditText textInputEditText, String message) {
        // Mencari object text input layout
        TextInputLayout textInputLayout = null;

        if (textInputEditText.getParent() instanceof TextInputLayout) {
            textInputLayout = (TextInputLayout) textInputEditText.getParent();
        } else {
            if (textInputEditText.getParent().getParent() instanceof TextInputLayout) {
                textInputLayout = (TextInputLayout) textInputEditText.getParent().getParent();
            }
        }

        // Reset kondisi
        if (textInputLayout != null) {
            textInputLayout.setErrorEnabled(false);
            textInputLayout.setError(null);
        } else {
            textInputEditText.setError(null);
        }

        // Validasi
        if (condition) {
            if (textInputLayout != null) {
                textInputLayout.setErrorEnabled(true);
                textInputLayout.setError(message);
            } else {
                textInputEditText.setError(message);
            }

            // Ganti fokus
            textInputEditText.requestFocus();

            return false;
        }

        return true;
    }

    public static boolean mandatory(Activity activity, View view, String fieldName) {
        return mandatory(activity, view, fieldName, true);
    }

    public static boolean mandatory(Activity activity, View view, String fieldName, boolean condition) {
        if (condition) {
            String error = fieldName + " harus diisi.";

            if (view instanceof TextInputEditText) {
                TextInputEditText textInputEditText = (TextInputEditText) view;

                // Mencari object text input layout
                TextInputLayout textInputLayout = null;

                if (textInputEditText.getParent() instanceof TextInputLayout) {
                    textInputLayout = (TextInputLayout) textInputEditText.getParent();
                } else {
                    if (textInputEditText.getParent().getParent() instanceof TextInputLayout) {
                        textInputLayout = (TextInputLayout) textInputEditText.getParent().getParent();
                    }
                }

                // Reset kondisi
                if (textInputLayout != null) {
                    textInputLayout.setErrorEnabled(false);
                    textInputLayout.setError(null);
                } else {
                    textInputEditText.setError(null);
                }

                // Validasi
                if (StringUtils.isBlank(textInputEditText.getText())) {
                    if (textInputLayout != null) {
                        textInputLayout.setErrorEnabled(true);
                        textInputLayout.setError(error);
                    } else {
                        textInputEditText.setError(error);
                    }

                    // Ganti fokus
                    textInputEditText.requestFocus();

                    return false;
                }
            } else if (view instanceof Spinner) {
                Spinner spinner = (Spinner) view;

                if (spinner.getSelectedItem() == null) {
                    // Munculkan pesan error
                    Dialogs.message(activity, error);

                    // Ganti fokus
                    spinner.requestFocus();

                    return false;
                }
            } else if (view instanceof AutoCompleteTextView) {
                AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) view;

                // Mencari object text input layout
                TextInputLayout textInputLayout = null;

                if (autoCompleteTextView.getParent() instanceof TextInputLayout) {
                    textInputLayout = (TextInputLayout) autoCompleteTextView.getParent();
                } else {
                    if (autoCompleteTextView.getParent().getParent() instanceof TextInputLayout) {
                        textInputLayout = (TextInputLayout) autoCompleteTextView.getParent().getParent();
                    }
                }

                // Reset kondisi
                if (textInputLayout != null) {
                    textInputLayout.setErrorEnabled(false);
                    textInputLayout.setError(null);
                } else {
                    autoCompleteTextView.setError(null);
                }

                // Validasi
                if (StringUtils.isBlank(autoCompleteTextView.getText())) {
                    if (textInputLayout != null) {
                        textInputLayout.setErrorEnabled(true);
                        textInputLayout.setError(error);
                    } else {
                        autoCompleteTextView.setError(error);
                    }

                    // Ganti fokus
                    autoCompleteTextView.requestFocus();

                    return false;
                }
            }
        }

        return true;
    }

    public static boolean email(TextInputEditText textInputEditText) {
        String error = "Format email tidak sesuai.";

        // Mencari object text input layout
        TextInputLayout textInputLayout = null;

        if (textInputEditText.getParent() instanceof TextInputLayout) {
            textInputLayout = (TextInputLayout) textInputEditText.getParent();
        } else {
            if (textInputEditText.getParent().getParent() instanceof TextInputLayout) {
                textInputLayout = (TextInputLayout) textInputEditText.getParent().getParent();
            }
        }

        // Reset kondisi
        if (textInputLayout != null) {
            textInputLayout.setErrorEnabled(false);
            textInputLayout.setError(null);
        } else {
            textInputEditText.setError(null);
        }

        // Validasi
        if (!emailValidation(Strings.value(textInputEditText.getText()))) {
            if (textInputLayout != null) {
                textInputLayout.setErrorEnabled(true);
                textInputLayout.setError(error);
            } else {
                textInputEditText.setError(error);
            }

            // Ganti fokus
            textInputEditText.requestFocus();

            return false;
        }

        return true;
    }

    public static boolean minLength(TextInputEditText textInputEditText, int minLength) {
        String error = "Panjang karakter minimal " + minLength;

        // Mencari object text input layout
        TextInputLayout textInputLayout = null;

        if (textInputEditText.getParent() instanceof TextInputLayout) {
            textInputLayout = (TextInputLayout) textInputEditText.getParent();
        } else {
            if (textInputEditText.getParent().getParent() instanceof TextInputLayout) {
                textInputLayout = (TextInputLayout) textInputEditText.getParent().getParent();
            }
        }

        // Reset kondisi
        if (textInputLayout != null) {
            textInputLayout.setErrorEnabled(false);
            textInputLayout.setError(null);
        } else {
            textInputEditText.setError(null);
        }

        // Validasi
        if (StringUtils.length(textInputEditText.getText()) < minLength) {
            if (textInputLayout != null) {
                textInputLayout.setErrorEnabled(true);
                textInputLayout.setError(error);
            } else {
                textInputEditText.setError(error);
            }

            // Ganti fokus
            textInputEditText.requestFocus();

            return false;
        }

        return true;
    }

    public static boolean ipAddress(Activity activity, TextInputEditText textInputEditText, String fieldName, boolean condition) {
        if (condition) {
            String error = "Format nilai pada kolom " + fieldName + " tidak sesuai";

            // Mencari object text input layout
            TextInputLayout textInputLayout = null;

            if (textInputEditText.getParent() instanceof TextInputLayout) {
                textInputLayout = (TextInputLayout) textInputEditText.getParent();
            } else {
                if (textInputEditText.getParent().getParent() instanceof TextInputLayout) {
                    textInputLayout = (TextInputLayout) textInputEditText.getParent().getParent();
                }
            }

            // Reset kondisi
            if (textInputLayout != null) {
                textInputLayout.setErrorEnabled(false);
                textInputLayout.setError(null);
            } else {
                textInputEditText.setError(null);
            }

            // Validasi
            if (!ipValidation(Strings.value(textInputEditText.getText()))) {
                if (textInputLayout != null) {
                    textInputLayout.setErrorEnabled(true);
                    textInputLayout.setError(error);
                } else {
                    textInputEditText.setError(error);
                }

                // Ganti fokus
                textInputEditText.requestFocus();

                return false;
            }
        }

        return true;
    }

    /**
     * @param mParentView  Parent View from mContentView.
     *                     if (mParentView == !TextInputEditText/EditText/AppCompatEditText/of one kind)
     *                     that's @param mContentView set null.
     *                     else @param mContentView @NonNull and content of @param mParentView
     *                     set parent from TextInputEditText/EditText/AppCompatEditText/of one kind.
     * @param mContentView Content from @param mContentView is TextInputEditText/EditText/AppCompatEditText/of one kind.
     * @param mMessage     Content from message error.
     * @param mType        Content from type is :
     *                     mType = 2; => password validation
     *                     mType = 1; => email validation
     *                     mType = 0; => general validation
     * @return Return from method that valid or not.
     */
    public static boolean generalValidation(@NonNull View mParentView, View mContentView,
                                            @NonNull String mMessage, @NonNull Integer mType) {
        if (mParentView instanceof Spinner) {
            Spinner mSpinner = (Spinner) mParentView;

            if (mSpinner.getSelectedItem() == null) {
                mSpinner.requestFocus();

                return false;
            }
        } else if (mParentView instanceof TextInputLayout) {
            TextInputLayout mTextInputLayout = (TextInputLayout) mParentView;

            if (mContentView != null) {
                if (mContentView instanceof TextInputEditText) {
                    TextInputEditText mTextInputEditText = (TextInputEditText) mContentView;

                    if (TextUtils.isEmpty(mTextInputEditText.getText().toString())) {
                        mTextInputEditText.requestFocus();
                        mTextInputEditText.setError(null);
                        mTextInputLayout.setErrorEnabled(true);
                        mTextInputLayout.setError(mMessage);

                        return false;
                    } else {
                        if (mType == 1) {
                            if (!emailValidation(mTextInputEditText.getText().toString())) {
                                mTextInputEditText.setError(null);
                                mTextInputLayout.setErrorEnabled(true);
                                mTextInputLayout.setError(mMessage);

                                return false;
                            }
                        } else if (mType == 2) {
                            if (mTextInputEditText.getText().toString().length() < 6) {
                                mTextInputEditText.setError(null);
                                mTextInputLayout.setErrorEnabled(true);
                                mTextInputLayout.setError(mMessage);

                                return false;
                            }
                        } else if (mType == 3) {
                            if (!ipValidation(mTextInputEditText.getText().toString())) {
                                mTextInputEditText.setError(null);
                                mTextInputLayout.setErrorEnabled(true);
                                mTextInputLayout.setError(mMessage);

                                return false;
                            }
                        }
                    }
                }

            } else {
                return false;
            }
        } else if (mParentView instanceof EditText) {
            EditText mEditText = (EditText) mParentView;

            if (TextUtils.isEmpty(mEditText.getText().toString())) {
                mEditText.setError(mMessage);
                mEditText.requestFocus();

                if (mContentView != null) {
                    if (mContentView instanceof TextInputLayout) {
                        TextInputLayout mTextInputLayout = (TextInputLayout) mContentView;
                        mEditText.setError(null);
                        mTextInputLayout.setErrorEnabled(true);
                        mTextInputLayout.setError(mMessage);
                    }
                }
                return false;
            }
        }

        return true;
    }

    private static boolean emailValidation(String mEmail) {

        if (StringUtils.isNotBlank(mEmail)) {
            String expression = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

            Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(mEmail);
            return matcher.matches();
        }

        return false;
    }

    private static boolean ipValidation(String mIpAddress) {
        if (StringUtils.isNotBlank(mIpAddress)) {
            String valid = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

            Pattern pattern = Pattern.compile(valid, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(mIpAddress);
            return matcher.matches();
        }

        return false;
    }

    /**
     * @param mTextInputLayout1   Content from mTextInputLayout1 is TextInputLayout1
     * @param mTextInputEditText1 Content from @param mTextInputEditText1 is TextInputEditText1.
     * @param mTextInputEditText2 Content from @param mTextInputEditText2 is TextInputEditText2.
     * @param mMessage            Content from message error.
     * @return Return from method that valid or not.
     */
    public static boolean passwordValidation(@NonNull TextInputLayout mTextInputLayout1,
                                             @NonNull TextInputEditText mTextInputEditText1, @NonNull TextInputEditText mTextInputEditText2,
                                             String mMessage) {
        if (!mTextInputEditText1.getText().toString().equals(mTextInputEditText2.getText().toString())) {
            mTextInputEditText1.requestFocus();
            mTextInputEditText1.setError(null);
            mTextInputLayout1.setErrorEnabled(true);
            mTextInputLayout1.setError(mMessage);
            return false;
        }

        return true;
    }

    public static void textChange(final Context mContext, final View mView1, final View mView2) {
        if (mView1 instanceof TextInputLayout) {
            final TextInputLayout mTextInputLayout = (TextInputLayout) mView1;
            if (mView2 instanceof TextInputEditText) {
                TextInputEditText mTextInputEditText = (TextInputEditText) mView2;

                mTextInputEditText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (s.length() > 0) {
                            mTextInputLayout.setError(null);
                            mTextInputLayout.setErrorEnabled(false);
                            mTextInputLayout.setPasswordVisibilityToggleEnabled(true);
                            mTextInputLayout.setPasswordVisibilityToggleTintList(ContextCompat.getColorStateList(mContext, android.R.color.black));
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            } else if (mView2 instanceof EditText) {
                EditText mEditText = (EditText) mView2;
                mEditText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (s.length() > 0) {
                            mTextInputLayout.setError(null);
                            mTextInputLayout.setErrorEnabled(false);
                        } else {
                            mTextInputLayout.setErrorEnabled(true);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
            }
        }
    }
}