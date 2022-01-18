package dev.burikk.carrentz.enumeration;

/**
 * @author Muhammad Irfan
 * @since 10/04/2020 17.43
 */
public enum DateFormat {
    ISO_DATE_TIME("yyyy-MM-dd'T'HH:mm:ss.SSS"),
    ISO_DATE("yyyy-MM-dd"),
    ISO_TIME("HH:mm:ss.SSS"),
    DISPLAY_DATE_TIME("dd/MM/yyyy HH:mm"),
    DISPLAY_DATE("dd/MM/yyyy"),
    DISPLAY_TIME("HH:mm");

    private String format;

    DateFormat(String format) {
        this.format = format;
    }

    public String getFormat() {
        return this.format;
    }
}