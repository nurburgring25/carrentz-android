package dev.burikk.carrentz.api.deserializer;

import android.util.Log;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Muhammad Irfan
 * @since 15/02/2019 17.04
 */
public class CustomDateTimeDeserializer implements JsonDeserializer<LocalDateTime> {
    private static final transient String TAG = CustomDateTimeDeserializer.class.getSimpleName();

    @Override
    public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (StringUtils.isNotBlank(json.getAsString())) {
            try {
                return LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ISO_DATE_TIME);
            } catch (Exception ex) {
                Log.wtf(TAG, ex);
            }
        }

        return null;
    }
}