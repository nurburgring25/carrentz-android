package dev.burikk.carrentz.helper;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.Closeable;

import dev.burikk.carrentz.enumeration.SharedPreferenceKey;

/**
 * @author Muhammad Irfan
 * @since 09/07/2018 16.14
 */
public class Preferences implements Closeable {
    private static final transient String TAG = Preferences.class.getSimpleName();

    private static Preferences INSTANCE;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private Preferences() {}

    public static Preferences getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Preferences();
        }

        return INSTANCE;
    }

    @SuppressLint("CommitPrefEdits")
    public void begin() {
        if (this.editor == null) {
            this.editor = this.sharedPreferences.edit();
        }
    }

    public <T> void put(SharedPreferenceKey sharedPreferenceKey, @NonNull T value) {
        if (this.editor == null) {
            Log.e(TAG, "You must call begin() first.");
            return;
        }

        if (value instanceof String) {
            this.editor.putString(sharedPreferenceKey.name(), (String) value);
        } else if (value instanceof Integer) {
            this.editor.putInt(sharedPreferenceKey.name(), (Integer) value);
        } else if (value instanceof Float) {
            this.editor.putFloat(sharedPreferenceKey.name(), (Float) value);
        } else if (value instanceof Long) {
            this.editor.putLong(sharedPreferenceKey.name(), (Long) value);
        } else if (value instanceof Boolean) {
            this.editor.putBoolean(sharedPreferenceKey.name(), (Boolean) value);
        } else {
            Log.e(TAG, "Unsupported value type.");
        }
    }

    public <T> void remove(SharedPreferenceKey sharedPreferenceKey) {
        if (this.editor == null) {
            Log.e(TAG, "You must call begin() first.");
            return;
        }

        this.editor.remove(sharedPreferenceKey.name());
    }

    @SuppressWarnings({"unchecked"})
    public static <T> T get(SharedPreferenceKey sharedPreferenceKey, Class<T> clazz, T defaultValue) {
        try (Preferences mSharedPreferenceHelper = getInstance()) {
            if (Objects.equals(String.class, clazz)) {
                return (T) mSharedPreferenceHelper.sharedPreferences.getString(sharedPreferenceKey.name(), defaultValue != null ? (String) defaultValue : null);
            } else if (Objects.equals(Integer.class, clazz)) {
                return (T) Integer.valueOf(mSharedPreferenceHelper.sharedPreferences.getInt(sharedPreferenceKey.name(), defaultValue != null ? (Integer) defaultValue : -9999));
            } else if (Objects.equals(Float.class, clazz)) {
                return (T) Float.valueOf(mSharedPreferenceHelper.sharedPreferences.getFloat(sharedPreferenceKey.name(), defaultValue != null ? (Float) defaultValue : -9999F));
            } else if (Objects.equals(Long.class, clazz)) {
                return (T) Long.valueOf(mSharedPreferenceHelper.sharedPreferences.getLong(sharedPreferenceKey.name(), defaultValue != null ? (Long) defaultValue : -9999L));
            } else if (Objects.equals(Boolean.class, clazz)) {
                return (T) Boolean.valueOf(mSharedPreferenceHelper.sharedPreferences.getBoolean(sharedPreferenceKey.name(), defaultValue != null && (Boolean) defaultValue));
            } else {
                return null;
            }
        }
    }

    public static <T> T get(SharedPreferenceKey sharedPreferenceKey, Class<T> clazz) {
        return get(sharedPreferenceKey, clazz, null);
    }

    public static <T> boolean contain(SharedPreferenceKey sharedPreferenceKey, Class<T> clazz) {
        return get(sharedPreferenceKey, clazz) != null;
    }

    public void commit() {
        if (this.editor == null) {
            Log.e(TAG, "You must call begin() first.");
            return;
        }

        this.editor.apply();

    }

    @Override
    public void close() {
        this.editor = null;
    }

    public void setSharedPreferences(SharedPreferences mSharedPreferences) {
        this.sharedPreferences = mSharedPreferences;
    }
}