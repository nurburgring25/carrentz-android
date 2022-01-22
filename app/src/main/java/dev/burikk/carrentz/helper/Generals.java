package dev.burikk.carrentz.helper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.InputFilter;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.fragment.app.Fragment;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;

import dev.burikk.carrentz.common.DialCode;

/**
 * @author Ichsanudin Chairin
 * @since 23/02/2018 15.09
 */
public class Generals {
    private static final transient String TAG = Generals.class.getName();

    public static boolean isTimeAutomatic(Context c) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return Settings.Global.getInt(c.getContentResolver(), Settings.Global.AUTO_TIME, 0) == 1;
        } else {
            return Settings.System.getInt(c.getContentResolver(), Settings.Global.AUTO_TIME, 0) == 1;
        }
    }

    public static int screenWidth(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        if (windowManager != null) {
            DisplayMetrics displayMetrics = new DisplayMetrics();

            windowManager.getDefaultDisplay().getMetrics(displayMetrics);

            return displayMetrics.widthPixels;
        }

        return 0;
    }

    public static int screenHeight(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        if (windowManager != null) {
            DisplayMetrics displayMetrics = new DisplayMetrics();

            windowManager.getDefaultDisplay().getMetrics(displayMetrics);

            return displayMetrics.heightPixels;
        }

        return 0;
    }

    public static int screenRotation(Activity activity) {
        return ((WindowManager) activity.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation();
    }

    public static <T> T checkData(Object data, Class class1) {
        if (data != null) {
            if (class1.equals(BigDecimal.class)) {
                return (T) data;
            } else if (class1.equals(String.class)) {
                return (T) data;
            } else if (class1.equals(Integer.class)) {
                return (T) data;
            } else if (class1.equals(Long.class)) {
                return (T) data;
            }
        } else {
            if (class1.equals(Integer.class)) {
                return (T) ((Integer) (0));
            } else if (class1.equals(Long.class)) {
                return (T) ((Long) (0L));
            }
        }

        return null;
    }

    public static Long roundingAmount(long amount) {
        long mRounded = 0L;

        if (String.valueOf(amount).length() >= 2) {
            String mRoundTemp = String.valueOf(amount).substring(0, (String.valueOf(amount).length() - 2));
            String mTest = String.valueOf(amount).substring((String.valueOf(amount).length() - 2));
            String mTens = String.valueOf(mTest.charAt(0));
            String mUnit = String.valueOf(mTest.charAt(1));

            if (Integer.parseInt(mUnit) <= 4) {
                if (Integer.parseInt(mTens) <= 4) {
                    mRounded = Long.valueOf(mRoundTemp + "00");
                } else {
                    mRounded = Long.valueOf(mRoundTemp + "00") + 100;
                }
            } else {
                mTens = String.valueOf(Integer.parseInt(mTens) + 1);

                if (Integer.parseInt(mTens) <= 4) {
                    mRounded = Long.valueOf(mRoundTemp + "00");
                } else {
                    mRounded = Long.valueOf(mRoundTemp + "00") + 100;
                }
            }
        }

        return mRounded - amount;
    }

    public static Long getRounded(Long mTaxAmount) {
        Long mRounded = 0L;

        if (String.valueOf(mTaxAmount).length() >= 2) {
            String mRoundTemp = String.valueOf(mTaxAmount).substring(0, (String.valueOf(mTaxAmount).length() - 2));
            String mTest = String.valueOf(mTaxAmount).substring((String.valueOf(mTaxAmount).length() - 2), String.valueOf(mTaxAmount).length());
            String mTens = String.valueOf(mTest.charAt(0));
            String mUnit = String.valueOf(mTest.charAt(1));

            if (Integer.parseInt(mUnit) <= 4) {
                if (Integer.parseInt(mTens) <= 4) {
                    mRounded = Long.valueOf(mRoundTemp + "00");
                } else {
                    mRounded = Long.valueOf(mRoundTemp + "00") + 100;
                }
            } else {
                mTens = String.valueOf(Integer.parseInt(mTens) + 1);

                if (Integer.parseInt(mTens) <= 4) {
                    mRounded = Long.valueOf(mRoundTemp + "00");
                } else {
                    mRounded = Long.valueOf(mRoundTemp + "00") + 100;
                }
            }
        } else {

        }

        return mRounded;
    }

    public static Bitmap base64ToBitmap(String base64) {
        byte[] imageAsByte = Base64.decode(base64.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsByte, 0, imageAsByte.length);
    }

    public static String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public static void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory()) {
            if (fileOrDirectory.listFiles() != null) {
                for (File child : fileOrDirectory.listFiles()) {
                    deleteRecursive(child);
                }
            }
        }

        fileOrDirectory.delete();
    }

    public static void saveImage(Bitmap bitmap, String mItemID) {
        File myDir = new File(Environment.getExternalStorageDirectory().toString() + "/.vireo");
        if (!myDir.exists()) {
            myDir.mkdirs();
        }

        String filename = "Image-" + mItemID + ".png";
        File file = new File(myDir, filename);
        if (file.exists()) {
            file.delete();
        }

        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp      A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(float dp, Context context) {
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px      A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(float px, Context context) {
        return px / ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static void moveForResult(Fragment fragment, Class<?> activityClass, Bundle bundle, int requestCode) {
        Intent intent = new Intent(fragment.getActivity(), activityClass);

        if (bundle != null) {
            intent.putExtras(bundle);
        }

        fragment.startActivityForResult(intent, requestCode);
    }

    public static void moveForResult(Activity activity, Class<?> activityClass, Bundle bundle, int requestCode) {
        Intent intent = new Intent(activity, activityClass);

        if (bundle != null) {
            intent.putExtras(bundle);
        }

        activity.startActivityForResult(intent, requestCode);
    }

    public static void move(Context context, Class<?> activityClass, boolean forget) {
        move(context, activityClass, null, forget);
    }

    public static void move(Context context, Class<?> activityClass, Bundle bundle, boolean forget) {
        Intent intent = new Intent(context, activityClass);

        if (forget) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        intent.putExtra("BUNDLE", bundle);

        context.startActivity(intent);
    }

    public static void hideKeyboard(Context context, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);

        inputMethodManager.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
    }

    public static void showKeyboard(Context context, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(view.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
    }

    public static boolean isTablet(Activity activity) {
        return convertPixelsToDp(screenWidth(activity), activity) >= 800;
    }

    public static InputFilter EMOJI_FILTER  = (source, start, end, dest, dstart, dend) -> {
        for (int index = start; index < end; index++) {
            int type = Character.getType(source.charAt(index));

            if (type == Character.SURROGATE || type == Character.OTHER_SYMBOL) {
                return "";
            }
        }

        return null;
    };

    public static Bitmap textAsBitmap(String mText, float mSize, int mColor) {
        Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mPaint.setTextSize(mSize);
        mPaint.setColor(mColor);
        mPaint.setTextAlign(Paint.Align.LEFT);

        float mBaseLine = -mPaint.ascent();

        int mWidth = (int) (mPaint.measureText(mText) + 0.0f);
        int mHeight = (int) (mBaseLine + mPaint.descent() + 0.0f);

        Bitmap mBitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);

        Canvas mCanvas = new Canvas(mBitmap);

        mCanvas.drawText(mText, 0, mBaseLine, mPaint);

        return mBitmap;
    }

    public static boolean isForcePortrait(Context context) {
        float width = 0;

        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        if (windowManager != null) {
            switch (windowManager.getDefaultDisplay().getRotation()) {
                case Surface.ROTATION_0:
                    width = Generals.convertPixelsToDp(Generals.screenWidth(context), context);
                    break;
                case Surface.ROTATION_90:
                    width = Generals.convertPixelsToDp(Generals.screenHeight(context), context);
                    break;
                case Surface.ROTATION_180:
                    width = Generals.convertPixelsToDp(Generals.screenHeight(context), context);
                    break;
                case Surface.ROTATION_270:
                    width = Generals.convertPixelsToDp(Generals.screenHeight(context), context);
                    break;
                default:
                    width = Generals.convertPixelsToDp(Generals.screenWidth(context), context);
                    break;
            }
        }

        return width < 500;
    }

    @SuppressLint("HardwareIds")
    public static String getFingerprintId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static Phonenumber.PhoneNumber getPhoneNumber(String rawPhoneNumber) {
        try {
            return PhoneNumberUtil.getInstance().parse(rawPhoneNumber, "ID");
        } catch (Exception ex) {
            Log.wtf(TAG, ex);
        }

        return null;
    }

    public static String getDialCode(Phonenumber.PhoneNumber phoneNumber) {
        if (phoneNumber != null) {
            DialCode dialCode = null;

            for (DialCode dialCodeCheck : DialCode.DIAL_CODES) {
                if (StringUtils.equals(dialCodeCheck.getDialCode(), ("+" + phoneNumber.getCountryCode()))) {
                    dialCode = dialCodeCheck;
                    break;
                }
            }

            if (dialCode != null) {
                return dialCode.getDialCode();
            }
        }

        return null;
    }
}