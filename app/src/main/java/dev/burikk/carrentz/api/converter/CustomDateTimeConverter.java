package dev.burikk.carrentz.api.converter;

import androidx.annotation.NonNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import retrofit2.Converter;

/**
 * @author Muhammad Irfan
 * @since 05/05/2019 20.45
 */
public class CustomDateTimeConverter implements Converter<LocalDateTime, String> {
    @Override
    public String convert(@NonNull LocalDateTime value) {
        return value.format(DateTimeFormatter.ISO_DATE_TIME);
    }
}