package dev.burikk.carrentz.helper;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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
}