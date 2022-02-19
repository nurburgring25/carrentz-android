package dev.burikk.carrentz.api.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Muhammad Irfan
 * @since 15/02/2019 17.11
 */
public class CustomTimeSerializer implements JsonSerializer<LocalTime> {
    @Override
    public JsonElement serialize(LocalTime src, Type typeOfSrc, JsonSerializationContext context) {
        if (src != null) {
            return context.serialize(src.format(DateTimeFormatter.ISO_TIME));
        } else {
            return null;
        }
    }
}