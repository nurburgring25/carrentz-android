package dev.burikk.carrentz.api.converter;

import androidx.annotation.NonNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import retrofit2.Converter;

/**
 * @author Muhammad Irfan
 * @since 05/05/2019 20.45
 */
public class CustomDateConverter implements Converter<LocalDate, String> {
    @Override
    public String convert(@NonNull LocalDate value) {
        return value.format(DateTimeFormatter.ISO_DATE);
    }
}