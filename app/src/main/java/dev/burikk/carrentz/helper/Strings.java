package dev.burikk.carrentz.helper;

import android.text.Editable;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ichsanudin Chairin
 * @since 01/02/2018 16.14
 */
@SuppressWarnings("unused")
public class Strings {
    public static String blankIfNull(String string) {
        return StringUtils.isNotBlank(string) ? string : "";
    }

    public static String value(Editable editable) {
        if (editable != null) {
            return editable.toString();
        }

        return null;
    }

    public static String textAvatar(String text) {
        String[] splitted = StringUtils.splitByWholeSeparator(text, null);

        List<String> splittedWithLetter = new ArrayList<>();

        StringBuilder result = new StringBuilder();

        for (String split : splitted) {
            for (int i = 0; i < split.length(); i++) {
                if (StringUtils.substring(split, i, i + 1).matches(".*[a-zA-Z].*")) {
                    splittedWithLetter.add(split);
                    break;
                }
            }
        }

        for (String split : splittedWithLetter) {
            for (int i = 0; i < split.length(); i++) {
                if (StringUtils.substring(split, i, i + 1).matches(".*[a-zA-Z].*")) {
                    if (StringUtils.length(result) < 3) {
                        result.append(split.charAt(i));
                    }
                    break;
                }
            }
        }

        return result.toString();
    }

    public static String[] splitToNChar(String text, int size) {
        List<String> parts = new ArrayList<>();

        int length = text.length();

        for (int i = 0; i < length; i += size) {
            parts.add(text.substring(i, Math.min(length, i + size)));
        }

        return parts.toArray(new String[0]);
    }

    public static String print(double data) {
        if (data % 1 == 0) {
            return String.valueOf((int) data);
        } else {
            return String.valueOf(data);
        }
    }

    public static String zeroToEmpty(long value) {
        if (value != 0) {
            return String.valueOf(value);
        } else {
            return null;
        }
    }

    public static String fullWidthText(String text, int length) {
        StringBuilder textReturn = new StringBuilder();

        for (int i = 0;i < length; i++) {
            textReturn.append(text);
        }

        return textReturn.toString();
    }
}