package dev.burikk.carrentz.helper;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * @author Muhammad Irfan
 * @since 02/03/2018 9:56
 */
@SuppressWarnings("unused")
public class Formats {
    private static DecimalFormat DECIMAL_FORMAT;

    public static DecimalFormat getDecimalFormat() {
        if (DECIMAL_FORMAT == null) {
            DecimalFormatSymbols decimalFormatSymbols = DecimalFormatSymbols.getInstance();

            decimalFormatSymbols.setGroupingSeparator('.');
            decimalFormatSymbols.setDecimalSeparator(',');

            DECIMAL_FORMAT = new DecimalFormat("###,##0.00", decimalFormatSymbols);
            DECIMAL_FORMAT.setParseBigDecimal(true);
            DECIMAL_FORMAT.setMinimumFractionDigits(0);
        }

        return DECIMAL_FORMAT;
    }

    public static String getCurrencyFormat(Long value) {
        return getCurrencyFormat(value, true);
    }

    public static String getCurrencyFormat(Long value, boolean withSymbol) {
        value = (value == null) ? 0L : value;

        if (value < 0L) {
            return (withSymbol ? ("- Rp" + " ") : "- ") + Formats.getDecimalFormat().format(Math.abs(value));
        } else {
            return (withSymbol ? ("Rp ") : "") + Formats.getDecimalFormat().format(value);
        }
    }
}