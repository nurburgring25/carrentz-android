package dev.burikk.carrentz.api.converter;

import androidx.annotation.NonNull;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import retrofit2.Converter;

/**
 * @author Muhammad Irfan
 * @since 05/05/2019 20.45
 */
public class CustomTimeConverter implements Converter<LocalTime, String> {
    @Override
    public String convert(@NonNull LocalTime value) {
        return value.format(DateTimeFormatter.ISO_TIME);
    }
}