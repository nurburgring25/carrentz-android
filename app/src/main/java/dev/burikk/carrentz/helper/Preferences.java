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

    public static <T> T get(String key, Class<T> clazz, T defaultValue) {
        try (Preferences sharedPreferenceManager = getInstance()) {
            return sharedPreferenceManager.getValue(key, clazz, defaultValue);
        }
    }

    public static <T> T get(String key, Class<T> clazz) {
        return get(key, clazz, null);
    }

    public static <T> T get(SharedPreferenceKey SharedPreferenceKey, Class<T> clazz, T defaultValue) {
        return get(SharedPreferenceKey.name(), clazz, defaultValue);
    }

    public static <T> T get(SharedPreferenceKey SharedPreferenceKey, Class<T> clazz) {
        return get(SharedPreferenceKey, clazz, null);
    }

    public static <T> boolean contain(String key) {
        try (Preferences sharedPreferenceManager = getInstance()) {
            return sharedPreferenceManager.sharedPreferences.contains(key);
        }
    }

    public static <T> boolean contain(SharedPreferenceKey SharedPreferenceKey) {
        return contain(SharedPreferenceKey.name());
    }

    @SuppressLint("CommitPrefEdits")
    public void begin() {
        if (this.editor == null) {
            this.editor = this.sharedPreferences.edit();
        }
    }

    public <T> void put(String key, @NonNull T value) {
        if (this.editor == null) {
            Log.e(TAG, "You must call begin() first.");
            return;
        }

        if (value instanceof String) {
            this.editor.putString(key, (String) value);
        } else if (value instanceof Integer) {
            this.editor.putInt(key, (Integer) value);
        } else if (value instanceof Float) {
            this.editor.putFloat(key, (Float) value);
        } else if (value instanceof Long) {
            this.editor.putLong(key, (Long) value);
        } else if (value instanceof Boolean) {
            this.editor.putBoolean(key, (Boolean) value);
        } else {
            Log.e(TAG, "Unsupported value type.");
        }
    }

    public <T> void put(SharedPreferenceKey SharedPreferenceKey, @NonNull T value) {
        put(SharedPreferenceKey.name(), value);
    }

    public void remove(String key) {
        if (this.editor == null) {
            Log.e(TAG, "You must call begin() first.");
            return;
        }

        this.editor.remove(key);
    }

    public void remove(SharedPreferenceKey SharedPreferenceKey) {
        remove(SharedPreferenceKey.name());
    }

    @SuppressWarnings("unchecked")
    public <T> T getValue(String key, Class<T> clazz, T defaultValue) {
        if (Objects.equals(String.class, clazz)) {
            return (T) this.sharedPreferences.getString(key, defaultValue != null ? (String) defaultValue : null);
        } else if (Objects.equals(Integer.class, clazz)) {
            return (T) Integer.valueOf(this.sharedPreferences.getInt(key, defaultValue != null ? (Integer) defaultValue : -9999));
        } else if (Objects.equals(Float.class, clazz)) {
            return (T) Float.valueOf(this.sharedPreferences.getFloat(key, defaultValue != null ? (Float) defaultValue : -9999F));
        } else if (Objects.equals(Long.class, clazz)) {
            return (T) Long.valueOf(this.sharedPreferences.getLong(key, defaultValue != null ? (Long) defaultValue : -9999L));
        } else if (Objects.equals(Boolean.class, clazz)) {
            return (T) Boolean.valueOf(this.sharedPreferences.getBoolean(key, defaultValue != null && (Boolean) defaultValue));
        } else {
            return null;
        }
    }

    public <T> T getValue(String key, Class<T> clazz) {
        return getValue(key, clazz, null);
    }

    public <T> T getValue(SharedPreferenceKey SharedPreferenceKey, Class<T> clazz, T defaultValue) {
        return getValue(SharedPreferenceKey.name(), clazz, defaultValue);
    }

    public <T> T getValue(SharedPreferenceKey SharedPreferenceKey, Class<T> clazz) {
        return getValue(SharedPreferenceKey, clazz, null);
    }

    public <T> boolean containValue(String key) {
        return this.sharedPreferences.contains(key);
    }

    public <T> boolean containValue(SharedPreferenceKey SharedPreferenceKey) {
        return containValue(SharedPreferenceKey.name());
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