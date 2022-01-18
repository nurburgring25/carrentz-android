package dev.burikk.carrentz.helper;

import java.util.Arrays;

/**
 * @author Muhammad Irfan
 * @since 11/05/2019 15.09
 */
@SuppressWarnings("unused")
public class Objects {
    public static boolean equals(Object a, Object b) {
        return (a == b) || (a != null && a.equals(b));
    }

    public static <T> T requireNonNull(T obj) {
        if (obj == null) {
            throw new NullPointerException();
        }

        return obj;
    }

    public static byte[][] divideArray(byte[] source, int chunksize) {
        byte[][] ret = new byte[(int) Math.ceil(source.length / (double) chunksize)][chunksize];

        int start = 0;

        for (int i = 0; i < ret.length; i++) {
            ret[i] = Arrays.copyOfRange(source, start, start + chunksize);

            start += chunksize;
        }

        return ret;
    }
}