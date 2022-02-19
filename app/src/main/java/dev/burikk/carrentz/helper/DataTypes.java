package dev.burikk.carrentz.helper;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Locale;

import dev.burikk.carrentz.enumeration.DateFormat;

/**
 * @author Muhammad Irfan
 * @since 28/08/2019 12.14
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class DataTypes {
    public static Date getDate(Long epochMillis) {
        return new Date(epochMillis);
    }

    public static Long getEpochMilli(Date date) {
        if (date != null) {
            return date.getTime();
        } else {
            return null;
        }
    }

    public static String convertDateTimeString(Date date, String type) {
        if (getEpochMilli(date) != null) {
            if (type == null) {
                type = "dd-MM-yyyy";
            }

            return new SimpleDateFormat(type, new Locale("in")).format(getEpochMilli(date));
        } else {
            return null;
        }
    }

    public static String convertDateTimeString(Date date) {
        return convertDateTimeString(date, null);
    }

    public static String convertDateTimeString(Long epochMillis, String type) {
        if (epochMillis != null) {
            if (type == null) {
                type = "dd-MM-yyyy";
            }

            return new SimpleDateFormat(type, new Locale("in")).format(epochMillis);
        } else {
            return null;
        }
    }

    public static String convertDateTimeString(Long epochMillis) {
        return convertDateTimeString(epochMillis, null);
    }

    public static String dateTime(Date date) {
        return new SimpleDateFormat(DateFormat.DISPLAY_DATE_TIME.getFormat(), new Locale("in")).format(date);
    }

    public static String date(Date date) {
        return new SimpleDateFormat(DateFormat.DISPLAY_DATE.getFormat(), new Locale("in")).format(date);
    }

    public static String time(Date date) {
        return new SimpleDateFormat(DateFormat.DISPLAY_TIME.getFormat(), new Locale("in")).format(date);
    }

    public static long parseLong(BigDecimal bigDecimal) {
        if (bigDecimal != null) {
            return bigDecimal.longValue();
        }

        return 0;
    }

    public static int parseInteger(BigDecimal bigDecimal) {
        if (bigDecimal != null) {
            return bigDecimal.intValue();
        }

        return 0;
    }

    public static double parseDouble(BigDecimal bigDecimal) {
        if (bigDecimal != null) {
            return bigDecimal.doubleValue();
        }

        return 0;
    }

    public static BigDecimal parseBigDecimal(Long value) {
        if (value != null) {
            return new BigDecimal(value);
        }

        return BigDecimal.ZERO;
    }

    public static BigDecimal parseBigDecimal(Integer value) {
        if (value != null) {
            return new BigDecimal(value);
        }

        return BigDecimal.ZERO;
    }

    public static BigDecimal parseBigDecimal(Double value) {
        if (value != null) {
            return new BigDecimal(value);
        }

        return BigDecimal.ZERO;
    }

    public static LocalDateTime toLocalDateTime(Long millis) {
        if (millis != null) {
            return Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDateTime();
        }

        return null;
    }

    public static LocalDate toLocalDate(Long millis) {
        if (millis != null) {
            return Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate();
        }

        return null;
    }

    public static LocalTime toLocalTime(Long millis) {
        if (millis != null) {
            return Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalTime();
        }

        return null;
    }

    public static LocalDateTime toLocalDateTime(Date date) {
        if (date != null) {
            return toLocalDateTime(date.getTime());
        }

        return null;
    }

    public static LocalDate toLocalDate(Date date) {
        if (date != null) {
            return toLocalDate(date.getTime());
        }

        return null;
    }

    public static LocalTime toLocalTime(Date date) {
        if (date != null) {
            return toLocalTime(date.getTime());
        }

        return null;
    }

    public static Long toEpochMillis(LocalDateTime localDateTime) {
        if (localDateTime != null) {
            return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        }

        return null;
    }

    public static Long toEpochMillis(LocalDate localDate) {
        if (localDate != null) {
            return localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        }

        return null;
    }

    public static Date toDate(LocalDateTime localDateTime) {
        Long millis = toEpochMillis(localDateTime);

        if (millis != null) {
            return new Date(millis);
        }

        return null;
    }

    public static Date toDate(LocalDate localDate) {
        Long millis = toEpochMillis(localDate);

        if (millis != null) {
            return new Date(millis);
        }

        return null;
    }

    public static Date toDate(LocalTime localTime) {
        Long millis = toEpochMillis(LocalDateTime.of(LocalDate.now(), localTime));

        if (millis != null) {
            return new Date(millis);
        }

        return null;
    }

    public static String translate(LocalDate localDate) {
        if (localDate != null) {
            Period period = Period.between(localDate, LocalDate.now());

            long difference = period.get(ChronoUnit.DAYS);

            if (difference == 0) {
                return "Hari Ini";
            } else if (difference == 1) {
                return "Kemarin";
            } else {
                return localDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            }
        }

        return null;
    }
}