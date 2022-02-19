package dev.burikk.carrentz.api.deserializer;

import android.util.Log;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author Muhammad Irfan
 * @since 15/02/2019 17.04
 */
public class CustomDateDeserializer implements JsonDeserializer<LocalDate> {
    private static final transient String TAG = CustomDateDeserializer.class.getSimpleName();

    @Override
    public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (StringUtils.isNotBlank(json.getAsString())) {
            try {
                return LocalDate.parse(json.getAsString(), DateTimeFormatter.ISO_DATE);
            } catch (Exception ex) {
                Log.wtf(TAG, ex);
            }
        }

        return null;
    }
}