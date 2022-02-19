package dev.burikk.carrentz.api.converter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import dev.burikk.carrentz.helper.Objects;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * @author Muhammad Irfan
 * @since 05/05/2019 20.52
 */
public class CustomConverterFactory extends Converter.Factory {
    @Nullable
    @Override
    public Converter<?, String> stringConverter(@NonNull Type type, @NonNull Annotation[] annotations, @NonNull Retrofit retrofit) {
        if (Objects.equals(LocalDate.class, type)) {
            return new CustomDateConverter();
        } else if (Objects.equals(LocalDateTime.class, type)) {
            return new CustomDateTimeConverter();
        } else if (Objects.equals(LocalTime.class, type)) {
            return new CustomTimeConverter();
        } else {
            return super.stringConverter(type, annotations, retrofit);
        }
    }
}